package com.example.sms.presentation.api.system.download;

import com.example.sms.domain.model.system.download.*;
import com.example.sms.domain.model.system.download.DownloadTarget;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Schema(description = "ダウンロード条件")
public class DownloadCriteriaResource implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    private DownloadTarget target;
    private String fileName;

    public static DownloadCriteria of(DownloadTarget target) {
        return switch (target) {
            case 部門 -> Department.of();
            case 社員 -> Employee.of();
            case 商品分類 -> ProductCategory.of();
            case 商品 -> Product.of();
            case 取引先グループ -> PartnerGroup.of();
            case 取引先 -> Partner.of();
            case 顧客 -> Customer.of();
            case 仕入先 -> Vendor.of();
            case 受注 -> SalesOrder.of();
        };
    }
}
