package com.example.isolationdemo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 三种读现象演示（MySQL / InnoDB 版）。
 *
 * <p>两个 Connection 模拟两个会话，切换隔离级别观察结果。后两个场景是 InnoDB 特有行为：
 * RR 下快照读挡住幻读、当前读却能让幻读显形；以及当前读靠 next-key lock 挡住插入。
 *
 * <p><b>运行前先填下面三个常量，并指向一个空的测试库</b>——
 * 本 demo 会执行 DROP TABLE IF EXISTS t、INSERT、DELETE。
 *
 * <p>运行方式：IDE 中右键 main 方法直接运行。
 */
public class MySQLIsolationDemo {

    // ==================== 先填这里，三个都要填 ====================
    // 示例：jdbc:mysql://127.0.0.1:3306/demo_test?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    private static final String URL = "jdbc:mysql://47.94.179.93:3306/mydb?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "YourStrongPassword123!";
    // =============================================================

    public static void main(String[] args) throws Exception {
        if (URL.isBlank() || USER.isBlank()) {
            System.out.println("请先填写 MySQLIsolationDemo 顶部的 URL / USER / PASSWORD");
            System.out.println("提示：指向一个空的测试库，本 demo 会 DROP 并重建表 t");
            return;
        }

        try {
            init();
            demoDirtyRead();
            demoNonRepeatableRead();
            demoPhantomReadReadCommitted();
            demoSnapshotVsCurrentRead();
            demoNextKeyLock();
            summary();
        }
        catch (SQLException e) {
            System.out.println("\n执行中断：" + e.getMessage());
            System.out.println("连接不稳定时重跑一次即可；每次握手要好几秒是正常的");
        }
    }

    // ---------------------------------------------------------- 场景一

