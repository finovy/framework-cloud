package tech.finovy.framework.logappender.utils;

public class CodingUtils {
    private CodingUtils() {
    }

    public static void assertParameterNotNull(Object param, String paramName) {
        if (param == null) {
            throw new NullPointerException(paramName + " is null");
        }
    }

    public static void assertStringNotNullOrEmpty(String param, String paramName) {
        assertParameterNotNull(param, paramName);
        if (param.isEmpty()) {
            throw new IllegalArgumentException(paramName + " is empty");
        }
    }
}
