package com.example.sms.presentation.api.master.partner;

import com.example.sms.domain.model.master.partner.invoice.ClosingDate;
import com.example.sms.domain.model.master.partner.invoice.PaymentDay;
import com.example.sms.domain.model.master.partner.invoice.PaymentMethod;
import com.example.sms.domain.model.master.partner.invoice.PaymentMonth;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "仕入先")
public class VendorResource {
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
    ClosingDate vendorClosingDate;
    PaymentMonth vendorPaymentMonth;
    PaymentDay vendorPaymentDate;
    PaymentMethod vendorPaymentMethod;
}
