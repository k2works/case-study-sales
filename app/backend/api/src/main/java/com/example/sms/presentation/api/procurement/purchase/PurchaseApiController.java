package com.example.sms.presentation.api.procurement.purchase;

import com.example.sms.domain.model.procurement.purchase.Purchase;
import com.example.sms.domain.model.procurement.purchase.rule.PurchaseRuleCheckList;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessType;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponseWithDetail;
import com.example.sms.service.BusinessException;
import com.example.sms.service.PageNationService;
import com.example.sms.service.procurement.purchase.PurchaseCriteria;
import com.example.sms.service.procurement.purchase.PurchaseService;
import com.example.sms.service.system.audit.AuditAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.sms.presentation.api.procurement.purchase.PurchaseResourceDTOMapper.convertToCriteria;
import static com.example.sms.presentation.api.procurement.purchase.PurchaseResourceDTOMapper.convertToEntity;

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
            PageInfo<Purchase> pageInfo = purchaseService.selectAllWithPageInfo();
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
            Purchase entity = purchaseService.find(purchaseNumber);
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
            Purchase purchase = convertToEntity(resource);
            if (purchase.getPurchaseNumber() != null &&
                purchaseService.find(purchase.getPurchaseNumber().getValue()) != null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.purchase.already.exist")));
            }
            purchaseService.register(purchase);
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
            Purchase purchase = convertToEntity(resource);
            if (purchaseService.find(purchaseNumber) == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.purchase.not.exist")));
            }
            purchaseService.save(purchase);
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
            Purchase purchase = purchaseService.find(purchaseNumber);
            if (purchase == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.purchase.not.exist")));
            }
            purchaseService.delete(purchase);
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
            PurchaseCriteria criteria = convertToCriteria(resource);
            PageInfo<Purchase> entity = purchaseService.searchPurchaseWithPageInfo(criteria);
            PageInfo<PurchaseResource> result = pageNationService.getPageInfo(entity, PurchaseResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "仕入ルールを確認する", description = "仕入ルール確認を実行する")
    @PostMapping("/check")
    @AuditAnnotation(process = ApplicationExecutionProcessType.仕入登録, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> check() {
        try {
            PurchaseRuleCheckList result = purchaseService.checkRule();
            if (!result.hasErrors()) {
                return ResponseEntity.ok(new MessageResponseWithDetail(message.getMessage("success.purchase.check"), result.getCheckList()));
            }
            return ResponseEntity.ok(new MessageResponseWithDetail(message.getMessage("error.purchase.check"), result.getCheckList()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
