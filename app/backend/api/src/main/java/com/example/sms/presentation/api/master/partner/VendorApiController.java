package com.example.sms.presentation.api.master.partner;

import com.example.sms.domain.model.master.partner.vendor.Vendor;
import com.example.sms.domain.model.master.partner.vendor.VendorCode;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessType;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.BusinessException;
import com.example.sms.service.PageNationService;
import com.example.sms.service.master.partner.VendorCriteria;
import com.example.sms.service.master.partner.VendorService;
import com.example.sms.service.system.audit.AuditAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 仕入先 API
 */
@RestController
@RequestMapping("/api/vendors")
@Tag(name = "Vendor", description = "仕入先管理")
@PreAuthorize("hasRole('ADMIN')")
public class VendorApiController {
    private final VendorService vendorService;
    private final PageNationService pageNationService;
    private final Message message;

    public VendorApiController(VendorService vendorService, PageNationService pageNationService, Message message) {
        this.vendorService = vendorService;
        this.pageNationService = pageNationService;
        this.message = message;
    }

    @Operation(summary = "仕入先一覧を取得する")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> select(@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                    @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PageInfo<Vendor> pageInfo = vendorService.selectAllWithPageInfo();
            PageInfo<VendorResource> result = pageNationService.getPageInfo(pageInfo, VendorResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "仕入先を取得する")
    @GetMapping("/{vendorCode}/{vendorBranchCode}")
    public ResponseEntity<?> select(@PathVariable("vendorCode") String vendorCode,
                                    @PathVariable("vendorBranchCode") String vendorBranchCode) {
        try {
            Vendor result = vendorService.find(VendorCode.of(vendorCode, Integer.valueOf(vendorBranchCode)));
            if (result == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.vendor.not.exist")));
            }
            return ResponseEntity.ok(VendorResource.from(result));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "仕入先を登録する")
    @PostMapping
    @AuditAnnotation(process = ApplicationExecutionProcessType.仕入先登録, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> register(@RequestBody VendorResource vendorResource) {
        try {
            Vendor vendor = convertToEntity(vendorResource);
            if (vendorService.find(vendor.getVendorCode()) != null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.vendor.already.exist")));
            }
            vendorService.register(vendor);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.vendor.registered")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "仕入先を更新する")
    @PutMapping("/{vendorCode}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.仕入先更新, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> update(@PathVariable("vendorCode") String vendorCode,
                                   @RequestBody VendorResource vendorResource) {
        try {
            Vendor vendor = convertToEntity(vendorResource);
            if (vendorService.find(vendor.getVendorCode()) == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.vendor.not.exist")));
            }
            vendorService.save(vendor);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.vendor.updated")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "仕入先を削除する")
    @DeleteMapping("/{vendorCode}/{vendorBranchCode}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.仕入先削除, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> delete(@PathVariable String vendorCode, @PathVariable String vendorBranchCode) {
        try {
            Vendor vendor = vendorService.find(VendorCode.of(vendorCode, Integer.valueOf(vendorBranchCode)));
            if (vendor == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.vendor.not.exist")));
            }
            vendorService.delete(vendor);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.vendor.deleted")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "仕入先を検索する")
    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody VendorCriteriaResource criteriaResource,
                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                   @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            VendorCriteria criteria = convertToCriteria(criteriaResource);
            PageInfo<Vendor> entity = vendorService.searchWithPageInfo(criteria);
            PageInfo<VendorResource> result = pageNationService.getPageInfo(entity, VendorResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    private Vendor convertToEntity(VendorResource resource) {
        return Vendor.of(
                resource.getVendorCode(),
                resource.getVendorBranchNumber(),
                resource.getVendorName(),
                resource.getVendorNameKana(),
                resource.getVendorContactName(),
                resource.getVendorDepartmentName(),
                resource.getVendorPostalCode(),
                resource.getVendorPrefecture(),
                resource.getVendorAddress1(),
                resource.getVendorAddress2(),
                resource.getVendorPhoneNumber(),
                resource.getVendorFaxNumber(),
                resource.getVendorEmailAddress(),
                resource.getVendorClosingDate().getValue(),
                resource.getVendorPaymentMonth().getValue(),
                resource.getVendorPaymentDate().getValue(),
                resource.getVendorPaymentMethod().getValue()
        );
    }

    private VendorCriteria convertToCriteria(VendorCriteriaResource resource) {
        return VendorCriteria.builder()
                .vendorCode(resource.getVendorCode())
                .vendorName(resource.getVendorName())
                .vendorContactName(resource.getVendorContactName())
                .vendorDepartmentName(resource.getVendorDepartmentName())
                .postalCode(resource.getPostalCode())
                .prefecture(resource.getPrefecture())
                .address1(resource.getAddress1())
                .address2(resource.getAddress2())
                .vendorPhoneNumber(resource.getVendorPhoneNumber())
                .vendorFaxNumber(resource.getVendorFaxNumber())
                .vendorEmailAddress(resource.getVendorEmailAddress())
                .build();
    }
}