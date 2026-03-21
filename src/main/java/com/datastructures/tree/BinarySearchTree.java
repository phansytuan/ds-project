package com.datastructures.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * A binary search tree keeps an ordering rule: every value in the left subtree is smaller than the node,
 * and every value in the right subtree is larger. In-order traversal visits values in sorted order.
 */
public class BinarySearchTree {

    private BinaryTree.TreeNode root;

    /** Inserts {@code value} if it is not already present (duplicates are ignored). */
    public void insert(int value) {
        root = insertUnder(root, value);
    }

    private BinaryTree.TreeNode insertUnder(BinaryTree.TreeNode node, int value) {
        if (node == null) {
            return new BinaryTree.TreeNode(value);
        }
        if (value < node.val) {
            node.left = insertUnder(node.left, value);
        } else if (value > node.val) {
            node.right = insertUnder(node.right, value);
        }
        return node;
    }

    /** Returns true if {@code value} appears in the tree. */
    public boolean search(int value) {
        return searchUnder(root, value);
    }

    private boolean searchUnder(BinaryTree.TreeNode node, int value) {
        if (node == null) {
            return false;
        }
        if (value == node.val) {
            return true;
        }
        if (value < node.val) {
            return searchUnder(node.left, value);
        }
        return searchUnder(node.right, value);
    }

    /**
     * Removes {@code value}. A leaf drops off; one child promotes; two children copy the successor
     * (smallest in the right subtree) then delete that successor.
     */
    public void delete(int value) {
        root = deleteUnder(root, value);
    }

    private BinaryTree.TreeNode deleteUnder(BinaryTree.TreeNode node, int value) {
        if (node == null) {
            return null;
        }

        if (value < node.val) {
            node.left = deleteUnder(node.left, value);
            return node;
        }
        if (value > node.val) {
            node.right = deleteUnder(node.right, value);
            return node;
        }

        if (node.left == null) {
            return node.right;
        }
        if (node.right == null) {
            return node.left;
        }

        BinaryTree.TreeNode successor = minimumNode(node.right);
        node.val = successor.val;
        node.right = deleteUnder(node.right, successor.val);
        return node;
    }

    public int findMin() {
        if (root == null) {
            throw new IllegalStateException("BST is empty.");
        }
        return minimumNode(root).val;
    }

