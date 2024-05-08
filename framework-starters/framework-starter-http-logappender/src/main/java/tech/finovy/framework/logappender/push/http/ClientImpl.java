package tech.finovy.framework.logappender.push.http;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import tech.finovy.framework.logappender.conf.ClientConfiguration;
import tech.finovy.framework.logappender.entry.*;
import tech.finovy.framework.logappender.exception.ClientException;
import tech.finovy.framework.logappender.exception.ErrorCodes;
import tech.finovy.framework.logappender.exception.LogException;
import tech.finovy.framework.logappender.exception.ServiceException;
import tech.finovy.framework.logappender.push.LogService;
import tech.finovy.framework.logappender.push.internals.Consts;
import tech.finovy.framework.logappender.push.internals.ListShardResponse;
import tech.finovy.framework.logappender.utils.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.Deflater;

public class ClientImpl implements LogService {
    private static final String HTTP = "http://";
    private final String realServerIP = null;
    private final AbstractServiceClient serviceClient;
    private final Boolean mUseDirectMode = false;
    private final boolean mUUIDTag = false;
    private String userAgent = "CONST_USER_AGENT_VALUE";
    private String accessId;
    private String accessKey;
    private String httpType;
    private String hostName;
    private String sourceIp;
    private String securityToken;

    public ClientImpl(String endpoint, String accessId, String accessKey) {
        this(endpoint, accessId, accessKey, NetworkUtils.getLocalMachineIP());
    }

    public ClientImpl(String endpoint, String accessId, String accessKey, String SourceIp) {
        this(endpoint, accessId, accessKey, SourceIp, Consts.HTTP_CONNECT_MAX_COUNT, Consts.HTTP_CONNECT_TIME_OUT, Consts.HTTP_SEND_TIME_OUT);
    }

    public ClientImpl(String endpoint, String accessId, String accessKey, String sourceIp, int connectMaxCount, int connectTimeout, int sendTimeout) {
        configure(endpoint, accessId, accessKey, sourceIp);
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setMaxConnections(connectMaxCount);
        clientConfig.setConnectionTimeout(connectTimeout);
        clientConfig.setSocketTimeout(sendTimeout);
        this.serviceClient = new DefaultServiceClient(clientConfig);
    }

    public ClientImpl(String endpoint, String accessId, String accessKey, AbstractServiceClient serviceClient) {
        configure(endpoint, accessId, accessKey, NetworkUtils.getLocalMachineIP());
        this.serviceClient = serviceClient;
    }

    public ClientImpl(String endpoint, String accessId, String accessKey, String sourceIp, ClientConfiguration config) {
        Args.notNull(config, "Config");
        configure(endpoint, accessId, accessKey, sourceIp);
        if (config.isRequestTimeoutEnabled()) {
            this.serviceClient = new TimeoutServiceClient(config);
        } else {
            this.serviceClient = new DefaultServiceClient(config);
        }
    }

    private static RequestMessage BuildRequest(URI endpoint, HttpMethod httpMethod, String resourceUri, Map<String, String> parameters, Map<String, String> headers, InputStream content, long size) {
        RequestMessage request = new RequestMessage();
        request.setMethod(httpMethod);
        request.setEndpoint(endpoint);
        request.setResourcePath(resourceUri);
        request.setParameters(parameters);
        request.setHeaders(headers);
        request.setContent(content);
        request.setContentLength(size);
        return request;
    }

    private static String encodeResponseBodyToUtf8String(ResponseMessage response, String requestId) throws LogException {
        byte[] body = response.getRawBody();
        if (body == null) {
            throw new LogException(ErrorCodes.BAD_RESPONSE, "The response body is null", null, requestId);
        }
        return new String(body, StandardCharsets.UTF_8);
    }

    private static byte[] encodeToUtf8(String source) throws LogException {
        return source.getBytes(StandardCharsets.UTF_8);
    }

