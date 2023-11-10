package tech.finovy.framework.redisson;

import com.alibaba.cloud.nacos.NacosConfigAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tech.finovy.framework.config.nacos.ShardingEngineNacosConfigAutoConfiguration;
import tech.finovy.framework.disruptor.core.DisruptorEventConfiguration;
import tech.finovy.framework.distributed.map.api.MapService;
import tech.finovy.framework.redisson.autoconfigure.RedissonClientAutoConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@ContextConfiguration
@ExtendWith(SpringExtension.class)
@ImportAutoConfiguration({RedissonClientAutoConfiguration.class, ShardingEngineNacosConfigAutoConfiguration.class, NacosConfigAutoConfiguration.class, DisruptorEventConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,classes = MapServiceTest.class)
public class MapServiceTest {
    private final static String txt = "this is a test message!!";
    private final static String MAP_KEY = "mapTest";
    private final static String KEY = "mapTest";
    @Autowired
    private MapService mapService;

    @Test
    @DisplayName("TestMapService")
    void mapServiceImplTest() {
        mapService.clear(MAP_KEY);
        boolean empty = mapService.isEmpty(MAP_KEY);
        Assertions.assertTrue(empty);
        String s = mapService.get(MAP_KEY, KEY);
        Assertions.assertNull(s);
        boolean exists = mapService.containsKey(MAP_KEY, KEY);
        Assertions.assertFalse(exists);
        mapService.put(MAP_KEY, KEY, txt);
        s = mapService.get(MAP_KEY, KEY);
        Assertions.assertNotNull(s);
        empty = mapService.isEmpty(MAP_KEY);
        Assertions.assertFalse(empty);
        exists = mapService.containsKey(MAP_KEY, KEY);
        Assertions.assertTrue(exists);
        mapService.remove(MAP_KEY, KEY);
        exists = mapService.containsKey(MAP_KEY, KEY);
        Assertions.assertFalse(exists);
        Map<String,String> tmp=new HashMap<>();
        tmp.put("1","3");
        tmp.put("2","2");
        mapService.putAll(MAP_KEY,tmp);
        int size=mapService.size(MAP_KEY);
        Assertions.assertEquals(size,2);
        Set<String> set= mapService.keySet(MAP_KEY);
        Assertions.assertEquals(set.size(),2);
        mapService.clear(MAP_KEY);
    }


}
