package com.example.sms.service.sales_order;

import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.employee.EmployeeCode;
import com.example.sms.domain.model.sales_order.SalesOrder;
import com.example.sms.domain.model.sales_order.SalesOrderLine;
import com.example.sms.domain.model.sales_order.SalesOrderList;
import com.example.sms.domain.service.sales_order.SalesOrderDomainService;
import com.example.sms.infrastructure.Pattern2ReadCSVUtil;
import com.example.sms.infrastructure.datasource.sales_order.SalesOrderUploadCSV;
import com.example.sms.service.master.department.DepartmentRepository;
import com.example.sms.service.master.employee.EmployeeRepository;
import com.example.sms.service.master.partner.PartnerRepository;
import com.example.sms.service.master.product.ProductRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.apache.commons.lang3.Validate.*;


/**
 * 受注サービス
 */
@Service
@Transactional
public class SalesOrderService {
    final SalesOrderRepository salesOrderRepository;
    final ProductRepository productRepository;
    final DepartmentRepository departmentRepository;
    final PartnerRepository partnerRepository;
    final EmployeeRepository employeeRepository;
    final SalesOrderDomainService salesOrderDomainService;

    public SalesOrderService(SalesOrderRepository salesOrderRepository, ProductRepository productRepository, DepartmentRepository departmentRepository, PartnerRepository partnerRepository, EmployeeRepository employeeRepository, SalesOrderDomainService salesOrderDomainService) {
        this.salesOrderRepository = salesOrderRepository;
        this.productRepository = productRepository;
        this.departmentRepository = departmentRepository;
        this.partnerRepository = partnerRepository;
        this.employeeRepository = employeeRepository;
        this.salesOrderDomainService = salesOrderDomainService;
    }

    /**
     * 受注一覧
     */
    public SalesOrderList selectAll() {
        return salesOrderRepository.selectAll();
    }

    /**
     * 受注一覧（ページング）
     */
    public PageInfo<SalesOrder> selectAllWithPageInfo() {
        return salesOrderRepository.selectAllWithPageInfo();
    }

    /**
     * 受注新規登録
     */
    public void register(SalesOrder salesOrder) {
        salesOrderRepository.save(salesOrder);
    }

    /**
     * 受注編集
     */
    public void save(SalesOrder salesOrder) {
        salesOrderRepository.save(salesOrder);
    }

    /**
     * 受注削除
     */
    public void delete(SalesOrder salesOrder) {
        salesOrderRepository.delete(salesOrder);
    }

    /**
     * 受注検索
     */
    public SalesOrder find(String salesOrderCode) {
        return salesOrderRepository.findById(salesOrderCode).orElse(null);
    }

    /**
     * 受注検索（ページング）
     */
    public PageInfo<SalesOrder> searchSalesOrderWithPageInfo(SalesOrderCriteria criteria) {
        return salesOrderRepository.searchWithPageInfo(criteria);
    }

    /**
     * 受注ルールチェック
     */
    public List<Map<String, String>> checkRule() {
        SalesOrderList salesOrders = salesOrderRepository.selectAllNotComplete();
        return salesOrderDomainService.checkRule(salesOrders);
    }

    /**
     * CSVファイルアップロード
     */
    public SalesOrderUploadErrorList uploadCsvFile(MultipartFile file) {
        notNull(file, "アップロードファイルは必須です。");
        isTrue(!file.isEmpty(), "アップロードファイルが空です。");
        notNull(file.getOriginalFilename(), "アップロードファイル名は必須です。");
        isTrue(file.getOriginalFilename().endsWith(".csv"), "アップロードファイルがCSVではありません。");
        isTrue(file.getSize() < 10000000, "アップロードファイルが大きすぎます。");

        Pattern2ReadCSVUtil<SalesOrderUploadCSV> csvUtil = new Pattern2ReadCSVUtil<>();
        List<SalesOrderUploadCSV> dataList = csvUtil.readCSV(SalesOrderUploadCSV.class, file, "UTF-8");
        isTrue(!dataList.isEmpty(), "CSVファイルの読み込みに失敗しました");

        SalesOrderUploadErrorList errorList = validateErrors(dataList);
        if (!errorList.isEmpty()) return errorList;

        SalesOrderList salesOrderList = convert(dataList);
        salesOrderRepository.save(salesOrderList);
        return errorList;
    }

