package com.example.asynctxdemo;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.util.List;

/**
 * @Async 与 @Transactional 叠加演示：事务会生效，但生效在异步线程里，是另一个新事务。
 *
 * <p>直接运行 {@link #main} 即可，不需要启动 Web 容器，用的是 H2 内存库。
 *
 * <p>两个场景：
 * <ol>
 *   <li>调用方事务提交、异步方法抛异常：只有异步那段回滚，调用方的数据还在</li>
 *   <li>调用方事务回滚、异步方法正常提交：异步的数据已经在另一个事务里提交，回滚不掉</li>
 * </ol>
 *
 * <p>关键证据是日志里的「线程名 + txActive + txName」三件套：
 * 调用方是 main 线程，异步是 async-tx-1 线程，两边 txName 也不一样。
 */
@Configuration
@EnableAsync
@EnableTransactionManagement
public class AsyncTxDemo {

    public static void main(String[] args) throws Exception {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(AsyncTxDemo.class)) {

            OrderJdbc orderJdbc = context.getBean(OrderJdbc.class);
            orderJdbc.initSchema();
            CallerService callerService = context.getBean(CallerService.class);

            System.out.println("\n=== 场景 1：调用方提交，异步方法抛异常（只回滚异步自己）===");
            orderJdbc.truncate();
            callerService.commitThenAsyncFail("A");
            Thread.sleep(1000);                       // 等异步线程跑完
            System.out.println("[main] 最终数据: " + orderJdbc.findAll());

            System.out.println("\n=== 场景 2：调用方回滚，异步方法正常提交（异步不受影响）===");
            orderJdbc.truncate();
            try {
                callerService.rollbackThenAsyncOk("B");
            }
            catch (IllegalStateException ex) {
                System.out.println("[main] 调用方异常: " + ex.getMessage());
            }
            Thread.sleep(1000);
            System.out.println("[main] 最终数据: " + orderJdbc.findAll());
        }
    }

    // ------------------------------------------------------------------ 业务

    /** 调用方：带事务，内部调另一个 bean 的 @Async 方法（跨 bean 才会走代理）。 */
    public static class CallerService {

        private final OrderJdbc orderJdbc;

        private final AsyncService asyncService;

        CallerService(OrderJdbc orderJdbc, AsyncService asyncService) {
            this.orderJdbc = orderJdbc;
            this.asyncService = asyncService;
        }

        @Transactional
        public void commitThenAsyncFail(String name) {
            Trace.log("caller");                                      // BREAKPOINT 1
            orderJdbc.insert(name + "-outer");
            asyncService.fail(name);                                  // 这里只是提交任务，立即返回
            Trace.log("caller-after-async-submit");                    // BREAKPOINT 2
        }

        @Transactional
        public void rollbackThenAsyncOk(String name) throws InterruptedException {
            Trace.log("caller-2");                                     // BREAKPOINT 3
            orderJdbc.insert(name + "-outer");
            asyncService.ok(name);
            Thread.sleep(300);              // 给异步线程留时间先提交，再让调用方回滚
            throw new IllegalStateException("caller rollback");
        }
    }

    /** 异步方：@Async 在外层先切线程，@Transactional 才在新线程里开事务。 */
    public static class AsyncService {

        private final OrderJdbc orderJdbc;

        AsyncService(OrderJdbc orderJdbc) {
            this.orderJdbc = orderJdbc;
        }

        @Async
        @Transactional
        public void fail(String name) {
            Trace.log("async-fail");                                  // BREAKPOINT 4
            orderJdbc.insert(name + "-inner");
            throw new IllegalStateException("async rollback");        // BREAKPOINT 5
        }

        @Async
        @Transactional
        public void ok(String name) {
            Trace.log("async-ok");                                    // BREAKPOINT 6
            orderJdbc.insert(name + "-inner");
        }
    }

    /** 打印线程名、事务是否激活、当前事务名。 */
    public static class Trace {

        static void log(String tag) {
            System.out.println("[" + tag + "] thread=" + Thread.currentThread().getName()
                    + " txActive=" + TransactionSynchronizationManager.isActualTransactionActive()
                    + " txName=" + TransactionSynchronizationManager.getCurrentTransactionName());
        }
    }

    /** H2 读写，和 txdemo 里的 OrderRepository 一个路子。 */
    public static class OrderJdbc {

        private final JdbcTemplate jdbcTemplate;

        OrderJdbc(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
        }

        void initSchema() {
            jdbcTemplate.execute("create table if not exists t_order ("
                    + "id bigint generated by default as identity primary key, "
                    + "name varchar(64))");
        }

        void truncate() {
            jdbcTemplate.execute("truncate table t_order");
        }

        void insert(String name) {
            jdbcTemplate.update("insert into t_order(name) values (?)", name);
        }

        List<String> findAll() {
            return jdbcTemplate.queryForList("select name from t_order order by id", String.class);
        }
    }

    // ------------------------------------------------------------------ 容器

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:asynctxdemo;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("async-tx-");
        return executor;
    }

    @Bean
    public OrderJdbc orderJdbc(JdbcTemplate jdbcTemplate) {
        return new OrderJdbc(jdbcTemplate);
    }

    @Bean
    public AsyncService asyncService(OrderJdbc orderJdbc) {
        return new AsyncService(orderJdbc);
    }

    @Bean
    public CallerService callerService(OrderJdbc orderJdbc, AsyncService asyncService) {
        return new CallerService(orderJdbc, asyncService);
    }
}
