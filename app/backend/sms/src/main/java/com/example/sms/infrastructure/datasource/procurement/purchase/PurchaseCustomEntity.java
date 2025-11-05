package com.example.sms.infrastructure.datasource.procurement.purchase;

import com.example.sms.infrastructure.datasource.autogen.model.仕入データ;
import com.example.sms.infrastructure.datasource.master.partner.supplier.SupplierCustomEntity;
import com.example.sms.infrastructure.datasource.master.employee.EmployeeCustomEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PurchaseCustomEntity extends 仕入データ {
    private SupplierCustomEntity 仕入先マスタ;
    private EmployeeCustomEntity 社員マスタ;
    private List<PurchaseLineCustomEntity> 仕入データ明細;
}
