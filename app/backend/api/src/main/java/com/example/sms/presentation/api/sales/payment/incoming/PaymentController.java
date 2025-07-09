package com.example.sms.presentation.api.sales.payment.incoming;

import com.example.sms.domain.model.sales.payment.incoming.Payment;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessType;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.BusinessException;
import com.example.sms.service.PageNationService;
import com.example.sms.service.sales.payment.incoming.PaymentCriteria;
import com.example.sms.service.sales.payment.incoming.PaymentService;
import com.example.sms.service.system.audit.AuditAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.example.sms.presentation.api.sales.payment.incoming.PaymentResourceDTOMapper.convertToEntity;
import static com.example.sms.presentation.api.sales.payment.incoming.PaymentResourceDTOMapper.convertToCriteria;

/**
 * 入金データAPI
 */
@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payment", description = "入金データ")
@PreAuthorize("hasRole('ADMIN')")
public class PaymentController {
    final PaymentService paymentService;
    final PageNationService pageNationService;
    final Message message;

    public PaymentController(PaymentService paymentService, PageNationService pageNationService, Message message) {
        this.paymentService = paymentService;
        this.pageNationService = pageNationService;
        this.message = message;
    }

    @Operation(summary = "入金データ一覧を取得する", description = "入金データ一覧を取得する")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> select(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PageInfo<Payment> pageInfo = paymentService.selectAllWithPageInfo();
            PageInfo<PaymentResource> result = pageNationService.getPageInfo(pageInfo, PaymentResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "入金データを取得する", description = "入金データを取得する")
    @GetMapping("/{paymentNumber}")
    public ResponseEntity<?> find(@PathVariable String paymentNumber) {
        try {
            Optional<Payment> payment = paymentService.findById(paymentNumber);
            if (payment.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(PaymentResource.from(payment.get()));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "入金データを登録する", description = "入金データを登録する")
    @PostMapping
    @AuditAnnotation(process = ApplicationExecutionProcessType.その他, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> create(@RequestBody @Validated PaymentResource resource) {
        try {
            Payment payment = convertToEntity(resource);
            Optional<Payment> existingPayment = paymentService.findById(resource.getPaymentNumber());
            if (existingPayment.isPresent()) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.payment.already.exist")));
            }
            paymentService.register(payment);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.payment.registered")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "入金データを更新する", description = "入金データを更新する")
    @PutMapping("/{paymentNumber}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.その他, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> update(@PathVariable String paymentNumber, @RequestBody PaymentResource resource) {
        try {
            resource.setPaymentNumber(paymentNumber);
            Payment payment = convertToEntity(resource);
            paymentService.save(payment);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.payment.updated")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "入金データを削除する", description = "入金データを削除する")
    @DeleteMapping("/{paymentNumber}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.その他, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> delete(@PathVariable String paymentNumber) {
        try {
            Optional<Payment> payment = paymentService.findById(paymentNumber);
            if (payment.isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.payment.not.exist")));
            }
            paymentService.delete(payment.get());
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.payment.deleted")));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "全ての入金データを取得する", description = "全ての入金データを取得する")
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> selectAll() {
        try {
            List<Payment> payments = paymentService.selectAll();
            List<PaymentResource> resources = payments.stream()
                    .map(PaymentResource::from)
                    .toList();
            return ResponseEntity.ok(resources);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "顧客コードで入金データを検索する", description = "顧客コードで入金データを検索する")
    @GetMapping("/customer/{customerCode}/{branchNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> findByCustomer(
            @PathVariable String customerCode,
            @PathVariable Integer branchNumber) {
        try {
            List<Payment> payments = paymentService.findByCustomer(customerCode, branchNumber);
            List<PaymentResource> resources = payments.stream()
                    .map(PaymentResource::from)
                    .toList();
            return ResponseEntity.ok(resources);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "入金口座コードで入金データを検索する", description = "入金口座コードで入金データを検索する")
    @GetMapping("/account/{accountCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> findByAccount(@PathVariable String accountCode) {
        try {
            List<Payment> payments = paymentService.findByAccount(accountCode);
            List<PaymentResource> resources = payments.stream()
                    .map(PaymentResource::from)
                    .toList();
            return ResponseEntity.ok(resources);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "入金データを検索する", description = "指定された検索条件で入金データを検索します。")
    @PostMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> search(
            @RequestBody PaymentCriteriaResource resource,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PaymentCriteria criteria = convertToCriteria(resource);
            PageInfo<Payment> entity = paymentService.searchWithPageInfo(criteria);
            PageInfo<PaymentResource> result = pageNationService.getPageInfo(entity, PaymentResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
