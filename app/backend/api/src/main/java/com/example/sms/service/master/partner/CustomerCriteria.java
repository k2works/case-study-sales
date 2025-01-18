package com.example.sms.service.master.partner;

import lombok.Builder;
import lombok.Value;

/**
 * 顧客検索条件
 */
@Value
@Builder
public class CustomerCriteria {
    String customerCode;          // 顧客コード
    String customerName;          // 顧客名
    String customerNameKana;      // 顧客名カナ
    Integer customerType;          // 顧客区分
    String billingCode;           // 請求先コード
    String collectionCode; // 回収先コード
    String companyRepresentativeCode; // 自社担当者コード
    String customerRepresentativeName; // 顧客担当者名
    String customerDepartmentName; // 顧客部門名
    String postalCode;
    String prefecture;
    String address1;
    String address2;
    String customerPhoneNumber; // 顧客電話番号
    String customerFaxNumber; // 顧客ｆａｘ番号
    String customerEmailAddress; // 顧客メールアドレス
    Integer customerBillingCategory; // 顧客請求区分
}
