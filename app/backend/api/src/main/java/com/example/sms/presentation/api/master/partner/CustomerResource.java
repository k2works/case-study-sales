package com.example.sms.presentation.api.master.partner;

import com.example.sms.domain.model.master.partner.customer.Shipping;
import com.example.sms.domain.type.partner.CustomerBillingCategory;
import com.example.sms.domain.type.partner.CustomerType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Schema(description = "顧客")
public class CustomerResource implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    String customerCode;
    @NotNull
    Integer customerBranchNumber;
    CustomerType customerType;
    String billingCode;
    Integer billingBranchNumber;
    String collectionCode;
    Integer collectionBranchNumber;
    String customerName;
    String customerNameKana;
    String companyRepresentativeCode;
    String customerRepresentativeName;
    String customerDepartmentName;
    String customerPostalCode;
    String customerPrefecture;
    String customerAddress1;
    String customerAddress2;
    String customerPhoneNumber;
    String customerFaxNumber;
    String customerEmailAddress;
    CustomerBillingCategory customerBillingType;
    Integer customerClosingDay1;
    Integer customerPaymentMonth1;
    Integer customerPaymentDay1;
    Integer customerPaymentMethod1;
    Integer customerClosingDay2;
    Integer customerPaymentMonth2;
    Integer customerPaymentDay2;
    Integer customerPaymentMethod2;
    List<Shipping> shippings;
}
