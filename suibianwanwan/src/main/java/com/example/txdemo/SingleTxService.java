package com.example.txdemo;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 方案一：把所有写操作收敛回主线程一个事务，保证强一致。
 */
public class SingleTxService {

    private final OrderRepository orderRepository;

    public SingleTxService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void createAll(List<String> names) {
        names.forEach(orderRepository::insert);       // BREAKPOINT 3
    }
}
