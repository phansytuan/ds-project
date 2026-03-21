package com.datastructures.linkedlist;

/**
 * A singly linked list connects nodes in one direction: each node knows its value and the next node.
 * Inserting at the head (or tail with a tail pointer) is O(1); finding an index requires walking from the head.
 */
public class SinglyLinkedList<T> {

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public SinglyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    /** Inserts a new node before the current head. */
    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }
        size++;
    }

    /** Appends a new node after the current tail. */
    public void addLast(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    /** Inserts at {@code index}; index 0 is the head, index {@code size} is the same as addLast. */
    public void addAt(int index, T data) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        if (index == 0) {
            addFirst(data);
            return;
        }
        if (index == size) {
            addLast(data);
            return;
        }

        Node<T> nodeBefore = getNodeAt(index - 1);
        Node<T> newNode = new Node<>(data);
        newNode.next = nodeBefore.next;
        nodeBefore.next = newNode;
        size++;
    }

    /** Removes and returns the head value. */
    public T removeFirst() {
        if (isEmpty()) {
            throw new IllegalStateException("List is empty.");
        }
        T removedData = head.data;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        size--;
        return removedData;
    }

    /** Removes and returns the tail value (walks to the node before tail). */
    public T removeLast() {
        if (isEmpty()) {
            throw new IllegalStateException("List is empty.");
        }
        if (size == 1) {
            return removeFirst();
        }

        Node<T> nodeBeforeTail = getNodeAt(size - 2);
        T removedData = tail.data;
        nodeBeforeTail.next = null;
        tail = nodeBeforeTail;
        size--;
        return removedData;
    }

    /** Removes the first node whose value equals {@code value}; returns true if something was removed. */
    public boolean remove(T value) {
        if (isEmpty()) {
            return false;
        }

        if (head.data.equals(value)) {
            removeFirst();
            return true;
        }

        Node<T> previousNode = head;
        Node<T> currentNode = head.next;
        while (currentNode != null) {
            if (currentNode.data.equals(value)) {
                previousNode.next = currentNode.next;
                if (currentNode == tail) {
                    tail = previousNode;
                }
                size--;
                return true;
            }
            previousNode = currentNode;
            currentNode = currentNode.next;
        }
        return false;
    }

    /** Returns the value at {@code index}. */
    public T get(int index) {
        validateIndex(index);
        return getNodeAt(index).data;
    }

    /** Returns the index of the first occurrence of {@code value}, or -1. */
    public int indexOf(T value) {
        Node<T> currentNode = head;
        int index = 0;
        while (currentNode != null) {
            if (currentNode.data.equals(value)) {
                return index;
            }
            currentNode = currentNode.next;
            index++;
        }
        return -1;
    }

    /**
     * Reverses links so the old head becomes the new tail.
     * Walk forward; point each node's next back to the previous node.
     */
    public void reverse() {
        Node<T> previousNode = null;
        Node<T> currentNode = head;
        tail = head;

        while (currentNode != null) {
            Node<T> nextNode = currentNode.next;
            currentNode.next = previousNode;
            previousNode = currentNode;
            currentNode = nextNode;
        }
        head = previousNode;
    }

    /**
     * Returns the value at the middle node using two pointers:
     * slowPointer moves one step per iteration, fastPointer moves two.
     * When fastPointer reaches the end, slowPointer is at the middle.
     */
    public T findMiddle() {
        if (isEmpty()) {
            throw new IllegalStateException("List is empty.");
        }
        Node<T> slowPointer = head;
        Node<T> fastPointer = head;

        while (fastPointer != null && fastPointer.next != null) {
            slowPointer = slowPointer.next;
            fastPointer = fastPointer.next.next;
        }
        return slowPointer.data;
    }

    /**
     * Cycle detection: if fastPointer ever meets slowPointer, there is a loop.
     */
    public boolean hasCycle() {
        Node<T> slowPointer = head;
        Node<T> fastPointer = head;

        while (fastPointer != null && fastPointer.next != null) {
            slowPointer = slowPointer.next;
            fastPointer = fastPointer.next.next;
            if (slowPointer == fastPointer) {
                return true;
            }
        }
        return false;
    }

    private Node<T> getNodeAt(int index) {
        Node<T> currentNode = head;
        for (int step = 0; step < index; step++) {
            currentNode = currentNode.next;
        }
        return currentNode;
    }

    private void validateIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public T peekFirst() {
        if (isEmpty()) {
            throw new IllegalStateException("List is empty.");
        }
        return head.data;
    }

    public T peekLast() {
        if (isEmpty()) {
            throw new IllegalStateException("List is empty.");
        }
        return tail.data;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("head -> ");
        Node<T> currentNode = head;
        while (currentNode != null) {
            builder.append("[").append(currentNode.data).append("]");
            if (currentNode.next != null) {
                builder.append(" -> ");
            }
            currentNode = currentNode.next;
        }
        builder.append(" -> null");
        return builder.toString();
    }

    public static void main(String[] args) {
        System.out.println("--- Singly linked list demo ---");

        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();
        list.addLast(10);
        list.addLast(20);
        list.addLast(30);
        list.addLast(40);
        list.addFirst(5);
        System.out.println("After adds: " + list);

        list.addAt(3, 25);
        System.out.println("Insert 25 at index 3: " + list);

        System.out.println("Remove first -> " + list.removeFirst() + " | List: " + list);
        System.out.println("Remove last -> " + list.removeLast() + " | List: " + list);

        list.remove(25);
        System.out.println("Remove value 25: " + list);

        System.out.println("Middle element -> " + list.findMiddle());

        list.reverse();
        System.out.println("After reverse: " + list);

        System.out.println("Has cycle? " + list.hasCycle());
    }
}
