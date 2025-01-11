package com.example.sms.domain.model.master.partner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("取引先グループ")
class PartnerGroupTest {
    private PartnerGroup getPartnerGroup() {
        return PartnerGroup.of("0001", "取引先グループ名");
    }

    @Test
    @DisplayName("取引先グループを作成できる")
    void shouldCreatePartnerGroup() {
        PartnerGroup partnerGroup = getPartnerGroup();

        assertAll(
            () -> assertEquals("0001", partnerGroup.getPartnerGroupCode().getValue()),
            () -> assertEquals("取引先グループ名", partnerGroup.getPartnerGroupName())
        );
    }

    @Test
    @DisplayName("取引先グループコードは以下の条件を満たす")
    void shouldCreatePartnerGroupCode() {
        assertDoesNotThrow(() -> PartnerGroupCode.of("0001"));
        assertThrows(IllegalArgumentException.class, () -> PartnerGroupCode.of(null));
        assertThrows(IllegalArgumentException.class, () -> PartnerGroupCode.of("00011"));
        assertThrows(IllegalArgumentException.class, () -> PartnerGroupCode.of("0001a"));
    }
}
