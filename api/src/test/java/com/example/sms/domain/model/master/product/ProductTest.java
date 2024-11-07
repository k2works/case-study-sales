package com.example.sms.domain.model.master.product;

import com.example.sms.domain.type.*;
import com.example.sms.domain.type.money.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("商品")
public class ProductTest {

    @Test
    @DisplayName("商品を作成できる")
    void shouldCreateProduct() {
        Product product = Product.of("99999001", "Test Product", "TP", "テストプロダクト", "1", 100, 50, 60, 1, "100", 1, 1, 1, "1000", 1);

        assertNotNull(product, "Product creation resulted in Null");
        assertEquals("99999001", product.getProductCode().getValue(), "Mismatch in productCode");
        assertEquals("Test Product", product.getProductName().getProductFormalName(), "Mismatch in productFormalName");
        assertEquals("TP", product.getProductName().getProductAbbreviation(), "Mismatch in productAbbreviation");
        assertEquals("テストプロダクト", product.getProductName().getProductNameKana(), "Mismatch in productNameKana");
        assertEquals(ProductType.商品, product.getProductType(), "Mismatch in productType");
        assertEquals(Money.of(100), product.getSellingPrice(), "Mismatch in sellingPrice");
        assertEquals(Money.of(50), product.getPurchasePrice(), "Mismatch in purchasePrice");
        assertEquals(Money.of(60), product.getCostOfSales(), "Mismatch in costOfSales");
        assertEquals(TaxType.外税, product.getTaxType(), "Mismatch in taxCategory");
        assertEquals("100", product.getProductCategoryCode().getValue(), "Mismatch in productCategoryCode");
        assertEquals(MiscellaneousType.適用, product.getMiscellaneousType(), "Mismatch in miscellaneousCategory");
        assertEquals(1, product.getStockManagementTargetType(), "Mismatch in stockManagementTargetCategory");
        assertEquals(1, product.getStockAllocationType(), "Mismatch in stockAllocationCategory");
        assertEquals("1000", product.getSupplierCode().getValue(), "Mismatch in supplierCode");
        assertEquals(1, product.getSupplierCode().getBranchNumber(), "Mismatch in supplierBranchNumber");
        assertTrue(product.getSubstituteProduct().isEmpty(), "SubstituteProduct list is not empty");
        assertTrue(product.getBoms().isEmpty(), "Boms list is not empty");
        assertTrue(product.getCustomerSpecificSellingPrices().isEmpty(), "CustomerSpecificSellingPrices list is not empty");
    }

    @Nested
    @DisplayName("商品コード")
    class ProductCodeTest {
        @Test
        @DisplayName("商品コードは必須")
        void shouldThrowExceptionWhenProductCodeIsNull() {
            assertThrows(IllegalArgumentException.class, () -> Product.of(null, "Test Product", "TP", "テストプロダクト", "1", 100, 50, 60, 1, "100", 1, 1, 1, "1000", 1));
        }

        @Test
        @DisplayName("商品コードは8桁の数字")
        void shouldThrowExceptionWhenProductCodeIsNot8DigitNumber() {
            assertThrows(IllegalArgumentException.class, () -> Product.of("1000", "Test Product", "TP", "テストプロダクト", "1", 100, 50, 60, 1, "100", 1, 1, 1, "1000", 1));
        }

        @Test
        @DisplayName("商品コードの事業区分は最初の1文字")
        void shouldExtractBusinessTypeFromProductCode() {
            Product product = Product.of("99999001", "Test Product", "TP", "テストプロダクト", "1", 100, 50, 60, 1, "100", 1, 1, 1, "1000", 1);
            assertEquals(BusinessType.その他, product.getProductCode().getBusinessType(), "Mismatch in businessType");
        }

        @Test
        @DisplayName("商品コードの品目区分は2文字目から3文字")
        void shouldExtractItemTypeFromProductCode() {
            Product product = Product.of("99999001", "Test Product", "TP", "テストプロダクト", "1", 100, 50, 60, 1, "100", 1, 1, 1, "1000", 1);
            assertEquals(ItemType.その他, product.getProductCode().getItemType(), "Mismatch in itemType");
        }

        @Test
        @DisplayName("商品コードの畜産区分は4文字目から5文字")
        void shouldExtractLivestockTypeFromProductCode() {
            Product product = Product.of("99999001", "Test Product", "TP", "テストプロダクト", "1", 100, 50, 60, 1, "100", 1, 1, 1, "1000", 1);
            assertEquals(LiveStockType.その他, product.getProductCode().getLivestockType(), "Mismatch in livestockType");
        }

        @Test
        @DisplayName("商品コードの連番は6文字目から8文字")
        void shouldExtractSerialNumberFromProductCode() {
            Product product = Product.of("99999001", "Test Product", "TP", "テストプロダクト", "1", 100, 50, 60, 1, "100", 1, 1, 1, "1000", 1);
            assertEquals(1, product.getProductCode().getSerialNumber(), "Mismatch in serialNumber");
        }
    }
}
