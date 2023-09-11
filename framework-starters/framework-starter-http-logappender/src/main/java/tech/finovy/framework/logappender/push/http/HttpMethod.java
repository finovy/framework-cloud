package tech.finovy.framework.logappender.push.http;

public enum HttpMethod {
    DELETE("DELETE"),
    GET("GET"),
    HEAD("HEAD"),
    POST("POST"),
    PUT("PUT"),
    OPTIONS("OPTIONS");
    private final String text;

    HttpMethod(final String text) {
        this.text = text;
    }

    public static HttpMethod fromString(String value) {
        for (HttpMethod method : HttpMethod.values()) {
            if (method.text.equals(value)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Illegal http method: " + value);
    }

    @Override
    public String toString() {
        return this.text;
    }
}
