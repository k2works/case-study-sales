package com.example.sms.infrastructure.datasource.sales.payment;

import com.example.sms.domain.model.master.partner.customer.Customer;
import com.example.sms.domain.model.master.payment.account.incoming.PaymentAccount;
import com.example.sms.domain.model.sales.payment.PaymentReceived;
import com.example.sms.infrastructure.datasource.autogen.model.入金口座マスタ;
import com.example.sms.infrastructure.datasource.autogen.model.入金データ;
import com.example.sms.infrastructure.datasource.master.partner.customer.CustomerCustomEntity;
import com.example.sms.infrastructure.datasource.master.payment.account.incoming.PaymentAccountCustomEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

/**
 * 入金データと入金口座マスタのエンティティマッパー
 */
@Component
public class PaymentReceivedEntityMapper {

    /**
     * 入金データドメインモデルをエンティティに変換する
     *
     * @param paymentReceived 入金データドメインモデル
     * @return 入金データエンティティ
     */
    public 入金データ mapToEntity(PaymentReceived paymentReceived) {
        if (paymentReceived == null) {
            return new 入金データ();
        }

        入金データ entity = new 入金データ();
        entity.set入金番号(paymentReceived.getPaymentNumber().getValue());
        entity.set入金日(paymentReceived.getPaymentDate());
        entity.set部門コード(paymentReceived.getDepartmentId().getDeptCode().getValue());
        entity.set開始日(paymentReceived.getDepartmentId().getDepartmentStartDate().getValue());
        entity.set顧客コード(paymentReceived.getCustomerCode().getCode().getValue());
        entity.set顧客枝番(paymentReceived.getCustomerCode().getBranchNumber());
        entity.set支払方法区分(paymentReceived.getPaymentMethodType().getCode());
        entity.set入金口座コード(paymentReceived.getPaymentAccountCode());
        entity.set入金金額(paymentReceived.getPaymentAmount().getAmount());
        entity.set消込金額(paymentReceived.getOffsetAmount().getAmount());

        return entity;
    }

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
     * 入金データエンティティをドメインモデルに変換する
     *
     * @param entity 入金データエンティティ
     * @return 入金データドメインモデル
     */
    public PaymentReceived mapToDomainModel(PaymentReceivedCustomEntity entity) {
        if (entity == null) {
            return null;
        }

        Function<CustomerCustomEntity, Customer> mapToCustomer = e -> {
            if (e == null) {
                return null;
            }
            return Customer.of(
                    e.get顧客コード(),
                    e.get顧客枝番(),
                    e.get顧客区分(),
                    e.get請求先コード(),
                    e.get請求先枝番(),
                    e.get回収先コード(),
                    e.get回収先枝番(),
                    e.get顧客名(),
                    e.get顧客名カナ(),
                    e.get自社担当者コード(),
                    e.get顧客担当者名(),
                    e.get顧客部門名(),
                    e.get顧客郵便番号(),
                    e.get顧客都道府県(),
                    e.get顧客住所１(),
                    e.get顧客住所２(),
                    e.get顧客電話番号(),
                    e.get顧客ｆａｘ番号(),
                    e.get顧客メールアドレス(),
                    e.get顧客請求区分(),
                    e.get顧客締日１(),
                    e.get顧客支払月１(),
                    e.get顧客支払日１(),
                    e.get顧客支払方法１(),
                    e.get顧客締日２(),
                    e.get顧客支払月２(),
                    e.get顧客支払日２(),
                    e.get顧客支払方法２()
            );
        };

        Function<PaymentAccountCustomEntity, PaymentAccount> mapToPaymentAccount = e -> {
            if (e == null) {
                return null;
            }
            return PaymentAccount.of(
                    e.get入金口座コード(),
                    e.get入金口座名(),
                    e.get適用開始日(),
                    e.get適用終了日(),
                    e.get適用開始後入金口座名(),
                    e.get入金口座区分(),
                    e.get入金口座番号(),
                    e.get銀行口座種別(),
                    e.get口座名義人(),
                    e.get部門コード(),
                    e.get部門開始日(),
                    e.get全銀協銀行コード(),
                    e.get全銀協支店コード()
            );
        };

        PaymentReceived paymentReceived = PaymentReceived.of(
                entity.get入金番号(),
                entity.get入金日(),
                entity.get部門コード(),
                entity.get開始日(),
                entity.get顧客コード(),
                entity.get顧客枝番(),
                entity.get支払方法区分(),
                entity.get入金口座コード(),
                entity.get入金金額(),
                entity.get消込金額()
        );

        return PaymentReceived.of(
                paymentReceived,
                Optional.ofNullable(entity.get顧客マスタ()).map(mapToCustomer).orElse(null),
                Optional.ofNullable(entity.get入金口座マスタ()).map(mapToPaymentAccount).orElse(null)
        );
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
