package com.example.sms.infrastructure.datasource.autogen.model;

public class 仕入先マスタKey {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.仕入先マスタ.仕入先コード
     *
     * @mbg.generated Tue Jan 07 18:37:28 JST 2025
     */
    private String 仕入先コード;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.仕入先マスタ.仕入先枝番
     *
     * @mbg.generated Tue Jan 07 18:37:28 JST 2025
     */
    private Integer 仕入先枝番;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.仕入先マスタ.仕入先コード
     *
     * @return the value of public.仕入先マスタ.仕入先コード
     *
     * @mbg.generated Tue Jan 07 18:37:28 JST 2025
     */
    public String get仕入先コード() {
        return 仕入先コード;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.仕入先マスタ.仕入先コード
     *
     * @param 仕入先コード the value for public.仕入先マスタ.仕入先コード
     *
     * @mbg.generated Tue Jan 07 18:37:28 JST 2025
     */
    public void set仕入先コード(String 仕入先コード) {
        this.仕入先コード = 仕入先コード == null ? null : 仕入先コード.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.仕入先マスタ.仕入先枝番
     *
     * @return the value of public.仕入先マスタ.仕入先枝番
     *
     * @mbg.generated Tue Jan 07 18:37:28 JST 2025
     */
    public Integer get仕入先枝番() {
        return 仕入先枝番;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.仕入先マスタ.仕入先枝番
     *
     * @param 仕入先枝番 the value for public.仕入先マスタ.仕入先枝番
     *
     * @mbg.generated Tue Jan 07 18:37:28 JST 2025
     */
    public void set仕入先枝番(Integer 仕入先枝番) {
        this.仕入先枝番 = 仕入先枝番;
    }
}