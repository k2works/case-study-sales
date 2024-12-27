package com.example.sms.infrastructure.datasource.autogen.mapper;

import com.example.sms.infrastructure.datasource.autogen.model.部門マスタ;
import com.example.sms.infrastructure.datasource.autogen.model.部門マスタKey;

public interface 部門マスタMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.部門マスタ
     *
     * @mbg.generated Fri Dec 27 09:59:07 JST 2024
     */
    int deleteByPrimaryKey(部門マスタKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.部門マスタ
     *
     * @mbg.generated Fri Dec 27 09:59:07 JST 2024
     */
    int insert(部門マスタ record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.部門マスタ
     *
     * @mbg.generated Fri Dec 27 09:59:07 JST 2024
     */
    int insertSelective(部門マスタ record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.部門マスタ
     *
     * @mbg.generated Fri Dec 27 09:59:07 JST 2024
     */
    部門マスタ selectByPrimaryKey(部門マスタKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.部門マスタ
     *
     * @mbg.generated Fri Dec 27 09:59:07 JST 2024
     */
    int updateByPrimaryKeySelective(部門マスタ record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.部門マスタ
     *
     * @mbg.generated Fri Dec 27 09:59:07 JST 2024
     */
    int updateByPrimaryKey(部門マスタ record);
}