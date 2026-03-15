package com.datastructures.heap;

/**
 * ============================================================
 * DATA STRUCTURE: Binary Heap (Min Heap & Max Heap)
 * ============================================================
 *
 * CONCEPT:
 *   A heap is a COMPLETE BINARY TREE that satisfies the HEAP PROPERTY:
 *
 *   Min Heap: Parent ≤ Children (smallest element at root)
 *   Max Heap: Parent ≥ Children (largest element at root)
 *
 *   "Complete" means all levels are fully filled, except possibly
 *   the last level, which is filled left to right.
 *
 *   Min Heap example:
 *          [1]
 *         /   \
 *       [3]   [2]
 *      /   \  /
 *    [7]  [4][5]
 *
 *   ARRAY REPRESENTATION (no pointers needed!):
 *   The complete binary tree maps perfectly to an array:
 *
 *   Index:   0    1    2    3    4    5
 *   Value:  [1]  [3]  [2]  [7]  [4]  [5]
 *
 *   For node at index i:
 *     Parent:      (i - 1) / 2
 *     Left child:  2 * i + 1
 *     Right child: 2 * i + 2
 *
 *   Check: parent of index 3 = (3-1)/2 = 1 ✓ (data[1]=3 is parent of data[3]=7)
 *
 * REAL-WORLD USE CASES:
 *   - Priority queues (OS task scheduling by priority)
 *   - Dijkstra's shortest path algorithm
 *   - Heap sort (O(n log n), in-place)
 *   - Finding top-K elements
 *   - Median maintenance (two heaps trick)
 *
 * TIME COMPLEXITY:
 *   Insert (offer)   : O(log n) — bubble up
 *   Remove min/max   : O(log n) — bubble down (heapify)
 *   Peek min/max     : O(1) ← always the root
 *   Build heap (heapify entire array): O(n) — not O(n log n)!
 *   Search           : O(n) — heaps aren't sorted
 *
 * SPACE COMPLEXITY: O(n)
 *
 * INTERVIEW TIPS:
 *   - "Find top K largest" → Min Heap of size K (counterintuitive!)
 *   - "Find top K smallest" → Max Heap of size K
 *   - Median of a stream → two heaps (max-heap left, min-heap right)
 *   - Java's PriorityQueue is a min heap by default
 *   - For max heap: PriorityQueue<>(Collections.reverseOrder())
 * ============================================================
 */
public class Heap {

    // ══════════════════════════════════════════════
    // MIN HEAP
    // ══════════════════════════════════════════════

    public static class MinHeap {
        private int[] data;
        private int size;

        public MinHeap(int capacity) {
            data = new int[capacity];
            size = 0;
        }

        // ──────────────────────────────────────────────
        // INDEX HELPERS
        // ──────────────────────────────────────────────

        private int parent(int i)     { return (i - 1) / 2; }
        private int leftChild(int i)  { return 2 * i + 1; }
        private int rightChild(int i) { return 2 * i + 2; }
        private boolean hasLeft(int i)  { return leftChild(i) < size; }
        private boolean hasRight(int i) { return rightChild(i) < size; }

        private void swap(int i, int j) {
            int tmp = data[i]; data[i] = data[j]; data[j] = tmp;
        }

        // ──────────────────────────────────────────────
        // CORE OPERATIONS
        // ──────────────────────────────────────────────

        /**
         * OFFER (INSERT): Add element, then restore heap property.
         * Time: O(log n)
         *
         * Strategy: Insert at end, then BUBBLE UP.
         *
         * Insert 1 into [2, 3, 4, 7, 5]:
         * Step 1: [2, 3, 4, 7, 5, 1]   (append at index 5)
         * Step 2: parent(5) = 2, data[2]=4 > 1 → swap
         *         [2, 3, 1, 7, 5, 4]
         * Step 3: parent(2) = 0, data[0]=2 > 1 → swap
         *         [1, 3, 2, 7, 5, 4]
         * Now root=1, heap property restored!
         */
        public void offer(int value) {
            if (size == data.length) throw new IllegalStateException("Heap is full");
            data[size] = value;  // Add at the end
            size++;
            bubbleUp(size - 1);  // Restore heap property
        }

