package tech.finovy.framework.transaction.tcc.client.service;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import tech.finovy.framework.transaction.tcc.client.api.inner.AbstractHttpTransactionRemoteService;

import java.util.Map;

public class HttpTransactionRemoteServiceImpl extends AbstractHttpTransactionRemoteService {

    private static final String TXID_URL_TEMPLATE = "/tcc/id/%s/%s";
    private static final String LAUNCH_URL_TEMPLATE = "/tcc/%s/%s";
    private static final String CHECK_URL_TEMPLATE = "/healthcheck/liveness";

    @Override
    @SneakyThrows
    public String getTxId(String typeName) {
        final String urlSuffix = String.format(TXID_URL_TEMPLATE, typeName, System.nanoTime());
        String txId = this.request(GET, null, urlSuffix, null, null, "$.txId");
        if (StringUtils.isNotEmpty(txId)) {
            return txId;
        }
        throw new Exception("can not get txId");
    }

    @Override
    public <T> boolean launch(String typeName, String txId, Map<String, String> headers, T body) {
        final String urlSuffix = String.format(LAUNCH_URL_TEMPLATE, typeName, txId);
        String code = this.request(POST, null, urlSuffix, headers, body, "$.code");
        return "success".equalsIgnoreCase(code);
    }

    @Override
    public boolean healthcheck(String host) {
        return this.request(GET, host, CHECK_URL_TEMPLATE, null, null, "$.success");
    }
}
