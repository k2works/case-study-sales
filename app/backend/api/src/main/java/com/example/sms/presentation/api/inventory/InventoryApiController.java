package com.example.sms.presentation.api.inventory;

import com.example.sms.domain.model.inventory.Inventory;
import com.example.sms.domain.model.inventory.InventoryKey;
import com.example.sms.domain.model.inventory.rule.InventoryRuleCheckList;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessType;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponseWithDetail;
import com.example.sms.service.BusinessException;
import com.example.sms.service.PageNationService;
import com.example.sms.service.inventory.InventoryCriteria;
import com.example.sms.service.inventory.InventoryService;
import com.example.sms.service.inventory.InventoryUploadErrorList;
import com.example.sms.service.system.audit.AuditAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 在庫API
 */
@RestController
@RequestMapping("/api/inventory")
@Tag(name = "Inventory", description = "在庫管理")
public class InventoryApiController {
    private final InventoryService inventoryService;
    private final PageNationService pageNationService;
    private final Message message;

    public InventoryApiController(InventoryService inventoryService, PageNationService pageNationService, Message message) {
        this.inventoryService = inventoryService;
        this.pageNationService = pageNationService;
        this.message = message;
    }

    @Operation(summary = "在庫一覧を取得する", description = "全ての在庫データをページ情報付きで取得します。")
    @GetMapping
    public ResponseEntity<?> selectAll(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PageInfo<Inventory> pageInfo = inventoryService.findAllWithPageInfo();
            PageInfo<InventoryResource> result = pageNationService.getPageInfo(pageInfo, InventoryResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "在庫情報を取得する", description = "指定された在庫キーに基づいて在庫データを取得します。")
    @GetMapping("/{warehouseCode}/{productCode}/{lotNumber}/{stockCategory}/{qualityCategory}")
    public ResponseEntity<?> select(
            @PathVariable String warehouseCode,
            @PathVariable String productCode,
            @PathVariable String lotNumber,
            @PathVariable String stockCategory,
            @PathVariable String qualityCategory) {
        try {
            InventoryKey key = InventoryKey.of(warehouseCode, productCode, lotNumber, stockCategory, qualityCategory);
            Optional<Inventory> inventory = inventoryService.findByKey(key);
            if (inventory.isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.inventory.not.exist")));
            }
            InventoryResource result = InventoryResource.from(inventory.get());
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "在庫を登録する", description = "新しい在庫データを登録します。")
    @PostMapping
    @AuditAnnotation(process = ApplicationExecutionProcessType.在庫登録, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> create(@RequestBody InventoryResource resource) {
        try {
            Inventory inventory = InventoryResourceDTOMapper.convertToEntity(resource);
            Optional<Inventory> existing = inventoryService.findByKey(inventory.getKey());
            if (existing.isPresent()) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.inventory.already.exist")));
            }
            inventoryService.register(inventory);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.inventory.registered")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "在庫情報を更新する", description = "指定された在庫データを更新します。")
    @PutMapping("/{warehouseCode}/{productCode}/{lotNumber}/{stockCategory}/{qualityCategory}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.在庫更新, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> update(
            @PathVariable String warehouseCode,
            @PathVariable String productCode,
            @PathVariable String lotNumber,
            @PathVariable String stockCategory,
            @PathVariable String qualityCategory,
            @RequestBody InventoryResource resource) {
        try {
            resource.setWarehouseCode(warehouseCode);
            resource.setProductCode(productCode);
            resource.setLotNumber(lotNumber);
            resource.setStockCategory(stockCategory);
            resource.setQualityCategory(qualityCategory);
            
            Inventory inventory = InventoryResourceDTOMapper.convertToEntity(resource);
            InventoryKey key = inventory.getKey();
            
            Optional<Inventory> existing = inventoryService.findByKey(key);
            if (existing.isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.inventory.not.exist")));
            }
            
            inventoryService.update(inventory);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.inventory.updated")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "在庫を削除する", description = "指定された在庫データを削除します。")
    @DeleteMapping("/{warehouseCode}/{productCode}/{lotNumber}/{stockCategory}/{qualityCategory}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.在庫削除, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> delete(
            @PathVariable String warehouseCode,
            @PathVariable String productCode,
            @PathVariable String lotNumber,
            @PathVariable String stockCategory,
            @PathVariable String qualityCategory) {
        try {
            InventoryKey key = InventoryKey.of(warehouseCode, productCode, lotNumber, stockCategory, qualityCategory);
            Optional<Inventory> inventory = inventoryService.findByKey(key);
            if (inventory.isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.inventory.not.exist")));
            }
            inventoryService.delete(key);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.inventory.deleted")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "在庫を検索する", description = "指定された検索条件で在庫データを検索します。")
    @PostMapping("/search")
    public ResponseEntity<?> search(
            @RequestBody InventoryCriteriaResource resource,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            InventoryCriteria criteria = InventoryResourceDTOMapper.convertToCriteria(resource);
            PageInfo<Inventory> pageInfo = inventoryService.searchWithPageInfo(criteria);
            PageInfo<InventoryResource> result = pageNationService.getPageInfo(pageInfo, InventoryResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "在庫を一括登録する", description = "ファイルアップロードで在庫を登録する")
    @PostMapping("/upload")
    @AuditAnnotation(process = ApplicationExecutionProcessType.在庫登録, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        try {
            InventoryUploadErrorList result = inventoryService.uploadCsvFile(file);
            if (result.isEmpty()) {
                return ResponseEntity.ok(new MessageResponseWithDetail(message.getMessage("success.inventory.upload"), result.asList()));
            }
            return ResponseEntity.ok(new MessageResponseWithDetail(message.getMessage("error.inventory.upload"), result.asList()));
        } catch (RuntimeException e) {
            System.out.println("InventoryApiController: Exception caught: " + e.getClass().getName() + " - " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponseWithDetail(e.getMessage(), java.util.List.of()));
        }
    }

    @Operation(summary = "在庫調整", description = "在庫数量を調整します。")
    @PostMapping("/{warehouseCode}/{productCode}/{lotNumber}/{stockCategory}/{qualityCategory}/adjust")
    @AuditAnnotation(process = ApplicationExecutionProcessType.在庫調整, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> adjustStock(
            @PathVariable String warehouseCode,
            @PathVariable String productCode,
            @PathVariable String lotNumber,
            @PathVariable String stockCategory,
            @PathVariable String qualityCategory,
            @RequestParam Integer adjustmentQuantity) {
        try {
            InventoryKey key = InventoryKey.of(warehouseCode, productCode, lotNumber, stockCategory, qualityCategory);
            inventoryService.adjustStock(key, adjustmentQuantity);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.inventory.adjusted")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "在庫予約", description = "在庫を予約します。")
    @PostMapping("/{warehouseCode}/{productCode}/{lotNumber}/{stockCategory}/{qualityCategory}/reserve")
    @AuditAnnotation(process = ApplicationExecutionProcessType.在庫予約, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> reserveStock(
            @PathVariable String warehouseCode,
            @PathVariable String productCode,
            @PathVariable String lotNumber,
            @PathVariable String stockCategory,
            @PathVariable String qualityCategory,
            @RequestParam Integer reserveQuantity) {
        try {
            InventoryKey key = InventoryKey.of(warehouseCode, productCode, lotNumber, stockCategory, qualityCategory);
            inventoryService.reserveStock(key, reserveQuantity);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.inventory.reserved")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "在庫出荷", description = "在庫を出荷します。")
    @PostMapping("/{warehouseCode}/{productCode}/{lotNumber}/{stockCategory}/{qualityCategory}/ship")
    @AuditAnnotation(process = ApplicationExecutionProcessType.在庫出荷, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> shipStock(
            @PathVariable String warehouseCode,
            @PathVariable String productCode,
            @PathVariable String lotNumber,
            @PathVariable String stockCategory,
            @PathVariable String qualityCategory,
            @RequestParam Integer shipmentQuantity) {
        try {
            InventoryKey key = InventoryKey.of(warehouseCode, productCode, lotNumber, stockCategory, qualityCategory);
            LocalDateTime shipmentDate = LocalDateTime.now();
            inventoryService.shipStock(key, shipmentQuantity, shipmentDate);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.inventory.shipped")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "在庫入荷", description = "在庫を入荷します。")
    @PostMapping("/{warehouseCode}/{productCode}/{lotNumber}/{stockCategory}/{qualityCategory}/receive")
    @AuditAnnotation(process = ApplicationExecutionProcessType.在庫入荷, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> receiveStock(
            @PathVariable String warehouseCode,
            @PathVariable String productCode,
            @PathVariable String lotNumber,
            @PathVariable String stockCategory,
            @PathVariable String qualityCategory,
            @RequestParam Integer receiptQuantity) {
        try {
            InventoryKey key = InventoryKey.of(warehouseCode, productCode, lotNumber, stockCategory, qualityCategory);
            inventoryService.receiveStock(key, receiptQuantity);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.inventory.received")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "在庫ルールチェック", description = "在庫業務ルールをチェックします。")
    @PostMapping("/check")
    @AuditAnnotation(process = ApplicationExecutionProcessType.在庫ルールチェック, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> checkInventoryRules(@RequestBody String requestBody) {
        try {
            InventoryRuleCheckList result = inventoryService.checkRule();
            if (!result.hasErrors()) {
                return ResponseEntity.ok(new MessageResponseWithDetail(message.getMessage("success.inventory.rule.check.no.issues"), result.getCheckList()));
            }
            return ResponseEntity.ok(new MessageResponseWithDetail(message.getMessage("success.inventory.rule.check.with.issues"), result.getCheckList()));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}