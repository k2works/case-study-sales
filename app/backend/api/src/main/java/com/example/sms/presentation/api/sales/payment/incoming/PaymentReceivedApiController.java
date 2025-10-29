package com.example.sms.presentation.api.sales.payment.incoming;

import com.example.sms.domain.model.sales.payment.incoming.PaymentReceived;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessType;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.BusinessException;
import com.example.sms.service.PageNationService;
import com.example.sms.service.sales.payment.incoming.PaymentReceivedCriteria;
import com.example.sms.service.sales.payment.incoming.PaymentReceivedService;
import com.example.sms.service.system.audit.AuditAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.example.sms.presentation.api.sales.payment.incoming.PaymentReceivedResourceDTOMapper.convertToEntity;
import static com.example.sms.presentation.api.sales.payment.incoming.PaymentReceivedResourceDTOMapper.convertToCriteria;

/**
 * 入金データAPI
 */
@RestController
@RequestMapping("/api/payments")
@Tag(name = "PaymentReceived", description = "入金データ")
public class PaymentReceivedApiController {
    final PaymentReceivedService paymentReceivedService;
    final PageNationService pageNationService;
    final Message message;

    public PaymentReceivedApiController(PaymentReceivedService paymentReceivedService, PageNationService pageNationService, Message message) {
        this.paymentReceivedService = paymentReceivedService;
        this.pageNationService = pageNationService;
        this.message = message;
    }

    @Operation(summary = "入金データ一覧を取得する", description = "入金データ一覧を取得する")
    @GetMapping
    public ResponseEntity<?> select(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PageInfo<PaymentReceived> pageInfo = paymentReceivedService.selectAllWithPageInfo();
            PageInfo<PaymentReceivedResource> result = pageNationService.getPageInfo(pageInfo, PaymentReceivedResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "入金データを取得する", description = "入金データを取得する")
    @GetMapping("/{paymentReceivedNumber}")
    public ResponseEntity<?> find(@PathVariable String paymentReceivedNumber) {
        try {
            Optional<PaymentReceived> paymentReceived = paymentReceivedService.findById(paymentReceivedNumber);
            if (paymentReceived.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(PaymentReceivedResource.from(paymentReceived.get()));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "入金データを登録する", description = "入金データを登録する")
    @PostMapping
    @AuditAnnotation(process = ApplicationExecutionProcessType.入金登録, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> create(@RequestBody @Validated PaymentReceivedResource resource) {
        try {
            PaymentReceived paymentReceived = convertToEntity(resource);
            Optional<PaymentReceived> existingPaymentReceived = paymentReceivedService.findById(resource.getPaymentNumber());
            if (existingPaymentReceived.isPresent()) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.payment.already.exist")));
            }
            paymentReceivedService.register(paymentReceived);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.payment.registered")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "入金データを更新する", description = "入金データを更新する")
    @PutMapping("/{paymentReceivedNumber}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.入金更新, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> update(@PathVariable String paymentReceivedNumber, @RequestBody PaymentReceivedResource resource) {
        try {
            resource.setPaymentNumber(paymentReceivedNumber);
            PaymentReceived paymentReceived = convertToEntity(resource);
            paymentReceivedService.save(paymentReceived);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.payment.updated")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "入金データを削除する", description = "入金データを削除する")
    @DeleteMapping("/{paymentReceivedNumber}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.入金削除, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> delete(@PathVariable String paymentReceivedNumber) {
        try {
            Optional<PaymentReceived> paymentReceived = paymentReceivedService.findById(paymentReceivedNumber);
            if (paymentReceived.isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.payment.not.exist")));
            }
            paymentReceivedService.delete(paymentReceived.get());
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.payment.deleted")));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "入金データを検索する", description = "指定された検索条件で入金データを検索します。")
    @PostMapping("/search")
    public ResponseEntity<?> search(
            @RequestBody PaymentReceivedCriteriaResource resource,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PaymentReceivedCriteria criteria = convertToCriteria(resource);
            PageInfo<PaymentReceived> entity = paymentReceivedService.searchWithPageInfo(criteria);
            PageInfo<PaymentReceivedResource> result = pageNationService.getPageInfo(entity, PaymentReceivedResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "入金データを集計する", description = "入金データを集計します。")
    @PostMapping("/aggregate")
    @AuditAnnotation(process = ApplicationExecutionProcessType.入金登録, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> aggregate(@RequestBody PaymentReceivedCriteriaResource resource) {
        try {
            paymentReceivedService.aggregate();
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.payment.aggregated")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
