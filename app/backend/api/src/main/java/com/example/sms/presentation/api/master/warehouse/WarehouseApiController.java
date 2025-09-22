package com.example.sms.presentation.api.master.warehouse;

import com.example.sms.domain.model.master.warehouse.Warehouse;
import com.example.sms.domain.model.master.warehouse.WarehouseCode;
import com.example.sms.domain.model.master.warehouse.WarehouseList;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessType;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.BusinessException;
import com.example.sms.service.PageNationService;
import com.example.sms.service.master.warehouse.WarehouseCriteria;
import com.example.sms.service.master.warehouse.WarehouseService;
import com.example.sms.service.system.audit.AuditAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.example.sms.presentation.api.master.warehouse.WarehouseResourceDTOMapper.convertToCriteria;
import static com.example.sms.presentation.api.master.warehouse.WarehouseResourceDTOMapper.convertToEntity;

/**
 * 倉庫API
 */
@RestController
@RequestMapping("/api/warehouses")
@Tag(name = "Warehouse", description = "倉庫")
@PreAuthorize("hasRole('ADMIN')")
public class WarehouseApiController {
    final WarehouseService warehouseService;
    final PageNationService pageNationService;
    final Message message;

    public WarehouseApiController(WarehouseService warehouseService, PageNationService pageNationService, Message message) {
        this.warehouseService = warehouseService;
        this.pageNationService = pageNationService;
        this.message = message;
    }

    @Operation(summary = "倉庫一覧を取得する", description = "倉庫一覧を取得する")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> select(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PageInfo<Warehouse> pageInfo = warehouseService.selectAllWithPageInfo();
            PageInfo<WarehouseResource> result = pageNationService.getPageInfo(pageInfo, WarehouseResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "倉庫を取得する", description = "倉庫を取得する")
    @GetMapping("/{warehouseCode}")
    public ResponseEntity<?> find(@PathVariable String warehouseCode) {
        try {
            WarehouseCode code = WarehouseCode.of(warehouseCode);
            Warehouse warehouse = warehouseService.find(code);
            if (warehouse == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.warehouse.not.exist")));
            }
            return ResponseEntity.ok(WarehouseResource.from(warehouse));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "倉庫を登録する", description = "倉庫を登録する")
    @PostMapping
    @AuditAnnotation(process = ApplicationExecutionProcessType.倉庫登録, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> create(@RequestBody @Validated WarehouseResource resource) {
        try {
            Warehouse warehouse = convertToEntity(resource);
            Warehouse existingWarehouse = warehouseService.find(warehouse.getWarehouseCode());
            if (existingWarehouse != null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.warehouse.already.exist")));
            }
            warehouseService.register(warehouse);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.warehouse.registered")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "倉庫を更新する", description = "倉庫を更新する")
    @PutMapping("/{warehouseCode}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.倉庫更新, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> update(@PathVariable String warehouseCode, @RequestBody @Validated WarehouseResource warehouseResource) {
        try {
            warehouseResource.setWarehouseCode(warehouseCode);
            Warehouse warehouse = convertToEntity(warehouseResource);
            warehouseService.save(warehouse);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.warehouse.updated")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "倉庫を削除する", description = "倉庫を削除する")
    @DeleteMapping("/{warehouseCode}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.倉庫削除, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> delete(@PathVariable String warehouseCode) {
        try {
            WarehouseCode code = WarehouseCode.of(warehouseCode);
            Warehouse warehouse = warehouseService.find(code);
            if (warehouse == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.warehouse.not.exist")));
            }
            warehouseService.delete(code);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.warehouse.deleted")));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "倉庫を検索する", description = "倉庫を検索する")
    @PostMapping("/search")
    public ResponseEntity<?> search(
            @RequestBody WarehouseCriteriaResource resource,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            WarehouseCriteria criteria = convertToCriteria(resource);
            PageInfo<Warehouse> entity = warehouseService.searchWithPageInfo(criteria);
            PageInfo<WarehouseResource> result = pageNationService.getPageInfo(entity, WarehouseResource::from);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}