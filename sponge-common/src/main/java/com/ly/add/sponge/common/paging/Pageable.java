package com.ly.add.sponge.common.paging;

/**
 * @Author: qy
 * @Date: 2019/5/9.
 */
public class Pageable {

    private Integer draw; //当前查询标识 datatables使用，必传但是不需要修改
    private Integer length; //每页容量
    private Integer start; //起始编号（例：第9页，每页10条的话，start = 90，length = 10）
    private Integer count; //总数量


    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getDraw() {
        return draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }
}