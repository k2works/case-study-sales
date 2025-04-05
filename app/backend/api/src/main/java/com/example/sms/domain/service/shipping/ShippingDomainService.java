package com.example.sms.domain.service.shipping;

import com.example.sms.domain.model.shipping.Shipping;
import com.example.sms.domain.model.shipping.ShippingList;
import com.example.sms.domain.model.shipping.rule.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 出荷ドメインサービス
 */
@Service
public class ShippingDomainService {

    /**
     * 出荷ルールチェック
     */
    public ShippingRuleCheckList checkRule(ShippingList shippings) {
        List<Map<String, String>> checkList = new ArrayList<>();

        List<Shipping> shippingList = shippings.asList();
        ShippingRule shipmentQuantityRule = new ShipmentQuantityRule();
        ShippingRule shipmentDeliveryOverDueRule = new ShipmentDeliveryOverDueRule();

        BiConsumer<String, String> addCheck = (orderNumber, message) -> {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put(orderNumber, message);
            checkList.add(errorMap);
        };

        shippingList.forEach(shipping -> {
            if (shipmentQuantityRule.isSatisfiedBy(shipping)) {
                addCheck.accept(shipping.getOrderNumber().getValue(), "出荷数量が受注数量を超えています。");
            }
            if (shipmentDeliveryOverDueRule.isSatisfiedBy(shipping)) {
                addCheck.accept(shipping.getOrderNumber().getValue(), "納期を超過しています。");
            }
        });

        return new ShippingRuleCheckList(checkList);
    }
}