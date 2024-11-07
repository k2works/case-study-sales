package com.example.sms.service.master.product;

import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.master.product.*;
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
        return TestDataFactoryImpl.product(productCode, "商品正式名", "商品略称", "商品名カナ", "9", 1000, 2000, 3000, 1, "00000000", 0, 1, 4, "00000000", 5);
    }

    private SubstituteProduct getSubstituteProduct(String productCode, String substituteProductCode) {
        return TestDataFactoryImpl.substituteProduct(productCode, substituteProductCode, 1);
    }

    private Bom getBom(String productCode, String componentCode) {
        return TestDataFactoryImpl.bom(productCode, componentCode, 1);
    }

    private CustomerSpecificSellingPrice getCustomerSpecificSellingPrice(String productCode, String format) {
        return TestDataFactoryImpl.customerSpecificSellingPrice(productCode, format, 1);
    }

    @Nested
    @DisplayName("商品")
    class ProductTest {
        @Test
        @DisplayName("商品一覧を取得できる")
        void shouldRetrieveAllProducts() {
            IntStream.range(0, 10).forEach(i -> {
                Product product = getProduct(String.format("99999%03d", i));
                repository.save(product);
            });

            assertEquals(10, repository.selectAll().size());
        }

        @Test
        @DisplayName("商品を登録できる")
        void shouldRegisterProduct() {
            Product product = getProduct("99999999");

            repository.save(product);

            Product actual = repository.findById(product.getProductCode().getValue()).get();
            assertEquals(product.getProductCode(), actual.getProductCode());
            assertEquals(product.getProductName().getProductFormalName(), actual.getProductName().getProductFormalName());
            assertEquals(product.getProductName().getProductAbbreviation(), actual.getProductName().getProductAbbreviation());
            assertEquals(product.getProductName().getProductNameKana(), actual.getProductName().getProductNameKana());
            assertEquals(product.getProductType(), actual.getProductType());
            assertEquals(product.getSellingPrice(), actual.getSellingPrice());
            assertEquals(product.getPurchasePrice(), actual.getPurchasePrice());
            assertEquals(product.getCostOfSales(), actual.getCostOfSales());
            assertEquals(product.getTaxType(), actual.getTaxType());
            assertEquals(product.getProductCategoryCode(), actual.getProductCategoryCode());
            assertEquals(product.getMiscellaneousType(), actual.getMiscellaneousType());
            assertEquals(product.getStockManagementTargetType(), actual.getStockManagementTargetType());
            assertEquals(product.getStockAllocationType(), actual.getStockAllocationType());
            assertEquals(product.getSupplierCode(), actual.getSupplierCode());
        }

        @Test
        @DisplayName("商品を更新できる")
        void shouldUpdateProduct() {
            Product product = getProduct("99999999");
            repository.save(product);

            product = repository.findById(product.getProductCode().getValue()).get();
            Product updatedProduct = Product.of(product.getProductCode().getValue(), "更新後商品正式名", "更新後商品略称", "更新後商品名カナ", "1", 2000, 3000, 4000, 2, "99999999", 0, 1, 5, "99999999", 6);
            repository.save(updatedProduct);

            Product actual = repository.findById(product.getProductCode().getValue()).get();
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
            List<SubstituteProduct> substituteProducts = IntStream.range(0, 10).mapToObj(i -> getSubstituteProduct(product.getProductCode().getValue(), String.format("%08d", i))).toList();
            Product newProduct = Product.of(product, substituteProducts, List.of(), List.of());
            repository.save(newProduct);

            ProductList actual = repository.selectAll();
            assertEquals(10, actual.asList().getFirst().getSubstituteProduct().size());
        }

        @Test
        @DisplayName("代替商品を登録できる")
        void shouldRegisterSubstituteProduct() {
            Product product = getProduct("99999999");
            SubstituteProduct substituteProduct = getSubstituteProduct(product.getProductCode().getValue(), "00000000");
            Product newProduct = Product.of(product, List.of(substituteProduct), List.of(), List.of());
            repository.save(newProduct);

            Product actual = repository.findById(product.getProductCode().getValue()).get();
            assertEquals(1, actual.getSubstituteProduct().size());
            assertEquals(substituteProduct, actual.getSubstituteProduct().get(0));
        }

        @Test
        @DisplayName("代替商品を更新できる")
        void shouldUpdateSubstituteProduct() {
            Product product = getProduct("99999999");
            SubstituteProduct substituteProduct = getSubstituteProduct(product.getProductCode().getValue(), "00000000");
            Product newProduct = Product.of(product, List.of(substituteProduct), List.of(), List.of());
            repository.save(newProduct);

            newProduct = repository.findById(product.getProductCode().getValue()).get();
            SubstituteProduct updatedSubstituteProduct = SubstituteProduct.of(product.getProductCode().getValue(), "00000000", 2);
            newProduct = Product.of(newProduct, List.of(updatedSubstituteProduct), List.of(), List.of());
            repository.save(newProduct);

            Product actual = repository.findById(product.getProductCode().getValue()).get();
            assertEquals(1, actual.getSubstituteProduct().size());
            assertEquals(updatedSubstituteProduct, actual.getSubstituteProduct().get(0));
        }

        @Test
        @DisplayName("代替商品を削除できる")
        void shouldDeleteSubstituteProduct() {
            Product product = getProduct("99999999");
            SubstituteProduct substituteProduct = getSubstituteProduct(product.getProductCode().getValue(), "00000000");
            Product newProduct = Product.of(product, List.of(substituteProduct), List.of(), List.of());
            repository.save(newProduct);

            Product deleteProduct = repository.findById(product.getProductCode().getValue()).get();
            repository.deleteById(deleteProduct);

            Optional<Product> actual = repository.findById(product.getProductCode().getValue());
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
            List<Bom> boms = IntStream.range(0, 10).mapToObj(i -> getBom(product.getProductCode().getValue(), String.format("%08d", i))).toList();
            Product newProduct = Product.of(product, List.of(), boms, List.of());
            repository.save(newProduct);

            ProductList actual = repository.selectAll();
            assertEquals(10, actual.asList().getFirst().getBoms().size());
        }

        @Test
        @DisplayName("部品表を登録できる")
        void shouldRegisterBom() {
            Product product = getProduct("99999999");
            Bom bom = getBom(product.getProductCode().getValue(), "00000000");
            Product newProduct = Product.of(product, List.of(), List.of(bom), List.of());
            repository.save(newProduct);

            Product actual = repository.findById(product.getProductCode().getValue()).get();
            assertEquals(1, actual.getBoms().size());
            assertEquals(bom, actual.getBoms().get(0));
        }

        @Test
        @DisplayName("部品表を更新できる")
        void shouldUpdateBom() {
            Product product = getProduct("99999999");
            Bom bom = getBom(product.getProductCode().getValue(), "00000000");
            Product newProduct = Product.of(product, List.of(), List.of(bom), List.of());
            repository.save(newProduct);

            newProduct = repository.findById(product.getProductCode().getValue()).get();
            Bom updatedBom = Bom.of(product.getProductCode().getValue(), "00000000", 2);
            newProduct = Product.of(newProduct, List.of(), List.of(updatedBom), List.of());
            repository.save(newProduct);

            Product actual = repository.findById(product.getProductCode().getValue()).get();
            assertEquals(1, actual.getBoms().size());
            assertEquals(updatedBom, actual.getBoms().get(0));
        }

        @Test
        @DisplayName("部品表を削除できる")
        void shouldDeleteBom() {
            Product product = getProduct("99999999");
            Bom bom = getBom(product.getProductCode().getValue(), "00000000");
            Product newProduct = Product.of(product, List.of(), List.of(bom), List.of());
            repository.save(newProduct);

            Product deleteProduct = repository.findById(product.getProductCode().getValue()).get();
            repository.deleteById(deleteProduct);

            Optional<Product> actual = repository.findById(product.getProductCode().getValue());
            assertEquals(Optional.empty(), actual);
        }
    }

    @Nested
    @DisplayName("顧客別販売単価")
    class CustomerSpecificSellingPriceTest {
        @Test
        @DisplayName("顧客別販売単価一覧を取得できる")
        void shouldRetrieveAllCustomerSpecificSellingPrices() {
            Product product = getProduct("99999999");
            List<CustomerSpecificSellingPrice> customerSpecificSellingPrices = IntStream.range(0, 10).mapToObj(i -> getCustomerSpecificSellingPrice(product.getProductCode().getValue(), String.format("%08d", i))).toList();
            Product newProduct = Product.of(product, List.of(), List.of(), customerSpecificSellingPrices);
            repository.save(newProduct);

            ProductList actual = repository.selectAll();
            assertEquals(10, actual.asList().getFirst().getCustomerSpecificSellingPrices().size());
        }

        @Test
        @DisplayName("顧客別販売単価を登録できる")
        void shouldRegisterCustomerSpecificSellingPrice() {
            Product product = getProduct("99999999");
            CustomerSpecificSellingPrice customerSpecificSellingPrice = getCustomerSpecificSellingPrice(product.getProductCode().getValue(), "00000000");
            Product newProduct = Product.of(product, List.of(), List.of(), List.of(customerSpecificSellingPrice));
            repository.save(newProduct);

            Product actual = repository.findById(product.getProductCode().getValue()).get();
            assertEquals(1, actual.getCustomerSpecificSellingPrices().size());
            assertEquals(customerSpecificSellingPrice, actual.getCustomerSpecificSellingPrices().get(0));
        }

        @Test
        @DisplayName("顧客別販売単価を更新できる")
        void shouldUpdateCustomerSpecificSellingPrice() {
            Product product = getProduct("99999999");
            CustomerSpecificSellingPrice customerSpecificSellingPrice = getCustomerSpecificSellingPrice(product.getProductCode().getValue(), "00000000");
            Product newProduct = Product.of(product, List.of(), List.of(), List.of(customerSpecificSellingPrice));
            repository.save(newProduct);

            newProduct = repository.findById(product.getProductCode().getValue()).get();
            CustomerSpecificSellingPrice updatedCustomerSpecificSellingPrice = CustomerSpecificSellingPrice.of(product.getProductCode().getValue(), "00000000", 2);
            newProduct = Product.of(newProduct, List.of(), List.of(), List.of(updatedCustomerSpecificSellingPrice));
            repository.save(newProduct);

            Product actual = repository.findById(product.getProductCode().getValue()).get();
            assertEquals(1, actual.getCustomerSpecificSellingPrices().size());
            assertEquals(updatedCustomerSpecificSellingPrice, actual.getCustomerSpecificSellingPrices().get(0));
        }

        @Test
        @DisplayName("顧客別販売単価を削除できる")
        void shouldDeleteCustomerSpecificSellingPrice() {
            Product product = getProduct("99999999");
            CustomerSpecificSellingPrice customerSpecificSellingPrice = getCustomerSpecificSellingPrice(product.getProductCode().getValue(), "00000000");
            Product newProduct = Product.of(product, List.of(), List.of(), List.of(customerSpecificSellingPrice));
            repository.save(newProduct);

            Product deleteProduct = repository.findById(product.getProductCode().getValue()).get();
            repository.deleteById(deleteProduct);

            Optional<Product> actual = repository.findById(product.getProductCode().getValue());
            assertEquals(Optional.empty(), actual);
        }
    }

}
