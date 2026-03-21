package com.datastructures.linkedlist;

/**
 * Each node points to both the previous and the next node, so you can walk the list forward or backward.
 * Removing the last element is O(1) here (with a tail pointer), unlike a singly linked list where you must walk from the head.
 */
public class DoublyLinkedList<T> {

    /**
     * Package visibility lets related code (for example an LRU cache) hold a node reference for O(1) unlinking.
     */
    static class Node<T> {
        T data;
        Node<T> prev;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.prev = null;
            this.next = null;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    public void addLast(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public T removeFirst() {
        if (isEmpty()) {
            throw new IllegalStateException("List is empty.");
        }
        T removedData = head.data;
        if (size == 1) {
            head = null;
            tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }
        size--;
        return removedData;
    }

    public T removeLast() {
        if (isEmpty()) {
            throw new IllegalStateException("List is empty.");
        }
        T removedData = tail.data;
        if (size == 1) {
            head = null;
            tail = null;
        } else {
            tail = tail.prev;
            tail.next = null;
        }
        size--;
        return removedData;
    }

    /**
     * Unlinks {@code node} from the list when you already have its reference (no search).
     */
    public void removeNode(Node<T> node) {
        if (node == head) {
            removeFirst();
            return;
        }
        if (node == tail) {
            removeLast();
            return;
        }

        node.prev.next = node.next;
        node.next.prev = node.prev;
        node.prev = null;
        node.next = null;
        size--;
    }

    /** Moves {@code node} to the head without allocating a new node. */
    public void moveToFront(Node<T> node) {
        if (node == head) {
            return;
        }
        removeNode(node);

        node.next = head;
        node.prev = null;
        head.prev = node;
        head = node;
        size++;
    }

    public T get(int index) {
        validateIndex(index);
        return getNodeAt(index).data;
    }

    /**
     * Walks from whichever end is closer: first half from head, second half from tail.
     */
    private Node<T> getNodeAt(int index) {
        if (index < size / 2) {
            Node<T> currentNode = head;
            for (int step = 0; step < index; step++) {
                currentNode = currentNode.next;
            }
            return currentNode;
        }

        Node<T> currentNode = tail;
        for (int step = size - 1; step > index; step--) {
            currentNode = currentNode.prev;
        }
        return currentNode;
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

    private void validateIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    public String toStringForward() {
        StringBuilder builder = new StringBuilder("head <-> ");
        Node<T> currentNode = head;
        while (currentNode != null) {
            builder.append("[").append(currentNode.data).append("]");
            if (currentNode.next != null) {
                builder.append(" <-> ");
            }
            currentNode = currentNode.next;
        }
        builder.append(" <-> tail");
        return builder.toString();
    }

    public String toStringBackward() {
        StringBuilder builder = new StringBuilder("tail <-> ");
        Node<T> currentNode = tail;
        while (currentNode != null) {
            builder.append("[").append(currentNode.data).append("]");
            if (currentNode.prev != null) {
                builder.append(" <-> ");
            }
            currentNode = currentNode.prev;
        }
        builder.append(" <-> head");
        return builder.toString();
    }

    @Override
    public String toString() {
        return toStringForward();
    }

    public static void main(String[] args) {
        System.out.println("--- Doubly linked list demo ---");

        DoublyLinkedList<Integer> list = new DoublyLinkedList<>();
        list.addLast(10);
        list.addLast(20);
        list.addLast(30);
        list.addLast(40);
        list.addFirst(5);

        System.out.println("Forward:  " + list.toStringForward());
        System.out.println("Backward: " + list.toStringBackward());

        System.out.println("Remove first -> " + list.removeFirst() + " | List: " + list);
        System.out.println("Remove last -> " + list.removeLast() + " | List: " + list);

        System.out.println("get(0) from head side -> " + list.get(0));
        System.out.println("get(2) from tail side -> " + list.get(2));

        System.out.println("See LRUCache for hash map + doubly linked list together.");
    }
}
