package cn.maiaimei.framework.web.client;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import javax.net.ssl.SSLContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class RestTemplateFactory {

  public static <T extends RestLoggingInterceptor> RestTemplate getRestTemplate(
      T restLoggingInterceptor, RestErrorHandler restErrorHandler)
      throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
    TrustStrategy trustStrategy = (x509Certificates, s) -> true;
    final SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, trustStrategy)
        .build();
    final SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext,
        new NoopHostnameVerifier());
    final CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory)
        .build();
    final HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
        httpClient);
    return new RestTemplateBuilder()
        .requestFactory(() -> new BufferingClientHttpRequestFactory(requestFactory))
        .interceptors(Collections.singletonList(restLoggingInterceptor))
        .errorHandler(restErrorHandler)
        .build();
  }

}
