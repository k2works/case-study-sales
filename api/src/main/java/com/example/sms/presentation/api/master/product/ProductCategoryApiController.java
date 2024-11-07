package com.example.sms.presentation.api.master.product;

import com.example.sms.domain.model.master.product.ProductCategory;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.master.product.ProductService;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 商品分類API
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/product/categories")
@Tag(name = "ProductCategory", description = "商品分類")
@PreAuthorize("hasRole('ADMIN')")
public class ProductCategoryApiController {
    final ProductService productService;

    final Message message;

    public ProductCategoryApiController(ProductService productService, Message message) {
        this.productService = productService;
        this.message = message;
    }

    @Operation(summary = "商品分類一覧を取得する")
    @GetMapping
    public ResponseEntity<?> select(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PageInfo<ProductCategory> result = productService.selectAllCategoryWithPageInfo();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "商品分類を取得する")
    @GetMapping("/{productCategoryCode}")
    public ResponseEntity<?> select(@PathVariable String productCategoryCode) {
        try {
            ProductCategory result = productService.findCategory(productCategoryCode);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "商品分類を登録する")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProductCategoryResource productCategoryResource) {
        try {
            ProductCategory productCategory = ProductCategory.of(productCategoryResource.getProductCategoryCode(), productCategoryResource.getProductCategoryName(), productCategoryResource.getProductCategoryHierarchy(), productCategoryResource.getProductCategoryPath(), productCategoryResource.getLowestLevelDivision());
            if (productService.findCategory(productCategory.getProductCategoryCode()) != null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.product_category.already.exist")));
            }
            productService.registerCategory(productCategory);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.product_category.registered")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "商品分類を更新する")
    @PutMapping("/{productCategoryCode}")
    public ResponseEntity<?> update(@PathVariable String productCategoryCode, @RequestBody ProductCategoryResource productCategoryResource) {
        try {

            ProductCategory productCategory = ProductCategory.of(productCategoryCode, productCategoryResource.getProductCategoryName(), productCategoryResource.getProductCategoryHierarchy(), productCategoryResource.getProductCategoryPath(), productCategoryResource.getLowestLevelDivision());
            if (productService.findCategory(productCategory.getProductCategoryCode()) == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.product_category.not.exist")));
            }
            productService.saveCategory(productCategory);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.product_category.updated")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "商品分類を削除する")
    @DeleteMapping("/{productCategoryCode}")
    public ResponseEntity<?> delete(@PathVariable String productCategoryCode) {
        try {
            ProductCategory productCategory = productService.findCategory(productCategoryCode);
            if (productCategory == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.product_category.not.exist")));
            }
            productService.deleteCategory(productCategory);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.product_category.deleted")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
