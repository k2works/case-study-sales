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
    @Schema(description = "顧客コード")
    String customerCode;
    @NotNull
    @Schema(description = "顧客枝番")
    Integer customerBranchNumber;
    @Schema(description = "顧客種別")
    CustomerType customerType;
    @Schema(description = "請求先コード")
    String billingCode;
    @Schema(description = "請求先枝番")
    Integer billingBranchNumber;
    @Schema(description = "回収先コード")
    String collectionCode;
    @Schema(description = "回収先枝番")
    Integer collectionBranchNumber;
    @Schema(description = "顧客名")
    String customerName;
    @Schema(description = "顧客名カナ")
    String customerNameKana;
    @Schema(description = "会社担当者コード")
    String companyRepresentativeCode;
    @Schema(description = "顧客担当者名")
    String customerRepresentativeName;
    @Schema(description = "顧客部署名")
    String customerDepartmentName;
    @Schema(description = "顧客郵便番号")
    String customerPostalCode;
    @Schema(description = "顧客都道府県")
    String customerPrefecture;
    @Schema(description = "顧客住所1")
    String customerAddress1;
    @Schema(description = "顧客住所2")
    String customerAddress2;
    @Schema(description = "顧客電話番号")
    String customerPhoneNumber;
    @Schema(description = "顧客FAX番号")
    String customerFaxNumber;
    @Schema(description = "顧客メールアドレス")
    String customerEmailAddress;
    @Schema(description = "顧客請求区分")
    CustomerBillingCategory customerBillingType;
    @Schema(description = "顧客締め日1")
    ClosingDate customerClosingDay1;
    @Schema(description = "顧客支払月1")
    PaymentMonth customerPaymentMonth1;
    @Schema(description = "顧客支払日1")
    PaymentDay customerPaymentDay1;
    @Schema(description = "顧客支払方法1")
    PaymentMethod customerPaymentMethod1;
    @Schema(description = "顧客締め日2")
    ClosingDate customerClosingDay2;
    @Schema(description = "顧客支払月2")
    PaymentMonth customerPaymentMonth2;
    @Schema(description = "顧客支払日2")
    PaymentDay customerPaymentDay2;
    @Schema(description = "顧客支払方法2")
    PaymentMethod customerPaymentMethod2;
    @Schema(description = "出荷先リスト")
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
