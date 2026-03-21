package com.datastructures.stack;

/**
 * A stack is Last-In-First-Out (LIFO): the last item you push is the first one you pop.
 * Think of a stack of plates—you only add or remove from the top.
 */
public class Stack<T> {

    private static final int DEFAULT_CAPACITY = 16;

    private Object[] data;
    /** Index of the top element, or -1 when empty. */
    private int topIndex;

    public Stack() {
        data = new Object[DEFAULT_CAPACITY];
        topIndex = -1;
    }

    /** Places {@code value} on top. Doubles the array if it is full. */
    public void push(T value) {
        if (topIndex == data.length - 1) {
            grow();
        }
        topIndex++;
        data[topIndex] = value;
    }

    /** Removes and returns the top element. */
    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack underflow: stack is empty.");
        }
        T value = (T) data[topIndex];
        data[topIndex] = null;
        topIndex--;
        return value;
    }

    /** Returns the top element without removing it. */
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty.");
        }
        return (T) data[topIndex];
    }

    public boolean isEmpty() {
        return topIndex == -1;
    }

    public int size() {
        return topIndex + 1;
    }

    private void grow() {
        Object[] newData = new Object[data.length * 2];
        System.arraycopy(data, 0, newData, 0, data.length);
        data = newData;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder builder = new StringBuilder("[");
        for (int index = topIndex; index >= 0; index--) {
            builder.append(data[index]);
            if (index > 0) {
                builder.append(", ");
            }
        }
        builder.append("] (top on left)");
        return builder.toString();
    }

    /**
     * Returns true if every opening bracket has a matching closing bracket in the right order.
     * Uses a stack of opening brackets; a closing bracket must match the most recent opening one.
     */
    public static boolean isValidParentheses(String text) {
        Stack<Character> stack = new Stack<>();

        for (int position = 0; position < text.length(); position++) {
            char character = text.charAt(position);

            if (character == '(' || character == '{' || character == '[') {
                stack.push(character);
                continue;
            }

            if (character == ')' || character == '}' || character == ']') {
                if (stack.isEmpty()) {
                    return false;
                }
                char topOpening = stack.pop();
                if (!bracketsMatch(topOpening, character)) {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    private static boolean bracketsMatch(char open, char close) {
        if (open == '(') {
            return close == ')';
        }
        if (open == '{') {
            return close == '}';
        }
        if (open == '[') {
            return close == ']';
        }
        return false;
    }

    /**
     * Evaluates an expression in reverse Polish (postfix) notation.
     * Numbers are pushed; operators pop the two top operands (second pop is the right-hand side).
     */
    public static int evalRPN(String[] tokens) {
        Stack<Integer> stack = new Stack<>();

        for (int index = 0; index < tokens.length; index++) {
            String token = tokens[index];

            if (token.equals("+")) {
                int right = stack.pop();
                int left = stack.pop();
                stack.push(left + right);
            } else if (token.equals("-")) {
                int right = stack.pop();
                int left = stack.pop();
                stack.push(left - right);
            } else if (token.equals("*")) {
                int right = stack.pop();
                int left = stack.pop();
                stack.push(left * right);
            } else if (token.equals("/")) {
                int right = stack.pop();
                int left = stack.pop();
                stack.push(left / right);
            } else {
                stack.push(Integer.parseInt(token));
            }
        }
        return stack.pop();
    }

    /**
     * Stack with O(1) minimum: a second stack stores the smallest value seen so far for each height
     * of the main stack. When you pop, you pop both stacks together.
     */
    public static class MinStack {
        private final Stack<Integer> valueStack;
        private final Stack<Integer> minimumStack;

        public MinStack() {
            valueStack = new Stack<>();
            minimumStack = new Stack<>();
        }

        public void push(int value) {
            valueStack.push(value);
            if (minimumStack.isEmpty()) {
                minimumStack.push(value);
            } else {
                int currentMin = minimumStack.peek();
                int newMin = Math.min(value, currentMin);
                minimumStack.push(newMin);
            }
        }

        public int pop() {
            minimumStack.pop();
            return valueStack.pop();
        }

        public int peek() {
            return valueStack.peek();
        }

        public int getMin() {
            return minimumStack.peek();
        }

        @Override
        public String toString() {
            return "values=" + valueStack + ", runningMinimums=" + minimumStack;
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Stack demo ---");

        Stack<Integer> stack = new Stack<>();

        stack.push(10);
        System.out.println("Push 10 | Current stack: " + stack);

        stack.push(20);
        System.out.println("Push 20 | Current stack: " + stack);

        stack.push(30);
        System.out.println("Push 30 | Current stack: " + stack);

        System.out.println("Peek -> " + stack.peek() + " | Current stack: " + stack);

        int popped = stack.pop();
        System.out.println("Pop -> " + popped + " | Current stack: " + stack);

        System.out.println();
        System.out.println("Valid parentheses \"({[]})\" -> " + isValidParentheses("({[]})"));
        System.out.println("Valid parentheses \"([)]\"   -> " + isValidParentheses("([)]"));

        System.out.println();
        String[] tokens1 = {"2", "1", "+", "3", "*"};
        String[] tokens2 = {"4", "13", "5", "/", "+"};
        System.out.println("RPN 2 1 + 3 *  = " + evalRPN(tokens1));
        System.out.println("RPN 4 13 5 / + = " + evalRPN(tokens2));

        System.out.println();
        MinStack minStack = new MinStack();
        minStack.push(5);
        System.out.println("Push 5 | " + minStack);
        minStack.push(3);
        System.out.println("Push 3 | " + minStack);
        minStack.push(7);
        System.out.println("Push 7 | " + minStack);
        minStack.push(2);
        System.out.println("Push 2 | " + minStack);
        System.out.println("Minimum right now -> " + minStack.getMin());
        minStack.pop();
        minStack.pop();
        System.out.println("After two pops | " + minStack);
        System.out.println("Minimum now -> " + minStack.getMin());
    }
}
