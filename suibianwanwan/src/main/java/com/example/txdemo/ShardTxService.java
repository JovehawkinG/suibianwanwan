package com.example.txdemo;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * 方案二：每个分片在线程池中用一个独立事务，失败分片单独记录，靠补偿收敛。
 */
public class ShardTxService {

    private final OrderRepository orderRepository;
    private final TransactionTemplate transactionTemplate;

    public ShardTxService(OrderRepository orderRepository, PlatformTransactionManager transactionManager) {
        this.orderRepository = orderRepository;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    public void writeShards(List<String> names, ExecutorService pool) {
        List<CompletableFuture<String>> futures = names.stream()
                .map(name -> CompletableFuture.supplyAsync(() -> {
                    try {
                        transactionTemplate.executeWithoutResult(status -> {
                            orderRepository.insert(name);
                            if (name.endsWith("2")) {
                                throw new IllegalStateException("shard fail: " + name);
                            }
                        });
                        return "OK   " + name;
                    }
                    catch (Exception ex) {
                        return "FAIL " + name + " -> " + ex.getMessage();
                    }
                }, pool))
                .toList();

        futures.forEach(future -> System.out.println("[shard] " + future.join()));  // BREAKPOINT 4
    }
}
