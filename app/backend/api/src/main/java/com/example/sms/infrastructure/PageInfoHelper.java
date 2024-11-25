package com.example.sms.infrastructure;

import com.github.pagehelper.PageInfo;

import java.util.function.Function;

public class PageInfoHelper {

    public static <T, R> PageInfo<R> of(PageInfo<T> sourcePageInfo, Function<T, R> mapper) {
        PageInfo<R> resultPageInfo = new PageInfo<>(sourcePageInfo.getList().stream().map(mapper).toList());

        resultPageInfo.setTotal(sourcePageInfo.getTotal());
        resultPageInfo.setPageNum(sourcePageInfo.getPageNum());
        resultPageInfo.setPageSize(sourcePageInfo.getPageSize());
        resultPageInfo.setPages(sourcePageInfo.getPages());
        resultPageInfo.setHasNextPage(sourcePageInfo.isHasNextPage());
        resultPageInfo.setHasPreviousPage(sourcePageInfo.isHasPreviousPage());
        resultPageInfo.setIsFirstPage(sourcePageInfo.isIsFirstPage());
        resultPageInfo.setIsLastPage(sourcePageInfo.isIsLastPage());
        resultPageInfo.setNavigateFirstPage(sourcePageInfo.getNavigateFirstPage());
        resultPageInfo.setNavigateLastPage(sourcePageInfo.getNavigateLastPage());
        resultPageInfo.setNavigatePages(sourcePageInfo.getNavigatePages());
        resultPageInfo.setNavigatepageNums(sourcePageInfo.getNavigatepageNums());
        resultPageInfo.setNextPage(sourcePageInfo.getNextPage());
        resultPageInfo.setPrePage(sourcePageInfo.getPrePage());
        resultPageInfo.setSize(sourcePageInfo.getSize());
        resultPageInfo.setStartRow(sourcePageInfo.getStartRow());
        resultPageInfo.setEndRow(sourcePageInfo.getEndRow());

        return resultPageInfo;
    }
}
