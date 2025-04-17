package com.example.sms.service.sales_order;

import com.example.sms.FeatureToggleProperties;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.employee.EmployeeCode;
import com.example.sms.domain.model.master.product.Product;
import com.example.sms.domain.model.sales.Sales;
import com.example.sms.domain.model.sales_order.SalesOrder;
import com.example.sms.domain.model.sales_order.SalesOrderLine;
import com.example.sms.domain.model.sales_order.SalesOrderList;
import com.example.sms.domain.model.system.autonumber.AutoNumber;
import com.example.sms.domain.service.sales_order.SalesOrderDomainService;
import com.example.sms.domain.model.sales_order.rule.SalesOrderRuleCheckList;
import com.example.sms.infrastructure.Pattern2ReadCSVUtil;
import com.example.sms.infrastructure.datasource.sales_order.SalesOrderUploadCSV;
import com.example.sms.service.master.department.DepartmentRepository;
import com.example.sms.service.master.employee.EmployeeRepository;
import com.example.sms.service.master.partner.PartnerRepository;
import com.example.sms.service.master.product.ProductRepository;
import com.example.sms.service.system.autonumber.AutoNumberService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.apache.commons.lang3.Validate.*;


/**
 * 受注サービス
 */
@Service
@Transactional
@Slf4j
public class SalesOrderService {
    final SalesOrderRepository salesOrderRepository;
    final ProductRepository productRepository;
    final DepartmentRepository departmentRepository;
    final PartnerRepository partnerRepository;
    final EmployeeRepository employeeRepository;
    final SalesOrderDomainService salesOrderDomainService;
    final FeatureToggleProperties featureToggleProperties;
    final AutoNumberService autoNumberService;

    public SalesOrderService(SalesOrderRepository salesOrderRepository, ProductRepository productRepository, DepartmentRepository departmentRepository, PartnerRepository partnerRepository, EmployeeRepository employeeRepository, SalesOrderDomainService salesOrderDomainService, FeatureToggleProperties featureToggleProperties, AutoNumberService autoNumberService) {
        this.salesOrderRepository = salesOrderRepository;
        this.productRepository = productRepository;
        this.departmentRepository = departmentRepository;
        this.partnerRepository = partnerRepository;
        this.employeeRepository = employeeRepository;
        this.salesOrderDomainService = salesOrderDomainService;
        this.featureToggleProperties = featureToggleProperties;
        this.autoNumberService = autoNumberService;
    }

