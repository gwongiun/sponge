package com.ly.add.sponge.common.http;

import com.ly.add.sponge.common.http.HttpSSLs.SSLProtocolVersion;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * @author : qqy48861
 * date : 2019/3/27.
 */
public class HttpGun extends HttpClientBuilder {

    private SSLProtocolVersion sslpv = SSLProtocolVersion.TLSv1_2;//ssl 协议版本
    //用于配置ssl
    private HttpSSLs ssls = HttpSSLs.getInstance();

    private HttpGun() {
    }

    public static HttpGun create() {
        return new HttpGun();
    }

    public HttpGun timeout(int timeout) {
        return timeout(timeout, true);
    }

    private HttpGun timeout(int timeout, boolean redirectEnable) {
        // 配置请求的超时设置
        RequestConfig config = RequestConfig.custom()
                .setConnectionRequestTimeout(timeout)
                .setConnectTimeout(timeout)
                .setSocketTimeout(timeout)
                .setRedirectsEnabled(redirectEnable)
                .build();
        return (HttpGun) this.setDefaultRequestConfig(config);
    }

    public HttpGun pool(int maxTotal, int defaultMaxPerRoute, Map<HttpRoute, Integer> map) {
        SSLConnectionSocketFactory sslconnsf = null;
        try {
            sslconnsf = ssls.getSSLCONNSF(sslpv);
        } catch (HttpProcessException e) {
            e.printStackTrace();
        }
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                .<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", sslconnsf).build();
        //设置连接池大小
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connManager.setMaxTotal(maxTotal);// Increase max total connection to $maxTotal
        connManager.setDefaultMaxPerRoute(defaultMaxPerRoute);// Increase default max connection per route to $defaultMaxPerRoute
        if (map != null) {
            for (Map.Entry<HttpRoute, Integer> me : map.entrySet()) {
                connManager.setMaxPerRoute(me.getKey(), me.getValue());
            }
        }
        //connManager.setMaxPerRoute(route, max);// Increase max connections for $route(eg：localhost:80) to 50
        return (HttpGun) this.setConnectionManager(connManager);
    }

    /**
     * 重试（如果请求是幂等的，就再次尝试）
     *
     * @param tryTimes 重试次数
     * @return 返回当前对象
     */
    public HttpGun retry(final int tryTimes) {
        // 请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount >= tryTimes) {// 如果已经重试了n次，就放弃
                    return false;
                }
                if (exception instanceof ConnectionPoolTimeoutException) {// 本地连接池丢弃器连接
                    return true;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return false;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return true;
                }
                if (exception instanceof SSLException) {// SSL握手异常
                    return false;
                }
                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };
        this.setRetryHandler(httpRequestRetryHandler);
        return this;
    }

}