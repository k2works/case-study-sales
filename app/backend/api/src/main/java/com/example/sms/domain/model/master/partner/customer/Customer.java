package com.example.sms.domain.model.master.partner.customer;

import com.example.sms.domain.model.master.partner.billing.Billing;
import com.example.sms.domain.model.master.partner.billing.ClosingBilling;
import com.example.sms.domain.type.mail.EmailAddress;
import com.example.sms.domain.type.phone.FaxNumber;
import com.example.sms.domain.type.phone.PhoneNumber;
import com.example.sms.domain.type.address.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * 顧客
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder(toBuilder = true)
public class Customer {
    CustomerCode customerCode; // 顧客コード
    CustomerType customerType; // 顧客区分
    BillingCode billingCode; // 請求先コード
    CollectionCode collectionCode; // 回収先コード
    CustomerName customerName; // 顧客名
    String companyRepresentativeCode; // 自社担当者コード
    String customerRepresentativeName; // 顧客担当者名
    String customerDepartmentName; // 顧客部門名
    Address customerAddress; // 顧客住所
    PhoneNumber customerPhoneNumber; // 顧客電話番号
    FaxNumber customerFaxNumber; // 顧客ｆａｘ番号
    EmailAddress customerEmailAddress; // 顧客メールアドレス
    Billing billing; // 請求
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
                Address.of(
                        customerPostalCode,
                        customerPrefecture,
                        customerAddress1,
                        customerAddress2
                ),
                PhoneNumber.of(customerPhoneNumber),
                FaxNumber.of(customerFaxNumber),
                EmailAddress.of(customerEmailAddress),
                Billing.of(
                        CustomerBillingCategory.fromCode(customerBillingCategory),
                        ClosingBilling.of(
                                customerClosingDay1,
                                customerPaymentMonth1,
                                customerPaymentDay1,
                                customerPaymentMethod1
                        ),
                        ClosingBilling.of(
                                customerClosingDay2,
                                customerPaymentMonth2,
                                customerPaymentDay2,
                                customerPaymentMethod2
                        )
                ),
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
                customer.customerAddress,
                customer.customerPhoneNumber,
                customer.customerFaxNumber,
                customer.customerEmailAddress,
                customer.billing,
                shippings
        );
    }

    public static Customer of(Customer customer, ClosingBilling closingBilling1, ClosingBilling closingBilling2) {
        isTrue(
                !(closingBilling1 == null && closingBilling2 == null),
                "締請求が設定されていません"
        );

        notNull(closingBilling1, "締請求1が設定されていません");

        return new Customer(
                customer.customerCode,
                customer.customerType,
                customer.billingCode,
                customer.collectionCode,
                customer.customerName,
                customer.companyRepresentativeCode,
                customer.customerRepresentativeName,
                customer.customerDepartmentName,
                customer.customerAddress,
                customer.customerPhoneNumber,
                customer.customerFaxNumber,
                customer.customerEmailAddress,
                Billing.of(
                        CustomerBillingCategory.締請求,
                        closingBilling1,
                        closingBilling2
                ),
                customer.shippings
        );
    }

    public static Customer of(
            CustomerCode customerCode,
            CustomerType customerType,
            BillingCode billingCode,
            CollectionCode collectionCode,
            CustomerName customerName,
            String companyRepresentativeCode,
            String customerRepresentativeName,
            String customerDepartmentName,
            Address customerAddress,
            PhoneNumber customerPhoneNumber,
            FaxNumber customerFaxNumber,
            EmailAddress customerEmailAddress,
            Billing billing,
            List<Shipping> shippings
    ) {
        return new Customer(
                customerCode,
                customerType,
                billingCode,
                collectionCode,
                customerName,
                companyRepresentativeCode,
                customerRepresentativeName,
                customerDepartmentName,
                customerAddress,
                customerPhoneNumber,
                customerFaxNumber,
                customerEmailAddress,
                billing,
                shippings
        );
    }
}