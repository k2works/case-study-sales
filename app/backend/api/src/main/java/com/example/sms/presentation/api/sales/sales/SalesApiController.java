package com.example.sms.presentation.api.sales.sales;

import com.example.sms.domain.model.sales.sales.Sales;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessType;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.BusinessException;
import com.example.sms.service.PageNationService;
import com.example.sms.service.sales.sales.SalesCriteria;
import com.example.sms.service.sales.sales.SalesService;
import com.example.sms.service.system.audit.AuditAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 売上API
 */
@RestController
@RequestMapping("/api/sales")
@Tag(name = "Sales", description = "売上管理")
public class SalesApiController {
    final SalesService salesService;
    final PageNationService pageNationService;
    final Message message;

    public SalesApiController(SalesService salesService, PageNationService pageNationService, Message message) {
        this.salesService = salesService;
        this.pageNationService = pageNationService;
        this.message = message;
    }

    @Operation(summary = "売上一覧を取得する", description = "全ての売上データをページ情報付きで取得します。")
    @GetMapping
    public ResponseEntity<?> selectAll(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PageInfo<Sales> pageInfo = salesService.selectAllWithPageInfo();
            PageInfo<SalesResource> result = pageNationService.getPageInfo(pageInfo, SalesResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "売上情報を取得する", description = "指定された売上番号に基づいて売上データを取得します。")
    @GetMapping("/{salesNumber}")
    public ResponseEntity<?> select(@PathVariable String salesNumber) {
        try {
            Sales entity = salesService.find(salesNumber);
            if (entity == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.sales.not.exist")));
            }
            SalesResource result = SalesResource.from(entity);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "売上を登録する", description = "新しい売上データを登録します。")
    @PostMapping
    @AuditAnnotation(process = ApplicationExecutionProcessType.売上登録, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> create(@RequestBody SalesResource resource) {
        try {
            Sales sales = SalesResourceDTOMapper.convertToEntity(resource);
            if (sales.getSalesNumber() != null) {
                if (salesService.find(sales.getSalesNumber().getValue()) != null) {
                    return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.sales.already.exist")));
                }
            }
            salesService.register(sales);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.sales.registered")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "売上情報を更新する", description = "指定された売上データを更新します。")
    @PutMapping("/{salesNumber}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.売上更新, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> update(@PathVariable String salesNumber, @RequestBody SalesResource resource) {
        try {
            resource.setSalesNumber(salesNumber);
            Sales sales = SalesResourceDTOMapper.convertToEntity(resource);
            if (salesService.find(salesNumber) == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.sales.not.exist")));
            }
            salesService.save(sales);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.sales.updated")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "売上を削除する", description = "指定された売上データを削除します。")
    @DeleteMapping("/{salesNumber}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.売上削除, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> delete(@PathVariable String salesNumber) {
        try {
            Sales sales = salesService.find(salesNumber);
            if (sales == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.sales.not.exist")));
            }
            salesService.delete(sales);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.sales.deleted")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "売上を検索する", description = "指定された検索条件で売上データを検索します。")
    @PostMapping("/search")
    public ResponseEntity<?> search(
            @RequestBody SalesCriteriaResource resource,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            SalesCriteria criteria = SalesResourceDTOMapper.convertToCriteria(resource);
            PageInfo<Sales> entity = salesService.searchWithPageInfo(criteria);
            PageInfo<SalesResource> result = pageNationService.getPageInfo(entity, SalesResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "売上を集計する", description = "売上を集計します。")
    @PostMapping("/aggregate")
    @AuditAnnotation(process = ApplicationExecutionProcessType.売上集計, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> aggregate(@RequestBody SalesResource resource) {
        try {
            salesService.aggregate();
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.sales.aggregated")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
