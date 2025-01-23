package com.example.sms.presentation.api.master.partner;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Schema(description = "顧客検索条件")
public class CustomerCriteriaResource implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    String customerCode;          // 顧客コード
    String customerName;          // 顧客名
    String customerNameKana;      // 顧客名カナ
    String customerType;          // 顧客区分
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
    String customerBillingCategory; // 顧客請求区分
}
