package com.ly.add.sponge.common.tuple;

/**
 * @author : qqy48861
 * date : 2018/6/10.
 */
public class ThreeTuple<A, B, C> extends TwoTuple<A, B> {

    public C third;

    public ThreeTuple(A a, B b, C c) {
        super(a, b);
        third = c;
    }

    public String toString() {
        return "(" + first + "," + second + "," + third + ")";
    }

}