    private void configure(String endpoint, String accessId, String accessKey, String sourceIp) {
        Args.notNullOrEmpty(endpoint, "endpoint");
        Args.notNullOrEmpty(accessId, "accessId");
        Args.notNullOrEmpty(accessKey, "accessKey");
        if (endpoint.startsWith(HTTP)) {
            this.hostName = endpoint.substring(7);
            this.httpType = HTTP;
        } else if (endpoint.startsWith("https://")) {
            this.hostName = endpoint.substring(8);
            this.httpType = "https://";
        } else {
            this.hostName = endpoint;
            this.httpType = HTTP;
        }
        while (this.hostName.endsWith("/")) {
            this.hostName = this.hostName.substring(0, this.hostName.length() - 1);
        }
        this.accessId = accessId;
        this.accessKey = accessKey;
        this.sourceIp = sourceIp;
        if (sourceIp == null || sourceIp.isEmpty()) {
            this.sourceIp = NetworkUtils.getLocalMachineIP();
        }
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    @Override
    public PutLogsResponse putLogs(PutLogsRequest request) throws LogException {
        CodingUtils.assertParameterNotNull(request, "request");
        String project = request.getProject();
        CodingUtils.assertStringNotNullOrEmpty(project, "project");
        String logStore = request.getLogStore();
        CodingUtils.assertStringNotNullOrEmpty(logStore, "logStore");
        String shardKey = request.getRouteKey();
        Consts.CompressType compressType = request.getCompressType();
        CodingUtils.assertParameterNotNull(compressType, "compressType");

        byte[] logBytes = request.getLogGroupBytes();
        if (logBytes == null) {
            List<LogItem> logItems = request.getLogItems();
            if (logItems.size() > Consts.CONST_MAX_PUT_LINES) {
                throw new LogException("InvalidLogSize", "logItems' length exceeds maximum limitation : " + Consts.CONST_MAX_PUT_LINES + " lines", "");
            }
            String topic = request.getTopic();
            CodingUtils.assertParameterNotNull(topic, "topic");
            String source = request.getSource();
            if (!Consts.CONST_SLS_JSON.equals(request.getContentType())) {

            } else {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("topic", topic);
                if (source == null || source.isEmpty()) {
                    jsonObj.put("source", this.sourceIp);
                } else {
                    jsonObj.put("source", source);
                }
                JSONArray logsArray = new JSONArray();
                for (int i = 0; i < logItems.size(); i++) {
                    LogItem item = logItems.get(i);
                    JSONObject jsonObjInner = new JSONObject();
                    jsonObjInner.put("timeStamp", item.getTime());
                    for (LogContent content : item.getLogContents()) {
                        jsonObjInner.put(content.mKey, content.mValue);
                    }
                    logsArray.add(jsonObjInner);
                }
                jsonObj.put("logs", logsArray);
                JSONObject tagObj = new JSONObject();
                List<TagContent> tags = request.getTags();
                if (tags != null && !tags.isEmpty()) {
                    for (TagContent tag : tags) {
                        tagObj.put(tag.getKey(), tag.getValue());
                    }
                }
                if (this.mUUIDTag) {
                    tagObj.put("packId", UUID.randomUUID() + "-" + Math.random());
                }
                if (tagObj.size() > 0) {
                    jsonObj.put("tags", tagObj);
                }
                logBytes = encodeToUtf8(jsonObj.toString());
            }
        }
        if (logBytes == null || logBytes.length > Consts.CONST_MAX_PUT_SIZE) {
            throw new LogException("InvalidLogSize", "logItems' size exceeds maximum limitation : " + Consts.CONST_MAX_PUT_SIZE + " bytes", "");
        }

        Map<String, String> headParameter = getCommonHeadPara(project);
        headParameter.put(Consts.CONST_CONTENT_TYPE, request.getContentType());
        int originalSize = logBytes.length;

        if (compressType == Consts.CompressType.LZ4) {
            logBytes = LZ4Encoder.compressToLhLz4Chunk(logBytes.clone());
            headParameter.put(Consts.CONST_X_SLS_COMPRESSTYPE, compressType.toString());
        } else if (compressType == Consts.CompressType.GZIP) {
            ByteArrayOutputStream out = new ByteArrayOutputStream(logBytes.length);
            Deflater compressor = new Deflater();
            compressor.setInput(logBytes);
            compressor.finish();
            byte[] buf = new byte[10240];
            while (!compressor.finished()) {
                int count = compressor.deflate(buf);
                out.write(buf, 0, count);
            }
            logBytes = out.toByteArray();
            headParameter.put(Consts.CONST_X_SLS_COMPRESSTYPE, compressType.toString());
        }
        headParameter.put(Consts.CONST_X_SLS_BODYRAWSIZE, String.valueOf(originalSize));

        String resourceUri = "/logstores/";
        if (!(project == null || project.isEmpty())) {
            resourceUri = resourceUri + project + "/";
        }
        resourceUri = resourceUri + logStore;
        if (shardKey == null || shardKey.length() == 0) {
            resourceUri += "/shards/lb";
        } else {
            resourceUri += "/shards/" + shardKey;
        }
        Map<String, String> urlParameter = request.getAllParams();
        long cmp_size = logBytes.length;
        for (int i = 0; i < 2; i++) {
            String server_ip = this.realServerIP;
            ClientConnectionStatus connection_status = null;
            if (this.mUseDirectMode) {
                connection_status = getGlobalConnectionStatus();
                server_ip = connection_status.getIpAddress();
            }
            try {
                ResponseMessage response = sendData(project, HttpMethod.POST, resourceUri, urlParameter, headParameter, logBytes, null, server_ip);
                Map<String, String> resHeaders = response.getHeaders();
                PutLogsResponse putLogsResponse = new PutLogsResponse(resHeaders);
                if (connection_status != null) {
                    connection_status.addSendDataSize(cmp_size);
                    connection_status.updateLastUsedTime(System.nanoTime());
                }
                return putLogsResponse;
            } catch (LogException e) {
                String request_id = e.getRequestId();
                if (i == 1 || request_id != null && !request_id.isEmpty()) {
                    throw e;
                }
                if (connection_status != null) {
                    connection_status.disableConnection();
                }
            }
        }
        return new PutLogsResponse(new HashMap<>());
    }

    private ResponseMessage sendData(String project, HttpMethod method, String resourceUri, Map<String, String> parameters, Map<String, String> headers, byte[] body, Map<String, String> outputHeader, String serverIp) throws LogException {
        if (body.length > 0) {
            headers.put(Consts.CONST_CONTENT_MD5, DigestUtils.md5Crypt(body));
        }
        headers.put(Consts.CONST_CONTENT_LENGTH, String.valueOf(body.length));
        DigestUtils.addSignature(this.accessId, this.accessKey, method.toString(), headers, resourceUri, parameters);
        URI uri;
        if (serverIp == null) {
            uri = getHostURI(project);
        } else {
            uri = getHostURIByIp(serverIp);
        }
        RequestMessage request = BuildRequest(uri, method, resourceUri, parameters, headers, new ByteArrayInputStream(body), body.length);
        ResponseMessage response = null;
        try {
            response = this.serviceClient.sendRequest(request, Consts.UTF_8_ENCODING);
            extractResponseBody(response);
            if (outputHeader != null) {
                outputHeader.putAll(response.getHeaders());
            }
            int statusCode = response.getStatusCode();
            if (statusCode != Consts.CONST_HTTP_OK) {
                String requestId = getRequestId(response.getHeaders());
                try {
                    JSONObject object = parseResponseBody(response, requestId);
                    errorCheck(object, requestId, statusCode);
                } catch (LogException ex) {
                    ex.setHttpCode(response.getStatusCode());
                    throw ex;
                }
            }
        } catch (ServiceException | ClientException e) {
            throw new LogException("RequestError", "Web request failed: " + e.getMessage(), e, "");
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException ignore) {
            }
        }
        return response;
    }

