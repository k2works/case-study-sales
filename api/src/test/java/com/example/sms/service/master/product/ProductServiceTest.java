package com.example.sms.service.master.product;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.master.product.Product;
import com.example.sms.domain.model.master.product.ProductCategory;
import com.example.sms.domain.model.master.product.ProductCategoryList;
import com.example.sms.domain.model.master.product.ProductList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

@IntegrationTest
@DisplayName("商品サービス")
public class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @Autowired
    private TestDataFactory testDataFactory;

    @BeforeEach
    void setUp() {
        testDataFactory.setUpForProductService();
    }

    @Nested
    @DisplayName("商品")
    class ProductTest {
        @Test
        @DisplayName("商品一覧を取得できる")
        void shouldRetrieveAllProducts() {
            ProductList result = productService.selectAll();
            assertEquals(3, result.asList().size());
        }

        @Test
        @DisplayName("商品を新規登録できる")
        void shouldRegisterNewProduct() {
            Product newProduct = testDataFactory.Product();

            productService.register(newProduct);

            ProductList result = productService.selectAll();
            assertEquals(4, result.asList().size());
            Product product = productService.find(newProduct.getProductCode().getValue());
            assertEquals(newProduct, product);
        }

        @Test
        @DisplayName("商品の登録情報を編集できる")
        void shouldEditProductDetails() {
            Product product = testDataFactory.Product();
            productService.register(product);

            Product updateProduct = Product.of(product.getProductCode().getValue(), "更新後商品正式名", "更新後商品略称", "更新後商品名カナ", "1", 2000, 3000, 4000, 2, "99999999", 3, 4, 5, "99999999", 6);
            productService.save(updateProduct);

            Product result = productService.find(product.getProductCode().getValue());
            assertEquals("更新後商品正式名", result.getProductName().getProductFormalName());
            assertEquals(updateProduct, result);
        }

        @Test
        @DisplayName("商品を削除できる")
        void shouldDeleteProduct() {
            Product product = testDataFactory.Product();
            productService.register(product);

            productService.delete(product);

            ProductList result = productService.selectAll();
            assertEquals(3, result.asList().size());
        }
    }

    @Nested
    @DisplayName("商品分類")
    class ProductCategoryTest {
        @Test
        @DisplayName("商品分類一覧を取得できる")
        void shouldRetrieveAllProductCategories() {
            ProductCategoryList result = productService.selectAllCategory();
            assertEquals(2, result.asList().size());
        }

        @Test
        @DisplayName("商品分類を新規登録できる")
        void shouldRegisterNewProductCategory() {
            ProductCategory newProductCategory = testDataFactory.ProductCategory();
            productService.registerCategory(newProductCategory);

            ProductCategoryList result = productService.selectAllCategory();
            assertEquals(3, result.asList().size());
            ProductCategory productCategory = productService.findCategory(newProductCategory.getProductCategoryCode().getValue());
            assertEquals(newProductCategory, productCategory);
        }

        @Test
        @DisplayName("商品分類の登録情報を編集できる")
        void shouldEditProductCategoryDetails() {
            ProductCategory productCategory = testDataFactory.ProductCategory();
            productService.registerCategory(productCategory);

            ProductCategory updateProductCategory = ProductCategory.of(productCategory.getProductCategoryCode().getValue(), "更新後商品分類名", productCategory.getProductCategoryHierarchy(), productCategory.getProductCategoryPath(), productCategory.getLowestLevelDivision());
            productService.saveCategory(updateProductCategory);

            ProductCategory result = productService.findCategory(productCategory.getProductCategoryCode().getValue());
            assertEquals("更新後商品分類名", result.getProductCategoryName());
            assertEquals(updateProductCategory, result);
        }

        @Test
        @DisplayName("商品分類を削除できる")
        void shouldDeleteProductCategory() {
            ProductCategory productCategory = testDataFactory.ProductCategory();
            productService.registerCategory(productCategory);

            productService.deleteCategory(productCategory);

            ProductCategoryList result = productService.selectAllCategory();
            assertEquals(2, result.asList().size());
        }
    }
}
