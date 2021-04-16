package ru.yakimov.searchapi;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;

public class SearchTree {
    private AbstractSearchNode root;
//
//    public static SearchTree fromQuery(String query) {
//        Deque<String> output = new LinkedList<>();
//        Deque<String> stack = new LinkedList<>();
//
//        Arrays.stream(query.split("\\s"))
//                .forEach(token -> {
//                    if ("OR".equalsIgnoreCase(token) || "AND".equalsIgnoreCase(token)) {
//                        while (!stack.isEmpty() && isHigherPrecedenceOperator(token, stack.peek())) {
//                            output.push(stack.pop());
//                        }
//                        stack.push(token);
//                    } else if (token.equals("(")) {
//                        stack.push(token);
//                    } else if (token.equals(")")) {
//                        while (!stack.isEmpty() && !stack.peek().equals("(")) {
//                            output.push(stack.pop());
//                        }
//                        stack.pop();
//                    } else {
//                        output.push(token);
//                    }
//                });
//
//        return output;
//    }

    private static boolean isHigherPrecedenceOperator(String first, String second) {
        if (Objects.equals(first, second)) {
            return false;
        }
        return "OR".equalsIgnoreCase(first);
    }
}
