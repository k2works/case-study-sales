package com.example.sms.presentation.api.master.product;

import com.example.sms.domain.model.master.product.*;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessType;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.BusinessException;
import com.example.sms.service.master.product.ProductCriteria;
import com.example.sms.service.master.product.ProductService;
import com.example.sms.service.system.audit.AuditAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

/**
 * 商品API
 */
@RestController
@RequestMapping("/api/products")
@Tag(name = "Product", description = "商品")
@PreAuthorize("hasRole('ADMIN')")
public class ProductApiController {
    final ProductService productService;

    final Message message;

    public ProductApiController(ProductService productService, Message message) {
        this.productService = productService;
        this.message = message;
    }

    @Operation(summary = "商品一覧を取得する")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> select(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PageInfo<Product> result = productService.selectAllWithPageInfo();
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "部品一覧を取得する")
    @GetMapping("/boms")
    public ResponseEntity<?> selectBoms(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PageInfo<Product> result = productService.selectAllBomsWithPageInfo();
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "商品を取得する")
    @GetMapping("/{productCode}")
    public ResponseEntity<?> select(@PathVariable String productCode) {
        try {
            Product result = productService.find(productCode);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "商品を登録する")
    @PostMapping
    @AuditAnnotation(process = ApplicationExecutionProcessType.商品登録, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> create(@RequestBody ProductResource resource) {
        try {
            Product product = convertToEntity(resource);
            if (productService.find(product.getProductCode().getValue()) != null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.product.already.exist")));
            }
            productService.register(product);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.product.registered")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "商品を更新する")
    @PutMapping("/{productCode}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.商品更新, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> update(@PathVariable String productCode, @RequestBody ProductResource resource) {
        try {
            resource.setProductCode(productCode);
            Product product = convertToEntity(resource);
            if (productService.find(product.getProductCode().getValue()) == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.product.not.exist")));
            }
            productService.save(product);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.product.updated")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "商品を削除する")
    @DeleteMapping("/{productCode}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.商品削除, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> delete(@PathVariable String productCode) {
        try {
            Product product = productService.find(productCode);
            if (product == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.product.not.exist")));
            }
            productService.delete(product);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.product.deleted")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "商品を検索する")
    @PostMapping("/search")
    public ResponseEntity<?> search(
            @RequestBody ProductCriteriaResource resource,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);

            ProductCriteria criteria = convertToCriteria(resource);
            PageInfo<Product> result = productService.searchProductWithPageInfo(criteria);

            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    private Product convertToEntity(ProductResource resource) {
        Product product = Product.of(
                resource.getProductCode(),
                resource.getProductFormalName(),
                resource.getProductAbbreviation(),
                resource.getProductNameKana(),
                resource.getProductType(),
                resource.getSellingPrice(),
                resource.getPurchasePrice(),
                resource.getCostOfSales(),
                resource.getTaxType(),
                resource.getProductClassificationCode(),
                resource.getMiscellaneousType(),
                resource.getStockManagementTargetType(),
                resource.getStockAllocationType(),
                resource.getVendorCode(),
                resource.getVendorBranchNumber()
        );
        product = Product.of(
                product,
                resource.getSubstituteProduct(),
                resource.getBoms(),
                resource.getCustomerSpecificSellingPrices()
        );
        return product;
    }

    private ProductCriteria convertToCriteria(ProductCriteriaResource resource) {
        return ProductCriteria.builder()
                .productCode(resource.getProductCode())
                .productNameFormal(resource.getProductNameFormal())
                .productNameAbbreviation(resource.getProductNameAbbreviation())
                .productNameKana(resource.getProductNameKana())
                .productCategoryCode(resource.getProductCategoryCode())
                .vendorCode(resource.getVendorCode())
                .productType(mapStringToCode(resource.getProductType(), ProductType::getCodeByName))
                .taxType(mapStringToCode(resource.getTaxType(), TaxType::getCodeByName))
                .miscellaneousType(mapStringToCode(resource.getMiscellaneousType(), MiscellaneousType::getCodeByName))
                .stockManagementTargetType(mapStringToCode(resource.getStockManagementTargetType(), StockManagementTargetType::getCodeByName))
                .stockAllocationType(mapStringToCode(resource.getStockAllocationType(), StockAllocationType::getCodeByName))
                .build();
    }

    private <T> T mapStringToCode(String value, Function<String, T> mapper) {
        return value != null ? mapper.apply(value) : null;
    }
}
