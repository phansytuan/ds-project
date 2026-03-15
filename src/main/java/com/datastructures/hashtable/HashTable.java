package com.datastructures.hashtable;

/**
 * ============================================================
 * DATA STRUCTURE: Hash Table (HashMap)
 * ============================================================
 *
 * CONCEPT:
 *   A hash table maps keys to values for O(1) average-case
 *   lookup, insertion, and deletion. It's one of the most
 *   powerful and frequently used data structures.
 *
 * HOW IT WORKS:
 *   1. Compute hash(key) → an integer
 *   2. Map that integer to a bucket index: index = hash % capacity
 *   3. Store (key, value) in that bucket
 *
 *   Key="name"  → hash() → 4382 → 4382 % 16 = 14 → bucket[14]
 *   Key="age"   → hash() → 9103 → 9103 % 16 = 15 → bucket[15]
 *   Key="city"  → hash() → 4382 → 4382 % 16 = 14 → COLLISION!
 *
 * COLLISION RESOLUTION (this implementation: Separate Chaining):
 *   Each bucket holds a linked list of entries.
 *   Collisions are handled by adding to the list.
 *
 *   bucket[14] → [("name","Alice")] → [("city","NY")] → null
 *
 *   Alternative: Open Addressing (linear/quadratic probing)
 *
 * LOAD FACTOR & REHASHING:
 *   load_factor = size / capacity
 *   When load_factor > threshold (e.g., 0.75), we rehash:
 *   double capacity and re-insert all entries.
 *   This keeps average chain length low → O(1) average operations.
 *
 * REAL-WORLD USE CASES:
 *   - Caching / memoization (most common!)
 *   - Counting frequencies (word count, anagram detection)
 *   - Two-sum / complement lookup
 *   - Deduplication (finding duplicates)
 *   - Symbol tables in compilers
 *
 * TIME COMPLEXITY (average):
 *   Put    : O(1) amortized
 *   Get    : O(1) average
 *   Remove : O(1) average
 *   Worst case (many collisions): O(n) — bad hash function!
 *
 * SPACE COMPLEXITY: O(n)
 *
 * INTERVIEW TIPS:
 *   - Hash maps appear in ~60% of coding problems
 *   - Common patterns: frequency counting, grouping, complement lookup
 *   - Know how to use Java's HashMap, HashSet from memory
 *   - Be ready to explain collision resolution strategies
 * ============================================================
 */
