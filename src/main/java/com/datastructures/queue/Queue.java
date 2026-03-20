package com.datastructures.queue;

/**
 * ============================================================
 * DATA STRUCTURE: Queue
 * ============================================================

 * CONCEPT:
 *   A queue is a FIFO (First In, First Out) data structure.
 *   Like a line at a coffee shop — first person in is the first person served.
 *   Elements are added at the REAR & removed from the FRONT.

 *   enqueue(x) → add to rear
 *   dequeue()  → remove from front
 *   peek()     → look at front without removing

 *   Visual:
 *   FRONT                        REAR
 *     │                            │
 *     ▼                            ▼
 *    [10] ← [20] ← [30] ← [40]    ← enqueue adds here
 *     ↑
 *   dequeue removes here

 * IMPLEMENTATION: Circular Array Queue
 *   Using a plain array with front / rear pointers that wrap around (modulo). 
 *   This avoids the O(n) shifting you'd need with a naive array-based queue.

 *   front=0, rear=3, size=4, capacity=6:
 *   Index: [0]  [1]  [2]  [3]  [4]  [5]
 *   Data:  [A]  [B]  [C]  [D]  [ ]  [ ]
 *           ↑              ↑
 *         front           rear

 *   After dequeue A and enqueue E:
 *   front=1, rear=4, size=4:
 *   Index: [0]  [1]  [2]  [3]  [4]  [5]
 *   Data:  [ ]  [B]  [C]  [D]  [E]  [ ]
 *                ↑              ↑
 *              front            rear

 * REAL-WORLD USE CASES:
 *   - BFS (Breadth-First Search) — most common queue use in interviews
 *   - Task schedulers, print spoolers
 *   - Message queues (Kafka, RabbitMQ concepts)
 *   - Handling HTTP request queues in a web server
 *   - Sliding window problems (monotonic deque)

 * TIME COMPLEXITY:
 *   Enqueue : O(1)
 *   Dequeue : O(1)
 *   Peek    : O(1)
 *   Search  : O(n)

 * SPACE COMPLEXITY: O(n)
 * ============================================================
 */
public class Queue<T> {

    private static final int DEFAULT_CAPACITY = 8;

    private Object[] data;
    private int front;    // Index of the front element
    private int rear;     // Index of the next empty slot (rear)
    private int size;     // Number of elements currently in the queue

    public Queue() {
        this.data = new Object[DEFAULT_CAPACITY];
        this.front = 0;
        this.rear = 0;
        this.size = 0;
    }

    // ──────────────────────────────────────────────
    // CORE OPERATIONS
    // ──────────────────────────────────────────────

    /**
     * ENQUEUE: Add element to the rear of the queue.
     * Time: O(1) amortized

     * We use modulo to wrap the rear pointer around the array.
     * rear = (rear + 1) % capacity
     */
    public void enqueue(T value) {
        if (size == data.length) {
            resize(); // Grow if full
        }
        data[rear] = value;
        rear = (rear + 1) % data.length;  // Wrap around (circular)
        size++;
    }

