package cn.maiaimei.framework.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

public class RestLoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RestLoggingInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        final String requestBody = body.length > 0 ? new String(body, Charset.defaultCharset()) : "";
        if (log.isDebugEnabled()) {
            log.debug("URI: {}, Method: {}, Headers: {}, RequestBody: {}", request.getURI(), request.getMethod(), request.getHeaders(), requestBody);
        }
        final ClientHttpResponse response = execution.execute(getHttpRequestWrapper(request), body);
        final String responseBody = StreamUtils.copyToString(response.getBody(), Charset.defaultCharset());
        if (log.isDebugEnabled()) {
            log.debug("URI: {}, Status: {}, Headers: {}, ResponseBody: {}", request.getURI(), response.getStatusCode(), response.getHeaders(), responseBody);
        }
        return response;
    }

    /**
     * {@link HttpRequestWrapper}
     */
    protected HttpRequest getHttpRequestWrapper(HttpRequest request) {
        return request;
    }

}
