package com.example.sms.presentation.api.master.payment;

import com.example.sms.domain.model.master.payment.account.incoming.PaymentAccount;
import com.example.sms.domain.model.master.payment.account.incoming.PaymentAccountType;
import com.example.sms.domain.model.master.payment.account.incoming.BankAccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Schema(description = "入金口座")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentAccountResource {
    @NotNull
    @Schema(description = "入金口座コード")
    private String accountCode;
    
    @NotNull
    @Schema(description = "入金口座名")
    private String accountName;
    
    @NotNull
    @Schema(description = "適用開始日")
    private String startDate;
    
    @NotNull
    @Schema(description = "適用終了日")
    private String endDate;
    
    @Schema(description = "適用開始後入金口座名")
    private String accountNameAfterStart;
    
    @Schema(description = "入金口座区分")
    private String accountType;
    
    @Schema(description = "入金口座番号")
    private String accountNumber;
    
    @Schema(description = "銀行口座種別")
    private String bankAccountType;
    
    @Schema(description = "口座名義人")
    private String accountHolder;
    
    @Schema(description = "部門コード")
    private String departmentCode;
    
    @Schema(description = "部門開始日")
    private String departmentStartDate;
    
    @Schema(description = "全銀協銀行コード")
    private String bankCode;
    
    @Schema(description = "全銀協支店コード")
    private String branchCode;

    public static PaymentAccountResource from(PaymentAccount paymentAccount) {
        return PaymentAccountResource.builder()
                .accountCode(paymentAccount.getAccountCode().getValue())
                .accountName(paymentAccount.getAccountName())
                .startDate(paymentAccount.getStartDate().toString())
                .endDate(paymentAccount.getEndDate().toString())
                .accountNameAfterStart(paymentAccount.getAccountNameAfterStart())
                .accountType(paymentAccount.getAccountType().getCode())
                .accountNumber(paymentAccount.getAccountNumber())
                .bankAccountType(paymentAccount.getBankAccountType().getCode())
                .accountHolder(paymentAccount.getAccountHolder())
                .departmentCode(paymentAccount.getDepartmentId().getDeptCode().getValue())
                .departmentStartDate(paymentAccount.getDepartmentId().getDepartmentStartDate().getValue().toString())
                .bankCode(paymentAccount.getBankCode())
                .branchCode(paymentAccount.getBranchCode())
                .build();
    }
}