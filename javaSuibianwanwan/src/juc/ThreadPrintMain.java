package juc;

public class ThreadPrintMain {

    private static int num = 0;
    private static int max_num = 100;

    private static Object lock = new Object();


    public static void main(String[] args) {
        Thread thread1 = new Thread(new ThreadPrint(0), "Thread-0");
        Thread thread2 = new Thread(new ThreadPrint(1), "Thread-1");
        Thread thread3 = new Thread(new ThreadPrint(2), "Thread-2");
        thread1.start();
        thread2.start();
        thread3.start();
    }

    static class ThreadPrint implements Runnable {
        private int threadId;

        public ThreadPrint(int threadId) {
            this.threadId = threadId;
        }
        @Override
        public void run() {
            while (num < max_num) {
                synchronized (lock) {
                    if (num >= max_num) {
                        break;
                    }
                    if (num % 3 == threadId) {
                        System.out.println(Thread.currentThread().getName() + ":" + num);
                        num++;
                        lock.notifyAll();
                    } else {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }


}