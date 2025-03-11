package com.example.sms.presentation.api.master.partner;

import com.example.sms.domain.model.master.partner.Partner;
import com.example.sms.domain.model.master.partner.TradeProhibitedFlag;
import com.example.sms.domain.model.master.partner.customer.*;
import com.example.sms.domain.model.master.partner.invoice.ClosingInvoice;
import com.example.sms.domain.model.master.partner.invoice.Invoice;
import com.example.sms.domain.model.master.partner.vendor.Vendor;
import com.example.sms.domain.model.master.partner.vendor.VendorCode;
import com.example.sms.domain.model.master.partner.vendor.VendorName;
import com.example.sms.domain.model.master.partner.vendor.VendorType;
import com.example.sms.domain.model.master.partner.MiscellaneousType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessType;
import com.example.sms.domain.type.address.Address;
import com.example.sms.domain.type.mail.EmailAddress;
import com.example.sms.domain.type.phone.FaxNumber;
import com.example.sms.domain.type.phone.PhoneNumber;
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

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

    private Partner convertToEntity(PartnerResource resource) {
        // Partner の基本プロパティを設定
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

        // Customers リストを変換 (null 対応とマッピング処理)
        List<Customer> customers = Optional.ofNullable(resource.getCustomers())
                .orElse(Collections.emptyList())  // null の場合は空リスト
                .stream()
                .filter(Objects::nonNull)         // null エントリーを除外
                .map(this::convertToCustomer)     // 変換ロジック (別途実装)
                .toList();

        // Vendors リストを変換 (null 対応とマッピング処理)
        List<Vendor> vendors = Optional.ofNullable(resource.getVendors())
                .orElse(Collections.emptyList())  // null の場合は空リスト
                .stream()
                .filter(Objects::nonNull)         // null エントリーを除外
                .map(this::convertToVendor)       // 変換ロジック (別途実装)
                .toList();

        // Partner の拡張プロパティを設定
        partner = Partner.of(
                partner.getPartnerCode(),
                partner.getPartnerName(),
                partner.getVendorType(),
                partner.getAddress(),
                partner.getTradeProhibitedFlag(),
                partner.getMiscellaneousType(),
                partner.getPartnerGroupCode(),
                partner.getCredit(),
                customers,  // 変換した Customers リスト
                vendors     // 変換した Vendors リスト
        );

        return partner;
    }

    // サブメソッド：Resource から Customer へ変換
    private Customer convertToCustomer(CustomerResource resource) {
        // Invoiceオブジェクトの生成（ClosingInvoiceを2つ含む）
        Invoice invoice = Invoice.of(
                resource.getCustomerBillingType(),
                ClosingInvoice.of(
                        resource.getCustomerClosingDay1().getValue(),
                        resource.getCustomerPaymentMonth1().getValue(),
                        resource.getCustomerPaymentDay1().getValue(),
                        resource.getCustomerPaymentMethod1().getValue()
                ),
                ClosingInvoice.of(
                        resource.getCustomerClosingDay2().getValue(),
                        resource.getCustomerPaymentMonth2().getValue(),
                        resource.getCustomerPaymentDay2().getValue(),
                        resource.getCustomerPaymentMethod2().getValue()
                )
        );

        // Customerオブジェクトの生成
        return Customer.of(
                CustomerCode.of(resource.getCustomerCode(), resource.getCustomerBranchNumber()),
                resource.getCustomerType(),
                BillingCode.of(resource.getBillingCode(), resource.getBillingBranchNumber()),
                CollectionCode.of(resource.getCollectionCode(), resource.getCollectionBranchNumber()),
                CustomerName.of(resource.getCustomerName(), resource.getCustomerNameKana()),
                resource.getCompanyRepresentativeCode(),
                resource.getCustomerRepresentativeName(),
                resource.getCustomerDepartmentName(),
                Address.of(
                        resource.getCustomerPostalCode(),
                        resource.getCustomerPrefecture(),
                        resource.getCustomerAddress1(),
                        resource.getCustomerAddress2()
                ),
                PhoneNumber.of(resource.getCustomerPhoneNumber()),
                FaxNumber.of(resource.getCustomerFaxNumber()),
                EmailAddress.of(resource.getCustomerEmailAddress()),
                invoice,
                Optional.ofNullable(resource.getShippings())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(this::convertToShipping)
                        .toList()
        );
    }

    private Shipping convertToShipping(ShippingResource shippingResource) {
        return Shipping.of(
                shippingResource.getShippingCode(),
                shippingResource.getDestinationName(),
                shippingResource.getRegionCode(),
                Address.of(
                        shippingResource.getShippingAddress().getPostalCode().getValue(),
                        shippingResource.getShippingAddress().getPrefecture().name(),
                        shippingResource.getShippingAddress().getAddress1(),
                        shippingResource.getShippingAddress().getAddress2()
                )
        );
    }

    // サブメソッド：Resource から Vendor に変換
    private Vendor convertToVendor(VendorResource resource) {
        return Vendor.of(
                VendorCode.of(resource.getVendorCode(), resource.getVendorBranchNumber()),
                VendorName.of(resource.getVendorName(), resource.getVendorNameKana()),
                resource.getVendorContactName(),
                resource.getVendorDepartmentName(),
                Address.of(
                        resource.getVendorPostalCode(),
                        resource.getVendorPrefecture(),
                        resource.getVendorAddress1(),
                        resource.getVendorAddress2()
                ),
                PhoneNumber.of(resource.getVendorPhoneNumber()),
                FaxNumber.of(resource.getVendorFaxNumber()),
                EmailAddress.of(resource.getVendorEmailAddress()),
                ClosingInvoice.of(
                        resource.getVendorClosingDate().getValue(),
                        resource.getVendorPaymentMonth().getValue(),
                        resource.getVendorPaymentDate().getValue(),
                        resource.getVendorPaymentMethod().getValue()
                )
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
