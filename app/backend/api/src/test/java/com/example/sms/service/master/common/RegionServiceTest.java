package com.example.sms.service.master.common;

import com.example.sms.IntegrationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.common.region.Region;
import com.example.sms.domain.model.common.region.RegionList;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@IntegrationTest
@DisplayName("地域サービス")
class RegionServiceTest {
    @Autowired
    private RegionService regionService;
    @Autowired
    private TestDataFactory testDataFactory;

    @BeforeEach
    void setUp() {
        testDataFactory.setUpForRegionService();
    }

    @Nested
    @DisplayName("地域")
    class RegionTest {
        @Test
        @DisplayName("地域一覧を取得できる")
        void shouldGetRegionList() {
            RegionList result = regionService.selectAll();
            assertEquals(3, result.size());
            assertEquals("R001", result.asList().getFirst().getRegionCode().getValue());
        }

        @Test
        @DisplayName("地域を新規登録できる")
        void shouldRegisterRegion() {
            Region region = TestDataFactoryImpl.getRegion("R004");
            regionService.register(region);
            Region result = regionService.find("R004");
            assertEquals(region, result);
        }

        @Test
        @DisplayName("地域を編集できる")
        void shouldSaveRegion() {
            Region region = TestDataFactoryImpl.getRegion("R001");
            Region updatedRegion = Region.of(region.getRegionCode().getValue(), "編集後の地域");
            regionService.save(region);
            regionService.save(updatedRegion);
            Region result = regionService.find("R001");
            assertNotEquals(region, result);
            assertEquals(updatedRegion, result);
        }

        @Test
        @DisplayName("地域を削除できる")
        void shouldDeleteRegion() {
            Region region = TestDataFactoryImpl.getRegion("R003");
            regionService.register(region);
            regionService.delete(region);
            Region result = regionService.find("R003");
            assertEquals(null, result);
        }

        @Nested
        @DisplayName("地域検索")
        class RegionSearchTest {
            @Test
            @DisplayName("地域コードで検索できる")
            void shouldFindRegionByCode() {
                RegionCriteria criteria = RegionCriteria.builder().regionCode("R001").build();
                PageInfo<Region> result = regionService.searchWithPageInfo(criteria);
                assertEquals(1, result.getList().size());
                assertEquals("R001", result.getList().getFirst().getRegionCode().getValue());
            }

            @Test
            @DisplayName("地域名で検索できる")
            void shouldFindRegionByName() {
                RegionCriteria criteria = RegionCriteria.builder().regionName("地域").build();
                PageInfo<Region> result = regionService.searchWithPageInfo(criteria);
                assertEquals(3, result.getList().size());
            }

            @Test
            @DisplayName("全ての地域をページングで取得できる")
            void shouldGetAllRegionsWithPaging() {
                PageInfo<Region> result = regionService.selectAllWithPageInfo();
                assertEquals(3, result.getList().size());
                assertEquals(3, result.getTotal());
            }
        }
    }
}