        /**
         * Bubble Up: Move the newly inserted element up until
         * the heap property is satisfied (parent ≤ current for min heap).
         */
        private void bubbleUp(int i) {
            // While we're not the root AND parent is GREATER (violates min heap)
            while (i > 0 && data[parent(i)] > data[i]) {
                swap(i, parent(i)); // Swap with parent
                i = parent(i);      // Move up to parent's position
            }
        }

        /**
         * POLL (REMOVE MIN): Remove and return the smallest element.
         * Time: O(log n)
         *
         * The minimum is always at data[0].
         * But we can't just remove it — we'd break the array structure.
         *
         * Strategy:
         * 1. Swap root with the LAST element (easy to remove)
         * 2. Decrease size (logically remove the last element)
         * 3. BUBBLE DOWN the new root until heap property is restored
         *
         * Poll from [1, 3, 2, 7, 5, 4]:
         * Step 1: Swap root with last → [4, 3, 2, 7, 5, 1]  remove 1
         * Step 2: size-- → effective array: [4, 3, 2, 7, 5]
         * Step 3: Bubble down 4:
         *   Children of 4 (index 0): left=3(idx1), right=2(idx2). Min child=2.
         *   4 > 2 → swap → [2, 3, 4, 7, 5]
         *   Children of 4 (now index 2): left=7(idx5)→out of bounds
         *   Done! [2, 3, 4, 7, 5] ← valid min heap
         */
        public int poll() {
            if (isEmpty()) throw new IllegalStateException("Heap is empty");
            int min = data[0];           // Save the minimum (at root)
            data[0] = data[size - 1];    // Move last element to root
            size--;
            bubbleDown(0);               // Restore heap property from root down
            return min;
        }

        /**
         * Bubble Down (Heapify Down): Move the root element down
         * until the heap property is satisfied.
         *
         * At each step, swap with the SMALLER child (to maintain min heap).
         */
        private void bubbleDown(int i) {
            while (hasLeft(i)) { // While there is at least a left child
                int smallest = i;     // Assume current is smallest

                // Find the smallest among node, left child, right child
                if (data[leftChild(i)] < data[smallest]) {
                    smallest = leftChild(i);
                }
                if (hasRight(i) && data[rightChild(i)] < data[smallest]) {
                    smallest = rightChild(i);
                }

                if (smallest == i) break; // Already in the right place!

                swap(i, smallest);
                i = smallest; // Move down to the child we swapped with
            }
        }

        /** PEEK: Look at minimum without removing. O(1) */
        public int peek() {
            if (isEmpty()) throw new IllegalStateException("Heap is empty");
            return data[0];
        }

        public int size() { return size; }
        public boolean isEmpty() { return size == 0; }

        /**
         * BUILD HEAP from an existing array.
         * Naive: insert elements one by one → O(n log n)
         * Optimal: heapify from bottom up → O(n)!
         *
         * Why O(n)? Most nodes are at the bottom (small height),
         * so most bubble-down operations are short. The math works out to O(n).
         */
        public static MinHeap buildHeap(int[] arr) {
            MinHeap heap = new MinHeap(arr.length);
            System.arraycopy(arr, 0, heap.data, 0, arr.length);
            heap.size = arr.length;

            // Start from the last non-leaf node and heapify downward
            // Last non-leaf is at index (size/2 - 1)
            for (int i = heap.size / 2 - 1; i >= 0; i--) {
                heap.bubbleDown(i);
            }
            return heap;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("MinHeap[");
            for (int i = 0; i < size; i++) {
                sb.append(data[i]);
                if (i < size - 1) sb.append(", ");
            }
            return sb.append("]").toString();
        }
    }

    // ══════════════════════════════════════════════
    // MAX HEAP
    // ══════════════════════════════════════════════

