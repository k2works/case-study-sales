package com.example.sms.infrastructure.datasource.sales.purchase.order;

import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.master.employee.EmployeeCode;
import com.example.sms.domain.model.master.partner.supplier.Supplier;
import com.example.sms.domain.model.master.partner.supplier.SupplierCode;
import com.example.sms.domain.model.master.product.ProductCode;
import com.example.sms.domain.model.sales.order.CompletionFlag;
import com.example.sms.domain.model.sales.purchase.order.*;
import com.example.sms.domain.type.money.Money;
import com.example.sms.domain.type.quantity.Quantity;
import com.example.sms.infrastructure.datasource.autogen.model.発注データ;
import com.example.sms.infrastructure.datasource.autogen.model.発注データ明細;
import com.example.sms.infrastructure.datasource.sales.purchase.order.order_line.PurchaseOrderLineCustomEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 発注エンティティマッパー
 */
@Component
public class PurchaseOrderEntityMapper {

    /**
     * CustomEntityからドメインモデルに変換
     */
    public PurchaseOrder toModel(PurchaseOrderCustomEntity entity) {
        if (entity == null) return null;
        
        List<PurchaseOrderLine> lines = entity.get発注データ明細() != null ?
                entity.get発注データ明細().stream()
                        .map(this::toLineModel)
                        .collect(Collectors.toList()) : List.of();
        
        return PurchaseOrder.of(
                entity.get発注番号(),
                entity.get発注日(),
                entity.get受注番号(),
                entity.get仕入先コード(),
                entity.get仕入先枝番(),
                entity.get発注担当者コード(),
                entity.get指定納期(),
                entity.get倉庫コード(),
                entity.get発注金額合計(),
                entity.get消費税合計(),
                entity.get備考(),
                lines
        );
    }

    /**
     * CustomEntityのListからドメインモデルのListに変換
     */
    public List<PurchaseOrder> toModelList(List<PurchaseOrderCustomEntity> entities) {
        if (entities == null) return null;
        return entities.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    /**
     * 明細CustomEntityからドメインモデルに変換
     */
    public PurchaseOrderLine toLineModel(PurchaseOrderLineCustomEntity entity) {
        if (entity == null) return null;
        
        return PurchaseOrderLine.of(
                entity.get発注番号(),
                entity.get発注行番号(),
                entity.get発注行表示番号(),
                entity.get受注番号(),
                entity.get受注行番号(),
                entity.get商品コード(),
                entity.get商品名(),
                entity.get発注単価(),
                entity.get発注数量(),
                entity.get入荷数量(),
                entity.get完了フラグ()
        );
    }

    /**
     * ドメインモデルから自動生成モデルに変換
     */
    public 発注データ toEntity(PurchaseOrder model) {
        if (model == null) return null;
        
        発注データ entity = new 発注データ();
        entity.set発注番号(model.getPurchaseOrderNumber().getValue());
        entity.set発注日(model.getPurchaseOrderDate().getValue());
        entity.set受注番号(model.getSalesOrderNumber());
        entity.set仕入先コード(model.getSupplierCode().getValue());
        entity.set仕入先枝番(model.getSupplierBranchNumber());
        entity.set発注担当者コード(model.getPurchaseManagerCode().getValue());
        entity.set指定納期(model.getDesignatedDeliveryDate().getValue());
        entity.set倉庫コード(model.getWarehouseCode());
        entity.set発注金額合計(model.getTotalPurchaseAmount().getValue());
        entity.set消費税合計(model.getTotalConsumptionTax().getValue());
        entity.set備考(model.getRemarks());
        return entity;
    }

    /**
     * 明細ドメインモデルから自動生成モデルに変換
     */
    public 発注データ明細 toLineEntity(PurchaseOrderLine model) {
        if (model == null) return null;
        
        発注データ明細 entity = new 発注データ明細();
        entity.set発注番号(model.getPurchaseOrderNumber().getValue());
        entity.set発注行番号(model.getPurchaseOrderLineNumber());
        entity.set発注行表示番号(model.getPurchaseOrderLineDisplayNumber());
        entity.set受注番号(model.getSalesOrderNumber());
        entity.set受注行番号(model.getSalesOrderLineNumber());
        entity.set商品コード(model.getProductCode().getValue());
        entity.set商品名(model.getProductName());
        entity.set発注単価(model.getPurchaseUnitPrice().getValue());
        entity.set発注数量(model.getPurchaseOrderQuantity().getValue());
        entity.set入荷数量(model.getReceivedQuantity().getValue());
        entity.set完了フラグ(model.getCompletionFlag().getValue());
        entity.set作成日時(model.getCreatedDateTime());
        entity.set作成者名(model.getCreatedBy());
        entity.set更新日時(model.getUpdatedDateTime());
        entity.set更新者名(model.getUpdatedBy());
        // entity.setVersion(model.getVersion()); // TODO: 仮実装
        return entity;
    }
    
    /**
     * 仕入先マスタからSupplierに変換
     */
    private Supplier toSupplier(com.example.sms.infrastructure.datasource.master.partner.supplier.SupplierCustomEntity entity) {
        // TODO: 実装が必要
        return null;
    }
    
    /**
     * 社員マスタからEmployeeに変換  
     */
    private Employee toPurchaseManager(com.example.sms.infrastructure.datasource.master.employee.EmployeeCustomEntity entity) {
        // TODO: 実装が必要
        return null;
    }
}