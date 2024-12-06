package com.example.sms.service.master.product;

import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.master.product.Product;
import com.example.sms.domain.type.product.*;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;


@Testcontainers
@SpringBootTest
@ActiveProfiles("container")
public class ProductRepositoryContainerTest {
    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:15"))
                    .withUsername("root")
                    .withPassword("password")
                    .withDatabaseName("postgres");

    @Autowired
    ProductRepository repository;

    @DynamicPropertySource
    static void setup(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
    }

    private Product getProduct(String productCode) {
        return TestDataFactoryImpl.product(productCode, "商品正式名", "商品略称", "商品名カナ", ProductType.その他, 1000, 2000, 3000, TaxType.外税, "00000000", MiscellaneousType.適用外, StockManagementTargetType.対象, StockAllocationType.引当済, "00000000", 5);
    }

    @Test
    @DisplayName("楽観ロックが正常に動作すること")
    void testOptimisticLockingWithThreads() throws InterruptedException {
        // Productを新規作成して保存
        Product product1 = getProduct("99999999");
        repository.save(product1);

        // 同じIDのProductをもう一度データベースから取得
        Product product2 = repository.findById("99999999").orElseThrow();

        // スレッド1でproduct1を更新
        Thread thread1 = new Thread(() -> {
            Product updatedProduct1 = getProduct("99999999");
            repository.save(updatedProduct1);
        });

        // スレッド2でproduct2を更新し、例外を確認
        Thread thread2 = new Thread(() -> {
            Product updatedProduct2 = getProduct("99999999");
            assertThrows(OptimisticLockingFailureException.class, () -> {
                repository.save(updatedProduct2);
            });
        });

        // スレッドを開始
        thread1.start();
        thread2.start();

        // スレッドの終了を待機
        thread1.join();
        thread2.join();
    }
}