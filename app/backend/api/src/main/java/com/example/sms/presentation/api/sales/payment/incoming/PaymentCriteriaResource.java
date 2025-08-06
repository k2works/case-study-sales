package com.example.sms.presentation.api.sales.payment.incoming;

import com.example.sms.domain.model.sales.payment.incoming.PaymentMethodType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 入金検索条件
 */
@Getter
@Setter
@Schema(description = "入金検索条件リソース")
public class PaymentCriteriaResource {
    @Schema(description = "入金番号")
    private String paymentNumber;

    @Schema(description = "入金日")
    private LocalDateTime paymentDate;

    private String departmentCode;

    @Schema(description = "顧客コード")
    private String customerCode;

    @Schema(description = "支払方法区分")
    private PaymentMethodType paymentMethodType;

    @Schema(description = "入金口座コード")
    private String paymentAccountCode;
}
