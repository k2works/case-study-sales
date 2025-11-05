package com.example.sms.presentation.api.master.product;

import com.example.sms.domain.model.master.product.*;
import com.example.sms.service.master.product.ProductCategoryCriteria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("商品分類DTOマッパーテスト")
class ProductCategoryResourceDTOMapperTest {

    @Test
    @DisplayName("商品分類リソースを商品分類エンティティに変換する")
    void testConvertToEntity_validResource_shouldReturnProductCategory() {
        // Arrange
        String productCategoryCode = "PC001";
        String productCategoryName = "テスト商品分類";
        Integer productCategoryHierarchy = 1;
        String productCategoryPath = "PC001~";
        Integer lowestLevelDivision = 1;

        ProductCategoryResource resource = ProductCategoryResource.builder()
                .productCategoryCode(productCategoryCode)
                .productCategoryName(productCategoryName)
                .productCategoryHierarchy(productCategoryHierarchy)
                .productCategoryPath(productCategoryPath)
                .lowestLevelDivision(lowestLevelDivision)
                .build();

        // Act
        ProductCategory productCategory = ProductCategoryResourceDTOMapper.convertToEntity(resource);

        // Assert
        assertNotNull(productCategory);
        assertEquals(productCategoryCode, productCategory.getProductCategoryCode().getValue());
        assertEquals(productCategoryName, productCategory.getProductCategoryName());
        assertEquals(productCategoryHierarchy, productCategory.getProductCategoryHierarchy());
        assertEquals(productCategoryPath, productCategory.getProductCategoryPath());
        assertEquals(lowestLevelDivision, productCategory.getLowestLevelDivision());
        assertTrue(productCategory.getProducts().isEmpty());
    }

    @Test
    @DisplayName("商品分類コードと商品分類リソースを商品分類エンティティに変換する")
    void testConvertToEntity_withProductCategoryCode_shouldReturnProductCategory() {
        // Arrange
        String productCategoryCode = "PC001";
        String productCategoryName = "テスト商品分類";
        Integer productCategoryHierarchy = 1;
        String productCategoryPath = "PC001~";
        Integer lowestLevelDivision = 1;

        ProductCategoryResource resource = ProductCategoryResource.builder()
                .productCategoryCode("DIFFERENT_CODE") // This should be overridden
                .productCategoryName(productCategoryName)
                .productCategoryHierarchy(productCategoryHierarchy)
                .productCategoryPath(productCategoryPath)
                .lowestLevelDivision(lowestLevelDivision)
                .build();

        // Act
        ProductCategory productCategory = ProductCategoryResourceDTOMapper.convertToEntity(productCategoryCode, resource);

        // Assert
        assertNotNull(productCategory);
        assertEquals(productCategoryCode, productCategory.getProductCategoryCode().getValue());
        assertEquals(productCategoryName, productCategory.getProductCategoryName());
        assertEquals(productCategoryHierarchy, productCategory.getProductCategoryHierarchy());
        assertEquals(productCategoryPath, productCategory.getProductCategoryPath());
        assertEquals(lowestLevelDivision, productCategory.getLowestLevelDivision());
        assertTrue(productCategory.getProducts().isEmpty());
    }

    @Test
    @DisplayName("商品分類検索条件リソースを商品分類検索条件に変換する")
    void testConvertToCriteria_validResource_shouldReturnProductCategoryCriteria() {
        // Arrange
        String productCategoryCode = "PC001";
        String productCategoryName = "テスト商品分類";
        String productCategoryPath = "PC001~";

        ProductCategoryCriteriaResource resource = new ProductCategoryCriteriaResource();
        resource.setProductCategoryCode(productCategoryCode);
        resource.setProductCategoryName(productCategoryName);
        resource.setProductCategoryPath(productCategoryPath);

        // Act
        ProductCategoryCriteria criteria = ProductCategoryResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(criteria);
        assertEquals(productCategoryCode, criteria.getProductCategoryCode());
        assertEquals(productCategoryName, criteria.getProductCategoryName());
        assertEquals(productCategoryPath, criteria.getProductCategoryPath());
    }

    @Test
    @DisplayName("追加フラグがtrueの商品のみをフィルタリングする")
    void testGetAddFilteredProducts_shouldReturnOnlyProductsWithAddFlagTrue() {
        // Arrange
        ProductResource product1 = createProductResource("P001", true, false);
        ProductResource product2 = createProductResource("P002", false, false);
        ProductResource product3 = createProductResource("P003", true, false);

        ProductCategoryResource resource = ProductCategoryResource.builder()
                .productCategoryCode("PC001")
                .Products(List.of(product1, product2, product3))
                .build();

        // Act
        List<Product> filteredProducts = ProductCategoryResourceDTOMapper.getAddFilteredProducts(resource);

        // Assert
        assertEquals(2, filteredProducts.size());
        assertEquals("P001", filteredProducts.get(0).getProductCode().getValue());
        assertEquals("P003", filteredProducts.get(1).getProductCode().getValue());
    }

    @Test
    @DisplayName("削除フラグがtrueの商品のみをフィルタリングする")
    void testGetDeleteFilteredProducts_shouldReturnOnlyProductsWithDeleteFlagTrue() {
        // Arrange
        ProductResource product1 = createProductResource("P001", false, true);
        ProductResource product2 = createProductResource("P002", false, false);
        ProductResource product3 = createProductResource("P003", false, true);

        ProductCategoryResource resource = ProductCategoryResource.builder()
                .productCategoryCode("PC001")
                .Products(List.of(product1, product2, product3))
                .build();

        // Act
        List<Product> filteredProducts = ProductCategoryResourceDTOMapper.getDeleteFilteredProducts(resource);

        // Assert
        assertEquals(2, filteredProducts.size());
        assertEquals("P001", filteredProducts.get(0).getProductCode().getValue());
        assertEquals("P003", filteredProducts.get(1).getProductCode().getValue());
    }

    @Test
    @DisplayName("商品リストがnullの場合は空のリストを返す")
    void testGetFilteredProducts_nullProductsList_shouldReturnEmptyList() {
        // Arrange
        ProductCategoryResource resource = ProductCategoryResource.builder()
                .productCategoryCode("PC001")
                .Products(null)
                .build();

        // Act
        List<Product> addFilteredProducts = ProductCategoryResourceDTOMapper.getAddFilteredProducts(resource);
        List<Product> deleteFilteredProducts = ProductCategoryResourceDTOMapper.getDeleteFilteredProducts(resource);

        // Assert
        assertTrue(addFilteredProducts.isEmpty());
        assertTrue(deleteFilteredProducts.isEmpty());
    }

    private ProductResource createProductResource(String productCode, boolean addFlag, boolean deleteFlag) {
        return ProductResource.builder()
                .productCode(productCode)
                .productFormalName("テスト商品")
                .productAbbreviation("テスト")
                .productNameKana("テストショウヒン")
                .sellingPrice(1000)
                .purchasePrice(500)
                .costOfSales(800)
                .taxType(TaxType.外税)
                .vendorCode("001")
                .vendorBranchNumber(1)
                .addFlag(addFlag)
                .deleteFlag(deleteFlag)
                .build();
    }
}
