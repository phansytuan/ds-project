package com.datastructures.hashtable;

import java.util.HashMap;

/**
 * A least-recently-used cache holds a fixed number of entries. When it is full, the item touched longest ago is removed.
 * A hash map finds a node by key in O(1); a doubly linked list keeps usage order so the tail side is easiest to evict.
 */
public class LRUCache {

    private static class CacheNode {
        int key;
        int value;
        CacheNode prev;
        CacheNode next;

        CacheNode(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private final int capacity;
    private final HashMap<Integer, CacheNode> keyToNode;

    /** Dummy nodes so we never insert/remove at null; real items sit between them. */
    private final CacheNode headSentinel;
    private final CacheNode tailSentinel;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.keyToNode = new HashMap<>();

        headSentinel = new CacheNode(0, 0);
        tailSentinel = new CacheNode(0, 0);
        headSentinel.next = tailSentinel;
        tailSentinel.prev = headSentinel;
    }

    /** Returns the value or -1; on a hit, moves that entry to the most-recent side. */
    public int get(int key) {
        if (!keyToNode.containsKey(key)) {
            return -1;
        }
        CacheNode node = keyToNode.get(key);
        moveToMostRecent(node);
        return node.value;
    }

    /** Adds or updates a key. Evicts the least-recent entry if the cache would exceed capacity. */
    public void put(int key, int value) {
        if (keyToNode.containsKey(key)) {
            CacheNode node = keyToNode.get(key);
            node.value = value;
            moveToMostRecent(node);
            return;
        }

        CacheNode newNode = new CacheNode(key, value);
        keyToNode.put(key, newNode);
        linkAfterHead(newNode);

        if (keyToNode.size() > capacity) {
            CacheNode leastRecent = tailSentinel.prev;
            unlink(leastRecent);
            keyToNode.remove(leastRecent.key);
            System.out.println("Evicted least-recent key -> " + leastRecent.key);
        }
    }

    private void linkAfterHead(CacheNode node) {
        node.next = headSentinel.next;
        node.prev = headSentinel;
        headSentinel.next.prev = node;
        headSentinel.next = node;
    }

    private void unlink(CacheNode node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void moveToMostRecent(CacheNode node) {
        unlink(node);
        linkAfterHead(node);
    }

    /** Human-readable order from most-recent to least-recent. */
    public String cacheOrder() {
        StringBuilder builder = new StringBuilder("[most recent ... least recent]: ");
        CacheNode current = headSentinel.next;
        while (current != tailSentinel) {
            builder.append("(").append(current.key).append("=").append(current.value).append(")");
            if (current.next != tailSentinel) {
                builder.append(" -> ");
            }
            current = current.next;
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        System.out.println("--- LRU cache demo (capacity 3) ---");

        LRUCache cache = new LRUCache(3);

        cache.put(1, 100);
        System.out.println("Put 1=100 | " + cache.cacheOrder());
        cache.put(2, 200);
        System.out.println("Put 2=200 | " + cache.cacheOrder());
        cache.put(3, 300);
        System.out.println("Put 3=300 | " + cache.cacheOrder());

        System.out.println();
        System.out.println("get(1) -> " + cache.get(1) + " | " + cache.cacheOrder());

        System.out.println();
        cache.put(4, 400);
        System.out.println("Put 4=400 | " + cache.cacheOrder());
        System.out.println("get(2) after eviction -> " + cache.get(2));

        System.out.println();
        cache.put(5, 500);
        System.out.println("Put 5=500 | " + cache.cacheOrder());

        System.out.println();
        cache.put(1, 999);
        System.out.println("Put 1=999 (update) | " + cache.cacheOrder());
        System.out.println("get(1) -> " + cache.get(1));
    }
}
