package logic;

import java.util.PriorityQueue;

// 295
public class S_295_SMedianFinder {
    private final PriorityQueue<Integer> small = new PriorityQueue<>((a, b) -> b - a);
    private final PriorityQueue<Integer> large = new PriorityQueue<>();

    public S_295_SMedianFinder() {
    }

    public void addNum(int num) {
        small.offer(num);
        large.offer(small.poll());
        if (large.size() > small.size()) {
            small.offer(large.poll());
        }
    }

    public double findMedian() {
        if (small.size() > large.size()) {
            return small.peek();
        }
        return (small.peek() + large.peek()) / 2.0;
    }
}
