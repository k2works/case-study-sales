package com.example.sms.service.sales_order;

import com.example.sms.domain.model.sales_order.SalesOrder;
import com.example.sms.domain.model.sales_order.SalesOrderLine;
import com.example.sms.domain.model.sales_order.SalesOrderList;
import com.example.sms.infrastructure.Pattern2ReadCSVUtil;
import com.example.sms.infrastructure.datasource.sales_order.SalesOrderUploadCSV;
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

    public SalesOrderService(SalesOrderRepository salesOrderRepository) {
        this.salesOrderRepository = salesOrderRepository;
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
    public void uploadCsvFile(MultipartFile file) {
        notNull(file, "アップロードファイルは必須です。");
        isTrue(!file.isEmpty(), "アップロードファイルが空です。");
        notNull(file.getOriginalFilename(), "アップロードファイル名は必須です。");
        isTrue(file.getOriginalFilename().endsWith(".csv"), "アップロードファイルがCSVではありません。");
        isTrue(file.getSize() < 10000000, "アップロードファイルが大きすぎます。");

        Pattern2ReadCSVUtil<SalesOrderUploadCSV> csvUtil = new Pattern2ReadCSVUtil<>();
        List<SalesOrderUploadCSV> dataList = csvUtil.readCSV(SalesOrderUploadCSV.class, file, "UTF-8");
        isTrue(!dataList.isEmpty(), "CSVファイルの読み込みに失敗しました");

        SalesOrderList salesOrderList = convert(dataList);
        salesOrderRepository.save(salesOrderList);
    }

    private static SalesOrderList convert(List<SalesOrderUploadCSV> dataList) {
        // 結果を格納するリスト
        List<SalesOrder> salesOrders = new ArrayList<>();

        // OrderNumber単位でマッピングするため、一時的にMapを活用
        Map<String, SalesOrder> orderMap = new HashMap<>();

        for (SalesOrderUploadCSV csv : dataList) {
            // 既にそのOrderNumberのSalesOrderが存在するかを確認
            SalesOrder existingOrder = orderMap.get(csv.getOrderNumber());

            if (existingOrder == null) {
                // 新しくSalesOrderを作成
                SalesOrder newOrder = SalesOrder.of(
                        csv.getOrderNumber(),
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
                orderMap.put(csv.getOrderNumber(), newOrder);
                salesOrders.add(newOrder);
                existingOrder = newOrder;
            }

            // SalesOrderLineを作成して追加
            SalesOrderLine orderLine = SalesOrderLine.of(
                    csv.getOrderNumber(),
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