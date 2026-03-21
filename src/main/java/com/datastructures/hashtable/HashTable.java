package com.datastructures.hashtable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * A hash table stores key–value pairs. A hash function turns each key into an array index so you can jump
 * straight to the right bucket. Collisions (two keys mapping to the same bucket) are handled by chaining:
 * each bucket is a short linked list of entries.
 */
public class HashTable<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;
    /** Clears the sign bit so modulo always gives a non-negative bucket index. */
    private static final int NON_NEGATIVE_MASK = 0x7FFFFFFF;

    private static class Entry<K, V> {
        final K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private Entry<K, V>[] buckets;
    private int entryCount;
    private int bucketCount;

    @SuppressWarnings("unchecked")
    public HashTable() {
        this.bucketCount = DEFAULT_CAPACITY;
        this.buckets = new Entry[bucketCount];
        this.entryCount = 0;
    }

    /**
     * Maps a key to a bucket index: hash code, optional mixing, then {@code % bucketCount}.
     */
    private int getBucketIndex(K key) {
        if (key == null) {
            return 0;
        }

        int hash = key.hashCode();
        hash = hash ^ (hash >>> 16);
        return (hash & NON_NEGATIVE_MASK) % bucketCount;
    }

    /** Inserts a new pair or updates the value if the key already exists. Rehashes when the table gets too full. */
    public void put(K key, V value) {
        int bucketIndex = getBucketIndex(key);
        Entry<K, V> head = buckets[bucketIndex];

        Entry<K, V> current = head;
        while (current != null) {
            if (keysEqual(current.key, key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.next = head;
        buckets[bucketIndex] = newEntry;
        entryCount++;

        double loadFactor = (double) entryCount / bucketCount;
        if (loadFactor > LOAD_FACTOR_THRESHOLD) {
            rehash();
        }
    }

    /** Returns the value for {@code key}, or null if the key is missing. */
    public V get(K key) {
        int bucketIndex = getBucketIndex(key);
        Entry<K, V> current = buckets[bucketIndex];

        while (current != null) {
            if (keysEqual(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    /** Removes the key and returns its value, or null if it was not present. */
    public V remove(K key) {
        int bucketIndex = getBucketIndex(key);
        Entry<K, V> current = buckets[bucketIndex];
        Entry<K, V> previous = null;

        while (current != null) {
            if (keysEqual(current.key, key)) {
                if (previous == null) {
                    buckets[bucketIndex] = current.next;
                } else {
                    previous.next = current.next;
                }
                entryCount--;
                return current.value;
            }
            previous = current;
            current = current.next;
        }
        return null;
    }

    public boolean containsKey(K key) {
        int bucketIndex = getBucketIndex(key);
        Entry<K, V> current = buckets[bucketIndex];
        while (current != null) {
            if (keysEqual(current.key, key)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Doubles the number of buckets and puts every entry back using fresh bucket indices.
     */
    @SuppressWarnings("unchecked")
    private void rehash() {
        int newBucketCount = bucketCount * 2;
        Entry<K, V>[] newBuckets = new Entry[newBucketCount];

        Entry<K, V>[] oldBuckets = buckets;
        int oldBucketCount = bucketCount;

        bucketCount = newBucketCount;
        buckets = newBuckets;

        for (int bucketIndex = 0; bucketIndex < oldBucketCount; bucketIndex++) {
            Entry<K, V> current = oldBuckets[bucketIndex];
            while (current != null) {
                Entry<K, V> next = current.next;
                int newIndex = getBucketIndex(current.key);
                current.next = newBuckets[newIndex];
                newBuckets[newIndex] = current;
                current = next;
            }
        }
    }

    private boolean keysEqual(K first, K second) {
        if (first == null) {
            return second == null;
        }
        return first.equals(second);
    }

    public int size() {
        return entryCount;
    }

    public boolean isEmpty() {
        return entryCount == 0;
    }

    /** Prints non-empty buckets so you can see chaining. */
    public void printBuckets() {
        System.out.println("Buckets (count=" + bucketCount + ", entries=" + entryCount + "):");
        for (int bucketIndex = 0; bucketIndex < bucketCount; bucketIndex++) {
            if (buckets[bucketIndex] == null) {
                continue;
            }
            System.out.print("  [" + bucketIndex + "]: ");
            Entry<K, V> current = buckets[bucketIndex];
            while (current != null) {
                System.out.print(current.key + "=" + current.value);
                if (current.next != null) {
                    System.out.print(" -> ");
                }
                current = current.next;
            }
            System.out.println();
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        boolean firstPair = true;
        for (int bucketIndex = 0; bucketIndex < bucketCount; bucketIndex++) {
            Entry<K, V> current = buckets[bucketIndex];
            while (current != null) {
                if (!firstPair) {
                    builder.append(", ");
                }
                builder.append(current.key).append("=").append(current.value);
                firstPair = false;
                current = current.next;
            }
        }
        return builder.append("}").toString();
    }

    /** Classic pattern: for each number, check whether (target - number) was seen already. */
    public static int[] twoSum(int[] numbers, int target) {
        HashMap<Integer, Integer> indexByValue = new HashMap<>();

        for (int index = 0; index < numbers.length; index++) {
            int value = numbers[index];
            int complement = target - value;
            if (indexByValue.containsKey(complement)) {
                int earlierIndex = indexByValue.get(complement);
                return new int[]{earlierIndex, index};
            }
            indexByValue.put(value, index);
        }
        return new int[]{-1, -1};
    }

    /** Anagrams sort to the same letter sequence; group words that share that sorted key. */
    public static List<List<String>> groupAnagrams(String[] words) {
        HashMap<String, List<String>> groups = new HashMap<>();

        for (int index = 0; index < words.length; index++) {
            String word = words[index];
            char[] letters = word.toCharArray();
            Arrays.sort(letters);
            String key = new String(letters);

            if (!groups.containsKey(key)) {
                groups.put(key, new ArrayList<>());
            }
            groups.get(key).add(word);
        }

        return new ArrayList<>(groups.values());
    }

    /**
     * Longest run of consecutive integers: put all numbers in a set, then only start counting from a true start
     * (a number whose predecessor is not in the set).
     */
    public static int longestConsecutive(int[] numbers) {
        HashSet<Integer> set = new HashSet<>();
        for (int index = 0; index < numbers.length; index++) {
            set.add(numbers[index]);
        }

        int bestLength = 0;
        for (int value : set) {
            if (set.contains(value - 1)) {
                continue;
            }
            int length = 1;
            while (set.contains(value + length)) {
                length++;
            }
            if (length > bestLength) {
                bestLength = length;
            }
        }
        return bestLength;
    }

    public static void main(String[] args) {
        System.out.println("--- Hash table demo ---");

        HashTable<String, Integer> table = new HashTable<>();
        table.put("Alice", 30);
        table.put("Bob", 25);
        table.put("Charlie", 35);
        table.put("Diana", 28);
        System.out.println("After puts: " + table);

        System.out.println("get(\"Bob\") -> " + table.get("Bob"));
        System.out.println("get(\"Eve\") -> " + table.get("Eve"));
        System.out.println("containsKey(\"Alice\") -> " + table.containsKey("Alice"));

        table.put("Alice", 31);
        System.out.println("Put Alice again (update age): " + table);

        table.remove("Bob");
        System.out.println("After remove Bob: " + table);
        table.printBuckets();

        System.out.println();
        int[] nums = {2, 7, 11, 15};
        int[] pair = twoSum(nums, 9);
        System.out.println("Two sum indices for target 9 -> [" + pair[0] + ", " + pair[1] + "]");

        System.out.println();
        String[] words = {"eat", "tea", "tan", "ate", "nat", "bat"};
        System.out.println("Group anagrams -> " + groupAnagrams(words));

        System.out.println();
        int[] seq = {100, 4, 200, 1, 3, 2};
        System.out.println("Longest consecutive run length -> " + longestConsecutive(seq));
    }
}
