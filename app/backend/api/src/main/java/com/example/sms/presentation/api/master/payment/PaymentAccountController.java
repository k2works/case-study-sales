package com.example.sms.presentation.api.master.payment;

import com.example.sms.domain.model.master.payment.account.incoming.PaymentAccount;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessType;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.BusinessException;
import com.example.sms.service.PageNationService;
import com.example.sms.service.master.payment.PaymentAccountService;
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

import static com.example.sms.presentation.api.master.payment.PaymentAccountResourceDTOMapper.convertToEntity;

/**
 * 入金口座API
 */
@RestController
@RequestMapping("/api/payment-accounts")
@Tag(name = "PaymentAccount", description = "入金口座")
@PreAuthorize("hasRole('ADMIN')")
public class PaymentAccountController {
    final PaymentAccountService paymentAccountService;
    final PageNationService pageNationService;
    final Message message;

    public PaymentAccountController(PaymentAccountService paymentAccountService, PageNationService pageNationService, Message message) {
        this.paymentAccountService = paymentAccountService;
        this.pageNationService = pageNationService;
        this.message = message;
    }

    @Operation(summary = "入金口座一覧を取得する", description = "入金口座一覧を取得する")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> select(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PageInfo<PaymentAccount> pageInfo = paymentAccountService.selectAllWithPageInfo();
            PageInfo<PaymentAccountResource> result = pageNationService.getPageInfo(pageInfo, PaymentAccountResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "入金口座を取得する", description = "入金口座を取得する")
    @GetMapping("/{accountCode}")
    public ResponseEntity<?> find(@PathVariable String accountCode) {
        try {
            Optional<PaymentAccount> paymentAccount = paymentAccountService.findById(accountCode);
            if (paymentAccount.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(PaymentAccountResource.from(paymentAccount.get()));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "入金口座を登録する", description = "入金口座を登録する")
    @PostMapping
    @AuditAnnotation(process = ApplicationExecutionProcessType.その他, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> create(@RequestBody @Validated PaymentAccountResource resource) {
        try {
            PaymentAccount paymentAccount = convertToEntity(resource);
            Optional<PaymentAccount> existingAccount = paymentAccountService.findById(resource.getAccountCode());
            if (existingAccount.isPresent()) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.payment.account.already.exist")));
            }
            paymentAccountService.register(paymentAccount);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.payment.account.registered")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "入金口座を更新する", description = "入金口座を更新する")
    @PutMapping("/{accountCode}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.その他, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> update(@PathVariable String accountCode, @RequestBody PaymentAccountResource resource) {
        try {
            resource.setAccountCode(accountCode);
            PaymentAccount paymentAccount = convertToEntity(resource);
            paymentAccountService.save(paymentAccount);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.payment.account.updated")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "入金口座を削除する", description = "入金口座を削除する")
    @DeleteMapping("/{accountCode}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.その他, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> delete(@PathVariable String accountCode) {
        try {
            Optional<PaymentAccount> paymentAccount = paymentAccountService.findById(accountCode);
            if (paymentAccount.isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.payment.account.not.exist")));
            }
            paymentAccountService.delete(paymentAccount.get());
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.payment.account.deleted")));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "全ての入金口座を取得する", description = "全ての入金口座を取得する")
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> selectAll() {
        try {
            List<PaymentAccount> accounts = paymentAccountService.selectAll();
            List<PaymentAccountResource> resources = accounts.stream()
                    .map(PaymentAccountResource::from)
                    .toList();
            return ResponseEntity.ok(resources);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
