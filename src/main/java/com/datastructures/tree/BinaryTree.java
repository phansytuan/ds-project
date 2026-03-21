package com.datastructures.tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A binary tree is a hierarchy where each node has at most a left and a right child.
 * Unlike a binary search tree, values are not ordered left-to-right; this class focuses on shape and traversals.
 */
public class BinaryTree {

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    TreeNode root;

    /** Collects values in order: left subtree, then this node, then right subtree. */
    public List<Integer> inOrder() {
        List<Integer> result = new ArrayList<>();
        inOrderVisit(root, result);
        return result;
    }

    private void inOrderVisit(TreeNode node, List<Integer> result) {
        if (node == null) {
            return;
        }
        inOrderVisit(node.left, result);
        result.add(node.val);
        inOrderVisit(node.right, result);
    }

    /** Collects values in order: this node, then left subtree, then right subtree. */
    public List<Integer> preOrder() {
        List<Integer> result = new ArrayList<>();
        preOrderVisit(root, result);
        return result;
    }

    private void preOrderVisit(TreeNode node, List<Integer> result) {
        if (node == null) {
            return;
        }
        result.add(node.val);
        preOrderVisit(node.left, result);
        preOrderVisit(node.right, result);
    }

    /** Collects values in order: left subtree, right subtree, then this node. */
    public List<Integer> postOrder() {
        List<Integer> result = new ArrayList<>();
        postOrderVisit(root, result);
        return result;
    }

    private void postOrderVisit(TreeNode node, List<Integer> result) {
        if (node == null) {
            return;
        }
        postOrderVisit(node.left, result);
        postOrderVisit(node.right, result);
        result.add(node.val);
    }

