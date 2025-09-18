package com.example.sms.service.master.locationnumber;

import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.master.locationnumber.LocationNumber;
import com.example.sms.domain.model.master.warehouse.Warehouse;
import com.example.sms.domain.model.master.product.Product;
import com.example.sms.infrastructure.datasource.autogen.model.棚番マスタKey;
import com.example.sms.service.master.warehouse.WarehouseRepository;
import com.example.sms.service.master.product.ProductRepository;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
@ActiveProfiles("container")
@DisplayName("棚番レポジトリ")
class LocationNumberRepositoryTest {

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
    private LocationNumberRepository repository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        // 前提データとして倉庫を作成
        Warehouse warehouse1 = TestDataFactoryImpl.getWarehouse("W01", "本社倉庫");
        Warehouse warehouse2 = TestDataFactoryImpl.getWarehouse("W02", "支社倉庫");
        warehouseRepository.save(warehouse1);
        warehouseRepository.save(warehouse2);

        // 前提データとして商品を作成
        Product product1 = TestDataFactoryImpl.getProduct("P001");
        Product product2 = TestDataFactoryImpl.getProduct("P002");
        Product product3 = TestDataFactoryImpl.getProduct("P003");
        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
    }

    private LocationNumber getLocationNumber() {
        return TestDataFactoryImpl.getLocationNumber("W01", "A001", "P001");
    }

    private 棚番マスタKey getLocationNumberKey() {
        return TestDataFactoryImpl.getLocationNumberKey("W01", "A001", "P001");
    }

    @Test
    @DisplayName("棚番一覧を取得できる")
    void shouldRetrieveAllLocationNumbers() {
        LocationNumber locationNumber = getLocationNumber();

        repository.save(locationNumber);

        assertEquals(1, repository.selectAll().asList().size());
    }

    @Test
    @DisplayName("棚番を登録できる")
    void shouldRegisterALocationNumber() {
        LocationNumber locationNumber = getLocationNumber();

        repository.save(locationNumber);

        棚番マスタKey key = getLocationNumberKey();
        LocationNumber actual = repository.findById(key).get();
        assertEquals(locationNumber.getWarehouseCode(), actual.getWarehouseCode());
        assertEquals(locationNumber.getLocationNumberCode(), actual.getLocationNumberCode());
        assertEquals(locationNumber.getProductCode(), actual.getProductCode());
    }

    @Test
    @DisplayName("棚番を更新できる")
    void shouldUpdateALocationNumber() {
        LocationNumber locationNumber = getLocationNumber();
        repository.save(locationNumber);

        // 同じキーで別の棚番データ（実際は同じキーなので更新テスト）
        LocationNumber updateLocationNumber = LocationNumber.of("W01", "A001", "P001");
        repository.save(updateLocationNumber);

        棚番マスタKey key = getLocationNumberKey();
        LocationNumber actual = repository.findById(key).get();
        assertEquals(updateLocationNumber.getWarehouseCode(), actual.getWarehouseCode());
        assertEquals(updateLocationNumber.getLocationNumberCode(), actual.getLocationNumberCode());
        assertEquals(updateLocationNumber.getProductCode(), actual.getProductCode());
    }

    @Test
    @DisplayName("棚番を削除できる")
    void shouldDeleteALocationNumber() {
        LocationNumber locationNumber = getLocationNumber();
        repository.save(locationNumber);

        棚番マスタKey key = getLocationNumberKey();
        repository.deleteById(key);

        assertEquals(0, repository.selectAll().asList().size());
    }

    @Test
    @DisplayName("倉庫コードで棚番を検索できる")
    void shouldFindLocationNumbersByWarehouseCode() {
        LocationNumber locationNumber1 = LocationNumber.of("W01", "A001", "P001");
        LocationNumber locationNumber2 = LocationNumber.of("W01", "A002", "P002");
        LocationNumber locationNumber3 = LocationNumber.of("W02", "B001", "P003");

        repository.save(locationNumber1);
        repository.save(locationNumber2);
        repository.save(locationNumber3);

        assertEquals(2, repository.findByWarehouseCode("W01").asList().size());
        assertEquals(1, repository.findByWarehouseCode("W02").asList().size());
    }

    @Test
    @DisplayName("棚番コードで棚番を検索できる")
    void shouldFindLocationNumbersByLocationNumberCode() {
        LocationNumber locationNumber1 = LocationNumber.of("W01", "A001", "P001");
        LocationNumber locationNumber2 = LocationNumber.of("W02", "A001", "P002");
        LocationNumber locationNumber3 = LocationNumber.of("W01", "B001", "P003");

        repository.save(locationNumber1);
        repository.save(locationNumber2);
        repository.save(locationNumber3);

        assertEquals(2, repository.findByLocationNumberCode("A001").asList().size());
        assertEquals(1, repository.findByLocationNumberCode("B001").asList().size());
    }

    @Test
    @DisplayName("存在しない棚番を検索した場合は空のOptionalが返される")
    void shouldReturnEmptyOptionalWhenLocationNumberNotFound() {
        棚番マスタKey key = TestDataFactoryImpl.getLocationNumberKey("W99", "Z999", "P999");
        assertTrue(repository.findById(key).isEmpty());
    }

    @Test
    @DisplayName("検索条件で棚番を検索できる")
    void shouldSearchLocationNumbersByCriteria() {
        LocationNumber locationNumber1 = LocationNumber.of("W01", "A001", "P001");
        LocationNumber locationNumber2 = LocationNumber.of("W01", "A002", "P002");
        LocationNumber locationNumber3 = LocationNumber.of("W02", "B001", "P001");

        repository.save(locationNumber1);
        repository.save(locationNumber2);
        repository.save(locationNumber3);

        LocationNumberCriteria criteria1 = new LocationNumberCriteria("W01", null, null);
        assertEquals(2, repository.searchWithPageInfo(criteria1).getList().size());

        LocationNumberCriteria criteria2 = new LocationNumberCriteria(null, "A00", null);
        assertEquals(2, repository.searchWithPageInfo(criteria2).getList().size());

        LocationNumberCriteria criteria3 = new LocationNumberCriteria(null, null, "P001");
        assertEquals(2, repository.searchWithPageInfo(criteria3).getList().size());
    }
}