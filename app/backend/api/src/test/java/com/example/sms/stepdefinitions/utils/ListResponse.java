package com.example.sms.stepdefinitions.utils;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class ListResponse<T> implements Serializable {
    private Integer total;
    @JsonProperty
    private List<T> list;

    private Integer pageNum;
    private Integer pageSize;
    private Integer size;
    private Integer startRow;
    private Integer endRow;
    private Integer pages;
    private Integer prePage;
    private Integer nextPage;
    @JsonAlias("isFirstPage")
    private Boolean firstPage;
    @JsonAlias("isLastPage")
    private Boolean lastPage;
    @JsonAlias("hasPreviousPage")
    private Boolean hasPreviousPage;
    @JsonAlias("hasNextPage")
    private Boolean hasNextPage;
    private Integer navigatePages;
    private List<Integer> navigatepageNums;
    private Integer navigateFirstPage;
    private Integer navigateLastPage;
}
