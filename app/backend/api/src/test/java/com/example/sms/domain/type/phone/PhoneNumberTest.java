package com.example.sms.domain.type.phone;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.DynamicTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("電話番号テスト")
class PhoneNumberTest {

    @Nested
    @DisplayName("正常値テスト")
    class Normal {
        @DisplayName("正しい電話番号であること")
        @TestFactory
        Stream<DynamicTest> should_be_a_valid_phone_number() {
            return Stream.of(
                            "03-1234-5678",
                            "03 5678 1234",
                            "090-1234-5678",
                            "090 9999 8888")
                    .map(input ->
                            DynamicTest.dynamicTest("Accepted: " + input, () ->
                                    assertDoesNotThrow(() -> PhoneNumber.of(input))));
        }
    }

    @Nested
    @DisplayName("境界値テスト")
    class Boundary {
        @DisplayName("境界範囲内である")
        @TestFactory
        Stream<DynamicTest> should_be_accepted() {
            return Stream.of(
                            "03-1234-5678",  // 最小限の正しい桁数
                            "090-1234-5678") // モバイル番号の正しい形式
                    .map(input ->
                            DynamicTest.dynamicTest("Accepted: " + input, () ->
                                    assertDoesNotThrow(() -> PhoneNumber.of(input))));
        }

        @DisplayName("境界範囲外である")
        @TestFactory
        Stream<DynamicTest> should_be_rejected() {
            return Stream.of(
                            "03-12-56789", // 桁数不足
                            "001-1234-5678", // 無効な市外局番
                            "090-123-567890", // 桁数多すぎ
                            "123-4567-8900", // 市外局番が「0」で始まらない
                            "090-1234-567", // 誤った桁数
                            "0800-1234-5678") // 無効な形式（11桁として誤った末尾）
                    .map(input ->
                            DynamicTest.dynamicTest("Rejected: " + input, () ->
                                    assertThrows(IllegalArgumentException.class, () -> PhoneNumber.of(input))));
        }
    }

    @Nested
    @DisplayName("異常値テスト")
    class Abnormal {
        @DisplayName("不正な電話番号である")
        @TestFactory
        Stream<DynamicTest> should_reject_invalid_phone_number() {
            return Stream.of(
                            null,
                            "",
                            " ",
                            "03-abc-def8", // 含まれる不正文字
                            "03--1234-5678", // 不正なハイフン
                            "\t",
                            "０９０ー１２３４ー５６７８", // 全角文字
                            "0901234567a") // 数字以外の文字を含む
                    .map(input ->
                            DynamicTest.dynamicTest("Rejected: " + input, () ->
                                    assertThrows(RuntimeException.class, () -> PhoneNumber.of(input))));
        }
    }

    @Nested
    @DisplayName("極端値テスト")
    class Extreme {
        @TestFactory
        Stream<DynamicTest> should_reject_extreme_input() {
            return Stream.of(
                            "0".repeat(50), // 異常に長い入力
                            "090-" + "1".repeat(1000)) // 異常に長い部分値
                    .map(input ->
                            DynamicTest.dynamicTest("Rejected: " + input.length() + " chars", () ->
                                    assertThrows(IllegalArgumentException.class, () -> PhoneNumber.of(input))));
        }
    }
}