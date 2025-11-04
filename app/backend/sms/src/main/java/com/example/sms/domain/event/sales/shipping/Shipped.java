package com.example.sms.domain.event.sales.shipping;

public record Shipped(ShippingAggregate shipping) {
    public ShippingAggregate getShipping() {
        return shipping;
    }
}