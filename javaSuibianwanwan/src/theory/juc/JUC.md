1. 什么是线程的上下文切换
    概念：CPU 切换线程时 保存当前线程状态 恢复另一个线程状态 过程

    触发场景：
        - 时间片
        - 主动让出
        - 被抢占
        - 同步点

2. JAVA 线程的六种状态
    - NEW：刚创建，没有调用 start()
    - RUNNABLE：正在执行或等待CPU时间片
    - BLOCKED：等待同步锁
    - WAITING：无限期等待，直到被唤醒
    - TIMED_WAITING：带超时时间的等待
    - TERMINATED：执行结束

3. 线程状态流转
    NEW -> RUNNABLE：调用 thread#start()
    RUNNABLE -> BLOCKED：进入Synchronized同步锁抢占失败
    BLOCKED -> RUNNABLE：拿到锁
    RUNNABLE -> TERMINATED：run()执行完
    RUNNABLE -> WAITING：Object#wait()、Thread#join()、LockSupport#park()
    RUNNABLE -> TIMED_WAITING：带参数
    TERMINATED：不能切换到其他状态

4. JAVA 创建线程的几种方式
    - 继承 Thread
    - 实现 Runnable
    - 实现 Callable + FutureTask
    - 线程池
    - CompletableFuture 异步编排
    - 虚拟线程

5. 线程池执行流程
    - 线程数 小于 核心线程数：新建核心线程；否则放入任务队列中
    - 如果任务队列满了，且线程数 小于 最大线程数，则新建非核心线程；否则拒绝策略

6. ThreadLocal
    - 用于作为 key，获取线程的特有数据
    - 通常用 static final 修饰，表示所有线程的 key 都是一个对象
    - 每个 Thread 线程内部都有一个 ThreadLocal.ThreadLocalMap，数据本质是存放在 Thread 中，使用 ThreadLocal 获取。
    - 可能存在内存泄漏问题，因为 ThreadLocalMap 的 key 是弱引用，但是 value 是强引用。用完需要 remove() 一下，清空 value 值。

7. 线程同步
    - 让多个线程按照规则访问共享资源，保证 可见性、原子性、有序性
    - 方法：
        - synchronized 锁
        - volatile 
        - Lock
        - CAS
        - JUC 工具类（Semaphore、CountDownLatch等）
        - 阻塞队列
        - ThreadLocal
        - 不可变对象
        - 并发容器（ConcurrentHashMap等）

8. 死锁
    - 多个线程互相持有对方需要的资源，且都等对象释放，导致程序卡死的场景
    - 必要条件：
        - 互斥：一个资源在同一时刻只能被一个线程占用
        - 占有且等待：占有一个且等待另一个
        - 不可抢占：只能等持有者主动释放
        - 循环等待：等待的一个被别人持有

9. JAVA 内存模型
    - 一套规范：规定 多线程 下，线程如何读写共享变量，以及什么情况下一个线程的写对另一个线程可见；为了提高性能，保证多线程共享变量的正确（并发安全）
    - 把内存分成两层：主内存和工作内存
        - 主内存：所有线程共享
        - 工作内存：线程私有，主内存的副本
        - 线程对变量的操作：读主内存 -> 写到工作内存 -> 修改 -> 刷回主内存
        - 线程对变量的操作要求：原子性、可见性、有序性
    - Happens-Before 原则
        - 如果操作 A happens-before 操作 B 之前，那么 A 的结果对 B 可见，且 A 在顺序上排在 B 的前面

10. JUC 中的原子性和数据库事务的AIDC中的原子性一样吗
    不一样，JUC 的原子性指的是操作不可分割，数据库事务的原子性是指操作要么都执行，要么都不执行