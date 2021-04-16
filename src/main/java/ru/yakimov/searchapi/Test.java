package ru.yakimov.searchapi;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {
        String query = "(age >= 18 AND age < 32) OR name IN (Антон,Евгений) AND (dateOfBirth = null OR workPlace <> null)";
        Deque<String> output = new LinkedList<>();
        Deque<String> stack = new LinkedList<>();

        Arrays.stream(query.replaceAll("\\(", "( ").replaceAll("\\)", " )")
                .split("(\\s)"))
                .forEach(token -> {
                    if ("OR".equalsIgnoreCase(token) || "AND".equalsIgnoreCase(token)) {
                        while (!stack.isEmpty() && isHigherPrecedenceOperator(token, stack.peek())) {
                            output.push(stack.pop());
                        }
                        stack.push(token);
                    } else if (token.equals("(")) {
                        stack.push(token);
                    } else if (token.equals(")")) {
                        while (!stack.isEmpty() && !stack.peek().equals("(")) {
                            output.push(stack.pop());
                        }
                        stack.pop();
                    } else {
                        output.push(token);
                    }
                });
        while (!stack.isEmpty()) {
            output.push(stack.pop());
        }


        Map<String, Object> params = new HashMap<>();
        System.out.println("Original query: ");
        System.out.println(query);
        Map<String, String> aliases = new HashMap<>();
        aliases.put("age", "P.PERSON_AGE");
        System.out.println("Aliases: ");
        for (Map.Entry<String, String> entry : aliases.entrySet()) {
            System.out.printf("Query param: %10s | SQL param: %s%n", entry.getKey(), entry.getValue());
        }
        System.out.println("SQL Query: ");
        System.out.println("WHERE " + toSql(output, params, aliases));
        System.out.println("Params:");
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String arg = entry.getValue() instanceof Object[] ? Arrays.toString((Object[]) entry.getValue()) : entry.getValue().toString();
            System.out.printf("Key: %10s | Value %s%n", entry.getKey(), arg);
        }
    }

    private static String toSql(Deque<String> output, Map<String, Object> params, Map<String, String> aliases) {
        return toSql(output.pop(), output, params, aliases);
    }

    private static String toSql(String arg, Deque<String> output, Map<String, Object> params, Map<String, String> aliases) {
        if (arg.equals("OR") || arg.equals("AND")) {
            String last = toSql(output.pop(), output, params, aliases);
            String first = toSql(output.pop(), output, params, aliases);
            return String.format("(%s %s %s)", first, arg, last);
        } else {
            SearchOperator operator = SearchOperator.of(output.pop());
            String argumentValue = arg;
            String argumentName = output.pop();
            String namedParameter = argumentName;
            if (aliases.containsKey(argumentName)) {
                argumentName = aliases.get(argumentName);
            }
            int count = 1;
            while (params.containsKey(namedParameter)) {
                namedParameter += count;
                count++;
            }
            String result;
            if (operator.equals(SearchOperator.IN)) {
                result = String.format("%s %s (:%s)", argumentName, operator.getSqlOperator(), namedParameter);
                params.put(namedParameter, argumentValue.split(","));
            } else if (operator.equals(SearchOperator.EQUALITY) && argumentValue.equalsIgnoreCase("NULL")) {
                result = String.format("%s IS NULL", argumentName);
            } else if (operator.equals(SearchOperator.UNEQUALITY) && argumentValue.equalsIgnoreCase("NULL")) {
                result = String.format("%s IS NOT NULL", argumentName);
            } else {
                result = String.format("%s %s :%s", argumentName, operator.getSqlOperator(), namedParameter);
                params.put(namedParameter, argumentValue);
            }
            return result;
        }
    }

    private static boolean isHigherPrecedenceOperator(String first, String second) {
        if (Objects.equals(first, second)) {
            return false;
        }
        return "OR".equalsIgnoreCase(first) && second.equalsIgnoreCase("AND");
    }

    public static Map<String, String> getAliases(String sql) {
        Pattern pattern = Pattern.compile("\\s+(.+\\s+AS\\s+.+(\\s|,)*)+\\s+");
        Matcher matcher = pattern.matcher(sql);
        if (!matcher.find()) {
            return Collections.emptyMap();
        }
        String[] params = matcher.group().split(",");
        for (String param : params) {
            String[] splitted = param.split("(\\s+AS\\s+)");
            
        }
    }
}
