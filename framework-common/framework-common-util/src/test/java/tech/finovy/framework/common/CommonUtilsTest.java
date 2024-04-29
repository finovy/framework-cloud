package tech.finovy.framework.common;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tech.finovy.framework.utils.DateTimeUtil;


@Slf4j
public class CommonUtilsTest {


    @Test
    @DisplayName("TestDateTimeUtil")
    void dateTimeUtilTest() {
        DateTimeUtil.getTimeStamp();
    }


}
