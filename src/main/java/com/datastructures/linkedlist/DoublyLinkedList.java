package com.datastructures.linkedlist;

/**
 * ============================================================
 * DATA STRUCTURE: Doubly Linked List
 * ============================================================

 * CONCEPT:
 *   Like a singly linked list, but each node has TWO pointers:
 *    1 to the next node & 1 to the PREVIOUS node.

 *   This makes traversal in both directions possible & enables O(1) removeLast (which costs O(n) in singly).

 *        head                          tail
 *         │                             │
 *         ▼                             ▼
 *  null ← [10] ⇄ [20] ⇄ [30] ⇄ [40] → null

 *   Each node: [prev | data | next]

 * SINGLY vs DOUBLY:
 *   Singly:  less memory (1 pointer), removeLast is O(n)
 *   Doubly:  more memory (2 pointers), removeLast is O(1), easier to implement deque/LRU cache

 * REAL-WORLD USE CASES:
 *   - Java's LinkedList (implements both List & Deque)
 *   - Browser history (forward AND back navigation)
 *   - LRU Cache (the hot structure behind memory caching)
 *   - Text editor cursor movement (prev / next character)
 *   - Operating system process scheduler

 * TIME COMPLEXITY:
 *   Access by index  : O(n) — but we can start from tail if index > size/2
 *   Search           : O(n)
 *   Insert at head   : O(1)
 *   Insert at tail   : O(1) ← advantage over singly
 *   Delete at head   : O(1)
 *   Delete at tail   : O(1) ← key advantage over singly
 *   Delete by ref    : O(1) — if you have the node reference!

 * SPACE COMPLEXITY: O(n), with extra O(n) for prev pointers

 * INTERVIEW TIPS:
 *   - LRU Cache = HashMap + Doubly Linked List (very common problem!)
 *   - When you have a node reference, deletion is O(1) — super useful
 *   - Drawing boxes with ←→ arrows helps in whiteboard interviews
 * ============================================================
 */
public class DoublyLinkedList<T> {

    // ──────────────────────────────────────────────
    // INNER CLASS: Node
    // ──────────────────────────────────────────────

    /**
     * A doubly linked node holds data & TWO pointers.
     * Making it package-private allows other classes (like LRU Cache) to hold direct node references for O(1) removal.
     */
    static class Node<T> {
        T data;
        Node<T> prev;   // Points to the previous node
        Node<T> next;   // Points to the next node

        Node(T data) {
            this.data = data;
            this.prev = null;
            this.next = null;
        }
    }

    // ──────────────────────────────────────────────
    // FIELDS
    // ──────────────────────────────────────────────

    private Node<T> head;
    private Node<T> tail;
    private int size;

    // ──────────────────────────────────────────────
    // CORE OPERATIONS
    // ──────────────────────────────────────────────

