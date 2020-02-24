package com.ly.add.sponge.common.http;

/**
 * @author : qqy48861
 * date : 2019/3/27.
 */
public enum HttpEntitys {

    STRING(1, "string"),
    JSON(2, "json"),
    FILE(3, "file"),
    BYTES(4, "bytes"),
    MULTIPART(5, "multipart");

    private int code;
    private String name;

    HttpEntitys(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
