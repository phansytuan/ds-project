package com.datastructures.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================
 * DATA STRUCTURE: Binary Search Tree (BST)
 * ============================================================
 *
 * CONCEPT:
 *   A BST is a binary tree with a special ORDERING PROPERTY:
 *
 *   For every node N:
 *     - ALL nodes in N's LEFT subtree have values < N.val
 *     - ALL nodes in N's RIGHT subtree have values > N.val
 *
 *   This property is maintained RECURSIVELY for every node.
 *
 *   Example BST:
 *          [8]
 *         /   \
 *       [3]   [10]
 *      /   \     \
 *    [1]   [6]   [14]
 *         /   \   /
 *       [4]  [7][13]
 *
 *   BST Property check for root [8]:
 *   - Left subtree: 3, 1, 6, 4, 7 — ALL < 8 ✓
 *   - Right subtree: 10, 14, 13 — ALL > 8 ✓
 *
 * KEY INSIGHT: In-order traversal of a BST yields SORTED output!
 *   In-order of above BST: 1, 3, 4, 6, 7, 8, 10, 13, 14
 *
 * TIME COMPLEXITY:
 *   Balanced BST:  O(log n) for search, insert, delete
 *   Worst case (skewed/degenerate tree): O(n)
 *
 *   Worst case happens when insertions are in sorted order:
 *   insert 1, 2, 3, 4, 5 → a right-leaning linked list!
 *   Fix: use self-balancing trees (AVL, Red-Black Tree)
 *
 * SPACE COMPLEXITY: O(n)
 *
 * INTERVIEW TIPS:
 *   - BST validation is a classic trick question:
 *     You must track min/max bounds, not just compare parent-child
 *   - k-th smallest element → in-order traversal
 *   - Convert sorted array to balanced BST → divide and conquer
 * ============================================================
 */
public class BinarySearchTree {

    private BinaryTree.TreeNode root;

    // ──────────────────────────────────────────────
    // CORE BST OPERATIONS
    // ──────────────────────────────────────────────

    /**
     * INSERT: Add a value to the BST while maintaining BST property.
     * Time: O(h) where h = height (O(log n) balanced, O(n) worst)
     *
     * Navigate: go LEFT if value < node, RIGHT if value > node.
     * Insert when we hit null.
     */
    public void insert(int value) {
        root = insertHelper(root, value);
    }

    private BinaryTree.TreeNode insertHelper(BinaryTree.TreeNode node, int value) {
        // BASE CASE: found the insertion point
        if (node == null) return new BinaryTree.TreeNode(value);

        if (value < node.val) {
            node.left = insertHelper(node.left, value);  // Go left
        } else if (value > node.val) {
            node.right = insertHelper(node.right, value); // Go right
        }
        // value == node.val: duplicate — do nothing (or update)

        return node; // Return unchanged node (enables recursive re-linking)
    }

    /**
     * SEARCH: Find a value in the BST.
     * Time: O(h)
     */
    public boolean search(int value) {
        return searchHelper(root, value);
    }

    private boolean searchHelper(BinaryTree.TreeNode node, int value) {
        if (node == null) return false;       // Not found
        if (value == node.val) return true;   // Found!
        if (value < node.val) return searchHelper(node.left, value);
        return searchHelper(node.right, value);
    }

    /**
     * DELETE: Remove a value from the BST.
     * Time: O(h)
     *
     * Three cases to handle:
     *   1. Node is a LEAF           → just remove it
     *   2. Node has ONE child       → replace node with that child
     *   3. Node has TWO children    → replace with in-order SUCCESSOR
     *                                  (smallest node in right subtree)
     *
     * Case 3 example: deleting [6] from:
     *       [8]                    [8]
     *      /   \                  /   \
     *    [6]   [10]  →  delete  [7]   [10]
     *   /   \          [6]     /
     * [4]   [7]       [4]
     *
     * Successor of 6 is 7 (smallest in right subtree {7}).
     * Replace 6's value with 7, then delete 7 from right subtree.
     */
    public void delete(int value) {
        root = deleteHelper(root, value);
    }

