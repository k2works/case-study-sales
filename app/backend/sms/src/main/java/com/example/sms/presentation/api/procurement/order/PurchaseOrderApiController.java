package com.example.sms.presentation.api.procurement.order;

import com.example.sms.domain.model.procurement.order.PurchaseOrder;
import com.example.sms.domain.model.procurement.order.rule.PurchaseOrderRuleCheckList;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessType;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponseWithDetail;
import com.example.sms.service.BusinessException;
import com.example.sms.service.PageNationService;
import com.example.sms.service.procurement.order.PurchaseOrderCriteria;
import com.example.sms.service.procurement.order.PurchaseOrderService;
import com.example.sms.service.procurement.order.PurchaseOrderUploadErrorList;
import com.example.sms.service.system.audit.AuditAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.example.sms.presentation.api.procurement.order.PurchaseOrderResourceDTOMapper.convertToCriteria;
import static com.example.sms.presentation.api.procurement.order.PurchaseOrderResourceDTOMapper.convertToEntity;

/**
 * 発注API
 */
@RestController
@RequestMapping("/api/purchase-orders")
@Tag(name = "PurchaseOrder", description = "発注")
public class PurchaseOrderApiController {
    final PurchaseOrderService purchaseOrderService;
    final PageNationService pageNationService;
    final Message message;

    public PurchaseOrderApiController(PurchaseOrderService purchaseOrderService, PageNationService pageNationService, Message message) {
        this.purchaseOrderService = purchaseOrderService;
        this.pageNationService = pageNationService;
        this.message = message;
    }

    @Operation(summary = "発注一覧を取得する")
    @GetMapping
    public ResponseEntity<?> selectAll(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PageInfo<PurchaseOrder> pageInfo = purchaseOrderService.selectAllWithPageInfo();
            PageInfo<PurchaseOrderResource> result = pageNationService.getPageInfo(pageInfo, PurchaseOrderResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "発注を取得する")
    @GetMapping("/{purchaseOrderNumber}")
    public ResponseEntity<?> select(@PathVariable String purchaseOrderNumber) {
        try {
            PurchaseOrder entity = purchaseOrderService.find(purchaseOrderNumber);
            if (entity == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.purchase.order.not.exist")));
            }
            PurchaseOrderResource result = PurchaseOrderResource.from(entity);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "発注を登録する")
    @PostMapping
    @AuditAnnotation(process = ApplicationExecutionProcessType.発注登録, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> create(@RequestBody PurchaseOrderResource resource) {
        try {
            PurchaseOrder purchaseOrder = convertToEntity(resource);
            if (purchaseOrder.getPurchaseOrderNumber() != null && 
                purchaseOrderService.find(purchaseOrder.getPurchaseOrderNumber().getValue()) != null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.purchase.order.already.exist")));
            }
            purchaseOrderService.register(purchaseOrder);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.purchase.order.registered")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "発注を一括登録する", description = "ファイルアップロードで発注を登録する")
    @PostMapping("/upload")
    @AuditAnnotation(process = ApplicationExecutionProcessType.発注登録, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        try {
            PurchaseOrderUploadErrorList result = purchaseOrderService.uploadCsvFile(file);
            if (result.isEmpty()) {
                return ResponseEntity.ok(new MessageResponseWithDetail(message.getMessage("success.purchase.order.upload"), result.asList()));
            }
            return ResponseEntity.ok(new MessageResponseWithDetail(message.getMessage("error.purchase.order.upload"), result.asList()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "発注を更新する")
    @PutMapping("/{purchaseOrderNumber}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.発注更新, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> update(@PathVariable String purchaseOrderNumber, @RequestBody PurchaseOrderResource resource) {
        try {
            resource.setPurchaseOrderNumber(purchaseOrderNumber);
            PurchaseOrder purchaseOrder = convertToEntity(resource);
            if (purchaseOrderService.find(purchaseOrderNumber) == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.purchase.order.not.exist")));
            }
            purchaseOrderService.save(purchaseOrder);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.purchase.order.updated")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "発注を削除する")
    @DeleteMapping("/{purchaseOrderNumber}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.発注削除, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> delete(@PathVariable String purchaseOrderNumber) {
        try {
            PurchaseOrder purchaseOrder = purchaseOrderService.find(purchaseOrderNumber);
            if (purchaseOrder == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.purchase.order.not.exist")));
            }
            purchaseOrderService.delete(purchaseOrderNumber);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.purchase.order.deleted")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "発注を検索する")
    @PostMapping("/search")
    public ResponseEntity<?> search(
            @RequestBody PurchaseOrderCriteriaResource resource,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PurchaseOrderCriteria criteria = convertToCriteria(resource);
            PageInfo<PurchaseOrder> entity = purchaseOrderService.searchPurchaseOrderWithPageInfo(criteria);
            PageInfo<PurchaseOrderResource> result = pageNationService.getPageInfo(entity, PurchaseOrderResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "発注ルールを確認する", description = "発注ルール確認を実行する")
    @PostMapping("/check")
    @AuditAnnotation(process = ApplicationExecutionProcessType.発注登録, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> check() {
        try {
            PurchaseOrderRuleCheckList result = purchaseOrderService.checkRule();
            if (!result.hasErrors()) {
                return ResponseEntity.ok(new MessageResponseWithDetail(message.getMessage("success.purchase.order.check"), result.getCheckList()));
            }
            return ResponseEntity.ok(new MessageResponseWithDetail(message.getMessage("error.purchase.order.check"), result.getCheckList()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}