    private URI getHostURI(String project) {
        String endPointUrl = this.httpType + this.hostName;
        if (project != null && !project.isEmpty()) {
            endPointUrl = this.httpType + this.hostName;
        }
        try {
            return new URI(endPointUrl);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(ErrorCodes.ENDPOINT_INVALID, e);
        }
    }

    private URI getHostURIByIp(String ip_addr) throws LogException {
        String endPointUrl = this.httpType + ip_addr;
        try {
            return new URI(endPointUrl);
        } catch (URISyntaxException e) {
            throw new LogException(ErrorCodes.ENDPOINT_INVALID, "Failed to get real server ip when direct mode in enabled", "");
        }
    }

    private void extractResponseBody(ResponseMessage response) throws LogException {
        InputStream in = response.getContent();
        if (in == null) {
            return;
        }
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        String requestId = getRequestId(response.getHeaders());
        int ch;
        try {
            byte[] cache = new byte[1024];
            while ((ch = in.read(cache, 0, 1024)) != -1) {
                bytestream.write(cache, 0, ch);
            }
        } catch (IOException e) {
            throw new LogException(ErrorCodes.BAD_RESPONSE, "Io exception happened when parse the response data : ", e, requestId);
        }
        response.setBody(bytestream.toByteArray());
    }

    protected String getRequestId(Map<String, String> headers) {
        return AbstractUtils.getOrEmpty(headers, Consts.CONST_X_SLS_REQUESTID);
    }

