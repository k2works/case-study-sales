package com.example.sms.presentation.api.master.product;

import com.example.sms.domain.model.master.product.*;
import com.example.sms.presentation.ResourceDTOMapperHelper;
import com.example.sms.service.master.product.ProductCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ProductResourceDTOMapper {

    public static Product convertToEntity(ProductResource resource) {
        Product product = Product.of(
                resource.getProductCode(),
                resource.getProductFormalName(),
                resource.getProductAbbreviation(),
                resource.getProductNameKana(),
                resource.getProductType(),
                resource.getSellingPrice(),
                resource.getPurchasePrice(),
                resource.getCostOfSales(),
                resource.getTaxType(),
                resource.getProductClassificationCode(),
                resource.getMiscellaneousType(),
                resource.getStockManagementTargetType(),
                resource.getStockAllocationType(),
                resource.getVendorCode(),
                resource.getVendorBranchNumber()
        );

        List<SubstituteProduct> substituteProducts = Optional.ofNullable(resource.getSubstituteProduct())
                .orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .map(ProductResourceDTOMapper::convertToSubstituteProduct)
                .toList();

        List<Bom> boms = Optional.ofNullable(resource.getBoms())
                .orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .map(ProductResourceDTOMapper::convertToBom)
                .toList();

        List<CustomerSpecificSellingPrice> customerSpecificSellingPrices = Optional.ofNullable(resource.getCustomerSpecificSellingPrices())
                .orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .map(ProductResourceDTOMapper::convertToCustomerSpecificSellingPrice)
                .toList();

        product = Product.of(
                product,
                substituteProducts,
                boms,
                customerSpecificSellingPrices
        );
        return product;
    }

    public static SubstituteProduct convertToSubstituteProduct(SubstituteProductResource resource) {
        return SubstituteProduct.of(
                resource.getProductCode(),
                resource.getSubstituteProductCode(),
                resource.getPriority()
        );
    }

    public static Bom convertToBom(BomResource resource) {
        return Bom.of(
                resource.getProductCode(),
                resource.getComponentCode(),
                resource.getComponentQuantity().getAmount()
        );
    }

    public static CustomerSpecificSellingPrice convertToCustomerSpecificSellingPrice(CustomerSpecificSellingPriceResource resource) {
        return CustomerSpecificSellingPrice.of(
                resource.getProductCode(),
                resource.getCustomerCode(),
                resource.getSellingPrice().getAmount()
        );
    }

    public static ProductCriteria convertToCriteria(ProductCriteriaResource resource) {
        return ProductCriteria.builder()
                .productCode(resource.getProductCode())
                .productNameFormal(resource.getProductNameFormal())
                .productNameAbbreviation(resource.getProductNameAbbreviation())
                .productNameKana(resource.getProductNameKana())
                .productCategoryCode(resource.getProductCategoryCode())
                .vendorCode(resource.getVendorCode())
                .productType(ResourceDTOMapperHelper.mapStringToCode(resource.getProductType(), ProductType::getCodeByName))
                .taxType(ResourceDTOMapperHelper.mapStringToCode(resource.getTaxType(), TaxType::getCodeByName))
                .miscellaneousType(ResourceDTOMapperHelper.mapStringToCode(resource.getMiscellaneousType(), MiscellaneousType::getCodeByName))
                .stockManagementTargetType(ResourceDTOMapperHelper.mapStringToCode(resource.getStockManagementTargetType(), StockManagementTargetType::getCodeByName))
                .stockAllocationType(ResourceDTOMapperHelper.mapStringToCode(resource.getStockAllocationType(), StockAllocationType::getCodeByName))
                .build();
    }

}
