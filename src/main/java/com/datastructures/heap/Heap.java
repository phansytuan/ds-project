package com.datastructures.heap;

/**
 * A binary heap is a complete binary tree stored in an array. A min-heap keeps the smallest item at index 0;
 * each parent is less than or equal to its children. Insert "bubbles up"; removing the root "bubbles down".
 */
public class Heap {

    public static class MinHeap {
        private int[] data;
        private int size;

        public MinHeap(int capacity) {
            data = new int[capacity];
            size = 0;
        }

        private int parentIndex(int index) {
            return (index - 1) / 2;
        }

        private int leftChildIndex(int index) {
            return 2 * index + 1;
        }

        private int rightChildIndex(int index) {
            return 2 * index + 2;
        }

        private boolean hasLeftChild(int index) {
            return leftChildIndex(index) < size;
        }

        private boolean hasRightChild(int index) {
            return rightChildIndex(index) < size;
        }

        private void swap(int firstIndex, int secondIndex) {
            int temp = data[firstIndex];
            data[firstIndex] = data[secondIndex];
            data[secondIndex] = temp;
        }

        /** Adds a value at the end, then swaps upward until the parent is smaller or we hit the root. */
        public void offer(int value) {
            if (size == data.length) {
                throw new IllegalStateException("Heap is full.");
            }
            data[size] = value;
            size++;
            bubbleUp(size - 1);
        }

        private void bubbleUp(int index) {
            while (index > 0) {
                int parent = parentIndex(index);
                if (data[parent] <= data[index]) {
                    break;
                }
                swap(index, parent);
                index = parent;
            }
        }

        /** Moves the last element to the root, shrinks size, then swaps downward with the smaller child until stable. */
        public int poll() {
            if (isEmpty()) {
                throw new IllegalStateException("Heap is empty.");
            }
            int minimum = data[0];
            data[0] = data[size - 1];
            size--;
            bubbleDown(0);
            return minimum;
        }

        private void bubbleDown(int index) {
            while (hasLeftChild(index)) {
                int smallestIndex = index;

                int leftIndex = leftChildIndex(index);
                if (data[leftIndex] < data[smallestIndex]) {
                    smallestIndex = leftIndex;
                }

                if (hasRightChild(index)) {
                    int rightIndex = rightChildIndex(index);
                    if (data[rightIndex] < data[smallestIndex]) {
                        smallestIndex = rightIndex;
                    }
                }

                if (smallestIndex == index) {
                    break;
                }

                swap(index, smallestIndex);
                index = smallestIndex;
            }
        }

        public int peek() {
            if (isEmpty()) {
                throw new IllegalStateException("Heap is empty.");
            }
            return data[0];
        }

        public int size() {
            return size;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        /**
         * Turns an existing array into a valid min-heap by calling bubbleDown on each non-leaf from the bottom up.
         */
        public static MinHeap buildHeap(int[] source) {
            MinHeap heap = new MinHeap(source.length);
            System.arraycopy(source, 0, heap.data, 0, source.length);
            heap.size = source.length;

            for (int index = heap.size / 2 - 1; index >= 0; index--) {
                heap.bubbleDown(index);
            }
            return heap;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder("MinHeap[");
            for (int index = 0; index < size; index++) {
                builder.append(data[index]);
                if (index < size - 1) {
                    builder.append(", ");
                }
            }
            return builder.append("]").toString();
        }
    }

    public static class MaxHeap {
        private int[] data;
        private int size;

        public MaxHeap(int capacity) {
            data = new int[capacity];
            size = 0;
        }

        private int parentIndex(int index) {
            return (index - 1) / 2;
        }

        private int leftChildIndex(int index) {
            return 2 * index + 1;
        }

        private int rightChildIndex(int index) {
            return 2 * index + 2;
        }

        private void swap(int firstIndex, int secondIndex) {
            int temp = data[firstIndex];
            data[firstIndex] = data[secondIndex];
            data[secondIndex] = temp;
        }

        public void offer(int value) {
            if (size == data.length) {
                throw new IllegalStateException("Heap is full.");
            }
            data[size] = value;
            size++;
            bubbleUp(size - 1);
        }

        private void bubbleUp(int index) {
            while (index > 0) {
                int parent = parentIndex(index);
                if (data[parent] >= data[index]) {
                    break;
                }
                swap(index, parent);
                index = parent;
            }
        }

        public int poll() {
            if (isEmpty()) {
                throw new IllegalStateException("Heap is empty.");
            }
            int maximum = data[0];
            size--;
            data[0] = data[size];
            bubbleDown(0);
            return maximum;
        }

