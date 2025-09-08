package com.example.sms.domain.model.sales.payment.incoming;

import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.partner.customer.Customer;
import com.example.sms.domain.model.master.partner.customer.CustomerCode;
import com.example.sms.domain.model.master.payment.account.incoming.PaymentAccount;
import com.example.sms.domain.type.money.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * 入金
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder(toBuilder = true)
public class Payment {
    PaymentNumber paymentNumber; // 入金番号
    LocalDateTime paymentDate; // 入金日
    DepartmentId departmentId; // 部門ID
    CustomerCode customerCode; // 顧客コード
    PaymentMethodType paymentMethodType; // 支払方法区分
    String paymentAccountCode; // 入金口座コード
    Money paymentAmount; // 入金金額
    Money offsetAmount; // 消込金額

    // 関連エンティティ
    Customer customer;
    PaymentAccount paymentAccount;

    /**
     * ファクトリーメソッド
     */
    public static Payment of(String paymentNumber, LocalDateTime paymentDate, String departmentCode, LocalDateTime departmentStartDate,
                             String customerCode, Integer customerBranchNumber, Integer paymentMethodType,
                             String paymentAccountCode, Integer paymentAmount, Integer offsetAmount) {
        return new Payment(
                PaymentNumber.of(paymentNumber),
                paymentDate,
                DepartmentId.of(departmentCode, departmentStartDate),
                CustomerCode.of(customerCode, customerBranchNumber),
                PaymentMethodType.fromCode(paymentMethodType),
                paymentAccountCode,
                Money.of(paymentAmount),
                Money.of(offsetAmount),
                null,
                null
        );
    }

    /**
     * 関連エンティティを含むファクトリーメソッド
     */
    public static Payment of(Payment payment, Customer customer, PaymentAccount paymentAccount) {
        return new Payment(
                payment.getPaymentNumber(),
                payment.getPaymentDate(),
                payment.getDepartmentId(),
                payment.getCustomerCode(),
                payment.getPaymentMethodType(),
                payment.getPaymentAccountCode(),
                payment.getPaymentAmount(),
                payment.getOffsetAmount(),
                customer,
                paymentAccount
        );
    }
}