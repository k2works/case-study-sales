package com.example.sms.domain.service.procurement.purchase;

import com.example.sms.domain.model.procurement.order.PurchaseOrder;
import com.example.sms.domain.model.procurement.order.PurchaseOrderList;
import com.example.sms.domain.model.procurement.order.rule.*;
import com.example.sms.domain.type.money.Money;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 発注ドメインサービス
 */
@Service
public class PurchaseOrderDomainService {

    /**
     * 発注金額合計計算
     */
    public Money calculateTotalPurchaseAmount(PurchaseOrder purchaseOrder) {
        if (purchaseOrder.getPurchaseOrderLines() == null || purchaseOrder.getPurchaseOrderLines().isEmpty()) {
            return Money.of(0);
        }
        
        int total = purchaseOrder.getPurchaseOrderLines().stream()
                .mapToInt(line -> line.getPurchaseUnitPrice().getAmount() 
                        * line.getPurchaseOrderQuantity().getAmount())
                .sum();
        
        return Money.of(total);
    }

    /**
     * 発注消費税合計計算
     */
    public Money calculateTotalConsumptionTax(PurchaseOrder purchaseOrder) {
        if (purchaseOrder.getPurchaseOrderLines() == null || purchaseOrder.getPurchaseOrderLines().isEmpty()) {
            return Money.of(0);
        }
        
        // 仮の消費税率（10%）
        double taxRate = 0.10;
        
        int totalTax = purchaseOrder.getPurchaseOrderLines().stream()
                .mapToInt(line -> {
                    int lineTotal = line.getPurchaseUnitPrice().getAmount() 
                            * line.getPurchaseOrderQuantity().getAmount();
                    return (int) (lineTotal * taxRate);
                })
                .sum();
        
        return Money.of(totalTax);
    }

    /**
     * 発注ルールチェック
     */
    public PurchaseOrderRuleCheckList checkRule(PurchaseOrderList purchaseOrders) {
        List<Map<String, String>> checkList = new ArrayList<>();

        List<PurchaseOrder> orderList = purchaseOrders.asList();
        PurchaseOrderRule purchaseOrderAmountRule = new PurchaseOrderAmountRule();
        PurchaseOrderRule purchaseOrderDeliveryRule = new PurchaseOrderDeliveryRule();
        PurchaseOrderRule purchaseOrderDeliveryOverDueRule = new PurchaseOrderDeliveryOverDueRule();

        BiConsumer<String, String> addCheck = (purchaseOrderNumber, message) -> {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put(purchaseOrderNumber, message);
            checkList.add(errorMap);
        };

        orderList.forEach(purchaseOrder -> {
            // 発注金額ルールチェック
            if (purchaseOrderAmountRule.isSatisfiedBy(purchaseOrder)) {
                addCheck.accept(purchaseOrder.getPurchaseOrderNumber().getValue(), "発注金額が500万円を超えています。");
            }
            
            // 発注納期ルールチェック
            if (purchaseOrderDeliveryRule.isSatisfiedBy(purchaseOrder)) {
                addCheck.accept(purchaseOrder.getPurchaseOrderNumber().getValue(), "指定納期が発注日より前です。");
            }
            
            // 発注納期超過ルールチェック
            if (purchaseOrderDeliveryOverDueRule.isSatisfiedBy(purchaseOrder)) {
                addCheck.accept(purchaseOrder.getPurchaseOrderNumber().getValue(), "納期を超過しています。");
            }
        });

        return new PurchaseOrderRuleCheckList(checkList);
    }
}