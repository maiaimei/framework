package cn.maiaimei.framework.spring.boot.web.filter;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class RepeatableHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private final byte[] bytes;

    public RepeatableHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        bytes = IOUtils.toByteArray(request.getInputStream());
    }

    @Override
    public ServletInputStream getInputStream() {
        return new ServletInputStream() {
            private int lastIndexRetrieved = -1;
            private ReadListener readListener = null;

            @Override
            public boolean isFinished() {
                return (lastIndexRetrieved == bytes.length - 1);
            }

            @Override
            public boolean isReady() {
                // This implementation will never block
                // We also never need to call the readListener from this method, as this method will never return false
                return isFinished();
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                this.readListener = readListener;
                if (!isFinished()) {
                    try {
                        readListener.onDataAvailable();
                    } catch (IOException e) {
                        readListener.onError(e);
                    }
                } else {
                    try {
                        readListener.onAllDataRead();
                    } catch (IOException e) {
                        readListener.onError(e);
                    }
                }
            }

            @Override
            public int read() throws IOException {
                int i;
                if (!isFinished()) {
                    i = bytes[lastIndexRetrieved + 1];
                    lastIndexRetrieved++;
                    if (isFinished() && (readListener != null)) {
                        try {
                            readListener.onAllDataRead();
                        } catch (IOException ex) {
                            readListener.onError(ex);
                            throw ex;
                        }
                    }
                    return i;
                } else {
                    return -1;
                }
            }
        };
    }

    @Override
    public BufferedReader getReader() {
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        BufferedReader temp = new BufferedReader(new InputStreamReader(is));
        return temp;
    }

    @Override
    public String getCharacterEncoding() {
        String enc = super.getCharacterEncoding();
        return (enc != null ? enc : "UTF-8");
    }

    @SneakyThrows
    public String getPayload() {
        if (null == bytes || bytes.length == 0) {
            return StringUtils.EMPTY;
        }
        return new String(bytes, getCharacterEncoding());
    }
}