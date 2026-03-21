package com.datastructures.array;

/**
 * A fixed-size array stores elements in consecutive slots (memory location). You pick the capacity once;
 * the logical {@code size} grows as you add values until the array is full.
 * <p>
 * Index access is O(1).
 * Inserting / removing in the middle needs shifting neighbors, so that is O(n).
 */
public class StaticArray {

    private int[] data; // [] data is a StaticArray
    private int size;

    /** constructor - Creates an empty array that can hold up to {@code capacity} integers. */
    public StaticArray(int capacity) {
        this.data = new int[capacity];
        this.size = 0;
    }

    /** Appends {@code value} at the next free slot. Throws if the array is full. */
    public void add(int value) {
        if (size >= data.length) { throw new IllegalStateException("Array is full. Capacity: " + data.length); }
        data[size] = value;
        size++;
    }

    /** Returns the value at {@code index}. */
    public int get(int index) {
        validateIndex(index);
        return data[index];
    }
    /** Overwrites the value at {@code index}. */
    public void set(int index, int value) {
        validateIndex(index);
        data[index] = value;
    }

    /**
     * Inserts {@code value} at {@code index} & shifts everything from that index to the right.
     * <p>
     * Example: [10, 20, 40, 50] insert 30 at index 2 → [10, 20, 30, 40, 50]
     */
    public void insertAt(int index, int value) {

        if (size >= data.length) { throw new IllegalStateException("Array is full!"); }
        if (index < 0 || index > size) { throw new IndexOutOfBoundsException("Index: " + index); }

        for (int slot = size; slot > index; slot--) {
            data[slot] = data[slot - 1];
        }
        data[index] = value;
        size++;
    }

    /**
     * Removes the value at {@code index} & shifts everything after it left <- by 1 slot.
     * <p>
     * Example: [10, 20, 30, 40, 50] remove index 2 → [10, 20, 40, 50]
     */
    public int removeAt(int index) {
        validateIndex(index);
        int removedValue = data[index];

        for (int slot = index; slot < size - 1; slot++) {
            data[slot] = data[slot + 1];
        }

        size--;
        data[size] = 0;
        return removedValue;
    }

    /** linear search = Iterate through a collection one element at a time */
    public int linearSearch(int value) {
        for (int index = 0; index < size; index++) {
            if (data[index] == value) {
                return index;
            }
        }
        return -1;
    }

    /**
     * binary search = Search algorithm that finds the position of a target value within a sorted array.
     * Half of the array is eliminated during each "step"
     * <p>
     * Repeat until found / the range is empty.
     */
    public int binarySearch(int value) {
        int leftIndex = 0;
        int rightIndex = size - 1;

        while (leftIndex <= rightIndex) {
            int middleIndex = leftIndex + (rightIndex - leftIndex) / 2;

            if (data[middleIndex] == value) {
                return middleIndex;
            }
            if (data[middleIndex] < value) {
                leftIndex = middleIndex + 1;
            } else {
                rightIndex = middleIndex - 1;
            }
        }
        return -1;
    }

    /**
     * Reverses the order of elements
     */
    public void reverse() {
        int leftIndex = 0;
        int rightIndex = size - 1;

        while (leftIndex < rightIndex) {
            int temp = data[leftIndex];
            data[leftIndex] = data[rightIndex];
            data[rightIndex] = temp;
            leftIndex++;
            rightIndex--;
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void validateIndex(int index) {
        if (index < 0 || index >= size) { throw new IndexOutOfBoundsException("Index " + index + " out of bounds for size " + size); }
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder builder = new StringBuilder("[");
        for (int index = 0; index < size; index++) {
            builder.append(data[index]);
            if (index < size - 1) {
                builder.append(", ");
            }
        }
        return builder.append("]").toString();
    }

    public static void main(String[] args) {
        System.out.println("--- Static array demo ---");

        StaticArray array = new StaticArray(10);

        array.add(10);
        array.add(20);
        array.add(30);
        array.add(40);
        array.add(50);
        System.out.println("After add: " + array);

        System.out.println("Get index 2 -> " + array.get(2));

        array.insertAt(2, 25);
        System.out.println("Insert 25 at index 2: " + array);

        int removed = array.removeAt(3);
        System.out.println("Remove index 3 -> " + removed + ". Current array: " + array);

        System.out.println("Linear search 25 -> index " + array.linearSearch(25));
        System.out.println("Binary search 40 -> index " + array.binarySearch(40));

        array.reverse();
        System.out.println("After reverse: " + array);
    }
}
