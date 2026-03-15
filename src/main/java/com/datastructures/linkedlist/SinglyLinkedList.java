package com.datastructures.linkedlist;

/**
 * ============================================================
 * DATA STRUCTURE: Singly Linked List
 * ============================================================
 *
 * CONCEPT:
 *   A linked list is a chain of nodes where each node holds
 *   a value and a pointer (reference) to the next node.
 *   Unlike arrays, nodes are NOT stored contiguously in memory —
 *   they can be anywhere, linked together by pointers.
 *
 *   head
 *    │
 *    ▼
 *   [10|→] → [20|→] → [30|→] → [40|null]
 *
 * KEY INSIGHT: Arrays vs Linked Lists
 *   Arrays:       fast access O(1), slow insert/delete O(n)
 *   Linked Lists: slow access O(n), fast insert/delete at head O(1)
 *
 * REAL-WORLD USE CASES:
 *   - Undo/redo history (each action is a node)
 *   - Browser's forward/back navigation
 *   - Music playlists (next song pointer)
 *   - Hash table chaining (collision resolution)
 *   - Implementing stacks and queues
 *
 * TIME COMPLEXITY:
 *   Access by index  : O(n) — must walk from head
 *   Search           : O(n)
 *   Insert at head   : O(1) ← key advantage
 *   Insert at tail   : O(1) if tail pointer maintained, else O(n)
 *   Insert at index  : O(n)
 *   Delete at head   : O(1)
 *   Delete at index  : O(n)
 *
 * SPACE COMPLEXITY: O(n) — plus O(n) extra for node pointers
 *
 * INTERVIEW TIPS:
 *   - Always handle edge cases: empty list, single node, head/tail
 *   - Classic interview problems: reverse, detect cycle, find middle,
 *     merge sorted lists, remove Nth from end
 *   - The "runner" technique: use two pointers at different speeds
 * ============================================================
 */
public class SinglyLinkedList<T> {

    // ──────────────────────────────────────────────
    // INNER CLASS: Node
    // ──────────────────────────────────────────────

    /**
     * A Node is the building block of a linked list.
     * It holds data and a reference to the next node.
     */
    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    // ──────────────────────────────────────────────
    // FIELDS
    // ──────────────────────────────────────────────

    private Node<T> head;  // Points to the first node
    private Node<T> tail;  // Points to the last node (O(1) tail add)
    private int size;

    public SinglyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    // ──────────────────────────────────────────────
    // CORE OPERATIONS
    // ──────────────────────────────────────────────