public class HashTable<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;

    // ──────────────────────────────────────────────
    // INNER CLASS: Entry (key-value pair node)
    // ──────────────────────────────────────────────

    /**
     * Each bucket in our table is a linked list of Entry objects.
     * Multiple entries in the same bucket = collision via chaining.
     */
    private static class Entry<K, V> {
        final K key;
        V value;
        Entry<K, V> next; // Next entry in the same bucket (for chaining)

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    // ──────────────────────────────────────────────
    // FIELDS
    // ──────────────────────────────────────────────

    private Entry<K, V>[] buckets; // Array of linked list heads
    private int size;              // Number of key-value pairs
    private int capacity;          // Number of buckets

    @SuppressWarnings("unchecked")
    public HashTable() {
        this.capacity = DEFAULT_CAPACITY;
        this.buckets = new Entry[capacity];
        this.size = 0;
    }

    // ──────────────────────────────────────────────
    // HASHING
    // ──────────────────────────────────────────────

    /**
     * Converts a key into a bucket index.
     *
     * Steps:
     *  1. key.hashCode() → raw hash (can be negative!)
     *  2. Apply secondary mixing to distribute bits more evenly
     *  3. Modulo capacity to get a valid bucket index
     *
     * Why mask with 0x7FFFFFFF? Because Java's % on negative numbers
     * returns a negative result. This clears the sign bit.
     */
    private int getBucketIndex(K key) {
        if (key == null) return 0; // Null keys go to bucket 0

        int hash = key.hashCode();
        // Secondary hash mixing (reduces clustering from bad hashCode()s)
        hash = hash ^ (hash >>> 16);
        // Map to [0, capacity) — always non-negative
        return (hash & 0x7FFFFFFF) % capacity;
    }

    // ──────────────────────────────────────────────
    // CORE OPERATIONS
    // ──────────────────────────────────────────────

    /**
     * PUT: Insert or update a key-value pair.
     * Time: O(1) average, O(n) worst case
     *
     * If the key already exists, update its value.
     * If the bucket is occupied by a different key → chain it.
     */
    public void put(K key, V value) {
        int index = getBucketIndex(key);
        Entry<K, V> head = buckets[index];

        // Walk the chain at this bucket looking for an existing key
        Entry<K, V> curr = head;
        while (curr != null) {
            if (keysEqual(curr.key, key)) {
                curr.value = value; // Key exists → update value
                return;
            }
            curr = curr.next;
        }

        // Key not found → prepend new entry at head of chain (O(1))
        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.next = head;       // New entry points to old chain head
        buckets[index] = newEntry;  // Bucket now starts at new entry
        size++;

        // Rehash if load factor exceeds threshold
        if ((double) size / capacity > LOAD_FACTOR_THRESHOLD) {
            rehash();
        }
    }

    /**
     * GET: Retrieve value by key.
     * Time: O(1) average
     */
    public V get(K key) {
        int index = getBucketIndex(key);
        Entry<K, V> curr = buckets[index];

        while (curr != null) {
            if (keysEqual(curr.key, key)) return curr.value;
            curr = curr.next;
        }
        return null; // Key not found
    }

    /**
     * REMOVE: Delete a key-value pair.
     * Time: O(1) average
     *
     * Must re-link the chain, skipping the removed entry.
     */
    public V remove(K key) {
        int index = getBucketIndex(key);
        Entry<K, V> curr = buckets[index];
        Entry<K, V> prev = null;

        while (curr != null) {
            if (keysEqual(curr.key, key)) {
                // Found it! Re-link around curr.
                if (prev == null) {
                    buckets[index] = curr.next; // Removing head of chain
                } else {
                    prev.next = curr.next;       // Removing from middle/end
                }
                size--;
                return curr.value;
            }
            prev = curr;
            curr = curr.next;
        }
        return null; // Key not found
    }

    /** Returns true if the key exists in the table. */
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    // ──────────────────────────────────────────────
    // REHASHING
    // ──────────────────────────────────────────────

    /**
     * Rehash: doubles capacity and re-inserts all entries.
     * This is triggered when load factor > 0.75.
     * Time: O(n) — but amortized O(1) per put operation.
     *
     * Why do we need this?
     *   More entries in each bucket → longer chains → slower lookup.
     *   By doubling capacity, we spread entries across more buckets,
     *   keeping average chain length < 1.
     */
    @SuppressWarnings("unchecked")
    private void rehash() {
        int oldCapacity = capacity;
        capacity *= 2;
        Entry<K, V>[] newBuckets = new Entry[capacity];
        System.out.println("  [HashTable] Rehashing: " + oldCapacity + " → " + capacity + " buckets");

        // Re-insert every entry into the new, larger table
        for (Entry<K, V> head : buckets) {
            Entry<K, V> curr = head;
            while (curr != null) {
                Entry<K, V> next = curr.next; // Save before we overwrite it

                // Recompute index for the new (doubled) capacity
                int newIndex = (curr.key.hashCode() & 0x7FFFFFFF) % capacity;

                // Prepend to new bucket (O(1))
                curr.next = newBuckets[newIndex];
                newBuckets[newIndex] = curr;

                curr = next;
            }
        }

        buckets = newBuckets;
    }

    // ──────────────────────────────────────────────
    // UTILITY
    // ──────────────────────────────────────────────

    /** Null-safe key comparison. */
    private boolean keysEqual(K a, K b) {
        if (a == null) return b == null;
        return a.equals(b);
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    /** Shows each bucket and its chain — great for debugging. */
    public void printBuckets() {
        System.out.println("HashTable internals (capacity=" + capacity + ", size=" + size + "):");
        for (int i = 0; i < capacity; i++) {
            if (buckets[i] != null) {
                System.out.print("  bucket[" + i + "]: ");
                Entry<K, V> curr = buckets[i];
                while (curr != null) {
                    System.out.print("[" + curr.key + "=" + curr.value + "]");
                    if (curr.next != null) System.out.print(" → ");
                    curr = curr.next;
                }
                System.out.println();
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Entry<K, V> head : buckets) {
            Entry<K, V> curr = head;
            while (curr != null) {
                if (!first) sb.append(", ");
                sb.append(curr.key).append("=").append(curr.value);
                first = false;
                curr = curr.next;
            }
        }
        return sb.append("}").toString();
    }

    // ══════════════════════════════════════════════
    // CLASSIC INTERVIEW PROBLEMS USING HASH TABLES
    // ══════════════════════════════════════════════

    /**
     * PROBLEM 1: Two Sum
     * Given an array and target, return indices of two numbers that add to target.
     * Input: nums=[2,7,11,15], target=9 → [0,1] (because 2+7=9)
     *
     * Approach: Store each number's index in a map.
     * For each num, check if (target - num) is already in the map.
     * Time: O(n), Space: O(n)
     */
    public static int[] twoSum(int[] nums, int target) {
        java.util.HashMap<Integer, Integer> map = new java.util.HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[]{map.get(complement), i};
            }
            map.put(nums[i], i); // Store this number's index
        }
        return new int[]{-1, -1}; // No solution
    }

    /**
     * PROBLEM 2: Group Anagrams
     * Group strings that are anagrams of each other.
     * Input: ["eat","tea","tan","ate","nat","bat"]
     * Output: [["bat"],["nat","tan"],["ate","eat","tea"]]
     *
     * Key insight: Two words are anagrams if their SORTED letters are equal.
     * Use the sorted string as the map KEY.
     * Time: O(n * k log k) where k is the max word length
     */
    public static java.util.List<java.util.List<String>> groupAnagrams(String[] strs) {
        java.util.HashMap<String, java.util.List<String>> map = new java.util.HashMap<>();

        for (String word : strs) {
            char[] chars = word.toCharArray();
            java.util.Arrays.sort(chars); // Sort letters → canonical form
            String key = new String(chars); // "eat" → "aet", "tea" → "aet"

            map.computeIfAbsent(key, k -> new java.util.ArrayList<>()).add(word);
        }

        return new java.util.ArrayList<>(map.values());
    }

    /**
     * PROBLEM 3: Longest Consecutive Sequence
     * Find the length of the longest sequence of consecutive integers.
     * Input: [100, 4, 200, 1, 3, 2] → 4  (sequence: 1,2,3,4)
     *
     * Key insight: Only START counting from a sequence's beginning.
     * A number n is a start if (n-1) is NOT in the set.
     * Time: O(n), Space: O(n)
     */
    public static int longestConsecutive(int[] nums) {
        java.util.HashSet<Integer> set = new java.util.HashSet<>();
        for (int n : nums) set.add(n);

        int longest = 0;

        for (int n : set) {
            if (!set.contains(n - 1)) { // n is the start of a sequence
                int length = 1;
                while (set.contains(n + length)) length++;
                longest = Math.max(longest, length);
            }
        }
        return longest;
    }

    // ──────────────────────────────────────────────
    // DEMO
    // ──────────────────────────────────────────────

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║        HASH TABLE DEMO           ║");
        System.out.println("╚══════════════════════════════════╝\n");

        HashTable<String, Integer> table = new HashTable<>();
        table.put("Alice", 30);
        table.put("Bob", 25);
        table.put("Charlie", 35);
        table.put("Diana", 28);
        System.out.println("After puts: " + table);

        System.out.println("get('Bob')     = " + table.get("Bob"));
        System.out.println("get('Eve')     = " + table.get("Eve"));  // null
        System.out.println("containsKey?   " + table.containsKey("Alice"));

        table.put("Alice", 31); // Update
        System.out.println("After update Alice→31: " + table);

        table.remove("Bob");
        System.out.println("After remove Bob: " + table);

        table.printBuckets();

        System.out.println("\n--- Two Sum ---");
        int[] nums = {2, 7, 11, 15};
        int[] result = twoSum(nums, 9);
        System.out.println("twoSum([2,7,11,15], 9) = [" + result[0] + ", " + result[1] + "]");

        System.out.println("\n--- Group Anagrams ---");
        String[] words = {"eat", "tea", "tan", "ate", "nat", "bat"};
        System.out.println("groupAnagrams: " + groupAnagrams(words));

        System.out.println("\n--- Longest Consecutive Sequence ---");
        int[] seq = {100, 4, 200, 1, 3, 2};
        System.out.println("longestConsecutive([100,4,200,1,3,2]) = " + longestConsecutive(seq));
    }
}
