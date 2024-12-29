package com.example.sms.presentation;

import com.github.pagehelper.PageHelper;

public class PageNation {
    public static final int PAGE_SIZE = 10;
    Integer page;
    Integer startPage;
    Integer endPage;
    Long totalPage;

    public PageNation(int page, int startPage, int endPage, long totalPage) {
        this.page = page;
        this.startPage = startPage;
        this.endPage = endPage;
        this.totalPage = totalPage;
    }

    public static void startPage(int[] page, int... pageSize) {
        int startPage = page != null && page.length > 0 ? page[0] : 1;
        int pageSizeValue = pageSize != null && pageSize.length > 0 ? pageSize[0] : PAGE_SIZE;
        PageHelper.startPage(startPage, pageSizeValue);
    }

    public Integer Page() {
        return page;
    }

    public Integer StartPage() {
        return startPage;
    }

    public Integer EndPage() {
        return endPage;
    }

    public Long TotalPage() {
        return totalPage;
    }

    public Integer PageSize() {
        return PAGE_SIZE;
    }
}
