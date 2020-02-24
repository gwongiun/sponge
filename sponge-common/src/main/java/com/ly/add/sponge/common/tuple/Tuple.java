package com.ly.add.sponge.common.tuple;

/**
 * 二元组
 *
 * @author mkk41112
 * @since 2017/5/16
 */
public class Tuple<A, B> {
    public A first;
    public B second;

    public Tuple() {
    }

    public Tuple(A a, B b) {
        this.first = a;
        this.second = b;
    }
}
