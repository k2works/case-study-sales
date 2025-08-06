package com.example.sms.service.master.payment;

import com.example.sms.domain.model.master.payment.account.incoming.PaymentAccountType;
import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * 入金口座検索条件
 */
@Value
@Builder(builderClassName = "PaymentAccountCriteriaBuilder")
@Slf4j
public class PaymentAccountCriteria {

    String accountCode;
    String accountName;
    LocalDateTime startDate;
    LocalDateTime endDate;
    String accountType;
    String departmentCode;

    private PaymentAccountCriteria(String accountCode, String accountName, LocalDateTime startDate, 
                                  LocalDateTime endDate, String accountType, String departmentCode) {
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            String errorMessage = "終了日は開始日より後の日付でなければなりません。";
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        this.accountCode = accountCode;
        this.accountName = accountName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.accountType = accountType;
        this.departmentCode = departmentCode;
    }

    public static class PaymentAccountCriteriaBuilder {
        public PaymentAccountCriteria build() {
            return new PaymentAccountCriteria(accountCode, accountName, startDate, endDate, accountType, departmentCode);
        }
    }
}