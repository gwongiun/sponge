package com.ly.add.sponge.common.tuple;

/**
 * @author : qqy48861
 * date : 2018/6/10.
 */
public class FiveTuple<A, B, C, D, E> extends FourTuple<A, B, C, D> {

    public E fifth;

    public FiveTuple(A a, B b, C c, D d, E e) {
        super(a, b, c, d);
        fifth = e;
    }

    public String toString() {
        return "(" + first + "," + second + "," + third + "," + fourth + "," + fifth + ")";
    }

}