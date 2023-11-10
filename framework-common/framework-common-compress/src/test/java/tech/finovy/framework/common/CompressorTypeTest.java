package tech.finovy.framework.common;

import org.junit.jupiter.api.Test;
import tech.finovy.framework.compress.CompressorType;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CompressorTypeTest {

    @Test
    void testGetValue() {
        assertEquals("gz", CompressorType.GZIP.getValue());
        assertEquals("bzip2", CompressorType.BZIP2.getValue());
        assertEquals("xz", CompressorType.XZ.getValue());
        assertEquals("pack200", CompressorType.PACK200.getValue());
        assertEquals("lzma", CompressorType.LZMA.getValue());
        assertEquals("deflate", CompressorType.DEFLATE.getValue());
        assertEquals("snappy-framed", CompressorType.SNAPPY_FRAMED.getValue());
        assertEquals("lz4-block", CompressorType.LZ4_BLOCK.getValue());
        assertEquals("lz4-framed", CompressorType.LZ4_FRAMED.getValue());
        assertEquals("zstd", CompressorType.ZSTANDARD.getValue());
    }

}
