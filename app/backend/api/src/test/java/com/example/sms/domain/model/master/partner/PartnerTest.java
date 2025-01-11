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
                "1234567",
                "東京都",
                "住所１",
                "住所２",
                0,
                0,
                "0001",
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
                () -> assertEquals("取引先名カナ", partner.getPartnerName().getNameKana()),
                () -> assertEquals(1, partner.getSupplierType()),
                () -> assertEquals("1234567", partner.getAddress().getPostalCode().getValue()),
                () -> assertEquals("東京都", partner.getAddress().getPrefecture().toString()),
                () -> assertEquals("住所１", partner.getAddress().getAddress1()),
                () -> assertEquals("住所２", partner.getAddress().getAddress2()),
                () -> assertEquals(0, partner.getTradeProhibitedFlag()),
                () -> assertEquals(0, partner.getMiscellaneousType()),
                () -> assertEquals("0001", partner.getPartnerGroupCode().getValue()),
                () -> assertEquals(100000, partner.getCreditLimit()),
                () -> assertEquals(100000, partner.getTemporaryCreditIncrease()),
                () -> assertEquals(0, partner.getCustomers().size()),
                () -> assertEquals(0, partner.getVendors().size())
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

    @Test
    @DisplayName("郵便番号は以下の条件を満たす")
    void shouldCreatePostalCode() {
        assertDoesNotThrow(() -> PostalCode.of("1234567"));
        assertDoesNotThrow(() -> PostalCode.of("123-4567"));
        assertThrows(IllegalArgumentException.class, () -> PostalCode.of(null));
        assertThrows(IllegalArgumentException.class, () -> PostalCode.of("12345678"));
        assertThrows(IllegalArgumentException.class, () -> PostalCode.of("XX34567"));
        assertThrows(IllegalArgumentException.class, () -> PostalCode.of("123456-"));
        assertThrows(IllegalArgumentException.class, () -> PostalCode.of("-123456"));
        assertThrows(IllegalArgumentException.class, () -> PostalCode.of("1-23456"));
    }

    @Test
    @DisplayName("住所は以下の条件を満たす")
    void shouldCreateAddress() {
        assertDoesNotThrow(() -> Address.of("1234567", "東京都", "住所１", "住所２"));
        assertDoesNotThrow(() -> Address.of("1234567", "東京都", "住".repeat(40), "住".repeat(40)));
        assertThrows(IllegalArgumentException.class, () -> Address.of(null, "東京都", "住所１", "住所２"));
        assertThrows(IllegalArgumentException.class, () -> Address.of("1234567", "東京都", "住".repeat(41), "住所２"));
        assertThrows(IllegalArgumentException.class, () -> Address.of("1234567", "東京都", "住所１", "住".repeat(41)));
        assertThrows(IllegalArgumentException.class, () -> Address.of("1234567", "東京都", "住".repeat(100), "住".repeat(100)));
        assertThrows(IllegalArgumentException.class, () -> Address.of("1234567", null, "住所１", "住所２"));
        assertThrows(IllegalArgumentException.class, () -> Address.of("1234567", "東京都", null, "住所２"));
        assertThrows(IllegalArgumentException.class, () -> Address.of("1234567", "東京都", "住所１", null));
        assertThrows(IllegalArgumentException.class, () -> Address.of("1234567", "異世界", "住所１", "住所２"));
        }
}