    private BinaryTree.TreeNode minimumNode(BinaryTree.TreeNode node) {
        BinaryTree.TreeNode current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    public int findMax() {
        if (root == null) {
            throw new IllegalStateException("BST is empty.");
        }
        BinaryTree.TreeNode current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.val;
    }

    /**
     * Valid BST means every subtree lies inside an allowed (min, max) open range passed down from ancestors.
     */
    public boolean isValidBST() {
        return isValidSubtree(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean isValidSubtree(BinaryTree.TreeNode node, long minAllowed, long maxAllowed) {
        if (node == null) {
            return true;
        }
        if (node.val <= minAllowed || node.val >= maxAllowed) {
            return false;
        }
        boolean leftOk = isValidSubtree(node.left, minAllowed, node.val);
        boolean rightOk = isValidSubtree(node.right, node.val, maxAllowed);
        return leftOk && rightOk;
    }

    /** k is 1-based: k=1 is the smallest element. */
    public int kthSmallest(int k) {
        VisitCounter counter = new VisitCounter();
        KthAnswer answer = new KthAnswer();
        kthSmallestVisit(root, k, counter, answer);
        return answer.value;
    }

    private static final class VisitCounter {
        int visitedCount;
    }

    private static final class KthAnswer {
        int value = -1;
    }

    private void kthSmallestVisit(BinaryTree.TreeNode node, int k, VisitCounter counter, KthAnswer answer) {
        if (node == null) {
            return;
        }
        if (counter.visitedCount >= k) {
            return;
        }

        kthSmallestVisit(node.left, k, counter, answer);
        counter.visitedCount++;
        if (counter.visitedCount == k) {
            answer.value = node.val;
            return;
        }
        kthSmallestVisit(node.right, k, counter, answer);
    }

    /** Lowest common ancestor for two values that exist in the BST. */
    public int lca(int p, int q) {
        return lcaUnder(root, p, q);
    }

    private int lcaUnder(BinaryTree.TreeNode node, int p, int q) {
        if (node == null) {
            return -1;
        }
        if (p < node.val && q < node.val) {
            return lcaUnder(node.left, p, q);
        }
        if (p > node.val && q > node.val) {
            return lcaUnder(node.right, p, q);
        }
        return node.val;
    }

    /** Builds a balanced BST from a sorted array by always choosing the middle element as the subtree root. */
    public static BinarySearchTree sortedArrayToBST(int[] sortedValues) {
        BinarySearchTree tree = new BinarySearchTree();
        tree.root = buildBalanced(sortedValues, 0, sortedValues.length - 1);
        return tree;
    }

    private static BinaryTree.TreeNode buildBalanced(int[] sortedValues, int leftIndex, int rightIndex) {
        if (leftIndex > rightIndex) {
            return null;
        }
        int middleIndex = leftIndex + (rightIndex - leftIndex) / 2;
        BinaryTree.TreeNode node = new BinaryTree.TreeNode(sortedValues[middleIndex]);
        node.left = buildBalanced(sortedValues, leftIndex, middleIndex - 1);
        node.right = buildBalanced(sortedValues, middleIndex + 1, rightIndex);
        return node;
    }

    public List<Integer> inOrder() {
        List<Integer> result = new ArrayList<>();
        inOrderVisit(root, result);
        return result;
    }

    private void inOrderVisit(BinaryTree.TreeNode node, List<Integer> result) {
        if (node == null) {
            return;
        }
        inOrderVisit(node.left, result);
        result.add(node.val);
        inOrderVisit(node.right, result);
    }

    public void printTree() {
        printSubtree(root, "", true);
    }

    private void printSubtree(BinaryTree.TreeNode node, String indent, boolean isRightChild) {
        if (node == null) {
            return;
        }
        System.out.println(indent + (isRightChild ? "└── " : "├── ") + node.val);
        printSubtree(node.left, indent + (isRightChild ? "    " : "│   "), false);
        printSubtree(node.right, indent + (isRightChild ? "    " : "│   "), true);
    }

    public static void main(String[] args) {
        System.out.println("--- Binary search tree demo ---");

        BinarySearchTree bst = new BinarySearchTree();
        int[] values = {8, 3, 10, 1, 6, 14, 4, 7, 13};
        for (int index = 0; index < values.length; index++) {
            bst.insert(values[index]);
        }

        System.out.println("Tree:");
        bst.printTree();

        System.out.println("In-order (sorted): " + bst.inOrder());
        System.out.println("Min: " + bst.findMin() + ", Max: " + bst.findMax());
        System.out.println("search(6): " + bst.search(6));
        System.out.println("search(9): " + bst.search(9));
        System.out.println("Valid BST? " + bst.isValidBST());

        System.out.println("1st smallest: " + bst.kthSmallest(1));
        System.out.println("3rd smallest: " + bst.kthSmallest(3));

        System.out.println("LCA(1, 6): " + bst.lca(1, 6));
        System.out.println("LCA(6, 14): " + bst.lca(6, 14));

        bst.delete(6);
        System.out.println("After delete 6, in-order: " + bst.inOrder());
        bst.printTree();

        System.out.println();
        int[] sorted = {1, 2, 3, 4, 5, 6, 7};
        BinarySearchTree balanced = BinarySearchTree.sortedArrayToBST(sorted);
        System.out.println("Balanced tree from sorted array:");
        balanced.printTree();
        System.out.println("In-order: " + balanced.inOrder());
    }
}
