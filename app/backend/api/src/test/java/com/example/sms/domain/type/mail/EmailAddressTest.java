package com.example.sms.domain.type.mail;

import org.junit.jupiter.api.*;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.repeat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("メールアドレステスト")
class EmailAddressTest {

    @Nested
    @DisplayName("正常値テスト")
    class Normal {
        @DisplayName("正しいアドレスであること")
        @TestFactory
        Stream<DynamicTest> should_be_a_valid_address() {
            return Stream.of(
                            "jane@example.com",
                            "jane01@example.com",
                            "jane.doe@example.com")
                    .map(input ->
                            dynamicTest("Accepted: " + input, () ->
                                    assertDoesNotThrow(() -> new EmailAddress(input))));
        }
    }

    @Nested
    @DisplayName("境界値テスト")
    class Boundary {
        @DisplayName("境界範囲内である")
        @TestFactory
        Stream<DynamicTest> should_be_accepted() {
            return Stream.of(
                            "aaa@example.com",
                            repeat("X", 64) + "@example.com")
                    .map(input ->
                            dynamicTest("Accepted: " + input, () ->
                                    assertDoesNotThrow(() -> new EmailAddress(input))));
        }

        @DisplayName("境界範囲外である")
        @TestFactory
        Stream<DynamicTest> should_be_rejected() {
            return Stream.of(
                            "a@example.com",
                            repeat("X", 64) + "@something.com",
                            repeat("X", 65) + "@example.com",
                            "address_with_invalid_char@example.com",
                            "jane@doe@example.com",
                            ".jane@example.com",
                            "jane.@example.com",
                            "jane.a.doe@example.com")
                    .map(input ->
                            dynamicTest("Rejected: " + input, () ->
                                    assertThrows(IllegalArgumentException.class, () -> new EmailAddress(input))));
        }
    }

    @Nested
    @DisplayName("異常値テスト")
    class Abnormal {
        @DisplayName("不正なアドレスである")
        @TestFactory
        Stream<DynamicTest> should_reject_invalid_address() {
            return Stream.of(
                    null,
                    "null",
                    "nil",
                    "0",
                    "",
                    " ",
                    "\t",
                    "\n",
                    "john.doe\n@example.com",
                    " @example.com",
                    "%20@example.com",
                    "john.d%20e@example.com",
                    "john..doe@example.com",
                    "",
                    "ＥＸＡＭＰＬＥ@hospital.ＣＯＭ",
                    "=0@$*^%;<!>.:\\()%#\"")
                    .map(input ->
                            dynamicTest("Rejected: " + input, () ->
                                    assertThrows(RuntimeException.class, () -> new EmailAddress(input))));
        }
    }

    @Nested
    @DisplayName("極端値テスト")
    class Extreme {
        @TestFactory
        Stream<DynamicTest> should_reject_extreme_input() {
            return Stream.<Supplier<String>>of(
                            () -> repeat("X", 10000),
                            () -> repeat("X", 100000),
                            () -> repeat("X", 1000000),
                            () -> repeat("X", 10000000),
                            () -> repeat("X", 20000000),
                            () -> repeat("X", 40000000))
                    .map(input ->
                            dynamicTest("Rejected: " + input.get().length() + " chars", () ->
                                    assertThrows(RuntimeException.class,
                                            () -> new EmailAddress(input.get()))));
        }
    }
}