    /** 脏读：读到别的事务未提交的数据，对方一回滚，这个值等于从未存在过。 */
    private static void demoDirtyRead() throws SQLException {
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

    // ---------------------------------------------------------- 场景二

    /** 不可重复读：同一事务内两次读同一行，值不一样。 */
    private static void demoNonRepeatableRead() throws SQLException {
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

    // ---------------------------------------------------------- 场景三

    /** 幻读：同一事务内两次查同一范围，行数不一样。 */
    private static void demoPhantomReadReadCommitted() throws SQLException {
        System.out.println("\n===== 场景三：幻读（READ COMMITTED）=====");
        try (Connection a = open(Connection.TRANSACTION_READ_COMMITTED);
             Connection b = open(Connection.TRANSACTION_READ_COMMITTED)) {

            exec(b, "DELETE FROM t WHERE id > 10");
            b.commit();

            int first = queryInt(a, "SELECT COUNT(*) FROM t WHERE id > 10");
            System.out.println("A 第一次 count(id > 10) = " + first);

            exec(b, "INSERT INTO t VALUES (21, 1)");
            b.commit();
            System.out.println("B 插入 id = 21 并提交");

            int second = queryInt(a, "SELECT COUNT(*) FROM t WHERE id > 10");
            System.out.println("A 第二次 count(id > 10) = " + second
                    + (second != first ? "   -> 行数变了，出现幻读" : "   -> 没变"));

            a.commit();
        }
    }

    // ---------------------------------------------------------- 场景四

    /**
     * InnoDB 的 RR 不是简单的“没有幻读”。
     *
     * <p>快照读靠 MVCC 挡住新行，当前读（UPDATE / FOR UPDATE）却看得见，
     * 而且自己改过的行会让后续快照读也看得见 —— 幻读就这么显形了。
     */
    private static void demoSnapshotVsCurrentRead() throws SQLException {
        System.out.println("\n===== 场景四：REPEATABLE READ 下快照读 vs 当前读 =====");
        try (Connection a = open(Connection.TRANSACTION_REPEATABLE_READ);
             Connection b = open(Connection.TRANSACTION_REPEATABLE_READ)) {

            exec(b, "DELETE FROM t WHERE id > 10");
            b.commit();

            int v1 = queryInt(a, "SELECT COUNT(*) FROM t WHERE id > 10");
            System.out.println("A 快照读 #1  count = " + v1);

            exec(b, "INSERT INTO t VALUES (31, 1)");
            b.commit();
            System.out.println("B 插入 id = 31 并提交");

            int v2 = queryInt(a, "SELECT COUNT(*) FROM t WHERE id > 10");
            System.out.println("A 快照读 #2  count = " + v2 + "   -> 没变，MVCC 挡住了");

            // 注意必须真正改到值。写成 SET c = c 的话 InnoDB 判定无变化，不产生新版本，
            // 行的 DB_TRX_ID 不变，后面那次快照读照样看不见它。
            int affected = execUpdate(a, "UPDATE t SET c = c + 1 WHERE id > 10");
            System.out.println("A 当前读 UPDATE 影响 = " + affected + " 行   -> 新插入的行被看见了");

            int v3 = queryInt(a, "SELECT COUNT(*) FROM t WHERE id > 10");
            System.out.println("A 快照读 #3  count = " + v3
                    + (v3 > v2 ? "   -> 自己改过的行变可见，幻读显形" : "   -> 仍是 0，说明这行没被真正改到"));

            a.commit();
        }
    }

    // ---------------------------------------------------------- 场景五

    /** 当前读加 next-key lock，把间隙一起锁住，别的事务插不进来。 */
    private static void demoNextKeyLock() throws SQLException {
        System.out.println("\n===== 场景五：当前读的 next-key lock 挡住插入 =====");
        try (Connection a = open(Connection.TRANSACTION_REPEATABLE_READ);
             Connection b = open(Connection.TRANSACTION_REPEATABLE_READ)) {

            exec(b, "DELETE FROM t WHERE id > 10");
            b.commit();

            exec(a, "SELECT * FROM t WHERE id > 10 FOR UPDATE");
            System.out.println("A 执行 SELECT ... FOR UPDATE，锁住 id > 10 的间隙");

            exec(b, "SET SESSION innodb_lock_wait_timeout = 3");
            try {
                exec(b, "INSERT INTO t VALUES (41, 1)");
                b.commit();
                System.out.println("B 插入成功   -> 没有间隙锁（不该发生，请确认引擎是 InnoDB）");
            }
            catch (SQLException e) {
                b.rollback();
                System.out.println("B 插入被挡住，3 秒后失败：" + e.getMessage());
                System.out.println("   -> 间隙锁生效，这就是 RR 下当前读不幻读的原因");
            }

            a.commit();
        }
    }

    // ---------------------------------------------------------- 结论

    private static void summary() {
        System.out.println("\n===== 结论 =====");
        System.out.println("脏读       : READ UNCOMMITTED 下出现，READ COMMITTED 起被挡住");
        System.out.println("不可重复读 : READ COMMITTED 下出现，REPEATABLE READ 起被挡住");
        System.out.println("幻读       : READ COMMITTED 下出现");
        System.out.println("InnoDB RR  : 快照读靠 MVCC 挡住幻读；当前读看得见新行，靠 next-key lock 挡住别人插入");
        System.out.println("注意       : 同一事务里先快照读再当前读，两个视角对不上，这是 RR 下真实会踩的坑");
    }

    // ---------------------------------------------------------- 工具方法

    private static void init() throws SQLException {
        try (Connection conn = open(Connection.TRANSACTION_READ_COMMITTED)) {
            exec(conn, "DROP TABLE IF EXISTS t");
            exec(conn, "CREATE TABLE t (id INT PRIMARY KEY, c INT) ENGINE = InnoDB");
            exec(conn, "INSERT INTO t VALUES (1, 1)");
            conn.commit();
        }
    }

    /** 开一个手动提交、指定隔离级别的连接，模拟一个会话。连不上时重试 3 次。 */
    private static Connection open(int isolation) throws SQLException {
        SQLException last = null;
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                Connection conn = DriverManager.getConnection(buildUrl(), USER, PASSWORD);
                conn.setAutoCommit(false);
                conn.setTransactionIsolation(isolation);
                return conn;
            }
            catch (SQLException e) {
                last = e;
                System.out.println("  连接失败（第 " + attempt + "/3 次），1 秒后重试");
                sleepQuietly();
            }
        }
        throw last;
    }

    /** 补上超时参数，避免网络抖动时卡在操作系统默认的 20 秒以上。 */
    private static String buildUrl() {
        String url = URL;
        if (!url.contains("connectTimeout")) {
            url += (url.contains("?") ? "&" : "?") + "connectTimeout=8000";
        }
        if (!url.contains("socketTimeout")) {
            url += "&socketTimeout=60000";
        }
        return url;
    }

    private static void sleepQuietly() {
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void exec(Connection conn, String sql) throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        }
    }

    private static int execUpdate(Connection conn, String sql) throws SQLException {
        try (Statement st = conn.createStatement()) {
            return st.executeUpdate(sql);
        }
    }

    private static int queryInt(Connection conn, String sql) throws SQLException {
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            rs.next();
            return rs.getInt(1);
        }
    }
}
