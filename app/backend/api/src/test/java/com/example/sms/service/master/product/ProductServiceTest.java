package com.example.sms.service.master.product;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.master.product.Product;
import com.example.sms.domain.model.master.product.ProductCategory;
import com.example.sms.domain.model.master.product.ProductCategoryList;
import com.example.sms.domain.model.master.product.ProductList;
import com.example.sms.domain.type.product.*;
import com.github.pagehelper.PageInfo;
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
        @Nested
        @DisplayName("商品アイテム")
        class ProductItemTest {
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

                Product updateProduct = Product.of(product.getProductCode().getValue(), "更新後商品正式名", "更新後商品略称", "更新後商品名カナ", ProductType.商品, 2000, 3000, 4000, TaxType.内税, "99999999", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "999", 6);
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

            @Nested
            @DisplayName("商品検索")
            class ProductSearchTest {
                @Test
                @DisplayName("商品をコードで検索できる")
                void case1() {
                    Product product = testDataFactory.Product();
                    Product searchProduct = Product.of("10101001", product.getProductName().getProductFormalName(), product.getProductName().getProductAbbreviation(), product.getProductName().getProductNameKana(), product.getProductType(), product.getSellingPrice().getAmount(), product.getPurchasePrice().getAmount(), product.getCostOfSales().getAmount(), product.getTaxType(), product.getTaxType().name(), product.getMiscellaneousType(), product.getStockManagementTargetType(), product.getStockAllocationType(), product.getVendorCode().getCode().getValue(), product.getVendorCode().getBranchNumber());
                    productService.register(searchProduct);
                    ProductCriteria criteria = ProductCriteria.builder().productCode("10101001").build();

                    PageInfo<Product> result = productService.searchProductWithPageInfo(criteria);

                    assertEquals(1, result.getList().size());
                    assertEquals(searchProduct, result.getList().getFirst());
                    assertEquals(1, result.getTotal());
                }

                @Test
                @DisplayName("商品を正式名で検索できる")
                void case2() {
                    Product product = testDataFactory.Product();
                    Product searchProduct = Product.of(product.getProductCode().getValue(), "検索用商品名", product.getProductName().getProductAbbreviation(), product.getProductName().getProductNameKana(), product.getProductType(), product.getSellingPrice().getAmount(), product.getPurchasePrice().getAmount(), product.getCostOfSales().getAmount(), product.getTaxType(), product.getProductCategoryCode().getValue(), product.getMiscellaneousType(), product.getStockManagementTargetType(), product.getStockAllocationType(), product.getVendorCode().getCode().getValue(), product.getVendorCode().getBranchNumber());
                    productService.register(searchProduct);
                    ProductCriteria criteria = ProductCriteria.builder().productNameFormal("検索用商品名").build();

                    PageInfo<Product> result = productService.searchProductWithPageInfo(criteria);

                    assertEquals(1, result.getList().size());
                    assertEquals(searchProduct, result.getList().getFirst());
                    assertEquals(1, result.getTotal());
                }

                @Test
                @DisplayName("商品を略称で検索できる")
                void case3() {
                    Product product = testDataFactory.Product();
                    Product searchProduct = Product.of(product.getProductCode().getValue(), product.getProductName().getProductFormalName(), "検索用商品略称", product.getProductName().getProductNameKana(), product.getProductType(), product.getSellingPrice().getAmount(), product.getPurchasePrice().getAmount(), product.getCostOfSales().getAmount(), product.getTaxType(), product.getProductCategoryCode().getValue(), product.getMiscellaneousType(), product.getStockManagementTargetType(), product.getStockAllocationType(), product.getVendorCode().getCode().getValue(), product.getVendorCode().getBranchNumber());
                    productService.register(searchProduct);
                    ProductCriteria criteria = ProductCriteria.builder().productNameAbbreviation("検索用商品略称").build();

                    PageInfo<Product> result = productService.searchProductWithPageInfo(criteria);

                    assertEquals(1, result.getList().size());
                    assertEquals(searchProduct, result.getList().getFirst());
                    assertEquals(1, result.getTotal());
                }

                @Test
                @DisplayName("商品をカナで検索できる")
                void case4() {
                    Product product = testDataFactory.Product();
                    Product searchProduct = Product.of(product.getProductCode().getValue(), product.getProductName().getProductFormalName(), product.getProductName().getProductAbbreviation(), "カナ", product.getProductType(), product.getSellingPrice().getAmount(), product.getPurchasePrice().getAmount(), product.getCostOfSales().getAmount(), product.getTaxType(), product.getProductCategoryCode().getValue(), product.getMiscellaneousType(), product.getStockManagementTargetType(), product.getStockAllocationType(), product.getVendorCode().getCode().getValue(), product.getVendorCode().getBranchNumber());
                    productService.register(searchProduct);
                    ProductCriteria criteria = ProductCriteria.builder().productNameKana("カナ").build();

                    PageInfo<Product> result = productService.searchProductWithPageInfo(criteria);

                    assertEquals(1, result.getList().size());
                    assertEquals(searchProduct, result.getList().getFirst());
                    assertEquals(1, result.getTotal());
                }

                @Test
                @DisplayName("商品を商品分類コードで検索できる")
                void case5() {
                    Product product = testDataFactory.Product();
                    Product searchProduct = Product.of(product.getProductCode().getValue(), product.getProductName().getProductFormalName(), product.getProductName().getProductAbbreviation(), product.getProductName().getProductNameKana(), product.getProductType(), product.getSellingPrice().getAmount(), product.getPurchasePrice().getAmount(), product.getCostOfSales().getAmount(), product.getTaxType(), "99999999", product.getMiscellaneousType(), product.getStockManagementTargetType(), product.getStockAllocationType(), product.getVendorCode().getCode().getValue(), product.getVendorCode().getBranchNumber());
                    productService.register(searchProduct);
                    ProductCriteria criteria = ProductCriteria.builder().productCategoryCode("99999999").build();

                    PageInfo<Product> result = productService.searchProductWithPageInfo(criteria);

                    assertEquals(1, result.getList().size());
                    assertEquals(searchProduct, result.getList().getFirst());
                    assertEquals(1, result.getTotal());
                }

                @Test
                @DisplayName("商品を仕入先コードで検索できる")
                void case6() {
                    Product product = testDataFactory.Product();
                    Product searchProduct = Product.of(product.getProductCode().getValue(), product.getProductName().getProductFormalName(), product.getProductName().getProductAbbreviation(), product.getProductName().getProductNameKana(), product.getProductType(), product.getSellingPrice().getAmount(), product.getPurchasePrice().getAmount(), product.getCostOfSales().getAmount(), product.getTaxType(), product.getProductCategoryCode().getValue(), product.getMiscellaneousType(), product.getStockManagementTargetType(), product.getStockAllocationType(), "999", product.getVendorCode().getBranchNumber());
                    productService.register(searchProduct);
                    ProductCriteria criteria = ProductCriteria.builder().vendorCode("999").build();

                    PageInfo<Product> result = productService.searchProductWithPageInfo(criteria);

                    assertEquals(1, result.getList().size());
                    assertEquals(searchProduct, result.getList().getFirst());
                    assertEquals(1, result.getTotal());
                }

                @Test
                @DisplayName("商品を商品区分で検索できる")
                void case7() {
                    Product product = testDataFactory.Product();
                    Product searchProduct = Product.of(product.getProductCode().getValue(), product.getProductName().getProductFormalName(), product.getProductName().getProductAbbreviation(), product.getProductName().getProductNameKana(), ProductType.商品, product.getSellingPrice().getAmount(), product.getPurchasePrice().getAmount(), product.getCostOfSales().getAmount(), product.getTaxType(), product.getProductCategoryCode().getValue(), product.getMiscellaneousType(), product.getStockManagementTargetType(), product.getStockAllocationType(), product.getVendorCode().getCode().getValue(), product.getVendorCode().getBranchNumber());
                    productService.register(searchProduct);
                    ProductCriteria criteria = ProductCriteria.builder().productType(ProductType.商品.getCode()).build();

                    PageInfo<Product> result = productService.searchProductWithPageInfo(criteria);

                    assertEquals(1, result.getList().size());
                    assertEquals(searchProduct, result.getList().getFirst());
                    assertEquals(1, result.getTotal());
                }

                @Test
                @DisplayName("商品を税区分で検索できる")
                void case8() {
                    Product product = testDataFactory.Product();
                    Product searchProduct = Product.of(product.getProductCode().getValue(), product.getProductName().getProductFormalName(), product.getProductName().getProductAbbreviation(), product.getProductName().getProductNameKana(), product.getProductType(), product.getSellingPrice().getAmount(), product.getPurchasePrice().getAmount(), product.getCostOfSales().getAmount(), TaxType.その他, product.getProductCategoryCode().getValue(), product.getMiscellaneousType(), product.getStockManagementTargetType(), product.getStockAllocationType(), product.getVendorCode().getCode().getValue(), product.getVendorCode().getBranchNumber());
                    productService.register(searchProduct);
                    ProductCriteria criteria = ProductCriteria.builder().taxType(TaxType.その他.getCode()).build();

                    PageInfo<Product> result = productService.searchProductWithPageInfo(criteria);

                    assertEquals(1, result.getList().size());
                    assertEquals(searchProduct, result.getList().getFirst());
                    assertEquals(1, result.getTotal());
                }

                @Test
                @DisplayName("商品を雑費区分で検索できる")
                void case9() {
                    Product product = testDataFactory.Product();
                    Product searchProduct = Product.of(product.getProductCode().getValue(), product.getProductName().getProductFormalName(), product.getProductName().getProductAbbreviation(), product.getProductName().getProductNameKana(), product.getProductType(), product.getSellingPrice().getAmount(), product.getPurchasePrice().getAmount(), product.getCostOfSales().getAmount(), product.getTaxType(), product.getProductCategoryCode().getValue(), MiscellaneousType.適用, product.getStockManagementTargetType(), product.getStockAllocationType(), product.getVendorCode().getCode().getValue(), product.getVendorCode().getBranchNumber());
                    productService.register(searchProduct);
                    ProductCriteria criteria = ProductCriteria.builder().miscellaneousType(MiscellaneousType.適用.getCode()).build();

                    PageInfo<Product> result = productService.searchProductWithPageInfo(criteria);

                    assertEquals(1, result.getList().size());
                    assertEquals(searchProduct, result.getList().getFirst());
                    assertEquals(1, result.getTotal());
                }

                @Test
                @DisplayName("商品を在庫管理対象区分で検索できる")
                void case10() {
                    Product product = testDataFactory.Product();
                    Product searchProduct = Product.of(product.getProductCode().getValue(), product.getProductName().getProductFormalName(), product.getProductName().getProductAbbreviation(), product.getProductName().getProductNameKana(), product.getProductType(), product.getSellingPrice().getAmount(), product.getPurchasePrice().getAmount(), product.getCostOfSales().getAmount(), product.getTaxType(), product.getProductCategoryCode().getValue(), product.getMiscellaneousType(), StockManagementTargetType.対象外, product.getStockAllocationType(), product.getVendorCode().getCode().getValue(), product.getVendorCode().getBranchNumber());
                    productService.register(searchProduct);
                    ProductCriteria criteria = ProductCriteria.builder().stockManagementTargetType(StockManagementTargetType.対象外.getCode()).build();

                    PageInfo<Product> result = productService.searchProductWithPageInfo(criteria);

                    assertEquals(1, result.getList().size());
                    assertEquals(searchProduct, result.getList().getFirst());
                    assertEquals(1, result.getTotal());
                }

                @Test
                @DisplayName("商品を在庫引当区分で検索できる")
                void case11() {
                    Product product = testDataFactory.Product();
                    Product searchProduct = Product.of(product.getProductCode().getValue(), product.getProductName().getProductFormalName(), product.getProductName().getProductAbbreviation(), product.getProductName().getProductNameKana(), product.getProductType(), product.getSellingPrice().getAmount(), product.getPurchasePrice().getAmount(), product.getCostOfSales().getAmount(), product.getTaxType(), product.getProductCategoryCode().getValue(), product.getMiscellaneousType(), product.getStockManagementTargetType(), StockAllocationType.未引当, product.getVendorCode().getCode().getValue(), product.getVendorCode().getBranchNumber());
                    productService.register(searchProduct);
                    ProductCriteria criteria = ProductCriteria.builder().stockAllocationType(StockAllocationType.未引当.getCode()).build();

                    PageInfo<Product> result = productService.searchProductWithPageInfo(criteria);

                    assertEquals(1, result.getList().size());
                    assertEquals(searchProduct, result.getList().getFirst());
                    assertEquals(1, result.getTotal());
                }

                @Test
                @DisplayName("商品を複合条件で検索できる")
                void case12() {
                    Product searchProduct = Product.of("10101001", "検索用商品正式名", "検索用商品略称", "検索用商品名カナ", ProductType.商品, 2000, 3000, 4000, TaxType.内税, "99999999", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "999", 6);
                    productService.register(searchProduct);
                    ProductCriteria criteria = ProductCriteria.builder().productCode("10101001").productNameFormal("検索用商品正式名").productNameAbbreviation("検索用商品略称").productNameKana("検索用商品名カナ").productType(ProductType.商品.getCode()).taxType(TaxType.内税.getCode()).miscellaneousType(MiscellaneousType.適用外.getCode()).stockManagementTargetType(StockManagementTargetType.対象.getCode()).stockAllocationType(StockAllocationType.引当済.getCode()).build();

                    PageInfo<Product> result = productService.searchProductWithPageInfo(criteria);

                    assertEquals(1, result.getList().size());
                    assertEquals(searchProduct, result.getList().getFirst());
                    assertEquals(1, result.getTotal());
                }
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

            @Nested
            @DisplayName("商品分類検索")
            class ProductCategorySearchTest {
                @Test
                @DisplayName("商品分類をコードで検索できる")
                void case1() {
                    ProductCategory productCategory = testDataFactory.ProductCategory();
                    ProductCategory searchProductCategory = ProductCategory.of("12345678", productCategory.getProductCategoryName(), productCategory.getProductCategoryHierarchy(), productCategory.getProductCategoryPath(), productCategory.getLowestLevelDivision());
                    productService.registerCategory(searchProductCategory);
                    ProductCategoryCriteria criteria = ProductCategoryCriteria.builder().productCategoryCode("12345678").build();

                    PageInfo<ProductCategory> result = productService.searchProductCategoryWithPageInfo(criteria);

                    assertEquals(1, result.getList().size());
                    assertEquals(searchProductCategory, result.getList().getFirst());
                    assertEquals(1, result.getTotal());
                }

                @Test
                @DisplayName("商品分類を名前で検索できる")
                void case2() {
                    ProductCategory productCategory = testDataFactory.ProductCategory();
                    ProductCategory searchProductCategory = ProductCategory.of(productCategory.getProductCategoryCode().getValue(), "検索用商品分類名", productCategory.getProductCategoryHierarchy(), productCategory.getProductCategoryPath(), productCategory.getLowestLevelDivision());
                    productService.registerCategory(searchProductCategory);
                    ProductCategoryCriteria criteria = ProductCategoryCriteria.builder().productCategoryCode(productCategory.getProductCategoryCode().getValue()).build();

                    PageInfo<ProductCategory> result = productService.searchProductCategoryWithPageInfo(criteria);

                    assertEquals(1, result.getList().size());
                    assertEquals(searchProductCategory, result.getList().getFirst());
                    assertEquals(1, result.getTotal());
                }

                @Test
                @DisplayName("商品分類をパスで検索できる")
                void case3() {
                    ProductCategory productCategory = testDataFactory.ProductCategory();
                    ProductCategory searchProductCategory = ProductCategory.of("10000000", productCategory.getProductCategoryName(), productCategory.getProductCategoryHierarchy(), "10000000~", productCategory.getLowestLevelDivision());
                    productService.registerCategory(searchProductCategory);
                    searchProductCategory = ProductCategory.of("11000000", productCategory.getProductCategoryName(), productCategory.getProductCategoryHierarchy(), "10000000~11000000", productCategory.getLowestLevelDivision());
                    productService.registerCategory(searchProductCategory);
                    ProductCategoryCriteria criteria = ProductCategoryCriteria.builder().productCategoryPath("10000000~").build();

                    PageInfo<ProductCategory> result = productService.searchProductCategoryWithPageInfo(criteria);

                    assertEquals(2, result.getList().size());
                    assertEquals(searchProductCategory, result.getList().getLast());
                    assertEquals(2, result.getTotal());
                }

                @Test
                @DisplayName("商品分類をコードと名前で検索できる")
                void case4() {
                    ProductCategory productCategory = testDataFactory.ProductCategory();
                    ProductCategory searchProductCategory = ProductCategory.of("12345678", "検索用商品分類名", productCategory.getProductCategoryHierarchy(), productCategory.getProductCategoryPath(), productCategory.getLowestLevelDivision());
                    productService.registerCategory(searchProductCategory);
                    ProductCategoryCriteria criteria = ProductCategoryCriteria.builder().productCategoryCode("12345678").productCategoryName("検索用商品分類名").build();

                    PageInfo<ProductCategory> result = productService.searchProductCategoryWithPageInfo(criteria);

                    assertEquals(1, result.getList().size());
                    assertEquals(searchProductCategory, result.getList().getFirst());
                    assertEquals(1, result.getTotal());
                }

                @Test
                @DisplayName("商品分類をコードとパスで検索できる")
                void case5() {
                    ProductCategory productCategory = testDataFactory.ProductCategory();
                    ProductCategory searchProductCategory = ProductCategory.of("12345678", productCategory.getProductCategoryName(), productCategory.getProductCategoryHierarchy(), "12345678~", productCategory.getLowestLevelDivision());
                    productService.registerCategory(searchProductCategory);
                    ProductCategoryCriteria criteria = ProductCategoryCriteria.builder().productCategoryCode("12345678").productCategoryPath("12345678~").build();

                    PageInfo<ProductCategory> result = productService.searchProductCategoryWithPageInfo(criteria);

                    assertEquals(1, result.getList().size());
                    assertEquals(searchProductCategory, result.getList().getFirst());
                    assertEquals(1, result.getTotal());
                }
            }
        }
    }
}