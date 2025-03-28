package com.example.sms.infrastructure.datasource.autogen.model;

import java.time.LocalDateTime;

public class 受注データ {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.受注データ.受注番号
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    private String 受注番号;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.受注データ.受注日
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    private LocalDateTime 受注日;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.受注データ.部門コード
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    private String 部門コード;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.受注データ.部門開始日
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    private LocalDateTime 部門開始日;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.受注データ.顧客コード
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    private String 顧客コード;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.受注データ.顧客枝番
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    private Integer 顧客枝番;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.受注データ.社員コード
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    private String 社員コード;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.受注データ.希望納期
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    private LocalDateTime 希望納期;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.受注データ.客先注文番号
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    private String 客先注文番号;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.受注データ.倉庫コード
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    private String 倉庫コード;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.受注データ.受注金額合計
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    private Integer 受注金額合計;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.受注データ.消費税合計
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    private Integer 消費税合計;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.受注データ.備考
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    private String 備考;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.受注データ.作成日時
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    private LocalDateTime 作成日時;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.受注データ.作成者名
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    private String 作成者名;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.受注データ.更新日時
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    private LocalDateTime 更新日時;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.受注データ.更新者名
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    private String 更新者名;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.受注データ.version
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    private Integer version;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.受注データ.受注番号
     *
     * @return the value of public.受注データ.受注番号
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public String get受注番号() {
        return 受注番号;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.受注データ.受注番号
     *
     * @param 受注番号 the value for public.受注データ.受注番号
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public void set受注番号(String 受注番号) {
        this.受注番号 = 受注番号 == null ? null : 受注番号.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.受注データ.受注日
     *
     * @return the value of public.受注データ.受注日
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public LocalDateTime get受注日() {
        return 受注日;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.受注データ.受注日
     *
     * @param 受注日 the value for public.受注データ.受注日
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public void set受注日(LocalDateTime 受注日) {
        this.受注日 = 受注日;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.受注データ.部門コード
     *
     * @return the value of public.受注データ.部門コード
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public String get部門コード() {
        return 部門コード;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.受注データ.部門コード
     *
     * @param 部門コード the value for public.受注データ.部門コード
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public void set部門コード(String 部門コード) {
        this.部門コード = 部門コード == null ? null : 部門コード.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.受注データ.部門開始日
     *
     * @return the value of public.受注データ.部門開始日
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public LocalDateTime get部門開始日() {
        return 部門開始日;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.受注データ.部門開始日
     *
     * @param 部門開始日 the value for public.受注データ.部門開始日
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public void set部門開始日(LocalDateTime 部門開始日) {
        this.部門開始日 = 部門開始日;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.受注データ.顧客コード
     *
     * @return the value of public.受注データ.顧客コード
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public String get顧客コード() {
        return 顧客コード;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.受注データ.顧客コード
     *
     * @param 顧客コード the value for public.受注データ.顧客コード
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public void set顧客コード(String 顧客コード) {
        this.顧客コード = 顧客コード == null ? null : 顧客コード.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.受注データ.顧客枝番
     *
     * @return the value of public.受注データ.顧客枝番
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public Integer get顧客枝番() {
        return 顧客枝番;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.受注データ.顧客枝番
     *
     * @param 顧客枝番 the value for public.受注データ.顧客枝番
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public void set顧客枝番(Integer 顧客枝番) {
        this.顧客枝番 = 顧客枝番;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.受注データ.社員コード
     *
     * @return the value of public.受注データ.社員コード
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public String get社員コード() {
        return 社員コード;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.受注データ.社員コード
     *
     * @param 社員コード the value for public.受注データ.社員コード
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public void set社員コード(String 社員コード) {
        this.社員コード = 社員コード == null ? null : 社員コード.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.受注データ.希望納期
     *
     * @return the value of public.受注データ.希望納期
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public LocalDateTime get希望納期() {
        return 希望納期;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.受注データ.希望納期
     *
     * @param 希望納期 the value for public.受注データ.希望納期
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public void set希望納期(LocalDateTime 希望納期) {
        this.希望納期 = 希望納期;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.受注データ.客先注文番号
     *
     * @return the value of public.受注データ.客先注文番号
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public String get客先注文番号() {
        return 客先注文番号;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.受注データ.客先注文番号
     *
     * @param 客先注文番号 the value for public.受注データ.客先注文番号
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public void set客先注文番号(String 客先注文番号) {
        this.客先注文番号 = 客先注文番号 == null ? null : 客先注文番号.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.受注データ.倉庫コード
     *
     * @return the value of public.受注データ.倉庫コード
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public String get倉庫コード() {
        return 倉庫コード;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.受注データ.倉庫コード
     *
     * @param 倉庫コード the value for public.受注データ.倉庫コード
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public void set倉庫コード(String 倉庫コード) {
        this.倉庫コード = 倉庫コード == null ? null : 倉庫コード.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.受注データ.受注金額合計
     *
     * @return the value of public.受注データ.受注金額合計
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public Integer get受注金額合計() {
        return 受注金額合計;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.受注データ.受注金額合計
     *
     * @param 受注金額合計 the value for public.受注データ.受注金額合計
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public void set受注金額合計(Integer 受注金額合計) {
        this.受注金額合計 = 受注金額合計;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.受注データ.消費税合計
     *
     * @return the value of public.受注データ.消費税合計
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public Integer get消費税合計() {
        return 消費税合計;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.受注データ.消費税合計
     *
     * @param 消費税合計 the value for public.受注データ.消費税合計
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public void set消費税合計(Integer 消費税合計) {
        this.消費税合計 = 消費税合計;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.受注データ.備考
     *
     * @return the value of public.受注データ.備考
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public String get備考() {
        return 備考;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.受注データ.備考
     *
     * @param 備考 the value for public.受注データ.備考
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public void set備考(String 備考) {
        this.備考 = 備考 == null ? null : 備考.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.受注データ.作成日時
     *
     * @return the value of public.受注データ.作成日時
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public LocalDateTime get作成日時() {
        return 作成日時;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.受注データ.作成日時
     *
     * @param 作成日時 the value for public.受注データ.作成日時
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public void set作成日時(LocalDateTime 作成日時) {
        this.作成日時 = 作成日時;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.受注データ.作成者名
     *
     * @return the value of public.受注データ.作成者名
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public String get作成者名() {
        return 作成者名;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.受注データ.作成者名
     *
     * @param 作成者名 the value for public.受注データ.作成者名
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public void set作成者名(String 作成者名) {
        this.作成者名 = 作成者名 == null ? null : 作成者名.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.受注データ.更新日時
     *
     * @return the value of public.受注データ.更新日時
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public LocalDateTime get更新日時() {
        return 更新日時;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.受注データ.更新日時
     *
     * @param 更新日時 the value for public.受注データ.更新日時
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public void set更新日時(LocalDateTime 更新日時) {
        this.更新日時 = 更新日時;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.受注データ.更新者名
     *
     * @return the value of public.受注データ.更新者名
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public String get更新者名() {
        return 更新者名;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.受注データ.更新者名
     *
     * @param 更新者名 the value for public.受注データ.更新者名
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public void set更新者名(String 更新者名) {
        this.更新者名 = 更新者名 == null ? null : 更新者名.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.受注データ.version
     *
     * @return the value of public.受注データ.version
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.受注データ.version
     *
     * @param version the value for public.受注データ.version
     *
     * @mbg.generated Mon Feb 17 17:55:57 JST 2025
     */
    public void setVersion(Integer version) {
        this.version = version;
    }
}