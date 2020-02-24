package com.ly.add.sponge.common.constant;

/**
 * @author meng
 */
public class RspConstants {

    // 项目中的错误，以300开头
    public static final Integer RSP_CODE_ERROR = 300; // 一般错误
    //
    public static final Integer RSP_CODE_TOKEN_ERROR = 403; // token无效或无权限
    public static final Integer RSP_CODE_PARAM_ERROR = 405; // 参数错误
    public static final Integer RSP_CODE_JSON_ERROR = 406; // json错误
    //
    public static final Integer RSP_CODE_SERVER_ERROR = 500; // 服务端异常错误
    // 成功代码
    public static final Integer RSP_CODE_SUCCESS = 200; // 一般成功

}
