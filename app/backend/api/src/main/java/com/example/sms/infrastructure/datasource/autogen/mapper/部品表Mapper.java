package com.example.sms.infrastructure.datasource.autogen.mapper;

import com.example.sms.infrastructure.datasource.autogen.model.部品表;
import com.example.sms.infrastructure.datasource.autogen.model.部品表Key;

public interface 部品表Mapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.部品表
     *
     * @mbg.generated Fri Dec 27 09:59:07 JST 2024
     */
    int deleteByPrimaryKey(部品表Key key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.部品表
     *
     * @mbg.generated Fri Dec 27 09:59:07 JST 2024
     */
    int insert(部品表 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.部品表
     *
     * @mbg.generated Fri Dec 27 09:59:07 JST 2024
     */
    int insertSelective(部品表 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.部品表
     *
     * @mbg.generated Fri Dec 27 09:59:07 JST 2024
     */
    部品表 selectByPrimaryKey(部品表Key key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.部品表
     *
     * @mbg.generated Fri Dec 27 09:59:07 JST 2024
     */
    int updateByPrimaryKeySelective(部品表 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.部品表
     *
     * @mbg.generated Fri Dec 27 09:59:07 JST 2024
     */
    int updateByPrimaryKey(部品表 record);
}