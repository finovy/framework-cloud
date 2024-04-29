package tech.finovy.framework.elasticsearch.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.context.ApplicationContext;
import tech.finovy.framework.config.nacos.listener.AbstractNacosConfigDefinitionListener;
import tech.finovy.framework.elasticsearch.client.autoconfigure.ElasticClientProperties;
import tech.finovy.framework.elasticsearch.impl.ElasticSearchContextHolder;

@Slf4j
public class ElasticSearchClientManager extends AbstractNacosConfigDefinitionListener<ElasticClientConfig> {

    private final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    private final ElasticClientProperties properties;
    private ElasticClientConfig clientConfig;
    private final ApplicationContext context;

    public ElasticSearchClientManager(ElasticClientProperties properties, ApplicationContext context) {
        super(ElasticClientConfig.class, properties.getDataId(), properties.getDataGroup());
        this.properties = properties;
        this.context = context;
    }

    @Override
    public String getDataId() {
        return properties.getDataId();
    }

    @Override
    public String getDataGroup() {
        return properties.getDataGroup();
    }

    @Override
    public void refresh(String dataId, String dataGroup, ElasticClientConfig config, int version) {
        clientConfig = config;
        if (clientConfig == null) {
            throw new ElasticClientConfigurationException("Nacos NacosDataId=" + properties.getDataId() + ",NacosDataGroup=" + properties.getDataGroup() + " ElasticSearch IS NULL");
        }
        RestClient restClient = initClient();
        ElasticSearchContextHolder.get().setClient(restClient);
    }

    private RestClient initClient() {
        HttpHost[] nodes = new HttpHost[clientConfig.getNodes().length];
        for (int i = 0; i < clientConfig.getNodes().length; i++) {
            String host = clientConfig.getNodes()[i];
            String[] ipPortPair = host.split(":");
            log.info("ElasticSearch node {}", host);
            String hostName = ipPortPair[1].trim().replace("//", "");
            int port = Integer.parseInt(ipPortPair[2].trim());
            String scheme = ipPortPair[0].trim();
            nodes[i] = (new HttpHost(hostName, port, scheme));
        }
        RestClientBuilder builder = RestClient.builder(nodes);
        Header[] defaultHeaders = new Header[]{new BasicHeader("header", "value")};
        builder.setDefaultHeaders(defaultHeaders);
        builder.setFailureListener(new RestClient.FailureListener() {
            @Override
            public void onFailure(Node node) {
                log.warn("onFailure:{}", node.toString());
            }
        });
        builder.setRequestConfigCallback(builder1 -> builder1.setSocketTimeout(clientConfig.getSocketTimeout())).setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                .setDefaultCredentialsProvider(credentialsProvider));
        if (StringUtils.isNotBlank(clientConfig.getUsername())) {
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(clientConfig.getUsername(), clientConfig.getPassword()));
            builder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                    .setDefaultCredentialsProvider(credentialsProvider));
        }
        RestClient restClient = builder.build();
//        if (properties.isEnableSniffer()) {
//            Sniffer.builder(restClient).build();
//        }
        log.info("ElasticSearch client init success!");
        return restClient;
    }
}
