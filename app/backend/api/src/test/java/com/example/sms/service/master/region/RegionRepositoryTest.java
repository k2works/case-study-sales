package com.example.sms.service.master.region;

import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.master.region.Region;
import com.example.sms.domain.model.master.region.RegionList;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Testcontainers
@ActiveProfiles("container")
@DisplayName("地域レポジトリ")
class RegionRepositoryTest {

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
    private RegionRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    private Region getRegion(String regionCode) {
        return TestDataFactoryImpl.getRegion(regionCode);
    }

    @Nested
    @DisplayName("地域")
    class RegionTest {
        private static final String REGION_CODE_1 = "R001";
        private static final String REGION_CODE_2 = "R002";
        private static final String REGION_CODE_3 = "R003";

        @Test
        @DisplayName("地域一覧を取得できる")
        void shouldReturnRegionList() {
            repository.save(getRegion(REGION_CODE_1));
            repository.save(getRegion(REGION_CODE_2));
            repository.save(getRegion(REGION_CODE_3));
            RegionList regionList = repository.selectAll();
            assertEquals(3, regionList.size());
        }

        @Test
        @DisplayName("地域を登録できる")
        void shouldRegisterRegion() {
            Region region = getRegion(REGION_CODE_1);
            repository.save(region);
            Region actual = repository.findById(REGION_CODE_1).orElse(null);
            assertEquals(region, actual);
        }

        @Test
        @DisplayName("地域を更新できる")
        void shouldUpdateRegion() {
            Region region = getRegion(REGION_CODE_1);
            repository.save(region);
            Region updatedRegion = Region.of(REGION_CODE_1, "地域2");
            repository.save(updatedRegion);
            Region actual = repository.findById(REGION_CODE_1).orElse(null);
            assertEquals(updatedRegion, actual);
        }

        @Test
        @DisplayName("地域を削除できる")
        void shouldDeleteRegion() {
            Region region = getRegion(REGION_CODE_1);
            repository.save(region);
            repository.deleteById(region);
            Region actual = repository.findById(REGION_CODE_1).orElse(null);
            assertNull(actual);
        }

        @Test
        @DisplayName("ページング情報を取得できる")
        void shouldReturnPagingInfo() {
            repository.save(getRegion(REGION_CODE_1));
            repository.save(getRegion(REGION_CODE_2));
            repository.save(getRegion(REGION_CODE_3));
            PageInfo<Region> pageInfo = repository.selectAllWithPageInfo();

            assertEquals(3, pageInfo.getList().size());
        }
    }
}