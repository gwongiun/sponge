package com.ly.add.sponge.common.tuple;

/**
 * @author : qqy48861
 * date : 2018/6/15.
 */
public class SixTuple<A, B, C, D, E, F> extends FiveTuple<A, B, C, D, E> {

    public F sixth;

    public SixTuple(A a, B b, C c, D d, E e, F f) {
        super(a, b, c, d, e);
        sixth = f;
    }

    public String toString() {
        return "(" + first + "," + second + "," + third + "," + fourth + "," + fifth + "," + sixth + ")";
    }

}