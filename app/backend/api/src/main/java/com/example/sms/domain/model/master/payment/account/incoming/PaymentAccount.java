package com.example.sms.domain.model.master.payment.account.incoming;

import com.example.sms.domain.model.master.department.DepartmentId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * 入金口座マスタのドメインモデル
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder(toBuilder = true)
public class PaymentAccount {
    PaymentAccountCode accountCode; // 入金口座コード
    String accountName; // 入金口座名
    LocalDateTime startDate; // 適用開始日
    LocalDateTime endDate; // 適用終了日
    String accountNameAfterStart; // 適用開始後入金口座名
    PaymentAccountType accountType; // 入金口座区分
    String accountNumber; // 入金口座番号
    BankAccountType bankAccountType; // 銀行口座種別
    String accountHolder; // 口座名義人
    DepartmentId departmentId; // 部門ID
    String bankCode; // 全銀協銀行コード
    String branchCode; // 全銀協支店コード

    /**
     * ファクトリーメソッド
     */
    public static PaymentAccount of(String accountCode, String accountName, LocalDateTime startDate, LocalDateTime endDate,
                                   String accountNameAfterStart, String accountType, String accountNumber,
                                   String bankAccountType, String accountHolder, String departmentCode, LocalDateTime departmentStartDate,
                                   String bankCode, String branchCode) {
        return new PaymentAccount(
                PaymentAccountCode.of(accountCode),
                accountName,
                startDate,
                endDate,
                accountNameAfterStart,
                PaymentAccountType.fromCode(accountType),
                accountNumber,
                BankAccountType.fromCode(bankAccountType),
                accountHolder,
                DepartmentId.of(departmentCode, departmentStartDate),
                bankCode,
                branchCode
        );
    }
}