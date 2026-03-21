package com.datastructures;

import com.datastructures.array.StaticArray;
import com.datastructures.array.DynamicArray;
import com.datastructures.linkedlist.SinglyLinkedList;
import com.datastructures.linkedlist.DoublyLinkedList;
import com.datastructures.stack.Stack;
import com.datastructures.queue.Queue;
import com.datastructures.hashtable.HashTable;
import com.datastructures.hashtable.LRUCache;
import com.datastructures.tree.BinaryTree;
import com.datastructures.tree.BinarySearchTree;
import com.datastructures.heap.Heap;
import com.datastructures.graph.Graph;

/**
 * Runs a demo of each data structure in order.
 * <p>
 * Each structure also has its own {@code main} method if you want to run one demo at a time.
 * <p>
 * From the project root:
 * {@code mvn compile}
 * {@code mvn exec:java -Dexec.mainClass="com.datastructures.Main"}
 */
public class Main {

    public static void main(String[] args) {
        printBanner();

        runSection("1. Static array", () -> StaticArray.main(args));
        runSection("2. Dynamic array", () -> DynamicArray.main(args));
        runSection("3. Singly linked list", () -> SinglyLinkedList.main(args));
        runSection("4. Doubly linked list", () -> DoublyLinkedList.main(args));
        runSection("5. Stack", () -> Stack.main(args));
        runSection("6. Queue and deque", () -> Queue.main(args));
        runSection("7. Hash table", () -> HashTable.main(args));
        runSection("8. LRU cache", () -> LRUCache.main(args));
        runSection("9. Binary tree", () -> BinaryTree.main(args));
        runSection("10. Binary search tree", () -> BinarySearchTree.main(args));
        runSection("11. Heap", () -> Heap.main(args));
        runSection("12. Graph", () -> Graph.main(args));

        printComplexityTable();
    }

    /** Runs one demo and prints a clear section title. */
    private static void runSection(String title, Runnable demo) {
        System.out.println();
        System.out.println("========== " + title + " ==========");
        System.out.println();
        try {
            demo.run();
        } catch (Exception e) {
            System.err.println("Error in " + title + ": " + e.getMessage());
        }
    }

    private static void printBanner() {
        System.out.println("Java data structures — learning demos");
        System.out.println("Arrays, lists, stack, queue, hash table, trees, heap, graph.");
        System.out.println();
    }

    private static void printComplexityTable() {
        System.out.println();
        System.out.println("========== Time complexity (typical) ==========");
        System.out.println("Static array:     get/set by index O(1); search O(n); insert/remove in middle O(n)");
        System.out.println("Dynamic array:    add at end O(1) amortized; insert/remove in middle O(n)");
        System.out.println("Linked list:      walk to index O(n); add/remove at ends O(1) with pointers");
        System.out.println("Stack / queue:    push-pop or enqueue-dequeue O(1)");
        System.out.println("Hash table:       get/put/remove average O(1); worst case O(n) if many collisions");
        System.out.println("BST (balanced):   search/insert/delete O(log n); skewed tree degrades to O(n)");
        System.out.println("Heap:             insert/remove root O(log n); peek min/max O(1)");
        System.out.println("Graph BFS/DFS:    O(V + E) with V vertices and E edges");
    }
}
