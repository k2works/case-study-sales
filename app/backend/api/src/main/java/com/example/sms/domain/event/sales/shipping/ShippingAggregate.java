package com.example.sms.domain.event.sales.shipping;

import com.example.sms.domain.model.master.product.ProductCode;
import com.example.sms.domain.model.sales.order.OrderNumber;
import com.example.sms.domain.model.sales.order.ShippingDate;
import com.example.sms.domain.model.sales.shipping.Shipping;
import com.example.sms.domain.type.quantity.Quantity;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.data.domain.AbstractAggregateRoot;

@Value
@Builder
@EqualsAndHashCode(callSuper=false)
public class ShippingAggregate extends AbstractAggregateRoot<ShippingAggregate> {
    OrderNumber orderNumber;
    ProductCode productCode;
    String warehouseCode;
    Quantity shippedQuantity;
    ShippingDate shippingDate;

    public static ShippingAggregate fromDomainModel(Shipping domainModel) {
        return ShippingAggregate.builder()
                .orderNumber(domainModel.getOrderNumber())
                .productCode(domainModel.getProductCode())
                .warehouseCode(domainModel.getWarehouseCode())
                .shippedQuantity(domainModel.getShippedQuantity())
                .shippingDate(domainModel.getShippingDate())
                .build();
    }
}