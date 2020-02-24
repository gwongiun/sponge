package com.ly.add.sponge.es;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ly.add.sponge.common.exception.BusiException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static jdk.nashorn.internal.runtime.PropertyDescriptor.GET;

//import com.ly.add.sponge.tcel.log.LogFactory;

public class ESHighLevelRestClient {


    private static final int TIME_OUT = 1000;

    private static final int MAX_CONN_PER_ROUTE = 30; //每个路由的最大连接

    private String clusterName;

    private String hostName;

    private RestHighLevelClient client;

    /**
     * 通过域名+集群创建链接
     * */
    public ESHighLevelRestClient(String host, String cluster) {
        this.clusterName = cluster;
        this.hostName = host;
        client = new RestHighLevelClient(
                RestClient.builder(new HttpHost(hostName))
                    .setRequestConfigCallback(
                        requestConfigBuilder -> requestConfigBuilder.setConnectTimeout(TIME_OUT).setSocketTimeout(TIME_OUT).setConnectionRequestTimeout(TIME_OUT))
                    .setHttpClientConfigCallback(
                        httpAsyncClientBuilder -> httpAsyncClientBuilder.setMaxConnPerRoute(MAX_CONN_PER_ROUTE).setMaxConnTotal(MAX_CONN_PER_ROUTE))
                    .setPathPrefix(clusterName));
    }

