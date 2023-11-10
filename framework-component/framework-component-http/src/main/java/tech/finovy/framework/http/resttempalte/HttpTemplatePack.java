package tech.finovy.framework.http.resttempalte;


public class HttpTemplatePack<T> {

    private T restTemplate;
    private String host;

    public HttpTemplatePack(T restTemplate) {
        this.restTemplate = restTemplate;
    }

    public T getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(T restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
