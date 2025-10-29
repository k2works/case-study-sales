package com.example.sms.presentation.api.procurement.payment;

import com.example.sms.domain.model.procurement.payment.PurchasePayment;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessType;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.BusinessException;
import com.example.sms.service.PageNationService;
import com.example.sms.service.procurement.payment.PurchasePaymentCriteria;
import com.example.sms.service.procurement.payment.PurchasePaymentService;
import com.example.sms.service.system.audit.AuditAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.example.sms.presentation.api.procurement.payment.PurchasePaymentResourceDTOMapper.convertToEntity;
import static com.example.sms.presentation.api.procurement.payment.PurchasePaymentResourceDTOMapper.convertToCriteria;

/**
 * 支払データAPI
 */
@RestController
@RequestMapping("/api/purchase-payments")
@Tag(name = "PurchasePayment", description = "支払データ")
public class PurchasePaymentApiController {
    final PurchasePaymentService purchasePaymentService;
    final PageNationService pageNationService;
    final Message message;

    public PurchasePaymentApiController(PurchasePaymentService purchasePaymentService, PageNationService pageNationService, Message message) {
        this.purchasePaymentService = purchasePaymentService;
        this.pageNationService = pageNationService;
        this.message = message;
    }

    @Operation(summary = "支払データ一覧を取得する", description = "支払データ一覧を取得する")
    @GetMapping
    public ResponseEntity<?> select(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PageInfo<PurchasePayment> pageInfo = purchasePaymentService.selectAllWithPageInfo();
            PageInfo<PurchasePaymentResource> result = pageNationService.getPageInfo(pageInfo, PurchasePaymentResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "支払データを取得する", description = "支払データを取得する")
    @GetMapping("/{paymentNumber}")
    public ResponseEntity<?> find(@PathVariable String paymentNumber) {
        try {
            Optional<PurchasePayment> payment = purchasePaymentService.findByPaymentNumber(paymentNumber);
            if (payment.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(PurchasePaymentResource.from(payment.get()));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "支払データを登録する", description = "支払データを登録する")
    @PostMapping
    @AuditAnnotation(process = ApplicationExecutionProcessType.支払登録, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> create(@RequestBody @Validated PurchasePaymentResource resource) {
        try {
            PurchasePayment payment = convertToEntity(resource);
            Optional<PurchasePayment> existingPayment = purchasePaymentService.findByPaymentNumber(resource.getPaymentNumber());
            if (existingPayment.isPresent()) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.purchase.payment.already.exist")));
            }
            purchasePaymentService.register(payment);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.purchase.payment.registered")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "支払データを更新する", description = "支払データを更新する")
    @PutMapping("/{paymentNumber}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.支払更新, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> update(@PathVariable String paymentNumber, @RequestBody PurchasePaymentResource resource) {
        try {
            resource.setPaymentNumber(paymentNumber);
            PurchasePayment payment = convertToEntity(resource);
            purchasePaymentService.save(payment);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.purchase.payment.updated")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "支払データを削除する", description = "支払データを削除する")
    @DeleteMapping("/{paymentNumber}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.支払削除, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> delete(@PathVariable String paymentNumber) {
        try {
            Optional<PurchasePayment> payment = purchasePaymentService.findByPaymentNumber(paymentNumber);
            if (payment.isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.purchase.payment.not.exist")));
            }
            purchasePaymentService.delete(paymentNumber);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.purchase.payment.deleted")));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "支払データを検索する", description = "指定された検索条件で支払データを検索します。")
    @PostMapping("/search")
    public ResponseEntity<?> search(
            @RequestBody PurchasePaymentCriteriaResource resource,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PurchasePaymentCriteria criteria = convertToCriteria(resource);
            PageInfo<PurchasePayment> entity = purchasePaymentService.searchWithPageInfo(criteria);
            PageInfo<PurchasePaymentResource> result = pageNationService.getPageInfo(entity, PurchasePaymentResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
