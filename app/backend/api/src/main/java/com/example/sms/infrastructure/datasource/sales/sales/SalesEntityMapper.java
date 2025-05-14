package com.example.sms.infrastructure.datasource.sales.sales;

import com.example.sms.domain.model.master.partner.customer.CustomerCode;
import com.example.sms.domain.model.sales.order.TaxRateType;
import com.example.sms.domain.model.sales.sales.*;
import com.example.sms.infrastructure.datasource.autogen.model.売上データ;
import com.example.sms.infrastructure.datasource.autogen.model.売上データ明細;
import com.example.sms.infrastructure.datasource.autogen.model.売上データ明細Key;
import com.example.sms.infrastructure.datasource.master.product.ProductEntityMapper;
import com.example.sms.infrastructure.datasource.sales.sales.sales_line.SalesLineCustomEntity;
import com.example.sms.infrastructure.datasource.system.download.SalesDownloadCSV;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Component
public class SalesEntityMapper {

    public 売上データ mapToEntity(Sales sales) {
        // salesがnullの場合、新しい空のSalesオブジェクトを返す
        if (sales == null) {
            return new 売上データ();
        }

        売上データ salesEntity = new 売上データ();

        // 各プロパティにnullチェックを追加
        salesEntity.set売上番号(sales.getSalesNumber().getValue());
        salesEntity.set受注番号(sales.getOrderNumber().getValue());
        salesEntity.set売上日(sales.getSalesDate().getValue());
        salesEntity.set売上区分(sales.getSalesType().getCode());
        salesEntity.set部門コード(sales.getDepartmentId().getDeptCode().getValue());
        salesEntity.set部門開始日(sales.getDepartmentId().getDepartmentStartDate().getValue());
        salesEntity.set取引先コード(sales.getPartnerCode().getValue());
        salesEntity.set顧客コード(sales.getCustomerCode() != null ? sales.getCustomerCode().getCode().getValue() : null);
        salesEntity.set顧客枝番(sales.getCustomerCode() != null ? sales.getCustomerCode().getBranchNumber() : null);
        salesEntity.set社員コード(sales.getEmployeeCode().getValue());
        salesEntity.set売上金額合計(
                sales.getTotalSalesAmount() != null ? sales.getTotalSalesAmount().getAmount() : 0
        );
        salesEntity.set消費税合計(
                sales.getTotalConsumptionTax() != null ? sales.getTotalConsumptionTax().getAmount() : 0
        );
        salesEntity.set備考(sales.getRemarks());
        salesEntity.set赤黒伝票番号(sales.getVoucherNumber());
        salesEntity.set元伝票番号(sales.getOriginalVoucherNumber());

        return salesEntity;
    }

    public 売上データ明細 mapToEntity(売上データ明細Key key, SalesLine salesLine) {
        売上データ明細 salesEntity = new 売上データ明細();
        salesEntity.set売上番号(key.get売上番号());
        salesEntity.set売上行番号(key.get売上行番号());
        salesEntity.set商品コード(Objects.requireNonNull(salesLine.getProductCode()).getValue());
        salesEntity.set商品名(salesLine.getProductName());
        salesEntity.set販売単価(Objects.requireNonNull(salesLine.getSalesUnitPrice()).getAmount());
        salesEntity.set売上数量(Objects.requireNonNull(salesLine.getSalesQuantity()).getAmount());
        salesEntity.set出荷数量(Objects.requireNonNull(salesLine.getShippedQuantity()).getAmount());
        salesEntity.set値引金額(Objects.requireNonNull(salesLine.getDiscountAmount()).getAmount());
        salesEntity.set請求日(Optional.of(salesLine)
                .map(SalesLine::getBillingDate)
                .map(BillingDate::getValue)
                .orElse(null));
        salesEntity.set請求番号(Optional.of(salesLine)
                .map(SalesLine::getBillingNumber)
                .map(BillingNumber::getValue)
                .orElse(null));
        salesEntity.set請求遅延区分(Optional.of(salesLine)
                .map(SalesLine::getBillingDelayType)
                .map(BillingDelayType::getCode)
                .orElse(null));
        salesEntity.set自動仕訳日(Optional.of(salesLine)
                .map(SalesLine::getAutoJournalDate)
                .map(AutoJournalDate::getValue)
                .orElse(null));
        salesEntity.set消費税率(Objects.requireNonNull(salesLine.getTaxRate()).getRate());
        return salesEntity;
    }