    private JSONObject parseResponseBody(ResponseMessage response, String requestId) throws LogException {
        String body = encodeResponseBodyToUtf8String(response, requestId);
        if (response.getStatusCode() == 200) {
            try {
                return JSON.parseObject(body);
            } catch (JSONException ex) {
                throw new LogException(ErrorCodes.BAD_RESPONSE, "The response is not valid json string : " + body, ex, requestId);
            }
        }
        return createJsonBody(response.getStatusCode(), requestId, body);

    }

    private JSONObject createJsonBody(int statusCode, String requestId, String body) {
        JSONObject object = new JSONObject();
        object.put("code", statusCode);
        object.put("requestId", requestId);
        object.put("message", body);
        return object;
    }

    protected void errorCheck(JSONObject object, String requestId, int httpCode) throws LogException {
        if (object == null) {
            throw new LogException(httpCode, "InvalidErrorResponse", "Error Response is NULL", requestId);
        }
        if (object.containsKey(Consts.CONST_ERROR_CODE)) {
            try {
                String errorCode = object.getString(Consts.CONST_ERROR_CODE);
                String errorMessage = object.getString(Consts.CONST_ERROR_MESSAGE);
                throw new LogException(httpCode, errorCode, errorMessage, requestId);
            } catch (JSONException e) {
                throw new LogException(httpCode, "InvalidErrorResponse", "Error response is not a valid error json : \n" + object, requestId);
            }
        } else if (object.containsKey("code")) {
            try {
                String errorCode = object.getString("code");
                String errorMessage = object.getString("message");
                throw new LogException(httpCode, errorCode, errorMessage, requestId);
            } catch (JSONException e) {
                throw new LogException(httpCode, "InvalidErrorResponse", "Error response is not a valid error json : \n" + object, requestId);
            }
        } else {
            throw new LogException(httpCode, "InvalidErrorResponse", "Error response is not a valid error json : \n" + object, requestId);
        }
    }

    private ClientConnectionStatus getGlobalConnectionStatus() throws LogException {
        ClientConnectionContainer connection_container = ClientConnectionHelper.getInstance().getConnectionContainer(this.hostName, this.accessId, this.accessKey);
        ClientConnectionStatus connection_status = connection_container.getGlobalConnection();
        if (connection_status == null || !connection_status.isValidConnection()) {
            connection_container.updateGlobalConnection();
            connection_status = connection_container.getGlobalConnection();
            if (connection_status == null || connection_status.getIpAddress() == null || connection_status.getIpAddress().isEmpty()) {
                throw new LogException(ErrorCodes.ENDPOINT_INVALID, "Failed to get real server ip when direct mode is enabled", "");
            }
        }
        return connection_status;
    }

    public ListShardResponse listShard(String project, String logStore) throws LogException {
        CodingUtils.assertStringNotNullOrEmpty(project, "project");
        CodingUtils.assertStringNotNullOrEmpty(logStore, "logStore");
        return listShard(new ListShardRequest(project, logStore));
    }

    protected ResponseMessage sendData(String project, HttpMethod method, String resourceUri, Map<String, String> parameters, Map<String, String> headers, byte[] body) throws LogException {
        return sendData(project, method, resourceUri, parameters, headers, body, null, null);
    }

