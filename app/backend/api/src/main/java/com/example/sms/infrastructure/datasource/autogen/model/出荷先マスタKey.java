package com.example.sms.infrastructure.datasource.autogen.model;

public class 出荷先マスタKey {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.出荷先マスタ.顧客コード
     *
     * @mbg.generated Tue Jan 07 18:37:28 JST 2025
     */
    private String 顧客コード;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.出荷先マスタ.出荷先番号
     *
     * @mbg.generated Tue Jan 07 18:37:28 JST 2025
     */
    private Integer 出荷先番号;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.出荷先マスタ.顧客枝番
     *
     * @mbg.generated Tue Jan 07 18:37:28 JST 2025
     */
    private Integer 顧客枝番;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.出荷先マスタ.顧客コード
     *
     * @return the value of public.出荷先マスタ.顧客コード
     *
     * @mbg.generated Tue Jan 07 18:37:28 JST 2025
     */
    public String get顧客コード() {
        return 顧客コード;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.出荷先マスタ.顧客コード
     *
     * @param 顧客コード the value for public.出荷先マスタ.顧客コード
     *
     * @mbg.generated Tue Jan 07 18:37:28 JST 2025
     */
    public void set顧客コード(String 顧客コード) {
        this.顧客コード = 顧客コード == null ? null : 顧客コード.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.出荷先マスタ.出荷先番号
     *
     * @return the value of public.出荷先マスタ.出荷先番号
     *
     * @mbg.generated Tue Jan 07 18:37:28 JST 2025
     */
    public Integer get出荷先番号() {
        return 出荷先番号;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.出荷先マスタ.出荷先番号
     *
     * @param 出荷先番号 the value for public.出荷先マスタ.出荷先番号
     *
     * @mbg.generated Tue Jan 07 18:37:28 JST 2025
     */
    public void set出荷先番号(Integer 出荷先番号) {
        this.出荷先番号 = 出荷先番号;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.出荷先マスタ.顧客枝番
     *
     * @return the value of public.出荷先マスタ.顧客枝番
     *
     * @mbg.generated Tue Jan 07 18:37:28 JST 2025
     */
    public Integer get顧客枝番() {
        return 顧客枝番;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.出荷先マスタ.顧客枝番
     *
     * @param 顧客枝番 the value for public.出荷先マスタ.顧客枝番
     *
     * @mbg.generated Tue Jan 07 18:37:28 JST 2025
     */
    public void set顧客枝番(Integer 顧客枝番) {
        this.顧客枝番 = 顧客枝番;
    }
}