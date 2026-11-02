package com.example.isolationdemo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * next-key lock 加锁范围可视化：执行 SELECT ... FOR UPDATE 后，
 * 把 performance_schema.data_locks 里的 RECORD 级锁逐条打出来，看清锁了哪几段。
 *
 * <p>三个场景对应 next-key lock 的三种形态：范围查询、等值命中、等值未命中。
 *
 * <p><b>版本要求</b>：performance_schema.data_locks 是 MySQL 8.0 起才有的；
 * 5.7 只能查 information_schema.innodb_locks，字段不一样。
 * 隔离级别必须是 REPEATABLE READ，RC 下没有间隙锁，这个 demo 看不到效果。
 *
 * <p><b>运行前先填下面三个常量，并指向一个空的测试库</b>——
 * 本 demo 会执行 DROP TABLE IF EXISTS t。
 *
 * <p>运行方式：IDE 中右键 main 方法直接运行。
 */
public class NextKeyLockDemo {

    // ==================== 先填这里，三个都要填 ====================
    private static final String URL = "";
    private static final String USER = "";
    private static final String PASSWORD = "";
    // =============================================================

    public static void main(String[] args) throws Exception {
        if (URL.isBlank() || USER.isBlank()) {
            System.out.println("请先填写 NextKeyLockDemo 顶部的 URL / USER / PASSWORD");
            System.out.println("提示：指向一个空的测试库，本 demo 会 DROP 并重建表 t");
            return;
        }

        init();
        demoRange();
        demoHitRecord();
        demoMissRecord();
        howToRead();
    }

    // ---------------------------------------------------------- 场景 A

    /** 范围查询：命中记录加 next-key lock，边界外的间隙补一把 gap lock。 */
    private static void demoRange() throws SQLException {
        System.out.println("\n===== 场景 A：范围查询 =====");
        System.out.println("数据 : id = 1, 5, 10, 20");
        System.out.println("SQL  : SELECT * FROM t WHERE id >= 5 AND id <= 15 FOR UPDATE");

        try (Connection conn = open()) {
            exec(conn, "SELECT * FROM t WHERE id >= 5 AND id <= 15 FOR UPDATE");
            printLocks(conn);
            conn.rollback();
        }
    }

    // ---------------------------------------------------------- 场景 B

    /** 等值命中唯一索引：next-key lock 退化成纯记录锁，不锁间隙。 */
    private static void demoHitRecord() throws SQLException {
        System.out.println("\n===== 场景 B：等值查询，命中已有记录 =====");
        System.out.println("SQL  : SELECT * FROM t WHERE id = 5 FOR UPDATE");

        try (Connection conn = open()) {
            exec(conn, "SELECT * FROM t WHERE id = 5 FOR UPDATE");
            printLocks(conn);
            conn.rollback();
        }
    }

    // ---------------------------------------------------------- 场景 C

    /** 等值未命中：记录不存在，只锁住它该落进去的那段间隙。 */
    private static void demoMissRecord() throws SQLException {
        System.out.println("\n===== 场景 C：等值查询，没命中记录 =====");
        System.out.println("SQL  : SELECT * FROM t WHERE id = 8 FOR UPDATE");

        try (Connection conn = open()) {
            exec(conn, "SELECT * FROM t WHERE id = 8 FOR UPDATE");
            printLocks(conn);
            conn.rollback();
        }
    }

    // ---------------------------------------------------------- 打印锁

    private static void printLocks(Connection conn) throws SQLException {
        String sql = """
                SELECT INDEX_NAME, LOCK_MODE, LOCK_DATA
                FROM performance_schema.data_locks
                WHERE OBJECT_SCHEMA = DATABASE()
                  AND OBJECT_NAME = 't'
                  AND LOCK_TYPE = 'RECORD'
                  AND LOCK_STATUS = 'GRANTED'
                """;

        System.out.printf("  %-10s %-16s %s%n", "INDEX", "LOCK_MODE", "LOCK_DATA");
        System.out.println("  " + "-".repeat(48));

        boolean empty = true;
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                empty = false;
                System.out.printf("  %-10s %-16s %s%n",
                        rs.getString("INDEX_NAME"),
                        rs.getString("LOCK_MODE"),
                        rs.getString("LOCK_DATA"));
            }
        }
        if (empty) {
            System.out.println("  (没有 RECORD 级锁，确认隔离级别是不是 REPEATABLE READ)");
        }
    }

    // ---------------------------------------------------------- 说明

    private static void howToRead() {
        System.out.println("\n===== 怎么读这张表 =====");
        System.out.println("X              : next-key lock，锁住「前一条记录之后，到这条记录」这段区间");
        System.out.println("X,REC_NOT_GAP  : 只锁这条记录本身，它前面的间隙不锁，别人还能插进来");
        System.out.println("X,GAP          : 只锁间隙，不锁记录本身");
        System.out.println("LOCK_DATA      : 这条锁的右边界；值是 supremum 表示正无穷那头");
        System.out.println("判断能否插入   : 看目标位置是否被某条 X 或 X,GAP 覆盖");
    }

    // ---------------------------------------------------------- 工具方法

    private static void init() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = conn.createStatement()) {
            st.execute("DROP TABLE IF EXISTS t");
            st.execute("CREATE TABLE t (id INT PRIMARY KEY, c INT) ENGINE = InnoDB");
            st.execute("INSERT INTO t VALUES (1, 1), (5, 5), (10, 10), (20, 20)");
        }
    }

    /** 开一个手动提交的 RR 连接，next-key lock 只在 RR 下完整存在。 */
    private static Connection open() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        conn.setAutoCommit(false);
        conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
        return conn;
    }

    private static void exec(Connection conn, String sql) throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        }
    }
}