    /**
     * 通过域名+集群+用户名密码创建链接
     * */
    public ESHighLevelRestClient(String host, String cluster, String user, String password) {
        this.clusterName = cluster;
        this.hostName = host;
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, password));
        client = new RestHighLevelClient(
                RestClient.builder(new HttpHost(hostName))
                        .setRequestConfigCallback(
                            requestConfigBuilder -> requestConfigBuilder.setConnectTimeout(TIME_OUT).setSocketTimeout(TIME_OUT).setConnectionRequestTimeout(TIME_OUT))
                        .setHttpClientConfigCallback(
                            httpAsyncClientBuilder -> httpAsyncClientBuilder.setMaxConnPerRoute(MAX_CONN_PER_ROUTE).setMaxConnTotal(MAX_CONN_PER_ROUTE).setDefaultCredentialsProvider(credentialsProvider))
                        .setPathPrefix(clusterName));
    }

    /**
     * 验证索引是否存在
     *
     * @param index 索引名称
     * @return boolean
     * @throws IOException
     */
    public boolean indexExists(String index) {
        try {
            GetIndexRequest request = new GetIndexRequest();
            request.indices(index);
            request.local(false);
            request.humanReadable(true);
            return client.indices().exists(request);
        } catch (IOException e) {
//            LogFactory.start()
//                    .withMarker("验证索引是否存在", "异常")
//                    .withMessage("cluster={}，index={}", clusterName, index)
//                    .withException(e)
//                    .withExtraInfo("查询ES异常:{}", ExceptionUtil.getErrorInfoFromException(e))
//                    .error();
           throw new BusiException("验证索引是否存在异常");
        }
    }

    /**
     * 查询总记录数
     *
     * @param index 索引名称
     * @param type 类型
     * @return Long
     * @throws IOException
     */
    public Long queryCount(String index, String type, String query) {
        long count = 0L;
        String endpoint = "/" + index + "/" + type + "/_count";
        try {
            HttpEntity entity = new NStringEntity(query, ContentType.APPLICATION_JSON);
            Response response = client.getLowLevelClient().performRequest(GET, endpoint, Collections.emptyMap(), entity);
            String result = EntityUtils.toString(response.getEntity());
            if (null != result) {
                JSONObject resultObject = JSONObject.parseObject(result);
                count = Long.parseLong(resultObject.getString("count"));
            }
        } catch (IOException e) {
//            LogFactory.start()
//                    .withMarker("查询总记录数", "异常")
//                    .withMessage("cluster={}，index={}", clusterName, index)
//                    .withException(e)
//                    .withExtraInfo("查询ES异常:{}", ExceptionUtil.getErrorInfoFromException(e))
//                    .error();
            throw new BusiException("查询总记录数异常");
        }
        return count;
    }

    /**
     * 查询记录列表
     *
     * @param index 索引名称
     * @param types 类型
     * @param sourceBuilder 查询构建器
     * @param clz 返回list的实体
     * @return List
     * @throws IOException
     */
    public <T> List<T> handleResultToList(SearchSourceBuilder sourceBuilder, Class<T> clz, String index, String... types){
        List<T> list = new ArrayList<>();
        try {
            SearchRequest searchRequest = new SearchRequest(index);
            searchRequest.types(types);
            searchRequest.source(sourceBuilder);
            SearchResponse response = client.search(searchRequest);
            execute(response, list, clz);
        } catch (IOException e) {
//            LogFactory.start()
//                    .withMarker("查询数据列表", "异常")
//                    .withMessage("cluster={}，index={}，builder:{}", clusterName, index, sourceBuilder.toString())
//                    .withException(e)
//                    .withExtraInfo("查询ES异常:{}", ExceptionUtil.getErrorInfoFromException(e))
//                    .error();
        }
        return list;
    }

    public <T> List<T> handleResultToList(SearchRequest searchRequest, Class<T> clz){
        List<T> list = new ArrayList<>();
        try {
            SearchResponse response = client.search(searchRequest);
            execute(response, list, clz);
        } catch (IOException e) {
//            LogFactory.start()
//                    .withMarker("查询数据列表", "异常")
//                    .withMessage("cluster={}，request:{}", clusterName, searchRequest.toString())
//                    .withException(e)
//                    .withExtraInfo("查询ES异常:{}", ExceptionUtil.getErrorInfoFromException(e))
//                    .error();
        }
        return list;
    }

    /**
     * 查询SearchResponse
     *
     * @param index 索引名称
     * @param types 类型
     * @param sourceBuilder 查询构建器
     * @return SearchResponse
     * @throws IOException
     */
    public SearchResponse getResponse(SearchSourceBuilder sourceBuilder, String index, String... types){
        SearchResponse searchResponse = null;
        try {
            SearchRequest searchRequest = new SearchRequest(index);
            searchRequest.types(types);
            searchRequest.source(sourceBuilder);
            searchResponse = client.search(searchRequest);
        } catch (IOException e) {
//            LogFactory.start()
//                    .withMarker("查询数据", "异常")
//                    .withMessage("cluster={}，index={}", clusterName, index)
//                    .withException(e)
//                    .withExtraInfo("查询ES异常:{}", ExceptionUtil.getErrorInfoFromException(e))
//                    .error();
        }
        return searchResponse;
    }

    /**
     * 查询SearchResponse
     *
     * @param searchRequest 查询请求
     * @return SearchResponse
     * @throws IOException
     */
    public SearchResponse getResponse(SearchRequest searchRequest){
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest);
        } catch (IOException e) {
//            LogFactory.start()
//                    .withMarker("查询数据", "异常")
//                    .withMessage("cluster={}，request={}", clusterName, searchRequest.toString())
//                    .withException(e)
//                    .withExtraInfo("查询ES异常:{}", ExceptionUtil.getErrorInfoFromException(e))
//                    .error();
        }
        return searchResponse;
    }

    /**
     * Scroll查询SearchResponse
     *
     * @param scrollRequest 查询请求
     * @return SearchResponse
     * @throws IOException
     */
    public SearchResponse getResponseByScroll(SearchScrollRequest scrollRequest){
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.searchScroll(scrollRequest);
        } catch (IOException e) {
//            LogFactory.start()
//                    .withMarker("查询数据", "异常")
//                    .withMessage("cluster={}，scrollRequest={}", clusterName, scrollRequest.toString())
//                    .withException(e)
//                    .withExtraInfo("查询ES异常:{}", ExceptionUtil.getErrorInfoFromException(e))
//                    .error();
        }
        return searchResponse;
    }

    /**
     * 清除快照
     *
     * @param scrollId
     * @return ClearScrollResponse
     * @throws IOException
     */
    public ClearScrollResponse clearScroll(String scrollId){
        ClearScrollResponse clearScrollResponse = null;
        try {
            ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
            clearScrollRequest.addScrollId(scrollId);
            clearScrollResponse = client.clearScroll(clearScrollRequest);
        } catch (IOException e) {
//            LogFactory.start()
//                    .withMarker("清除Scroll", "异常")
//                    .withMessage("cluster={}，ScrollId={}", clusterName, scrollId)
//                    .withException(e)
//                    .withExtraInfo("查询ES异常:{}", ExceptionUtil.getErrorInfoFromException(e))
//                    .error();
        }
        return clearScrollResponse;
    }

    /**
     * 通过主键查询记录
     *
     * @param index 索引名称
     * @param type 类型
     * @param id 主键
     * @return String
     * @throws IOException
     */
    public String getResponseById(String id, String index, String type){
        String result = null;
        String endPoint = "/" + index + "/" + type + "/" + id + "/_source";
        try {
            result = EntityUtils.toString(client.getLowLevelClient().performRequest("GET", endPoint, Collections.emptyMap()).getEntity());
        } catch (IOException e) {
//            LogFactory.start()
//                    .withMarker("通过主键查询数据", "异常")
//                    .withMessage("cluster={}，index={}", clusterName, index)
//                    .withException(e)
//                    .withExtraInfo("查询ES异常:{}", ExceptionUtil.getErrorInfoFromException(e))
//                    .error();
        }
        return result;
    }


    /**
     * 响应response处理
     */
    private <T> void execute(SearchResponse response, List<T> list, Class<T> clz) {
        if(null != response){
            SearchHit[] hits = response.getHits().getHits();
            if (hits != null) {
                for (SearchHit hit : hits) {
                    T t = JSON.parseObject(hit.getSourceAsString(), clz);
                    list.add(t);
                }
            }
        }
    }

    /**
     * 关闭
     */
    public void close() {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e){
//                LogFactory.start()
//                        .withMarker("客户端关闭", "异常")
//                        .withMessage("cluster={}", clusterName)
//                        .withException(e)
//                        .withExtraInfo("查询ES异常:{}", ExceptionUtil.getErrorInfoFromException(e))
//                        .error();
                throw new BusiException("客户端关闭异常");
            }
        }
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
}
