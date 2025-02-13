package com.example.sms.domain.type.address;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.DynamicTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("郵便番号テスト")
class PostalCodeTest {

    @Nested
    @DisplayName("正常値テスト")
    class Normal {
        @DisplayName("正しい郵便番号が設定されること")
        @TestFactory
        Stream<DynamicTest> should_be_a_valid_postal_code() {
            return Stream.of(
                            "123-4567",
                            "9876543"
                    )
                    .map(input -> DynamicTest.dynamicTest(
                            "Accepted: " + input,
                            () -> assertDoesNotThrow(() -> new PostalCode(input))
                    ));
        }
    }

    @Nested
    @DisplayName("境界値テスト")
    class Boundary {
        @DisplayName("境界値内の郵便番号が受け入れられること")
        @TestFactory
        Stream<DynamicTest> should_be_accepted() {
            return Stream.of(
                            "000-0000", // 最小値
                            "9999999"   // 最大値
                    )
                    .map(input -> DynamicTest.dynamicTest(
                            "Accepted: " + input,
                            () -> assertDoesNotThrow(() -> new PostalCode(input))
                    ));
        }

        @DisplayName("境界値外の郵便番号が拒否されること")
        @TestFactory
        Stream<DynamicTest> should_be_rejected() {
            return Stream.of(
                            "123-45678", // 8桁
                            "123",       // 3桁
                            "123-45"     // ハイフン付き不完全な長さ
                    )
                    .map(input -> DynamicTest.dynamicTest(
                            "Rejected: " + input,
                            () -> assertThrows(IllegalArgumentException.class, () -> new PostalCode(input))
                    ));
        }
    }

    @Nested
    @DisplayName("異常値テスト")
    class Abnormal {
        @DisplayName("空値や無効な値が拒否されること")
        @TestFactory
        Stream<DynamicTest> should_reject_invalid_postal_code() {
            return Stream.of(
                            null,          // null 値
                            "abc-defg",    // 英字
                            "1234-abcd",   // 混在無効値
                            "!@#-4567",    // 記号
                            "１２３-４５６７" // 全角数字
                    )
                    .map(input -> DynamicTest.dynamicTest(
                            "Rejected: " + input,
                            () -> assertThrows(RuntimeException.class, () -> new PostalCode(input))
                    ));
        }
    }

    @Nested
    @DisplayName("極端値テスト")
    class Extreme {
        @DisplayName("極端に長い値や不正な形式を拒否すること")
        @TestFactory
        Stream<DynamicTest> should_reject_extreme_input() {
            return Stream.of(
                            "123456789012345", // 非常に長い
                            repeat("9", 10000) // 大量の9
                    )
                    .map(input -> DynamicTest.dynamicTest(
                            "Rejected: " + input,
                            () -> assertThrows(IllegalArgumentException.class, () -> new PostalCode(input))
                    ));
        }
    }

    private static String repeat(String str, int count) {
        return str.repeat(count);
    }
}