package com.example.sms.presentation.api.system.download;

import com.example.sms.domain.model.system.download.*;
import com.example.sms.domain.model.system.download.DownloadTarget;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "ダウンロード条件")
public class DownloadCriteriaResource {
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
            case 受注 -> Order.of();
            case 出荷 -> Shipment.of();
            case 売上 -> Sales.of();
            case 発注 -> PurchaseOrder.of();
            case 請求 -> Invoice.of();
            case 入金 -> Payment.of();
            case 口座 -> PaymentAccount.of();
            case 在庫 -> Inventory.of();
            case 倉庫 -> Warehouse.of();
            case 棚番 -> LocationNumber.of();
        };
    }
}
