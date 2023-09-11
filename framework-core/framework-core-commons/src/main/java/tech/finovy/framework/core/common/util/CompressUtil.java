package tech.finovy.framework.core.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


public class CompressUtil {

    /**
     * Compress byte [ ].
     *
     * @param src the src
     * @return the byte [ ]
     * @throws IOException the io exception
     */
    public static byte[] compress(final byte[] src) throws IOException {
        byte[] result;
        ByteArrayOutputStream bos = new ByteArrayOutputStream(src.length);
        GZIPOutputStream gos = new GZIPOutputStream(bos);
        try {
            gos.write(src);
            gos.finish();
            result = bos.toByteArray();
        } finally {
            IOUtil.close(bos, gos);
        }
        return result;
    }

    /**
     * Uncompress byte [ ].
     *
     * @param src the src
     * @return the byte [ ]
     * @throws IOException the io exception
     */
    public static byte[] uncompress(final byte[] src) throws IOException {
        byte[] result;
        byte[] uncompressData = new byte[src.length];
        ByteArrayInputStream bis = new ByteArrayInputStream(src);
        GZIPInputStream iis = new GZIPInputStream(bis);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(src.length);

        try {
            while (true) {
                int len = iis.read(uncompressData, 0, uncompressData.length);
                if (len <= 0) {
                    break;
                }
                bos.write(uncompressData, 0, len);
            }
            bos.flush();
            result = bos.toByteArray();
        } finally {
            IOUtil.close(bis, iis, bos);
        }
        return result;
    }

    /**
     * Is compress data boolean.
     *
     * @param bytes the bytes
     * @return the boolean
     */
    public static boolean isCompressData(byte[] bytes) {
        if (bytes != null && bytes.length > 2) {
            int header = ((bytes[0] & 0xff)) | (bytes[1] & 0xff) << 8;
            return GZIPInputStream.GZIP_MAGIC == header;
        }
        return false;
    }
}
