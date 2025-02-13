package com.example.sms.presentation.api.master.partner;

import com.example.sms.domain.model.master.partner.customer.Shipping;
import com.example.sms.domain.model.master.partner.customer.CustomerBillingCategory;
import com.example.sms.domain.model.master.partner.customer.CustomerType;
import com.example.sms.domain.model.master.partner.invoice.ClosingDate;
import com.example.sms.domain.model.master.partner.invoice.PaymentDay;
import com.example.sms.domain.model.master.partner.invoice.PaymentMethod;
import com.example.sms.domain.model.master.partner.invoice.PaymentMonth;
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
    ClosingDate customerClosingDay1;
    PaymentMonth customerPaymentMonth1;
    PaymentDay customerPaymentDay1;
    PaymentMethod customerPaymentMethod1;
    ClosingDate customerClosingDay2;
    PaymentMonth customerPaymentMonth2;
    PaymentDay customerPaymentDay2;
    PaymentMethod customerPaymentMethod2;
    List<Shipping> shippings;
}
