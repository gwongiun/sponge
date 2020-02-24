package com.ly.add.sponge.es;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * @author : qqy48861
 * date : 2018/11/28.
 */
public class Main {

    public static void main(String[] args) {
        ESHighLevelRestClient client = new ESHighLevelRestClient("es.qa.17usoft.com", "test", "", "");
//        ESHighLevelRestClient client = new ESHighLevelRestClient("es.qa.17usoft.com", "test", "userName", "password");
        // 判断索引是否存在
        System.out.println("索引是否存在->" + client.indexExists("xiaoma_test"));
        // json查询总记录数
        String query ="{\"query\":{\"bool\":{\"must\":[{\"match_all\":{}}]}},\"from\":0,\"size\":10}";
        System.out.println("总记录数->" + client.queryCount("xiaoma_test", "xiaoma_test", query));
        // 构建查询器查询数据
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(2);
        String[] fields = {"openid"};
        sourceBuilder.fetchSource(fields, new String[]{});
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("isdel", "1");
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("cdate");
        rangeQueryBuilder.gte("2012-01-01");
        rangeQueryBuilder.lte("2018-01-26");
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        boolBuilder.must(termQueryBuilder);
        boolBuilder.must(rangeQueryBuilder);
        sourceBuilder.query(boolBuilder);
        SearchResponse response = client.getResponse(sourceBuilder, "wechat_member_airticket", "wechat_member_airticket");
        System.out.println("查询器查询数据->" + response);
        // 通过主键查询
        String s = client.getResponseById("104", "wechat_member_airticket", "wechat_member_airticket");
        System.out.println("主键查询数据->" + s);
        client.close();
    }

}