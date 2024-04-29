package tech.finovy.framework.common.core.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringFormatUtilsTest {

    @Test
    void testCamelToUnderline() {
        assertThat(StringFormatUtils.camelToUnderline("dataCode")).isEqualTo("data_code");
    }

    @Test
    void testUnderlineToCamel() {
        assertThat(StringFormatUtils.underlineToCamel("data_code")).isEqualTo("dataCode");
    }

    @Test
    void testMinusToCamel() {
        assertThat(StringFormatUtils.minusToCamel("data-code")).isEqualTo("dataCode");
    }

    @Test
    void testDotToCamel() {
        assertThat(StringFormatUtils.dotToCamel("data.code")).isEqualTo("dataCode");
    }
}
