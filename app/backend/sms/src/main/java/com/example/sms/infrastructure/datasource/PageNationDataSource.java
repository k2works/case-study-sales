package com.example.sms.infrastructure.datasource;

import com.example.sms.infrastructure.PageInfoHelper;
import com.example.sms.service.PageNationRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Repository;

import java.util.function.Function;

@Repository
public class PageNationDataSource implements PageNationRepository {
    @Override
    public <T, R> PageInfo<R> getPageInfo(PageInfo<T> sourcePageInfo, Function<T, R> mapper) {
        return PageInfoHelper.of(sourcePageInfo, mapper);
    }
}
