package com.example.sms.presentation.api.master.partner;

import com.example.sms.domain.model.master.partner.customer.Customer;
import com.example.sms.domain.model.master.partner.customer.CustomerBillingCategory;
import com.example.sms.domain.model.master.partner.customer.CustomerType;
import com.example.sms.domain.model.master.partner.invoice.ClosingDate;
import com.example.sms.domain.model.master.partner.invoice.PaymentDay;
import com.example.sms.domain.model.master.partner.invoice.PaymentMethod;
import com.example.sms.domain.model.master.partner.invoice.PaymentMonth;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Schema(description = "顧客")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResource {
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
    List<ShippingResource> shippings;

    public static CustomerResource from(Customer customer) {
        return CustomerResource.builder()
                .customerCode(customer.getCustomerCode().getCode().getValue())
                .customerBranchNumber(customer.getCustomerCode().getBranchNumber())
                .customerType(customer.getCustomerType())
                .billingCode(customer.getBillingCode().getCode().getValue())
                .billingBranchNumber(customer.getBillingCode().getBranchNumber())
                .collectionCode(customer.getCollectionCode().getCode().getValue())
                .collectionBranchNumber(customer.getCollectionCode().getBranchNumber())
                .customerName(customer.getCustomerName().getValue().getName())
                .customerNameKana(customer.getCustomerName().getValue().getNameKana())
                .companyRepresentativeCode(customer.getCompanyRepresentativeCode())
                .customerRepresentativeName(customer.getCustomerRepresentativeName())
                .customerDepartmentName(customer.getCustomerDepartmentName())
                .customerPostalCode(customer.getCustomerAddress().getPostalCode().getValue())
                .customerPrefecture(customer.getCustomerAddress().getPrefecture().name())
                .customerAddress1(customer.getCustomerAddress().getAddress1())
                .customerAddress2(customer.getCustomerAddress().getAddress2())
                .customerPhoneNumber(customer.getCustomerPhoneNumber().getValue())
                .customerFaxNumber(customer.getCustomerFaxNumber().getValue())
                .customerEmailAddress(customer.getCustomerEmailAddress().getValue())
                .customerBillingType(customer.getInvoice().getCustomerBillingCategory())
                .customerClosingDay1(customer.getInvoice().getClosingInvoice1().getClosingDay())
                .customerPaymentMonth1(customer.getInvoice().getClosingInvoice1().getPaymentMonth())
                .customerPaymentDay1(customer.getInvoice().getClosingInvoice1().getPaymentDay())
                .customerPaymentMethod1(customer.getInvoice().getClosingInvoice1().getPaymentMethod())
                .customerClosingDay2(customer.getInvoice().getClosingInvoice2().getClosingDay())
                .customerPaymentMonth2(customer.getInvoice().getClosingInvoice2().getPaymentMonth())
                .customerPaymentDay2(customer.getInvoice().getClosingInvoice2().getPaymentDay())
                .customerPaymentMethod2(customer.getInvoice().getClosingInvoice2().getPaymentMethod())
                .shippings(ShippingResource.from(customer.getShippings()))
                .build();
    }

    public static List<CustomerResource> from(List<Customer> customers) {
        return customers.stream()
                .map(CustomerResource::from)
                .toList();
    }
}
