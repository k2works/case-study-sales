package com.example.sms.infrastructure.datasource.autogen.mapper;

import com.example.sms.infrastructure.datasource.autogen.model.受注データ;

public interface 受注データMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.受注データ
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    int deleteByPrimaryKey(String 受注番号);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.受注データ
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    int insert(受注データ record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.受注データ
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    int insertSelective(受注データ record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.受注データ
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    受注データ selectByPrimaryKey(String 受注番号);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.受注データ
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    int updateByPrimaryKeySelective(受注データ record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.受注データ
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    int updateByPrimaryKey(受注データ record);
}