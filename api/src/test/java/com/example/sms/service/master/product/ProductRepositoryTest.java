package com.example.sms.service.master.product;

import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.master.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@DisplayName("商品レポジトリ")
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    private Product getProduct() {
        return TestDataFactoryImpl.product("99999999", "商品正式名", "商品略称", "商品名カナ", "0", 1000, 2000, 3000, 1, "00000000", 2, 3, 4, "00000000", 5);
    }

    @Test
    @DisplayName("商品一覧を取得できる")
    void shouldRetrieveAllProducts() {
        Product product = getProduct();

        repository.save(product);

        assertEquals(1, repository.selectAll().size());
    }

    @Test
    @DisplayName("商品を登録できる")
    void shouldRegisterProduct() {
        Product product = getProduct();

        repository.save(product);

        Product actual = repository.findById(product.getProductCode()).get();
        assertEquals(product.getProductCode(), actual.getProductCode());
        assertEquals(product.getProductFormalName(), actual.getProductFormalName());
        assertEquals(product.getProductAbbreviation(), actual.getProductAbbreviation());
        assertEquals(product.getProductNameKana(), actual.getProductNameKana());
        assertEquals(product.getProductCategory(), actual.getProductCategory());
        assertEquals(product.getSellingPrice(), actual.getSellingPrice());
        assertEquals(product.getPurchasePrice(), actual.getPurchasePrice());
        assertEquals(product.getCostOfSales(), actual.getCostOfSales());
        assertEquals(product.getTaxCategory(), actual.getTaxCategory());
        assertEquals(product.getProductClassificationCode(), actual.getProductClassificationCode());
        assertEquals(product.getMiscellaneousCategory(), actual.getMiscellaneousCategory());
        assertEquals(product.getStockManagementTargetCategory(), actual.getStockManagementTargetCategory());
        assertEquals(product.getStockAllocationCategory(), actual.getStockAllocationCategory());
        assertEquals(product.getSupplierCode(), actual.getSupplierCode());
        assertEquals(product.getSupplierBranchNumber(), actual.getSupplierBranchNumber());
    }

    @Test
    @DisplayName("商品を更新できる")
    void shouldUpdateProduct() {
        Product product = getProduct();
        repository.save(product);

        product = repository.findById(product.getProductCode()).get();
        Product updatedProduct = Product.of(product.getProductCode(), "更新後商品正式名", "更新後商品略称", "更新後商品名カナ", "1", 2000, 3000, 4000, 2, "99999999", 3, 4, 5, "99999999", 6);
        repository.save(updatedProduct);

        Product actual = repository.findById(product.getProductCode()).get();
        assertEquals(updatedProduct, actual);
    }

    @Test
    @DisplayName("商品を削除できる")
    void shouldDeleteProduct() {
        Product product = getProduct();
        repository.save(product);

        repository.deleteById(product.getProductCode());

        assertEquals(0, repository.selectAll().size());
    }
}
