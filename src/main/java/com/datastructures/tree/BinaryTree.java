package com.datastructures.tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * ============================================================
 * DATA STRUCTURE: Binary Tree
 * ============================================================
 *
 * CONCEPT:
 *   A tree is a hierarchical data structure. A BINARY TREE is
 *   a tree where each node has at most TWO children: left and right.
 *
 *   Anatomy:
 *          [1]         ← root (depth 0)
 *         /   \
 *       [2]   [3]      ← internal nodes (depth 1)
 *      /   \     \
 *    [4]   [5]   [6]   ← leaves (depth 2)
 *
 *   Terminology:
 *     Root   = topmost node (no parent)
 *     Leaf   = node with no children
 *     Height = longest path from root to a leaf
 *     Depth  = distance from root to a node
 *     Level  = set of all nodes at depth d
 *
 * TRAVERSAL ORDERS — this is crucial for interviews!
 *   Given tree:     [1]
 *                  /   \
 *                [2]   [3]
 *               /   \
 *             [4]   [5]
 *
 *   In-Order   (Left, Root, Right): 4 → 2 → 5 → 1 → 3
 *   Pre-Order  (Root, Left, Right): 1 → 2 → 4 → 5 → 3
 *   Post-Order (Left, Right, Root): 4 → 5 → 2 → 3 → 1
 *   Level-Order (BFS):              1 → 2 → 3 → 4 → 5
 *
 * REAL-WORLD USE CASES:
 *   - File system directory trees
 *   - HTML/XML DOM structure
 *   - Abstract Syntax Trees (ASTs) in compilers
 *   - Decision trees in ML
 *   - Game state trees (chess, minimax)
 *
 * TIME COMPLEXITY:
 *   All operations: O(n) for a general binary tree
 *   (BST gives us O(log n) for ordered operations)
 *
 * SPACE COMPLEXITY:
 *   O(n) for the tree itself
 *   O(h) for recursion stack, where h = height
 *     Balanced tree: h = O(log n)
 *     Skewed tree:   h = O(n)
 *
 * INTERVIEW TIPS:
 *   - Most tree problems are solved with DFS (recursion)
 *   - Always define the BASE CASE (null node) first
 *   - Level-order traversal = BFS with a queue
 *   - Know the 4 traversal orders cold
 * ============================================================
 */
public class BinaryTree {

    // ──────────────────────────────────────────────
    // INNER CLASS: TreeNode
    // ──────────────────────────────────────────────

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    // ──────────────────────────────────────────────
    // FIELDS
    // ──────────────────────────────────────────────

    TreeNode root; // Package-visible for BST subclass

    // ──────────────────────────────────────────────
    // TRAVERSALS
    // ──────────────────────────────────────────────

    /**
     * IN-ORDER traversal: Left → Root → Right
     * Special property: For a BST, this gives elements in SORTED ORDER!
     * Time: O(n), Space: O(h)
     */
    public List<Integer> inOrder() {
        List<Integer> result = new ArrayList<>();
        inOrderHelper(root, result);
        return result;
    }

    private void inOrderHelper(TreeNode node, List<Integer> result) {
        if (node == null) return; // BASE CASE: empty subtree
        inOrderHelper(node.left, result);  // 1. Recurse LEFT
        result.add(node.val);              // 2. Visit ROOT
        inOrderHelper(node.right, result); // 3. Recurse RIGHT
    }

    /**
     * PRE-ORDER traversal: Root → Left → Right
     * Used to COPY or SERIALIZE a tree (root first so you can reconstruct).
     * Time: O(n), Space: O(h)
     */
    public List<Integer> preOrder() {
        List<Integer> result = new ArrayList<>();
        preOrderHelper(root, result);
        return result;
    }

    private void preOrderHelper(TreeNode node, List<Integer> result) {
        if (node == null) return;
        result.add(node.val);              // 1. Visit ROOT first
        preOrderHelper(node.left, result); // 2. Recurse LEFT
        preOrderHelper(node.right, result);// 3. Recurse RIGHT
    }

