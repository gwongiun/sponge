package com.ly.add.sponge.common.tuple;

/**
 * 三元组
 *
 * @author mkk41112
 * @since 2017/5/16
 */
public class Triple<A, B, C> extends Tuple<A, B> {
    public C third;

    public Triple() {
        super();
    }

    public Triple(A a, B b, C c) {
        super(a, b);
        this.third = c;
    }
}
