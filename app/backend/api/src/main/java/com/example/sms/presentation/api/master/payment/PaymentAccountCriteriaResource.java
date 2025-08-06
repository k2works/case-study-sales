package com.example.sms.presentation.api.master.payment;

import com.example.sms.domain.model.master.payment.account.incoming.PaymentAccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "口座検索条件")
public class PaymentAccountCriteriaResource {
    @Schema(description = "入金口座コード")
    private String accountCode;

    @Schema(description = "入金口座名")
    private String accountName;

    @Schema(description = "適用開始日")
    private String startDate;

    @Schema(description = "適用終了日")
    private String endDate;

    @Schema(description = "入金口座区分")
    private PaymentAccountType accountType;

    @Schema(description = "部門コード")
    private String departmentCode;
}
