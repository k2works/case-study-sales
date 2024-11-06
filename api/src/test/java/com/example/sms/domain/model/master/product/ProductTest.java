package com.example.sms.domain.model.master.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("商品")
public class ProductTest {

    @Test
    @DisplayName("商品を作成できる")
    void shouldCreateProduct() {
        Product product = Product.of("10000", "Test Product", "TP", "テストプロダクト", "1", 100, 50, 60, 1, "100", 1, 1, 1, "1000", 1);

        assertNotNull(product, "Product creation resulted in Null");
        assertEquals("10000", product.getProductCode(), "Mismatch in productCode");
        assertEquals("Test Product", product.getProductFormalName(), "Mismatch in productFormalName");
        assertEquals("TP", product.getProductAbbreviation(), "Mismatch in productAbbreviation");
        assertEquals("テストプロダクト", product.getProductNameKana(), "Mismatch in productNameKana");
        assertEquals("1", product.getProductType(), "Mismatch in productType");
        assertEquals(100, product.getSellingPrice(), "Mismatch in sellingPrice");
        assertEquals(50, product.getPurchasePrice(), "Mismatch in purchasePrice");
        assertEquals(60, product.getCostOfSales(), "Mismatch in costOfSales");
        assertEquals(1, product.getTaxCategory(), "Mismatch in taxCategory");
        assertEquals("100", product.getProductCategoryCode(), "Mismatch in productCategoryCode");
        assertEquals(1, product.getMiscellaneousType(), "Mismatch in miscellaneousCategory");
        assertEquals(1, product.getStockManagementTargetType(), "Mismatch in stockManagementTargetCategory");
        assertEquals(1, product.getStockAllocationType(), "Mismatch in stockAllocationCategory");
        assertEquals("1000", product.getSupplierCode(), "Mismatch in supplierCode");
        assertEquals(1, product.getSupplierBranchNumber(), "Mismatch in supplierBranchNumber");
        assertTrue(product.getSubstituteProduct().isEmpty(), "SubstituteProduct list is not empty");
        assertTrue(product.getBoms().isEmpty(), "Boms list is not empty");
        assertTrue(product.getCustomerSpecificSellingPrices().isEmpty(), "CustomerSpecificSellingPrices list is not empty");
    }
}
