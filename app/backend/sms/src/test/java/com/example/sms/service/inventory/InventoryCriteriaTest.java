package com.example.sms.service.inventory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("在庫検索条件")
class InventoryCriteriaTest {

    @Nested
    @DisplayName("検索条件のMap変換")
    class ToMapTest {

        @Test
        @DisplayName("すべての検索条件が設定されている場合")
        void toMap_AllCriteriaSet() {
            InventoryCriteria criteria = InventoryCriteria.builder()
                    .warehouseCode("WH001")
                    .productCode("PR001")
                    .lotNumber("LOT001")
                    .stockCategory("1")
                    .qualityCategory("G")
                    .productName("商品A")
                    .warehouseName("倉庫A")
                    .hasStock(true)
                    .isAvailable(true)
                    .build();

            Map<String, Object> result = criteria.toMap();

            assertEquals(9, result.size());
            assertEquals("WH001", result.get("倉庫コード"));
            assertEquals("PR001", result.get("商品コード"));
            assertEquals("LOT001", result.get("ロット番号"));
            assertEquals("1", result.get("在庫区分"));
            assertEquals("G", result.get("良品区分"));
            assertEquals("商品A", result.get("商品名"));
            assertEquals("倉庫A", result.get("倉庫名"));
        }

        @Test
        @DisplayName("一部の条件のみ設定されている場合")
        void toMap_PartialCriteriaSet() {
            InventoryCriteria criteria = InventoryCriteria.builder()
                    .warehouseCode("WH001")
                    .productCode("PR001")
                    .build();

            Map<String, Object> result = criteria.toMap();

            assertEquals(2, result.size());
            assertEquals("WH001", result.get("倉庫コード"));
            assertEquals("PR001", result.get("商品コード"));
            assertNull(result.get("ロット番号"));
        }

        @Test
        @DisplayName("空文字やnullが含まれる場合")
        void toMap_WithEmptyAndNullValues() {
            InventoryCriteria criteria = InventoryCriteria.builder()
                    .warehouseCode("")
                    .productCode(null)
                    .lotNumber("  ")
                    .stockCategory("1")
                    .build();

            Map<String, Object> result = criteria.toMap();

            assertEquals(1, result.size());
            assertEquals("1", result.get("在庫区分"));
            assertNull(result.get("倉庫コード"));
            assertNull(result.get("商品コード"));
            assertNull(result.get("ロット番号"));
        }

        @Test
        @DisplayName("在庫有無がfalseの場合はMapに含まれない")
        void toMap_HasStockFalse() {
            InventoryCriteria criteria = InventoryCriteria.builder()
                    .warehouseCode("WH001")
                    .hasStock(false)
                    .build();

            Map<String, Object> result = criteria.toMap();

            assertEquals(1, result.size());
            assertEquals("WH001", result.get("倉庫コード"));
            assertNull(result.get("在庫有無"));
        }

        @Test
        @DisplayName("利用可能がfalseの場合はMapに含まれない")
        void toMap_IsAvailableFalse() {
            InventoryCriteria criteria = InventoryCriteria.builder()
                    .warehouseCode("WH001")
                    .isAvailable(false)
                    .build();

            Map<String, Object> result = criteria.toMap();

            assertEquals(1, result.size());
            assertEquals("WH001", result.get("倉庫コード"));
            assertNull(result.get("利用可能"));
        }

        @Test
        @DisplayName("すべての条件が未設定の場合")
        void toMap_NoCriteria() {
            InventoryCriteria criteria = InventoryCriteria.builder().build();

            Map<String, Object> result = criteria.toMap();

            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("前後の空白をトリムする")
        void toMap_TrimValues() {
            InventoryCriteria criteria = InventoryCriteria.builder()
                    .warehouseCode("  WH001  ")
                    .productCode("  PR001  ")
                    .lotNumber("  LOT001  ")
                    .stockCategory("  1  ")
                    .qualityCategory("  G  ")
                    .productName("  商品A  ")
                    .warehouseName("  倉庫A  ")
                    .build();

            Map<String, Object> result = criteria.toMap();

            assertEquals(7, result.size());
            assertEquals("WH001", result.get("倉庫コード"));
            assertEquals("PR001", result.get("商品コード"));
            assertEquals("LOT001", result.get("ロット番号"));
            assertEquals("1", result.get("在庫区分"));
            assertEquals("G", result.get("良品区分"));
            assertEquals("商品A", result.get("商品名"));
            assertEquals("倉庫A", result.get("倉庫名"));
        }
    }

    @Nested
    @DisplayName("ビルダーパターン")
    class BuilderTest {

        @Test
        @DisplayName("toBuilderで既存インスタンスから新しいインスタンスを作成")
        void toBuilder() {
            InventoryCriteria original = InventoryCriteria.builder()
                    .warehouseCode("WH001")
                    .productCode("PR001")
                    .build();

            InventoryCriteria modified = original.toBuilder()
                    .productCode("PR002")
                    .lotNumber("LOT001")
                    .build();

            assertEquals("WH001", modified.getWarehouseCode());
            assertEquals("PR002", modified.getProductCode());
            assertEquals("LOT001", modified.getLotNumber());

            // 元のインスタンスは変更されない
            assertEquals("PR001", original.getProductCode());
            assertNull(original.getLotNumber());
        }
    }

    @Nested
    @DisplayName("equals と hashCode")
    class EqualsAndHashCodeTest {

        @Test
        @DisplayName("同じ値を持つインスタンスは等しい")
        void equals_SameValues() {
            InventoryCriteria criteria1 = InventoryCriteria.builder()
                    .warehouseCode("WH001")
                    .productCode("PR001")
                    .build();

            InventoryCriteria criteria2 = InventoryCriteria.builder()
                    .warehouseCode("WH001")
                    .productCode("PR001")
                    .build();

            assertEquals(criteria1, criteria2);
            assertEquals(criteria1.hashCode(), criteria2.hashCode());
        }

        @Test
        @DisplayName("異なる値を持つインスタンスは等しくない")
        void equals_DifferentValues() {
            InventoryCriteria criteria1 = InventoryCriteria.builder()
                    .warehouseCode("WH001")
                    .build();

            InventoryCriteria criteria2 = InventoryCriteria.builder()
                    .warehouseCode("WH002")
                    .build();

            assertNotEquals(criteria1, criteria2);
        }
    }
}