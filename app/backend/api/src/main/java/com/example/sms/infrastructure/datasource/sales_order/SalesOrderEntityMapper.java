package com.example.sms.infrastructure.datasource.sales_order;

import com.example.sms.domain.model.sales_order.SalesOrder;
import com.example.sms.domain.model.sales_order.SalesOrderLine;
import com.example.sms.infrastructure.datasource.autogen.model.受注データ;
import com.example.sms.infrastructure.datasource.autogen.model.受注データ明細;
import com.example.sms.infrastructure.datasource.autogen.model.受注データ明細Key;
import com.example.sms.infrastructure.datasource.system.download.SalesOrderDownloadCSV;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class SalesOrderEntityMapper {
    public 受注データ mapToEntity(SalesOrder salesOrder) {
        受注データ salesOrderEntity = new 受注データ();
        salesOrderEntity.set受注番号(salesOrder.getOrderNumber().getValue());
        salesOrderEntity.set受注日(salesOrder.getOrderDate().getValue());
        salesOrderEntity.set部門コード(salesOrder.getDepartmentCode());
        salesOrderEntity.set部門開始日(salesOrder.getDepartmentStartDate());
        salesOrderEntity.set顧客コード(salesOrder.getCustomerCode());
        salesOrderEntity.set顧客枝番(salesOrder.getCustomerBranchNumber());
        salesOrderEntity.set社員コード(salesOrder.getEmployeeCode());
        salesOrderEntity.set希望納期(salesOrder.getDesiredDeliveryDate());
        salesOrderEntity.set客先注文番号(salesOrder.getCustomerOrderNumber());
        salesOrderEntity.set倉庫コード(salesOrder.getWarehouseCode());
        salesOrderEntity.set受注金額合計(salesOrder.getTotalOrderAmount());
        salesOrderEntity.set消費税合計(salesOrder.getTotalConsumptionTax());
        salesOrderEntity.set備考(salesOrder.getRemarks());

        return salesOrderEntity;
    }

    public 受注データ明細 mapToEntity(受注データ明細Key key, SalesOrderLine salesOrderLine) {
        受注データ明細 salesOrderLineEntity = new 受注データ明細();
        salesOrderLineEntity.set受注番号(key.get受注番号());
        salesOrderLineEntity.set受注行番号(key.get受注行番号());
        salesOrderLineEntity.set商品コード(salesOrderLine.getProductCode());
        salesOrderLineEntity.set商品名(salesOrderLine.getProductName());
        salesOrderLineEntity.set販売単価(salesOrderLine.getSalesUnitPrice());
        salesOrderLineEntity.set受注数量(salesOrderLine.getOrderQuantity());
        salesOrderLineEntity.set消費税率(salesOrderLine.getTaxRate());
        salesOrderLineEntity.set引当数量(salesOrderLine.getAllocationQuantity());
        salesOrderLineEntity.set出荷指示数量(salesOrderLine.getShipmentInstructionQuantity());
        salesOrderLineEntity.set出荷済数量(salesOrderLine.getShippedQuantity());
        salesOrderLineEntity.set完了フラグ(salesOrderLine.getCompletionFlag());
        salesOrderLineEntity.set値引金額(salesOrderLine.getDiscountAmount());
        salesOrderLineEntity.set納期(salesOrderLine.getDeliveryDate());

        return salesOrderLineEntity;
    }

    public SalesOrder mapToDomainModel(SalesOrderCustomEntity salesOrderCustomEntity) {
        Function<受注データ明細, SalesOrderLine> salesOrderLineMapper = e -> SalesOrderLine.of(
                e.get受注番号(),
                e.get受注行番号(),
                e.get商品コード(),
                e.get商品名(),
                e.get販売単価(),
                e.get受注数量(),
                e.get消費税率(),
                e.get引当数量(),
                e.get出荷指示数量(),
                e.get出荷済数量(),
                e.get完了フラグ(),
                e.get値引金額(),
                e.get納期()
        );

        return SalesOrder.of(
                salesOrderCustomEntity.get受注番号(),
                salesOrderCustomEntity.get受注日(),
                salesOrderCustomEntity.get部門コード(),
                salesOrderCustomEntity.get部門開始日(),
                salesOrderCustomEntity.get顧客コード(),
                salesOrderCustomEntity.get顧客枝番(),
                salesOrderCustomEntity.get社員コード(),
                salesOrderCustomEntity.get希望納期(),
                salesOrderCustomEntity.get客先注文番号(),
                salesOrderCustomEntity.get倉庫コード(),
                salesOrderCustomEntity.get受注金額合計(),
                salesOrderCustomEntity.get消費税合計(),
                salesOrderCustomEntity.get備考(),
                salesOrderCustomEntity.get受注データ明細().stream().map(salesOrderLineMapper).toList()
        );
    }

    public SalesOrderDownloadCSV mapToCsvModel(SalesOrder salesOrder, SalesOrderLine salesOrderLine) {
        return new SalesOrderDownloadCSV(
                salesOrder.getOrderNumber().getValue(),
                salesOrder.getOrderDate().getValue(),
                salesOrder.getDepartmentCode(),
                salesOrder.getDepartmentStartDate(),
                salesOrder.getCustomerCode(),
                salesOrder.getCustomerBranchNumber(),
                salesOrder.getEmployeeCode(),
                salesOrder.getDesiredDeliveryDate(),
                salesOrder.getCustomerOrderNumber(),
                salesOrder.getWarehouseCode(),
                salesOrder.getTotalOrderAmount(),
                salesOrder.getTotalConsumptionTax(),
                salesOrder.getRemarks(),
                salesOrderLine.getOrderLineNumber(),
                salesOrderLine.getProductCode(),
                salesOrderLine.getProductName(),
                salesOrderLine.getSalesUnitPrice(),
                salesOrderLine.getOrderQuantity(),
                salesOrderLine.getTaxRate(),
                salesOrderLine.getAllocationQuantity(),
                salesOrderLine.getShipmentInstructionQuantity(),
                salesOrderLine.getShippedQuantity(),
                salesOrderLine.getCompletionFlag(),
                salesOrderLine.getDiscountAmount(),
                salesOrderLine.getDeliveryDate()
        );
    }
}
