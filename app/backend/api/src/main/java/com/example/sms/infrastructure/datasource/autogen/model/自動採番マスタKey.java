package com.example.sms.infrastructure.datasource.autogen.model;

import java.time.LocalDateTime;

public class 自動採番マスタKey {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.自動採番マスタ.伝票種別コード
     *
     * @mbg.generated Mon Mar 31 13:42:42 JST 2025
     */
    private String 伝票種別コード;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.自動採番マスタ.年月
     *
     * @mbg.generated Mon Mar 31 13:42:42 JST 2025
     */
    private LocalDateTime 年月;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.自動採番マスタ.伝票種別コード
     *
     * @return the value of public.自動採番マスタ.伝票種別コード
     *
     * @mbg.generated Mon Mar 31 13:42:42 JST 2025
     */
    public String get伝票種別コード() {
        return 伝票種別コード;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.自動採番マスタ.伝票種別コード
     *
     * @param 伝票種別コード the value for public.自動採番マスタ.伝票種別コード
     *
     * @mbg.generated Mon Mar 31 13:42:42 JST 2025
     */
    public void set伝票種別コード(String 伝票種別コード) {
        this.伝票種別コード = 伝票種別コード == null ? null : 伝票種別コード.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.自動採番マスタ.年月
     *
     * @return the value of public.自動採番マスタ.年月
     *
     * @mbg.generated Mon Mar 31 13:42:42 JST 2025
     */
    public LocalDateTime get年月() {
        return 年月;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.自動採番マスタ.年月
     *
     * @param 年月 the value for public.自動採番マスタ.年月
     *
     * @mbg.generated Mon Mar 31 13:42:42 JST 2025
     */
    public void set年月(LocalDateTime 年月) {
        this.年月 = 年月;
    }
}