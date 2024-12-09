package com.example.sms.presentation.api.master.product;

import com.example.sms.domain.model.master.product.Product;
import com.example.sms.domain.type.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.type.audit.ApplicationExecutionProcessType;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.master.product.ProductService;
import com.example.sms.service.system.audit.AuditAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 商品API
 */
@CrossOrigin(origins = "*", maxAge = 3600)
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
    public ResponseEntity<?> select(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PageInfo<Product> result = productService.selectAllWithPageInfo();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
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
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "商品を取得する")
    @GetMapping("/{productCode}")
    public ResponseEntity<?> select(@PathVariable String productCode) {
        try {
            Product result = productService.find(productCode);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "商品を登録する")
    @PostMapping
    @AuditAnnotation(process = ApplicationExecutionProcessType.PRODUCT_CREATE, type = ApplicationExecutionHistoryType.SYNC)
    public ResponseEntity<?> create(@RequestBody ProductResource productResource) {
        try {
            Product product = createProduct(productResource.getProductCode(), productResource);
            if (productService.find(product.getProductCode().getValue()) != null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.product.already.exist")));
            }
            productService.register(product);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.product.registered")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "商品を更新する")
    @PutMapping("/{productCode}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.PRODUCT_UPDATE, type = ApplicationExecutionHistoryType.SYNC)
    public ResponseEntity<?> update(@PathVariable String productCode, @RequestBody ProductResource productResource) {
        try {
            Product product = createProduct(productCode, productResource);
            if (productService.find(product.getProductCode().getValue()) == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.product.not.exist")));
            }
            productService.save(product);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.product.updated")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "商品を削除する")
    @DeleteMapping("/{productCode}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.PRODUCT_DELETE, type = ApplicationExecutionHistoryType.SYNC)
    public ResponseEntity<?> delete(@PathVariable String productCode) {
        try {
            Product product = productService.find(productCode);
            if (product == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.product.not.exist")));
            }
            productService.delete(product);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.product.deleted")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    private static Product createProduct(String productResource, ProductResource productResource1) {
        Product product = Product.of(
                productResource,
                productResource1.getProductFormalName(),
                productResource1.getProductAbbreviation(),
                productResource1.getProductNameKana(),
                productResource1.getProductType(),
                productResource1.getSellingPrice(),
                productResource1.getPurchasePrice(),
                productResource1.getCostOfSales(),
                productResource1.getTaxType(),
                productResource1.getProductClassificationCode(),
                productResource1.getMiscellaneousType(),
                productResource1.getStockManagementTargetType(),
                productResource1.getStockAllocationType(),
                productResource1.getSupplierCode(),
                productResource1.getSupplierBranchNumber()
        );
        product = Product.of(
                product,
                productResource1.getSubstituteProduct(),
                productResource1.getBoms(),
                productResource1.getCustomerSpecificSellingPrices()
        );
        return product;
    }

}
