package com.example.sms.infrastructure.datasource.procurement.payment;

import com.example.sms.domain.model.procurement.payment.PurchasePayment;
import com.example.sms.infrastructure.datasource.autogen.model.支払データ;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 支払エンティティマッパー
 */
@Component
public class PurchasePaymentEntityMapper {

    /**
     * CustomEntityからドメインモデルに変換
     */
    public PurchasePayment toModel(PurchasePaymentCustomEntity entity) {
        if (entity == null) return null;

        return PurchasePayment.builder()
                .paymentNumber(com.example.sms.domain.model.procurement.payment.PurchasePaymentNumber.of(entity.get支払番号()))
                .paymentDate(com.example.sms.domain.model.procurement.payment.PurchasePaymentDate.of(entity.get支払日()))
                .departmentCode(com.example.sms.domain.model.master.department.DepartmentCode.of(entity.get部門コード()))
                .departmentStartDate(entity.get部門開始日())
                .supplierCode(com.example.sms.domain.model.master.partner.supplier.SupplierCode.of(
                        entity.get仕入先コード(), entity.get仕入先枝番()))
                .paymentMethodType(com.example.sms.domain.model.procurement.payment.PurchasePaymentMethodType.of(entity.get支払方法区分()))
                .paymentAmount(com.example.sms.domain.type.money.Money.of(entity.get支払金額()))
                .totalConsumptionTax(com.example.sms.domain.type.money.Money.of(entity.get消費税合計()))
                .paymentCompletedFlag(entity.get支払完了フラグ() != null && entity.get支払完了フラグ() == 1)
                .createdDateTime(entity.get作成日時())
                .createdBy(entity.get作成者名())
                .updatedDateTime(entity.get更新日時())
                .updatedBy(entity.get更新者名())
                .build();
    }

    /**
     * CustomEntityのListからドメインモデルのListに変換
     */
    public List<PurchasePayment> toModelList(List<PurchasePaymentCustomEntity> entities) {
        if (entities == null) return null;
        return entities.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    /**
     * ドメインモデルから自動生成モデルに変換
     */
    public 支払データ toEntity(PurchasePayment model) {
        if (model == null) return null;

        支払データ entity = new 支払データ();
        entity.set支払番号(model.getPaymentNumber().getValue());
        entity.set支払日(model.getPaymentDate().getValue());
        entity.set部門コード(model.getDepartmentCode().getValue());
        entity.set部門開始日(model.getDepartmentStartDate());
        entity.set仕入先コード(model.getSupplierCode().getValue());
        entity.set仕入先枝番(model.getSupplierCode().getBranchNumber());
        entity.set支払方法区分(model.getPaymentMethodType().getValue());
        entity.set支払金額(model.getPaymentAmount().getAmount());
        entity.set消費税合計(model.getTotalConsumptionTax().getAmount());
        entity.set支払完了フラグ(model.getPaymentCompletedFlag() != null && model.getPaymentCompletedFlag() ? 1 : 0);
        return entity;
    }
}
