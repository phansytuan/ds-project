package com.datastructures.stack;

/**
 * ============================================================
 * DATA STRUCTURE: Stack
 * ============================================================

 * CONCEPT:
 *   A stack is a LIFO (Last In, First Out) data structure.
 *   Think of a stack of plates — you can only add / remove
 *   from the TOP. The last plate you put on is the first
 *   one you take off.

 *   Operations:
 *     push(x) → add x to the top
 *     pop()   → remove and return the top
 *     peek()  → look at the top without removing

 *   Visual (push 10, 20, 30):
 *     [30] ← top  (most recently pushed)
 *     [20]
 *     [10] ← bottom (first pushed)

 * REAL-WORLD USE CASES:
 *   - Function call stack (how your program's methods work!)
 *   - Undo/redo in editors (Ctrl+Z)
 *   - Browser back button
 *   - Expression evaluation and parentheses matching
 *   - DFS (Depth-First Search) in graphs
 *   - Syntax parsing in compilers

 * TIME COMPLEXITY:
 *   Push  : O(1)
 *   Pop   : O(1)
 *   Peek  : O(1)
 *   Search: O(n)

 * SPACE COMPLEXITY: O(n)

 * INTERVIEW TIPS:
 *   - Most common stack problems: valid parentheses, min stack,
 *     evaluate reverse polish notation, daily temperatures
 *   - "Monotonic stack" is a powerful pattern for problems involving "next greater element"
 *   - Recursive algorithms can always be converted to use
 *     an explicit stack
 * ============================================================
 */
public class Stack<T> {

    // ──────────────────────────────────────────────
    // IMPLEMENTATION: Array-based Stack
    // ──────────────────────────────────────────────
    // We use an array for O(1) access to the top & O(1) amortized push.
    // The 'top' index tracks where the next element should go.

    private static final int DEFAULT_CAPACITY = 16;
    private Object[] data;
    private int top; // Index of the topmost element (-1 if empty)

    public Stack() {
        data = new Object[DEFAULT_CAPACITY];
        top = -1;
    }

    // ──────────────────────────────────────────────
    // CORE OPERATIONS
    // ──────────────────────────────────────────────

    /**
     * PUSH: Add element to the top of the stack.
     * Time: O(1) amortized
     */
    public void push(T value) {
        if (top == data.length - 1) {
            grow(); // Resize when full
        }
        data[++top] = value; // Increment top FIRST, then assign
    }

