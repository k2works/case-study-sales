package com.example.sms.infrastructure.datasource.master.product;

public interface 商品マスタMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.商品マスタ
     *
     * @mbg.generated Sat Oct 19 18:42:50 JST 2024
     */
    int deleteByPrimaryKey(String 商品コード);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.商品マスタ
     *
     * @mbg.generated Sat Oct 19 18:42:50 JST 2024
     */
    int insert(商品マスタ record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.商品マスタ
     *
     * @mbg.generated Sat Oct 19 18:42:50 JST 2024
     */
    int insertSelective(商品マスタ record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.商品マスタ
     *
     * @mbg.generated Sat Oct 19 18:42:50 JST 2024
     */
    商品マスタ selectByPrimaryKey(String 商品コード);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.商品マスタ
     *
     * @mbg.generated Sat Oct 19 18:42:50 JST 2024
     */
    int updateByPrimaryKeySelective(商品マスタ record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.商品マスタ
     *
     * @mbg.generated Sat Oct 19 18:42:50 JST 2024
     */
    int updateByPrimaryKey(商品マスタ record);
}
