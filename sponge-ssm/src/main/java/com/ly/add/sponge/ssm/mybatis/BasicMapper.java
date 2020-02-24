package com.ly.add.sponge.ssm.mybatis;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author : qqy48861
 * date : 2018/7/25.
 */
public interface BasicMapper<T> extends Mapper<T>, MySqlMapper<T> {

    //TODO
    //FIXME 特别注意，该接口不能被扫描到，否则会出错
}
