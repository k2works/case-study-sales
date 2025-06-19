package com.example.sms.infrastructure.datasource.master.payment.account.incoming;

import com.example.sms.domain.model.master.payment.account.incoming.PaymentAccount;
import com.example.sms.infrastructure.datasource.autogen.model.入金口座マスタ;
import org.springframework.stereotype.Component;

/**
 * 入金データと入金口座マスタのエンティティマッパー
 */
@Component
public class PaymentAccountEntityMapper {

    /**
     * 入金口座マスタドメインモデルをエンティティに変換する
     *
     * @param paymentAccount 入金口座マスタドメインモデル
     * @return 入金口座マスタエンティティ
     */
    public 入金口座マスタ mapToEntity(PaymentAccount paymentAccount) {
        if (paymentAccount == null) {
            return new 入金口座マスタ();
        }

        入金口座マスタ entity = new 入金口座マスタ();
        entity.set入金口座コード(paymentAccount.getAccountCode().getValue());
        entity.set入金口座名(paymentAccount.getAccountName());
        entity.set適用開始日(paymentAccount.getStartDate());
        entity.set適用終了日(paymentAccount.getEndDate());
        entity.set適用開始後入金口座名(paymentAccount.getAccountNameAfterStart());
        entity.set入金口座区分(paymentAccount.getAccountType().getCode());
        entity.set入金口座番号(paymentAccount.getAccountNumber());
        entity.set銀行口座種別(paymentAccount.getBankAccountType().getCode());
        entity.set口座名義人(paymentAccount.getAccountHolder());
        entity.set部門コード(paymentAccount.getDepartmentId().getDeptCode().getValue());
        entity.set部門開始日(paymentAccount.getDepartmentId().getDepartmentStartDate().getValue());
        entity.set全銀協銀行コード(paymentAccount.getBankCode());
        entity.set全銀協支店コード(paymentAccount.getBranchCode());

        return entity;
    }

    /**
     * 入金口座マスタエンティティをドメインモデルに変換する
     *
     * @param entity 入金口座マスタエンティティ
     * @return 入金口座マスタドメインモデル
     */
    public PaymentAccount mapToDomainModel(PaymentAccountCustomEntity entity) {
        if (entity == null) {
            return null;
        }

        return PaymentAccount.of(
                entity.get入金口座コード(),
                entity.get入金口座名(),
                entity.get適用開始日(),
                entity.get適用終了日(),
                entity.get適用開始後入金口座名(),
                entity.get入金口座区分(),
                entity.get入金口座番号(),
                entity.get銀行口座種別(),
                entity.get口座名義人(),
                entity.get部門コード(),
                entity.get部門開始日(),
                entity.get全銀協銀行コード(),
                entity.get全銀協支店コード()
        );
    }
}
