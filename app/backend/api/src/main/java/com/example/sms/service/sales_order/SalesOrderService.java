package com.example.sms.service.sales_order;

import com.example.sms.domain.model.master.department.Department;
import com.example.sms.domain.model.master.department.DepartmentId;
import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.master.employee.EmployeeCode;
import com.example.sms.domain.model.master.partner.Partner;
import com.example.sms.domain.model.master.product.Product;
import com.example.sms.domain.model.sales_order.SalesOrder;
import com.example.sms.domain.model.sales_order.SalesOrderLine;
import com.example.sms.domain.model.sales_order.SalesOrderList;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public SalesOrderService(SalesOrderRepository salesOrderRepository, ProductRepository productRepository, DepartmentRepository departmentRepository, PartnerRepository partnerRepository, EmployeeRepository employeeRepository) {
        this.salesOrderRepository = salesOrderRepository;
        this.productRepository = productRepository;
        this.departmentRepository = departmentRepository;
        this.partnerRepository = partnerRepository;
        this.employeeRepository = employeeRepository;
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
     * CSVファイルアップロード
     */
    public List<Map<String, String>>  uploadCsvFile(MultipartFile file) {
        notNull(file, "アップロードファイルは必須です。");
        isTrue(!file.isEmpty(), "アップロードファイルが空です。");
        notNull(file.getOriginalFilename(), "アップロードファイル名は必須です。");
        isTrue(file.getOriginalFilename().endsWith(".csv"), "アップロードファイルがCSVではありません。");
        isTrue(file.getSize() < 10000000, "アップロードファイルが大きすぎます。");

        Pattern2ReadCSVUtil<SalesOrderUploadCSV> csvUtil = new Pattern2ReadCSVUtil<>();
        List<SalesOrderUploadCSV> dataList = csvUtil.readCSV(SalesOrderUploadCSV.class, file, "UTF-8");
        isTrue(!dataList.isEmpty(), "CSVファイルの読み込みに失敗しました");

        List<Map<String, String>> errorList = checkError(dataList);
        if (!errorList.isEmpty()) return errorList;

        SalesOrderList salesOrderList = convert(dataList);
        salesOrderRepository.save(salesOrderList);
        return errorList;
    }

    /**
     * 受注ルールチェック
     */
    public List<Map<String, String>> checkRule() {
        List<Map<String, String>> checkList = new ArrayList<>();

        // 受注金額が100万円以上の場合
        SalesOrderList salesOrders = salesOrderRepository.selectAllNotComplete();
        salesOrders.asList().forEach(salesOrder -> {
            if (salesOrder.getTotalOrderAmount() >= 1000000) {
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put(salesOrder.getOrderNumber(), "受注金額が100万円を超えています。");
                checkList.add(errorMap);
            }
        });

        return checkList;
    }

    private List<Map<String, String>> checkError(List<SalesOrderUploadCSV> dataList) {
        List<Map<String, String>> errorList = new ArrayList<>();
        dataList.forEach(data -> {
                    Department department = departmentRepository.findById(DepartmentId.of(data.getDepartmentCode(), data.getDepartmentStartDate())).orElse(null);
                    if (department == null) {
                        Map<String, String> errorMap = new HashMap<>();
                        errorMap.put(data.getOrderNumber(), "部門マスタに存在しません:" + data.getDepartmentCode());
                        errorList.add(errorMap);
                    }
                    Partner partner = partnerRepository.findById(data.getCustomerCode()).orElse(null);
                    if (partner == null) {
                        Map<String, String> errorMap = new HashMap<>();
                        errorMap.put(data.getOrderNumber(), "取引先マスタに存在しません:" + data.getCustomerCode());
                        errorList.add(errorMap);
                    }
                    Product product = productRepository.findById(data.getProductCode()).orElse(null);
                    if (product == null) {
                        Map<String, String> errorMap = new HashMap<>();
                        errorMap.put(data.getOrderNumber(), "商品マスタに存在しません:" + data.getProductCode());
                        errorList.add(errorMap);
                    }
                    Employee employee = employeeRepository.findById(EmployeeCode.of(data.getEmployeeCode())).orElse(null);
                    if (employee == null) {
                        Map<String, String> errorMap = new HashMap<>();
                        errorMap.put(data.getOrderNumber(), "社員マスタに存在しません:" + data.getEmployeeCode());
                        errorList.add(errorMap);
                    }
                }
        );
        return errorList;
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