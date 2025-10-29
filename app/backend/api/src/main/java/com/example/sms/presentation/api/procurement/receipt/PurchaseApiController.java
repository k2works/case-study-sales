package com.example.sms.presentation.api.procurement.receipt;

import com.example.sms.domain.model.procurement.purchase.PurchaseOrder;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessType;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.BusinessException;
import com.example.sms.service.PageNationService;
import com.example.sms.service.procurement.purchase.PurchaseOrderCriteria;
import com.example.sms.service.procurement.receipt.PurchaseService;
import com.example.sms.service.system.audit.AuditAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.sms.presentation.api.procurement.receipt.PurchaseResourceDTOMapper.convertToCriteria;
import static com.example.sms.presentation.api.procurement.receipt.PurchaseResourceDTOMapper.convertToEntity;

/**
 * 仕入API
 */
@RestController
@RequestMapping("/api/purchases")
@Tag(name = "Purchase", description = "仕入")
public class PurchaseApiController {
    final PurchaseService purchaseService;
    final PageNationService pageNationService;
    final Message message;

    public PurchaseApiController(PurchaseService purchaseService, PageNationService pageNationService, Message message) {
        this.purchaseService = purchaseService;
        this.pageNationService = pageNationService;
        this.message = message;
    }

    @Operation(summary = "仕入一覧を取得する")
    @GetMapping
    public ResponseEntity<?> selectAll(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PageInfo<PurchaseOrder> pageInfo = purchaseService.selectAllWithPageInfo();
            PageInfo<PurchaseResource> result = pageNationService.getPageInfo(pageInfo, PurchaseResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "仕入を取得する")
    @GetMapping("/{purchaseNumber}")
    public ResponseEntity<?> select(@PathVariable String purchaseNumber) {
        try {
            PurchaseOrder entity = purchaseService.find(purchaseNumber);
            if (entity == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.purchase.not.exist")));
            }
            PurchaseResource result = PurchaseResource.from(entity);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "仕入を登録する")
    @PostMapping
    @AuditAnnotation(process = ApplicationExecutionProcessType.仕入登録, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> create(@RequestBody PurchaseResource resource) {
        try {
            PurchaseOrder purchaseOrder = convertToEntity(resource);
            if (purchaseOrder.getPurchaseOrderNumber() != null &&
                purchaseService.find(purchaseOrder.getPurchaseOrderNumber().getValue()) != null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.purchase.already.exist")));
            }
            purchaseService.register(purchaseOrder);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.purchase.registered")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "仕入を更新する")
    @PutMapping("/{purchaseNumber}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.仕入更新, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> update(@PathVariable String purchaseNumber, @RequestBody PurchaseResource resource) {
        try {
            resource.setPurchaseNumber(purchaseNumber);
            PurchaseOrder purchaseOrder = convertToEntity(resource);
            if (purchaseService.find(purchaseNumber) == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.purchase.not.exist")));
            }
            purchaseService.save(purchaseOrder);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.purchase.updated")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "仕入を削除する")
    @DeleteMapping("/{purchaseNumber}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.仕入削除, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> delete(@PathVariable String purchaseNumber) {
        try {
            PurchaseOrder purchaseOrder = purchaseService.find(purchaseNumber);
            if (purchaseOrder == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.purchase.not.exist")));
            }
            purchaseService.delete(purchaseOrder);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.purchase.deleted")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "仕入を検索する")
    @PostMapping("/search")
    public ResponseEntity<?> search(
            @RequestBody PurchaseCriteriaResource resource,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PurchaseOrderCriteria criteria = convertToCriteria(resource);
            PageInfo<PurchaseOrder> entity = purchaseService.searchPurchaseOrderWithPageInfo(criteria);
            PageInfo<PurchaseResource> result = pageNationService.getPageInfo(entity, PurchaseResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
