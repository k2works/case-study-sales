package com.example.sms.infrastructure.datasource.autogen.mapper;

import com.example.sms.infrastructure.datasource.autogen.model.Usr;

public interface UsrMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system.usr
     *
     * @mbg.generated Fri Dec 27 09:59:07 JST 2024
     */
    int deleteByPrimaryKey(String userId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system.usr
     *
     * @mbg.generated Fri Dec 27 09:59:07 JST 2024
     */
    int insert(Usr record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system.usr
     *
     * @mbg.generated Fri Dec 27 09:59:07 JST 2024
     */
    int insertSelective(Usr record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system.usr
     *
     * @mbg.generated Fri Dec 27 09:59:07 JST 2024
     */
    Usr selectByPrimaryKey(String userId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system.usr
     *
     * @mbg.generated Fri Dec 27 09:59:07 JST 2024
     */
    int updateByPrimaryKeySelective(Usr record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table system.usr
     *
     * @mbg.generated Fri Dec 27 09:59:07 JST 2024
     */
    int updateByPrimaryKey(Usr record);
}