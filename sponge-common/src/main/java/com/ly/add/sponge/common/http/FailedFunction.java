package com.ly.add.sponge.common.http;

/**
 * @author : qqy48861
 * date : 2019/4/16.
 */
@FunctionalInterface
public interface FailedFunction<R> {
    void failed(R result);
}