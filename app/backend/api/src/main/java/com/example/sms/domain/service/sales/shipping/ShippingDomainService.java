package com.example.sms.domain.service.sales.shipping;

import com.example.sms.domain.model.sales.shipping.Shipping;
import com.example.sms.domain.model.sales.shipping.ShippingList;
import com.example.sms.domain.model.sales.shipping.rule.ShipmentDeliveryOverDueRule;
import com.example.sms.domain.model.sales.shipping.rule.ShippingRule;
import com.example.sms.domain.model.sales.shipping.rule.ShippingRuleCheckList;
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
        ShippingRule shipmentDeliveryOverDueRule = new ShipmentDeliveryOverDueRule();

        BiConsumer<String, String> addCheck = (orderNumber, message) -> {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put(orderNumber, message);
            checkList.add(errorMap);
        };

        shippingList.forEach(shipping -> {
            if (shipmentDeliveryOverDueRule.isSatisfiedBy(shipping)) {
                addCheck.accept(shipping.getOrderNumber().getValue(), "納期を超過しています。");
            }
        });

        return new ShippingRuleCheckList(checkList);
    }
}