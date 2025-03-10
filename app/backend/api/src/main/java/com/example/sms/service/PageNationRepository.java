package com.example.sms.service;

import com.github.pagehelper.PageInfo;

import java.util.function.Function;

public interface PageNationRepository {
    <T, R> PageInfo<R> getPageInfo(PageInfo<T> sourcePageInfo, Function<T, R> mapper);
}
