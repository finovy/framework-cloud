package tech.finovy.framework.core.common.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class CompressUtilTest {

    final byte[] originBytes = new byte[]{1, 2, 3};

    final byte[] compressedBytes1 = new byte[]{31, -117, 8, 0, 0, 0, 0, 0, 0, 0,
            99, 100, 98, 6, 0, 29, -128, -68, 85, 3, 0, 0, 0};

    // for java17
    final byte[] compressedBytes2 = new byte[]{31, -117, 8, 0, 0, 0, 0, 0, 0, -1,
            99, 100, 98, 6, 0, 29, -128, -68, 85, 3, 0, 0, 0};


//    @Test
//    @DisabledOnJre(JRE.JAVA_15)
//    public void testCompress() throws IOException {
//        Assertions.assertArrayEquals(compressedBytes1, CompressUtil.compress(originBytes));
//    }

//    @Test
//    @EnabledOnJre(JRE.JAVA_15)
//    public void testCompressForJava17() throws IOException {
//        Assertions.assertArrayEquals(compressedBytes2,
//                CompressUtil.compress(originBytes));
//    }

    @Test
    public void testUncompress() throws IOException {
        Assertions.assertArrayEquals(originBytes,
                CompressUtil.uncompress(compressedBytes1));

        Assertions.assertArrayEquals(originBytes,
                CompressUtil.uncompress(compressedBytes2));
    }

    @Test
    public void testIsCompressData() {
        Assertions.assertFalse(CompressUtil.isCompressData(null));
        Assertions.assertFalse(CompressUtil.isCompressData(new byte[0]));
        Assertions.assertFalse(CompressUtil.isCompressData(new byte[]{31, 11}));
        Assertions.assertFalse(
                CompressUtil.isCompressData(new byte[]{31, 11, 0}));

        Assertions.assertTrue(
                CompressUtil.isCompressData(new byte[]{31, -117, 0}));
    }
}
