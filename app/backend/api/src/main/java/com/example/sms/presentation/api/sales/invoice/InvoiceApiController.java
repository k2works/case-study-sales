package com.example.sms.presentation.api.sales.invoice;

import com.example.sms.domain.model.sales.invoice.Invoice;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessType;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.BusinessException;
import com.example.sms.service.PageNationService;
import com.example.sms.service.sales.invoice.InvoiceCriteria;
import com.example.sms.service.sales.invoice.InvoiceService;
import com.example.sms.service.system.audit.AuditAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 請求API
 */
@RestController
@RequestMapping("/api/invoices")
@Tag(name = "Invoice", description = "請求管理")
public class InvoiceApiController {
    final InvoiceService invoiceService;
    final PageNationService pageNationService;
    final Message message;

    public InvoiceApiController(InvoiceService invoiceService, PageNationService pageNationService, Message message) {
        this.invoiceService = invoiceService;
        this.pageNationService = pageNationService;
        this.message = message;
    }

    @Operation(summary = "請求一覧を取得する", description = "全ての請求データをページ情報付きで取得します。")
    @GetMapping
    public ResponseEntity<?> selectAll(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PageInfo<Invoice> pageInfo = invoiceService.selectAllWithPageInfo();
            PageInfo<InvoiceResource> result = pageNationService.getPageInfo(pageInfo, InvoiceResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "請求情報を取得する", description = "指定された請求番号に基づいて請求データを取得します。")
    @GetMapping("/{invoiceNumber}")
    public ResponseEntity<?> select(@PathVariable String invoiceNumber) {
        try {
            Invoice entity = invoiceService.find(invoiceNumber);
            if (entity == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.invoice.not.exist")));
            }
            InvoiceResource result = InvoiceResource.from(entity);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "請求を登録する", description = "新しい請求データを登録します。")
    @PostMapping
    @AuditAnnotation(process = ApplicationExecutionProcessType.請求登録, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> create(@RequestBody InvoiceResource resource) {
        try {
            Invoice invoice = InvoiceResourceDTOMapper.convertToEntity(resource);
            invoiceService.register(invoice);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.invoice.registered")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "請求情報を更新する", description = "指定された請求データを更新します。")
    @PutMapping("/{invoiceNumber}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.請求更新, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> update(@PathVariable String invoiceNumber, @RequestBody InvoiceResource resource) {
        try {
            resource.setInvoiceNumber(invoiceNumber);
            Invoice invoice = InvoiceResourceDTOMapper.convertToEntity(resource);
            if (invoiceService.find(invoiceNumber) == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.invoice.not.exist")));
            }
            invoiceService.save(invoice);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.invoice.updated")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "請求を削除する", description = "指定された請求データを削除します。")
    @DeleteMapping("/{invoiceNumber}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.請求削除, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> delete(@PathVariable String invoiceNumber) {
        try {
            Invoice invoice = invoiceService.find(invoiceNumber);
            if (invoice == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.invoice.not.exist")));
            }
            invoiceService.delete(invoice);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.invoice.deleted")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "請求を検索する", description = "指定された検索条件で請求データを検索します。")
    @PostMapping("/search")
    public ResponseEntity<?> search(
            @RequestBody InvoiceCriteriaResource resource,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            InvoiceCriteria criteria = InvoiceResourceDTOMapper.convertToCriteria(resource);
            PageInfo<Invoice> entity = invoiceService.searchWithPageInfo(criteria);
            PageInfo<InvoiceResource> result = pageNationService.getPageInfo(entity, InvoiceResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "請求を集計する", description = "請求を集計します。")
    @PostMapping("/aggregate")
    @AuditAnnotation(process = ApplicationExecutionProcessType.請求登録, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> aggregate(@RequestBody InvoiceResource resource) {
        try {
            invoiceService.aggregate();
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.invoice.aggregated")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
