package tech.finovy.framework.common.core.compress;

import org.apache.commons.compress.compressors.CompressorStreamFactory;


public enum CompressorType {
    GZIP(CompressorStreamFactory.GZIP),
    BZIP2(CompressorStreamFactory.BZIP2),
    XZ(CompressorStreamFactory.XZ),
    PACK200(CompressorStreamFactory.PACK200),
    LZMA(CompressorStreamFactory.LZMA),
    DEFLATE(CompressorStreamFactory.DEFLATE),
    SNAPPY_FRAMED(CompressorStreamFactory.SNAPPY_FRAMED),
    LZ4_BLOCK(CompressorStreamFactory.LZ4_BLOCK),
    LZ4_FRAMED(CompressorStreamFactory.LZ4_FRAMED),
    ZSTANDARD(CompressorStreamFactory.ZSTANDARD);
    private final String type;

    CompressorType(String type) {
        this.type = type;
    }

    public String getValue() {
        return type;
    }
}
