package com.example.sms.domain.model.master.partner;

import com.example.sms.domain.type.partner.CustomerType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

/**
 * 顧客
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Customer {
    CustomerCode customerCode; // 顧客コード
    CustomerType customerType; // 顧客区分
    BillingCode billingCode; // 請求先コード
    CollectionCode collectionCode; // 回収先コード
    CustomerName customerName; // 顧客名
    String companyRepresentativeCode; // 自社担当者コード
    String customerRepresentativeName; // 顧客担当者名
    String customerDepartmentName; // 顧客部門名
    String customerPostalCode; // 顧客郵便番号
    String customerPrefecture; // 顧客都道府県
    String customerAddress1; // 顧客住所１
    String customerAddress2; // 顧客住所２
    String customerPhoneNumber; // 顧客電話番号
    String customerFaxNumber; // 顧客ｆａｘ番号
    String customerEmailAddress; // 顧客メールアドレス
    Integer customerBillingCategory; // 顧客請求区分
    Integer customerClosingDay1; // 顧客締日１
    Integer customerPaymentMonth1; // 顧客支払月１
    Integer customerPaymentDay1; // 顧客支払日１
    Integer customerPaymentMethod1; // 顧客支払方法１
    Integer customerClosingDay2; // 顧客締日２
    Integer customerPaymentMonth2; // 顧客支払月２
    Integer customerPaymentDay2; // 顧客支払日２
    Integer customerPaymentMethod2; // 顧客支払方法２
    List<Shipping> shippings; // 出荷先

    public static Customer of(
            String customerCode,
            Integer customerBranchNumber,
            Integer customerCategory,
            String billingCode,
            Integer billingBranchNumber,
            String collectionCode,
            Integer collectionBranchNumber,
            String customerName,
            String customerNameKana,
            String companyRepresentativeCode,
            String customerRepresentativeName,
            String customerDepartmentName,
            String customerPostalCode,
            String customerPrefecture,
            String customerAddress1,
            String customerAddress2,
            String customerPhoneNumber,
            String customerFaxNumber,
            String customerEmailAddress,
            Integer customerBillingCategory,
            Integer customerClosingDay1,
            Integer customerPaymentMonth1,
            Integer customerPaymentDay1,
            Integer customerPaymentMethod1,
            Integer customerClosingDay2,
            Integer customerPaymentMonth2,
            Integer customerPaymentDay2,
            Integer customerPaymentMethod2
    ) {
        return new Customer(
                CustomerCode.of(customerCode, customerBranchNumber),
                CustomerType.fromCode(customerCategory),
                BillingCode.of(billingCode, billingBranchNumber),
                CollectionCode.of(collectionCode, collectionBranchNumber),
                CustomerName.of(customerName, customerNameKana),
                companyRepresentativeCode,
                customerRepresentativeName,
                customerDepartmentName,
                customerPostalCode,
                customerPrefecture,
                customerAddress1,
                customerAddress2,
                customerPhoneNumber,
                customerFaxNumber,
                customerEmailAddress,
                customerBillingCategory,
                customerClosingDay1,
                customerPaymentMonth1,
                customerPaymentDay1,
                customerPaymentMethod1,
                customerClosingDay2,
                customerPaymentMonth2,
                customerPaymentDay2,
                customerPaymentMethod2,
                List.of()
        );
    }

    public static Customer of(Customer customer, List<Shipping> shippings) {
        return new Customer(
                customer.customerCode,
                customer.customerType,
                customer.billingCode,
                customer.collectionCode,
                customer.customerName,
                customer.companyRepresentativeCode,
                customer.customerRepresentativeName,
                customer.customerDepartmentName,
                customer.customerPostalCode,
                customer.customerPrefecture,
                customer.customerAddress1,
                customer.customerAddress2,
                customer.customerPhoneNumber,
                customer.customerFaxNumber,
                customer.customerEmailAddress,
                customer.customerBillingCategory,
                customer.customerClosingDay1,
                customer.customerPaymentMonth1,
                customer.customerPaymentDay1,
                customer.customerPaymentMethod1,
                customer.customerClosingDay2,
                customer.customerPaymentMonth2,
                customer.customerPaymentDay2,
                customer.customerPaymentMethod2,
                shippings
        );
    }
}
