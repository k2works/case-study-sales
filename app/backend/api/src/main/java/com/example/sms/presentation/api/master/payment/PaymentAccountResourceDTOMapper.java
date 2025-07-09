package com.example.sms.presentation.api.master.payment;

import com.example.sms.domain.model.master.payment.account.incoming.PaymentAccount;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PaymentAccountResourceDTOMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    public static PaymentAccount convertToEntity(PaymentAccountResource resource) {
        return PaymentAccount.of(
                resource.getAccountCode(),
                resource.getAccountName(),
                LocalDateTime.parse(resource.getStartDate(), FORMATTER),
                LocalDateTime.parse(resource.getEndDate(), FORMATTER),
                resource.getAccountNameAfterStart(),
                resource.getAccountType().getCode(),
                resource.getAccountNumber(),
                resource.getBankAccountType().getCode(),
                resource.getAccountHolder(),
                resource.getDepartmentCode(),
                LocalDateTime.parse(resource.getDepartmentStartDate(), FORMATTER),
                resource.getBankCode(),
                resource.getBranchCode()
        );
    }
}