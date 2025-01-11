package com.example.sms.domain.model.master.partner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("取引先")
class PartnerTest {
    private Partner getPartner () {
        return Partner.of(
                "001",
                "取引先名",
                "取引先名カナ",
                1,
                "123-4567",
                "東京都",
                "住所１",
                "住所２",
                0,
                0,
                "取引先グループコード",
                100000,
                100000
        );
    }

    @Test
    @DisplayName("取引先を作成できる")
    void shouldCreatePartner() {
        Partner partner = getPartner();

        assertAll(
                () -> assertEquals("001", partner.getPartnerCode().getValue()),
                () -> assertEquals("取引先名", partner.getPartnerName().getName()),
                () -> assertEquals("取引先名カナ", partner.getPartnerName().getNameKana())
        );
    }

    @Test
    @DisplayName("取引先コードは以下の条件を満たす")
    void shouldCreatePartnerCode() {
        assertDoesNotThrow(() -> PartnerCode.of("001"));
        assertThrows(IllegalArgumentException.class, () -> PartnerCode.of(null));
        assertThrows(IllegalArgumentException.class, () -> PartnerCode.of("0011"));
        assertThrows(IllegalArgumentException.class, () -> PartnerCode.of("001a"));
    }

    @Test
    @DisplayName("取引先名は以下の条件を満たす")
    void shouldCreatePartnerName() {
        assertDoesNotThrow(() -> PartnerName.of("取引先名", "取引先名カナ"));
        assertThrows(IllegalArgumentException.class, () -> PartnerName.of(null, "取引先名カナ"));
        assertThrows(IllegalArgumentException.class, () -> PartnerName.of("取引先名", null));
        assertThrows(IllegalArgumentException.class, () -> PartnerName.of("取引先名", "取引先カナ名".repeat(50)));
        assertThrows(IllegalArgumentException.class, () -> PartnerName.of("取引先名".repeat(50), "取引先カナ名"));
        assertThrows(IllegalArgumentException.class, () -> PartnerName.of("取引先名".repeat(50), "取引先カナ名".repeat(50)));
    }
}
