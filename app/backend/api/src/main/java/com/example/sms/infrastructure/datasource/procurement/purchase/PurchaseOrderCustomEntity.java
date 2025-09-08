package com.example.sms.infrastructure.datasource.procurement.purchase;

import com.example.sms.infrastructure.datasource.autogen.model.*;
import com.example.sms.infrastructure.datasource.master.partner.supplier.SupplierCustomEntity;
import com.example.sms.infrastructure.datasource.master.employee.EmployeeCustomEntity;
import com.example.sms.infrastructure.datasource.procurement.purchase.PurchaseOrderLineCustomEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PurchaseOrderCustomEntity extends 発注データ {
    private SupplierCustomEntity 仕入先マスタ;
    private EmployeeCustomEntity 社員マスタ;
    private List<PurchaseOrderLineCustomEntity> 発注データ明細;
}