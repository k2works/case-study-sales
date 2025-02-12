package com.example.sms.presentation.api.master.region;

import com.example.sms.domain.model.master.region.Region;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessType;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.BusinessException;
import com.example.sms.service.master.region.RegionCriteria;
import com.example.sms.service.master.region.RegionService;
import com.example.sms.service.system.audit.AuditAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 地域コードAPI
 */
@RestController
@RequestMapping("/api/regions")
@Tag(name = "Region", description = "地域管理")
@PreAuthorize("hasRole('ADMIN')")
public class RegionApiController {
    final RegionService regionService;
    final Message message;

    public RegionApiController(RegionService regionService, Message message) {
        this.regionService = regionService;
        this.message = message;
    }

    @Operation(summary = "地域一覧を取得")
    @GetMapping
    public ResponseEntity<?> selectAll(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PageInfo<Region> result = regionService.selectAllWithPageInfo();
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "地域情報を取得")
    @GetMapping("/{regionCode}")
    public ResponseEntity<?> select(@PathVariable("regionCode") String regionCode) {
        try {
            Region result = regionService.find(regionCode);
            if (result == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.region.not.exist")));
            }
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "地域を登録")
    @PostMapping
    @AuditAnnotation(process = ApplicationExecutionProcessType.地域登録, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> register(@RequestBody RegionResource resource) {
        try {
            Region region = convertToEntity(resource);
            if (regionService.find(region.getRegionCode().getValue()) != null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.region.already.exist")));
            }
            regionService.register(region);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.region.registered")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "地域情報を更新")
    @PutMapping("/{regionCode}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.地域更新, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> update(
            @PathVariable("regionCode") String regionCode,
            @RequestBody RegionResource resource) {
        try {
            resource.setRegionCode(regionCode);
            Region region = convertToEntity(resource);
            if (regionService.find(region.getRegionCode().getValue()) == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.region.not.exist")));
            }
            regionService.save(region);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.region.updated")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "地域を削除")
    @DeleteMapping("/{regionCode}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.地域削除, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> delete(@PathVariable("regionCode") String regionCode) {
        try {
            Region region = regionService.find(regionCode);
            if (region == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.region.not.exist")));
            }
            regionService.delete(region);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.region.deleted")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "地域情報を検索")
    @PostMapping("/search")
    public ResponseEntity<?> search(
            @RequestBody RegionCriteriaResource resource,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            RegionCriteria criteria = convertToCriteria(resource);
            PageInfo<Region> result = regionService.searchWithPageInfo(criteria);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    private Region convertToEntity(RegionResource resource) {
        return Region.of(resource.getRegionCode(), resource.getRegionName());
    }

    private RegionCriteria convertToCriteria(RegionCriteriaResource resource) {
        return RegionCriteria.builder()
                .regionCode(resource.getRegionCode())
                .regionName(resource.getRegionName())
                .build();
    }
}