package tech.finovy.framework.logappender.push.http;

public enum Protocol {
    HTTP("http"),
    HTTPS("https");
    private final String protocol;

    Protocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String toString() {
        return protocol;
    }
}