    public static class MaxHeap {
        private int[] data;
        private int size;

        public MaxHeap(int capacity) {
            data = new int[capacity];
            size = 0;
        }

        private int parent(int i)    { return (i - 1) / 2; }
        private int left(int i)      { return 2 * i + 1; }
        private int right(int i)     { return 2 * i + 2; }
        private void swap(int i, int j) {
            int tmp = data[i]; data[i] = data[j]; data[j] = tmp;
        }

        public void offer(int value) {
            if (size == data.length) throw new IllegalStateException("Heap is full");
            data[size++] = value;
            bubbleUp(size - 1);
        }

        /** Bubble Up for MAX heap: parent must be GREATER than child. */
        private void bubbleUp(int i) {
            while (i > 0 && data[parent(i)] < data[i]) { // < instead of >
                swap(i, parent(i));
                i = parent(i);
            }
        }

        public int poll() {
            if (isEmpty()) throw new IllegalStateException("Heap is empty");
            int max = data[0];
            data[0] = data[--size];
            bubbleDown(0);
            return max;
        }

        /** Bubble Down for MAX heap: swap with LARGER child. */
        private void bubbleDown(int i) {
            while (left(i) < size) {
                int largest = i;
                if (data[left(i)] > data[largest]) largest = left(i);
                if (right(i) < size && data[right(i)] > data[largest]) largest = right(i);
                if (largest == i) break;
                swap(i, largest);
                i = largest;
            }
        }

        public int peek() {
            if (isEmpty()) throw new IllegalStateException("Heap is empty");
            return data[0];
        }

