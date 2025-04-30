package com.example.sms.presentation.api.sales.order;

import com.example.sms.domain.model.sales.order.Order;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessType;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponseWithDetail;
import com.example.sms.service.BusinessException;
import com.example.sms.service.PageNationService;
import com.example.sms.service.sales.order.SalesOrderCriteria;
import com.example.sms.domain.model.sales.order.rule.OrderRuleCheckList;
import com.example.sms.service.sales.order.SalesOrderService;
import com.example.sms.service.sales.order.SalesOrderUploadErrorList;
import com.example.sms.service.system.audit.AuditAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.example.sms.presentation.api.sales.order.OrderResourceDTOMapper.convertToCriteria;
import static com.example.sms.presentation.api.sales.order.OrderResourceDTOMapper.convertToEntity;

/**
 * 受注API
 */
@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order", description = "受注")
public class OrderApiController {
    final SalesOrderService salesOrderService;
    final PageNationService pageNationService;
    final Message message;

    public OrderApiController(SalesOrderService salesOrderService, PageNationService pageNationService, Message message) {
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
            PageInfo<Order> pageInfo = salesOrderService.selectAllWithPageInfo();
            PageInfo<OrderResource> result = pageNationService.getPageInfo(pageInfo, OrderResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "受注を取得する")
    @GetMapping("/{orderNumber}")
    public ResponseEntity<?> select(@PathVariable String orderNumber) {
        try {
            Order entity = salesOrderService.find(orderNumber);
            if (entity == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.order.not.exist")));
            }
            OrderResource result = OrderResource.from(entity);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "受注を登録する")
    @PostMapping
    @AuditAnnotation(process = ApplicationExecutionProcessType.受注登録, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> create(@RequestBody OrderResource resource) {
        try {
            Order order = convertToEntity(resource);
            if (salesOrderService.find(order.getOrderNumber().getValue()) != null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.order.already.exist")));
            }
            salesOrderService.register(order);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.order.registered")));
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
                return ResponseEntity.ok(new MessageResponseWithDetail(message.getMessage("success.order.upload"), result.asList()));
            }
            return ResponseEntity.ok(new MessageResponseWithDetail(message.getMessage("error.order.upload"), result.asList()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "受注を更新する")
    @PutMapping("/{orderNumber}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.受注更新, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> update(@PathVariable String orderNumber, @RequestBody OrderResource resource) {
        try {
            resource.setOrderNumber(orderNumber);
            Order order = convertToEntity(resource);
            if (salesOrderService.find(orderNumber) == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.order.not.exist")));
            }
            salesOrderService.save(order);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.order.updated")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "受注を削除する")
    @DeleteMapping("/{orderNumber}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.受注削除, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> delete(@PathVariable String orderNumber) {
        try {
            Order order = salesOrderService.find(orderNumber);
            if (order == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.order.not.exist")));
            }
            salesOrderService.delete(order);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.order.deleted")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "受注を検索する")
    @PostMapping("/search")
    public ResponseEntity<?> search(
            @RequestBody OrderCriteriaResource resource,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            SalesOrderCriteria criteria = convertToCriteria(resource);
            PageInfo<Order> entity = salesOrderService.searchSalesOrderWithPageInfo(criteria);
            PageInfo<OrderResource> result = pageNationService.getPageInfo(entity, OrderResource::from);
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
            OrderRuleCheckList result = salesOrderService.checkRule();
            if (result.isEmpty()) {
                return ResponseEntity.ok(new MessageResponseWithDetail(message.getMessage("success.order.check"), result.asList()));
            }
            return ResponseEntity.ok(new MessageResponseWithDetail(message.getMessage("error.order.check"), result.asList()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

}