    /**
     * DEQUEUE: Remove & return the front element.
     * Time: O(1)
     */
    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) throw new IllegalStateException("Queue is empty!");
        T value = (T) data[front];
        data[front] = null;                  // Help GC
        front = (front + 1) % data.length;   // Advance front (wrap around)
        size--;
        return value;
    }

    /**
     * PEEK: Look at the front element without removing it.
     * Time: O(1)
     */
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) throw new IllegalStateException("Queue is empty!");
        return (T) data[front];
    }

    public boolean isEmpty() { return size == 0; }
    public int size() { return size; }

    /**
     * Resize: Allocate a new array (2x), copy elements in order starting from 'front', reset front and rear pointers.
     * This "unwinds" the circular layout into a fresh linear layout.
     */
    private void resize() {
        Object[] newData = new Object[data.length * 2];
        for (int i = 0; i < size; i++) {
            // Access elements in order, accounting for circular wrap
            newData[i] = data[(front + i) % data.length];
        }
        data = newData;
        front = 0;
        rear = size; // rear comes right after the last element
    }

    @Override
    public String toString() {
        if (isEmpty()) return "Queue []";
        StringBuilder sb = new StringBuilder("Queue [front->");
        for (int i = 0; i < size; i++) {
            sb.append(data[(front + i) % data.length]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("<-rear]").toString();
    }

    // ══════════════════════════════════════════════
    // DEQUE: Double-Ended Queue
    // (supports add / remove from BOTH ends)
    // ══════════════════════════════════════════════

    /**
     * A Deque (Double-Ended Queue) generalizes both Stack & Queue:
     *   - addFirst / removeFirst → Stack behavior
     *   - addLast  / removeLast  → Queue behavior

     * Real use: sliding window maximum problem (monotonic deque)
     */
    public static class Deque<T> {
        private Object[] data;
        private int front;
        private int rear;
        private int size;

        public Deque() {
            data  = new Object[8];
            front = 0;
            rear  = 0;
            size  = 0;
        }

        /** Add to front. */
        public void addFirst(T value) {
            if (size == data.length) grow();
            // Move front backward (with wrap-around)
            front = (front - 1 + data.length) % data.length;
            data[front] = value;
            size++;
        }

        /** Add to rear. */
        public void addLast(T value) {
            if (size == data.length) grow();
            data[rear] = value;
            rear = (rear + 1) % data.length;
            size++;
        }

        /** Remove from front. */
        @SuppressWarnings("unchecked")
        public T removeFirst() {
            if (isEmpty()) throw new IllegalStateException("Deque empty");
            T val = (T) data[front];
            data[front] = null;
            front = (front + 1) % data.length;
            size--;
            return val;
        }

        /** Remove from rear. */
        @SuppressWarnings("unchecked")
        public T removeLast() {
            if (isEmpty()) throw new IllegalStateException("Deque empty");
            rear = (rear - 1 + data.length) % data.length;
            T val = (T) data[rear];
            data[rear] = null;
            size--;
            return val;
        }

        @SuppressWarnings("unchecked")
        public T peekFirst() { return (T) data[front]; }

        @SuppressWarnings("unchecked")
        public T peekLast()  {
            return (T) data[(rear - 1 + data.length) % data.length];
        }

        public boolean isEmpty() { return size == 0; }
        public int size() { return size; }

        private void grow() {
            Object[] newData = new Object[data.length * 2];
            for (int i = 0; i < size; i++) {
                newData[i] = data[(front + i) % data.length];
            }
            data  = newData;
            front = 0;
            rear  = size;
        }

        @Override
        public String toString() {
            if (isEmpty()) return "Deque []";
            StringBuilder sb = new StringBuilder("Deque [");
            for (int i = 0; i < size; i++) {
                sb.append(data[(front + i) % data.length]);
                if (i < size - 1) sb.append(", ");
            }
            return sb.append("]").toString();
        }
    }

    // ══════════════════════════════════════════════
    // CLASSIC INTERVIEW PROBLEM: Sliding Window Maximum
    // ══════════════════════════════════════════════

    /**
     * PROBLEM: Given array and window size k, find the max of each window.
     * Input: nums=[1,3,-1,-3,5,3,6,7], k=3
     * Output: [3,3,5,5,6,7]

     * Approach: Monotonic Deque — maintain a deque of INDICES in decreasing order of their values.
     * The front always holds the index of the current window's maximum.

     * Time: O(n), Space: O(k)
     */
    public static int[] slidingWindowMax(int[] nums, int k) {
        java.util.Objects.requireNonNull(nums, "nums");
        if (k <= 0) {
            throw new IllegalArgumentException("k must be positive");
        }
        if (k > nums.length) {
            throw new IllegalArgumentException("k must be <= nums.length");
        }

        int n = nums.length;
        int[] result = new int[n - k + 1];
        Deque<Integer> deque = new Deque<>(); // Stores indices, not values

        for (int i = 0; i < n; i++) {
            // Remove indices that are out of the current window
            while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {
                deque.removeFirst();
            }

            // Remove indices of elements smaller than nums[i] from the rear.
            // They can never be the maximum of any future window.
            while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
                deque.removeLast();
            }

            deque.addLast(i);

            // Start recording results once our first window is complete
            if (i >= k - 1) {
                result[i - k + 1] = nums[deque.peekFirst()]; // Front = max
            }
        }
        return result;
    }

    /* ─────────────────────────────────────────────
    DEMO
                    Queue	     Deque
    Nghĩa	        Hàng đợi	 Hàng đợi 2 đầu
    Lấy dữ liệu	    Lấy từ đầu	 Lấy từ đầu / cuối
    Thêm dữ liệu	Thêm cuối	 Thêm đầu / cuối
    ──────────────────────────────────────────────
    */

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║           QUEUE DEMO             ║");
        System.out.println("╚══════════════════════════════════╝\n");

        // Basic queue
        Queue<String> q = new Queue<>();
        q.enqueue("Alice"); q.enqueue("Bob"); q.enqueue("Charlie");
        System.out.println("After enqueues: " + q);
        System.out.println("peek():         " + q.peek());
        System.out.println("dequeue():      " + q.dequeue() + " -> " + q);

        // Deque
        System.out.println("\n--- Deque Demo ---");
        Deque<Integer> dq = new Deque<>();
        dq.addLast(10); dq.addLast(20); dq.addFirst(5);
        System.out.println("Deque:       " + dq);
        System.out.println("removeFirst: " + dq.removeFirst());
        System.out.println("removeLast:  " + dq.removeLast());
        System.out.println("After:       " + dq);

        // Sliding Window Maximum
        System.out.println("\n--- Sliding Window Maximum (k=3) ---");
        int[] nums = {1, 3, -1, -3, 5, 3, 6, 7};
        int[] maxes = slidingWindowMax(nums, 3);

        System.out.print("Input:  [1, 3, -1, -3, 5, 3, 6, 7]\nOutput: [");
        for (int i = 0; i < maxes.length; i++) {
            System.out.print(maxes[i] + (i < maxes.length - 1 ? ", " : ""));
        }
        System.out.println("]"); // Expected: [3,3,5,5,6,7]
    }
}
