package project20280.stacksqueues;

class BracketChecker {
    private final String input;

    public BracketChecker(String in) {
        input = in;
    }

    public boolean check() {
        // if c == ( || { || [ -> add to stack
            // when c == ) || } || ] check stack
                // if match continue
                // else false
        ArrayStack<Character> stack = new ArrayStack<>(input.length());
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if ("({[".indexOf(c) != -1) {
                stack.push(c);
            } else if (")]}".indexOf(c) != -1) {
                if (stack.isEmpty() || flipBracket(c) != stack.pop()) {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    private char flipBracket(char c) {
        return switch (c) {
            case ')' -> '(';
            case '}' -> '{';
            case ']' -> '[';
            default -> '\0';
        };
    }

    public static void main(String[] args) {
        String[] inputs = {
                "[]]()()", // not correct
                "c[d]", // correct\n" +
                "a{b[c]d}e", // correct\n" +
                "a{b(c]d}e", // not correct; ] doesn't match (\n" +
                "a[b{c}d]e}", // not correct; nothing matches final }\n" +
                "a{b(c) ", // // not correct; Nothing matches opening {
        };

        for (String input : inputs) {
            BracketChecker checker = new BracketChecker(input);
            System.out.println("checking: " + input);
            System.out.println(checker.check());
        }
    }
}