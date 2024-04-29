package tech.finovy.framework.common.core.util;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MapUtilTest {

    @Test
    void testAsMap() {
        // case 1
        String data = "object";
        final Map<String, Object> result = MapUtil.asMap(data);
        final LinkedHashMap<String, Object> check = new LinkedHashMap<>();
        check.put("document",data);
        assertThat(result).isEqualTo(check);
        // case 2
        final HashMap<Object, Object> dataMap = new HashMap<>();
        dataMap.put("name","Bob");
        dataMap.put(18,"age");
        final Map<String, Object> resultMap = MapUtil.asMap(dataMap);
        assertThat(resultMap.get("[18]")).isEqualTo("age");
    }

    @Test
    void testGetFlattenedMap() {
        // case 1
        assertThat(MapUtil.getFlattenedMap(Map.ofEntries(Map.entry("key", "value"))).get("key")).isEqualTo("value");
        // case 2
        final HashMap<Object, Object> dataMap = new HashMap<>();
        dataMap.put("name","Bob");
        dataMap.put("age",18);
        final Map<String, Object> source = Map.ofEntries(Map.entry("key", dataMap));
        final Map<String, Object> result = MapUtil.getFlattenedMap(source);
        assertThat(result.get("key.age")).isEqualTo(18);
        // case 3
        assertThat(MapUtil.getFlattenedMap(Map.ofEntries(Map.entry("key", Lists.newArrayList("v1", "v2")))).get("key[0]")).isEqualTo("v1");
        assertThat(MapUtil.getFlattenedMap(Map.ofEntries(Map.entry("key", 1))).get("key")).isEqualTo(1);

    }
}