        private void bubbleDown(int index) {
            while (leftChildIndex(index) < size) {
                int largestIndex = index;
                int leftIndex = leftChildIndex(index);
                if (data[leftIndex] > data[largestIndex]) {
                    largestIndex = leftIndex;
                }
                int rightIndex = rightChildIndex(index);
                if (rightIndex < size && data[rightIndex] > data[largestIndex]) {
                    largestIndex = rightIndex;
                }
                if (largestIndex == index) {
                    break;
                }
                swap(index, largestIndex);
                index = largestIndex;
            }
        }

        public int peek() {
            if (isEmpty()) {
                throw new IllegalStateException("Heap is empty.");
            }
            return data[0];
        }

        public int size() {
            return size;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder("MaxHeap[");
            for (int index = 0; index < size; index++) {
                builder.append(data[index]);
                if (index < size - 1) {
                    builder.append(", ");
                }
            }
            return builder.append("]").toString();
        }
    }

    /**
     * Keep a min-heap of size k: every time it grows past k, remove the smallest.
     * What remains are the k largest values (in heap order until you drain it).
     */
    public static int[] topKLargest(int[] numbers, int k) {
        MinHeap heap = new MinHeap(k + 1);

        for (int index = 0; index < numbers.length; index++) {
            heap.offer(numbers[index]);
            if (heap.size() > k) {
                heap.poll();
            }
        }

        int[] result = new int[k];
        for (int index = k - 1; index >= 0; index--) {
            result[index] = heap.poll();
        }
        return result;
    }

    public static int kthLargest(int[] numbers, int k) {
        MinHeap heap = new MinHeap(k + 1);
        for (int index = 0; index < numbers.length; index++) {
            heap.offer(numbers[index]);
            if (heap.size() > k) {
                heap.poll();
            }
        }
        return heap.peek();
    }

    /**
     * Lower half in a max-heap, upper half in a min-heap; sizes differ by at most one.
     * The median comes from the tops of the two heaps.
     */
    public static class MedianFinder {
        private final MaxHeap lowerHalf;
        private final MinHeap upperHalf;

        public MedianFinder() {
            lowerHalf = new MaxHeap(1000);
            upperHalf = new MinHeap(1000);
        }

        public void addNum(int value) {
            lowerHalf.offer(value);

            if (!upperHalf.isEmpty() && lowerHalf.peek() > upperHalf.peek()) {
                upperHalf.offer(lowerHalf.poll());
            }

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
            return lowerHalf.peek();
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Heap demo ---");

        System.out.println("Min heap inserts:");
        MinHeap minHeap = new MinHeap(10);
        int[] minSample = {5, 3, 8, 1, 4, 2};
        for (int index = 0; index < minSample.length; index++) {
            minHeap.offer(minSample[index]);
            System.out.println("Offer " + minSample[index] + " | " + minHeap + " | peek -> " + minHeap.peek());
        }
        System.out.print("Poll smallest first: ");
        while (!minHeap.isEmpty()) {
            System.out.print(minHeap.poll() + " ");
        }
        System.out.println();

        System.out.println();
        System.out.println("Max heap: ");
        MaxHeap maxHeap = new MaxHeap(10);
        for (int index = 0; index < minSample.length; index++) {
            maxHeap.offer(minSample[index]);
        }
        System.out.println(maxHeap + " | peek -> " + maxHeap.peek());
        System.out.print("Poll largest first: ");
        while (!maxHeap.isEmpty()) {
            System.out.print(maxHeap.poll() + " ");
        }
        System.out.println();

        System.out.println();
        int[] buildFrom = {7, 3, 5, 1, 9, 2, 6};
        MinHeap built = MinHeap.buildHeap(buildFrom);
        System.out.println("buildHeap gives: " + built);

        System.out.println();
        int[] nums = {3, 2, 1, 5, 6, 4, 8, 7};
        int[] top3 = topKLargest(nums, 3);
        System.out.print("Top 3 largest: ");
        for (int index = 0; index < top3.length; index++) {
            System.out.print(top3[index] + " ");
        }
        System.out.println();
        System.out.println("2nd largest: " + kthLargest(nums, 2));

        System.out.println();
        MedianFinder finder = new MedianFinder();
        int[] stream = {1, 2, 3, 4, 5};
        for (int index = 0; index < stream.length; index++) {
            finder.addNum(stream[index]);
            System.out.println("Add " + stream[index] + " | median -> " + finder.findMedian());
        }
    }
}
