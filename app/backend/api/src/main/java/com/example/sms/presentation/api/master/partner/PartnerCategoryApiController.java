package com.example.sms.presentation.api.master.partner;

import com.example.sms.domain.model.master.partner.PartnerCategoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessType;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.BusinessException;
import com.example.sms.service.PageNationService;
import com.example.sms.service.master.partner.PartnerCategoryCriteria;
import com.example.sms.service.master.partner.PartnerCategoryService;
import com.example.sms.service.system.audit.AuditAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.example.sms.presentation.api.master.partner.PartnerCategoryResourceDTOMapper.convertToCriteria;
import static com.example.sms.presentation.api.master.partner.PartnerCategoryResourceDTOMapper.convertToEntity;

/**
 * 取引先分類API
 */
@RestController
@RequestMapping("/api/partner-categories")
@Tag(name = "PartnerCategory", description = "取引先分類種別")
@PreAuthorize("hasRole('ADMIN')")
public class PartnerCategoryApiController {
    final PartnerCategoryService partnerCategoryService;
    final PageNationService pageNationService;
    final Message message;

    public PartnerCategoryApiController(PartnerCategoryService partnerCategoryService, PageNationService pageNationService, Message message) {
        this.partnerCategoryService = partnerCategoryService;
        this.pageNationService = pageNationService;
        this.message = message;
    }

    @Operation(summary = "取引先分類種別一覧を取得する")
    @GetMapping
    public ResponseEntity<?> select(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PageInfo<PartnerCategoryType> pageInfo = partnerCategoryService.selectAllWithPageInfo();
            PageInfo<PartnerCategoryTypeResource> result = pageNationService.getPageInfo(pageInfo, PartnerCategoryTypeResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "取引先分類種別を取得する")
    @GetMapping("/{partnerCategoryTypeCode}")
    public ResponseEntity<?> select(@PathVariable("partnerCategoryTypeCode") String partnerCategoryTypeCode) {
        try {
            PartnerCategoryType result = partnerCategoryService.find(partnerCategoryTypeCode);
            return ResponseEntity.ok(PartnerCategoryTypeResource.from(result));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "取引先分類種別を登録する")
    @PostMapping
    @AuditAnnotation(process = ApplicationExecutionProcessType.取引先分類種別登録, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> register(@RequestBody PartnerCategoryTypeResource partnerCategoryResource) {
        try {
            PartnerCategoryType partnerCategoryType = convertToEntity(partnerCategoryResource);

            if (partnerCategoryService.find(partnerCategoryType.getPartnerCategoryTypeCode()) != null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.partner-category.already.exist")));
            }

            partnerCategoryService.register(partnerCategoryType);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.partner-category.registered")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "取引先分類種別を更新する")
    @PutMapping("/{partnerCategoryTypeCode}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.取引先分類種別更新, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> update(
            @PathVariable("partnerCategoryTypeCode") String partnerCategoryTypeCode,
            @RequestBody PartnerCategoryTypeResource partnerCategoryResource) {
        try {
            PartnerCategoryType partnerCategoryType = convertToEntity(partnerCategoryResource);

            if (partnerCategoryService.find(partnerCategoryType.getPartnerCategoryTypeCode()) == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.partner-category.not.exist")));
            }

            partnerCategoryService.save(partnerCategoryType);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.partner-category.updated")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "取引先分類種別を削除する")
    @DeleteMapping("/{partnerCategoryTypeCode}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.取引先分類種別削除, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> delete(@PathVariable("partnerCategoryTypeCode") String partnerCategoryTypeCode) {
        try {
            PartnerCategoryType partnerCategoryType = partnerCategoryService.find(partnerCategoryTypeCode);
            if (partnerCategoryType == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.partner-category.not.exist")));
            }

            partnerCategoryService.delete(partnerCategoryType);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.partner-category.deleted")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "取引先分類種別を検索する")
    @PostMapping("/search")
    public ResponseEntity<?> search(
            @RequestBody PartnerCategoryCriteriaResource resource,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PartnerCategoryCriteria criteria = convertToCriteria(resource);
            PageInfo<PartnerCategoryType> entity = partnerCategoryService.searchWithPageInfo(criteria);
            PageInfo<PartnerCategoryTypeResource> result = pageNationService.getPageInfo(entity, PartnerCategoryTypeResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
