package com.datastructures.array;

/**
 * ============================================================
 * DATA STRUCTURE: Dynamic Array (like Java's ArrayList)
 * ============================================================

 * CONCEPT:
 *   A dynamic array starts with a fixed capacity, and when it
 *   runs out of space, it automatically grows by allocating a
 *   new, larger array and copying all elements over.
 *   This gives us the best of both worlds: O(1) access AND
 *   unlimited growth (within memory limits).

 * THE GROWTH TRICK (Amortized Analysis):
 *   When full, we double the capacity (e.g., 4 → 8 → 16 → 32).
 *   Doubling means we copy N elements, but we won't need to copy
 *   again for another N adds. So the "average" cost per add
 *   (amortized) is still O(1).

 *   Copies timeline for capacity-doubling strategy:
 *   N=1:  copy 1    (total: 1)
 *   N=2:  copy 2    (total: 3)
 *   N=4:  copy 4    (total: 7)
 *   N=8:  copy 8    (total: 15)
 *   For N total adds → at most 2N copies → amortized O(1) per add

 * REAL-WORLD USE CASES:
 *   - Java's ArrayList, Python's list, C++ std::vector
 *   - Shopping carts, playlists, dynamic tables
 *   - Any list whose size you don't know in advance

 * TIME COMPLEXITY:
 *   Access by index : O(1)
 *   Add at end      : O(1) amortized  ← key advantage
 *   Add at index    : O(n)
 *   Remove at index : O(n)
 *   Search          : O(n)
 *   Resize (rare)   : O(n)

 * SPACE COMPLEXITY: O(n) — up to 2x wasted space after a resize

 * INTERVIEW TIPS:
 *   - Know the difference between size (elements stored) vs
 *     capacity (total allocated slots)
 *   - Interviewers love asking "why double instead of +1?"
 *     Answer: doubling gives O(1) amortized; +1 gives O(n²)
 * ============================================================
 */
@SuppressWarnings("unchecked")
public class DynamicArray<T> {

    private static final int DEFAULT_CAPACITY = 4; // Start small

    private Object[] data;    // Internal storage (Object[] to support generics)
    private int size;         // Logical size (number of elements)

    public DynamicArray() {
        this.data = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public DynamicArray(int initialCapacity) {
        if (initialCapacity <= 0) throw new IllegalArgumentException("Capacity must be > 0");
        this.data = new Object[initialCapacity];
        this.size = 0;
    }

    // ──────────────────────────────────────────────
    // CORE OPERATIONS
    // ──────────────────────────────────────────────

    /**
     * Adds an element at the end.
     * Time: O(1) amortized — resize is rare.
     */
    public void add(T value) {
        ensureCapacity();            // Grow the array if needed
        data[size] = value;          // Place at next available slot
        size++;
    }

    /**
     * Inserts an element at a specific index.
     * Time: O(n) — must shift elements right.
     */
    public void add(int index, T value) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index);
        ensureCapacity();

        // Shift elements from index to end, one step right
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
        }
        data[index] = value;
        size++;
    }

    /**
     * Returns the element at index.
     * Time: O(1)
     */
    public T get(int index) {
        validateIndex(index);
        return (T) data[index];
    }

    /**
     * Replaces the element at index and returns the old value.
     * Time: O(1)
     */
    public T set(int index, T value) {
        validateIndex(index);
        T old = (T) data[index];
        data[index] = value;
        return old;
    }

    /**
     * Removes the element at index and returns it.
     * Time: O(n) — must shift elements left.
     */
    public T remove(int index) {
        validateIndex(index);
        T removed = (T) data[index];

        // Shift elements from index+1 to end, one step left
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }

        size--;
        data[size] = null; // Prevent memory leak (let GC collect it)

        // Optional: shrink when array is only 25% full
        // This keeps memory usage bounded
        if (size > 0 && size == data.length / 4) {
            shrink();
        }

        return removed;
    }

    /**
     * Returns true if the list contains the specified element.
     * Time: O(n) — linear scan.
     */
    public boolean contains(T value) {
        return indexOf(value) != -1;
    }

    /**
     * Returns the index of the first occurrence, or -1 if not found.
     * Time: O(n)
     */
    public int indexOf(T value) {
        for (int i = 0; i < size; i++) {
            if (value == null ? data[i] == null : value.equals(data[i])) {
                return i;
            }
        }
        return -1;
    }

    // ──────────────────────────────────────────────
    // GROWTH & SHRINK LOGIC
    // ──────────────────────────────────────────────

    /**
     * Ensures there is at least one free slot.
     * If not, DOUBLES the capacity and copies all elements.

     * Why double? → Amortized O(1) per add.
     * If we grew by 1 each time: N adds = 1+2+3+...+N = O(N²) total.
     * If we double each time:    N adds ≈ 2N copies total = O(N).
     */
    private void ensureCapacity() {
        if (size < data.length) return; // Still room — do nothing

        int newCapacity = data.length * 2;
        resize(newCapacity);
        System.out.println("  [DynamicArray] Grew from " + data.length/2 + " → " + newCapacity);
    }

    /**
     * Shrinks internal array to half its size to reclaim memory.
     * Triggered when the array is only 25% full after a remove.
     */
    private void shrink() {
        int newCapacity = data.length / 2;
        resize(newCapacity);
        System.out.println("  [DynamicArray] Shrunk to capacity " + newCapacity);
    }

    /**
     * Creates a new array with newCapacity, copies all data, replaces old array.
     * This is the actual O(n) work during resize.
     */
    private void resize(int newCapacity) {
        Object[] newData = new Object[newCapacity];
        // System.arraycopy is faster than a manual loop (uses native memory copy)
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
    }

    // ──────────────────────────────────────────────
    // UTILITY
    // ──────────────────────────────────────────────

    public int size() { return size; }
    public int capacity() { return data.length; }
    public boolean isEmpty() { return size == 0; }

    public void clear() {
        // Null out all refs for GC, reset size
        for (int i = 0; i < size; i++) data[i] = null;
        size = 0;
    }

    private void validateIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                "Index: " + index + ", Size: " + size
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
        System.out.println("║       DYNAMIC ARRAY DEMO         ║");
        System.out.println("╚══════════════════════════════════╝\n");

        DynamicArray<Integer> list = new DynamicArray<>(2); // Start tiny (capacity 2)
        System.out.println("Initial capacity: " + list.capacity());

        System.out.println("\n--- Adding 8 elements (watch it grow!) ---");
        for (int i = 1; i <= 8; i++) {
            list.add(i * 10);
            System.out.println("  Added " + (i*10) + " → size=" + list.size()
                + ", capacity=" + list.capacity() + " → " + list);
        }

        System.out.println("\n--- Insert at index 3 ---");
        list.add(3, 999);
        System.out.println("After add(3,999): " + list);

        System.out.println("\n--- Remove at index 3 ---");
        int removed = list.remove(3);
        System.out.println("Removed: " + removed + " → " + list);

        System.out.println("\n--- Contains checks ---");
        System.out.println("Contains 50? " + list.contains(50));
        System.out.println("Contains 99? " + list.contains(99));
        System.out.println("Index of 60? " + list.indexOf(60));

        System.out.println("\n--- Generic usage with Strings ---");
        DynamicArray<String> names = new DynamicArray<>();
        names.add("Alice"); names.add("Bob"); names.add("Charlie");
        System.out.println("Names: " + names);
        names.remove(1);
        System.out.println("After remove(1): " + names);
    }
}
