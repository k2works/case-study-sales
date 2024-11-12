package com.example.sms.domain.model.common.quantity;

import com.example.sms.domain.type.quantity.UnitType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("数量テスト")
class QuantityTest {

    @Test
    @DisplayName("1倍にすると同じ数量を返す")
    void testTimes_WhenMultipliedByOne_ShouldReturnSameQuantity() {
        Quantity quantity = new Quantity(3, UnitType.個);
        Quantity newQuantity = quantity.times(1);
        Assertions.assertEquals(quantity.getAmount(), newQuantity.getAmount());
        Assertions.assertEquals(quantity.getUnit(), newQuantity.getUnit());
    }

    @Test
    @DisplayName("正の数で乗算すると乗算された数量を返す")
    void testTimes_WhenMultipliedByPositiveNumber_ShouldReturnMultipliedQuantity() {
        Quantity quantity = new Quantity(3, UnitType.個);
        Quantity newQuantity = quantity.times(3);
        Assertions.assertEquals(9, newQuantity.getAmount());
        Assertions.assertEquals(quantity.getUnit(), newQuantity.getUnit());
    }

    @Test
    @DisplayName("0倍にすると数量は0を返す")
    void testTimes_WhenMultipliedByZero_ShouldReturnZeroQuantity() {
        Quantity quantity = new Quantity(3, UnitType.個);
        Quantity newQuantity = quantity.times(0);
        Assertions.assertEquals(0, newQuantity.getAmount());
        Assertions.assertEquals(quantity.getUnit(), newQuantity.getUnit());
    }

    @Test
    @DisplayName("負の数で乗算すると例外をスローする")
    void testTimes_WhenMultipliedByNegativeNumber_ShouldThrowException() {
        Quantity quantity = new Quantity(3, UnitType.個);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            quantity.times(-1);
        });
    }
}
