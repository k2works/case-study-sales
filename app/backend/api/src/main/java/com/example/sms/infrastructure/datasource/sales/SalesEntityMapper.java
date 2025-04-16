package com.example.sms.infrastructure.datasource.sales;

import com.example.sms.domain.model.sales.Sales;
import com.example.sms.domain.model.sales.SalesLine;
import com.example.sms.infrastructure.datasource.autogen.model.売上データ;
import com.example.sms.infrastructure.datasource.autogen.model.売上データ明細;
import com.example.sms.infrastructure.datasource.autogen.model.売上データ明細Key;
import com.example.sms.infrastructure.datasource.sales.sales_line.SalesLineCustomEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;
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
        salesEntity.set取引先コード(sales.getCustomerCode().getValue());
        salesEntity.set社員コード(sales.getEmployeeCode());
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
        salesEntity.set商品コード(salesLine.getProductCode());
        salesEntity.set商品名(salesLine.getProductName());
        salesEntity.set販売単価(salesLine.getSalesUnitPrice().getAmount());
        salesEntity.set売上数量(salesLine.getSalesQuantity().getAmount());
        salesEntity.set出荷数量(salesLine.getShippedQuantity().getAmount());
        salesEntity.set値引金額(salesLine.getDiscountAmount().getAmount());
        salesEntity.set請求日(salesLine.getBillingDate());
        salesEntity.set請求番号(salesLine.getBillingNumber());
        salesEntity.set請求遅延区分(salesLine.getBillingDelayCategory());
        salesEntity.set自動仕訳日(salesLine.getAutoJournalDate());
        return salesEntity;
    }

    public 売上データ明細Key mapToKey(SalesLine salesLine) {
        売上データ明細Key key = new 売上データ明細Key();
        key.set売上番号(salesLine.getSalesNumber());
        key.set売上行番号(salesLine.getSalesLineNumber());
        return key;
    }

    public Sales mapToDomainModel(SalesCustomEntity salesData) {

        Function<SalesLineCustomEntity, SalesLine> salesLineMapper = e -> {
            if (Objects.isNull(e)) {
                return SalesLine.of(null, null, null, null, null, null, null, null, null, null, null, null);
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
                    e.get自動仕訳日()
            );
        };


        return Sales.of(
                salesData.get売上番号(),
                salesData.get受注番号(),
                salesData.get売上日(),
                salesData.get売上区分(),
                salesData.get部門コード(),
                salesData.get部門開始日(),
                salesData.get取引先コード(),
                salesData.get社員コード(),
                salesData.get赤黒伝票番号(),
                salesData.get元伝票番号(),
                salesData.get備考(),
                salesData.get売上データ明細().stream()
                        .map(salesLineMapper)
                        .toList()
        );
    }
}