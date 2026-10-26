package com.example.txdemo;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 场景 1：@Transactional 方法内开子线程，验证事务上下文不会跨线程传播。
 */
public class DeclarativeTxService {

    private final OrderRepository orderRepository;

    public DeclarativeTxService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void createThenThrow(String name) throws InterruptedException {
        orderRepository.insert(name + "-parent");
        System.out.println("[parent] active=" + TransactionSynchronizationManager.isActualTransactionActive()
                + ", thread=" + Thread.currentThread().getName());          // BREAKPOINT 1

        Thread child = new Thread(() -> {
            System.out.println("[child] active=" + TransactionSynchronizationManager.isActualTransactionActive()
                    + ", thread=" + Thread.currentThread().getName());      // BREAKPOINT 2
            orderRepository.insert(name + "-child");
        }, "child-thread");
        child.start();
        child.join();

        throw new IllegalStateException("parent rollback");
    }
}
