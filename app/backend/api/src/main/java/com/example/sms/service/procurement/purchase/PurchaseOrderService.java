package com.example.sms.service.procurement.purchase;

import com.example.sms.domain.model.procurement.purchase.PurchaseOrder;
import com.example.sms.domain.model.procurement.purchase.PurchaseOrderList;
import com.example.sms.domain.model.procurement.purchase.PurchaseOrderLine;
import com.example.sms.domain.model.procurement.purchase.PurchaseOrderNumber;
import com.example.sms.domain.model.system.autonumber.AutoNumber;
import com.example.sms.domain.model.system.autonumber.DocumentTypeCode;
import com.example.sms.domain.model.procurement.purchase.rule.PurchaseOrderRuleCheckList;
import com.example.sms.domain.service.procurement.purchase.PurchaseOrderDomainService;
import com.example.sms.domain.type.money.Money;
import com.example.sms.service.system.autonumber.AutoNumberService;
import com.example.sms.infrastructure.Pattern2ReadCSVUtil;
import com.example.sms.infrastructure.datasource.procurement.purchase.PurchaseOrderUploadCSV;
import com.example.sms.service.master.partner.PartnerRepository;
import com.example.sms.service.master.product.ProductRepository;
import com.example.sms.service.master.employee.EmployeeRepository;
import com.example.sms.domain.model.master.employee.EmployeeCode;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

