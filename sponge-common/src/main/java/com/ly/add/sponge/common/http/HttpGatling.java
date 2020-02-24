package com.ly.add.sponge.common.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * @author : qqy48861
 * date : 2019/3/26.
 */
public class HttpGatling {

    private static CloseableHttpClient client;

    static {
        client = HttpGun.create().pool(1000, 100, null).timeout(10000).retry(1).build();
    }

    /* 建议使用 shoot() */
    @Deprecated
    public static HttpReport execute(HttpBullet httpBullet) {
        return process(httpBullet);
    }

    public static HttpReport shoot(HttpBullet httpBullet) {
        return process(httpBullet);
    }

    public static HttpReport get(HttpBullet httpBullet) {
        return process(httpBullet);
    }

    public static HttpReport post(HttpBullet httpBullet) {
        httpBullet.method(HttpMethods.POST);
        return process(httpBullet);
    }

    private static HttpReport process(HttpBullet httpBullet) {
        HttpReport hr = new HttpReport();
        HttpRequestBase httpRequestBase = getRequest(httpBullet.getUrl(), httpBullet.getMethod().getCode());
        if (httpBullet.getHeaders() != null) {
            httpRequestBase.setHeaders(httpBullet.getHeaders());
        }
        if (httpBullet.getClient() == null) {
            httpBullet.client(client);
        }
        setEntity(httpBullet, httpRequestBase);
        httpBullet.httpRequestBase(httpRequestBase);
        long start = System.currentTimeMillis();
        hr = requestCore(httpBullet, hr);
        getJsonResult(hr);
        long end = System.currentTimeMillis();
        hr.setSignConsume(end - start);
        setLog(hr, 0);

        hr = retryHandler(httpBullet, hr);
        failedHandler(httpBullet, hr);
        return hr;
    }

    private static void setEntity(HttpBullet httpBullet, HttpRequestBase httpRequestBase) {
        boolean assignableFrom = HttpEntityEnclosingRequestBase.class.isAssignableFrom(httpRequestBase.getClass());
//        boolean b = ((HttpEntityEnclosingRequestBase) httpRequestBase).expectContinue();
        if (!assignableFrom) {
            String url = httpBullet.getUrl();
            String param = "";
            BODY:
            {
                JSONObject paramJSONObject = httpBullet.getParamJSONObject();
                if (paramJSONObject != null) {
                    param = HttpTools.paramConverter(paramJSONObject, "?", true);
                    httpBullet.url(url + param);
                    break BODY;
                }
                Map<String, Object> paramMap = httpBullet.getParamMap();
                if (paramMap != null) {
                    param = HttpTools.paramConverter(paramMap, "?", true);
                    break BODY;
                }
                String paramString = httpBullet.getParamString();
                if (paramString != null) {
                    param = "?" + paramString;
                }
            }
            httpBullet.setParam(param);
            String finalUrl = url + param;
            httpBullet.url(finalUrl);
            httpRequestBase.setURI(URI.create(finalUrl));
            return;
        }
        HttpEntitys entity = httpBullet.getEntity();

        switch (entity) {
            case STRING:
                String entityString1 = "";
                JSONObject paramJSONObject1 = httpBullet.getParamJSONObject();
                if (paramJSONObject1 != null) {
                    entityString1 = HttpTools.paramConverter(paramJSONObject1, "", false);
                }
                Map<String, Object> paramMap1 = httpBullet.getParamMap();
                if (paramMap1 != null) {
                    entityString1 = HttpTools.paramConverter(paramMap1, "", false);
                }
                String paramString1 = httpBullet.getParamString();
                if (paramString1 != null) {
                    entityString1 = paramString1;
                }
                httpBullet.setParam(entityString1);
                StringEntity stringEntity1 = new StringEntity(entityString1, Consts.UTF_8);

                //设置参数到请求对象中
                ((HttpEntityEnclosingRequestBase) httpRequestBase).setEntity(stringEntity1);
                httpRequestBase.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
                break;
            case JSON:
                String entityString = "";
                JSONObject paramJSONObject = httpBullet.getParamJSONObject();
                if (paramJSONObject != null) {
                    entityString = paramJSONObject.toString();
                }
                Map<String, Object> paramMap = httpBullet.getParamMap();
                if (paramMap != null) {
                    entityString = JSONObject.toJSONString(paramMap);
                }
                String paramString = httpBullet.getParamString();
                if (paramString != null) {
                    entityString = paramString;
                }
                httpBullet.setParam(entityString);
                StringEntity stringEntity = new StringEntity(entityString, Consts.UTF_8);

                //设置参数到请求对象中
                ((HttpEntityEnclosingRequestBase) httpRequestBase).setEntity(stringEntity);
                httpRequestBase.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
                break;
            case FILE:
                MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                        .setCharset(Consts.UTF_8)
                        .setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                JSONObject paramJSONObject3 = httpBullet.getParamJSONObject();
                if (paramJSONObject3 != null) {
                    for (Map.Entry<String, Object> e : paramJSONObject3.entrySet()) {
                        builder.addTextBody(e.getKey(), e.getValue().toString());
                    }
                }
                Map<String, Object> paramMap3 = httpBullet.getParamMap();
                if (paramMap3 != null) {
                    for (Map.Entry<String, Object> e : paramMap3.entrySet()) {
                        builder.addTextBody(e.getKey(), e.getValue().toString());
                    }
                }
                Map<String, File> file = httpBullet.getFile();
                if (file != null) {
                    for (Map.Entry<String, File> e : file.entrySet()) {
                        FileBody fileBody = new FileBody(e.getValue());
                        builder.addPart(e.getKey(), fileBody);
                    }
                }
                HttpEntity fileEntity = builder.build();
                ((HttpEntityEnclosingRequestBase) httpRequestBase).setEntity(fileEntity);
        }
    }