    /**
     * Adds a node at the HEAD (front) of the list.
     * Time: O(1) — just update the head pointer.
     *
     * Before: head → [20] → [30] → null
     * After:  head → [10] → [20] → [30] → null
     */
    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head; // New node points to old head
            head = newNode;      // Head now points to new node
        }
        size++;
    }

    /**
     * Adds a node at the TAIL (end) of the list.
     * Time: O(1) — we maintain a tail pointer!
     *
     * Before: head → [10] → [20] → null  (tail → [20])
     * After:  head → [10] → [20] → [30] → null  (tail → [30])
     */
    public void addLast(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode; // Old tail points to new node
            tail = newNode;      // Tail is now the new node
        }
        size++;
    }

    /**
     * Inserts at a specific index.
     * Time: O(n) — must walk to position.
     */
    public void addAt(int index, T data) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index);
        if (index == 0)    { addFirst(data); return; }
        if (index == size) { addLast(data); return; }

        // Walk to the node BEFORE the insertion point
        Node<T> prev = getNode(index - 1);
        Node<T> newNode = new Node<>(data);
        newNode.next = prev.next; // New node → node after prev
        prev.next = newNode;      // prev → new node
        size++;
    }

    /**
     * Removes and returns the first element.
     * Time: O(1)
     */
    public T removeFirst() {
        if (isEmpty()) throw new IllegalStateException("List is empty");
        T data = head.data;
        head = head.next;  // Advance head to the next node
        if (head == null) tail = null; // List is now empty
        size--;
        return data;
    }

    /**
     * Removes and returns the last element.
     * Time: O(n) — singly linked, so we must walk to find second-to-last.
     * (Doubly linked list solves this with O(1) removeLast)
     */
    public T removeLast() {
        if (isEmpty()) throw new IllegalStateException("List is empty");
        if (size == 1) return removeFirst();

        // Walk to the second-to-last node
        Node<T> prev = getNode(size - 2);
        T data = tail.data;
        prev.next = null; // Detach the last node
        tail = prev;      // Update tail to second-to-last
        size--;
        return data;
    }

    /**
     * Removes the first occurrence of a value.
     * Time: O(n)
     */
    public boolean remove(T value) {
        if (isEmpty()) return false;

        // Special case: removing the head
        if (head.data.equals(value)) {
            removeFirst();
            return true;
        }

        // Walk list, tracking 'prev' so we can re-link around the node
        Node<T> prev = head;
        Node<T> curr = head.next;
        while (curr != null) {
            if (curr.data.equals(value)) {
                prev.next = curr.next;          // Skip over curr
                if (curr == tail) tail = prev;  // Update tail if needed
                size--;
                return true;
            }
            prev = curr;
            curr = curr.next;
        }
        return false; // Value not found
    }

    /**
     * Returns the value at a specific index.
     * Time: O(n)
     */
    public T get(int index) {
        validateIndex(index);
        return getNode(index).data;
    }

    /**
     * Searches for a value and returns its index.
     * Time: O(n)
     */
    public int indexOf(T value) {
        Node<T> curr = head;
        int index = 0;
        while (curr != null) {
            if (curr.data.equals(value)) return index;
            curr = curr.next;
            index++;
        }
        return -1;
    }

    // ──────────────────────────────────────────────
    // CLASSIC INTERVIEW ALGORITHMS
    // ──────────────────────────────────────────────

    /**
     * Reverses the linked list IN-PLACE.
     * Time: O(n), Space: O(1)
     *
     * Technique: Iterate and flip each node's 'next' pointer.
     *
     * Before: [1] → [2] → [3] → [4] → null
     *
     * Step 1: prev=null, curr=[1]
     *   next=[2], [1].next=null, prev=[1], curr=[2]
     * Step 2: prev=[1], curr=[2]
     *   next=[3], [2].next=[1], prev=[2], curr=[3]
     * ... and so on.
     *
     * After:  [4] → [3] → [2] → [1] → null
     */
    public void reverse() {
        Node<T> prev = null;
        Node<T> curr = head;
        tail = head; // Old head becomes the new tail

        while (curr != null) {
            Node<T> next = curr.next; // Save next before overwriting
            curr.next = prev;         // Flip the pointer
            prev = curr;              // Advance prev
            curr = next;              // Advance curr
        }

        head = prev; // prev is now at the end (new head)
    }

    /**
     * Finds the MIDDLE node using the "slow & fast pointer" technique.
     * Time: O(n), Space: O(1)
     *
     * Slow moves 1 step, fast moves 2 steps.
     * When fast reaches the end, slow is at the middle.
     *
     * [1] → [2] → [3] → [4] → [5]
     *  s          s          s       (slow: 1 step each)
     *  f               f       done  (fast: 2 steps each)
     *                  ↑ middle = 3
     */
    public T findMiddle() {
        if (isEmpty()) throw new IllegalStateException("List is empty");
        Node<T> slow = head;
        Node<T> fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;       // Move 1 step
            fast = fast.next.next;  // Move 2 steps
        }
        return slow.data;
    }

    /**
     * Detects a cycle using Floyd's Tortoise & Hare algorithm.
     * Time: O(n), Space: O(1)
     *
     * If there's a cycle, the fast pointer will eventually lap
     * the slow pointer and they'll meet.
     */
    public boolean hasCycle() {
        Node<T> slow = head;
        Node<T> fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) return true; // They met → cycle detected!
        }
        return false;
    }

    // ──────────────────────────────────────────────
    // HELPERS & UTILITY
    // ──────────────────────────────────────────────

    /** Walks to the node at 'index'. O(n) */
    private Node<T> getNode(int index) {
        Node<T> curr = head;
        for (int i = 0; i < index; i++) {
            curr = curr.next;
        }
        return curr;
    }

    private void validateIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
    public T peekFirst() { if (isEmpty()) throw new IllegalStateException(); return head.data; }
    public T peekLast()  { if (isEmpty()) throw new IllegalStateException(); return tail.data; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("head → ");
        Node<T> curr = head;
        while (curr != null) {
            sb.append("[").append(curr.data).append("]");
            if (curr.next != null) sb.append(" → ");
            curr = curr.next;
        }
        return sb.append(" → null").toString();
    }

    // ──────────────────────────────────────────────
    // DEMO
    // ──────────────────────────────────────────────

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║    SINGLY LINKED LIST DEMO       ║");
        System.out.println("╚══════════════════════════════════╝\n");

        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();

        // Build the list
        list.addLast(10); list.addLast(20); list.addLast(30); list.addLast(40);
        list.addFirst(5);
        System.out.println("After builds:        " + list);

        // Insert in the middle
        list.addAt(3, 25);
        System.out.println("After addAt(3, 25):  " + list);

        // Remove operations
        System.out.println("removeFirst():       " + list.removeFirst() + " → " + list);
        System.out.println("removeLast():        " + list.removeLast()  + " → " + list);
        list.remove(25);
        System.out.println("remove(25):          " + list);

        // Find middle
        System.out.println("Middle element:      " + list.findMiddle());

        // Reverse
        list.reverse();
        System.out.println("After reverse:       " + list);

        // Cycle detection
        System.out.println("Has cycle?           " + list.hasCycle()); // false
    }
}
