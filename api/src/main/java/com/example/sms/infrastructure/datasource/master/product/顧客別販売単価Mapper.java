package com.example.sms.infrastructure.datasource.master.product;

public interface 顧客別販売単価Mapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.顧客別販売単価
     *
     * @mbg.generated Sat Oct 19 18:42:50 JST 2024
     */
    int deleteByPrimaryKey(顧客別販売単価Key key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.顧客別販売単価
     *
     * @mbg.generated Sat Oct 19 18:42:50 JST 2024
     */
    int insert(顧客別販売単価 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.顧客別販売単価
     *
     * @mbg.generated Sat Oct 19 18:42:50 JST 2024
     */
    int insertSelective(顧客別販売単価 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.顧客別販売単価
     *
     * @mbg.generated Sat Oct 19 18:42:50 JST 2024
     */
    顧客別販売単価 selectByPrimaryKey(顧客別販売単価Key key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.顧客別販売単価
     *
     * @mbg.generated Sat Oct 19 18:42:50 JST 2024
     */
    int updateByPrimaryKeySelective(顧客別販売単価 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.顧客別販売単価
     *
     * @mbg.generated Sat Oct 19 18:42:50 JST 2024
     */
    int updateByPrimaryKey(顧客別販売単価 record);
}
