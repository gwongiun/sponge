package com.ly.add.sponge.common.http;

/**
 * @author : qqy48861
 * date : 2019/3/12.
 */
@FunctionalInterface
public interface RetryFunction<R, T> {
    boolean retry(R result, T times);
}