    private SalesOrderUploadErrorList validateErrors(List<SalesOrderUploadCSV> dataList) {
        List<Map<String, String>> checkResult = new ArrayList<>();

        dataList.forEach(data -> {
            checkEntityExistence(
                    departmentRepository.findById(DepartmentId.of(data.getDepartmentCode(), data.getDepartmentStartDate())),
                    checkResult,
                    data.getOrderNumber(),
                    "部門マスタに存在しません:" + data.getDepartmentCode()
            );

            checkEntityExistence(
                    partnerRepository.findById(data.getCustomerCode()),
                    checkResult,
                    data.getOrderNumber(),
                    "取引先マスタに存在しません:" + data.getCustomerCode()
            );

            checkEntityExistence(
                    productRepository.findById(data.getProductCode()),
                    checkResult,
                    data.getOrderNumber(),
                    "商品マスタに存在しません:" + data.getProductCode()
            );

            checkEntityExistence(
                    employeeRepository.findById(EmployeeCode.of(data.getEmployeeCode())),
                    checkResult,
                    data.getOrderNumber(),
                    "社員マスタに存在しません:" + data.getEmployeeCode()
            );
        });

        return new SalesOrderUploadErrorList(checkResult);
    }

    private void checkEntityExistence(Optional<?> entity, List<Map<String, String>> checkResult, String orderNumber, String errorMessage) {
        if (entity.isEmpty()) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put(orderNumber, errorMessage);
            checkResult.add(errorMap);
        }
    }

    private static SalesOrderList convert(List<SalesOrderUploadCSV> dataList) {
        // 結果を格納するリスト
        List<SalesOrder> salesOrders = new ArrayList<>();

        // OrderNumber単位でマッピングするため、一時的にMapを活用
        Map<String, SalesOrder> orderMap = new HashMap<>();

        for (SalesOrderUploadCSV csv : dataList) {
            String orderNumber = csv.getOrderNumber();
            if (orderNumber != null) {
                orderNumber = orderNumber.replaceAll("\\s", ""); // すべての空白文字を削除
            }
            // 既にそのOrderNumberのSalesOrderが存在するかを確認
            SalesOrder existingOrder = orderMap.get(orderNumber);

            if (existingOrder == null) {
                // 新しくSalesOrderを作成
                SalesOrder newOrder = SalesOrder.of(
                        orderNumber,
                        csv.getOrderDate(),
                        csv.getDepartmentCode(),
                        csv.getDepartmentStartDate(),
                        csv.getCustomerCode(),
                        csv.getCustomerBranchNumber(),
                        csv.getEmployeeCode(),
                        csv.getDesiredDeliveryDate(),
                        csv.getCustomerOrderNumber(),
                        csv.getWarehouseCode(),
                        csv.getTotalOrderAmount(),
                        csv.getTotalConsumptionTax(),
                        csv.getRemarks(),
                        new ArrayList<>()
                );

                // Mapと結果リストに登録
                orderMap.put(orderNumber, newOrder);
                salesOrders.add(newOrder);
                existingOrder = newOrder;
            }

            // SalesOrderLineを作成して追加
            SalesOrderLine orderLine = SalesOrderLine.of(
                    orderNumber,
                    csv.getOrderLineNumber(),
                    csv.getProductCode(),
                    csv.getProductName(),
                    csv.getSalesUnitPrice(),
                    csv.getOrderQuantity(),
                    csv.getTaxRate(),
                    csv.getAllocationQuantity(),
                    csv.getShipmentInstructionQuantity(),
                    csv.getShippedQuantity(),
                    csv.getCompletionFlag(),
                    csv.getDiscountAmount(),
                    csv.getDeliveryDate()
            );

            // SalesOrderのsalesOrderLinesリストに追加
            existingOrder.getSalesOrderLines().add(orderLine);
        }

        return new SalesOrderList(salesOrders);
    }
}