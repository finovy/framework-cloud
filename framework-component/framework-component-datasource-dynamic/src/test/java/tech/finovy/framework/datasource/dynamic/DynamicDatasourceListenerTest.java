package tech.finovy.framework.datasource.dynamic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import tech.finovy.framework.common.SecurityEncryption;
import tech.finovy.framework.datasource.dynamic.entity.DynamicDatasourceConfig;
import tech.finovy.framework.datasource.dynamic.entity.DynamicDatasourceConfigList;
import tech.finovy.framework.datasource.dynamic.manager.DynamicDatasourceListener;
import tech.finovy.framework.datasource.dynamic.manager.DynamicDatasourceProperties;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class DynamicDatasourceListenerTest {

    @Mock
    private DynamicDatasourceProperties mockProperties;

    private DynamicDatasourceListener dynamicDatasourceListenerUnderTest;

    @BeforeEach
    void setUp() {
        initMocks(this);
        dynamicDatasourceListenerUnderTest = new DynamicDatasourceListener(mockProperties);
    }

    @Test
    void testGetDataId() {
        // Setup
        when(mockProperties.getDataId()).thenReturn("dataId");
        // Run the test
        final String result = dynamicDatasourceListenerUnderTest.getDataId();
        // Verify the results
        assertEquals("dataId", result);
    }

    @Test
    void testGetDataGroup() {
        // Setup
        when(mockProperties.getDataGroup()).thenReturn("dataGroup");
        // Run the test
        final String result = dynamicDatasourceListenerUnderTest.getDataGroup();
        // Verify the results
        assertEquals("dataGroup", result);
    }

    @Test
    void testRefresh() {
        // Setup
        final DynamicDatasourceConfigList config = new DynamicDatasourceConfigList();
        config.setEncrypt(true);
        final DynamicDatasourceConfig dynamicDatasourceConfig = new DynamicDatasourceConfig();
        dynamicDatasourceConfig.setKey("gtlJ3UPkellEOKOIZF0cJA==");
        dynamicDatasourceConfig.setUrl("yzZrNBYug8Di+VFRDZ4eBBy1f9GOXD2+8ooYhharZT84puUgeK9vWsQwHrl0DqPW");
        dynamicDatasourceConfig.setUsername("gWwo7GECs9rEVQFd3Nz2gg==");
        dynamicDatasourceConfig.setPassword("gWwo7GECs9rEVQFd3Nz2gg==");
        dynamicDatasourceConfig.setAsyncInit(false);
        dynamicDatasourceConfig.setPoolPreparedStatements(false);


        when(mockProperties.getDataId()).thenReturn("dataId");
        when(mockProperties.getSecret()).thenReturn("WtuFIq2gOLhC972mH7xBC4mB4zjg9dUi");
        when(mockProperties.getIv()).thenReturn("IB9N75V82Q0KJ3BK");

        // Run the test
        dynamicDatasourceListenerUnderTest.refresh("dataId", "dataGroup", config, 0);
        config.setConfig(List.of(dynamicDatasourceConfig));
        dynamicDatasourceListenerUnderTest.refresh("dataId", "dataGroup", config, 0);
        dynamicDatasourceListenerUnderTest.refresh("dataId", "dataGroup", config, 0);
        // Destroy
        final boolean result = dynamicDatasourceListenerUnderTest.destroyDatasource("key");
        // Verify the results
        assertTrue(result);

        when(mockProperties.getSecret()).thenReturn(null);
        when(mockProperties.getIv()).thenReturn(null);
        dynamicDatasourceListenerUnderTest.refresh("dataId", "dataGroup", config, 0);

        when(mockProperties.getSecret()).thenThrow(new RuntimeException());
        dynamicDatasourceListenerUnderTest.refresh("dataId", "dataGroup", config, 0);
    }

    @Test
    void testCreateDatasource() {
        assertFalse(dynamicDatasourceListenerUnderTest.createDatasource("key"));
    }

}
