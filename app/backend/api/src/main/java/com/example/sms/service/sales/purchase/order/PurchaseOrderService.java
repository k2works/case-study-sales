package com.example.sms.service.sales.purchase.order;

import com.example.sms.domain.model.sales.purchase.order.PurchaseOrder;
import com.example.sms.domain.model.sales.purchase.order.PurchaseOrderList;
import com.example.sms.domain.model.sales.purchase.order.PurchaseOrderNumber;
import com.example.sms.domain.model.system.autonumber.AutoNumber;
import com.example.sms.domain.model.system.autonumber.DocumentTypeCode;
import com.example.sms.domain.model.sales.purchase.order.rule.PurchaseOrderRuleCheckList;
import com.example.sms.domain.service.sales.purchase.order.PurchaseOrderDomainService;
import com.example.sms.domain.type.money.Money;
import com.example.sms.service.system.autonumber.AutoNumberService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Objects;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * 発注サービス
 */
@Service
@Transactional
@Slf4j
public class PurchaseOrderService {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final AutoNumberService autoNumberService;
    private final PurchaseOrderDomainService purchaseOrderDomainService;

    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository, 
                              AutoNumberService autoNumberService,
                              PurchaseOrderDomainService purchaseOrderDomainService) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.autoNumberService = autoNumberService;
        this.purchaseOrderDomainService = purchaseOrderDomainService;
    }

    /**
     * 発注一覧
     */
    public PurchaseOrderList selectAll() {
        return purchaseOrderRepository.selectAll();
    }

    /**
     * 発注一覧（ページング）
     */
    public PageInfo<PurchaseOrder> selectAllWithPageInfo() {
        return purchaseOrderRepository.selectAllWithPageInfo();
    }

    /**
     * 発注新規登録
     */
    public void register(PurchaseOrder purchaseOrder) {
        notNull(purchaseOrder, "発注データは必須です。");
        
        if (purchaseOrder.getPurchaseOrderNumber() == null || 
            purchaseOrder.getPurchaseOrderNumber().getValue().isEmpty()) {
            String purchaseOrderNumber = generatePurchaseOrderNumber(purchaseOrder);
            
            // 金額を自動計算
            Money totalPurchaseAmount = purchaseOrderDomainService.calculateTotalPurchaseAmount(purchaseOrder);
            Money totalConsumptionTax = purchaseOrderDomainService.calculateTotalConsumptionTax(purchaseOrder);
            
            purchaseOrder = PurchaseOrder.of(
                    purchaseOrderNumber,
                    Objects.requireNonNull(purchaseOrder.getPurchaseOrderDate().getValue()),
                    purchaseOrder.getSalesOrderNumber(),
                    Objects.requireNonNull(purchaseOrder.getSupplierCode().getValue()),
                    purchaseOrder.getSupplierBranchNumber(),
                    Objects.requireNonNull(purchaseOrder.getPurchaseManagerCode().getValue()),
                    Objects.requireNonNull(purchaseOrder.getDesignatedDeliveryDate().getValue()),
                    purchaseOrder.getWarehouseCode(),
                    totalPurchaseAmount.getAmount(),
                    totalConsumptionTax.getAmount(),
                    purchaseOrder.getRemarks(),
                    Objects.requireNonNull(purchaseOrder.getPurchaseOrderLines())
            );
        }
        purchaseOrderRepository.save(purchaseOrder);
    }

    /**
     * 発注編集
     */
    public void save(PurchaseOrder purchaseOrder) {
        notNull(purchaseOrder, "発注データは必須です。");
        purchaseOrderRepository.save(purchaseOrder);
    }

    /**
     * 発注削除
     */
    public void delete(String purchaseOrderNumber) {
        notNull(purchaseOrderNumber, "発注番号は必須です。");
        purchaseOrderRepository.delete(purchaseOrderNumber);
    }

    /**
     * 発注検索
     */
    public PurchaseOrder find(String purchaseOrderNumber) {
        notNull(purchaseOrderNumber, "発注番号は必須です。");
        return purchaseOrderRepository.findByPurchaseOrderNumber(purchaseOrderNumber).orElse(null);
    }

    /**
     * 発注検索（ページング）
     */
    public PageInfo<PurchaseOrder> searchPurchaseOrderWithPageInfo(PurchaseOrderCriteria criteria) {
        notNull(criteria, "検索条件は必須です。");
        return purchaseOrderRepository.searchWithPageInfo(criteria);
    }

    /**
     * 発注ルールチェック
     */
    public PurchaseOrderRuleCheckList checkRule() {
        PurchaseOrderList purchaseOrders = purchaseOrderRepository.selectAll();
        return purchaseOrderDomainService.checkRule(purchaseOrders);
    }

    /**
     * 発注金額合計計算
     */
    public Money calculateTotalPurchaseAmount(PurchaseOrder purchaseOrder) {
        notNull(purchaseOrder, "発注データは必須です。");
        return purchaseOrderDomainService.calculateTotalPurchaseAmount(purchaseOrder);
    }

    /**
     * 発注消費税合計計算
     */
    public Money calculateTotalConsumptionTax(PurchaseOrder purchaseOrder) {
        notNull(purchaseOrder, "発注データは必須です。");
        return purchaseOrderDomainService.calculateTotalConsumptionTax(purchaseOrder);
    }

    /**
     * 発注番号生成
     */
    private String generatePurchaseOrderNumber(PurchaseOrder purchaseOrder) {
        String code = DocumentTypeCode.発注.getCode();
        LocalDateTime purchaseOrderDate = Objects.requireNonNull(purchaseOrder.getPurchaseOrderDate().getValue());
        LocalDateTime yearMonth = YearMonth.of(purchaseOrderDate.getYear(), purchaseOrderDate.getMonth()).atDay(1).atStartOfDay();
        Integer autoNumber = autoNumberService.getNextDocumentNumber(code, yearMonth);
        String purchaseOrderNumber = PurchaseOrderNumber.generate(code, yearMonth, autoNumber);
        autoNumberService.save(AutoNumber.of(code, yearMonth, autoNumber));
        autoNumberService.incrementDocumentNumber(code, yearMonth);
        return purchaseOrderNumber;
    }
}