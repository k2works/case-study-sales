package com.example.sms.service;

import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PageNationService {
    final PageNationRepository pageNationRepository;

    public PageNationService(PageNationRepository pageNationRepository) {
        this.pageNationRepository = pageNationRepository;
    }

    public <T, R> PageInfo<R> getPageInfo(PageInfo<T> sourcePageInfo, Function<T, R> mapper) {
        return pageNationRepository.getPageInfo(sourcePageInfo, mapper);
    }
}
