package com.example.sms.domain.model.master.partner.supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("仕入先ドメインモデル")
class SupplierTest {

    @Test
    @DisplayName("仕入先を作成できる")
    void shouldCreateSupplier() {
        Supplier supplier = Supplier.of("001", 1, "テスト仕入先");
        
        assertAll(
                () -> assertNotNull(supplier),
                () -> assertEquals("001", supplier.getSupplierCode().getValue()),
                () -> assertEquals(Integer.valueOf(1), supplier.getSupplierCode().getBranchNumber()),
                () -> assertEquals("テスト仕入先", supplier.getSupplierName().getName()),
                () -> assertEquals("テスト仕入先", supplier.getSupplierName().getNameKana())
        );
    }

    @Test
    @DisplayName("ビルダーパターンで仕入先を作成できる")
    void shouldCreateSupplierWithBuilder() {
        Supplier supplier = Supplier.builder()
                .supplierCode(SupplierCode.of("002", 2))
                .supplierName(com.example.sms.domain.model.master.partner.PartnerName.of("テスト仕入先２", "テストシイレサキ２"))
                .build();
        
        assertAll(
                () -> assertNotNull(supplier),
                () -> assertEquals("002", supplier.getSupplierCode().getValue()),
                () -> assertEquals(Integer.valueOf(2), supplier.getSupplierCode().getBranchNumber()),
                () -> assertEquals("テスト仕入先２", supplier.getSupplierName().getName()),
                () -> assertEquals("テストシイレサキ２", supplier.getSupplierName().getNameKana())
        );
    }

    @Test
    @DisplayName("toBuilderで既存の仕入先を変更できる")
    void shouldModifySupplierWithToBuilder() {
        Supplier original = Supplier.of("001", 1, "オリジナル仕入先");
        
        Supplier modified = original.toBuilder()
                .supplierName(com.example.sms.domain.model.master.partner.PartnerName.of("変更後仕入先", "ヘンコウゴシイレサキ"))
                .build();
        
        assertAll(
                () -> assertNotNull(modified),
                () -> assertEquals("001", modified.getSupplierCode().getValue()),
                () -> assertEquals(Integer.valueOf(1), modified.getSupplierCode().getBranchNumber()),
                () -> assertEquals("変更後仕入先", modified.getSupplierName().getName()),
                () -> assertEquals("ヘンコウゴシイレサキ", modified.getSupplierName().getNameKana()),
                // 元のオブジェクトは変更されないことを確認
                () -> assertEquals("オリジナル仕入先", original.getSupplierName().getName())
        );
    }

    @Nested
    @DisplayName("仕入先コード")
    class SupplierCodeTest {
        
        @Test
        @DisplayName("仕入先コードを作成できる")
        void shouldCreateSupplierCode() {
            SupplierCode supplierCode = SupplierCode.of("001", 1);
            
            assertAll(
                    () -> assertEquals("001", supplierCode.getValue()),
                    () -> assertEquals(Integer.valueOf(1), supplierCode.getBranchNumber())
            );
        }

        @Test
        @DisplayName("枝番なしで仕入先コードを作成できる")
        void shouldCreateSupplierCodeWithoutBranchNumber() {
            SupplierCode supplierCode = SupplierCode.of("002");
            
            assertAll(
                    () -> assertEquals("002", supplierCode.getValue()),
                    () -> assertEquals(Integer.valueOf(0), supplierCode.getBranchNumber())
            );
        }

        @Test
        @DisplayName("枝番が0から999の範囲内で有効")
        void shouldValidateBranchNumberRange() {
            assertDoesNotThrow(() -> SupplierCode.of("001", 0));
            assertDoesNotThrow(() -> SupplierCode.of("001", 999));
            assertDoesNotThrow(() -> SupplierCode.of("001", 500));
        }

        @Test
        @DisplayName("枝番が範囲外の場合に例外が発生")
        void shouldThrowExceptionForInvalidBranchNumber() {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class, 
                            () -> SupplierCode.of("001", -1)),
                    () -> assertThrows(IllegalArgumentException.class, 
                            () -> SupplierCode.of("001", 1000))
            );
        }
    }

    @Test
    @DisplayName("Valueアノテーションにより不変オブジェクトとして動作する")
    void shouldBehaveLikeImmutableObject() {
        Supplier supplier1 = Supplier.of("001", 1, "テスト仕入先");
        Supplier supplier2 = Supplier.of("001", 1, "テスト仕入先");
        Supplier supplier3 = Supplier.of("002", 1, "テスト仕入先");
        
        assertAll(
                () -> assertEquals(supplier1, supplier2), // 同じ値なら等価
                () -> assertEquals(supplier1.hashCode(), supplier2.hashCode()), // ハッシュコードも同じ
                () -> assertNotEquals(supplier1, supplier3), // 違う値なら非等価
                () -> assertTrue(supplier1.toString().contains("001")) // toStringにフィールド値が含まれる
        );
    }
}