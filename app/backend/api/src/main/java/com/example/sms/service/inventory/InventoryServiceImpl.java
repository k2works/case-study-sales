package com.example.sms.service.inventory;

import com.example.sms.domain.model.inventory.Inventory;
import com.example.sms.domain.model.inventory.InventoryKey;
import com.example.sms.domain.model.inventory.InventoryList;
import com.example.sms.domain.model.inventory.rule.InventoryRuleCheckList;
import com.example.sms.domain.service.inventory.InventoryDomainService;
import com.example.sms.infrastructure.Pattern2ReadCSVUtil;
import com.example.sms.infrastructure.datasource.inventory.InventoryUploadCSV;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

import static org.apache.commons.lang3.Validate.notNull;
import static org.apache.commons.lang3.Validate.isTrue;

/**
 * 在庫サービス実装
 */
@Service
@Transactional
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryDomainService inventoryDomainService;

    public InventoryServiceImpl(InventoryRepository inventoryRepository,
                               InventoryDomainService inventoryDomainService) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryDomainService = inventoryDomainService;
    }

    @Override
    public Inventory register(Inventory inventory) {
        notNull(inventory, "在庫情報は必須です");
        
        log.info("在庫登録開始: {}", inventory.getKey());
        
        // 既に同じキーの在庫が存在するかチェック
        Optional<Inventory> existing = inventoryRepository.findByKey(inventory.getKey());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("既に存在する在庫キーです: " + inventory.getKey());
        }
        
        Inventory saved = inventoryRepository.save(inventory);
        log.info("在庫登録完了: {}", saved.getKey());
        
        return saved;
    }

    @Override
    public Inventory update(Inventory inventory) {
        notNull(inventory, "在庫情報は必須です");
        
        log.info("在庫更新開始: {}", inventory.getKey());
        
        // 既存の在庫が存在するかチェック
        Optional<Inventory> existing = inventoryRepository.findByKey(inventory.getKey());
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("更新対象の在庫が見つかりません: " + inventory.getKey());
        }
        
        Inventory saved = inventoryRepository.save(inventory);
        log.info("在庫更新完了: {}", saved.getKey());
        
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Inventory> findByKey(InventoryKey key) {
        notNull(key, "在庫キーは必須です");
        
        log.debug("在庫キー検索: {}", key);
        return inventoryRepository.findByKey(key);
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryList findAll() {
        log.debug("全在庫取得");
        return inventoryRepository.selectAll();
    }

    @Override
    @Transactional(readOnly = true)
    public PageInfo<Inventory> findAllWithPageInfo() {
        log.debug("全在庫取得（ページ情報付き）");
        return inventoryRepository.selectAllWithPageInfo();
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryList searchByCriteria(InventoryCriteria criteria) {
        notNull(criteria, "検索条件は必須です");
        
        log.debug("在庫条件検索: {}", criteria);
        return inventoryRepository.searchByCriteria(criteria);
    }

    @Override
    @Transactional(readOnly = true)
    public PageInfo<Inventory> searchWithPageInfo(InventoryCriteria criteria) {
        notNull(criteria, "検索条件は必須です");
        
        log.debug("在庫条件検索（ページ情報付き）: {}", criteria);
        return inventoryRepository.searchWithPageInfo(criteria);
    }

    @Override
    public Inventory adjustStock(InventoryKey key, Integer adjustmentQuantity) {
        notNull(key, "在庫キーは必須です");
        notNull(adjustmentQuantity, "調整数量は必須です");
        
        log.info("在庫調整開始: key={}, 調整数量={}", key, adjustmentQuantity);
        
        Inventory inventory = inventoryRepository.findByKey(key)
                .orElseThrow(() -> new IllegalArgumentException("在庫が見つかりません: " + key));
        
        Inventory adjusted = inventory.adjustStock(adjustmentQuantity);
        Inventory saved = inventoryRepository.save(adjusted);
        
        log.info("在庫調整完了: key={}, 調整前実在庫={}, 調整後実在庫={}", 
                 key, inventory.getActualStockQuantity(), saved.getActualStockQuantity());
        
        return saved;
    }

    @Override
    public Inventory reserveStock(InventoryKey key, Integer reserveQuantity) {
        notNull(key, "在庫キーは必須です");
        notNull(reserveQuantity, "予約数量は必須です");
        
        log.info("在庫予約開始: key={}, 予約数量={}", key, reserveQuantity);
        
        Inventory inventory = inventoryRepository.findByKey(key)
                .orElseThrow(() -> new IllegalArgumentException("在庫が見つかりません: " + key));
        
        Inventory reserved = inventory.reserve(reserveQuantity);
        Inventory saved = inventoryRepository.save(reserved);
        
        log.info("在庫予約完了: key={}, 予約前有効在庫={}, 予約後有効在庫={}", 
                 key, inventory.getAvailableStockQuantity(), saved.getAvailableStockQuantity());
        
        return saved;
    }

    @Override
    public Inventory shipStock(InventoryKey key, Integer shipmentQuantity, LocalDateTime shipmentDate) {
        notNull(key, "在庫キーは必須です");
        notNull(shipmentQuantity, "出荷数量は必須です");
        notNull(shipmentDate, "出荷日時は必須です");
        
        log.info("在庫出荷開始: key={}, 出荷数量={}, 出荷日時={}", key, shipmentQuantity, shipmentDate);
        
        Inventory inventory = inventoryRepository.findByKey(key)
                .orElseThrow(() -> new IllegalArgumentException("在庫が見つかりません: " + key));
        
        Inventory shipped = inventory.ship(shipmentQuantity, shipmentDate);
        Inventory saved = inventoryRepository.save(shipped);
        
        log.info("在庫出荷完了: key={}, 出荷前実在庫={}, 出荷後実在庫={}", 
                 key, inventory.getActualStockQuantity(), saved.getActualStockQuantity());
        
        return saved;
    }

    @Override
    public Inventory receiveStock(InventoryKey key, Integer receiptQuantity) {
        notNull(key, "在庫キーは必須です");
        notNull(receiptQuantity, "入荷数量は必須です");
        
        log.info("在庫入荷開始: key={}, 入荷数量={}", key, receiptQuantity);
        
        Inventory inventory = inventoryRepository.findByKey(key)
                .orElseThrow(() -> new IllegalArgumentException("在庫が見つかりません: " + key));
        
        Inventory received = inventory.receive(receiptQuantity);
        Inventory saved = inventoryRepository.save(received);
        
        log.info("在庫入荷完了: key={}, 入荷前実在庫={}, 入荷後実在庫={}", 
                 key, inventory.getActualStockQuantity(), saved.getActualStockQuantity());
        
        return saved;
    }

    @Override
    public void delete(InventoryKey key) {
        notNull(key, "在庫キーは必須です");
        
        log.info("在庫削除開始: {}", key);
        
        // 削除前に存在チェック
        Optional<Inventory> existing = inventoryRepository.findByKey(key);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("削除対象の在庫が見つかりません: " + key);
        }
        
        inventoryRepository.delete(key);
        log.info("在庫削除完了: {}", key);
    }

    @Override
    public void deleteAll() {
        log.info("全在庫削除開始");
        inventoryRepository.deleteAll();
        log.info("全在庫削除完了");
    }

    @Override
    public InventoryUploadErrorList uploadCsvFile(MultipartFile file) {
        notNull(file, "アップロードファイルは必須です。");
        isTrue(!file.isEmpty(), "CSVファイルの読み込みに失敗しました");
        String originalFilename = Optional.ofNullable(file.getOriginalFilename())
                .orElseThrow(() -> new IllegalArgumentException("アップロードファイル名は必須です。"));
        isTrue(originalFilename.endsWith(".csv"), "アップロードファイルがCSVではありません。");
        isTrue(file.getSize() < 1000, "アップロードファイルが大きすぎます。");

        log.info("在庫CSVアップロード開始: ファイル名={}, サイズ={}", originalFilename, file.getSize());

        Pattern2ReadCSVUtil<InventoryUploadCSV> csvUtil = new Pattern2ReadCSVUtil<>();
        List<InventoryUploadCSV> dataList = csvUtil.readCSV(InventoryUploadCSV.class, file, "UTF-8");
        isTrue(!dataList.isEmpty(), "CSVファイルの読み込みに失敗しました");

        InventoryUploadErrorList errorList = validateErrors(dataList);
        if (!errorList.isEmpty()) {
            log.warn("在庫CSVアップロードバリデーションエラー: エラー数={}", errorList.size());
            return errorList;
        }

        // CSV データをドメインオブジェクトに変換して保存
        List<Inventory> inventoryList = convertFromCsv(dataList);
        List<Map<String, String>> saveErrors = new ArrayList<>();
        
        for (Inventory inventory : inventoryList) {
            try {
                inventoryRepository.save(inventory);
                log.debug("在庫登録成功: {}", inventory.getKey());
            } catch (Exception e) {
                log.error("在庫登録エラー: key={}, error={}", inventory.getKey(), e.getMessage());
                Map<String, String> error = new HashMap<>();
                error.put("倉庫コード", inventory.getKey().getWarehouseCode());
                error.put("商品コード", inventory.getKey().getProductCode());
                error.put("ロット番号", inventory.getKey().getLotNumber());
                error.put("エラー", e.getMessage());
                saveErrors.add(error);
            }
        }
        
        // 保存時のエラーがあれば追加
        for (Map<String, String> saveError : saveErrors) {
            errorList = errorList.add(saveError);
        }
        
        log.info("在庫CSVアップロード完了: 処理件数={}, エラー数={}", dataList.size(), errorList.size());
        return errorList;
    }

    /**
     * CSVファイルアップロードバリデーション
     */
    private InventoryUploadErrorList validateErrors(List<InventoryUploadCSV> dataList) {
        List<Map<String, String>> checkResult = new ArrayList<>();

        for (InventoryUploadCSV data : dataList) {
            Map<String, String> error = new HashMap<>();
            boolean hasError = false;

            // 必須項目チェック
            if (data.getWarehouseCode() == null || data.getWarehouseCode().trim().isEmpty()) {
                error.put("倉庫コード", data.getWarehouseCode());
                error.put("エラー", "倉庫コードは必須です");
                hasError = true;
            }
            
            if (data.getProductCode() == null || data.getProductCode().trim().isEmpty()) {
                error.put("商品コード", data.getProductCode());
                error.put("エラー", "商品コードは必須です");
                hasError = true;
            }
            
            if (data.getLotNumber() == null || data.getLotNumber().trim().isEmpty()) {
                error.put("ロット番号", data.getLotNumber());
                error.put("エラー", "ロット番号は必須です");
                hasError = true;
            }
            
            if (data.getStockCategory() == null || data.getStockCategory().trim().isEmpty()) {
                error.put("在庫区分", data.getStockCategory());
                error.put("エラー", "在庫区分は必須です");
                hasError = true;
            }
            
            if (data.getQualityCategory() == null || data.getQualityCategory().trim().isEmpty()) {
                error.put("良品区分", data.getQualityCategory());
                error.put("エラー", "良品区分は必須です");
                hasError = true;
            }

            // 数量の数値チェック
            try {
                if (data.getActualStockQuantity() != null) {
                    Integer.parseInt(data.getActualStockQuantity());
                }
            } catch (NumberFormatException e) {
                error.put("実在庫数量", data.getActualStockQuantity());
                error.put("エラー", "実在庫数量は数値で入力してください");
                hasError = true;
            }
            
            try {
                if (data.getAvailableStockQuantity() != null) {
                    Integer.parseInt(data.getAvailableStockQuantity());
                }
            } catch (NumberFormatException e) {
                error.put("有効在庫数量", data.getAvailableStockQuantity());
                error.put("エラー", "有効在庫数量は数値で入力してください");
                hasError = true;
            }

            if (hasError) {
                checkResult.add(error);
            }
        }

        return new InventoryUploadErrorList(checkResult);
    }

    /**
     * CSVデータからInventoryオブジェクトのリストに変換
     */
    private List<Inventory> convertFromCsv(List<InventoryUploadCSV> dataList) {
        List<Inventory> inventoryList = new ArrayList<>();
        
        for (InventoryUploadCSV data : dataList) {
            Integer actualStock = data.getActualStockQuantity() != null ? 
                Integer.parseInt(data.getActualStockQuantity()) : 0;
            Integer availableStock = data.getAvailableStockQuantity() != null ? 
                Integer.parseInt(data.getAvailableStockQuantity()) : 0;
                
            Inventory inventory = Inventory.of(
                data.getWarehouseCode(),
                data.getProductCode(), 
                data.getLotNumber(),
                data.getStockCategory(),
                data.getQualityCategory(),
                actualStock,
                availableStock,
                null // lastShipmentDate
            );
            
            inventoryList.add(inventory);
        }
        
        return inventoryList;
    }

    @Override
    public InventoryRuleCheckList checkRule() {
        InventoryList inventories = inventoryRepository.selectAll();
        return inventoryDomainService.checkRule(inventories);
    }
}