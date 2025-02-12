package com.example.sms.domain.type.address;

import org.junit.jupiter.api.*;

import java.util.List;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.repeat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class AddressTest {

    @Nested
    @DisplayName("正常値テスト")
    class Normal {
        @DisplayName("正しい住所が設定されること")
        @TestFactory
        Stream<DynamicTest> should_be_a_valid_address() {
            List<String[]> validInputs = List.of(
                    new String[]{"123-4567", "東京都", "Shibuya-ku 1-1-1", "Building A"},
                    new String[]{"987-6543", "東京都", "Namba 2-2-2", "Room 201"}
            );

            return validInputs.stream()
                    .map(input -> dynamicTest(
                            "Accepted: " + String.join(", ", input),
                            () -> assertDoesNotThrow(() -> new Address(
                                    PostalCode.of(input[0]),
                                    Prefecture.fromName(input[1]),
                                    input[2],
                                    input[3]
                            ))
                    ));
        }
    }

    @Nested
    @DisplayName("境界値テスト")
    class Boundary {
        @DisplayName("境界値内の住所が受け入れられること")
        @TestFactory
        Stream<DynamicTest> should_be_accepted() {
            List<String[]> validInputs = List.of(
                    new String[]{"123-4567", "東京都", repeat("x", 40), "Building A"},
                    new String[]{"987-6543", "東京都", "Namba 2-2-2", repeat("x", 40)},
                    new String[]{"987-6543", "東京都", repeat("x", 40), repeat("x", 40)}
            );

            return validInputs.stream()
                    .map(input -> dynamicTest(
                            "Accepted: " + String.join(", ", input),
                            () -> assertDoesNotThrow(() -> new Address(
                                    PostalCode.of(input[0]),
                                    Prefecture.fromName(input[1]),
                                    input[2],
                                    input[3]
                            ))
                    ));
        }

        @DisplayName("境界値外の住所が拒否されること")
        @TestFactory
        Stream<DynamicTest> should_be_rejected() {
            List<String[]> validInputs = List.of(
                    new String[]{"123-4567", "東京都", repeat("x", 41), "Building A"},
                    new String[]{"987-6543", "東京都", "Namba 2-2-2", repeat("x", 41)},
                    new String[]{"987-6543", "東京都", repeat("x", 41), repeat("x", 41)}
            );

            return validInputs.stream()
                    .map(input -> dynamicTest(
                            "Rejected: " + String.join(", ", input),
                            () -> assertThrows(IllegalArgumentException.class, () -> new Address(
                                    PostalCode.of(input[0]),
                                    Prefecture.fromName(input[1]),
                                    input[2],
                                    input[3]
                            ))
                    ));
        }
    }
    @Nested
    @DisplayName("異常値テスト")
    class Abnormal {
        @DisplayName("空値や無効な値が拒否されること")
        @TestFactory
        Stream<DynamicTest> should_reject_invalid_address() {
            List<String[]> inValidInputs = List.of(
                    new String[]{"123-4567", "東京都", null, "Building A"},
                    new String[]{"987-6543", "東京都", "Namba 2-2-2", null},
                    new String[]{"987-6543", "東京都", null, null}
            );

            return inValidInputs.stream()
                    .map(input -> dynamicTest(
                            "Rejected: " + String.join(", ", input),
                            () -> assertThrows(NullPointerException.class, () -> new Address(
                                    PostalCode.of(input[0]),
                                    Prefecture.fromName(input[1]),
                                    input[2],
                                    input[3]
                            ))
                    ));
        }
    }

    @Nested
    @DisplayName("極端値テスト")
    class Extreme {
        @DisplayName("極端に長い住所を拒否すること")
        @TestFactory
        Stream<DynamicTest> should_reject_extreme_input() {
            List<String[]> inValidInputs = List.of(
                    new String[]{"123-4567", "東京都", repeat("x", 10000), repeat("x", 10000)},
                    new String[]{"987-6543", "東京都", repeat("x", 100000), repeat("x", 100000)},
                    new String[]{"987-6543", "東京都", repeat("x", 1000000), repeat("x", 1000000)}
            );

            return inValidInputs.stream()
                    .map(input -> dynamicTest(
                            "Rejected: " + String.join(", ", input),
                            () -> assertThrows(IllegalArgumentException.class, () -> new Address(
                                    PostalCode.of(input[0]),
                                    Prefecture.fromName(input[1]),
                                    input[2],
                                    input[3]
                            ))
                    ));
        }
    }
}
