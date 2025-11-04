package com.example.sms.service.master.warehouse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WarehouseCriteria {
    private String warehouseName;
    private String warehouseCode;
}