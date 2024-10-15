package com.example.sms.infrastructure.datasource.master.employee;

import java.time.LocalDateTime;

public class 社員マスタ {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.社員マスタ.社員コード
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    private String 社員コード;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.社員マスタ.社員名
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    private String 社員名;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.社員マスタ.社員名カナ
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    private String 社員名カナ;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.社員マスタ.パスワード
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    private String パスワード;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.社員マスタ.電話番号
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    private String 電話番号;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.社員マスタ.FAX番号
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    private String fax番号;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.社員マスタ.部門コード
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    private String 部門コード;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.社員マスタ.開始日
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    private LocalDateTime 開始日;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.社員マスタ.職種コード
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    private String 職種コード;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.社員マスタ.承認権限コード
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    private String 承認権限コード;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.社員マスタ.作成日時
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    private LocalDateTime 作成日時;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.社員マスタ.作成者名
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    private String 作成者名;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.社員マスタ.更新日時
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    private LocalDateTime 更新日時;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.社員マスタ.更新者名
     *
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    private String 更新者名;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.社員マスタ.社員コード
     *
     * @return the value of public.社員マスタ.社員コード
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public String get社員コード() {
        return 社員コード;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.社員マスタ.社員コード
     *
     * @param 社員コード the value for public.社員マスタ.社員コード
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public void set社員コード(String 社員コード) {
        this.社員コード = 社員コード == null ? null : 社員コード.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.社員マスタ.社員名
     *
     * @return the value of public.社員マスタ.社員名
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public String get社員名() {
        return 社員名;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.社員マスタ.社員名
     *
     * @param 社員名 the value for public.社員マスタ.社員名
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public void set社員名(String 社員名) {
        this.社員名 = 社員名 == null ? null : 社員名.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.社員マスタ.社員名カナ
     *
     * @return the value of public.社員マスタ.社員名カナ
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public String get社員名カナ() {
        return 社員名カナ;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.社員マスタ.社員名カナ
     *
     * @param 社員名カナ the value for public.社員マスタ.社員名カナ
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public void set社員名カナ(String 社員名カナ) {
        this.社員名カナ = 社員名カナ == null ? null : 社員名カナ.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.社員マスタ.パスワード
     *
     * @return the value of public.社員マスタ.パスワード
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public String getパスワード() {
        return パスワード;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.社員マスタ.パスワード
     *
     * @param パスワード the value for public.社員マスタ.パスワード
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public void setパスワード(String パスワード) {
        this.パスワード = パスワード == null ? null : パスワード.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.社員マスタ.電話番号
     *
     * @return the value of public.社員マスタ.電話番号
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public String get電話番号() {
        return 電話番号;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.社員マスタ.電話番号
     *
     * @param 電話番号 the value for public.社員マスタ.電話番号
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public void set電話番号(String 電話番号) {
        this.電話番号 = 電話番号 == null ? null : 電話番号.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.社員マスタ.FAX番号
     *
     * @return the value of public.社員マスタ.FAX番号
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public String getFax番号() {
        return fax番号;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.社員マスタ.FAX番号
     *
     * @param fax番号 the value for public.社員マスタ.FAX番号
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public void setFax番号(String fax番号) {
        this.fax番号 = fax番号 == null ? null : fax番号.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.社員マスタ.部門コード
     *
     * @return the value of public.社員マスタ.部門コード
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public String get部門コード() {
        return 部門コード;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.社員マスタ.部門コード
     *
     * @param 部門コード the value for public.社員マスタ.部門コード
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public void set部門コード(String 部門コード) {
        this.部門コード = 部門コード == null ? null : 部門コード.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.社員マスタ.開始日
     *
     * @return the value of public.社員マスタ.開始日
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public LocalDateTime get開始日() {
        return 開始日;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.社員マスタ.開始日
     *
     * @param 開始日 the value for public.社員マスタ.開始日
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public void set開始日(LocalDateTime 開始日) {
        this.開始日 = 開始日;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.社員マスタ.職種コード
     *
     * @return the value of public.社員マスタ.職種コード
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public String get職種コード() {
        return 職種コード;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.社員マスタ.職種コード
     *
     * @param 職種コード the value for public.社員マスタ.職種コード
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public void set職種コード(String 職種コード) {
        this.職種コード = 職種コード == null ? null : 職種コード.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.社員マスタ.承認権限コード
     *
     * @return the value of public.社員マスタ.承認権限コード
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public String get承認権限コード() {
        return 承認権限コード;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.社員マスタ.承認権限コード
     *
     * @param 承認権限コード the value for public.社員マスタ.承認権限コード
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public void set承認権限コード(String 承認権限コード) {
        this.承認権限コード = 承認権限コード == null ? null : 承認権限コード.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.社員マスタ.作成日時
     *
     * @return the value of public.社員マスタ.作成日時
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public LocalDateTime get作成日時() {
        return 作成日時;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.社員マスタ.作成日時
     *
     * @param 作成日時 the value for public.社員マスタ.作成日時
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public void set作成日時(LocalDateTime 作成日時) {
        this.作成日時 = 作成日時;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.社員マスタ.作成者名
     *
     * @return the value of public.社員マスタ.作成者名
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public String get作成者名() {
        return 作成者名;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.社員マスタ.作成者名
     *
     * @param 作成者名 the value for public.社員マスタ.作成者名
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public void set作成者名(String 作成者名) {
        this.作成者名 = 作成者名 == null ? null : 作成者名.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.社員マスタ.更新日時
     *
     * @return the value of public.社員マスタ.更新日時
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public LocalDateTime get更新日時() {
        return 更新日時;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.社員マスタ.更新日時
     *
     * @param 更新日時 the value for public.社員マスタ.更新日時
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public void set更新日時(LocalDateTime 更新日時) {
        this.更新日時 = 更新日時;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.社員マスタ.更新者名
     *
     * @return the value of public.社員マスタ.更新者名
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public String get更新者名() {
        return 更新者名;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.社員マスタ.更新者名
     *
     * @param 更新者名 the value for public.社員マスタ.更新者名
     * @mbg.generated Fri Oct 04 15:48:01 JST 2024
     */
    public void set更新者名(String 更新者名) {
        this.更新者名 = 更新者名 == null ? null : 更新者名.trim();
    }
}
