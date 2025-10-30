package com.example.sms.service.procurement.receipt;

import com.example.sms.domain.model.procurement.receipt.Purchase;
import com.example.sms.domain.model.procurement.receipt.PurchaseList;
import com.example.sms.domain.model.procurement.receipt.PurchaseNumber;
import com.example.sms.domain.model.procurement.receipt.rule.PurchaseRuleCheckList;
import com.example.sms.domain.model.system.autonumber.AutoNumber;
import com.example.sms.domain.model.system.autonumber.DocumentTypeCode;
import com.example.sms.domain.service.procurement.receipt.PurchaseDomainService;
import com.example.sms.service.master.department.DepartmentRepository;
import com.example.sms.service.master.employee.EmployeeRepository;
import com.example.sms.service.master.partner.PartnerRepository;
import com.example.sms.service.master.product.ProductRepository;
import com.example.sms.service.procurement.payment.PurchasePaymentRepository;
import com.example.sms.service.system.autonumber.AutoNumberService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Objects;

/**
 * 仕入サービス
 */
@Service
@Transactional
@Slf4j
public class PurchaseService {
    final PurchasePaymentRepository purchasePaymentRepository;
    final PurchaseRepository purchaseRepository;
    final PurchaseDomainService purchaseDomainService;
    final ProductRepository productRepository;
    final DepartmentRepository departmentRepository;
    final PartnerRepository partnerRepository;
    final EmployeeRepository employeeRepository;
    final AutoNumberService autoNumberService;

    public PurchaseService(
            PurchasePaymentRepository purchasePaymentRepository,
            PurchaseRepository purchaseRepository,
            PurchaseDomainService purchaseDomainService,
            ProductRepository productRepository,
            DepartmentRepository departmentRepository,
            PartnerRepository partnerRepository,
            EmployeeRepository employeeRepository,
            AutoNumberService autoNumberService) {
        this.purchasePaymentRepository = purchasePaymentRepository;
        this.purchaseRepository = purchaseRepository;
        this.purchaseDomainService = purchaseDomainService;
        this.productRepository = productRepository;
        this.departmentRepository = departmentRepository;
        this.partnerRepository = partnerRepository;
        this.employeeRepository = employeeRepository;
        this.autoNumberService = autoNumberService;
    }

    /**
     * 仕入一覧
     */
    public PurchaseList selectAll() {
        return purchaseRepository.selectAll();
    }

    /**
     * 仕入一覧（ページング）
     */
    public PageInfo<Purchase> selectAllWithPageInfo() {
        return purchaseRepository.selectAllWithPageInfo();
    }

    /**
     * 仕入新規登録
     */
    public void register(Purchase purchase) {
        if (purchase.getPurchaseNumber() == null) {
            String purchaseNumber = generatePurchaseNumber(purchase);

            purchase = Purchase.of(
                    purchaseNumber,
                    Objects.requireNonNull(Objects.requireNonNull(purchase.getPurchaseDate()).getValue()),
                    Objects.requireNonNull(Objects.requireNonNull(purchase.getSupplierCode()).getCode()).getValue(),
                    purchase.getSupplierCode().getBranchNumber(),
                    Objects.requireNonNull(purchase.getPurchaseManagerCode()).getValue(),
                    Objects.requireNonNull(purchase.getStartDate()),
                    purchase.getPurchaseOrderNumber() != null ? purchase.getPurchaseOrderNumber().getValue() : null,
                    Objects.requireNonNull(purchase.getDepartmentCode()).getValue(),
                    Objects.requireNonNull(purchase.getTotalPurchaseAmount()).getAmount(),
                    Objects.requireNonNull(purchase.getTotalConsumptionTax()).getAmount(),
                    purchase.getRemarks(),
                    Objects.requireNonNull(purchase.getPurchaseLines())
            );
        }
        purchaseRepository.save(purchase);
    }

    /**
     * 仕入編集
     */
    public void save(Purchase purchase) {
        purchaseRepository.save(purchase);
    }

    /**
     * 仕入削除
     */
    public void delete(Purchase purchase) {
        purchaseRepository.delete(Objects.requireNonNull(purchase.getPurchaseNumber()).getValue());
    }

    /**
     * 仕入検索
     */
    public Purchase find(String purchaseNumber) {
        return purchaseRepository.findByPurchaseNumber(purchaseNumber).orElse(null);
    }

    /**
     * 仕入検索（ページング）
     */
    public PageInfo<Purchase> searchPurchaseWithPageInfo(PurchaseCriteria criteria) {
        return purchaseRepository.searchWithPageInfo(criteria);
    }

    /**
     * 仕入番号生成
     */
    private String generatePurchaseNumber(Purchase purchase) {
        String code = DocumentTypeCode.仕入.getCode();
        LocalDateTime purchaseDate = Objects.requireNonNull(Objects.requireNonNull(purchase.getPurchaseDate()).getValue());
        LocalDateTime yearMonth = YearMonth.of(purchaseDate.getYear(), purchaseDate.getMonth()).atDay(1).atStartOfDay();
        Integer autoNumber = autoNumberService.getNextDocumentNumber(code, yearMonth);
        String purchaseNumber = code + yearMonth.format(java.time.format.DateTimeFormatter.ofPattern("yyMM")) + String.format("%04d", autoNumber);
        autoNumberService.save(AutoNumber.of(code, yearMonth, autoNumber));
        autoNumberService.incrementDocumentNumber(code, yearMonth);
        return purchaseNumber;
    }

    /**
     * 仕入ルールチェック
     */
    public PurchaseRuleCheckList checkRule() {
        PurchaseList purchases = purchaseRepository.selectAll();
        return purchaseDomainService.checkRule(purchases);
    }
}
