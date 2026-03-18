package com.datastructures.hashtable;

/**
 * ============================================================
 * BONUS: LRU Cache (Least Recently Used)
 * ============================================================

 * CONCEPT:
 *   An LRU Cache evicts the Least Recently Used item when it's
 *   full & a new item needs to be inserted.

 *   "Recently used" = accessed (get) / added (put).
 *   The item not used for the longest time is evicted first.

 * THE TRICK — Combine 2 data structures:

 *   1. HashMap<key, Node>:
 *      Provides O(1) lookup of any node by key.

 *   2. Doubly Linked List:
 *      Maintains RECENCY ORDER.
 *          Most recently used = head.
 *          Least recently used = tail.
 *      Allows O(1) insert & delete anywhere (given the node ref).

 *   Example (capacity=3):

 *   put(1): [1] (most recent)
 *   put(2): [2] ⇄ [1]
 *   put(3): [3] ⇄ [2] ⇄ [1] (full)
 *   get(1): [1] ⇄ [3] ⇄ [2] (1 moved to front as most recent)
 *   put(4): [4] ⇄ [1] ⇄ [3] (2 evicted — LRU)

 * TIME COMPLEXITY : O(1) for both get & put!
 * SPACE COMPLEXITY: O(capacity)

 * This is one of the MOST POPULAR coding interview questions at top tech companies (Google, Meta, Amazon).
 * ============================================================
 */
public class LRUCache {

    // ──────────────────────────────────────────────
    // INNER CLASSES
    // ──────────────────────────────────────────────

    /** Node that stores both key & value (we need the key to delete from map). */
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

    // ──────────────────────────────────────────────
    // FIELDS
    // ──────────────────────────────────────────────

    private final int capacity;
    private final java.util.HashMap<Integer, CacheNode> map; // key → node (O(1) lookup)

    // Use SENTINEL nodes (dummy head & tail) to simplify edge cases.
    //  head.next = most recently used
    //  tail.prev = least recently used
    private final CacheNode head; // Dummy head (most recent side)
    private final CacheNode tail; // Dummy tail (least recent side)

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new java.util.HashMap<>();

        // Initialize sentinel nodes
        head = new CacheNode(0, 0);
        tail = new CacheNode(0, 0);
        head.next = tail;
        tail.prev = head;
    }

    // ──────────────────────────────────────────────
    // CORE OPERATIONS
    // ──────────────────────────────────────────────

    /**
     * GET: Return the value if key exists, otherwise -1.
     * On hit: move node to the FRONT (mark as most recently used).
     * Time: O(1)
     */
    public int get(int key) {
        if (!map.containsKey(key))  return -1;

        CacheNode node = map.get(key);
        moveToFront(node); // Mark as recently used
        return node.value;
    }

    /**
     * PUT: Insert or update a key-value pair.
     * - If key exists: update value and move to front.
     * - If new key: add to front. If over capacity, evict LRU (tail).
     * Time: O(1)
     */
    public void put(int key, int value) {
        if (map.containsKey(key)) {
            // Update existing node
            CacheNode node = map.get(key);
            node.value = value;
            moveToFront(node);
        } else {
            // Create new node
            CacheNode newNode = new CacheNode(key, value);
            map.put(key, newNode);
            insertAtFront(newNode);

            // Evict LRU if over capacity
            if (map.size() > capacity) {
                CacheNode lru = tail.prev; // Least recently used is right before dummy tail
                removeNode(lru);
                map.remove(lru.key);
                System.out.println("  [LRU] Evicted key=" + lru.key);
            }
        }
    }

    // ──────────────────────────────────────────────
    // LINKED LIST HELPERS
    // ──────────────────────────────────────────────

    /** Insert node right after the dummy head (= most recently used position). */
    private void insertAtFront(CacheNode node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    /** Detach a node from the list (O(1) with prev/next pointers). */
    private void removeNode(CacheNode node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    /** Move an existing node to the front (most recently used). */
    private void moveToFront(CacheNode node) {
        removeNode(node);     // Detach from current position
        insertAtFront(node);  // Re-insert at front
    }

    /** Print the list from MRU to LRU for visualization. */
    public String cacheOrder() {
        StringBuilder sb = new StringBuilder("[MRU→LRU]: ");
        CacheNode curr = head.next;
        while (curr != tail) {
            sb.append("(").append(curr.key).append("=").append(curr.value).append(")");
            if (curr.next != tail) sb.append(" → ");
            curr = curr.next;
        }
        return sb.toString();
    }

    // ──────────────────────────────────────────────
    // DEMO
    // ──────────────────────────────────────────────

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║          LRU CACHE DEMO          ║");
        System.out.println("╚══════════════════════════════════╝\n");

        LRUCache cache = new LRUCache(3); // Capacity: 3

        System.out.println("--- Building cache (capacity=3) ---");
        cache.put(1, 100); System.out.println("put(1,100): " + cache.cacheOrder());
        cache.put(2, 200); System.out.println("put(2,200): " + cache.cacheOrder());
        cache.put(3, 300); System.out.println("put(3,300): " + cache.cacheOrder());

        System.out.println("\n--- Access key=1 (moves to front) ---");
        System.out.println("get(1) = " + cache.get(1));
        System.out.println("Order: " + cache.cacheOrder());

        System.out.println("\n--- Add key=4 (evicts LRU=key 2) ---");
        cache.put(4, 400);
        System.out.println("put(4,400): " + cache.cacheOrder());

        System.out.println("\n--- Try to access evicted key=2 ---");
        System.out.println("get(2) = " + cache.get(2)); // -1: evicted!

        System.out.println("\n--- Add more (evict key=3) ---");
        cache.put(5, 500);
        System.out.println("put(5,500): " + cache.cacheOrder());

        System.out.println("\n--- Update existing key ---");
        cache.put(1, 999);
        System.out.println("put(1,999): " + cache.cacheOrder());
        System.out.println("get(1) = " + cache.get(1)); // 999
    }
}
