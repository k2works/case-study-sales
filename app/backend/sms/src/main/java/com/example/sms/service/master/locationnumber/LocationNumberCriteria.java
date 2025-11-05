package com.example.sms.service.master.locationnumber;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationNumberCriteria {
    private String warehouseCode;
    private String locationNumberCode;
    private String productCode;
}