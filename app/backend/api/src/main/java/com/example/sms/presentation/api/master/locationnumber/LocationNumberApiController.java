package com.example.sms.presentation.api.master.locationnumber;

import com.example.sms.domain.model.master.locationnumber.LocationNumber;
import com.example.sms.domain.model.master.locationnumber.LocationNumberList;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessType;
import com.example.sms.infrastructure.datasource.autogen.model.棚番マスタKey;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.BusinessException;
import com.example.sms.service.PageNationService;
import com.example.sms.service.master.locationnumber.LocationNumberCriteria;
import com.example.sms.service.master.locationnumber.LocationNumberService;
import com.example.sms.service.system.audit.AuditAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.example.sms.presentation.api.master.locationnumber.LocationNumberResourceDTOMapper.convertToCriteria;
import static com.example.sms.presentation.api.master.locationnumber.LocationNumberResourceDTOMapper.convertToEntity;

/**
 * 棚番API
 */
@RestController
@RequestMapping("/api/locationnumbers")
@Tag(name = "LocationNumber", description = "棚番")
@PreAuthorize("hasRole('ADMIN')")
public class LocationNumberApiController {
    final LocationNumberService locationNumberService;
    final PageNationService pageNationService;
    final Message message;

    public LocationNumberApiController(LocationNumberService locationNumberService, PageNationService pageNationService, Message message) {
        this.locationNumberService = locationNumberService;
        this.pageNationService = pageNationService;
        this.message = message;
    }

    @Operation(summary = "棚番一覧を取得する", description = "棚番一覧を取得する")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> select(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PageInfo<LocationNumber> pageInfo = locationNumberService.selectAllWithPageInfo();
            PageInfo<LocationNumberResource> result = pageNationService.getPageInfo(pageInfo, LocationNumberResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "棚番を取得する", description = "棚番を取得する")
    @GetMapping("/{warehouseCode}/{locationNumberCode}/{productCode}")
    public ResponseEntity<?> find(
            @PathVariable String warehouseCode,
            @PathVariable String locationNumberCode,
            @PathVariable String productCode) {
        try {
            棚番マスタKey key = new 棚番マスタKey();
            key.set倉庫コード(warehouseCode);
            key.set棚番コード(locationNumberCode);
            key.set商品コード(productCode);

            LocationNumber locationNumber = locationNumberService.find(key);
            if (locationNumber == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.locationnumber.not.exist")));
            }
            return ResponseEntity.ok(LocationNumberResource.from(locationNumber));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "棚番を登録する", description = "棚番を登録する")
    @PostMapping
    @AuditAnnotation(process = ApplicationExecutionProcessType.棚番登録, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> create(@RequestBody @Validated LocationNumberResource resource) {
        try {
            LocationNumber locationNumber = convertToEntity(resource);

            棚番マスタKey key = new 棚番マスタKey();
            key.set倉庫コード(resource.getWarehouseCode());
            key.set棚番コード(resource.getLocationNumberCode());
            key.set商品コード(resource.getProductCode());

            LocationNumber existingLocationNumber = locationNumberService.find(key);
            if (existingLocationNumber != null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.locationnumber.already.exist")));
            }
            locationNumberService.register(locationNumber);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.locationnumber.registered")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "棚番を更新する", description = "棚番を更新する")
    @PutMapping("/{warehouseCode}/{locationNumberCode}/{productCode}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.棚番更新, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> update(
            @PathVariable String warehouseCode,
            @PathVariable String locationNumberCode,
            @PathVariable String productCode,
            @RequestBody @Validated LocationNumberResource locationNumberResource) {
        try {
            locationNumberResource.setWarehouseCode(warehouseCode);
            locationNumberResource.setLocationNumberCode(locationNumberCode);
            locationNumberResource.setProductCode(productCode);
            LocationNumber locationNumber = convertToEntity(locationNumberResource);
            locationNumberService.save(locationNumber);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.locationnumber.updated")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "棚番を削除する", description = "棚番を削除する")
    @DeleteMapping("/{warehouseCode}/{locationNumberCode}/{productCode}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.棚番削除, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> delete(
            @PathVariable String warehouseCode,
            @PathVariable String locationNumberCode,
            @PathVariable String productCode) {
        try {
            棚番マスタKey key = new 棚番マスタKey();
            key.set倉庫コード(warehouseCode);
            key.set棚番コード(locationNumberCode);
            key.set商品コード(productCode);

            LocationNumber locationNumber = locationNumberService.find(key);
            if (locationNumber == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.locationnumber.not.exist")));
            }
            locationNumberService.delete(key);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.locationnumber.deleted")));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "棚番を検索する", description = "棚番を検索する")
    @PostMapping("/search")
    public ResponseEntity<?> search(
            @RequestBody LocationNumberCriteriaResource resource,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            LocationNumberCriteria criteria = convertToCriteria(resource);
            PageInfo<LocationNumber> entity = locationNumberService.searchWithPageInfo(criteria);
            PageInfo<LocationNumberResource> result = pageNationService.getPageInfo(entity, LocationNumberResource::from);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "倉庫コードで棚番を検索する", description = "倉庫コードで棚番を検索する")
    @GetMapping("/by-warehouse/{warehouseCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> findByWarehouseCode(@PathVariable String warehouseCode) {
        try {
            LocationNumberList locationNumbers = locationNumberService.findByWarehouseCode(warehouseCode);
            return ResponseEntity.ok(locationNumbers.asList().stream()
                    .map(LocationNumberResource::from)
                    .toList());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "棚番コードで棚番を検索する", description = "棚番コードで棚番を検索する")
    @GetMapping("/by-location/{locationNumberCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> findByLocationNumberCode(@PathVariable String locationNumberCode) {
        try {
            LocationNumberList locationNumbers = locationNumberService.findByLocationNumberCode(locationNumberCode);
            return ResponseEntity.ok(locationNumbers.asList().stream()
                    .map(LocationNumberResource::from)
                    .toList());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}