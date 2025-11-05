package com.example.sms.infrastructure.datasource.sales.payment;

import com.example.sms.infrastructure.datasource.autogen.model.入金データ;
import com.example.sms.infrastructure.datasource.master.department.DepartmentCustomEntity;
import com.example.sms.infrastructure.datasource.master.partner.customer.CustomerCustomEntity;
import com.example.sms.infrastructure.datasource.master.payment.account.incoming.PaymentAccountCustomEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 入金データのカスタムエンティティ
 * 自動生成されたモデルを拡張し、追加の関連エンティティを保持する
 */
@Getter
@Setter
public class PaymentReceivedCustomEntity extends 入金データ {
    // 関連エンティティ
    private DepartmentCustomEntity 部門マスタ;
    private CustomerCustomEntity 顧客マスタ;
    private PaymentAccountCustomEntity 入金口座マスタ;
}