    /**
     * POP: Remove & return the top element.
     * Time: O(1)
     */
    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty()) throw new IllegalStateException("Stack underflow! Stack is empty.");
        T value = (T) data[top];
        data[top] = null; // Let GC collect it (prevent memory leak)
        top--;
        return value;
    }

    /**
     * PEEK: Look at the top element WITHOUT removing it.
     * Time: O(1)
     */
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) throw new IllegalStateException("Stack is empty.");
        return (T) data[top];
    }

    public boolean isEmpty() { return top == -1; }
    public int size() { return top + 1; }

    /** Double the array when full. */
    private void grow() {
        Object[] newData = new Object[data.length * 2];
        System.arraycopy(data, 0, newData, 0, data.length);
        data = newData;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "Stack []";
        StringBuilder sb = new StringBuilder("Stack [top→");

        for (int i = top; i >= 0; i--) {
            sb.append(data[i]);
            if (i > 0) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    // ══════════════════════════════════════════════
    // CLASSIC INTERVIEW PROBLEMS USING STACK
    // ══════════════════════════════════════════════

    /**
     * PROBLEM 1: Valid Parentheses
     * Given a string of brackets, return true if they are balanced.
     * Input: "({[]})" → true
     * Input: "([)]"   → false
     * Input: "{[]}"   → true

     * Approach: push opening brackets, pop when we see a closing bracket.
     * If they match → continue. If not → invalid.
     * Time: O(n), Space: O(n)
     */
    public static boolean isValidParentheses(String s) {
        Stack<Character> stack = new Stack<>();

        for (char c : s.toCharArray()) {
            // Push all opening brackets
            if (c == '(' || c == '{' || c == '[') {
                stack.push(c);
            }
            // For closing brackets, check if the top matches
            else if (c == ')' || c == '}' || c == ']') {
                if (stack.isEmpty())  return false;  // No matching opening bracket
                char top = stack.pop();
                // Verify the closing bracket matches the last opening bracket
                if (c == ')' && top != '(')  return false;
                if (c == '}' && top != '{')  return false;
                if (c == ']' && top != '[')  return false;
            }
        }

        return stack.isEmpty(); // True only if all brackets were matched
    }

    /**
     * PROBLEM 2: Evaluate Reverse Polish Notation (RPN)
     * Given an array of tokens in postfix notation, evaluate the expression.
     * Input: ["2","1","+","3","*"]  → ((2+1)*3) = 9
     * Input: ["4","13","5","/","+"] → (4+(13/5)) = 6

     * Approach: push numbers; when we see an operator, pop 2 numbers, apply the operator, & push the result.
     * Time: O(n), Space: O(n)
     */
    public static int evalRPN(String[] tokens) {
        Stack<Integer> stack = new Stack<>();

        for (String token : tokens) {
            switch (token) {
                case "+" -> stack.push(stack.pop() + stack.pop());
                case "-" -> {
                    int b = stack.pop(), a = stack.pop();
                    stack.push(a - b); // Order matters for subtraction!
                }
                case "*" -> stack.push(stack.pop() * stack.pop());
                case "/" -> {
                    int b = stack.pop(), a = stack.pop();
                    stack.push(a / b);
                }
                default -> stack.push(Integer.parseInt(token)); // It's a number
            }
        }

        return stack.pop(); // The final result
    }

    /**
     * PROBLEM 3: Min Stack — O(1) getMin()
     * Design a stack that supports push, pop, peek, & retrieving the minimum element in O(1).

     * Trick: Use a SECOND stack to track the running minimum.
     *        Every time we push, we also push the current min.
     *        When we pop, we pop both stacks simultaneously.
     */
    public static class MinStack {
        private final Stack<Integer> mainStack;
        private final Stack<Integer> minStack;  // Tracks minimums

        public MinStack() {
            mainStack = new Stack<>();
            minStack  = new Stack<>();
        }

        public void push(int val) {
            mainStack.push(val);
            // Push the NEW minimum (val vs current min)
            if (minStack.isEmpty()) {
                minStack.push(val);
            } else {
                minStack.push(Math.min(val, minStack.peek()));
            }
        }

        public int pop() {
            minStack.pop(); // Keep minStack in sync with mainStack
            return mainStack.pop();
        }

        public int peek() { return mainStack.peek(); }

        /** O(1) minimum lookup — top of minStack is always the current min */
        public int getMin() { return minStack.peek(); }

        @Override
        public String toString() {
            return "main = " + mainStack + ", minTracker = " + minStack;
        }
    }

    // ──────────────────────────────────────────────
    // DEMO
    // ──────────────────────────────────────────────

/*    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║           STACK DEMO             ║");
        System.out.println("╚══════════════════════════════════╝\n");

        // Basic stack operations
        Stack<Integer> stack = new Stack<>();
        stack.push(10); stack.push(20); stack.push(30);
        System.out.println("After pushes: " + stack);
        System.out.println("peek():       " + stack.peek());
        System.out.println("pop():        " + stack.pop() + " → " + stack);

        // Problem 1: Valid Parentheses
        System.out.println("\n--- Valid Parentheses ---");
        System.out.println("\"({[]})\" → " + isValidParentheses("({[]})"));  // true
        System.out.println("\"([)]\"   → " + isValidParentheses("([)]"));    // false
        System.out.println("\"{[}\"    → " + isValidParentheses("{[}"));     // false

        // Problem 2: RPN Evaluation
        System.out.println("\n--- Reverse Polish Notation ---");
        String[] tokens1 = {"2", "1", "+", "3", "*"};
        System.out.println("2 1 + 3 * = " + evalRPN(tokens1)); // 9

        String[] tokens2 = {"4", "13", "5", "/", "+"};
        System.out.println("4 13 5 / + = " + evalRPN(tokens2)); // 6

        // Problem 3: Min Stack
        System.out.println("\n--- Min Stack ---");
        MinStack ms = new MinStack();
        ms.push(5); ms.push(3); ms.push(7); ms.push(2); ms.push(4);
        System.out.println("After pushes: " + ms);
        System.out.println("getMin() = "  + ms.getMin()); // 2
        ms.pop(); ms.pop();
        System.out.println("After 2 pops: " + ms);
        System.out.println("getMin() = " + ms.getMin()); // 3
    }
*/

    public static void main(String[] args) {
        System.out.println("════════════ STACK DEMO ════════════");

        // ─────────────────────────────
        // BASIC STACK OPERATIONS
        // ─────────────────────────────
        Stack<Integer> stack = new Stack<>();

        stack.push(10);
        System.out.printf("[%-5s] %-6s | %s%n", "PUSH", 10, stack);

        stack.push(20);
        System.out.printf("[%-5s] %-6s | %s%n", "PUSH", 20, stack);

        stack.push(30);
        System.out.printf("[%-5s] %-6s | %s%n", "PUSH", 30, stack);

        System.out.printf("[%-5s] %-6s | %s%n", "PEEK", stack.peek(), stack);

        int popped = stack.pop();
        System.out.printf("[%-5s] %-6s | %s%n", "POP", popped, stack);

        // ─────────────────────────────
        // VALID PARENTHESES
        // ─────────────────────────────
        System.out.println("\n──────── Valid Parentheses ────────");

        String s1 = "({[ ]})";
        String s2 = "([ )]";
        String s3 = "{[ }";

        System.out.printf("%-10s → %s%n", s1, isValidParentheses(s1));
        System.out.printf("%-10s → %s%n", s2, isValidParentheses(s2));
        System.out.printf("%-10s → %s%n", s3, isValidParentheses(s3));

        // ─────────────────────────────
        // REVERSE POLISH NOTATION
        // ─────────────────────────────
        System.out.println("\n──────── Reverse Polish Notation ────────");

        String[] tokens1 = {"2", "1", "+", "3", "*"};
        String[] tokens2 = {"4", "13", "5", "/", "+"};

        System.out.printf("2 1 + 3 *  = %d%n", evalRPN(tokens1));
        System.out.printf("4 13 5 / + = %d%n", evalRPN(tokens2));
        /*
            4 13 5 / +
            = 4 + (13 / 5)
            = 4 + 2
            = 6
        */

        // ─────────────────────────────
        // MIN STACK
        // ─────────────────────────────
        System.out.println("\n──────── Min Stack ────────");

        MinStack ms = new MinStack();

        ms.push(5);
        System.out.println("push 5  | " + ms);
        ms.push(3);
        System.out.println("push 3  | " + ms);
        ms.push(7);
        System.out.println("push 7  | " + ms);
        ms.push(2);
        System.out.println("push 2  | " + ms);
        ms.push(4);
        System.out.println("push 4  | " + ms);

        System.out.println("getMin  → " + ms.getMin());

        ms.pop();
        System.out.println("pop     | " + ms);
        ms.pop();
        System.out.println("pop     | " + ms);

        System.out.println("getMin  → " + ms.getMin());
    }
}
