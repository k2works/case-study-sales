package com.example.sms.presentation.api.sales_order;

import com.example.sms.domain.model.sales_order.*;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessType;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponseWithDetail;
import com.example.sms.service.BusinessException;
import com.example.sms.service.sales_order.SalesOrderCriteria;
import com.example.sms.service.sales_order.SalesOrderService;
import com.example.sms.service.sales_order.SalesOrderUploadErrorList;
import com.example.sms.service.system.audit.AuditAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * 受注API
 */
@RestController
@RequestMapping("/api/sales-orders")
@Tag(name = "SalesOrder", description = "受注")
public class SalesOrderApiController {
    final SalesOrderService salesOrderService;
    final Message message;

    public SalesOrderApiController(SalesOrderService salesOrderService, Message message) {
        this.salesOrderService = salesOrderService;
        this.message = message;
    }

    @Operation(summary = "受注一覧を取得する")
    @GetMapping
    public ResponseEntity<?> selectAll(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PageInfo<SalesOrder> result = salesOrderService.selectAllWithPageInfo();
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "受注を取得する")
    @GetMapping("/{orderNumber}")
    public ResponseEntity<?> select(@PathVariable String orderNumber) {
        try {
            SalesOrder result = salesOrderService.find(orderNumber);
            if (result == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.sales-order.not.exist")));
            }
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
        } catch (BusinessException | IllegalArgumentException e) {
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
            PageInfo<SalesOrder> result = salesOrderService.searchSalesOrderWithPageInfo(criteria);
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
            List<Map<String, String>> result = salesOrderService.checkRule();
            if (result.isEmpty()) {
                return ResponseEntity.ok(new MessageResponseWithDetail(message.getMessage("success.sales-order.check"), result));
            }
            return ResponseEntity.ok(new MessageResponseWithDetail(message.getMessage("error.sales-order.check"), result));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    private SalesOrder convertToEntity(SalesOrderResource resource) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        List<SalesOrderLine> salesOrderLines = resource.getSalesOrderLines() != null
                ? resource.getSalesOrderLines().stream().map(line -> SalesOrderLine.of(
                        line.getOrderNumber(),
                        line.getOrderLineNumber(),
                        line.getProductCode(),
                        line.getProductName(),
                        line.getSalesUnitPrice(),
                        line.getOrderQuantity(),
                        line.getTaxRate(),
                        line.getAllocationQuantity(),
                        line.getShipmentInstructionQuantity(),
                        line.getShippedQuantity(),
                        line.getCompletionFlag(),
                        line.getDiscountAmount(),
                        LocalDateTime.parse(line.getDeliveryDate(), formatter)
                )).toList()
                : null;

        return SalesOrder.of(
                resource.getOrderNumber(),
                LocalDateTime.parse(resource.getOrderDate(), formatter),
                resource.getDepartmentCode(),
                LocalDateTime.parse(resource.getDepartmentStartDate(), formatter),
                resource.getCustomerCode(),
                resource.getCustomerBranchNumber(),
                resource.getEmployeeCode(),
                LocalDateTime.parse(resource.getDesiredDeliveryDate(), formatter),
                resource.getCustomerOrderNumber(),
                resource.getWarehouseCode(),
                resource.getTotalOrderAmount(),
                resource.getTotalConsumptionTax(),
                resource.getRemarks(),
                salesOrderLines
        );
    }

    private SalesOrderCriteria convertToCriteria(SalesOrderCriteriaResource resource) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        LocalDateTime orderDate = resource.getOrderDate() != null ? LocalDateTime.parse(resource.getOrderDate(), formatter) : null;
        LocalDateTime departmentStartDate = resource.getDepartmentStartDate() != null ? LocalDateTime.parse(resource.getDepartmentStartDate(), formatter) : null;
        LocalDateTime desiredDeliveryDate = resource.getDesiredDeliveryDate() != null ? LocalDateTime.parse(resource.getDesiredDeliveryDate(), formatter) : null;

        return SalesOrderCriteria.builder()
                .orderNumber(resource.getOrderNumber())
                .orderDate(orderDate)
                .departmentCode(resource.getDepartmentCode())
                .departmentStartDate(departmentStartDate)
                .customerCode(resource.getCustomerCode())
                .customerBranchNumber(resource.getCustomerBranchNumber())
                .employeeCode(resource.getEmployeeCode())
                .desiredDeliveryDate(desiredDeliveryDate)
                .customerOrderNumber(resource.getCustomerOrderNumber())
                .warehouseCode(resource.getWarehouseCode())
                .remarks(resource.getRemarks())
                .build();
    }
}