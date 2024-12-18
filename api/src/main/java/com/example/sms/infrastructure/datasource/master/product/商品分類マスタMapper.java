package com.example.sms.infrastructure.datasource.master.product;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface 商品分類マスタMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.商品分類マスタ
     *
     * @mbg.generated Sat Oct 19 18:42:50 JST 2024
     */
    int deleteByPrimaryKey(String 商品分類コード);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.商品分類マスタ
     *
     * @mbg.generated Sat Oct 19 18:42:50 JST 2024
     */
    int insert(商品分類マスタ record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.商品分類マスタ
     *
     * @mbg.generated Sat Oct 19 18:42:50 JST 2024
     */
    int insertSelective(商品分類マスタ record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.商品分類マスタ
     *
     * @mbg.generated Sat Oct 19 18:42:50 JST 2024
     */
    商品分類マスタ selectByPrimaryKey(String 商品分類コード);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.商品分類マスタ
     *
     * @mbg.generated Sat Oct 19 18:42:50 JST 2024
     */
    int updateByPrimaryKeySelective(商品分類マスタ record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.商品分類マスタ
     *
     * @mbg.generated Sat Oct 19 18:42:50 JST 2024
     */
    int updateByPrimaryKey(商品分類マスタ record);

    @Delete("DELETE FROM public.商品分類マスタ")
    void deleteAll();

    List<商品分類マスタ> selectAll();
}