    private BinaryTree.TreeNode deleteHelper(BinaryTree.TreeNode node, int value) {
        if (node == null) return null; // Value not found

        if (value < node.val) {
            node.left = deleteHelper(node.left, value); // Go left
        } else if (value > node.val) {
            node.right = deleteHelper(node.right, value); // Go right
        } else {
            // Found the node to delete!

            // CASE 1 & 2: Zero or one child
            if (node.left == null) return node.right;  // Replace with right (or null)
            if (node.right == null) return node.left;  // Replace with left

            // CASE 3: Two children — find in-order successor (min of right subtree)
            BinaryTree.TreeNode successor = findMin(node.right);
            node.val = successor.val;  // Copy successor's value here

            // Delete the successor from the right subtree
            // (successor has at most one right child — it's the minimum!)
            node.right = deleteHelper(node.right, successor.val);
        }

        return node;
    }

    /**
     * FIND MIN: Leftmost node (smallest value).
     * Keep going left until there's no left child.
     */
    public int findMin() {
        if (root == null) throw new IllegalStateException("BST is empty");
        return findMin(root).val;
    }

    private BinaryTree.TreeNode findMin(BinaryTree.TreeNode node) {
        while (node.left != null) node = node.left; // Leftmost = minimum
        return node;
    }

    /**
     * FIND MAX: Rightmost node (largest value).
     */
    public int findMax() {
        if (root == null) throw new IllegalStateException("BST is empty");
        BinaryTree.TreeNode node = root;
        while (node.right != null) node = node.right; // Rightmost = maximum
        return node.val;
    }

    // ──────────────────────────────────────────────
    // CLASSIC BST INTERVIEW ALGORITHMS
    // ──────────────────────────────────────────────

