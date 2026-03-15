# Java Data Structures — Interview Preparation Guide

> A complete, from-scratch implementation of the 11 most important data structures in Java. Built for developers preparing for technical interviews at top tech companies.

---

## Project Structure

```
src/main/java/com/datastructures/
├── Main.java                        ← Run all demos + complexity table
├── array/
│   ├── StaticArray.java             ← Fixed-size array with binary search
│   └── DynamicArray.java            ← ArrayList clone with amortized O(1) add
├── linkedlist/
│   ├── SinglyLinkedList.java        ← With reverse, find middle, cycle detection
│   └── DoublyLinkedList.java        ← With O(1) removeLast, moveToFront
├── stack/
│   └── Stack.java                   ← With: valid parentheses, min stack, RPN eval
├── queue/
│   └── Queue.java                   ← Circular array + Deque + sliding window max
├── hashtable/
│   ├── HashTable.java               ← Chaining + rehashing + interview problems
│   └── LRUCache.java                ← HashMap + Doubly Linked List = O(1) cache
├── tree/
│   ├── BinaryTree.java              ← All traversals + height + LCA + path sum
│   └── BinarySearchTree.java        ← Insert/delete/search + kth smallest + LCA
├── heap/
│   └── Heap.java                    ← MinHeap + MaxHeap + Top-K + Median stream
└── graph/
    └── Graph.java                   ← BFS + DFS + shortest path + topo sort + cycle
```

---

## How to Run

```bash
# Compile the project
mvn compile

# Run all data structure demos
mvn exec:java -Dexec.mainClass="com.datastructures.Main"

# Run a specific data structure
mvn exec:java -Dexec.mainClass="com.datastructures.array.StaticArray"
mvn exec:java -Dexec.mainClass="com.datastructures.stack.Stack"
mvn exec:java -Dexec.mainClass="com.datastructures.graph.Graph"
# ... (each class has its own main method)
```

**Requirements:** Java 17+, Maven 3.6+

---

## Data Structure Quick Reference

### 1. Array
**What it is:** Contiguous block of memory. Elements accessed by index in O(1).

**When to use:**
- You know the size in advance
- You need random access by index
- You need a foundation for other structures (heap, hash table)

**Key patterns:** Two pointers, sliding window, prefix sum, binary search

---

### 2. Dynamic Array (ArrayList)
**What it is:** Self-resizing array. Doubles capacity when full → amortized O(1) add.

**When to use:**
- You need a resizable list with fast random access
- Most general-purpose "list" scenarios

**Key insight:** Doubling gives O(1) amortized. Adding 1 each time gives O(n²) total.

---

### 3. Singly Linked List
**What it is:** Chain of nodes, each pointing to the next. Fast head insert/delete; slow access.

**When to use:**
- Frequent insert/delete at the front
- Building stacks and queues
- When you don't need random access

