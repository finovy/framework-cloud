package tech.finovy.framework.common;


import tech.finovy.framework.compress.CompressorFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;


@Slf4j
public class UtilsTest {


    @Test
    @DisplayName("compress")
    void dateTimeUtilTest() throws CompressorException, IOException {
        String a="00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
        String b=CompressorFactory.compress(CompressorStreamFactory.GZIP,a);
        String c=CompressorFactory.uncompresss(CompressorStreamFactory.GZIP,b);
        Assertions.assertEquals(a, c);
    }


}
