package com.dawn.processor.utils;

import java.util.Map;

public class Utils {
    public static boolean isEmpty(String moduleName) {
        return moduleName == null || moduleName.isEmpty();
    }

    public static boolean isNotEmpty(Map<String, String> options) {
        return options != null && !options.isEmpty();
    }

}