    public 売上データ明細Key mapToKey(SalesLine salesLine) {
        売上データ明細Key key = new 売上データ明細Key();
        key.set売上番号(salesLine.getSalesNumber().getValue());
        key.set売上行番号(salesLine.getSalesLineNumber());
        return key;
    }

    public Sales mapToDomainModel(SalesCustomEntity salesData) {

        Function<SalesLineCustomEntity, SalesLine> salesLineMapper = e -> {
            if (Objects.isNull(e)) {
                return SalesLine.of(null, null, null, null, null, null, null, null, null, null, null, null, null, null);
            }

            return SalesLine.of(
                    e.get売上番号(),
                    e.get売上行番号(),
                    e.get商品コード(),
                    e.get商品名(),
                    e.get販売単価(),
                    e.get売上数量(),
                    e.get出荷数量(),
                    e.get値引金額(),
                    e.get請求日(),
                    e.get請求番号(),
                    e.get請求遅延区分(),
                    e.get自動仕訳日(),
                    e.get商品マスタ()  != null ? ProductEntityMapper.mapToDomainModel(e.get商品マスタ()) : null,
                    TaxRateType.of(e.get消費税率())
            );
        };


        Sales sales = Sales.of(
                salesData.get売上番号(),
                salesData.get受注番号(),
                salesData.get売上日(),
                salesData.get売上区分(),
                salesData.get部門コード(),
                salesData.get部門開始日(),
                salesData.get顧客コード(),
                salesData.get顧客枝番(),
                salesData.get社員コード(),
                salesData.get赤黒伝票番号(),
                salesData.get元伝票番号(),
                salesData.get備考(),
                salesData.get売上データ明細().stream()
                        .map(salesLineMapper)
                        .toList()
        );

        // 顧客コード、顧客枝番を設定
        return new Sales(
                sales.getSalesNumber(),
                sales.getOrderNumber(),
                sales.getSalesDate(),
                sales.getSalesType(),
                sales.getDepartmentId(),
                sales.getPartnerCode(),
                salesData.get顧客コード() != null ? CustomerCode.of(salesData.get顧客コード(), salesData.get顧客枝番()) : null,
                sales.getEmployeeCode(),
                sales.getTotalSalesAmount(),
                sales.getTotalConsumptionTax(),
                sales.getRemarks(),
                sales.getVoucherNumber(),
                sales.getOriginalVoucherNumber(),
                sales.getSalesLines()
        );
    }

    public SalesDownloadCSV mapToCsvModel(Sales sales, SalesLine salesLine) {
        return new SalesDownloadCSV(
                sales.getSalesNumber().getValue(),
                sales.getOrderNumber().getValue(),
                sales.getSalesDate().getValue(),
                sales.getSalesType().getCode(),
                sales.getDepartmentId().getDeptCode().getValue(),
                sales.getDepartmentId().getDepartmentStartDate().getValue(),
                sales.getPartnerCode().getValue(),
                sales.getEmployeeCode().getValue(),
                sales.getCustomerCode() != null ? sales.getCustomerCode().getCode().getValue() : null,
                sales.getCustomerCode() != null ? sales.getCustomerCode().getBranchNumber() : null,
                sales.getTotalSalesAmount().getAmount(),
                sales.getTotalConsumptionTax().getAmount(),
                sales.getRemarks(),
                sales.getVoucherNumber(),
                sales.getOriginalVoucherNumber(),
                salesLine.getSalesLineNumber(),
                salesLine.getProductCode().getValue(),
                salesLine.getProductName(),
                salesLine.getSalesUnitPrice().getAmount(),
                salesLine.getSalesQuantity().getAmount(),
                salesLine.getShippedQuantity().getAmount(),
                salesLine.getDiscountAmount().getAmount(),
                salesLine.getBillingDate() != null ? salesLine.getBillingDate().getValue() : null,
                salesLine.getBillingNumber() != null ? salesLine.getBillingNumber().getValue() : null,
                salesLine.getBillingDelayType() != null ? salesLine.getBillingDelayType().getCode() : null,
                salesLine.getAutoJournalDate() != null ? salesLine.getAutoJournalDate().getValue() : null,
                salesLine.getTaxRate().getRate()
        );
    }
}
