package tech.finovy.framework.compress;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
@Slf4j
public class CompressorFactory {
    private CompressorFactory() {
    }

    public static String compress(String compressor, String txt) throws CompressorException, IOException{
        byte[] resp=txt.getBytes(StandardCharsets.UTF_8);
        byte[] b=compress(compressor,resp);
        return Base64.encodeBase64String(b);
    }

    public static byte[] compress(String compressor,byte[] txt) throws CompressorException, IOException {
         ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (CompressorOutputStream gzip = new CompressorStreamFactory().createCompressorOutputStream(compressor, out)) {
            gzip.write(txt);
        } catch (IOException | CompressorException e) {
            log.error("compress error:{}", e);
            throw e;
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                log.error("Close Stream error:{}", e.toString());
            }
        }
        return out.toByteArray();
    }
    public static String uncompresss(String compressor,String txt) throws CompressorException, IOException {
        byte[] resp=Base64.decodeBase64(txt);
        byte[] b=uncompresss(compressor,resp);
        return new String(b);
    }
    public static byte[] uncompresss(String compressor,byte[] txt) throws CompressorException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(txt);
        try (CompressorInputStream gzip = new CompressorStreamFactory().createCompressorInputStream(compressor, in)) {
            byte[] buffer = new byte[1024];
            int n;
            while ((n = gzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        } catch (IOException | CompressorException e) {
            log.error("uncompress error:{}", e.toString());
           throw e;
        } finally {
            try {
                out.close();
                in.close();
            } catch (IOException e) {
                log.error("Close Stream error:{}", e.toString());
            }
        }
    }
}
