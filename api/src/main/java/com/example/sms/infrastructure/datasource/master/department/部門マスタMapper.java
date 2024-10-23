package com.example.sms.infrastructure.datasource.master.department;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface 部門マスタMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.部門マスタ
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    int deleteByPrimaryKey(部門マスタKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.部門マスタ
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    int insert(部門マスタ record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.部門マスタ
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    int insertSelective(部門マスタ record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.部門マスタ
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    部門マスタ selectByPrimaryKey(部門マスタKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.部門マスタ
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    int updateByPrimaryKeySelective(部門マスタ record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.部門マスタ
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    int updateByPrimaryKey(部門マスタ record);

    @Delete("DELETE FROM 部門マスタ")
    void deleteAll();

    List<部門マスタ> selectAll();

    List<部門マスタ> selectByDepartmentCode(String departmentCode);
}
