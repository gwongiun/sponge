package com.ly.add.sponge.common.http;


import com.alibaba.fastjson.JSONObject;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author : qqy48861
 * date : 2019/3/27.
 */
public class HttpBullet {

    public HttpBullet() {
    }

    public static HttpBullet custom() {
        return new HttpBullet();
    }

    private String url;

    private Header[] headers;

    private HttpMethods method = HttpMethods.GET;

    private String paramString;

    private JSONObject paramJSONObject;

    private Map<String, Object> paramMap;

    private RetryFunction<HttpReport, Integer> retry;

    private FailedFunction<HttpReport> failed;

    private DisasterFunction disaster;

    private Charset inenc = Consts.UTF_8;

    private Charset outenc = Consts.UTF_8;

    private Map<String, File> file;

    private HttpEntitys entity = HttpEntitys.JSON;

    private CloseableHttpClient client;

    private HttpRequestBase httpRequestBase;

    private String param;

    public HttpBullet url(String url) {
        this.url = url;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public HttpBullet headers(Header[] headers) {
        this.headers = headers;
        return this;
    }

    public Header[] getHeaders() {
        return headers;
    }

//    public HttpBullet method(HttpMethods method, HttpEntitys entity) {
//        this.entity = entity;
//        this.method = method;
//        return this;
//    }

    public HttpBullet method(HttpMethods method) {
        this.method = method;
        return this;
    }

    public HttpMethods getMethod() {
        return method;
    }

//    public HttpBullet param(HttpEntitys entity, String param) {
//        this.entity = entity;
//        this.paramString = param;
//        return this;
//    }

    public HttpBullet param(String param) {
        this.paramString = param;
        return this;
    }

    public String getParamString() {
        return paramString;
    }

    public HttpBullet param(JSONObject param) {
        this.paramJSONObject = param;
        return this;
    }

    public JSONObject getParamJSONObject() {
        return paramJSONObject;
    }

    public HttpBullet param(Map<String, Object> param) {
        this.paramMap = param;
        return this;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public HttpBullet retry(RetryFunction<HttpReport, Integer> retry) {
        this.retry = retry;
        return this;
    }

    public RetryFunction<HttpReport, Integer> getRetry() {
        return retry;
    }

    public HttpBullet failed(FailedFunction<HttpReport> failed) {
        this.failed = failed;
        return this;
    }

    public FailedFunction<HttpReport> getFailed() {
        return failed;
    }

    public HttpBullet disaster(DisasterFunction disaster) {
        this.disaster = disaster;
        return this;
    }

    public HttpBullet inenc(Charset inenc) {
        this.inenc = inenc;
        return this;
    }

    public HttpBullet outenc(Charset outenc) {
        this.outenc = outenc;
        return this;
    }

    public HttpBullet file(Map<String, File> file) {
        this.file = file;
        return this;
    }

    public Map<String, File> getFile() {
        return file;
    }

    public HttpBullet entity(HttpEntitys entity) {
        this.entity = entity;
        return this;
    }

    public HttpEntitys getEntity() {
        return entity;
    }

    public HttpBullet client(CloseableHttpClient client) {
        this.client = client;
        return this;
    }

    public CloseableHttpClient getClient() {
        return client;
    }

    public void httpRequestBase(HttpRequestBase httpRequestBase) {
        this.httpRequestBase = httpRequestBase;
    }

    public HttpRequestBase getHttpRequestBase() {
        return httpRequestBase;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getParam() {
        return param;
    }
}