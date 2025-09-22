package com.example.sms.domain.model.master.warehouse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("倉庫コードのテスト")
class WarehouseCodeTest {

    @Test
    @DisplayName("正しい形式の倉庫コードを作成できる")
    void testValidWarehouseCode() {
        // W + 2桁の数字の正しい形式
        assertDoesNotThrow(() -> WarehouseCode.of("W01"));
        assertDoesNotThrow(() -> WarehouseCode.of("W02"));
        assertDoesNotThrow(() -> WarehouseCode.of("W99"));
        assertDoesNotThrow(() -> WarehouseCode.of("W00"));
    }

    @Test
    @DisplayName("nullの倉庫コードは例外をスローする")
    void testNullWarehouseCode() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> WarehouseCode.of(null)
        );
        assertEquals("倉庫コードは必須です", exception.getMessage());
    }

    @Test
    @DisplayName("空文字の倉庫コードは例外をスローする")
    void testEmptyWarehouseCode() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> WarehouseCode.of("")
        );
        assertEquals("倉庫コードは必須です", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "001",    // 先頭がWでない
        "A01",    // 先頭がWでない
        "w01",    // 小文字のw
        "W1",     // 2文字（短い）
        "W001",   // 4文字（長い）
        "WA1",    // 2文字目が数字でない
        "W0A",    // 3文字目が数字でない
        "WAB",    // 2,3文字目が数字でない
        "123",    // 全て数字
        "ABC"     // 全て英字
    })
    @DisplayName("不正な形式の倉庫コードは例外をスローする")
    void testInvalidFormatWarehouseCode(String invalidCode) {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> WarehouseCode.of(invalidCode)
        );
        assertEquals("倉庫コードは先頭がW、残り2文字が数字の形式である必要があります（例: W01）", exception.getMessage());
    }

    @Test
    @DisplayName("倉庫コードの値を取得できる")
    void testGetValue() {
        WarehouseCode code = WarehouseCode.of("W01");
        assertEquals("W01", code.getValue());
    }
}