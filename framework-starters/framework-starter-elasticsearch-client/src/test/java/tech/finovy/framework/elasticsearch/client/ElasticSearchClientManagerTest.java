package tech.finovy.framework.elasticsearch.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import tech.finovy.framework.elasticsearch.client.autoconfigure.ElasticClientProperties;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ElasticSearchClientManagerTest {

    @Mock
    private ElasticClientProperties mockProperties;
    @Mock
    private ApplicationContext mockContext;

    private ElasticSearchClientManager elasticSearchClientManagerUnderTest;

    @BeforeEach
    void setUp() {
        elasticSearchClientManagerUnderTest = new ElasticSearchClientManager(mockProperties, mockContext);
    }

    @Test
    void testRefresh() {
        when(mockProperties.getDataId()).thenReturn("result");
        when(mockProperties.getDataGroup()).thenReturn("result");
        // Run the test
        Assertions.assertThrows(ElasticClientConfigurationException.class, () -> elasticSearchClientManagerUnderTest.refresh("dataId", "dataGroup", null, 0));
    }
}
