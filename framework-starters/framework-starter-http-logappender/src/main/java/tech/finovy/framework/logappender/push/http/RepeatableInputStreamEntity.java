package tech.finovy.framework.logappender.push.http;

import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.BasicHttpEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class RepeatableInputStreamEntity extends BasicHttpEntity {

    private final NoAutoClosedInputStreamEntity innerEntity;
    private final InputStream content;
    private boolean firstAttempt = true;

    public RepeatableInputStreamEntity(AbstractServiceClient.Request request) {
        setChunked(false);
        String contentType = request.getHeaders().get(HttpHeaders.CONTENT_TYPE);
        content = request.getContent();
        long contentLength = request.getContentLength();
        innerEntity = new NoAutoClosedInputStreamEntity(content, contentLength);
        innerEntity.setContentType(contentType);
        setContent(content);
        setContentType(contentType);
        setContentLength(request.getContentLength());
    }

    @Override
    public boolean isChunked() {
        return false;
    }

    @Override
    public boolean isRepeatable() {
        return content.markSupported() || innerEntity.isRepeatable();
    }

    @Override
    public void writeTo(OutputStream output) throws IOException {
        if (!firstAttempt && isRepeatable()) {
            content.reset();
        }
        firstAttempt = false;
        innerEntity.writeTo(output);
    }

    public static class NoAutoClosedInputStreamEntity extends AbstractHttpEntity {
        private static final int BUFFER_SIZE = 2048;
        private final InputStream content;
        private final long length;

        public NoAutoClosedInputStreamEntity(final InputStream instream, long length) {
            super();
            if (instream == null) {
                throw new IllegalArgumentException("Source input stream may not be null");
            }
            this.content = instream;
            this.length = length;
        }

        @Override
        public boolean isRepeatable() {
            return false;
        }

        @Override
        public long getContentLength() {
            return this.length;
        }

        @Override
        public InputStream getContent() {
            return this.content;
        }

        @Override
        public void writeTo(final OutputStream outstream) throws IOException {
            if (outstream == null) {
                throw new IllegalArgumentException("Output stream may not be null");
            }
            InputStream instream = this.content;
            byte[] buffer = new byte[BUFFER_SIZE];
            int l;
            if (this.length < 0) {
                // consume until EOF
                while ((l = instream.read(buffer)) != -1) {
                    outstream.write(buffer, 0, l);
                }
            } else {
                // consume no more than length
                long remaining = this.length;
                while (remaining > 0) {
                    l = instream.read(buffer, 0, (int) Math.min(BUFFER_SIZE, remaining));
                    if (l == -1) {
                        break;
                    }
                    outstream.write(buffer, 0, l);
                    remaining -= l;
                }
            }

        }

        public boolean isStreaming() {
            return true;
        }
    }
}