    /**
     * Inserts at the front (head).
     * Time: O(1)

     * Before: head → [20] ⇄ [30] ← tail
     * After:  head → [10] ⇄ [20] ⇄ [30] ← tail
     */
    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.next = head;    // New → Old head
            head.prev = newNode;    // Old head ← New
            head = newNode;
        }
        size++;
    }

    /**
     * Inserts at the back (tail).
     * Time: O(1)
     */
    public void addLast(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.prev = tail;    // Old tail ← New
            tail.next = newNode;    // Old tail → New
            tail = newNode;
        }
        size++;
    }

    /**
     * Removes & returns the first element.
     * Time: O(1)
     */
    public T removeFirst() {
        if (isEmpty()) throw new IllegalStateException("List is empty");
        T data = head.data;
        if (size == 1) {
            head = tail = null;
        } else {
            head = head.next;
            head.prev = null;   // Break old head's back-link
        }
        size--;
        return data;
    }

    /**
     * Removes & returns the last element.
     * Time: O(1) ← THIS is the big win over singly linked list!
     */
    public T removeLast() {
        if (isEmpty()) throw new IllegalStateException("List is empty");
        T data = tail.data;
        if (size == 1) {
            head = tail = null;
        } else {
            tail = tail.prev;
            tail.next = null;   // Detach old tail
        }
        size--;
        return data;
    }

    /**
     * Removes a specific node by reference in O(1)!
     * This is extremely powerful — if you hold a node reference
        (e.g., from a HashMap), deletion is O(1).
     * This is the foundation of LRU Cache.

     * Time: O(1) — no traversal needed!
     */
    public void removeNode(Node<T> node) {
        if (node == head) { removeFirst(); return; }
        if (node == tail) { removeLast();  return; }

        // node is in the middle — re-link around it
        node.prev.next = node.next;   // prev skips over node
        node.next.prev = node.prev;   // next points back to prev
        node.prev = null;
        node.next = null;
        size--;
    }

    /**
     * Moves a node to the front (head) of the list.
     * Time: O(1) — used in LRU Cache to mark as "most recently used"
     */
    public void moveToFront(Node<T> node) {
        if (node == head) return; // Already at front
        removeNode(node);
        // Re-insert at head (can't call addFirst since node already exists)
        node.next = head;
        node.prev = null;
        head.prev = node;
        head = node;
        size++;
    }

    /**
     * Returns the element at a specific index.
     * Time: O(n), but optimized: start from head / tail based on index.
     */
    public T get(int index) {
        validateIndex(index);
        return getNode(index).data;
    }

    /**
     * Smart traversal: start from the closer end.
     * If index < size/2 → walk from head
     * If index >= size/2 → walk from tail (backward!)
     */
    private Node<T> getNode(int index) {
        if (index < size / 2) {
            // Traverse from head
            Node<T> curr = head;
            for (int i = 0; i < index; i++) curr = curr.next;
            return curr;
        } else {
            // Traverse from tail (backwards)
            Node<T> curr = tail;
            for (int i = size - 1; i > index; i--) curr = curr.prev;
            return curr;
        }
    }

    // ──────────────────────────────────────────────
    // UTILITY
    // ──────────────────────────────────────────────

    public int size()       { return size; }
    public boolean isEmpty(){ return size == 0; }
    public T peekFirst()    { if (isEmpty())  throw new IllegalStateException(); return head.data; }
    public T peekLast()     { if (isEmpty())  throw new IllegalStateException(); return tail.data; }

    private void validateIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    /** Print forward: head → ... → tail */
    public String toStringForward() {
        StringBuilder sb = new StringBuilder("head ⇄ ");
        Node<T> curr = head;
        while (curr != null) {
            sb.append("[").append(curr.data).append("]");
            if (curr.next != null) sb.append(" ⇄ ");
            curr = curr.next;
        }
        return sb.append(" ⇄ tail").toString();
    }

    /** Print backward: tail → ... → head */
    public String toStringBackward() {
        StringBuilder sb = new StringBuilder("tail ⇄ ");
        Node<T> curr = tail;
        while (curr != null) {
            sb.append("[").append(curr.data).append("]");
            if (curr.prev != null) sb.append(" ⇄ ");
            curr = curr.prev;
        }
        return sb.append(" ⇄ head").toString();
    }

    @Override
    public String toString() { return toStringForward(); }

    // ──────────────────────────────────────────────
    // DEMO
    // ──────────────────────────────────────────────

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║    DOUBLY LINKED LIST DEMO       ║");
        System.out.println("╚══════════════════════════════════╝\n");

        DoublyLinkedList<Integer> list = new DoublyLinkedList<>();

        list.addLast(10); list.addLast(20); list.addLast(30); list.addLast(40);
        list.addFirst(5);
        System.out.println("Forward:  " + list.toStringForward());
        System.out.println("Backward: " + list.toStringBackward());

        System.out.println("\n--- O(1) remove from both ends ---");
        System.out.println("removeFirst(): " + list.removeFirst() + " → " + list);
        System.out.println("removeLast():  " + list.removeLast()  + " → " + list);

        System.out.println("\n--- Get by index (bidirectional optimization) ---");
        System.out.println("get(0) = " + list.get(0) + " (from head)");
        System.out.println("get(2) = " + list.get(2) + " (from tail)");

        System.out.println("\n--- LRU Cache simulation ---");
        System.out.println("(Doubly Linked List + HashMap = O(1) LRU Cache)");
        System.out.println("See: LRUCache.java in the hashtable package");
    }
}