        public int size() { return size; }
        public boolean isEmpty() { return size == 0; }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("MaxHeap[");
            for (int i = 0; i < size; i++) {
                sb.append(data[i]);
                if (i < size - 1) sb.append(", ");
            }
            return sb.append("]").toString();
        }
    }

    // ══════════════════════════════════════════════
    // CLASSIC INTERVIEW PROBLEMS USING HEAPS
    // ══════════════════════════════════════════════

    /**
     * PROBLEM 1: Top K Largest Elements
     * Find the k largest elements from an array.
     * Input: nums=[3,2,1,5,6,4], k=2 → [5,6]
     *
     * Approach: MIN heap of size k.
     * Keep adding elements; when size > k, remove the MINIMUM.
     * At the end, the heap contains the k LARGEST elements.
     *
     * Why min heap? Because when we remove, we discard the SMALLEST
     * of our current candidates, keeping the largest k.
     *
     * Time: O(n log k), Space: O(k)
     */
    public static int[] topKLargest(int[] nums, int k) {
        MinHeap minHeap = new MinHeap(k + 1);

        for (int num : nums) {
            minHeap.offer(num);
            if (minHeap.size() > k) {
                minHeap.poll(); // Remove the smallest — it's not in top-k
            }
        }

        // Drain the heap into a result array
        int[] result = new int[k];
        for (int i = k - 1; i >= 0; i--) {
            result[i] = minHeap.poll();
        }
        return result;
    }

    /**
     * PROBLEM 2: Kth Largest Element in an Array
     * Input: nums=[3,2,1,5,6,4], k=2 → 5
     * Time: O(n log k), Space: O(k)
     * (Same as Top K, just return the heap's top after processing all)
     */
    public static int kthLargest(int[] nums, int k) {
        MinHeap minHeap = new MinHeap(k + 1);
        for (int num : nums) {
            minHeap.offer(num);
            if (minHeap.size() > k) minHeap.poll();
        }
        return minHeap.peek(); // Root of min heap = k-th largest
    }

    /**
     * PROBLEM 3: Find Median from Data Stream
     * Continuously find the median as numbers are added.
     *
     * Approach: Two heaps partitioned around the median.
     *   maxHeap: holds the LOWER half (top = max of lower half)
     *   minHeap: holds the UPPER half (top = min of upper half)
     *
     *   Invariant: maxHeap.size() == minHeap.size()  (even total)
     *          OR  maxHeap.size() == minHeap.size() + 1 (odd total)
     *
     *   Median = maxHeap.peek() if odd count
     *          = avg(maxHeap.peek(), minHeap.peek()) if even count
     *
     *   Example: stream = [1, 2, 3, 4, 5]
     *   After [1]: max=[1], min=[]         → median=1
     *   After [2]: max=[1], min=[2]        → median=1.5
     *   After [3]: max=[1,2](as max), min=[3] → median=2
     *   ...
     */
    public static class MedianFinder {
        private final MaxHeap lowerHalf; // Left part (smaller values)
        private final MinHeap upperHalf; // Right part (larger values)

        public MedianFinder() {
            lowerHalf = new MaxHeap(1000);
            upperHalf = new MinHeap(1000);
        }

        public void addNum(int num) {
            // Step 1: Add to lower half (max heap)
            lowerHalf.offer(num);

            // Step 2: Balance — largest of lower must be ≤ smallest of upper
            if (!upperHalf.isEmpty() && lowerHalf.peek() > upperHalf.peek()) {
                upperHalf.offer(lowerHalf.poll()); // Move to upper half
            }

            // Step 3: Rebalance sizes (allow lower to have at most 1 extra)
            if (lowerHalf.size() > upperHalf.size() + 1) {
                upperHalf.offer(lowerHalf.poll());
            } else if (upperHalf.size() > lowerHalf.size()) {
                lowerHalf.offer(upperHalf.poll());
            }
        }

        public double findMedian() {
            if (lowerHalf.size() == upperHalf.size()) {
                return (lowerHalf.peek() + upperHalf.peek()) / 2.0;
            }
            return lowerHalf.peek(); // lowerHalf always has the extra element
        }
    }

    // ──────────────────────────────────────────────
    // DEMO
    // ──────────────────────────────────────────────

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║           HEAP DEMO              ║");
        System.out.println("╚══════════════════════════════════╝\n");

        // Min Heap
        System.out.println("--- Min Heap ---");
        MinHeap minH = new MinHeap(10);
        for (int v : new int[]{5, 3, 8, 1, 4, 2}) {
            minH.offer(v);
            System.out.println("  offer(" + v + ") → " + minH + " | peek=" + minH.peek());
        }
        System.out.println("Polling in order: ");
        while (!minH.isEmpty()) System.out.print(minH.poll() + " "); // Sorted!
        System.out.println();

        // Max Heap
        System.out.println("\n--- Max Heap ---");
        MaxHeap maxH = new MaxHeap(10);
        for (int v : new int[]{5, 3, 8, 1, 4, 2}) maxH.offer(v);
        System.out.println("Max heap: " + maxH + " | peek=" + maxH.peek());
        System.out.print("Polling in order: ");
        while (!maxH.isEmpty()) System.out.print(maxH.poll() + " "); // Reverse sorted!
        System.out.println();

        // Build Heap in O(n)
        System.out.println("\n--- Build Heap O(n) ---");
        int[] arr = {7, 3, 5, 1, 9, 2, 6};
        MinHeap built = MinHeap.buildHeap(arr);
        System.out.println("Built from [7,3,5,1,9,2,6]: " + built);

        // Top K Largest
        System.out.println("\n--- Top K Largest ---");
        int[] nums = {3, 2, 1, 5, 6, 4, 8, 7};
        int[] top3 = topKLargest(nums, 3);
        System.out.print("Top 3 largest from [3,2,1,5,6,4,8,7]: ");
        for (int v : top3) System.out.print(v + " ");
        System.out.println();

        System.out.println("Kth largest (k=2): " + kthLargest(nums, 2)); // 7

        // Median Finder
        System.out.println("\n--- Median Finder (Stream) ---");
        MedianFinder mf = new MedianFinder();
        int[] stream = {1, 2, 3, 4, 5};
        for (int v : stream) {
            mf.addNum(v);
            System.out.println("  Added " + v + " → median=" + mf.findMedian());
        }
    }
}
