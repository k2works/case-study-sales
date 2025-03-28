package com.example.sms.presentation.api.sales_order;

import com.example.sms.domain.model.sales_order.*;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessType;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponseWithDetail;
import com.example.sms.service.BusinessException;
import com.example.sms.service.PageNationService;
import com.example.sms.service.sales_order.SalesOrderCriteria;
import com.example.sms.domain.model.sales_order.rule.SalesOrderRuleCheckList;
import com.example.sms.service.sales_order.SalesOrderService;
import com.example.sms.service.sales_order.SalesOrderUploadErrorList;
import com.example.sms.service.system.audit.AuditAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.example.sms.presentation.api.sales_order.SalesOrderResourceDTOMapper.convertToCriteria;
import static com.example.sms.presentation.api.sales_order.SalesOrderResourceDTOMapper.convertToEntity;

/**
 * 受注API
 */
@RestController
@RequestMapping("/api/sales-orders")
@Tag(name = "SalesOrder", description = "受注")
public class SalesOrderApiController {
    final SalesOrderService salesOrderService;
    final PageNationService pageNationService;
    final Message message;

    public SalesOrderApiController(SalesOrderService salesOrderService, PageNationService pageNationService, Message message) {
        this.salesOrderService = salesOrderService;
        this.pageNationService = pageNationService;
        this.message = message;
    }

    @Operation(summary = "受注一覧を取得する")
    @GetMapping
    public ResponseEntity<?> selectAll(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PageInfo<SalesOrder> pageInfo = salesOrderService.selectAllWithPageInfo();
            PageInfo<SalesOrderResource> result = pageNationService.getPageInfo(pageInfo, SalesOrderResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "受注を取得する")
    @GetMapping("/{orderNumber}")
    public ResponseEntity<?> select(@PathVariable String orderNumber) {
        try {
            SalesOrder entity = salesOrderService.find(orderNumber);
            if (entity == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.sales-order.not.exist")));
            }
            SalesOrderResource result = SalesOrderResource.from(entity);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "受注を登録する")
    @PostMapping
    @AuditAnnotation(process = ApplicationExecutionProcessType.受注登録, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> create(@RequestBody SalesOrderResource resource) {
        try {
            SalesOrder salesOrder = convertToEntity(resource);
            if (salesOrderService.find(salesOrder.getOrderNumber().getValue()) != null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.sales-order.already.exist")));
            }
            salesOrderService.register(salesOrder);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.sales-order.registered")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "受注を一括登録する", description = "ファイルアップロードで受注を登録する")
    @PostMapping("/upload")
    @AuditAnnotation(process = ApplicationExecutionProcessType.受注登録, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        try {
            SalesOrderUploadErrorList result = salesOrderService.uploadCsvFile(file);
            if (result.isEmpty()) {
                return ResponseEntity.ok(new MessageResponseWithDetail(message.getMessage("success.sales-order.upload"), result.asList()));
            }
            return ResponseEntity.ok(new MessageResponseWithDetail(message.getMessage("error.sales-order.upload"), result.asList()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "受注を更新する")
    @PutMapping("/{orderNumber}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.受注更新, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> update(@PathVariable String orderNumber, @RequestBody SalesOrderResource resource) {
        try {
            resource.setOrderNumber(orderNumber);
            SalesOrder salesOrder = convertToEntity(resource);
            if (salesOrderService.find(orderNumber) == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.sales-order.not.exist")));
            }
            salesOrderService.save(salesOrder);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.sales-order.updated")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "受注を削除する")
    @DeleteMapping("/{orderNumber}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.受注削除, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> delete(@PathVariable String orderNumber) {
        try {
            SalesOrder salesOrder = salesOrderService.find(orderNumber);
            if (salesOrder == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.sales-order.not.exist")));
            }
            salesOrderService.delete(salesOrder);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.sales-order.deleted")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "受注を検索する")
    @PostMapping("/search")
    public ResponseEntity<?> search(
            @RequestBody SalesOrderCriteriaResource resource,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            SalesOrderCriteria criteria = convertToCriteria(resource);
            PageInfo<SalesOrder> entity = salesOrderService.searchSalesOrderWithPageInfo(criteria);
            PageInfo<SalesOrderResource> result = pageNationService.getPageInfo(entity, SalesOrderResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "受注ルールを確認する", description = "受注ルール確認を実行する")
    @PostMapping("/check")
    @AuditAnnotation(process = ApplicationExecutionProcessType.受注登録, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> check() {
        try {
            SalesOrderRuleCheckList result = salesOrderService.checkRule();
            if (result.isEmpty()) {
                return ResponseEntity.ok(new MessageResponseWithDetail(message.getMessage("success.sales-order.check"), result.asList()));
            }
            return ResponseEntity.ok(new MessageResponseWithDetail(message.getMessage("error.sales-order.check"), result.asList()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

}
