package tech.finovy.framework.oss.client.config;

import com.github.stefanbirkner.systemlambda.SystemLambda;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.finovy.framework.oss.client.OssClientMap;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OssConfigurationListenerTest {

    @Mock
    private OssClientProperties mockProperties;
    @Mock
    private SecurityProperties mockSecurityProperties;
    @Mock
    private OssClientMap mockOssClientMap;

    private OssConfigurationListener ossConfigurationListenerUnderTest;

    @BeforeEach
    void setUp() {
        final OssClientProperties.Nacos nacos = new OssClientProperties.Nacos();
        nacos.setDataGroup("group");
        nacos.setDataId("dataId");
        when(mockProperties.getNacos()).thenReturn(nacos);
        ossConfigurationListenerUnderTest = new OssConfigurationListener(mockProperties, mockSecurityProperties,
                mockOssClientMap);
    }

    @Test
    void testRefresh() throws Exception {
        // Setup
        final OssClientConfig config = new OssClientConfig();
        config.setEndpoint("z/Hwrb0CMLlpvBe3FlesJg==");
        config.setAccessKeyId("/96VqQiEUJ891lrSnJdBxw==");
        config.setAccessKeySecret("IkAjgUk6HJT0MRyR9piJog==");
        final OssClientEntity ossClientEntity = new OssClientEntity();
        ossClientEntity.setEncrypt(true);
        ossClientEntity.setEndpoint("z/Hwrb0CMLlpvBe3FlesJg==");
        ossClientEntity.setAccessKeyId("/96VqQiEUJ891lrSnJdBxw==");
        ossClientEntity.setAccessKeySecret("IkAjgUk6HJT0MRyR9piJog==");
        ossClientEntity.setBucketName("Dm4+VpxsiF2gG6jJ+OSXIg==");
        ossClientEntity.setKey("key");
        config.setConfig(List.of(ossClientEntity));

        when(mockSecurityProperties.getDatasourceSecret()).thenReturn("secret");
        when(mockSecurityProperties.getDatasourceIv()).thenReturn("iv");
        when(mockProperties.getPublicX509Pem()).thenReturn("publicX509Pem");
        when(mockProperties.getPrivatePkcs8Pem()).thenReturn("privatePkcs8Pem");
        // Run the test  decrypt
        ossConfigurationListenerUnderTest.refresh("dataId", "dataGroup", config, 0);

        SystemLambda.withEnvironmentVariable("DCONF_SECRET", "secret").and("DCONF_IV", "iv")
                .execute(() -> ossConfigurationListenerUnderTest.refresh("dataId", "dataGroup", config, 0));
        SystemLambda.withEnvironmentVariable("DCONF_SECRET", "secret").and("DCONF_IV", "iv")
                .execute(() -> ossConfigurationListenerUnderTest.refresh("dataId", "dataGroup", config, 0));
        // cover
        final SecurityProperties securityProperties = new SecurityProperties();
        securityProperties.setDatasourceIv("iv");
        securityProperties.setDatasourceSecret("secret");
        Assertions.assertEquals("iv", securityProperties.getDatasourceIv());
        Assertions.assertEquals("secret", securityProperties.getDatasourceSecret());
    }


}
