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