import static org.apache.commons.lang3.Validate.notNull;
import static org.apache.commons.lang3.Validate.isTrue;

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
    private final PartnerRepository partnerRepository;
    private final ProductRepository productRepository;
    private final EmployeeRepository employeeRepository;

    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository, 
                              AutoNumberService autoNumberService,
                              PurchaseOrderDomainService purchaseOrderDomainService,
                              PartnerRepository partnerRepository,
                              ProductRepository productRepository,
                              EmployeeRepository employeeRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.autoNumberService = autoNumberService;
        this.purchaseOrderDomainService = purchaseOrderDomainService;
        this.partnerRepository = partnerRepository;
        this.productRepository = productRepository;
        this.employeeRepository = employeeRepository;
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
                    purchaseOrder.getSalesOrderNumber().getValue(),
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
     * CSVファイルアップロード
     */
    public PurchaseOrderUploadErrorList uploadCsvFile(MultipartFile file) {
        notNull(file, "アップロードファイルは必須です。");
        isTrue(!file.isEmpty(), "アップロードファイルが空です。");
        String originalFilename = Optional.ofNullable(file.getOriginalFilename())
                .orElseThrow(() -> new IllegalArgumentException("アップロードファイル名は必須です。"));
        isTrue(originalFilename.endsWith(".csv"), "アップロードファイルがCSVではありません。");
        isTrue(file.getSize() < 10000000, "アップロードファイルが大きすぎます。");

        Pattern2ReadCSVUtil<PurchaseOrderUploadCSV> csvUtil = new Pattern2ReadCSVUtil<>();
        List<PurchaseOrderUploadCSV> dataList = csvUtil.readCSV(PurchaseOrderUploadCSV.class, file, "Windows-31J");
        isTrue(!dataList.isEmpty(), "CSVファイルの読み込みに失敗しました");

        PurchaseOrderUploadErrorList errorList = validateErrors(dataList);
        if (!errorList.isEmpty()) return errorList;

        // CSV データをドメインオブジェクトに変換して保存
        PurchaseOrderList purchaseOrderList = convert(dataList);
        purchaseOrderList.asList().forEach(purchaseOrderRepository::save);
        
        return errorList;
    }

    /**
     * CSVファイルアップロードバリデーション
     */
    private PurchaseOrderUploadErrorList validateErrors(List<PurchaseOrderUploadCSV> dataList) {
        List<Map<String, String>> checkResult = new ArrayList<>();

        dataList.forEach(data -> {
            checkEntityExistence(
                    partnerRepository.findById(data.getSupplierCode()),
                    checkResult,
                    data.getPurchaseOrderNumber(),
                    "仕入先マスタに存在しません:" + data.getSupplierCode()
            );

            checkEntityExistence(
                    productRepository.findById(data.getProductCode()),
                    checkResult,
                    data.getPurchaseOrderNumber(),
                    "商品マスタに存在しません:" + data.getProductCode()
            );

            checkEntityExistence(
                    employeeRepository.findById(EmployeeCode.of(data.getPurchaseManagerCode())),
                    checkResult,
                    data.getPurchaseOrderNumber(),
                    "社員マスタに存在しません:" + data.getPurchaseManagerCode()
            );
        });

        return new PurchaseOrderUploadErrorList(checkResult);
    }

    /**
     * マスタデータ存在チェック
     */
    private void checkEntityExistence(Optional<?> entity, List<Map<String, String>> checkResult, String purchaseOrderNumber, String errorMessage) {
        if (entity.isEmpty()) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put(purchaseOrderNumber, errorMessage);
            checkResult.add(errorMap);
        }
    }

    /**
     * CSVデータをPurchaseOrderListに変換
     */
    private PurchaseOrderList convert(List<PurchaseOrderUploadCSV> dataList) {
        // 結果を格納するリスト
        List<PurchaseOrder> purchaseOrders = new ArrayList<>();

        // PurchaseOrderNumber単位でマッピングするため、一時的にMapを活用
        Map<String, PurchaseOrder> purchaseOrderMap = new HashMap<>();

        for (PurchaseOrderUploadCSV csv : dataList) {
            // ヘッダー行や無効なデータをスキップ
            if (csv.getPurchaseOrderDate() == null || csv.getSupplierCode() == null || csv.getProductCode() == null) {
                continue;
            }

            // PurchaseOrderNumberの取得と空白削除
            String purchaseOrderNumber = Optional.ofNullable(csv.getPurchaseOrderNumber())
                    .map(num -> num.replaceAll("\\s", ""))
                    .orElse(null);

            if (purchaseOrderNumber == null || purchaseOrderNumber.isEmpty()) {
                // 発注番号が空の場合は自動採番
                purchaseOrderNumber = generatePurchaseOrderNumber(csv.getPurchaseOrderDate());
            }

            // `computeIfAbsent`を使って、存在しなければ新しいPurchaseOrderを作成
            PurchaseOrder purchaseOrder = purchaseOrderMap.computeIfAbsent(purchaseOrderNumber, key -> {
                if (csv.getSalesOrderNumber() == null) csv.setSalesOrderNumber("");
                PurchaseOrder newPurchaseOrder = PurchaseOrder.of(
                        key,
                        csv.getPurchaseOrderDate(),
                        csv.getSalesOrderNumber(),
                        csv.getSupplierCode(),
                        csv.getSupplierBranchNumber(),
                        csv.getPurchaseManagerCode(),
                        csv.getDesignatedDeliveryDate(),
                        csv.getWarehouseCode(),
                        csv.getTotalPurchaseAmount() != null ? csv.getTotalPurchaseAmount() : 0,
                        csv.getTotalConsumptionTax() != null ? csv.getTotalConsumptionTax() : 0,
                        csv.getRemarks(),
                        new ArrayList<>() // 空のPurchaseOrderLineリスト
                );
                // 結果のリストにも登録
                purchaseOrders.add(newPurchaseOrder);
                return newPurchaseOrder;
            });

            // PurchaseOrderLineを作成して追加
            PurchaseOrderLine purchaseOrderLine = PurchaseOrderLine.of(
                    purchaseOrderNumber,
                    csv.getPurchaseOrderLineNumber(),
                    csv.getPurchaseOrderLineNumber(), // 表示番号として使用
                    csv.getSalesOrderNumber(),
                    0, // 受注行番号はCSVに含まれていない
                    csv.getProductCode(),
                    csv.getProductName(),
                    csv.getPurchaseUnitPrice() != null ? csv.getPurchaseUnitPrice() : 0,
                    csv.getPurchaseQuantity() != null ? csv.getPurchaseQuantity() : 0,
                    csv.getReceivedQuantity() != null ? csv.getReceivedQuantity() : 0,
                    csv.getCompletionFlag() != null ? csv.getCompletionFlag() : 0
            );

            // PurchaseOrderの`purchaseOrderLines`リストに追加
            Objects.requireNonNull(purchaseOrder.getPurchaseOrderLines()).add(purchaseOrderLine);
        }

        // PurchaseOrderリストを返す
        return new PurchaseOrderList(purchaseOrders);
    }

    /**
     * 発注番号生成（日付指定版）
     */
    private String generatePurchaseOrderNumber(LocalDateTime purchaseOrderDate) {
        String code = DocumentTypeCode.発注.getCode();
        LocalDateTime yearMonth = YearMonth.of(purchaseOrderDate.getYear(), purchaseOrderDate.getMonth()).atDay(1).atStartOfDay();
        Integer autoNumber = autoNumberService.getNextDocumentNumber(code, yearMonth);
        String purchaseOrderNumber = PurchaseOrderNumber.generate(code, yearMonth, autoNumber);
        autoNumberService.save(AutoNumber.of(code, yearMonth, autoNumber));
        autoNumberService.incrementDocumentNumber(code, yearMonth);
        return purchaseOrderNumber;
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