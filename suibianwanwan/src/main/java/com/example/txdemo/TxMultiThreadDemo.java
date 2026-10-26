package com.example.txdemo;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 多线程事务演示入口，直接在 IDE 中运行 main 即可，不需要启动 Web 容器。
 *
 * <p>三个场景：
 * <ol>
 *   <li>@Transactional 方法内开子线程：父线程回滚，子线程数据仍在</li>
 *   <li>方案一：所有写操作收回主线程一个事务，全成或全败</li>
 *   <li>方案二：分片独立事务，失败分片单独记录，其余保留</li>
 * </ol>
 */
@Configuration
@EnableTransactionManagement
public class TxMultiThreadDemo {

    public static void main(String[] args) throws Exception {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(TxMultiThreadDemo.class)) {

            OrderRepository repository = context.getBean(OrderRepository.class);
            repository.initSchema();

            System.out.println("\n=== 场景 1：@Transactional 内开子线程（验证失效）===");
            repository.truncate();
            try {
                context.getBean(DeclarativeTxService.class).createThenThrow("A");
            }
            catch (Exception ex) {
                System.out.println("[main] 父线程异常: " + ex.getMessage());
            }
            System.out.println("[main] 库中数据: " + repository.findAll());

            System.out.println("\n=== 场景 2：方案一，主线程单事务（强一致）===");
            repository.truncate();
            context.getBean(SingleTxService.class).createAll(List.of("B1", "B2", "B3"));
            System.out.println("[main] 库中数据: " + repository.findAll());

            System.out.println("\n=== 场景 3：方案二，分片独立事务（最终一致）===");
            repository.truncate();
            ExecutorService pool = Executors.newFixedThreadPool(3);
            context.getBean(ShardTxService.class).writeShards(List.of("C1", "C2", "C3"), pool);
            pool.shutdown();
            System.out.println("[main] 库中数据: " + repository.findAll());
        }
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:txdemo;DB_CLOSE_DELAY=-1");
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
    public OrderRepository orderRepository(JdbcTemplate jdbcTemplate) {
        return new OrderRepository(jdbcTemplate);
    }

    @Bean
    public DeclarativeTxService declarativeTxService(OrderRepository orderRepository) {
        return new DeclarativeTxService(orderRepository);
    }

    @Bean
    public SingleTxService singleTxService(OrderRepository orderRepository) {
        return new SingleTxService(orderRepository);
    }

    @Bean
    public ShardTxService shardTxService(OrderRepository orderRepository,
                                         PlatformTransactionManager transactionManager) {
        return new ShardTxService(orderRepository, transactionManager);
    }
}
