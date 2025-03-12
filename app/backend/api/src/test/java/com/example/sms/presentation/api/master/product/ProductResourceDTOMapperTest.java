package com.example.sms.presentation.api.master.product;

import com.example.sms.domain.model.master.product.*;
import com.example.sms.domain.type.money.Money;
import com.example.sms.domain.type.quantity.Quantity;
import com.example.sms.service.master.product.ProductCriteria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("商品DTOマッパーテスト")
class ProductResourceDTOMapperTest {

    @Test
    @DisplayName("商品リソースを商品エンティティに変換する")
    void testConvertToEntity_validResource_shouldReturnProduct() {
        // Arrange
        String productCode = "P001";
        String productFormalName = "テスト商品";
        String productAbbreviation = "テスト";
        String productNameKana = "テストショウヒン";
        ProductType productType = ProductType.商品;
        Integer sellingPrice = 1000;
        Integer purchasePrice = 800;
        Integer costOfSales = 800;
        TaxType taxType = TaxType.外税;
        String productClassificationCode = "PC001";
        MiscellaneousType miscellaneousType = MiscellaneousType.適用;
        StockManagementTargetType stockManagementTargetType = StockManagementTargetType.対象;
        StockAllocationType stockAllocationType = StockAllocationType.引当済;
        String vendorCode = "001";
        Integer vendorBranchNumber = 1;

        ProductResource resource = ProductResource.builder()
                .productCode(productCode)
                .productFormalName(productFormalName)
                .productAbbreviation(productAbbreviation)
                .productNameKana(productNameKana)
                .productType(productType)
                .sellingPrice(sellingPrice)
                .purchasePrice(purchasePrice)
                .costOfSales(costOfSales)
                .taxType(taxType)
                .productClassificationCode(productClassificationCode)
                .miscellaneousType(miscellaneousType)
                .stockManagementTargetType(stockManagementTargetType)
                .stockAllocationType(stockAllocationType)
                .vendorCode(vendorCode)
                .vendorBranchNumber(vendorBranchNumber)
                .build();

        // Act
        Product product = ProductResourceDTOMapper.convertToEntity(resource);

        // Assert
        assertNotNull(product);
        assertEquals(productCode, product.getProductCode().getValue());
        assertEquals(productFormalName, product.getProductName().getProductFormalName());
        assertEquals(productAbbreviation, product.getProductName().getProductAbbreviation());
        assertEquals(productNameKana, product.getProductName().getProductNameKana());
        assertEquals(productType, product.getProductType());
        assertEquals(sellingPrice, product.getSellingPrice().getAmount());
        assertEquals(purchasePrice, product.getPurchasePrice().getAmount());
        assertEquals(costOfSales, product.getCostOfSales().getAmount());
        assertEquals(taxType, product.getTaxType());
        assertEquals(productClassificationCode, product.getProductCategoryCode().getValue());
        assertEquals(miscellaneousType, product.getMiscellaneousType());
        assertEquals(stockManagementTargetType, product.getStockManagementTargetType());
        assertEquals(stockAllocationType, product.getStockAllocationType());
        assertEquals(vendorCode, product.getVendorCode().getCode().getValue());
        assertEquals(vendorBranchNumber, product.getVendorCode().getBranchNumber());
        assertTrue(product.getSubstituteProduct().isEmpty());
        assertTrue(product.getBoms().isEmpty());
        assertTrue(product.getCustomerSpecificSellingPrices().isEmpty());
    }

