package com.example.sms.service.master.warehouse;

import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.master.warehouse.Warehouse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
@ActiveProfiles("container")
@DisplayName("倉庫レポジトリ")
class WarehouseRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:15"))
                    .withUsername("root")
                    .withPassword("password")
                    .withDatabaseName("postgres");

    @DynamicPropertySource
    static void setup(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
    }

    @Autowired
    private WarehouseRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    private Warehouse getWarehouse() {
        return TestDataFactoryImpl.getWarehouse("W01", "本社倉庫");
    }

    @Test
    @DisplayName("倉庫一覧を取得できる")
    void shouldRetrieveAllWarehouses() {
        Warehouse warehouse = getWarehouse();

        repository.save(warehouse);

        assertEquals(1, repository.selectAll().size());
    }

    @Test
    @DisplayName("倉庫を登録できる")
    void shouldRegisterAWarehouse() {
        Warehouse warehouse = getWarehouse();

        repository.save(warehouse);

        Warehouse actual = repository.findById(warehouse.getWarehouseCode()).get();
        assertEquals(warehouse.getWarehouseCode(), actual.getWarehouseCode());
        assertEquals(warehouse.getWarehouseName(), actual.getWarehouseName());
    }

    @Test
    @DisplayName("倉庫を更新できる")
    void shouldUpdateAWarehouse() {
        Warehouse warehouse = getWarehouse();
        repository.save(warehouse);

        Warehouse updateWarehouse = Warehouse.of("W01", "支社倉庫", "N", "1234567", "東京都", "千代田区", "1-1-1");
        repository.save(updateWarehouse);

        Warehouse actual = repository.findById(updateWarehouse.getWarehouseCode()).get();
        assertEquals(updateWarehouse.getWarehouseCode(), actual.getWarehouseCode());
        assertEquals(updateWarehouse.getWarehouseName(), actual.getWarehouseName());
    }

    @Test
    @DisplayName("倉庫を削除できる")
    void shouldDeleteAWarehouse() {
        Warehouse warehouse = getWarehouse();
        repository.save(warehouse);

        repository.deleteById(warehouse.getWarehouseCode());

        assertEquals(0, repository.selectAll().size());
    }
}