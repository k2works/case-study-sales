package com.example.sms.infrastructure.datasource.procurement.receipt;

import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.master.partner.supplier.Supplier;
import com.example.sms.domain.model.procurement.receipt.Purchase;
import com.example.sms.domain.model.procurement.receipt.PurchaseLine;
import com.example.sms.infrastructure.datasource.autogen.model.仕入データ;
import com.example.sms.infrastructure.datasource.autogen.model.仕入データ明細;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 仕入エンティティマッパー
 */
@Component
public class PurchaseEntityMapper {

    /**
     * CustomEntityからドメインモデルに変換
     */
    public Purchase toModel(PurchaseCustomEntity entity) {
        if (entity == null) return null;

        List<PurchaseLine> lines = entity.get仕入データ明細() != null ?
                entity.get仕入データ明細().stream()
                        .map(this::toLineModel)
                        .collect(Collectors.toList()) : List.of();

        Purchase purchase = Purchase.of(
                entity.get仕入番号(),
                entity.get仕入日(),
                entity.get仕入先コード(),
                entity.get仕入先枝番(),
                entity.get仕入担当者コード(),
                entity.get開始日(),
                entity.get発注番号(),
                entity.get部門コード(),
                entity.get仕入金額合計(),
                entity.get消費税合計(),
                entity.get備考(),
                lines
        );

        // 関連エンティティの変換は現在のところ使用されていないため、基本のPurchaseを返す
        return purchase;
    }

    /**
     * CustomEntityのListからドメインモデルのListに変換
     */
    public List<Purchase> toModelList(List<PurchaseCustomEntity> entities) {
        if (entities == null) return null;
        return entities.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    /**
     * 明細CustomEntityからドメインモデルに変換
     */
    public PurchaseLine toLineModel(PurchaseLineCustomEntity entity) {
        if (entity == null) return null;

        return PurchaseLine.of(
                entity.get仕入番号(),
                entity.get仕入行番号(),
                entity.get仕入行表示番号(),
                entity.get発注行番号(),
                entity.get商品コード(),
                entity.get倉庫コード(),
                entity.get商品名(),
                entity.get仕入単価(),
                entity.get仕入数量()
        );
    }

    /**
     * ドメインモデルから自動生成モデルに変換
     */
    public 仕入データ toEntity(Purchase model) {
        if (model == null) return null;

        仕入データ entity = new 仕入データ();
        entity.set仕入番号(model.getPurchaseNumber().getValue());
        entity.set仕入日(model.getPurchaseDate().getValue());
        entity.set仕入先コード(model.getSupplierCode().getValue());
        entity.set仕入先枝番(model.getSupplierCode().getBranchNumber());
        entity.set仕入担当者コード(model.getPurchaseManagerCode().getValue());
        entity.set開始日(model.getStartDate());
        entity.set発注番号(model.getPurchaseOrderNumber() != null ? model.getPurchaseOrderNumber().getValue() : null);
        entity.set部門コード(model.getDepartmentCode().getValue());
        entity.set仕入金額合計(model.getTotalPurchaseAmount().getAmount());
        entity.set消費税合計(model.getTotalConsumptionTax().getAmount());
        entity.set備考(model.getRemarks());
        return entity;
    }

    /**
     * 明細ドメインモデルから自動生成モデルに変換
     */
    public 仕入データ明細 toLineEntity(PurchaseLine model) {
        if (model == null) return null;

        仕入データ明細 entity = new 仕入データ明細();
        entity.set仕入番号(model.getPurchaseNumber().getValue());
        entity.set仕入行番号(model.getPurchaseLineNumber());
        entity.set仕入行表示番号(model.getPurchaseLineDisplayNumber());
        entity.set発注行番号(model.getPurchaseOrderLineNumber());
        entity.set商品コード(model.getProductCode().getValue());
        entity.set倉庫コード(model.getWarehouseCode().getValue());
        entity.set商品名(model.getProductName());
        entity.set仕入単価(model.getPurchaseUnitPrice().getAmount());
        entity.set仕入数量(model.getPurchaseQuantity().getAmount());
        entity.set作成日時(model.getCreatedDateTime());
        entity.set作成者名(model.getCreatedBy());
        entity.set更新日時(model.getUpdatedDateTime());
        entity.set更新者名(model.getUpdatedBy());
        return entity;
    }

    /**
     * 仕入先マスタからSupplierに変換
     */
    private Supplier toSupplier(com.example.sms.infrastructure.datasource.master.partner.supplier.SupplierCustomEntity entity) {
        if (entity == null) return null;

        return Supplier.of(
                entity.get仕入先コード(),
                entity.get仕入先枝番(),
                entity.get仕入先名()
        );
    }

    /**
     * 社員マスタからEmployeeに変換
     */
    private Employee toPurchaseManager(com.example.sms.infrastructure.datasource.master.employee.EmployeeCustomEntity entity) {
        if (entity == null) return null;

        return Employee.of(
                entity.get社員コード(),
                entity.get社員名(),
                entity.get社員名カナ(),
                entity.get電話番号(),
                entity.getFax番号(),
                entity.get職種コード()
        );
    }
}
