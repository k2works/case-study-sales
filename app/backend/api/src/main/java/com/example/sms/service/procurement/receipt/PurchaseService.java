package com.example.sms.service.procurement.receipt;

import com.example.sms.domain.model.procurement.purchase.PurchaseOrder;
import com.example.sms.domain.model.procurement.purchase.PurchaseOrderList;
import com.example.sms.domain.model.procurement.purchase.PurchaseOrderNumber;
import com.example.sms.domain.model.system.autonumber.AutoNumber;
import com.example.sms.domain.model.system.autonumber.DocumentTypeCode;
import com.example.sms.service.master.department.DepartmentRepository;
import com.example.sms.service.master.employee.EmployeeRepository;
import com.example.sms.service.master.partner.PartnerRepository;
import com.example.sms.service.master.product.ProductRepository;
import com.example.sms.service.procurement.payment.PurchasePaymentRepository;
import com.example.sms.service.procurement.purchase.PurchaseOrderCriteria;
import com.example.sms.service.procurement.purchase.PurchaseOrderRepository;
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
    final PurchaseOrderRepository purchaseOrderRepository;
    final PurchasePaymentRepository purchasePaymentRepository;
    final ProductRepository productRepository;
    final DepartmentRepository departmentRepository;
    final PartnerRepository partnerRepository;
    final EmployeeRepository employeeRepository;
    final AutoNumberService autoNumberService;

    public PurchaseService(
            PurchaseOrderRepository purchaseOrderRepository,
            PurchasePaymentRepository purchasePaymentRepository,
            ProductRepository productRepository,
            DepartmentRepository departmentRepository,
            PartnerRepository partnerRepository,
            EmployeeRepository employeeRepository,
            AutoNumberService autoNumberService) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.purchasePaymentRepository = purchasePaymentRepository;
        this.productRepository = productRepository;
        this.departmentRepository = departmentRepository;
        this.partnerRepository = partnerRepository;
        this.employeeRepository = employeeRepository;
        this.autoNumberService = autoNumberService;
    }

    /**
     * 仕入一覧
     */
    public PurchaseOrderList selectAll() {
        return purchaseOrderRepository.selectAll();
    }

    /**
     * 仕入一覧（ページング）
     */
    public PageInfo<PurchaseOrder> selectAllWithPageInfo() {
        return purchaseOrderRepository.selectAllWithPageInfo();
    }

    /**
     * 仕入新規登録
     */
    public void register(PurchaseOrder purchaseOrder) {
        if (purchaseOrder.getPurchaseOrderNumber() == null) {
            String purchaseOrderNumber = generatePurchaseOrderNumber(purchaseOrder);

            purchaseOrder = PurchaseOrder.of(
                    purchaseOrderNumber,
                    Objects.requireNonNull(Objects.requireNonNull(purchaseOrder.getPurchaseOrderDate()).getValue()),
                    Objects.requireNonNull(Objects.requireNonNull(purchaseOrder.getSalesOrderNumber()).getValue()),
                    Objects.requireNonNull(Objects.requireNonNull(purchaseOrder.getSupplierCode()).getCode()).getValue(),
                    purchaseOrder.getSupplierCode().getBranchNumber(),
                    Objects.requireNonNull(purchaseOrder.getPurchaseManagerCode()).getValue(),
                    Objects.requireNonNull(Objects.requireNonNull(purchaseOrder.getDesignatedDeliveryDate()).getValue()),
                    Objects.requireNonNull(purchaseOrder.getWarehouseCode()).getValue(),
                    Objects.requireNonNull(purchaseOrder.getTotalPurchaseAmount()).getAmount(),
                    Objects.requireNonNull(purchaseOrder.getTotalConsumptionTax()).getAmount(),
                    purchaseOrder.getRemarks(),
                    Objects.requireNonNull(purchaseOrder.getPurchaseOrderLines())
            );
        }
        purchaseOrderRepository.save(purchaseOrder);
    }

    /**
     * 仕入編集
     */
    public void save(PurchaseOrder purchaseOrder) {
        purchaseOrderRepository.save(purchaseOrder);
    }

    /**
     * 仕入削除
     */
    public void delete(PurchaseOrder purchaseOrder) {
        purchaseOrderRepository.delete(Objects.requireNonNull(purchaseOrder.getPurchaseOrderNumber()).getValue());
    }

    /**
     * 仕入検索
     */
    public PurchaseOrder find(String purchaseOrderNumber) {
        return purchaseOrderRepository.findByPurchaseOrderNumber(purchaseOrderNumber).orElse(null);
    }

    /**
     * 仕入検索（ページング）
     */
    public PageInfo<PurchaseOrder> searchPurchaseOrderWithPageInfo(PurchaseOrderCriteria criteria) {
        return purchaseOrderRepository.searchWithPageInfo(criteria);
    }

    /**
     * 仕入番号生成
     */
    private String generatePurchaseOrderNumber(PurchaseOrder purchaseOrder) {
        String code = DocumentTypeCode.発注.getCode();
        LocalDateTime purchaseOrderDate = Objects.requireNonNull(Objects.requireNonNull(purchaseOrder.getPurchaseOrderDate()).getValue());
        LocalDateTime yearMonth = YearMonth.of(purchaseOrderDate.getYear(), purchaseOrderDate.getMonth()).atDay(1).atStartOfDay();
        Integer autoNumber = autoNumberService.getNextDocumentNumber(code, yearMonth);
        String purchaseOrderNumber = PurchaseOrderNumber.generate(code, yearMonth, autoNumber);
        autoNumberService.save(AutoNumber.of(code, yearMonth, autoNumber));
        autoNumberService.incrementDocumentNumber(code, yearMonth);
        return purchaseOrderNumber;
    }
}
