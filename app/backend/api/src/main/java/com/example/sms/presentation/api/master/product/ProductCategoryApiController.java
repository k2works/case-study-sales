package com.example.sms.presentation.api.master.product;

import com.example.sms.domain.model.master.product.Product;
import com.example.sms.domain.model.master.product.ProductCategory;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistoryType;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcessType;
import com.example.sms.presentation.Message;
import com.example.sms.presentation.PageNation;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.BusinessException;
import com.example.sms.service.PageNationService;
import com.example.sms.service.master.product.ProductCategoryCriteria;
import com.example.sms.service.master.product.ProductService;
import com.example.sms.service.system.audit.AuditAnnotation;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.example.sms.presentation.api.master.product.ProductCategoryResourceDTOMapper.convertToCriteria;
import static com.example.sms.presentation.api.master.product.ProductCategoryResourceDTOMapper.convertToEntity;
import static com.example.sms.presentation.api.master.product.ProductCategoryResourceDTOMapper.getAddFilteredProducts;
import static com.example.sms.presentation.api.master.product.ProductCategoryResourceDTOMapper.getDeleteFilteredProducts;

import java.util.List;

/**
 * 商品分類API
 */
@RestController
@RequestMapping("/api/product/categories")
@Tag(name = "ProductCategory", description = "商品分類")
@PreAuthorize("hasRole('ADMIN')")
public class ProductCategoryApiController {
    final ProductService productService;
    final PageNationService pageNationService;
    final Message message;

    public ProductCategoryApiController(ProductService productService, PageNationService pageNationService, Message message) {
        this.productService = productService;
        this.pageNationService = pageNationService;
        this.message = message;
    }

    @Operation(summary = "商品分類一覧を取得する")
    @GetMapping
    public ResponseEntity<?> select(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            PageNation.startPage(page, pageSize);
            PageInfo<ProductCategory> pageInfo = productService.selectAllCategoryWithPageInfo();
            PageInfo<ProductCategoryResource> result = pageNationService.getPageInfo(pageInfo, ProductCategoryResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "商品分類を取得する")
    @GetMapping("/{productCategoryCode}")
    public ResponseEntity<?> select(@PathVariable String productCategoryCode) {
        try {
            ProductCategory result = productService.findCategory(productCategoryCode);
            return ResponseEntity.ok(ProductCategoryResource.from(result));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "商品分類を登録する")
    @PostMapping
    @AuditAnnotation(process = ApplicationExecutionProcessType.商品分類登録, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> create(@RequestBody ProductCategoryResource productCategoryResource) {
        try {
            ProductCategory productCategory = convertToEntity(productCategoryResource);
            if (productService.findCategory(productCategory.getProductCategoryCode().getValue()) != null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.product_category.already.exist")));
            }
            productService.registerCategory(productCategory);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.product_category.registered")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "商品分類を更新する")
    @PutMapping("/{productCategoryCode}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.商品分類更新, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> update(@PathVariable String productCategoryCode, @RequestBody ProductCategoryResource productCategoryResource) {
        try {
            ProductCategory productCategory = convertToEntity(productCategoryCode, productCategoryResource);
            if (productService.findCategory(productCategory.getProductCategoryCode().getValue()) == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.product_category.not.exist")));
            }
            List<Product> addProducts = getAddFilteredProducts(productCategoryResource);
            List<Product> deleteProducts = getDeleteFilteredProducts(productCategoryResource);
            productService.saveCategory(productCategory, addProducts, deleteProducts);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.product_category.updated")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "商品分類を削除する")
    @DeleteMapping("/{productCategoryCode}")
    @AuditAnnotation(process = ApplicationExecutionProcessType.商品分類削除, type = ApplicationExecutionHistoryType.同期)
    public ResponseEntity<?> delete(@PathVariable String productCategoryCode) {
        try {
            ProductCategory productCategory = productService.findCategory(productCategoryCode);
            if (productCategory == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(message.getMessage("error.product_category.not.exist")));
            }
            productService.deleteCategory(productCategory);
            return ResponseEntity.ok(new MessageResponse(message.getMessage("success.product_category.deleted")));
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "商品分類を検索する")
    @PostMapping("/search")
    public ResponseEntity<?> search(
            @RequestBody ProductCategoryCriteriaResource resource,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int... page) {
        try {
            ProductCategoryCriteria criteria = convertToCriteria(resource);
            PageInfo<ProductCategory> entity = productService.searchProductCategoryWithPageInfo(criteria);
            PageInfo<ProductCategoryResource> result = pageNationService.getPageInfo(entity, ProductCategoryResource::from);
            return ResponseEntity.ok(result);
        } catch (BusinessException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
