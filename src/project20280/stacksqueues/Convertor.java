package project20280.stacksqueues;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Convertor {
    static String convertToBinary(int decNum) {
        ArrayStack<Integer> stack = new ArrayStack<>();

        while (decNum > 0) {
            stack.push(decNum % 2);
            decNum = decNum / 2;
        }

        String output = "";
        while (!stack.isEmpty()) {
            output = output + stack.pop().toString();
        }

        return output;
    }

    public static void main(String[] args) {
        System.out.println(convertToBinary(23));
    }
}