package com.example.sms.presentation.api.master.partner;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Schema(description = "仕入先")
public class VendorResource implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    String vendorCode;
    @NotNull
    Integer vendorBranchNumber;
    String vendorName;
    String vendorNameKana;
    String vendorContactName;
    String vendorDepartmentName;
    String vendorPostalCode;
    String vendorPrefecture;
    String vendorAddress1;
    String vendorAddress2;
    String vendorPhoneNumber;
    String vendorFaxNumber;
    String vendorEmailAddress;
    Integer vendorClosingDate;
    Integer vendorPaymentMonth;
    Integer vendorPaymentDate;
    Integer vendorPaymentMethod;
}
