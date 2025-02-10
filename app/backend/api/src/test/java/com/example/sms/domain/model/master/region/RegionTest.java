package com.example.sms.domain.model.master.region;

import com.example.sms.domain.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("地域テスト")
class RegionTest {
    private Region getRegion() {
        return Region.of("R001", "地域名");
    }

    @Test
    @DisplayName("地域を作成できる")
    void shouldCreateRegion() {
        Region region = getRegion();

        assertAll(
            () -> assertEquals("R001", region.getRegionCode().getValue()),
            () -> assertEquals("地域名", region.getRegionName())
        );
    }

    @Test
    @DisplayName("地域コードは以下の条件を満たす")
    void shouldCreateRegionCode() {
        assertDoesNotThrow(() -> RegionCode.of("R001"));
        assertThrows(BusinessException.class, () -> RegionCode.of(null));
        assertThrows(BusinessException.class, () -> RegionCode.of("R0011"));
        assertThrows(BusinessException.class, () -> RegionCode.of("R001a"));
        assertThrows(BusinessException.class, () -> RegionCode.of("0001"));
        assertThrows(BusinessException.class, () -> RegionCode.of("001"));
    }
}