    /**
     * IS VALID BST: Verify the BST property holds for every node.
     *
     * COMMON MISTAKE: Just checking node.val > node.left.val doesn't work!
     * Example:  [10]            This looks valid node-by-node,
     *          /    \           but 6 > 5, violating the BST property
     *        [5]   [15]         for the subtree rooted at 10.
     *           \
     *           [6]  ← 6 > 5 (parent) but 6 < 10 (grandparent) = INVALID!
     *
     * FIX: Pass down min/max BOUNDS that each subtree's values must respect.
     * Time: O(n)
     */
    public boolean isValidBST() {
        return isValidHelper(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean isValidHelper(BinaryTree.TreeNode node, long min, long max) {
        if (node == null) return true; // Empty subtree is valid

        // This node's value must be strictly within (min, max)
        if (node.val <= min || node.val >= max) return false;

        // Left subtree: values must be < node.val (new max = node.val)
        // Right subtree: values must be > node.val (new min = node.val)
        return isValidHelper(node.left,  min, node.val)
            && isValidHelper(node.right, node.val, max);
    }

    /**
     * K-TH SMALLEST: Find the k-th smallest element in the BST.
     * Key insight: In-order traversal of a BST gives sorted order!
     * Time: O(n) worst case, O(k + h) if you stop early.
     */
    public int kthSmallest(int k) {
        int[] counter = {0};  // Use array to mutate inside lambda/inner class
        int[] result  = {-1};
        kthHelper(root, k, counter, result);
        return result[0];
    }

    private void kthHelper(BinaryTree.TreeNode node, int k, int[] count, int[] result) {
        if (node == null || count[0] >= k) return;

        kthHelper(node.left, k, count, result); // Go as far left as possible
        count[0]++;                             // Visiting this node in-order
        if (count[0] == k) {
            result[0] = node.val;              // Found the k-th smallest!
            return;
        }
        kthHelper(node.right, k, count, result);
    }

    /**
     * LOWEST COMMON ANCESTOR for BST:
     * Much simpler than the general binary tree case!
     * Use the BST property to navigate.
     *
     * If both p and q are < node → LCA is in left subtree
     * If both p and q are > node → LCA is in right subtree
     * Otherwise → node is the LCA (they diverge here)
     * Time: O(h)
     */
    public int lca(int p, int q) {
        return lcaHelper(root, p, q);
    }

    private int lcaHelper(BinaryTree.TreeNode node, int p, int q) {
        if (node == null) return -1;

        if (p < node.val && q < node.val) {
            return lcaHelper(node.left, p, q);  // Both in left subtree
        }
        if (p > node.val && q > node.val) {
            return lcaHelper(node.right, p, q); // Both in right subtree
        }
        return node.val; // They diverge here → this is the LCA
    }

    /**
     * SORTED ARRAY TO BALANCED BST:
     * Construct a height-balanced BST from a sorted array.
     * Strategy: Always pick the MIDDLE element as root.
     * This ensures equal distribution left/right → balanced.
     * Time: O(n), Space: O(log n) for recursion stack
     */
    public static BinarySearchTree sortedArrayToBST(int[] nums) {
        BinarySearchTree bst = new BinarySearchTree();
        bst.root = buildHelper(nums, 0, nums.length - 1);
        return bst;
    }

    private static BinaryTree.TreeNode buildHelper(int[] nums, int left, int right) {
        if (left > right) return null;

        int mid = left + (right - left) / 2;
        BinaryTree.TreeNode node = new BinaryTree.TreeNode(nums[mid]);

        node.left  = buildHelper(nums, left, mid - 1);   // Left half
        node.right = buildHelper(nums, mid + 1, right);  // Right half
        return node;
    }

    // ──────────────────────────────────────────────
    // UTILITY
    // ──────────────────────────────────────────────

    /** In-order traversal gives sorted output for BST. */
    public List<Integer> inOrder() {
        List<Integer> result = new ArrayList<>();
        inOrderHelper(root, result);
        return result;
    }

    private void inOrderHelper(BinaryTree.TreeNode node, List<Integer> result) {
        if (node == null) return;
        inOrderHelper(node.left, result);
        result.add(node.val);
        inOrderHelper(node.right, result);
    }

    public void printTree() {
        printHelper(root, "", true);
    }

    private void printHelper(BinaryTree.TreeNode node, String indent, boolean isRight) {
        if (node == null) return;
        System.out.println(indent + (isRight ? "└── " : "├── ") + node.val);
        printHelper(node.left,  indent + (isRight ? "    " : "│   "), false);
        printHelper(node.right, indent + (isRight ? "    " : "│   "), true);
    }

    // ──────────────────────────────────────────────
    // DEMO
    // ──────────────────────────────────────────────

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║    BINARY SEARCH TREE DEMO       ║");
        System.out.println("╚══════════════════════════════════╝\n");

        BinarySearchTree bst = new BinarySearchTree();
        int[] values = {8, 3, 10, 1, 6, 14, 4, 7, 13};
        for (int v : values) bst.insert(v);

        System.out.println("BST structure:");
        bst.printTree();

        System.out.println("\nIn-Order (should be sorted): " + bst.inOrder());
        System.out.println("Min: " + bst.findMin() + ", Max: " + bst.findMax());
        System.out.println("search(6): " + bst.search(6));
        System.out.println("search(9): " + bst.search(9));
        System.out.println("Is Valid BST: " + bst.isValidBST());

        System.out.println("\n--- k-th Smallest ---");
        System.out.println("1st smallest: " + bst.kthSmallest(1)); // 1
        System.out.println("3rd smallest: " + bst.kthSmallest(3)); // 4

        System.out.println("\n--- LCA ---");
        System.out.println("LCA(1, 6):  " + bst.lca(1, 6));  // 3
        System.out.println("LCA(6, 14): " + bst.lca(6, 14)); // 8

        System.out.println("\n--- Delete ---");
        bst.delete(6);
        System.out.println("After delete(6) - In-Order: " + bst.inOrder());
        bst.printTree();

        System.out.println("\n--- Sorted Array → Balanced BST ---");
        int[] sorted = {1, 2, 3, 4, 5, 6, 7};
        BinarySearchTree balanced = BinarySearchTree.sortedArrayToBST(sorted);
        balanced.printTree();
        System.out.println("In-Order: " + balanced.inOrder());
    }
}
