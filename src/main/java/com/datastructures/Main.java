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
 * ============================================================
 * MAIN ENTRY POINT
 * Java Data Structures — Complete Interview Preparation Guide
 * ============================================================
 *
 * This class runs all data structure demonstrations.
 * You can also run each class individually (each has its own main()).
 *
 * HOW TO RUN:
 *   mvn compile
 *   mvn exec:java -Dexec.mainClass="com.datastructures.Main"
 *
 *   Or run individually:
 *   mvn exec:java -Dexec.mainClass="com.datastructures.array.StaticArray"
 * ============================================================
 */
public class Main {

    public static void main(String[] args) {
        printBanner();

        runSection("1. STATIC ARRAY",          () -> StaticArray.main(args));
        runSection("2. DYNAMIC ARRAY",         () -> DynamicArray.main(args));
        runSection("3. SINGLY LINKED LIST",    () -> SinglyLinkedList.main(args));
        runSection("4. DOUBLY LINKED LIST",    () -> DoublyLinkedList.main(args));
        runSection("5. STACK",                 () -> Stack.main(args));
        runSection("6. QUEUE & DEQUE",         () -> Queue.main(args));
        runSection("7. HASH TABLE",            () -> HashTable.main(args));
        runSection("8. LRU CACHE",             () -> LRUCache.main(args));
        runSection("9. BINARY TREE",           () -> BinaryTree.main(args));
        runSection("10. BINARY SEARCH TREE",   () -> BinarySearchTree.main(args));
        runSection("11. HEAP",                 () -> Heap.main(args));
        runSection("12. GRAPH",                () -> Graph.main(args));

        printComplexityTable();
    }

    private static void runSection(String title, Runnable demo) {
        System.out.println("\n");
        System.out.println("████████████████████████████████████████████████████████");
        System.out.println("   " + title);
        System.out.println("████████████████████████████████████████████████████████");
        System.out.println();
        try {
            demo.run();
        } catch (Exception e) {
            System.err.println("Error in " + title + ": " + e.getMessage());
        }
    }

    private static void printBanner() {
        System.out.println("""
            ╔══════════════════════════════════════════════════════════════╗
            ║                                                              ║
            ║     JAVA DATA STRUCTURES — Interview Preparation Guide       ║
            ║                                                              ║
            ║  Data Structures Covered:                                    ║
            ║   ✓ Array (Static & Dynamic)                                 ║
            ║   ✓ Linked List (Singly & Doubly)                            ║
            ║   ✓ Stack                                                    ║
            ║   ✓ Queue & Deque                                            ║
            ║   ✓ Hash Table + LRU Cache                                   ║
            ║   ✓ Binary Tree                                              ║
            ║   ✓ Binary Search Tree                                       ║
            ║   ✓ Heap (Min & Max)                                         ║
            ║   ✓ Graph (Adjacency List)                                   ║
            ║                                                              ║
            ╚══════════════════════════════════════════════════════════════╝
            """);
    }

    private static void printComplexityTable() {
        System.out.println("""

            ████████████████████████████████████████████████████████
               TIME COMPLEXITY CHEAT SHEET
            ████████████████████████████████████████████████████████

            ┌──────────────────┬─────────┬─────────┬─────────┬─────────┬────────┐
            │ Data Structure   │ Access  │ Search  │ Insert  │ Delete  │ Space  │
            ├──────────────────┼─────────┼─────────┼─────────┼─────────┼────────┤
            │ Array            │  O(1)   │  O(n)   │  O(n)   │  O(n)   │  O(n)  │
            │ Dynamic Array    │  O(1)   │  O(n)   │ O(1)*   │  O(n)   │  O(n)  │
            │ Singly LinkedList│  O(n)   │  O(n)   │  O(1)** │  O(n)   │  O(n)  │
            │ Doubly LinkedList│  O(n)   │  O(n)   │  O(1)** │ O(1)** │  O(n)  │
            │ Stack            │  O(n)   │  O(n)   │  O(1)   │  O(1)   │  O(n)  │
            │ Queue            │  O(n)   │  O(n)   │  O(1)   │  O(1)   │  O(n)  │
            │ Hash Table       │   N/A   │  O(1)*  │  O(1)*  │  O(1)*  │  O(n)  │
            │ Binary Tree      │  O(n)   │  O(n)   │  O(n)   │  O(n)   │  O(n)  │
            │ BST (balanced)   │ O(logn) │ O(logn) │ O(logn) │ O(logn) │  O(n)  │
            │ Min/Max Heap     │  O(1)†  │  O(n)   │ O(logn) │ O(logn) │  O(n)  │
            │ Graph (Adj List) │   N/A   │ O(V+E)  │  O(1)   │  O(E)   │ O(V+E) │
            └──────────────────┴─────────┴─────────┴─────────┴─────────┴────────┘

            *  = amortized
            ** = at head/tail with pointer
            †  = only for min (min heap) or max (max heap)

            WHEN TO USE WHAT:
            ┌─────────────────────────────────────────────────────┐
            │ Need O(1) access by index?      → Array             │
            │ Need O(1) front/back insert?    → LinkedList/Deque  │
            │ Need LIFO?                      → Stack             │
            │ Need FIFO?                      → Queue             │
            │ Need O(1) key lookup?           → HashMap           │
            │ Need ordered data + fast ops?   → BST               │
            │ Need min/max quickly?           → Heap              │
            │ Need shortest path?             → BFS + Graph       │
            │ Need top-K elements?            → Heap              │
            │ Need LRU Cache?                 → HashMap + DLL     │
            └─────────────────────────────────────────────────────┘
            """);
    }
}
