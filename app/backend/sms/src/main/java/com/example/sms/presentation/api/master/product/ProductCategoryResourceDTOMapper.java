package com.example.sms.presentation.api.master.product;

import com.example.sms.domain.model.master.product.Product;
import com.example.sms.domain.model.master.product.ProductCategory;
import com.example.sms.service.master.product.ProductCategoryCriteria;

import java.util.Collections;
import java.util.List;

public class ProductCategoryResourceDTOMapper {

    public static ProductCategory convertToEntity(ProductCategoryResource resource) {
        return ProductCategory.of(
                resource.getProductCategoryCode(),
                resource.getProductCategoryName(),
                resource.getProductCategoryHierarchy(),
                resource.getProductCategoryPath(),
                resource.getLowestLevelDivision()
        );
    }

    public static ProductCategory convertToEntity(String productCategoryCode, ProductCategoryResource resource) {
        return ProductCategory.of(
                productCategoryCode,
                resource.getProductCategoryName(),
                resource.getProductCategoryHierarchy(),
                resource.getProductCategoryPath(),
                resource.getLowestLevelDivision()
        );
    }

    public static ProductCategoryCriteria convertToCriteria(ProductCategoryCriteriaResource resource) {
        return ProductCategoryCriteria.builder()
                .productCategoryCode(resource.getProductCategoryCode())
                .productCategoryName(resource.getProductCategoryName())
                .productCategoryPath(resource.getProductCategoryPath())
                .build();
    }

    public static List<Product> getAddFilteredProducts(ProductCategoryResource resource) {
        return resource.getProducts() == null ? Collections.emptyList() :
                resource.getProducts().stream()
                        .filter(ProductResource::isAddFlag)
                        .map(productResource -> Product.of(
                                productResource.getProductCode(),
                                productResource.getProductFormalName(),
                                productResource.getProductAbbreviation(),
                                productResource.getProductNameKana(),
                                productResource.getProductType(),
                                productResource.getSellingPrice(),
                                productResource.getPurchasePrice(),
                                productResource.getCostOfSales(),
                                productResource.getTaxType(),
                                productResource.getProductClassificationCode(),
                                productResource.getMiscellaneousType(),
                                productResource.getStockManagementTargetType(),
                                productResource.getStockAllocationType(),
                                productResource.getVendorCode(),
                                productResource.getVendorBranchNumber()
                        ))
                        .toList();
    }

    public static List<Product> getDeleteFilteredProducts(ProductCategoryResource resource) {
        return resource.getProducts() == null ? Collections.emptyList() :
                resource.getProducts().stream()
                        .filter(ProductResource::isDeleteFlag)
                        .map(productResource -> Product.of(
                                productResource.getProductCode(),
                                productResource.getProductFormalName(),
                                productResource.getProductAbbreviation(),
                                productResource.getProductNameKana(),
                                productResource.getProductType(),
                                productResource.getSellingPrice(),
                                productResource.getPurchasePrice(),
                                productResource.getCostOfSales(),
                                productResource.getTaxType(),
                                productResource.getProductClassificationCode(),
                                productResource.getMiscellaneousType(),
                                productResource.getStockManagementTargetType(),
                                productResource.getStockAllocationType(),
                                productResource.getVendorCode(),
                                productResource.getVendorBranchNumber()
                        ))
                        .toList();
    }
}