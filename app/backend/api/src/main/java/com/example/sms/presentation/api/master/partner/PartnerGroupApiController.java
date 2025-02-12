package com.example.sms.presentation.api.master.partner;

import com.example.sms.domain.model.master.partner.PartnerGroup;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessType;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.BusinessException;
import com.example.sms.service.master.partner.PartnerGroupCriteria;
import com.example.sms.service.master.partner.PartnerGroupService;
import com.example.sms.service.system.audit.AuditAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 取引先グループAPI
 */
@RestController
@RequestMapping("/api/partner-groups")
@Tag(name = "PartnerGroup", description = "取引先グループ")
@PreAuthorize("hasRole('ADMIN')")
public class PartnerGroupApiController {
    final PartnerGroupService partnerGroupService;
    final Message message;

    public PartnerGroupApiController(PartnerGroupService partnerGroupService, Message message) {
        this.partnerGroupService = partnerGroupService;
        this.message = message;
    }

    @Operation(summary = "取引先グループ一覧を取得する")
    @GetMapping
    public ResponseEntity<?> select(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PageInfo<PartnerGroup> result = partnerGroupService.selectAllWithPageInfo();
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "取引先グループ一覧を取得する")
    @GetMapping("/{partnerGroupCode}")
    public ResponseEntity<?> select(@PathVariable("partnerGroupCode") String partnerGroupCode) {
        try {
            PartnerGroup result = partnerGroupService.find(partnerGroupCode);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "取引先グループを登録する")
    @PostMapping
    @AuditAnnotation(process = ApplicationExecutionProcessType.取引先グループ登録, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> register(@RequestBody PartnerGroupResource partnerGroupResource) {
        try {
            PartnerGroup partnerGroup = convertToEntity(partnerGroupResource);
            if (partnerGroupService.find(partnerGroup.getPartnerGroupCode().getValue()) != null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.partner-group.already.exist")));
            }
            partnerGroupService.register(partnerGroup);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.partner-group.registered")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "取引先グループを更新する")
    @PutMapping("/{partnerGroupCode}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.取引先グループ更新, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> update(@PathVariable("partnerGroupCode") String partnerGroupCode, @RequestBody PartnerGroupResource partnerGroupResource) {
        try {
            PartnerGroup partnerGroup = convertToEntity(partnerGroupResource);
            if (partnerGroupService.find(partnerGroup.getPartnerGroupCode().getValue()) == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.partner-group.not.exist")));
            }
            partnerGroupService.save(partnerGroup);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.partner-group.updated")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "取引先グループを削除する")
    @DeleteMapping("/{partnerGroupCode}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.取引先グループ削除, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> delete(@PathVariable("partnerGroupCode") String partnerGroupCode) {
        try {
            PartnerGroup partnerGroup = partnerGroupService.find(partnerGroupCode);
            if (partnerGroup == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.partner-group.not.exist")));
            }
            partnerGroupService.delete(partnerGroup);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.partner-group.deleted")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "取引先グループを検索する")
    @PostMapping("/search")
    public ResponseEntity<?> search(
            @RequestBody PartnerGroupCriteriaResource resource,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);

            PartnerGroupCriteria criteria = convertToCriteria(resource);
            PageInfo<PartnerGroup> result = partnerGroupService.searchWithPageInfo(criteria);

            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    private PartnerGroup convertToEntity(PartnerGroupResource resource) {
        return PartnerGroup.of(resource.getPartnerGroupCode(), resource.getPartnerGroupName());
    }

    private PartnerGroupCriteria convertToCriteria(PartnerGroupCriteriaResource resource) {
        return PartnerGroupCriteria.builder()
                .partnerGroupCode(resource.getPartnerGroupCode())
                .partnerGroupName(resource.getPartnerGroupName())
                .build();
    }
}