    @Test
    @DisplayName("商品リソースと関連エンティティを商品エンティティに変換する")
    void testConvertToEntity_withRelatedEntities_shouldReturnProduct() {
        // Arrange
        String productCode = "P001";
        String productFormalName = "テスト商品";
        String productAbbreviation = "テスト";
        String productNameKana = "テストショウヒン";
        ProductType productType = ProductType.商品;
        Integer sellingPrice = 1000;
        Integer purchasePrice = 800;
        Integer costOfSales = 800;
        TaxType taxType = TaxType.外税;
        String productClassificationCode = "PC001";
        MiscellaneousType miscellaneousType = MiscellaneousType.適用;
        StockManagementTargetType stockManagementTargetType = StockManagementTargetType.対象;
        StockAllocationType stockAllocationType = StockAllocationType.引当済;
        String vendorCode = "001";
        Integer vendorBranchNumber = 1;

        // Create substitute product resource
        String substituteProductCode = "P002";
        Integer priority = 1;
        SubstituteProductResource substituteProductResource = SubstituteProductResource.builder()
                .productCode(productCode)
                .substituteProductCode(substituteProductCode)
                .priority(priority)
                .build();

        // Create BOM resource
        String componentCode = "C001";
        Quantity componentQuantity = Quantity.of(2);
        BomResource bomResource = BomResource.builder()
                .productCode(productCode)
                .componentCode(componentCode)
                .componentQuantity(componentQuantity)
                .build();

        // Create customer specific selling price resource
        String customerCode = "C001";
        Money customerSellingPrice = Money.of(900);
        CustomerSpecificSellingPriceResource customerSpecificSellingPriceResource = CustomerSpecificSellingPriceResource.builder()
                .productCode(productCode)
                .customerCode(customerCode)
                .sellingPrice(customerSellingPrice)
                .build();

        ProductResource resource = ProductResource.builder()
                .productCode(productCode)
                .productFormalName(productFormalName)
                .productAbbreviation(productAbbreviation)
                .productNameKana(productNameKana)
                .productType(productType)
                .sellingPrice(sellingPrice)
                .purchasePrice(purchasePrice)
                .costOfSales(costOfSales)
                .taxType(taxType)
                .productClassificationCode(productClassificationCode)
                .miscellaneousType(miscellaneousType)
                .stockManagementTargetType(stockManagementTargetType)
                .stockAllocationType(stockAllocationType)
                .vendorCode(vendorCode)
                .vendorBranchNumber(vendorBranchNumber)
                .substituteProduct(List.of(substituteProductResource))
                .boms(List.of(bomResource))
                .customerSpecificSellingPrices(List.of(customerSpecificSellingPriceResource))
                .build();

        // Act
        Product product = ProductResourceDTOMapper.convertToEntity(resource);

        // Assert
        assertNotNull(product);
        assertEquals(productCode, product.getProductCode().getValue());
        assertEquals(1, product.getSubstituteProduct().size());
        assertEquals(1, product.getBoms().size());
        assertEquals(1, product.getCustomerSpecificSellingPrices().size());

        SubstituteProduct substituteProduct = product.getSubstituteProduct().get(0);
        assertEquals(productCode, substituteProduct.getProductCode().getValue());
        assertEquals(substituteProductCode, substituteProduct.getSubstituteProductCode().getValue());
        assertEquals(priority, substituteProduct.getPriority());

        Bom bom = product.getBoms().get(0);
        assertEquals(productCode, bom.getProductCode().getValue());
        assertEquals(componentCode, bom.getComponentCode().getValue());
        assertEquals(componentQuantity.getAmount(), bom.getComponentQuantity().getAmount());

        CustomerSpecificSellingPrice customerSpecificSellingPrice = product.getCustomerSpecificSellingPrices().get(0);
        assertEquals(productCode, customerSpecificSellingPrice.getProductCode().getValue());
        assertEquals(customerCode, customerSpecificSellingPrice.getCustomerCode());
        assertEquals(customerSellingPrice.getAmount(), customerSpecificSellingPrice.getSellingPrice().getAmount());
    }

    @Test
    @DisplayName("代替商品リソースを代替商品エンティティに変換する")
    void testConvertToSubstituteProduct_validResource_shouldReturnSubstituteProduct() {
        // Arrange
        String productCode = "P001";
        String substituteProductCode = "P002";
        Integer priority = 1;

        SubstituteProductResource resource = SubstituteProductResource.builder()
                .productCode(productCode)
                .substituteProductCode(substituteProductCode)
                .priority(priority)
                .build();

        // Act
        SubstituteProduct substituteProduct = ProductResourceDTOMapper.convertToSubstituteProduct(resource);

        // Assert
        assertNotNull(substituteProduct);
        assertEquals(productCode, substituteProduct.getProductCode().getValue());
        assertEquals(substituteProductCode, substituteProduct.getSubstituteProductCode().getValue());
        assertEquals(priority, substituteProduct.getPriority());
    }

