package ru.yakimov.searchapi;

import java.util.Map;

public interface AbstractSearchNode {
    String toSqlStatement();
    void populateArguments(Map<String, Object> arguments);
}
