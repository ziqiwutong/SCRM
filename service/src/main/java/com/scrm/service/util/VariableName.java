package com.scrm.service.util;

public class VariableName {

    public static String camelToUnderscore(String camel) {
        return camel.replaceAll("[A-Z]", "_$0").toLowerCase();
    }
}