    @Test
    @DisplayName("部品表リソースを部品表エンティティに変換する")
    void testConvertToBom_validResource_shouldReturnBom() {
        // Arrange
        String productCode = "P001";
        String componentCode = "C001";
        Quantity componentQuantity = Quantity.of(2);

        BomResource resource = BomResource.builder()
                .productCode(productCode)
                .componentCode(componentCode)
                .componentQuantity(componentQuantity)
                .build();

        // Act
        Bom bom = ProductResourceDTOMapper.convertToBom(resource);

        // Assert
        assertNotNull(bom);
        assertEquals(productCode, bom.getProductCode().getValue());
        assertEquals(componentCode, bom.getComponentCode().getValue());
        assertEquals(componentQuantity.getAmount(), bom.getComponentQuantity().getAmount());
    }

    @Test
    @DisplayName("顧客別販売単価リソースを顧客別販売単価エンティティに変換する")
    void testConvertToCustomerSpecificSellingPrice_validResource_shouldReturnCustomerSpecificSellingPrice() {
        // Arrange
        String productCode = "P001";
        String customerCode = "C001";
        Money sellingPrice = Money.of(900);

        CustomerSpecificSellingPriceResource resource = CustomerSpecificSellingPriceResource.builder()
                .productCode(productCode)
                .customerCode(customerCode)
                .sellingPrice(sellingPrice)
                .build();

        // Act
        CustomerSpecificSellingPrice customerSpecificSellingPrice = ProductResourceDTOMapper.convertToCustomerSpecificSellingPrice(resource);

        // Assert
        assertNotNull(customerSpecificSellingPrice);
        assertEquals(productCode, customerSpecificSellingPrice.getProductCode().getValue());
        assertEquals(customerCode, customerSpecificSellingPrice.getCustomerCode());
        assertEquals(sellingPrice.getAmount(), customerSpecificSellingPrice.getSellingPrice().getAmount());
    }

    @Test
    @DisplayName("商品検索条件リソースを商品検索条件に変換する")
    void testConvertToCriteria_validResource_shouldReturnProductCriteria() {
        // Arrange
        String productCode = "P001";
        String productNameFormal = "テスト商品";
        String productNameAbbreviation = "テスト";
        String productNameKana = "テストショウヒン";
        String productCategoryCode = "PC001";
        String vendorCode = "001";
        String productType = "商品";
        String taxType = "外税";
        String miscellaneousType = "適用";
        String stockManagementTargetType = "対象";
        String stockAllocationType = "引当済";

        ProductCriteriaResource resource = new ProductCriteriaResource();
        resource.setProductCode(productCode);
        resource.setProductNameFormal(productNameFormal);
        resource.setProductNameAbbreviation(productNameAbbreviation);
        resource.setProductNameKana(productNameKana);
        resource.setProductCategoryCode(productCategoryCode);
        resource.setVendorCode(vendorCode);
        resource.setProductType(productType);
        resource.setTaxType(taxType);
        resource.setMiscellaneousType(miscellaneousType);
        resource.setStockManagementTargetType(stockManagementTargetType);
        resource.setStockAllocationType(stockAllocationType);

        // Act
        ProductCriteria criteria = ProductResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(criteria);
        assertEquals(productCode, criteria.getProductCode());
        assertEquals(productNameFormal, criteria.getProductNameFormal());
        assertEquals(productNameAbbreviation, criteria.getProductNameAbbreviation());
        assertEquals(productNameKana, criteria.getProductNameKana());
        assertEquals(productCategoryCode, criteria.getProductCategoryCode());
        assertEquals(vendorCode, criteria.getVendorCode());
        assertEquals(ProductType.商品.getCode(), criteria.getProductType());
        assertEquals(TaxType.外税.getCode(), criteria.getTaxType());
        assertEquals(MiscellaneousType.適用.getCode(), criteria.getMiscellaneousType());
        assertEquals(StockManagementTargetType.対象.getCode(), criteria.getStockManagementTargetType());
        assertEquals(StockAllocationType.引当済.getCode(), criteria.getStockAllocationType());
    }
}