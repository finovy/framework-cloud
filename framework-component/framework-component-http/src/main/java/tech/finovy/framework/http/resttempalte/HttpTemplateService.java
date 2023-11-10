package tech.finovy.framework.http.resttempalte;


public class HttpTemplateService<T> {
    private final RestTemplateProperties properties;
    private final T loadbalanceRestTemplate;
    private final T restTemplate;

    public HttpTemplateService(RestTemplateProperties properties, T loadbalanceRestTemplate, T restTemplate) {
        this.properties = properties;
        this.loadbalanceRestTemplate = loadbalanceRestTemplate;
        this.restTemplate = restTemplate;
    }


    public HttpTemplatePack<T> choice(String url) {
        HttpTemplatePack<T> pack = new HttpTemplatePack(loadbalanceRestTemplate);
        if (properties.isEnableHttp()) {
            pack.setRestTemplate(restTemplate);
        }
        String host = url.toLowerCase();
        pack.setHost(host);
        while (host.endsWith("/")) {
            host = host.substring(0, host.length() - 1);
        }
        if (host.startsWith(Constant.LB) || host.startsWith(Constant.LBS)) {
            host = host.replaceFirst(Constant.LB, Constant.HTTP);
            host = host.replaceFirst(Constant.LBS, Constant.HTTPS);
            pack.setHost(host);
            pack.setRestTemplate(loadbalanceRestTemplate);
            return pack;
        }
        return pack;
    }

}