    /**
     * Level order (breadth-first): visit top level left-to-right, then the next level, and so on.
     * A queue holds the frontier of nodes waiting to be visited.
     */
    public List<List<Integer>> levelOrder() {
        List<List<Integer>> levels = new ArrayList<>();
        if (root == null) {
            return levels;
        }

        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            int nodesThisLevel = queue.size();
            List<Integer> levelValues = new ArrayList<>();

            for (int count = 0; count < nodesThisLevel; count++) {
                TreeNode node = queue.removeFirst();
                levelValues.add(node.val);
                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
            }
            levels.add(levelValues);
        }
        return levels;
    }

    /** Longest path from root down to a leaf, measured in edges; empty tree is -1. */
    public int height() {
        return heightOf(root);
    }

    private int heightOf(TreeNode node) {
        if (node == null) {
            return -1;
        }
        int leftHeight = heightOf(node.left);
        int rightHeight = heightOf(node.right);
        return 1 + Math.max(leftHeight, rightHeight);
    }

    /** True if every node has left and right subtree heights differing by at most one. */
    public boolean isBalanced() {
        return balancedHeight(root) != -1;
    }

    /**
     * Returns the height of {@code node}'s subtree, or -1 if that subtree is unbalanced.
     */
    private int balancedHeight(TreeNode node) {
        if (node == null) {
            return 0;
        }

        int leftHeight = balancedHeight(node.left);
        if (leftHeight == -1) {
            return -1;
        }

        int rightHeight = balancedHeight(node.right);
        if (rightHeight == -1) {
            return -1;
        }

        if (Math.abs(leftHeight - rightHeight) > 1) {
            return -1;
        }
        return 1 + Math.max(leftHeight, rightHeight);
    }

    /** Number of nodes on the longest root-to-leaf path. */
    public int maxDepth() {
        return maxDepthOf(root);
    }

    private int maxDepthOf(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(maxDepthOf(node.left), maxDepthOf(node.right));
    }

    /** True if both trees have the same shape and values at every position. */
    public static boolean isSameTree(TreeNode firstRoot, TreeNode secondRoot) {
        if (firstRoot == null && secondRoot == null) {
            return true;
        }
        if (firstRoot == null || secondRoot == null) {
            return false;
        }
        if (firstRoot.val != secondRoot.val) {
            return false;
        }
        return isSameTree(firstRoot.left, secondRoot.left)
            && isSameTree(firstRoot.right, secondRoot.right);
    }

    /** True if the tree is symmetric around its root (left mirror equals right). */
    public boolean isSymmetric() {
        return mirrors(root.left, root.right);
    }

    private boolean mirrors(TreeNode leftSide, TreeNode rightSide) {
        if (leftSide == null && rightSide == null) {
            return true;
        }
        if (leftSide == null || rightSide == null) {
            return false;
        }
        if (leftSide.val != rightSide.val) {
            return false;
        }
        return mirrors(leftSide.left, rightSide.right)
            && mirrors(leftSide.right, rightSide.left);
    }

    /** Swaps every node's left and right child (mirror image of the tree). */
    public void invert() {
        invertUnder(root);
    }

    private TreeNode invertUnder(TreeNode node) {
        if (node == null) {
            return null;
        }

        TreeNode newLeft = invertUnder(node.right);
        TreeNode newRight = invertUnder(node.left);
        node.left = newLeft;
        node.right = newRight;
        return node;
    }

    /** True if some root-to-leaf path sums to {@code targetSum}. */
    public boolean hasPathSum(int targetSum) {
        return hasPathSumFrom(root, targetSum);
    }

    private boolean hasPathSumFrom(TreeNode node, int remaining) {
        if (node == null) {
            return false;
        }

        remaining -= node.val;

        boolean isLeaf = node.left == null && node.right == null;
        if (isLeaf) {
            return remaining == 0;
        }

        return hasPathSumFrom(node.left, remaining)
            || hasPathSumFrom(node.right, remaining);
    }

    /** Lowest common ancestor of nodes {@code p} and {@code q} (same reference identity as in the tree). */
    public TreeNode lowestCommonAncestor(TreeNode p, TreeNode q) {
        return lowestCommonAncestorUnder(root, p, q);
    }

    private TreeNode lowestCommonAncestorUnder(TreeNode node, TreeNode p, TreeNode q) {
        if (node == null) {
            return null;
        }
        if (node == p || node == q) {
            return node;
        }

        TreeNode leftResult = lowestCommonAncestorUnder(node.left, p, q);
        TreeNode rightResult = lowestCommonAncestorUnder(node.right, p, q);

        if (leftResult != null && rightResult != null) {
            return node;
        }
        if (leftResult != null) {
            return leftResult;
        }
        return rightResult;
    }

    /** Prints a small ASCII-style picture of the tree. */
    public void printTree() {
        printSubtree(root, "", true);
    }

    private void printSubtree(TreeNode node, String indent, boolean isRightChild) {
        if (node == null) {
            return;
        }
        System.out.println(indent + (isRightChild ? "└── " : "├── ") + node.val);
        printSubtree(node.left, indent + (isRightChild ? "    " : "│   "), false);
        printSubtree(node.right, indent + (isRightChild ? "    " : "│   "), true);
    }

    public static void main(String[] args) {
        System.out.println("--- Binary tree demo ---");

        BinaryTree tree = new BinaryTree();
        tree.root = new TreeNode(1);
        tree.root.left = new TreeNode(2);
        tree.root.right = new TreeNode(3);
        tree.root.left.left = new TreeNode(4);
        tree.root.left.right = new TreeNode(5);
        tree.root.right.right = new TreeNode(6);

        System.out.println("Shape:");
        tree.printTree();

        System.out.println("In-order:    " + tree.inOrder());
        System.out.println("Pre-order:   " + tree.preOrder());
        System.out.println("Post-order:  " + tree.postOrder());
        System.out.println("Level-order: " + tree.levelOrder());

        System.out.println("Height: " + tree.height());
        System.out.println("Max depth: " + tree.maxDepth());
        System.out.println("Balanced? " + tree.isBalanced());
        System.out.println("Symmetric? " + tree.isSymmetric());
        System.out.println("Path sum 8 along 1->2->5? " + tree.hasPathSum(8));

        System.out.println();
        System.out.println("Invert (mirror) tree.");
        tree.invert();
        System.out.println("In-order after invert: " + tree.inOrder());
        tree.printTree();
    }
}
