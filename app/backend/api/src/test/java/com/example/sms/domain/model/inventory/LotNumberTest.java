package com.example.sms.domain.model.inventory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ロット番号")
class LotNumberTest {

    @Test
    @DisplayName("正常なロット番号を作成できる")
    void shouldCreateValidLotNumber() {
        // 英数字のみ
        LotNumber lot1 = LotNumber.of("LOT001");
        assertEquals("LOT001", lot1.getValue());

        // ハイフンを含む
        LotNumber lot2 = LotNumber.of("LOT-2024-01");
        assertEquals("LOT-2024-01", lot2.getValue());

        // 最大20文字
        LotNumber lot3 = LotNumber.of("12345678901234567890");
        assertEquals("12345678901234567890", lot3.getValue());
    }

    @Test
    @DisplayName("nullの場合は例外が発生する")
    void shouldThrowExceptionForNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> LotNumber.of(null)
        );
        assertEquals("ロット番号は必須です", exception.getMessage());
    }

    @Test
    @DisplayName("空文字の場合は例外が発生する")
    void shouldThrowExceptionForEmptyString() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> LotNumber.of("")
        );
        assertEquals("ロット番号は必須です", exception.getMessage());
    }

    @Test
    @DisplayName("20文字を超える場合は例外が発生する")
    void shouldThrowExceptionForTooLongString() {
        String tooLong = "123456789012345678901"; // 21文字
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> LotNumber.of(tooLong)
        );
        assertEquals("ロット番号は20文字以下である必要があります", exception.getMessage());
    }

    @Test
    @DisplayName("英数字とハイフン以外を含む場合は例外が発生する")
    void shouldThrowExceptionForInvalidCharacters() {
        // スペースを含む
        IllegalArgumentException exception1 = assertThrows(
            IllegalArgumentException.class,
            () -> LotNumber.of("LOT 001")
        );
        assertEquals("ロット番号は英数字とハイフンのみ使用できます", exception1.getMessage());

        // アンダースコアを含む
        IllegalArgumentException exception2 = assertThrows(
            IllegalArgumentException.class,
            () -> LotNumber.of("LOT_001")
        );
        assertEquals("ロット番号は英数字とハイフンのみ使用できます", exception2.getMessage());

        // 日本語を含む
        IllegalArgumentException exception3 = assertThrows(
            IllegalArgumentException.class,
            () -> LotNumber.of("ロット001")
        );
        assertEquals("ロット番号は英数字とハイフンのみ使用できます", exception3.getMessage());
    }

    @Test
    @DisplayName("toStringメソッドが値を返す")
    void shouldReturnValueInToString() {
        LotNumber lotNumber = LotNumber.of("LOT123");
        assertEquals("LOT123", lotNumber.toString());
    }

    @Test
    @DisplayName("equalsとhashCodeが正しく動作する")
    void shouldWorkEqualsAndHashCode() {
        LotNumber lot1 = LotNumber.of("LOT001");
        LotNumber lot2 = LotNumber.of("LOT001");
        LotNumber lot3 = LotNumber.of("LOT002");

        // 同じ値なら等しい
        assertEquals(lot1, lot2);
        assertEquals(lot1.hashCode(), lot2.hashCode());

        // 異なる値なら等しくない
        assertNotEquals(lot1, lot3);
        assertNotEquals(lot1.hashCode(), lot3.hashCode());
    }
}