package com.ly.add.sponge.common.paging;

import java.util.List;

/**
 * @Author: qy
 * @Date: 2019/5/9.
 */
public class Paging<T> {

    private List<T> data;
    private int draw;
    private int recordsTotal;
    private int recordsFiltered;
    private String error;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(int recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public int getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(int recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Paging() {
    }

    public Paging(List<T> data, Pageable pageable) {
        this.data = data;
        this.draw = pageable.getDraw();
        this.recordsTotal = pageable.getCount();
        this.recordsFiltered = pageable.getCount();
        this.error = error;
    }

    public Paging(List<T> data) {
        this.data = data;
    }
    //    private int totalPages;     // 总页数
//    private int totalElements;  // count总数
//    private List<T> data;
//    private int page;  // 外显，originPage + 1
//    private int originPage; //从0开始的那个
//    int size;  //每页数量
//    int numberOfElements;
//    boolean hasContent;
//    boolean isFirst;
//    boolean isLast;
//    boolean hasNext;
//    boolean hasPrevious;
//
//    private int index;
//
//    public Paging(List<T> data, Pageable pageable) {
//        this.data = data;
//        this.totalElements = pageable.getCount();
//        this.originPage = pageable.getPage();
//        this.size = pageable.getSize();
//    }
//
//    private int getCurrentTotal() {
//        return index + size;
//    }
//
//    public List<T> getContent() {
//        return data;
//    }
//
///*    public void setContent(List<T> content) {
//        this.content = content;
//    }*/
//
//    public boolean isHasContent() {
//        return data.size() > 0;
//    }
//
//    public boolean isHasNext() {
//        return getCurrentTotal() < totalElements;
//    }
//
//    public boolean isHasPrevious() {
//        return getCurrentTotal() > size;
//    }
//
//    public boolean isFirst() {
//        return index == 0;
//    }
//
//    public boolean isLast() {
//        return getCurrentTotal() >= totalElements;
//    }
//
//    public int getPage() {
//        return originPage + 1;
//    }
//
//    public int getOriginPage() {
//        return originPage;
//    }
//
///*    public void setOriginPage(int originPage) {
//        this.originPage = originPage;
//    }*/
//
//    public int getNumberOfElements() {
//        return data.size();
//    }
//
//    public int getSize() {
//        return size;
//    }
//
////    public void setSize(int size) {
////        this.size = size;
////    }
//
//    public int getTotalElements() {
//        return totalElements;
//    }
//
///*    public void setTotalElements(int totalElements) {
//        this.totalElements = totalElements;
//    }*/
//
//    public int getTotalPages() {
//        int totalPages = totalElements / size;
//        if (totalElements % size > 0) {
//            totalPages++;
//        }
//        return totalPages;
//    }
}