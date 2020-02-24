package com.ly.add.sponge.common.tuple;

/**
 * @author : qqy48861
 * date : 2018/6/10.
 */
public class TwoTuple<A, B> {

    public A first;
    public B second;

    public TwoTuple(A a, B b) {
        first = a;
        second = b;
    }

    public String toString() {
        return "(" + first + ", " + second + ")";
    }

}