package com.example.sms.service.sales.order;

import com.example.sms.FeatureToggleProperties;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.employee.EmployeeCode;
import com.example.sms.domain.model.master.product.Product;
import com.example.sms.domain.model.sales.order.Order;
import com.example.sms.domain.model.sales.order.OrderNumber;
import com.example.sms.domain.model.sales.order.OrderLine;
import com.example.sms.domain.model.sales.order.OrderList;
import com.example.sms.domain.model.sales.sales.SalesNumber;
import com.example.sms.domain.model.system.autonumber.AutoNumber;
import com.example.sms.domain.model.system.autonumber.DocumentTypeCode;
import com.example.sms.domain.service.sales.order.OrderDomainService;
import com.example.sms.domain.model.sales.order.rule.OrderRuleCheckList;
import com.example.sms.infrastructure.Pattern2ReadCSVUtil;
import com.example.sms.infrastructure.datasource.sales.order.OrderUploadCSV;
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
    final OrderDomainService orderDomainService;
    final FeatureToggleProperties featureToggleProperties;
    final AutoNumberService autoNumberService;

    public SalesOrderService(SalesOrderRepository salesOrderRepository, ProductRepository productRepository, DepartmentRepository departmentRepository, PartnerRepository partnerRepository, EmployeeRepository employeeRepository, OrderDomainService orderDomainService, FeatureToggleProperties featureToggleProperties, AutoNumberService autoNumberService) {
        this.salesOrderRepository = salesOrderRepository;
        this.productRepository = productRepository;
        this.departmentRepository = departmentRepository;
        this.partnerRepository = partnerRepository;
        this.employeeRepository = employeeRepository;
        this.orderDomainService = orderDomainService;
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
    public OrderList selectAll() {
        return salesOrderRepository.selectAll();
    }

    /**
     * 受注一覧（ページング）
     */
    public PageInfo<Order> selectAllWithPageInfo() {
        return salesOrderRepository.selectAllWithPageInfo();
    }

    /**
     * 受注新規登録
     */
    public void register(Order order) {
        if (order.getOrderNumber() == null) {
            String orderNumber = generateOrderNumber(order);

            order = Order.of(
                    orderNumber,
                    Objects.requireNonNull(Objects.requireNonNull(order.getOrderDate()).getValue()),
                    Objects.requireNonNull(order.getDepartmentCode()).getValue(),
                    order.getDepartmentStartDate(),
                    Objects.requireNonNull(Objects.requireNonNull(order.getCustomerCode()).getCode()).getValue(),
                    order.getCustomerCode().getBranchNumber(),
                    Objects.requireNonNull(order.getEmployeeCode()).getValue(),
                    Objects.requireNonNull(order.getDesiredDeliveryDate()).getValue(),
                    order.getCustomerOrderNumber(),
                    order.getWarehouseCode(),
                    Objects.requireNonNull(order.getTotalOrderAmount()).getAmount(),
                    Objects.requireNonNull(order.getTotalConsumptionTax()).getAmount(),
                    order.getRemarks(),
                    Objects.requireNonNull(order.getOrderLines())
            );
        }
        salesOrderRepository.save(order);
    }

    /**
     * 受注編集
     */
    public void save(Order order) {
        salesOrderRepository.save(order);
    }

    /**
     * 受注削除
     */
    public void delete(Order order) {
        salesOrderRepository.delete(order);
    }

    /**
     * 受注検索
     */
    public Order find(String salesOrderCode) {
        return salesOrderRepository.findById(salesOrderCode).orElse(null);
    }

    /**
     * 受注検索（ページング）
     */
    public PageInfo<Order> searchSalesOrderWithPageInfo(SalesOrderCriteria criteria) {
        return salesOrderRepository.searchWithPageInfo(criteria);
    }

    /**
     * 受注ルールチェック
     */
    public OrderRuleCheckList checkRule() {
        OrderList salesOrders = salesOrderRepository.selectAllNotComplete();
        return orderDomainService.checkRule(salesOrders);
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

        Pattern2ReadCSVUtil<OrderUploadCSV> csvUtil = new Pattern2ReadCSVUtil<>();
        List<OrderUploadCSV> dataList = csvUtil.readCSV(OrderUploadCSV.class, file, "Windows-31J");
        isTrue(!dataList.isEmpty(), "CSVファイルの読み込みに失敗しました");

        SalesOrderUploadErrorList errorList = validateErrors(dataList);
        if (!errorList.isEmpty()) return errorList;

        OrderList orderList = convert(dataList);
        salesOrderRepository.save(orderList);
        return errorList;
    }

    /**
     * 受注番号生成
     */
    private String generateOrderNumber(Order order) {
        String code = DocumentTypeCode.受注.getCode();
        LocalDateTime orderDate = Objects.requireNonNull(Objects.requireNonNull(order.getOrderDate()).getValue());
        LocalDateTime yearMonth = YearMonth.of(orderDate.getYear(), orderDate.getMonth()).atDay(1).atStartOfDay();
        Integer autoNumber = autoNumberService.getNextDocumentNumber(code, yearMonth);
        String orderNumber = OrderNumber.generate(code, yearMonth, autoNumber);
        autoNumberService.save(AutoNumber.of(code, yearMonth, autoNumber));
        autoNumberService.incrementDocumentNumber(code, yearMonth);
        return orderNumber;
    }

    /**
     * 受注番号生成
     */
    private String generateOrderNumber(LocalDateTime orderDate) {
        String code = DocumentTypeCode.受注.getCode();
        LocalDateTime yearMonth = YearMonth.of(orderDate.getYear(), orderDate.getMonth()).atDay(1).atStartOfDay();
        Integer autoNumber = autoNumberService.getNextDocumentNumber(code, yearMonth);
        String orderNumber = OrderNumber.generate(code, yearMonth, autoNumber);
        autoNumberService.save(AutoNumber.of(code, yearMonth, autoNumber));
        autoNumberService.incrementDocumentNumber(code, yearMonth);
        return orderNumber;
    }

    /**
     * CSVファイルアップロードバリデーション
     */
    private SalesOrderUploadErrorList validateErrors(List<OrderUploadCSV> dataList) {
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
    private OrderList convert(List<OrderUploadCSV> dataList) {
        // 結果を格納するリスト
        List<Order> orders = new ArrayList<>();

        // OrderNumber単位でマッピングするため、一時的にMapを活用
        Map<String, Order> orderMap = new HashMap<>();

        for (OrderUploadCSV csv : dataList) {
            // OrderNumberの取得と空白削除
            String orderNumber = Optional.ofNullable(csv.getOrderNumber())
                    .map(num -> num.replaceAll("\\s", ""))
                    .orElse(null);

            if (orderNumber == null || orderNumber.isEmpty()) {
                // 受注番号が空の場合は自動採番
                orderNumber = generateOrderNumber(csv.getOrderDate());
            }

            // `computeIfAbsent`を使って、存在しなければ新しいSalesOrderを作成
            Order order = orderMap.computeIfAbsent(orderNumber, key -> {
                Order newOrder = Order.of(
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
                orders.add(newOrder);
                return newOrder;
            });

            // SalesOrderLineを作成して追加
            OrderLine orderLine = OrderLine.of(
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
                    csv.getDeliveryDate(),
                    csv.getShippingDate()
            );
            // Productを取得
            Product product = productRepository.findById(csv.getProductCode()).orElse(null);

            // SalesOrderの`salesOrderLines`リストに追加
            Objects.requireNonNull(order.getOrderLines()).add(OrderLine.of(orderLine, product));
        }

        // SalesOrderリストを返す
        return new OrderList(orders);
    }
}