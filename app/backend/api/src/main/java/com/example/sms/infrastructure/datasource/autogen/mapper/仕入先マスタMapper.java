package com.example.sms.infrastructure.datasource.autogen.mapper;

import com.example.sms.infrastructure.datasource.autogen.model.仕入先マスタ;
import com.example.sms.infrastructure.datasource.autogen.model.仕入先マスタKey;

public interface 仕入先マスタMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.仕入先マスタ
     *
     * @mbg.generated Tue Jan 07 18:37:28 JST 2025
     */
    int deleteByPrimaryKey(仕入先マスタKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.仕入先マスタ
     *
     * @mbg.generated Tue Jan 07 18:37:28 JST 2025
     */
    int insert(仕入先マスタ record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.仕入先マスタ
     *
     * @mbg.generated Tue Jan 07 18:37:28 JST 2025
     */
    int insertSelective(仕入先マスタ record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.仕入先マスタ
     *
     * @mbg.generated Tue Jan 07 18:37:28 JST 2025
     */
    仕入先マスタ selectByPrimaryKey(仕入先マスタKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.仕入先マスタ
     *
     * @mbg.generated Tue Jan 07 18:37:28 JST 2025
     */
    int updateByPrimaryKeySelective(仕入先マスタ record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.仕入先マスタ
     *
     * @mbg.generated Tue Jan 07 18:37:28 JST 2025
     */
    int updateByPrimaryKey(仕入先マスタ record);
}