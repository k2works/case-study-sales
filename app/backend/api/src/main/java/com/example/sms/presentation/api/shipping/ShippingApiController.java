package com.example.sms.presentation.api.shipping;

import com.example.sms.domain.model.shipping.Shipping;
import com.example.sms.domain.model.shipping.ShippingList;
import com.example.sms.domain.model.shipping.rule.ShippingRuleCheckList;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessType;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponseWithDetail;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.BusinessException;
import com.example.sms.service.PageNationService;
import com.example.sms.service.shipping.ShippingCriteria;
import com.example.sms.service.shipping.ShippingService;
import com.example.sms.service.system.audit.AuditAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * 出荷API
 */
@RestController
@RequestMapping("/api/shipping")
@Tag(name = "Shipping", description = "出荷管理")
public class ShippingApiController {
    final ShippingService shippingService;
    final PageNationService pageNationService;
    final Message message;

    public ShippingApiController(ShippingService shippingService, PageNationService pageNationService, Message message) {
        this.shippingService = shippingService;
        this.pageNationService = pageNationService;
        this.message = message;
    }

    @Operation(summary = "出荷一覧を取得する", description = "全ての出荷データをページ情報付きで取得します。")
    @GetMapping
    public ResponseEntity<?> selectAll(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PageInfo<Shipping> pageInfo = shippingService.selectAllWithPageInfo();
            PageInfo<ShippingResource> result = pageNationService.getPageInfo(pageInfo, ShippingResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "出荷情報を取得する", description = "指定された受注番号に基づいて出荷データを取得します。")
    @GetMapping("/{orderNumber}/{orderLineNumber}")
    public ResponseEntity<?> select(@PathVariable String orderNumber, @PathVariable String orderLineNumber) {
        try {
            Optional<Shipping> entity = shippingService.findById(orderNumber, orderLineNumber);
            if (entity.isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.shipping.not.exist")));
            }
            ShippingResource result = ShippingResource.from(entity.get());
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "出荷情報を保存する", description = "出荷データを保存します。")
    @PostMapping
    @AuditAnnotation(process = ApplicationExecutionProcessType.出荷更新, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> save(@RequestBody ShippingResource resource) {
        try {
            Shipping shipping = ShippingResourceDTOMapper.convertToEntity(resource);
            shippingService.save(shipping);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.shipping.saved")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "出荷を検索する", description = "指定された検索条件で出荷データを検索します。")
    @PostMapping("/search")
    public ResponseEntity<?> search(
            @RequestBody ShippingCriteriaResource resource,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            ShippingCriteria criteria = ShippingResourceDTOMapper.convertToCriteria(resource);
            PageInfo<Shipping> entity = shippingService.searchWithPageInfo(criteria);
            PageInfo<ShippingResource> result = pageNationService.getPageInfo(entity, ShippingResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "出荷を指示する", description = "指定された検索条件で出荷指示をします。")
    @PostMapping("/order-shipping")
    @AuditAnnotation(process = ApplicationExecutionProcessType.出荷登録, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> orderShipping(
            @RequestBody ShippingCriteriaResource resource )
    {
        try {
            ShippingCriteria criteria = ShippingResourceDTOMapper.convertToCriteria(resource);
            ShippingList shippingList = shippingService.search(criteria);
            shippingService.orderShipping(shippingList);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.shipping-order.exec")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "出荷を確認する", description = "指定された検索条件で出荷確認をします。")
    @PostMapping("/confirm-shipping")
    @AuditAnnotation(process = ApplicationExecutionProcessType.出荷登録, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> confirmShipping(
            @RequestBody ShippingCriteriaResource resource )
    {
        try {
            ShippingCriteria criteria = ShippingResourceDTOMapper.convertToCriteria(resource);
            ShippingList shippingList = shippingService.search(criteria);
            shippingService.confirmShipping(shippingList);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.shipping-confirm.exec")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "出荷ルールを確認する", description = "出荷ルール確認を実行する")
    @PostMapping("/check")
    @AuditAnnotation(process = ApplicationExecutionProcessType.その他, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> check() {
        try {
            ShippingRuleCheckList result = shippingService.checkRule();
            if (result.isEmpty()) {
                return ResponseEntity.ok(new MessageResponseWithDetail(message.getMessage("success.shipping.check"), result.asList()));
            }
            return ResponseEntity.ok(new MessageResponseWithDetail(message.getMessage("error.shipping.check"), result.asList()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
