package ru.yakimov.searchapi;

import java.util.Map;

public class ValueNode implements AbstractSearchNode {
    private final String argumentName;
    private final SearchOperator operator;
    private final String value;

    protected ValueNode(String argumentName, SearchOperator operator, String value) {
        this.argumentName = argumentName;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public String toSqlStatement() {
        return String.format("%s %s :%s", argumentName, operator.getSqlOperator(), argumentName);
    }

    @Override
    public void populateArguments(Map<String, Object> arguments) {
        arguments.put(argumentName, value);
    }
}
