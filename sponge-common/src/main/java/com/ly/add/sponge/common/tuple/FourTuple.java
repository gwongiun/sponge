package com.ly.add.sponge.common.tuple;

/**
 * @author : qqy48861
 * date : 2018/6/10.
 */
public class FourTuple<A, B, C, D> extends ThreeTuple<A, B, C> {

    public D fourth;

    public FourTuple(A a, B b, C c, D d) {
        super(a, b, c);
        fourth = d;
    }

    public String toString() {
        return "(" + first + "," + second + "," + third + "," + fourth + ")";
    }

}