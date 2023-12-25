package tech.finovy.framework.oss.client;


import com.alibaba.cloud.nacos.NacosConfigAutoConfiguration;
import com.aliyun.oss.OSSException;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tech.finovy.framework.config.nacos.ShardingEngineNacosConfigAutoConfiguration;
import tech.finovy.framework.disruptor.core.DisruptorEventConfiguration;
import tech.finovy.framework.oss.client.service.OssClientEncryptionService;
import tech.finovy.framework.oss.client.service.OssClientService;

@Slf4j
@ContextConfiguration
@ExtendWith(SpringExtension.class)
@ImportAutoConfiguration({RefreshAutoConfiguration.class, OssClientAutoConfiguration.class, ShardingEngineNacosConfigAutoConfiguration.class, NacosConfigAutoConfiguration.class, DisruptorEventConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = OssClientTest.class)
public class OssClientTest {

    @Autowired
    private OssClientService ossClientService;
    @Autowired
    private OssClientEncryptionService encryptionService;

    @Autowired
    private OssClientMap ossClientMap;

    @Test
    public void ossClientTest() {
        String customBucketName = "test-bucket";
        String smBucket = "sm-dev";
        Assertions.assertNotNull(ossClientMap.getBucketName(customBucketName));
        Assertions.assertNotNull(ossClientMap.getOSSClient(smBucket));
        Assertions.assertNull(ossClientMap.getEncryptionClient(customBucketName));
        Assertions.assertNotNull(ossClientMap.getOssClientEntity(smBucket));

        Assertions.assertThrows(OSSException.class, () -> ossClientMap.getOssClientEntity("sm-dev-test"));
        Assertions.assertNotNull(ossClientMap.getOSSClientMap());
        Assertions.assertNotNull(ossClientMap.getOSSEncryptionClientMap());
        ossClientMap.destroy(customBucketName);
        // normal
        Assertions.assertThrows(OSSException.class, () -> ossClientService.getObject(smBucket, "test.file"));
        Assertions.assertFalse(ossClientService.doesObjectExist(smBucket, "test.file"));
        Assertions.assertThrows(OSSException.class, () -> ossClientService.deleteObjects(smBucket, Lists.newArrayList("test-key")));
        // encrypt
        Assertions.assertThrows(OSSException.class, () -> encryptionService.getObject(smBucket, "test.file"));
        Assertions.assertFalse(encryptionService.doesObjectExist(smBucket, "test.file"));
        Assertions.assertThrows(OSSException.class, () -> encryptionService.deleteObjects(smBucket, Lists.newArrayList("test-key")));

        ossClientMap.destroy(smBucket);

        Assertions.assertNotNull(ossClientMap.getEncryptionClient(smBucket));
        Assertions.assertNotNull(ossClientMap.getOSSClient(smBucket));
    }

}