    public void executeFeature() {
        if (featureToggleProperties.isNewFeature()) {
            log.info("新機能が有効です: 新しい処理を実行します！");
        } else {
            log.info("新機能は無効です: 従来の処理を実行します。");
        }

        if (featureToggleProperties.isBetaFeature()) {
            log.info("ベータ機能が有効です: 特定の機能にアクセス可能です。");
        } else {
            log.info("ベータ機能は無効です: ロックされています。");
        }
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
        if (salesOrder.getOrderNumber() == null) {
            LocalDateTime orderDate = Objects.requireNonNull(Objects.requireNonNull(salesOrder.getOrderDate()).getValue());
            LocalDateTime yearMonth = YearMonth.of(orderDate.getYear(), orderDate.getMonth()).atDay(1).atStartOfDay();
            Integer autoNumber = autoNumberService.getNextDocumentNumber("O", yearMonth);
            String orderNumber = "O" + yearMonth.format(DateTimeFormatter.ofPattern("yyMM")) + String.format("%05d", autoNumber);
            salesOrder = SalesOrder.of(
                    orderNumber,
                    orderDate,
                    Objects.requireNonNull(salesOrder.getDepartmentCode()).getValue(),
                    salesOrder.getDepartmentStartDate(),
                    Objects.requireNonNull(Objects.requireNonNull(salesOrder.getCustomerCode()).getCode()).getValue(),
                    salesOrder.getCustomerCode().getBranchNumber(),
                    Objects.requireNonNull(salesOrder.getEmployeeCode()).getValue(),
                    Objects.requireNonNull(salesOrder.getDesiredDeliveryDate()).getValue(),
                    salesOrder.getCustomerOrderNumber(),
                    salesOrder.getWarehouseCode(),
                    Objects.requireNonNull(salesOrder.getTotalOrderAmount()).getAmount(),
                    Objects.requireNonNull(salesOrder.getTotalConsumptionTax()).getAmount(),
                    salesOrder.getRemarks(),
                    Objects.requireNonNull(salesOrder.getSalesOrderLines())
            );
            autoNumberService.save(AutoNumber.of("O", yearMonth, autoNumber));
            autoNumberService.incrementDocumentNumber("O", yearMonth);
        }
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
    public SalesOrderRuleCheckList checkRule() {
        SalesOrderList salesOrders = salesOrderRepository.selectAllNotComplete();
        return salesOrderDomainService.checkRule(salesOrders);
    }

    /**
     * CSVファイルアップロード
     */
    public SalesOrderUploadErrorList uploadCsvFile(MultipartFile file) {
        notNull(file, "アップロードファイルは必須です。");
        isTrue(!file.isEmpty(), "アップロードファイルが空です。");
        String originalFilename = Optional.ofNullable(file.getOriginalFilename())
                .orElseThrow(() -> new IllegalArgumentException("アップロードファイル名は必須です。"));
        isTrue(originalFilename.endsWith(".csv"), "アップロードファイルがCSVではありません。");
        isTrue(file.getSize() < 10000000, "アップロードファイルが大きすぎます。");

        Pattern2ReadCSVUtil<SalesOrderUploadCSV> csvUtil = new Pattern2ReadCSVUtil<>();
        List<SalesOrderUploadCSV> dataList = csvUtil.readCSV(SalesOrderUploadCSV.class, file, "Windows-31J");
        isTrue(!dataList.isEmpty(), "CSVファイルの読み込みに失敗しました");

        SalesOrderUploadErrorList errorList = validateErrors(dataList);
        if (!errorList.isEmpty()) return errorList;

        SalesOrderList salesOrderList = convert(dataList);
        salesOrderRepository.save(salesOrderList);
        return errorList;
    }

    /**
     * CSVファイルアップロードバリデーション
     */
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

    /**
     * マスタデータ存在チェック
     */
    private void checkEntityExistence(Optional<?> entity, List<Map<String, String>> checkResult, String orderNumber, String errorMessage) {
        if (entity.isEmpty()) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put(orderNumber, errorMessage);
            checkResult.add(errorMap);
        }
    }

    /**
     * CSVデータをSalesOrderListに変換
     */
    private SalesOrderList convert(List<SalesOrderUploadCSV> dataList) {
        // 結果を格納するリスト
        List<SalesOrder> salesOrders = new ArrayList<>();

        // OrderNumber単位でマッピングするため、一時的にMapを活用
        Map<String, SalesOrder> orderMap = new HashMap<>();

        for (SalesOrderUploadCSV csv : dataList) {
            // OrderNumberの取得と空白削除
            String orderNumber = Optional.ofNullable(csv.getOrderNumber())
                    .map(num -> num.replaceAll("\\s", ""))
                    .orElse(null);

            if (orderNumber == null) {
                // OrderNumberがnullであればスキップ
                continue;
            }

            // `computeIfAbsent`を使って、存在しなければ新しいSalesOrderを作成
            SalesOrder salesOrder = orderMap.computeIfAbsent(orderNumber, key -> {
                SalesOrder newOrder = SalesOrder.of(
                        key,
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
                        new ArrayList<>() // 空のSalesOrderLineリスト
                );
                // 結果のリストにも登録
                salesOrders.add(newOrder);
                return newOrder;
            });

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
            // Productを取得
            Product product = productRepository.findById(csv.getProductCode()).orElse(null);

            // SalesOrderの`salesOrderLines`リストに追加
            Objects.requireNonNull(salesOrder.getSalesOrderLines()).add(SalesOrderLine.of(orderLine, product));
        }

        // SalesOrderリストを返す
        return new SalesOrderList(salesOrders);
    }
}