package tech.finovy.framework.oss.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.crypto.SimpleRSAEncryptionMaterials;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import tech.finovy.framework.oss.client.config.OssClientConfig;
import tech.finovy.framework.oss.client.config.OssClientEntity;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class OssClientMap {

    private static final Logger LOGGER = LoggerFactory.getLogger(OssClientMap.class);

    private static final Map<String, OSSEncryptionClient> OSS_ENCRYPTION_CLIENT_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();
    private static final Map<String, OSSClient> OSS_CLIENT_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();
    private static final Map<String, String> KEY_ENCRYPTION_CONFIGS = new HashMap<>();
    private static final Map<String, String> KEY_CONFIGS = new HashMap<>();
    private static final Map<String, String> KEY_PME_MAP = new HashMap<>();
    private static final Map<String, String> KEY_BUCKET_MAP = new HashMap<>();
    private static final String PUBLICX_509_PEMKEY = "publicX509Pem";
    private static final String PRIVATEPKCS_8_PEMKEY = "privatePkcs8Pem";

    public Map<String, OSSEncryptionClient> getOSSEncryptionClientMap() {
        return OSS_ENCRYPTION_CLIENT_CONCURRENT_HASH_MAP;
    }

    public Map<String, OSSClient> getOSSClientMap() {
        return OSS_CLIENT_CONCURRENT_HASH_MAP;
    }

    public synchronized void refreshClientMap(OssClientConfig clientConfig, String publicX509Pem, String privatePkcs8Pem) {
        KEY_PME_MAP.put(PUBLICX_509_PEMKEY, publicX509Pem);
        KEY_PME_MAP.put(PRIVATEPKCS_8_PEMKEY, privatePkcs8Pem);
        List<OssClientEntity> configs = clientConfig.getConfig();
        if (StringUtils.isNotBlank(publicX509Pem) && StringUtils.isNotBlank(privatePkcs8Pem)) {
            this.getEncryptionClients(configs, true);
        }
        this.getOSSClients(configs, true);
        LOGGER.info("key-Bucket:{}", JSON.toJSONString(KEY_BUCKET_MAP));
    }

    public void destroy(String bucketName) {
        OSSEncryptionClient client = OSS_ENCRYPTION_CLIENT_CONCURRENT_HASH_MAP.get(bucketName);
        if (client == null) {
            return;
        }
        client.shutdown();
        OSS_ENCRYPTION_CLIENT_CONCURRENT_HASH_MAP.remove(bucketName);
        OSSClient ossClient = OSS_CLIENT_CONCURRENT_HASH_MAP.get(bucketName);
        if (ossClient == null) {
            return;
        }
        ossClient.shutdown();
        OSS_CLIENT_CONCURRENT_HASH_MAP.remove(bucketName);
        return;
    }

    public String getBucketName(String key) {
        return KEY_BUCKET_MAP.get(key);
    }

    public OssClientEntity getOssClientEntity(String bucketName) {
        String json = KEY_CONFIGS.get(bucketName);
        if (StringUtils.isEmpty(json)) {
            json = KEY_ENCRYPTION_CONFIGS.get(bucketName);
        }
        if (StringUtils.isEmpty(json)) {
            throw new OSSException();
        }
        return JSONObject.parseObject(json, OssClientEntity.class);
    }

    public OSSEncryptionClient getEncryptionClient(String bucketName) {
        OSSEncryptionClient client = OSS_ENCRYPTION_CLIENT_CONCURRENT_HASH_MAP.get(bucketName);
        if (client == null && StringUtils.isNotBlank(KEY_ENCRYPTION_CONFIGS.get(bucketName))) {
            List<OssClientEntity> configs = new ArrayList<>();
            configs.add(JSONObject.parseObject(KEY_ENCRYPTION_CONFIGS.get(bucketName), OssClientEntity.class));
            this.getEncryptionClients(configs, false);
            client = OSS_ENCRYPTION_CLIENT_CONCURRENT_HASH_MAP.get(bucketName);
        }
        return client;
    }

    public OSSClient getOSSClient(String bucketName) {
        OSSClient client = OSS_CLIENT_CONCURRENT_HASH_MAP.get(bucketName);
        if (client == null && StringUtils.isNotBlank(KEY_CONFIGS.get(bucketName))) {
            List<OssClientEntity> configs = new ArrayList<>();
            configs.add(JSONObject.parseObject(KEY_CONFIGS.get(bucketName), OssClientEntity.class));
            this.getOSSClients(configs, false);
            client = OSS_CLIENT_CONCURRENT_HASH_MAP.get(bucketName);
        }
        return client;
    }

    public Map<String, OSSEncryptionClient> getEncryptionClients(List<OssClientEntity> configs, boolean isInit) {
        Map<String, OSSEncryptionClient> maps = new ConcurrentHashMap<>();
        for (OssClientEntity config : configs) {
            String bucketName = config.getBucketName();
            KEY_BUCKET_MAP.put(config.getKey(), bucketName);
//            config.setKey(null);
            String configStr = JSON.toJSONString(config);
            if (isInit && configStr.equals(KEY_ENCRYPTION_CONFIGS.get(config.getBucketName()))) {
                continue;
            }
            if (!isInit && OSS_ENCRYPTION_CLIENT_CONCURRENT_HASH_MAP.containsKey(bucketName)) {
                continue;
            }
            RSAPrivateKey privateKey = SimpleRSAEncryptionMaterials.getPrivateKeyFromPemPKCS8(KEY_PME_MAP.get(PRIVATEPKCS_8_PEMKEY));
            RSAPublicKey publicKey = SimpleRSAEncryptionMaterials.getPublicKeyFromPemX509(KEY_PME_MAP.get(PUBLICX_509_PEMKEY));
            KeyPair keyPair = new KeyPair(publicKey, privateKey);
            SimpleRSAEncryptionMaterials encryptionMaterials = new SimpleRSAEncryptionMaterials(keyPair, null);
            OSSEncryptionClient encryptionClient = new OSSEncryptionClientBuilder().
                    build(config.getEndpoint(), config.getAccessKeyId(), config.getAccessKeySecret(), encryptionMaterials);
            maps.put(bucketName, encryptionClient);
            KEY_ENCRYPTION_CONFIGS.put(bucketName, configStr);
            this.createTestClient(bucketName, encryptionClient);
        }
        if (!CollectionUtils.isEmpty(maps)) {
            OSS_ENCRYPTION_CLIENT_CONCURRENT_HASH_MAP.putAll(maps);
        }
        return maps;
    }

    public Map<String, OSSClient> getOSSClients(List<OssClientEntity> configs, boolean isInit) {
        Map<String, OSSClient> maps = new ConcurrentHashMap<>();
        for (OssClientEntity config : configs) {
            String bucketName = config.getBucketName();
            KEY_BUCKET_MAP.put(config.getKey(), bucketName);
//            config.setKey(null);
            String configStr = JSON.toJSONString(config);
            if (isInit && configStr.equals(KEY_CONFIGS.get(config.getBucketName()))) {
                continue;
            }
            if (!isInit && OSS_CLIENT_CONCURRENT_HASH_MAP.containsKey(bucketName)) {
                continue;
            }
            OSSClient client = new OSSClient(config.getEndpoint(),
                    new DefaultCredentialProvider(config.getAccessKeyId(), config.getAccessKeySecret()),
                    new ClientConfiguration());
            maps.put(bucketName, client);
            KEY_CONFIGS.put(bucketName, configStr);
            this.createTestClient(bucketName, client);
        }
        if (!CollectionUtils.isEmpty(maps)) {
            OSS_CLIENT_CONCURRENT_HASH_MAP.putAll(maps);
        }
        return maps;
    }

    private void createTestClient(String bucketName, OSSClient ossClient) {
        ossClient.doesObjectExist(bucketName, "test.jpg");
        LOGGER.info("------init oss success bucketName:{}", bucketName);
    }

    private void createTestClient(String bucketName, OSSEncryptionClient ossClient) {
        ossClient.doesObjectExist(bucketName, "test.jpg");
        LOGGER.info("------init oss Encryption success bucketName:{}", bucketName);
    }

}
