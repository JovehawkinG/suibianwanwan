package com.example.suibianwanwan.transaction.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
// import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class TransactionServiceTest {

    @Test
    public void testRequired() {
        final Thread thread1 = new Thread(() -> System.out.println(Thread.currentThread().getName() + " is Running."),"T1");

        final Thread thread2 = new Thread(() -> {
            try {
                thread1.join();
            } catch (InterruptedException e) {
                System.out.println("join thread1 failed");
            }
            System.out.println(Thread.currentThread().getName() + " is Running.");
        },"T2");

        Thread thread3 = new Thread(() -> {
            try {
                Thread.sleep(2000);
                thread2.join();
            } catch (InterruptedException e) {
                System.out.println("join thread1 failed");
            }
            System.out.println(Thread.currentThread().getName() + " is Running.");
        },"T3");

        thread3.start();
        thread2.start();
        thread1.start();
    }

}