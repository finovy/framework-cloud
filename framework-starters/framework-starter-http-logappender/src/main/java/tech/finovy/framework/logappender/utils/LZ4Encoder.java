package tech.finovy.framework.logappender.utils;

import tech.finovy.framework.logappender.exception.LogException;
import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Exception;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;

public class LZ4Encoder {
    private LZ4Encoder() {
    }

    public static byte[] compressToLhLz4Chunk(byte[] data) throws LogException {
        final int rawSize = data.length;
        LZ4Factory factory = LZ4Factory.fastestInstance();

        // compress data
        LZ4Compressor compressor = factory.fastCompressor();

        int maxCompressedLength = compressor.maxCompressedLength(rawSize);
        int encodingSize;
        byte[] rawCompressed = new byte[maxCompressedLength];
        try {
            encodingSize = compressor.compress(data, 0, rawSize, rawCompressed, 0, maxCompressedLength);
        } catch (LZ4Exception e) {
            throw new LogException("CompressException", e.getMessage(), "");
        }

        if (encodingSize <= 0) {
            throw new LogException("CompressException", "Invalid enconding size", "");
        }

        byte[] ret = new byte[encodingSize];
        System.arraycopy(rawCompressed, 0, ret, 0, encodingSize);

        return ret;
    }

    public static byte[] decompressFromLhLz4Chunk(byte[] compressedData, int rawSize) throws LogException {
        LZ4Factory factory = LZ4Factory.fastestInstance();
        LZ4FastDecompressor decompressor = factory.fastDecompressor();

        byte[] restored = new byte[rawSize];
        try {
            decompressor.decompress(compressedData, 0, restored, 0, rawSize);
        } catch (LZ4Exception e) {
            throw new LogException("DecompressException", e.getMessage(), "");
        }
        return restored;
    }
}
