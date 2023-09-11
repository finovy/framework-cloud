package tech.finovy.framework.core.common.util;


public class IOUtil {

    /**
     * close Closeable
     *
     * @param closeables the closeables
     */
    public static void close(AutoCloseable... closeables) {
        if (CollectionUtils.isNotEmpty(closeables)) {
            for (AutoCloseable closeable : closeables) {
                close(closeable);
            }
        }
    }

    /**
     * close Closeable
     *
     * @param closeable the closeable
     */
    public static void close(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ignore) {
            }
        }
    }

}
