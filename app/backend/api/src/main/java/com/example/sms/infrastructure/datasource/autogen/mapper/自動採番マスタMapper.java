package com.example.sms.infrastructure.datasource.autogen.mapper;

import com.example.sms.infrastructure.datasource.autogen.model.自動採番マスタ;
import com.example.sms.infrastructure.datasource.autogen.model.自動採番マスタKey;

public interface 自動採番マスタMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.自動採番マスタ
     *
     * @mbg.generated Mon Mar 31 13:42:42 JST 2025
     */
    int deleteByPrimaryKey(自動採番マスタKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.自動採番マスタ
     *
     * @mbg.generated Mon Mar 31 13:42:42 JST 2025
     */
    int insert(自動採番マスタ record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.自動採番マスタ
     *
     * @mbg.generated Mon Mar 31 13:42:42 JST 2025
     */
    int insertSelective(自動採番マスタ record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.自動採番マスタ
     *
     * @mbg.generated Mon Mar 31 13:42:42 JST 2025
     */
    自動採番マスタ selectByPrimaryKey(自動採番マスタKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.自動採番マスタ
     *
     * @mbg.generated Mon Mar 31 13:42:42 JST 2025
     */
    int updateByPrimaryKeySelective(自動採番マスタ record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.自動採番マスタ
     *
     * @mbg.generated Mon Mar 31 13:42:42 JST 2025
     */
    int updateByPrimaryKey(自動採番マスタ record);
}