package tech.finovy.framework.core.common.util;

import tech.finovy.framework.core.common.BranchDO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BeanUtilsTest {

    @Test
    public void testBeanToStringNotNull() {
        BranchDO branchDO = new BranchDO("xid123123", 123L, 1, 2.2, new Date());
        Assertions.assertNotNull(BeanUtils.beanToString(branchDO));
    }

    @Test
    public void testBeanToStringNull() {
        BranchDO branchDO = null;
        Assertions.assertNull(BeanUtils.beanToString(branchDO));
    }

    @Test
    public void testMapToObjectNotNull() {
        Map<String, String> map = new HashMap<>();
        Date date = new Date();
        map.put("xid", "192.166.166.11:9010:12423424234234");
        map.put("transactionId", "12423424234234");
        map.put("status", "2");
        map.put("test", "22.22");
        map.put("gmtCreate", String.valueOf(date.getTime()));
        BranchDO branchDO =
                (BranchDO) BeanUtils.mapToObject(map, BranchDO.class);
        Assertions.assertEquals(map.get("xid"), branchDO.getXid());
        Assertions.assertEquals(Long.valueOf(map.get("transactionId")), branchDO.getTransactionId());
        Assertions.assertEquals(Integer.valueOf(map.get("status")), branchDO.getStatus());
        Assertions.assertEquals(Double.valueOf(map.get("test")), branchDO.getTest());
        Assertions.assertEquals(new Date(date.getTime()), branchDO.getGmtCreate());
    }

    @Test
    public void testMapToObjectNull() {
        Map<String, String> map = null;
        BranchDO branchDO =
                (BranchDO) BeanUtils.mapToObject(map, BranchDO.class);
        Assertions.assertNull(branchDO);
    }

    @Test
    public void testObjectToMapNotNull() {
        BranchDO branchDO = new BranchDO("xid123123", 123L, 1, 2.2, new Date());
        Map<String, String> map = BeanUtils.objectToMap(branchDO);
        Assertions.assertEquals(branchDO.getXid(), map.get("xid"));
        Assertions.assertEquals(branchDO.getTransactionId(), Long.valueOf(map.get("transactionId")));
        Assertions.assertEquals(branchDO.getStatus(), Integer.valueOf(map.get("status")));
        Assertions.assertEquals(branchDO.getTest(), Double.valueOf(map.get("test")));
        Assertions.assertEquals(branchDO.getGmtCreate().getTime(), Long.valueOf(map.get("gmtCreate")));
    }

    @Test
    public void testObjectToMapNull() {
        BranchDO branchDO = null;
        Map<String, String> map = BeanUtils.objectToMap(branchDO);
        Assertions.assertNull(map);
    }

}
