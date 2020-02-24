package com.ly.add.sponge.common.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;

import java.util.Arrays;

/**
 * @author : qqy48861
 * date : 2019/4/3.
 */
public class HttpReport {

    private String reqUrl;

    private Header[] reqHeaders;

    private String reqBody;

    private Integer resCode;

    private Header[] resHeaders;

    private String resBody;

    private JSONObject resJson = new JSONObject();

    private String signException;

    private Integer signCode;

    private Boolean signSuccess;

    private Long signConsume;

    private JSONObject log = new JSONObject();

    public HttpReport() {
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }

    public Header[] getReqHeaders() {
        return reqHeaders;
    }

    public void setReqHeaders(Header[] reqHeaders) {
        this.reqHeaders = reqHeaders;
    }

    public String getReqBody() {
        return reqBody;
    }

    public void setReqBody(String reqBody) {
        this.reqBody = reqBody;
    }

    public Integer getResCode() {
        return resCode;
    }

    public void setResCode(Integer resCode) {
        this.resCode = resCode;
    }

    public Header[] getResHeaders() {
        return resHeaders;
    }

    public void setResHeaders(Header[] resHeaders) {
        this.resHeaders = resHeaders;
    }

    public String getResBody() {
        return resBody;
    }

    public void setResBody(String resBody) {
        this.resBody = resBody;
    }

    public JSONObject getResJson() {
        return resJson;
    }

    public void setResJson(JSONObject resJson) {
        this.resJson = resJson;
    }

    public String getSignException() {
        return signException;
    }

    public void setSignException(String signException) {
        this.signException = signException;
    }

    public Integer getSignCode() {
        return signCode;
    }

    public void setSignCode(Integer signCode) {
        this.signCode = signCode;
    }

    public Boolean getSignSuccess() {
        return signSuccess;
    }

    public void setSignSuccess(Boolean signSuccess) {
        this.signSuccess = signSuccess;
    }

    public Long getSignConsume() {
        return signConsume;
    }

    public void setSignConsume(Long signConsume) {
        this.signConsume = signConsume;
    }

    public void setLog(JSONObject log) {
        this.log = log;
    }

    public JSONObject getLog() {
        return log;
    }

    public void setLog(String key, JSONObject value) {
        this.log.put(key, value);
    }

    public void setHttpCodeEnum(HttpCodeEnum httpCodeEnum){
        this.signException = httpCodeEnum.getMessage();
        this.signCode = httpCodeEnum.getCode();
        this.signSuccess = httpCodeEnum.getSuccess();
    }

    @Override
    public String toString() {
        return "reqUrl  : " + reqUrl + "\n"
                + "reqHead : " + Arrays.toString(reqHeaders) + "\n"
                + "reqBody : " + reqBody + "\n"
                + "resCode : " + resCode + "\n"
                + "resHead : " + Arrays.toString(resHeaders) + "\n"
                + "resBody : " + resBody + "\n"
                + "resJson : " + resJson.toString() + "\n"
                + "signCode: " + signCode + "\n"
                + "signSucc: " + signSuccess + "\n"
                + "signCons: " + signConsume + "\n"
                + "signExce: " + signException;
    }

    public JSONObject toLogObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("resCode", resCode);
        jsonObject.put("resHeaders", Arrays.toString(resHeaders));
        jsonObject.put("resBody", resBody);
        jsonObject.put("signCode", signCode);
        jsonObject.put("signSuccess", signSuccess);
        jsonObject.put("signExcp", signException);
        jsonObject.put("signConsume", signConsume);
        return jsonObject;
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("reqUrl", reqUrl);
        jsonObject.put("reqHeaders", Arrays.toString(reqHeaders));
        jsonObject.put("reqBody", reqBody);
        jsonObject.put("resCode", resCode);
        jsonObject.put("resHeaders", Arrays.toString(resHeaders));
        jsonObject.put("resBody", resBody);
        jsonObject.put("resJson", resJson);
        jsonObject.put("signCode", signCode);
        jsonObject.put("signSuccess", signSuccess);
        jsonObject.put("signExcp", signException);
        jsonObject.put("signConsume", signConsume);
        jsonObject.put("log", log);
        return jsonObject;
    }
}