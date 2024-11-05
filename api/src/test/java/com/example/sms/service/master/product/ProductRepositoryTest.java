package com.example.sms.service.master.product;

import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.master.product.Bom;
import com.example.sms.domain.model.master.product.Product;
import com.example.sms.domain.model.master.product.ProductList;
import com.example.sms.domain.model.master.product.SubstituteProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

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

    private Product getProduct(String productCode) {
        return TestDataFactoryImpl.product(productCode, "商品正式名", "商品略称", "商品名カナ", "0", 1000, 2000, 3000, 1, "00000000", 2, 3, 4, "00000000", 5);
    }

    private SubstituteProduct getSubstituteProduct(String productCode, String substituteProductCode) {
        return TestDataFactoryImpl.substituteProduct(productCode, substituteProductCode, 1);
    }

    private Bom getBom(String productCode, String componentCode) {
        return TestDataFactoryImpl.bom(productCode, componentCode, 1);
    }


    @Nested
    @DisplayName("商品")
    class ProductTest {
        @Test
        @DisplayName("商品一覧を取得できる")
        void shouldRetrieveAllProducts() {
            IntStream.range(0, 10).forEach(i -> {
                Product product = getProduct(String.format("%08d", i));
                repository.save(product);
            });

            assertEquals(10, repository.selectAll().size());
        }

        @Test
        @DisplayName("商品を登録できる")
        void shouldRegisterProduct() {
            Product product = getProduct("99999999");

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
            Product product = getProduct("99999999");
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
            Product product = getProduct("99999999");
            repository.save(product);

            repository.deleteById(product);

            assertEquals(0, repository.selectAll().size());
        }
    }

    @Nested
    @DisplayName("代替商品")
    class SubstituteProductTest {
        @Test
        @DisplayName("代替商品一覧を取得できる")
        void shouldRetrieveAllSubstituteProducts() {
            Product product = getProduct("99999999");
            List<SubstituteProduct> substituteProducts = IntStream.range(0, 10).mapToObj(i -> getSubstituteProduct(product.getProductCode(), String.format("%08d", i))).toList();
            Product newProduct = Product.of(product, substituteProducts, List.of());
            repository.save(newProduct);

            ProductList actual = repository.selectAll();
            assertEquals(10, actual.asList().getFirst().getSubstituteProduct().size());
        }

        @Test
        @DisplayName("代替商品を登録できる")
        void shouldRegisterSubstituteProduct() {
            Product product = getProduct("99999999");
            SubstituteProduct substituteProduct = getSubstituteProduct(product.getProductCode(), "00000000");
            Product newProduct = Product.of(product, List.of(substituteProduct), List.of());
            repository.save(newProduct);

            Product actual = repository.findById(product.getProductCode()).get();
            assertEquals(1, actual.getSubstituteProduct().size());
            assertEquals(substituteProduct, actual.getSubstituteProduct().get(0));
        }

        @Test
        @DisplayName("代替商品を更新できる")
        void shouldUpdateSubstituteProduct() {
            Product product = getProduct("99999999");
            SubstituteProduct substituteProduct = getSubstituteProduct(product.getProductCode(), "00000000");
            Product newProduct = Product.of(product, List.of(substituteProduct), List.of());
            repository.save(newProduct);

            newProduct = repository.findById(product.getProductCode()).get();
            SubstituteProduct updatedSubstituteProduct = SubstituteProduct.of(product.getProductCode(), "00000000", 2);
            newProduct = Product.of(newProduct, List.of(updatedSubstituteProduct), List.of());
            repository.save(newProduct);

            Product actual = repository.findById(product.getProductCode()).get();
            assertEquals(1, actual.getSubstituteProduct().size());
            assertEquals(updatedSubstituteProduct, actual.getSubstituteProduct().get(0));
        }

        @Test
        @DisplayName("代替商品を削除できる")
        void shouldDeleteSubstituteProduct() {
            Product product = getProduct("99999999");
            SubstituteProduct substituteProduct = getSubstituteProduct(product.getProductCode(), "00000000");
            Product newProduct = Product.of(product, List.of(substituteProduct), List.of());
            repository.save(newProduct);

            Product deleteProduct = repository.findById(product.getProductCode()).get();
            repository.deleteById(deleteProduct);

            Optional<Product> actual = repository.findById(product.getProductCode());
            assertEquals(Optional.empty(), actual);
        }
    }

    @Nested
    @DisplayName("部品表")
    class BomTest {
        @Test
        @DisplayName("部品表一覧を取得できる")
        void shouldRetrieveAllBoms() {
            Product product = getProduct("99999999");
            List<Bom> boms = IntStream.range(0, 10).mapToObj(i -> getBom(product.getProductCode(), String.format("%08d", i))).toList();
            Product newProduct = Product.of(product, List.of(), boms);
            repository.save(newProduct);

            ProductList actual = repository.selectAll();
            assertEquals(10, actual.asList().getFirst().getBoms().size());
        }

        @Test
        @DisplayName("部品表を登録できる")
        void shouldRegisterBom() {
            Product product = getProduct("99999999");
            Bom bom = getBom(product.getProductCode(), "00000000");
            Product newProduct = Product.of(product, List.of(), List.of(bom));
            repository.save(newProduct);

            Product actual = repository.findById(product.getProductCode()).get();
            assertEquals(1, actual.getBoms().size());
            assertEquals(bom, actual.getBoms().get(0));
        }

        @Test
        @DisplayName("部品表を更新できる")
        void shouldUpdateBom() {
            Product product = getProduct("99999999");
            Bom bom = getBom(product.getProductCode(), "00000000");
            Product newProduct = Product.of(product, List.of(), List.of(bom));
            repository.save(newProduct);

            newProduct = repository.findById(product.getProductCode()).get();
            Bom updatedBom = Bom.of(product.getProductCode(), "00000000", 2);
            newProduct = Product.of(newProduct, List.of(), List.of(updatedBom));
            repository.save(newProduct);

            Product actual = repository.findById(product.getProductCode()).get();
            assertEquals(1, actual.getBoms().size());
            assertEquals(updatedBom, actual.getBoms().get(0));
        }

        @Test
        @DisplayName("部品表を削除できる")
        void shouldDeleteBom() {
            Product product = getProduct("99999999");
            Bom bom = getBom(product.getProductCode(), "00000000");
            Product newProduct = Product.of(product, List.of(), List.of(bom));
            repository.save(newProduct);

            Product deleteProduct = repository.findById(product.getProductCode()).get();
            repository.deleteById(deleteProduct);

            Optional<Product> actual = repository.findById(product.getProductCode());
            assertEquals(Optional.empty(), actual);
        }
    }

}
