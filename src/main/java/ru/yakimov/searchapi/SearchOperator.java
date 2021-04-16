package ru.yakimov.searchapi;

import java.util.Arrays;

public enum SearchOperator {

    LEFT_PARENTHESIS("(", "("),
    RIGHT_PARENTHESIS(")", ")"),
    EQUALITY("=", "="),
    UNEQUALITY("<>", "<>"),
    STRICT_GREATER_THEN(">", ">"),
    STRICT_LESSER_THEN("<","<"),
    GREATER_THEN(">=", ">="),
    LESSER_THEN("<=", "<="),
    LIKE("~", "LIKE"),
    IN("IN", "IN");

    private final String sqlOperator;
    private final String searchOperator;

    SearchOperator(String searchOperator, String sqlOperator) {
        this.searchOperator = searchOperator;
        this.sqlOperator = sqlOperator;
    }

    public static SearchOperator of(String searchOperator) {
        return Arrays.stream(values())
                .filter(op -> op.getSearchOperator().equalsIgnoreCase(searchOperator))
                .findFirst()
                .orElseThrow(() -> new EnumConstantNotPresentException(SearchOperator.class, searchOperator));
    }

    public String getSqlOperator() {
        return sqlOperator;
    }

    public String getSearchOperator() {
        return searchOperator;
    }
}
