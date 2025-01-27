package com.example.sms.presentation.api.master.partner;

import com.example.sms.domain.model.master.partner.Partner;
import com.example.sms.domain.model.master.partner.TradeProhibitedFlag;
import com.example.sms.domain.model.master.partner.vendor.VendorType;
import com.example.sms.domain.model.master.partner.MiscellaneousType;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.master.partner.PartnerCriteria;
import com.example.sms.service.master.partner.PartnerService;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

/**
 * 取引先 API
 */
@RestController
@RequestMapping("/api/partners")
@Tag(name = "Partner", description = "取引先管理")
@PreAuthorize("hasRole('ADMIN')")
public class PartnerApiController {

    private final PartnerService partnerService;
    private final Message message;

    public PartnerApiController(PartnerService partnerService, Message message) {
        this.partnerService = partnerService;
        this.message = message;
    }

    @Operation(summary = "取引先一覧を取得する")
    @GetMapping
    public ResponseEntity<?> select(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PageInfo<Partner> result = partnerService.selectAllWithPageInfo();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
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
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    //TODO:監査アノテーション追加
    @Operation(summary = "取引先を登録する")
    @PostMapping
    public ResponseEntity<?> register(@RequestBody PartnerResource partnerResource) {
        try {
            Partner partner = convertToEntity(partnerResource);
            if (partnerService.find(partner.getPartnerCode().getValue()) != null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.partner.already.exist")));
            }
            partnerService.register(partner);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.partner.registered")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "取引先を更新する")
    @PutMapping("/{partnerCode}")
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
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "取引先を削除する")
    @DeleteMapping("/{partnerCode}")
    public ResponseEntity<?> delete(@PathVariable("partnerCode") String partnerCode) {
        try {
            Partner partner = partnerService.find(partnerCode);
            if (partner == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.partner.not.exist")));
            }
            partnerService.delete(partner);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.partner.deleted")));
        } catch (Exception e) {
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
            PageInfo<Partner> result = partnerService.searchWithPageInfo(criteria);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    private Partner convertToEntity(PartnerResource resource) {
        Partner partner = Partner.of(
                resource.getPartnerCode(),
                resource.getPartnerName(),
                resource.getPartnerNameKana(),
                resource.getVendorType().getValue(),
                resource.getPostalCode(),
                resource.getPrefecture(),
                resource.getAddress1(),
                resource.getAddress2(),
                resource.getTradeProhibitedFlag().getValue(),
                resource.getMiscellaneousType().getCode(),
                resource.getPartnerGroupCode(),
                resource.getCreditLimit(),
                resource.getTemporaryCreditIncrease()
        );

        return Partner.of(
                partner.getPartnerCode(),
                partner.getPartnerName(),
                partner.getVendorType(),
                partner.getAddress(),
                partner.getTradeProhibitedFlag(),
                partner.getMiscellaneousType(),
                partner.getPartnerGroupCode(),
                partner.getCredit(),
                resource.getCustomers(),
                resource.getVendors()
        );
    }

    private PartnerCriteria convertToCriteria(PartnerCriteriaResource resource) {
        return PartnerCriteria.builder()
                .partnerCode(resource.getPartnerCode())
                .partnerName(resource.getPartnerName())
                .partnerNameKana(resource.getPartnerNameKana())
                .vendorType(mapStringToCode(resource.getVendorType(), VendorType::getCodeByName))
                .postalCode(resource.getPostalCode())
                .prefecture(resource.getPrefecture())
                .address1(resource.getAddress1())
                .address2(resource.getAddress2())
                .tradeProhibitedFlag(mapStringToCode(resource.getTradeProhibitedFlag(), TradeProhibitedFlag::getCodeByName))
                .miscellaneousType(mapStringToCode(resource.getMiscellaneousType(), MiscellaneousType::getCodeByName))
                .partnerGroupCode(resource.getPartnerGroupCode())
                .creditLimit(resource.getCreditLimit())
                .temporaryCreditIncrease(resource.getTemporaryCreditIncrease())
                .build();
    }

    private <T> T mapStringToCode(String value, Function<String, T> mapper) {
        return value != null ? mapper.apply(value) : null;
    }
}