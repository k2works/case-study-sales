package com.example.sms.infrastructure.datasource.autogen.mapper;

import com.example.sms.infrastructure.datasource.autogen.model.取引先マスタ;

public interface 取引先マスタMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.取引先マスタ
     *
     * @mbg.generated Sat Jan 11 12:54:37 JST 2025
     */
    int deleteByPrimaryKey(String 取引先コード);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.取引先マスタ
     *
     * @mbg.generated Sat Jan 11 12:54:37 JST 2025
     */
    int insert(取引先マスタ record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.取引先マスタ
     *
     * @mbg.generated Sat Jan 11 12:54:37 JST 2025
     */
    int insertSelective(取引先マスタ record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.取引先マスタ
     *
     * @mbg.generated Sat Jan 11 12:54:37 JST 2025
     */
    取引先マスタ selectByPrimaryKey(String 取引先コード);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.取引先マスタ
     *
     * @mbg.generated Sat Jan 11 12:54:37 JST 2025
     */
    int updateByPrimaryKeySelective(取引先マスタ record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.取引先マスタ
     *
     * @mbg.generated Sat Jan 11 12:54:37 JST 2025
     */
    int updateByPrimaryKey(取引先マスタ record);
}