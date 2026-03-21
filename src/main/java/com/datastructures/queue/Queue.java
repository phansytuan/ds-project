package com.datastructures.queue;

/**
 * A queue is First-In-First-Out (FIFO): enqueue adds at the back, dequeue removes from the front.
 * This implementation uses a circular array so enqueue and dequeue stay O(1) without shifting the whole array.
 */
public class Queue<T> {

    private static final int DEFAULT_CAPACITY = 8;

    private Object[] data;
    private int frontIndex;
    /** Next free slot at the back (may wrap around). */
    private int rearIndex;
    private int elementCount;

    public Queue() {
        this.data = new Object[DEFAULT_CAPACITY];
        this.frontIndex = 0;
        this.rearIndex = 0;
        this.elementCount = 0;
    }

    /** Adds {@code value} at the rear. */
    public void enqueue(T value) {
        if (elementCount == data.length) {
            resize();
        }
        data[rearIndex] = value;
        rearIndex = (rearIndex + 1) % data.length;
        elementCount++;
    }

    /** Removes and returns the front element. */
    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty.");
        }
        T value = (T) data[frontIndex];
        data[frontIndex] = null;
        frontIndex = (frontIndex + 1) % data.length;
        elementCount--;
        return value;
    }

    /** Returns the front element without removing it. */
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty.");
        }
        return (T) data[frontIndex];
    }

    public boolean isEmpty() {
        return elementCount == 0;
    }

    public int size() {
        return elementCount;
    }

    /**
     * Copies elements in order into a new array twice as large, then resets front to 0 and rear to size.
     */
    private void resize() {
        Object[] newData = new Object[data.length * 2];
        for (int offset = 0; offset < elementCount; offset++) {
            int sourceIndex = (frontIndex + offset) % data.length;
            newData[offset] = data[sourceIndex];
        }
        data = newData;
        frontIndex = 0;
        rearIndex = elementCount;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder builder = new StringBuilder("[");
        for (int offset = 0; offset < elementCount; offset++) {
            int index = (frontIndex + offset) % data.length;
            builder.append(data[index]);
            if (offset < elementCount - 1) {
                builder.append(", ");
            }
        }
        builder.append("] (front on left)");
        return builder.toString();
    }

    /**
     * Double-ended queue: add or remove from either end in O(1) with the same circular-array idea.
     */
    public static class Deque<T> {
        private Object[] data;
        private int frontIndex;
        private int rearIndex;
        private int elementCount;

        public Deque() {
            data = new Object[8];
            frontIndex = 0;
            rearIndex = 0;
            elementCount = 0;
        }

        public void addFirst(T value) {
            if (elementCount == data.length) {
                grow();
            }
            frontIndex = (frontIndex - 1 + data.length) % data.length;
            data[frontIndex] = value;
            elementCount++;
        }

        public void addLast(T value) {
            if (elementCount == data.length) {
                grow();
            }
            data[rearIndex] = value;
            rearIndex = (rearIndex + 1) % data.length;
            elementCount++;
        }

        @SuppressWarnings("unchecked")
        public T removeFirst() {
            if (isEmpty()) {
                throw new IllegalStateException("Deque is empty.");
            }
            T value = (T) data[frontIndex];
            data[frontIndex] = null;
            frontIndex = (frontIndex + 1) % data.length;
            elementCount--;
            return value;
        }

        @SuppressWarnings("unchecked")
        public T removeLast() {
            if (isEmpty()) {
                throw new IllegalStateException("Deque is empty.");
            }
            rearIndex = (rearIndex - 1 + data.length) % data.length;
            T value = (T) data[rearIndex];
            data[rearIndex] = null;
            elementCount--;
            return value;
        }

        @SuppressWarnings("unchecked")
        public T peekFirst() {
            return (T) data[frontIndex];
        }

        @SuppressWarnings("unchecked")
        public T peekLast() {
            int lastIndex = (rearIndex - 1 + data.length) % data.length;
            return (T) data[lastIndex];
        }

        public boolean isEmpty() {
            return elementCount == 0;
        }

        public int size() {
            return elementCount;
        }

        private void grow() {
            Object[] newData = new Object[data.length * 2];
            for (int offset = 0; offset < elementCount; offset++) {
                int sourceIndex = (frontIndex + offset) % data.length;
                newData[offset] = data[sourceIndex];
            }
            data = newData;
            frontIndex = 0;
            rearIndex = elementCount;
        }

        @Override
        public String toString() {
            if (isEmpty()) {
                return "[]";
            }
            StringBuilder builder = new StringBuilder("[");
            for (int offset = 0; offset < elementCount; offset++) {
                int index = (frontIndex + offset) % data.length;
                builder.append(data[index]);
                if (offset < elementCount - 1) {
                    builder.append(", ");
                }
            }
            builder.append("]");
            return builder.toString();
        }
    }

    /**
     * For each window of length {@code windowSize}, returns the maximum value in that window.
     * The deque stores indices of candidates; indices at the front are dropped when they leave the window.
     * Indices at the back are dropped while their values are smaller than the new value (they cannot win).
     */
    public static int[] slidingWindowMax(int[] values, int windowSize) {
        if (values == null) {
            throw new IllegalArgumentException("values cannot be null");
        }
        if (windowSize <= 0) {
            throw new IllegalArgumentException("windowSize must be positive");
        }
        if (windowSize > values.length) {
            throw new IllegalArgumentException("windowSize must be <= values.length");
        }

        int valueCount = values.length;
        int[] maxPerWindow = new int[valueCount - windowSize + 1];
        Deque<Integer> indexDeque = new Deque<>();

        for (int rightIndex = 0; rightIndex < valueCount; rightIndex++) {
            removeExpiredIndices(indexDeque, rightIndex, windowSize);
            removeSmallerFromBack(indexDeque, values, rightIndex);
            indexDeque.addLast(rightIndex);

            if (rightIndex >= windowSize - 1) {
                int windowStart = rightIndex - windowSize + 1;
                maxPerWindow[windowStart] = values[indexDeque.peekFirst()];
            }
        }
        return maxPerWindow;
    }

    private static void removeExpiredIndices(Deque<Integer> indexDeque, int rightIndex, int windowSize) {
        while (!indexDeque.isEmpty()) {
            int oldestIndex = indexDeque.peekFirst();
            if (oldestIndex >= rightIndex - windowSize + 1) {
                break;
            }
            indexDeque.removeFirst();
        }
    }

    private static void removeSmallerFromBack(Deque<Integer> indexDeque, int[] values, int rightIndex) {
        while (!indexDeque.isEmpty()) {
            int backIndex = indexDeque.peekLast();
            if (values[backIndex] >= values[rightIndex]) {
                break;
            }
            indexDeque.removeLast();
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Queue demo ---");

        Queue<String> queue = new Queue<>();
        queue.enqueue("Alice");
        queue.enqueue("Bob");
        queue.enqueue("Charlie");
        System.out.println("Enqueue Alice, Bob, Charlie | Current queue: " + queue);
        System.out.println("Peek -> " + queue.peek());
        String served = queue.dequeue();
        System.out.println("Dequeue -> " + served + " | Current queue: " + queue);

        System.out.println();
        System.out.println("--- Deque demo ---");
        Deque<Integer> deque = new Deque<>();
        deque.addLast(10);
        deque.addLast(20);
        deque.addFirst(5);
        System.out.println("After addLast(10), addLast(20), addFirst(5): " + deque);
        System.out.println("Remove first -> " + deque.removeFirst());
        System.out.println("Remove last -> " + deque.removeLast());
        System.out.println("Current deque: " + deque);

        System.out.println();
        System.out.println("--- Sliding window maximum (window size 3) ---");
        int[] nums = {1, 3, -1, -3, 5, 3, 6, 7};
        int[] maxes = slidingWindowMax(nums, 3);
        System.out.print("Input:  [");
        printIntArray(nums);
        System.out.println("]");
        System.out.print("Output: [");
        printIntArray(maxes);
        System.out.println("]");
    }

    private static void printIntArray(int[] array) {
        for (int index = 0; index < array.length; index++) {
            System.out.print(array[index]);
            if (index < array.length - 1) {
                System.out.print(", ");
            }
        }
    }
}
