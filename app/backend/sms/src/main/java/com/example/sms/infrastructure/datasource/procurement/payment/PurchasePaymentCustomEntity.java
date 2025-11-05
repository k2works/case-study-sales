package com.example.sms.infrastructure.datasource.procurement.payment;

import com.example.sms.infrastructure.datasource.autogen.model.支払データ;
import com.example.sms.infrastructure.datasource.master.partner.supplier.SupplierCustomEntity;
import com.example.sms.infrastructure.datasource.master.department.DepartmentCustomEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchasePaymentCustomEntity extends 支払データ {
    private SupplierCustomEntity 仕入先マスタ;
    private DepartmentCustomEntity 部門マスタ;
}
