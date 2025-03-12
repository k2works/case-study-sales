package com.example.sms.presentation.api.master.partner;

import com.example.sms.domain.model.master.partner.Partner;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessType;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.BusinessException;
import com.example.sms.service.PageNationService;
import com.example.sms.service.master.partner.PartnerCriteria;
import com.example.sms.service.master.partner.PartnerService;
import com.example.sms.service.system.audit.AuditAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.example.sms.presentation.api.master.partner.PartnerResourceDTOMapper.convertToCriteria;
import static com.example.sms.presentation.api.master.partner.PartnerResourceDTOMapper.convertToEntity;

/**
 * 取引先 API
 */
@RestController
@RequestMapping("/api/partners")
@Tag(name = "Partner", description = "取引先管理")
@PreAuthorize("hasRole('ADMIN')")
public class PartnerApiController {
    private final PartnerService partnerService;
    private final PageNationService pageNationService;
    private final Message message;

    public PartnerApiController(PartnerService partnerService, PageNationService pageNationService, Message message) {
        this.partnerService = partnerService;
        this.pageNationService = pageNationService;
        this.message = message;
    }

    @Operation(summary = "取引先一覧を取得する")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> select(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PageInfo<Partner> pageInfo = partnerService.selectAllWithPageInfo();
            PageInfo<PartnerResource> result = pageNationService.getPageInfo(pageInfo, PartnerResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "取引先を取得する")
    @GetMapping("/{partnerCode}")
    public ResponseEntity<?> select(@PathVariable("partnerCode") String partnerCode) {
        try {
            Partner result = partnerService.find(partnerCode);
            if (result == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.partner.not.exist")));
            }
            return ResponseEntity.ok(PartnerResource.from(result));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "取引先を登録する")
    @PostMapping
    @AuditAnnotation(process = ApplicationExecutionProcessType.取引先登録, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> register(@RequestBody PartnerResource partnerResource) {
        try {
            Partner partner = convertToEntity(partnerResource);
            if (partnerService.find(partner.getPartnerCode().getValue()) != null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.partner.already.exist")));
            }
            partnerService.register(partner);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.partner.registered")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "取引先を更新する")
    @PutMapping("/{partnerCode}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.取引先更新, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> update(
            @PathVariable("partnerCode") String partnerCode,
            @RequestBody PartnerResource partnerResource) {
        try {
            Partner partner = convertToEntity(partnerResource);
            if (partnerService.find(partnerCode) == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.partner.not.exist")));
            }
            partnerService.save(partner);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.partner.updated")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "取引先を削除する")
    @DeleteMapping("/{partnerCode}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.取引先削除, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> delete(@PathVariable("partnerCode") String partnerCode) {
        try {
            Partner partner = partnerService.find(partnerCode);
            if (partner == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.partner.not.exist")));
            }
            partnerService.delete(partner);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.partner.deleted")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "取引先を検索する")
    @PostMapping("/search")
    public ResponseEntity<?> search(
            @RequestBody PartnerCriteriaResource criteriaResource,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PartnerCriteria criteria = convertToCriteria(criteriaResource);
            PageInfo<Partner> entity = partnerService.searchWithPageInfo(criteria);
            PageInfo<PartnerResource> result = pageNationService.getPageInfo(entity, PartnerResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

}
