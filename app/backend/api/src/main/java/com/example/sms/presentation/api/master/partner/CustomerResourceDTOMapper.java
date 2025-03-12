package com.example.sms.presentation.api.master.partner;

import com.example.sms.domain.model.master.partner.customer.*;
import com.example.sms.domain.type.address.Address;
import com.example.sms.presentation.ResourceDTOMapperHelper;
import com.example.sms.service.master.partner.CustomerCriteria;

import java.util.Collections;
import java.util.Optional;

public class CustomerResourceDTOMapper {

    public static Customer convertToEntity(CustomerResource resource) {
        Customer customer = Customer.of(
                resource.getCustomerCode(),
                resource.getCustomerBranchNumber(),
                resource.getCustomerType().getValue(),
                resource.getBillingCode(),
                resource.getBillingBranchNumber(),
                resource.getCollectionCode(),
                resource.getCollectionBranchNumber(),
                resource.getCustomerName(),
                resource.getCustomerNameKana(),
                resource.getCompanyRepresentativeCode(),
                resource.getCustomerRepresentativeName(),
                resource.getCustomerDepartmentName(),
                resource.getCustomerPostalCode(),
                resource.getCustomerPrefecture(),
                resource.getCustomerAddress1(),
                resource.getCustomerAddress2(),
                resource.getCustomerPhoneNumber(),
                resource.getCustomerFaxNumber(),
                resource.getCustomerEmailAddress(),
                resource.getCustomerBillingType().getValue(),
                resource.getCustomerClosingDay1().getValue(),
                resource.getCustomerPaymentMonth1().getValue(),
                resource.getCustomerPaymentDay1().getValue(),
                resource.getCustomerPaymentMethod1().getValue(),
                resource.getCustomerClosingDay2().getValue(),
                resource.getCustomerPaymentMonth2().getValue(),
                resource.getCustomerPaymentDay2().getValue(),
                resource.getCustomerPaymentMethod2().getValue()
        );

        return Customer.of(
                customer,
                Optional.ofNullable(resource.getShippings())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(CustomerResourceDTOMapper::convertToShipping)
                        .toList()
        );
    }

    private static Shipping convertToShipping(ShippingResource shippingResource) {
        return Shipping.of(
                shippingResource.getShippingCode(),
                shippingResource.getDestinationName(),
                shippingResource.getRegionCode(),
                Address.of(
                        shippingResource.getShippingAddress().getPostalCode().getValue(),
                        shippingResource.getShippingAddress().getPrefecture().name(),
                        shippingResource.getShippingAddress().getAddress1(),
                        shippingResource.getShippingAddress().getAddress2()
                )
        );
    }

    public static CustomerCriteria convertToCriteria(CustomerCriteriaResource resource) {
        return CustomerCriteria.builder()
                .customerCode(resource.getCustomerCode())
                .customerName(resource.getCustomerName())
                .customerNameKana(resource.getCustomerNameKana())
                .customerType(ResourceDTOMapperHelper.mapStringToCode(resource.getCustomerType(), CustomerType::getCodeByName))
                .companyRepresentativeCode(resource.getCompanyRepresentativeCode())
                .customerRepresentativeName(resource.getCustomerRepresentativeName())
                .customerDepartmentName(resource.getCustomerDepartmentName())
                .postalCode(resource.getPostalCode())
                .prefecture(resource.getPrefecture())
                .address1(resource.getAddress1())
                .address2(resource.getAddress2())
                .customerPhoneNumber(resource.getCustomerPhoneNumber())
                .customerFaxNumber(resource.getCustomerFaxNumber())
                .customerEmailAddress(resource.getCustomerEmailAddress())
                .customerBillingCategory(ResourceDTOMapperHelper.mapStringToCode(resource.getCustomerBillingCategory(), CustomerBillingCategory::getCodeByName))
                .build();
    }

}