    private static HttpReport retryHandler(HttpBullet httpBullet, HttpReport hr) {
        RetryFunction<HttpReport, Integer> retryFunction = httpBullet.getRetry();
        if (retryFunction != null) {
            Integer ti = 10;
            for (int i = 1; i <= ti; i++) {
                if (retryFunction.retry(hr, i)) {
                    long start = System.currentTimeMillis();
                    hr = requestCore(httpBullet, hr);
                    getJsonResult(hr);
                    long end = System.currentTimeMillis();
                    hr.setSignConsume(end - start);
                    setLog(hr, i);
                } else {
                    break;
                }
            }
        }
        return hr;
    }

    private static void failedHandler(HttpBullet httpBullet, HttpReport httpReport) {
        FailedFunction<HttpReport> failed = httpBullet.getFailed();
        if (failed != null) {
            failed.failed(httpReport);
        }
    }

    /**
     * 核心请求，excute操作在这里
     */
    private static HttpReport requestCore(HttpBullet httpBullet, HttpReport hr) {

        HttpRequestBase httpRequestBase = httpBullet.getHttpRequestBase();
//        RequestLine requestLine = httpRequestBase.getRequestLine();
//        String s = requestLine.toString();
        Header[] reqAllHeaders = httpRequestBase.getAllHeaders();
//        Map<String, String> requestHeaders = new LinkedHashMap<>();
//        HeaderIterator requestHeaderIterator = httpRequestBase.headerIterator();
//        while (requestHeaderIterator.hasNext()) {
//            Header header = requestHeaderIterator.nextHeader();
//            requestHeaders.put(header.getName(), header.getValue());
//        }
        CloseableHttpResponse response;
        int statusCode = 0;
        String result = null;
        Boolean success = true;
        Exception exception = null;
        int signCode = HttpCodeEnum.HTTP_REQUEST_SUCCESS.getCode();
        Header[] resAllHeaders = null;
//        Map<String, String> responseHeaders = new LinkedHashMap<>();
        BODY:
        {
            try {
                response = httpBullet.getClient().execute(httpRequestBase);
            } catch (Exception e) {
                exception = e;
                success = false;
                signCode = HttpCodeEnum.HTTP_REQUEST_ERROR.getCode();
                break BODY;
            }
            statusCode = response.getStatusLine().getStatusCode();
            try {
                result = HttpTools.read(response, Consts.UTF_8);
                resAllHeaders = response.getAllHeaders();
//                HeaderIterator responseHeaderIterator = response.headerIterator();
//                while (responseHeaderIterator.hasNext()) {
//                    Header header = responseHeaderIterator.nextHeader();
//                    responseHeaders.put(header.getName(), header.getValue());
//                }
            } catch (Exception e) {
                exception = e;
                success = false;
                signCode = HttpCodeEnum.HTTP_RESPONSE_ERROR.getCode();
            } finally {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        hr.setReqUrl(httpBullet.getUrl());
        hr.setReqHeaders(reqAllHeaders);
        hr.setReqBody(httpBullet.getParam());
        hr.setResCode(statusCode);
        hr.setResBody(result);
        hr.setResHeaders(resAllHeaders);
        hr.setSignException(exception != null ? exception.getMessage() : null);
        hr.setSignCode(signCode);
        hr.setSignSuccess(success);
        return hr;
    }

    private static void getJsonResult(HttpReport hr) {
        Boolean success = hr.getSignSuccess();
        if (success) {
            String body = hr.getResBody();
            if (body == null) {
                hr.setHttpCodeEnum(HttpCodeEnum.HTTP_RESPONSE_NULL_ERROR);
            } else if (body.equals("")) {
                hr.setHttpCodeEnum(HttpCodeEnum.HTTP_RESPONSE_EMPTY_ERROR);
            } else {
                try {
                    hr.setResJson(JSON.parseObject(body));
                } catch (Exception e) {
                    hr.setHttpCodeEnum(HttpCodeEnum.HTTP_RESPONSE_JSON_ERROR);
                }
            }
        }
    }

    private static void setLog(HttpReport hr, Integer times) {
        hr.setLog(times + "th", hr.toLogObject());
    }

    private static HttpRequestBase getRequest(String url, Integer method) {
        HttpRequestBase request;
        switch (method) {
            case 0:// HttpGet
                request = new HttpGet(url);
                break;
            case 1:// HttpPost
                request = new HttpPost(url);
                break;
            case 2:// HttpHead
                request = new HttpHead(url);
                break;
            case 3:// HttpPut
                request = new HttpPut(url);
                break;
            case 4:// HttpDelete
                request = new HttpDelete(url);
                break;
            case 5:// HttpTrace
                request = new HttpTrace(url);
                break;
            case 6:// HttpPatch
                request = new HttpPatch(url);
                break;
            case 7:// HttpOptions
                request = new HttpOptions(url);
                break;
            default:
                request = new HttpPost(url);
                break;
        }
        return request;
    }



}