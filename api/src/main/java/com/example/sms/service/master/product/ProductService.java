package com.example.sms.service.master.product;

import com.example.sms.domain.model.master.product.Product;
import com.example.sms.domain.model.master.product.ProductCategory;
import com.example.sms.domain.model.master.product.ProductCategoryList;
import com.example.sms.domain.model.master.product.ProductList;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商品サービス
 */
@Service
@Transactional
public class ProductService {
    final ProductRepository productRepository;
    final ProductCategoryRepository productCategoryRepository;

    public ProductService(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    /**
     * 商品一覧
     */
    public ProductList selectAll() {
        return productRepository.selectAll();
    }

    /**
     * 商品一覧（ページング）
     */
    public PageInfo<Product> selectAllWithPageInfo() {
        return productRepository.selectAllWithPageInfo();
    }

    /**
     * 商品新規登録
     */
    public void register(Product product) {
        productRepository.save(product);
    }

    /**
     * 商品情報編集
     */
    public void save(Product product) {
        productRepository.save(product);
    }

    /**
     * 商品削除
     */
    public void delete(Product product) {
        productRepository.deleteById(product);
    }

    /**
     * 商品検索
     */
    public Product find(String productCode) {
        return productRepository.findById(productCode).orElse(null);
    }

    /**
     * 商品分類一覧
     */
    public ProductCategoryList selectAllCategory() {
        return productCategoryRepository.selectAll();
    }

    /**
     * 商品分類一覧（ページング）
     */
    public PageInfo<ProductCategory> selectAllCategoryWithPageInfo() {
        return productCategoryRepository.selectAllWithPageInfo();
    }

    /**
     * 商品分類新規登録
     */
    public void registerCategory(ProductCategory productCategory) {
        productCategoryRepository.save(productCategory);
    }

    /**
     * 商品分類情報編集
     */
    public void saveCategory(ProductCategory productCategory) {
        productCategoryRepository.save(productCategory);
    }

    /**
     * 商品分類検索
     */
    public ProductCategory findCategory(String productCategoryCode) {
        return productCategoryRepository.findById(productCategoryCode).orElse(null);
    }

    /**
     * 商品分類削除
     */
    public void deleteCategory(ProductCategory productCategory) {
        productCategoryRepository.deleteById(productCategory);
    }
}