    private ResponseMessage sendData(String project, HttpMethod method, String resourceUri, Map<String, String> urlParams, Map<String, String> headParams) throws LogException {
        return sendData(project, method, resourceUri, urlParams, headParams, new byte[0]);
    }

    public ListShardResponse listShard(ListShardRequest request) throws LogException {
        CodingUtils.assertParameterNotNull(request, "request");
        String project = request.getProject();
        CodingUtils.assertStringNotNullOrEmpty(project, "project");
        String logStore = request.getLogStore();
        CodingUtils.assertStringNotNullOrEmpty(logStore, "logStore");
        Map<String, String> headParameter = getCommonHeadPara(project);
        String resourceUri = "/logstores/" + logStore + "/shards";
        Map<String, String> urlParameter = request.getAllParams();
        ResponseMessage response = sendData(project, HttpMethod.GET, resourceUri, urlParameter, headParameter);
        Map<String, String> resHeaders = response.getHeaders();
        String requestId = getRequestId(resHeaders);
        JSONArray array = parseResponseMessageToArray(response, requestId);
        ArrayList<Shard> shards = extractShards(array, requestId);
        return new ListShardResponse(resHeaders, shards);
    }

    private Map<String, String> getCommonHeadPara(String project) {
        HashMap<String, String> headParameter = new HashMap<>();
        headParameter.put(Consts.CONST_USER_AGENT, userAgent);
        headParameter.put(Consts.CONST_CONTENT_LENGTH, "0");
        headParameter.put(Consts.CONST_X_SLS_BODYRAWSIZE, "0");
        headParameter.put(Consts.CONST_CONTENT_TYPE, Consts.CONST_PROTO_BUF);
        headParameter.put(Consts.CONST_DATE, DateUtil.formatRfc822Date(new Date()));
        headParameter.put(Consts.CONST_HOST, this.hostName);
        headParameter.put(Consts.CONST_X_SLS_APIVERSION, Consts.DEFAULT_API_VESION);
        headParameter.put(Consts.CONST_X_SLS_SIGNATUREMETHOD, Consts.HMAC_SHA1);
        if (securityToken != null && !securityToken.isEmpty()) {
            headParameter.put(Consts.CONST_X_ACS_SECURITY_TOKEN, securityToken);
        }
        return headParameter;
    }

    private JSONArray parseResponseMessageToArray(ResponseMessage response, String requestId) throws LogException {
        String returnStr = encodeResponseBodyToUtf8String(response, requestId);
        try {
            return JSON.parseArray(returnStr);
        } catch (JSONException e) {
            throw new LogException(ErrorCodes.BAD_RESPONSE, "The response is not valid json string : " + returnStr, e, requestId);
        }
    }

    protected ArrayList<Shard> extractShards(JSONArray array, String requestId) throws LogException {
        ArrayList<Shard> shards = new ArrayList<Shard>();
        try {
            for (int i = 0; i < array.size(); i++) {
                JSONObject shardDict = array.getJSONObject(i);
                int shardId = shardDict.getInteger("shardID");
                String status = shardDict.getString("status");
                String begin = shardDict.getString("inclusiveBeginKey");
                String end = shardDict.getString("exclusiveEndKey");
                int createTime = shardDict.getInteger("createTime");
                Shard shard = new Shard(shardId, status, begin, end, createTime);
                if (shardDict.containsKey("serverIp")) {
                    shard.setServerIp(shardDict.getString("serverIp"));
                }
                shards.add(shard);
            }
        } catch (JSONException e) {
            throw new LogException(ErrorCodes.BAD_RESPONSE, "The response is not valid shard json array string : " + array + e.getMessage(), e, requestId);
        }

        return shards;
    }

    public String getServerIpAddress(String project) {
        Map<String, String> headParameter = getCommonHeadPara(project);
        String resourceUri = "/direct_mode_ip";
        Map<String, String> urlParameter = new HashMap<>();
        Map<String, String> out_header = new HashMap<>();
        try {
            sendData(project, HttpMethod.GET, resourceUri, urlParameter, headParameter, new byte[0], out_header, null);
        } catch (LogException e) {
            // ignore
        }
        return AbstractUtils.getOrEmpty(out_header, Consts.CONST_X_SLS_HOSTIP);
    }
}
