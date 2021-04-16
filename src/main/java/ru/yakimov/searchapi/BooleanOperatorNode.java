package ru.yakimov.searchapi;

import java.util.Map;

public class BooleanOperatorNode implements AbstractSearchNode {
    private final AbstractSearchNode left;

    private final BooleanOperator operator;

    private final AbstractSearchNode right;

    public BooleanOperatorNode(AbstractSearchNode left, BooleanOperator operator, AbstractSearchNode right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public String toSqlStatement() {
        String fieldName = left.toSqlStatement();
        return String.format("(%s) %s (%s)", fieldName, operator.name(), fieldName);
    }

    @Override
    public void populateArguments(Map<String, Object> arguments) {
        left.populateArguments(arguments);
        right.populateArguments(arguments);
    }

}
