package com.example.isolationdemo;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

/**
 * 三种读现象演示：脏读、不可重复读、幻读。
 *
 * <p>用 H2 内存库，每次开两个 Connection 模拟两个会话，切换隔离级别观察结果。
 *
 * <p>运行方式：
 * <pre>
 * IDE 中右键 main 方法运行
 * .\mvnw.cmd spring-boot:run "-Dspring-boot.run.main-class=com.example.isolationdemo.IsolationLevelDemo"
 * </pre>
 *
 * <p>注意 H2 与 MySQL 的差异：H2 的 REPEATABLE READ 用快照实现，幻读会被挡住；
 * 所以幻读分两段演示 —— READ COMMITTED 下出现，REPEATABLE READ 下消失。
 */
@SpringBootApplication(scanBasePackages = "com.example.isolationdemo")
public class IsolationLevelDemo implements ApplicationRunner {

    private final DataSource dataSource;

    public IsolationLevelDemo(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(IsolationLevelDemo.class);
        app.setDefaultProperties(Map.of(
                "spring.main.web-application-type", "none",
                "spring.datasource.url", "jdbc:h2:mem:isolation;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"));
        app.run(args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        init();
        demoDirtyRead();
        demoNonRepeatableRead();
        demoPhantomRead(Connection.TRANSACTION_READ_COMMITTED, "READ COMMITTED", 21);
        demoPhantomRead(Connection.TRANSACTION_REPEATABLE_READ, "REPEATABLE READ", 22);
        summary();
    }

    // ---------------------------------------------------------------- 场景一

    /** 脏读：读到别的事务未提交的数据，对方一回滚，这个值等于从未存在过。 */
    private void demoDirtyRead() throws Exception {
        System.out.println("\n===== 场景一：脏读（READ UNCOMMITTED）=====");
        try (Connection a = open(Connection.TRANSACTION_READ_UNCOMMITTED);
             Connection b = open(Connection.TRANSACTION_READ_UNCOMMITTED)) {

            exec(a, "UPDATE t SET c = 99 WHERE id = 1");
            System.out.println("A 把 c 改成 99，还没提交");

            int seen = queryInt(b, "SELECT c FROM t WHERE id = 1");
            System.out.println("B 读到 c = " + seen + (seen == 99 ? "   -> 脏读，A 还没提交" : "   -> 没读到"));

            a.rollback();
            System.out.println("A 回滚");

            int after = queryInt(b, "SELECT c FROM t WHERE id = 1");
            System.out.println("B 再读 c = " + after + "   -> 刚才那个 99 从未真实存在");

            b.rollback();
        }
    }

    // ---------------------------------------------------------------- 场景二

    /** 不可重复读：同一事务内两次读同一行，值不一样。 */
    private void demoNonRepeatableRead() throws Exception {
        System.out.println("\n===== 场景二：不可重复读（READ COMMITTED）=====");
        try (Connection a = open(Connection.TRANSACTION_READ_COMMITTED);
             Connection b = open(Connection.TRANSACTION_READ_COMMITTED)) {

            int first = queryInt(a, "SELECT c FROM t WHERE id = 1");
            System.out.println("A 第一次读 c = " + first);

            exec(b, "UPDATE t SET c = 2 WHERE id = 1");
            b.commit();
            System.out.println("B 改成 2 并提交");

            int second = queryInt(a, "SELECT c FROM t WHERE id = 1");
            System.out.println("A 第二次读 c = " + second
                    + (second != first ? "   -> 不可重复读，同一个事务两次不一样" : "   -> 没变"));

            a.commit();
        }
    }

    // ---------------------------------------------------------------- 场景三、四

    /** 幻读：同一事务内两次查同一范围，行数不一样。 */
    private void demoPhantomRead(int isolation, String label, int newId) throws Exception {
        System.out.println("\n===== " + (isolation == Connection.TRANSACTION_READ_COMMITTED
                ? "场景三" : "场景四") + "：幻读（" + label + "）=====");

        try (Connection a = open(isolation);
             Connection b = open(isolation)) {

            exec(b, "DELETE FROM t WHERE id > 10");
            b.commit();

            int first = queryInt(a, "SELECT COUNT(*) FROM t WHERE id > 10");
            System.out.println("A 第一次 count(id > 10) = " + first);

            exec(b, "INSERT INTO t VALUES (" + newId + ", 1)");
            b.commit();
            System.out.println("B 插入 id = " + newId + " 并提交");

            int second = queryInt(a, "SELECT COUNT(*) FROM t WHERE id > 10");
            System.out.println("A 第二次 count(id > 10) = " + second
                    + (second != first ? "   -> 行数变了，出现幻读" : "   -> 行数没变，快照挡住了幻读"));

            a.commit();
        }
    }

    // ---------------------------------------------------------------- 输出结论

    private void summary() {
        System.out.println("\n===== 结论 =====");
        System.out.println("脏读       : READ UNCOMMITTED 下出现，READ COMMITTED 起被挡住");
        System.out.println("不可重复读 : READ COMMITTED 下出现，REPEATABLE READ 起被挡住");
        System.out.println("幻读       : READ COMMITTED 下出现；H2 的 REPEATABLE READ 用快照挡住");
        System.out.println("MySQL 对照 : 默认 RR，快照读靠 MVCC 防幻读，当前读靠 next-key lock 防幻读");
    }

    // ---------------------------------------------------------------- 工具方法

    private void init() {
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);
        jdbc.execute("DROP TABLE IF EXISTS t");
        jdbc.execute("CREATE TABLE t (id INT PRIMARY KEY, c INT)");
        jdbc.update("INSERT INTO t VALUES (1, 1)");
    }

    /** 开一个手动提交、指定隔离级别的连接，模拟一个会话。 */
    private Connection open(int isolation) throws Exception {
        Connection conn = dataSource.getConnection();
        conn.setAutoCommit(false);
        conn.setTransactionIsolation(isolation);
        return conn;
    }

    private void exec(Connection conn, String sql) throws Exception {
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        }
    }

    private int queryInt(Connection conn, String sql) throws Exception {
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            rs.next();
            return rs.getInt(1);
        }
    }
}
