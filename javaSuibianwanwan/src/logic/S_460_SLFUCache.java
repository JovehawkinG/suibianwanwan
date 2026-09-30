package logic;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

// 460
public class S_460_SLFUCache {
    private int capacity;
    private int size;
    private int minFreq;
    private Map<Integer, Node> keyMap;
    private Map<Integer, LinkedHashMap<Integer, Node>> freqMap;

    private static class Node {
        int key;
        int value;
        int freq;
        Node(int key, int value) {
            this.key = key;
            this.value = value;
            this.freq = 1;
        }
    }

    public S_460_SLFUCache(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.minFreq = 0;
        this.keyMap = new HashMap<>();
        this.freqMap = new HashMap<>();
    }

    public int get(int key) {
        Node node = keyMap.get(key);
        if (node == null) {
            return -1;
        }
        update(node);
        return node.value;
    }

    public void put(int key, int value) {
        if (capacity == 0) {
            return;
        }
        Node node = keyMap.get(key);
        if (node != null) {
            node.value = value;
            update(node);
            return;
        }
        if (size == capacity) {
            LinkedHashMap<Integer, Node> minList = freqMap.get(minFreq);
            Node toRemove = minList.values().iterator().next();
            minList.remove(toRemove.key);
            keyMap.remove(toRemove.key);
            size--;
        }
        Node newNode = new Node(key, value);
        minFreq = 1;
        keyMap.put(key, newNode);
        freqMap.computeIfAbsent(1, k -> new LinkedHashMap<>()).put(key, newNode);
        size++;
    }

    private void update(Node node) {
        int freq = node.freq;
        LinkedHashMap<Integer, Node> list = freqMap.get(freq);
        list.remove(node.key);
        if (list.isEmpty()) {
            freqMap.remove(freq);
            if (minFreq == freq) {
                minFreq++;
            }
        }
        node.freq++;
        freqMap.computeIfAbsent(node.freq, k -> new LinkedHashMap<>()).put(node.key, node);
    }
}