    /**
     * POST-ORDER traversal: Left → Right → Root
     * Used to DELETE a tree (children before parent)
     * or evaluate expression trees (operands before operator).
     * Time: O(n), Space: O(h)
     */
    public List<Integer> postOrder() {
        List<Integer> result = new ArrayList<>();
        postOrderHelper(root, result);
        return result;
    }

    private void postOrderHelper(TreeNode node, List<Integer> result) {
        if (node == null) return;
        postOrderHelper(node.left, result); // 1. Recurse LEFT
        postOrderHelper(node.right, result);// 2. Recurse RIGHT
        result.add(node.val);              // 3. Visit ROOT last
    }

    /**
     * LEVEL-ORDER (BFS) traversal: Level by level, left to right.
     * Uses a Queue — the hallmark of BFS!
     * Returns each level as a separate list.
     * Time: O(n), Space: O(w) where w = max width of the tree
     */
    public List<List<Integer>> levelOrder() {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;

        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size(); // Number of nodes on this level
            List<Integer> level = new ArrayList<>();

            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll(); // Remove from front of queue
                level.add(node.val);

                // Enqueue children for the NEXT level
                if (node.left  != null) queue.add(node.left);
                if (node.right != null) queue.add(node.right);
            }
            result.add(level);
        }
        return result;
    }

    // ──────────────────────────────────────────────
    // CLASSIC TREE ALGORITHMS
    // ──────────────────────────────────────────────

    /**
     * HEIGHT: The number of edges on the longest path from root to a leaf.
     * Time: O(n) — must visit every node.
     * Space: O(h) — recursion stack.
     *
     * Recurrence: height(node) = 1 + max(height(left), height(right))
     * Base case:  height(null) = -1 (empty tree has height -1)
     */
    public int height() {
        return heightHelper(root);
    }

    private int heightHelper(TreeNode node) {
        if (node == null) return -1; // Empty subtree contributes -1
        int leftHeight  = heightHelper(node.left);
        int rightHeight = heightHelper(node.right);
        return 1 + Math.max(leftHeight, rightHeight);
    }

    /**
     * IS BALANCED: True if the tree's height difference between any
     * left and right subtrees is at most 1.
     * Time: O(n) — optimized single-pass approach.
     */
    public boolean isBalanced() {
        return checkBalance(root) != -1;
    }

    /**
     * Returns the height of the subtree, or -1 if it's unbalanced.
     * This elegantly combines height calculation + balance check in one pass.
     */
    private int checkBalance(TreeNode node) {
        if (node == null) return 0;

        int leftHeight  = checkBalance(node.left);
        if (leftHeight == -1) return -1; // Early exit if left is unbalanced

        int rightHeight = checkBalance(node.right);
        if (rightHeight == -1) return -1; // Early exit if right is unbalanced

        if (Math.abs(leftHeight - rightHeight) > 1) return -1; // This node is unbalanced

        return 1 + Math.max(leftHeight, rightHeight);
    }

    /**
     * MAX DEPTH: Number of nodes along the longest root-to-leaf path.
     * Time: O(n), Space: O(h)
     */
    public int maxDepth() {
        return maxDepthHelper(root);
    }

    private int maxDepthHelper(TreeNode node) {
        if (node == null) return 0;
        return 1 + Math.max(maxDepthHelper(node.left), maxDepthHelper(node.right));
    }

    /**
     * IS SAME TREE: Returns true if two trees have the same structure and values.
     * Classic recursive problem — a great template.
     * Time: O(n)
     */
    public static boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null && q == null) return true;   // Both null → equal
        if (p == null || q == null) return false;  // One null → not equal
        if (p.val != q.val) return false;           // Different values

        // Both nodes exist and have the same value → check children
        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }

    /**
     * IS SYMMETRIC: Returns true if the tree is a mirror of itself.
     * Input:    [1]          Input:    [1]
     *          /   \                  /   \
     *        [2]   [2]    vs        [2]   [2]
     *       /  \  /  \              \       \
     *      [3][4][4][3]             [3]      [3]
     *      → true                   → false
     * Time: O(n)
     */
    public boolean isSymmetric() {
        return isMirror(root, root);
    }

    private boolean isMirror(TreeNode left, TreeNode right) {
        if (left == null && right == null) return true;
        if (left == null || right == null) return false;
        // Mirror: left.val == right.val
        //         AND left.left mirrors right.right
        //         AND left.right mirrors right.left
        return left.val == right.val
            && isMirror(left.left, right.right)
            && isMirror(left.right, right.left);
    }

    /**
     * INVERT (MIRROR) TREE: Flip the tree left-right.
     * Input:   [4]         Output:  [4]
     *         /   \                /   \
     *       [2]   [7]           [7]   [2]
     *      /  \  /  \          /  \  /  \
     *    [1][3][6][9]        [9][6][3][1]
     * Time: O(n)
     */
    public void invert() {
        invertHelper(root);
    }

    private TreeNode invertHelper(TreeNode node) {
        if (node == null) return null;

        // Recursively invert both subtrees
        TreeNode left  = invertHelper(node.left);
        TreeNode right = invertHelper(node.right);

        // Swap left and right children
        node.left  = right;
        node.right = left;
        return node;
    }

    /**
     * PATH SUM: Does any root-to-leaf path sum to targetSum?
     * Input: tree=[5,4,8,11,null,13,4,7,2], targetSum=22 → true (5+4+11+2)
     * Time: O(n)
     */
    public boolean hasPathSum(int targetSum) {
        return pathSumHelper(root, targetSum);
    }

    private boolean pathSumHelper(TreeNode node, int remaining) {
        if (node == null) return false;

        remaining -= node.val;

        // Leaf node: check if we've hit our target
        if (node.left == null && node.right == null) {
            return remaining == 0;
        }

        // Otherwise, check either subtree
        return pathSumHelper(node.left, remaining)
            || pathSumHelper(node.right, remaining);
    }

    /**
     * LOWEST COMMON ANCESTOR (LCA): Given two nodes p and q,
     * find their lowest (deepest) common ancestor.
     * Time: O(n)
     */
    public TreeNode lowestCommonAncestor(TreeNode p, TreeNode q) {
        return lcaHelper(root, p, q);
    }

    private TreeNode lcaHelper(TreeNode node, TreeNode p, TreeNode q) {
        if (node == null) return null;        // Fell off the tree
        if (node == p || node == q) return node; // Found one of the targets

        TreeNode left  = lcaHelper(node.left,  p, q);
        TreeNode right = lcaHelper(node.right, p, q);

        if (left != null && right != null) return node; // p is in one subtree, q in the other
        return left != null ? left : right; // Both in the same subtree
    }

    // ──────────────────────────────────────────────
    // UTILITY
    // ──────────────────────────────────────────────

    /** Print tree visually (simple, works for small trees). */
    public void printTree() {
        printHelper(root, "", true);
    }

    private void printHelper(TreeNode node, String indent, boolean isRight) {
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
        System.out.println("║       BINARY TREE DEMO           ║");
        System.out.println("╚══════════════════════════════════╝\n");

        /*
         *     Build this tree:
         *           [1]
         *          /   \
         *        [2]   [3]
         *       /   \     \
         *     [4]   [5]   [6]
         */
        BinaryTree tree = new BinaryTree();
        tree.root = new TreeNode(1);
        tree.root.left  = new TreeNode(2);
        tree.root.right = new TreeNode(3);
        tree.root.left.left  = new TreeNode(4);
        tree.root.left.right = new TreeNode(5);
        tree.root.right.right = new TreeNode(6);

        System.out.println("Tree structure:");
        tree.printTree();

        System.out.println("\nIn-Order    (L→Root→R): " + tree.inOrder());
        System.out.println("Pre-Order   (Root→L→R): " + tree.preOrder());
        System.out.println("Post-Order  (L→R→Root): " + tree.postOrder());
        System.out.println("Level-Order (BFS):       " + tree.levelOrder());

        System.out.println("\nHeight:       " + tree.height());
        System.out.println("Max Depth:    " + tree.maxDepth());
        System.out.println("Is Balanced:  " + tree.isBalanced());
        System.out.println("Is Symmetric: " + tree.isSymmetric());
        System.out.println("Has path 1→2→5 (sum=8)? " + tree.hasPathSum(8));

        System.out.println("\n--- Invert Tree ---");
        tree.invert();
        System.out.println("After invert, In-Order: " + tree.inOrder());
        tree.printTree();
    }
}
