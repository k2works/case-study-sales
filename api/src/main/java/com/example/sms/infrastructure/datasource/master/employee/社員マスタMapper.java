package com.example.sms.infrastructure.datasource.master.employee;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface 社員マスタMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.社員マスタ
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    int deleteByPrimaryKey(String 社員コード);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.社員マスタ
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    int insert(社員マスタ record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.社員マスタ
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    int insertSelective(社員マスタ record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.社員マスタ
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    社員マスタ selectByPrimaryKey(String 社員コード);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.社員マスタ
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    int updateByPrimaryKeySelective(社員マスタ record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.社員マスタ
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    int updateByPrimaryKey(社員マスタ record);

    List<社員マスタ> selectAll();

    @Delete("DELETE FROM 社員マスタ")
    void deleteAll();
}
