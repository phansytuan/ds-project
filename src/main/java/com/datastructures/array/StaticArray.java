package com.datastructures.array;

/**
 * ============================================================
 * DATA STRUCTURE: Static Array
 * ============================================================

 * CONCEPT:
 *   An array is the most fundamental data structure — a contiguous
 *   block of memory where elements are stored sequentially.
 *   Each element is accessed in O(1) time via its index.

 * HOW IT WORKS IN MEMORY:
 *   Index:  [0]  [1]  [2]  [3]  [4]
 *   Value:  [10] [20] [30] [40] [50]
 *   Memory: 100  104  108  112  116   (4 bytes per int)
 *   address[i] = base_address + (i * element_size)

 * REAL-WORLD USE CASES:
 *   - Storing a fixed list of items (days of the week, RGB pixels)
 *   - Image processing (pixel matrices)
 *   - Lookup tables and caches
 *   - Building blocks for other data structures (heap, hash table)

 * TIME COMPLEXITY:
 *   Access by index : O(1)  ← best feature of arrays
 *   Search (linear) : O(n)
 *   Search (binary) : O(log n) — only if sorted
 *   Insert at end   : O(1)  (if space available)
 *   Insert at index : O(n)  (must shift elements right)
 *   Delete at index : O(n)  (must shift elements left)
 *
 * SPACE COMPLEXITY: O(n)
 *
 * INTERVIEW TIPS:
 *   - Always clarify if the array is sorted (enables binary search)
 *   - Common patterns: two pointers, sliding window, prefix sum
 *   - Watch for off-by-one errors on index bounds
 * ============================================================
 */
public class StaticArray {

    private int[] data;   // The underlying fixed-size array
    private int size;     // Number of elements currently stored

    /**
     * Constructor: allocates a fixed-size array.
     * @param capacity Maximum number of elements this array can hold.
     */
    public StaticArray(int capacity) {
        this.data = new int[capacity];
        this.size = 0;
    }

    // ──────────────────────────────────────────────
    // CORE OPERATIONS
    // ──────────────────────────────────────────────

    /**
     * Adds an element at the end of the array.
     * Time: O(1) — we know exactly where the next slot is.
     */
    public void add(int value) {
        if (size >= data.length) {
            throw new IllegalStateException("Array is full! Capacity: " + data.length);
        }
        data[size] = value;  // Place value at current end
        size++;              // Expand logical size
    }

    /**
     * Gets the element at a specific index.
     * Time: O(1) — direct memory address calculation.
     */
    public int get(int index) {
        validateIndex(index);
        return data[index];
    }

    /**
     * Updates the value at a specific index.
     * Time: O(1)
     */
    public void set(int index, int value) {
        validateIndex(index);
        data[index] = value;
    }

    /**
     * Inserts a value at a specific index, shifting elements right.
     * Time: O(n) — in the worst case, every element shifts right.
     *
     * Before: [10, 20, 40, 50]  insert 30 at index 2
     * After:  [10, 20, 30, 40, 50]
     */
    public void insertAt(int index, int value) {
        if (size >= data.length) throw new IllegalStateException("Array is full!");
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index);

        // Shift elements from the end to 'index' one position to the right
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
        }

        data[index] = value;
        size++;
    }

    /**
     * Removes the element at a specific index, shifting elements left.
     * Time: O(n)
     *
     * Before: [10, 20, 30, 40, 50]  remove at index 2
     * After:  [10, 20, 40, 50]
     */
    public int removeAt(int index) {
        validateIndex(index);
        int removed = data[index];

        // Shift elements from 'index+1' to end one position left
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }

        size--;
        data[size] = 0; // Optional: clear the ghost element
        return removed;
    }

    /**
     * Linear search — scan every element.
     * Time: O(n)
     * @return index of value, or -1 if not found
     */
    public int linearSearch(int value) {
        for (int i = 0; i < size; i++) {
            if (data[i] == value) return i;
        }
        return -1;
    }

    /**
     * Binary search — REQUIRES the array to be sorted.
     * Time: O(log n) — halves the search space each step.
     *
     * Visual: searching for 30 in [10, 20, 30, 40, 50]
     *   Step 1: mid = 2, data[2] = 30 → FOUND!
     */
    public int binarySearch(int value) {
        int left = 0;
        int right = size - 1;

        while (left <= right) {
            // Use (left + right) / 2 is safe for small arrays,
            // but left + (right - left) / 2 avoids integer overflow
            int mid = left + (right - left) / 2;

            if (data[mid] == value) {
                return mid;             // Found it!
            } else if (data[mid] < value) {
                left = mid + 1;         // Search the right half
            } else {
                right = mid - 1;        // Search the left half
            }
        }
        return -1; // Not found
    }

    /**
     * Reverses the array in-place using two pointers.
     * Time: O(n), Space: O(1) — no extra array needed.
     *
     * [1, 2, 3, 4, 5]
     *  ↑           ↑   swap → [5, 2, 3, 4, 1]
     *     ↑     ↑       swap → [5, 4, 3, 2, 1]
     *        ↑          mid — done
     */
    public void reverse() {
        int left = 0;
        int right = size - 1;

        while (left < right) {
            // Swap data[left] and data[right]
            int temp = data[left];
            data[left] = data[right];
            data[right] = temp;

            left++;
            right--;
        }
    }

    // ──────────────────────────────────────────────
    // UTILITY
    // ──────────────────────────────────────────────

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    private void validateIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                "Index " + index + " out of bounds for size " + size
            );
        }
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    // ──────────────────────────────────────────────
    // DEMO
    // ──────────────────────────────────────────────

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║       STATIC ARRAY DEMO          ║");
        System.out.println("╚══════════════════════════════════╝\n");

        StaticArray arr = new StaticArray(10);

        // Add elements
        arr.add(10); arr.add(20); arr.add(30); arr.add(40); arr.add(50);
        System.out.println("After adds:           " + arr);

        // Access
        System.out.println("Get index 2:          " + arr.get(2));

        // Insert in the middle
        arr.insertAt(2, 25);
        System.out.println("After insertAt(2,25): " + arr);

        // Remove
        int removed = arr.removeAt(3);
        System.out.println("After removeAt(3)=" + removed + ": " + arr);

        // Linear search
        System.out.println("LinearSearch(25):     index " + arr.linearSearch(25));

        // Binary search on sorted array
        System.out.println("BinarySearch(40):     index " + arr.binarySearch(40));

        // Reverse
        arr.reverse();
        System.out.println("After reverse:        " + arr);
    }
}