**Must-know algorithms:** Reverse in-place, find middle (slow/fast pointers), detect cycle (Floyd's)

---

### 4. Doubly Linked List
**What it is:** Like singly, but each node has both `prev` and `next` pointers.

**When to use:**
- Need O(1) insert AND delete from both ends
- Implementing LRU Cache (combined with HashMap)
- Browser history (forward + back navigation)

**Key advantage over singly:** `removeLast()` is O(1) instead of O(n)

---

### 5. Stack
**What it is:** LIFO (Last In, First Out). Think of a stack of plates.

**When to use:**
- Undo/redo functionality
- Balanced parentheses checking
- DFS traversal
- Expression evaluation

**Classic problems:** Valid parentheses, min stack, evaluate reverse Polish notation, daily temperatures, next greater element (monotonic stack)

---

### 6. Queue / Deque
**What it is:** FIFO (First In, First Out). Queue = coffee shop line.

**When to use:**
- BFS (Breadth-First Search)
- Level-order tree traversal
- Rate limiting, task scheduling

**Deque:** Double-ended queue — can add/remove from both ends. Used for sliding window problems (monotonic deque).

---

### 7. Hash Table (HashMap)
**What it is:** Maps keys to values using a hash function for O(1) average operations.

**When to use:**
- Fast lookup by key
- Counting frequencies
- Checking membership
- Caching / memoization

**Collision resolution:** Separate chaining (this implementation) or open addressing.

**Classic problems:** Two sum, group anagrams, longest consecutive sequence, subarray sum equals k

---

### 8. Binary Tree
**What it is:** Hierarchical tree where each node has at most 2 children.

**Traversal orders (memorize these!):**
| Order | Pattern | Use Case |
|-------|---------|----------|
| In-Order | Left → Root → Right | BST sorted output |
| Pre-Order | Root → Left → Right | Copy/serialize tree |
| Post-Order | Left → Right → Root | Delete tree, evaluate expression |
| Level-Order | Level by level (BFS) | Shortest path in tree |

**Classic problems:** Height, balance check, invert tree, symmetric, LCA, path sum, serialize/deserialize

---

### 9. Binary Search Tree (BST)
**What it is:** Binary tree where left subtree < root < right subtree at every node.

**When to use:**
- Need ordered data with fast insert/search/delete: O(log n) average
- Need k-th smallest/largest element
- Need floor/ceiling of a value

**Warning:** Degenerates to O(n) if inserted in sorted order. Use AVL or Red-Black Tree for guaranteed O(log n).

**Classic problems:** Validate BST, k-th smallest, BST to sorted array, sorted array to BST, LCA in BST

---

### 10. Heap (Priority Queue)
**What it is:** Complete binary tree satisfying heap property. Stored as array — no pointers needed!

| Type | Property | Root |
|------|---------|------|
| Min Heap | Parent ≤ Children | Minimum element |
| Max Heap | Parent ≥ Children | Maximum element |

**Array index formulas:**
- Parent of i: `(i - 1) / 2`
- Left child of i: `2 * i + 1`
- Right child of i: `2 * i + 2`

**When to use:**
- Need the minimum or maximum quickly: O(1)
- Top-K problems
- Dijkstra's shortest path
- Median from a data stream

**Counterintuitive trick:** Top-K LARGEST → use a MIN heap of size K!

**Classic problems:** Kth largest, top-K frequent, merge K sorted lists, median from stream

---

### 11. Graph
**What it is:** Set of vertices (nodes) connected by edges. Can be directed/undirected, weighted/unweighted.

**Representation:**
| Type | Space | Edge check | Best for |
|------|-------|-----------|---------|
| Adjacency Matrix | O(V²) | O(1) | Dense graphs |
| Adjacency List | O(V+E) | O(degree) | Sparse graphs (most interviews) |

**Traversals:**
- **BFS** (uses Queue): Shortest path, level-order, nearest neighbors first
- **DFS** (uses Stack/recursion): Cycle detection, topological sort, connected components

**Classic problems:** Number of islands, course schedule (topological sort), clone graph, word ladder (BFS), Pacific Atlantic water flow

---

## Time Complexity Cheat Sheet

| Data Structure | Access | Search | Insert | Delete | Space |
|----------------|--------|--------|--------|--------|-------|
| Array | O(1) | O(n) | O(n) | O(n) | O(n) |
| Dynamic Array | O(1) | O(n) | O(1)* | O(n) | O(n) |
| Singly Linked List | O(n) | O(n) | O(1)† | O(n) | O(n) |
| Doubly Linked List | O(n) | O(n) | O(1)† | O(1)† | O(n) |
| Stack | O(n) | O(n) | O(1) | O(1) | O(n) |
| Queue | O(n) | O(n) | O(1) | O(1) | O(n) |
| Hash Table | N/A | O(1)* | O(1)* | O(1)* | O(n) |
| Binary Tree | O(n) | O(n) | O(n) | O(n) | O(n) |
| BST (balanced) | O(log n) | O(log n) | O(log n) | O(log n) | O(n) |
| Min/Max Heap | O(1)‡ | O(n) | O(log n) | O(log n) | O(n) |
| Graph (Adj. List) | N/A | O(V+E) | O(1) | O(E) | O(V+E) |

`*` = amortized  
`†` = at head/tail with pointer  
`‡` = only for min (min heap) or max (max heap)

---

## Interview Problem Patterns

| Pattern | Data Structure | Example Problems |
|---------|---------------|-----------------|
| Two Pointers | Array | Two Sum II, Container with Most Water |
| Sliding Window | Array + Deque | Max Sliding Window, Longest Substring |
| Fast & Slow Pointers | Linked List | Cycle Detection, Find Middle |
| Monotonic Stack | Stack | Next Greater Element, Daily Temperatures |
| Top-K Elements | Heap | Kth Largest, Top-K Frequent |
| Merge Intervals | Array + Sorting | Meeting Rooms, Calendar |
| BFS | Queue + Graph | Shortest Path, Level Order |
| DFS | Stack/Recursion + Graph | Number of Islands, Path Sum |
| Trie | Tree | Word Search, Auto-complete |
| Union-Find | Array | Connected Components, Redundant Connection |
| Dynamic Programming | Array/HashMap | Fibonacci, Coin Change, LCS |

---

## Java Built-in Equivalents

| Our Implementation | Java Built-in |
|-------------------|--------------|
| StaticArray | `int[]`, `Integer[]` |
| DynamicArray | `ArrayList<T>` |
| SinglyLinkedList | `LinkedList<T>` |
| DoublyLinkedList | `LinkedList<T>` (implements Deque) |
| Stack | `Deque<T>` used as stack (`ArrayDeque`) |
| Queue | `Queue<T>`, `ArrayDeque<T>` |
| Deque | `ArrayDeque<T>` |
| HashTable | `HashMap<K,V>`, `HashSet<T>` |
| MinHeap | `PriorityQueue<T>` (default) |
| MaxHeap | `PriorityQueue<T>(Collections.reverseOrder())` |
| Graph | `Map<Integer, List<Integer>>` |

---

## License
MIT — Free to use for learning and interview preparation.
