package com.datastructures.array;

/**
 * A dynamic array keeps elements in a backing array. When it runs out of room, it allocates a larger array,
 * copies the elements over, and continues. Doubling the capacity keeps the average cost of an add-at-end O(1).
 * <p>
 * {@code size} is how many elements you are using; {@code capacity} is the length of the backing array.
 */
@SuppressWarnings("unchecked")
public class DynamicArray<T> {

    private static final int DEFAULT_CAPACITY = 4;

    private Object[] data;
    private int size;

    public DynamicArray() {
        this.data = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public DynamicArray(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Capacity must be > 0");
        }
        this.data = new Object[initialCapacity];
        this.size = 0;
    }

    /** Adds {@code value} at the end. Grows the backing array if needed. */
    public void add(T value) {
        ensureCapacity();
        data[size] = value;
        size++;
    }

    /**
     * Inserts {@code value} at {@code index} and shifts the rest to the right.
     */
    public void add(int index, T value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        ensureCapacity();

        for (int slot = size; slot > index; slot--) {
            data[slot] = data[slot - 1];
        }
        data[index] = value;
        size++;
    }

    /** Returns the element at {@code index}. */
    public T get(int index) {
        validateIndex(index);
        return (T) data[index];
    }

    /** Replaces the element at {@code index} and returns the old value. */
    public T set(int index, T value) {
        validateIndex(index);
        T oldValue = (T) data[index];
        data[index] = value;
        return oldValue;
    }

    /** Removes the element at {@code index} and shifts the rest left. May shrink the backing array when very sparse. */
    public T remove(int index) {
        validateIndex(index);
        T removedValue = (T) data[index];

        for (int slot = index; slot < size - 1; slot++) {
            data[slot] = data[slot + 1];
        }

        size--;
        data[size] = null;

        if (size > 0 && size == data.length / 4) {
            shrink();
        }
        return removedValue;
    }

    /** Returns true if {@code value} appears in the list. */
    public boolean contains(T value) {
        return indexOf(value) != -1;
    }

    /** Returns the first index of {@code value}, or -1. */
    public int indexOf(T value) {
        for (int index = 0; index < size; index++) {
            boolean bothNull = value == null && data[index] == null;
            boolean equalNonNull = value != null && value.equals(data[index]);
            if (bothNull || equalNonNull) {
                return index;
            }
        }
        return -1;
    }

    /**
     * If the backing array is full, allocate a new array twice as large and copy elements over.
     */
    private void ensureCapacity() {
        if (size < data.length) {
            return;
        }
        int oldCapacity = data.length;
        int newCapacity = oldCapacity * 2;
        resize(newCapacity);
        System.out.println("  [DynamicArray] Grew capacity: " + oldCapacity + " -> " + newCapacity);
    }

    /** Halves capacity when the list is only one-quarter full (optional memory trade-off). */
    private void shrink() {
        int newCapacity = data.length / 2;
        resize(newCapacity);
        System.out.println("  [DynamicArray] Shrunk capacity to " + newCapacity);
    }

    private void resize(int newCapacity) {
        Object[] newData = new Object[newCapacity];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return data.length;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        for (int index = 0; index < size; index++) {
            data[index] = null;
        }
        size = 0;
    }

    private void validateIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
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
        System.out.println("--- Dynamic array demo ---");

        DynamicArray<Integer> list = new DynamicArray<>(2);
        System.out.println("Start capacity: " + list.capacity());

        for (int i = 1; i <= 8; i++) {
            int value = i * 10;
            list.add(value);
            System.out.println("Add " + value + " | size=" + list.size() + " capacity=" + list.capacity()
                + " | current list: " + list);
        }

        list.add(3, 999);
        System.out.println("Insert 999 at index 3: " + list);

        int removed = list.remove(3);
        System.out.println("Remove index 3 -> " + removed + ". Current list: " + list);

        System.out.println("Contains 50? " + list.contains(50));
        System.out.println("Contains 99? " + list.contains(99));
        System.out.println("Index of 60: " + list.indexOf(60));

        DynamicArray<String> names = new DynamicArray<>();
        names.add("Alice");
        names.add("Bob");
        names.add("Charlie");
        System.out.println("Names: " + names);
        names.remove(1);
        System.out.println("After remove index 1: " + names);
    }
}
