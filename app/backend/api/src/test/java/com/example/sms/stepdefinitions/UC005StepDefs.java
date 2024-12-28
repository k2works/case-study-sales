package com.example.sms.stepdefinitions;

import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.master.product.Product;
import com.example.sms.domain.model.master.product.ProductCategory;
import com.example.sms.domain.type.product.*;
import com.example.sms.presentation.api.master.product.ProductCategoryCriteriaResource;
import com.example.sms.presentation.api.master.product.ProductCategoryResource;
import com.example.sms.presentation.api.master.product.ProductCriteriaResource;
import com.example.sms.presentation.api.master.product.ProductResource;
import com.example.sms.stepdefinitions.utils.ListResponse;
import com.example.sms.stepdefinitions.utils.MessageResponse;
import com.example.sms.stepdefinitions.utils.SpringAcceptanceTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.cucumber.java.ja.かつ;
import io.cucumber.java.ja.ならば;
import io.cucumber.java.ja.もし;
import io.cucumber.java.ja.前提;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class UC005StepDefs extends SpringAcceptanceTest {
    private static final String PORT = "8079";
    private static final String HOST = "http://localhost:" + PORT;
    private static final String AUTH_API_URL = HOST + "/api/auth";
    private static final String PRODUCTS_API_URL = HOST + "/api/products";
    private static final String PRODUCT_CATEGORIES_API_URL = HOST + "/api/product/categories";
    @Autowired
    TestDataFactory testDataFactory;

    @前提(":UC005 {string} である")
    public void login(String user) {
        String url = AUTH_API_URL + "/" + "signin";

        if (user.equals("管理者")) {
            signin("U888888", "demo", url);
        } else {
            signin("U999999", "demo", url);
        }
    }

    @前提(":UC005 {string} が登録されている")
    public void init(String data) {
        switch (data) {
            case "商品データ":
                testDataFactory.setUpForProductService();
                break;
            default:
                break;
        }
    }

    @もし(":UC005 {string} を取得する")
    public void toGet(String list) throws IOException {
        if (list.equals("商品一覧")) {
            executeGet(PRODUCTS_API_URL);
        } else if (list.equals("商品分類一覧")) {
            executeGet(PRODUCT_CATEGORIES_API_URL);
        }
    }

    @ならば(":UC005 {string} を取得できる")
    public void catGet(String list) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        if (list.equals("商品一覧")) {
            String result = latestResponse.getBody();
            ListResponse<Product> response = objectMapper.readValue(result, new TypeReference<>() {
            });
            List<Product> actual = response.getList();
            assertEquals(3, actual.size());
        } else if (list.equals("商品分類一覧")) {
            String result = latestResponse.getBody();
            ListResponse<ProductCategory> response = objectMapper.readValue(result, new TypeReference<>() {
            });
            List<ProductCategory> actual = response.getList();
            assertEquals(2, actual.size());
        }
    }

    @もし(":UC005 商品コード {string} 商品名 {string} で新規登録する")
    public void toRegist(String code, String name) throws IOException {
        ProductResource productResource = new ProductResource();
        productResource.setProductCode(code);
        productResource.setProductFormalName(name);
        productResource.setProductAbbreviation("");
        productResource.setProductNameKana("");
        productResource.setProductType(ProductType.その他);
        productResource.setSellingPrice(0);
        productResource.setPurchasePrice(0);
        productResource.setCostOfSales(0);
        productResource.setTaxType(TaxType.その他);
        productResource.setProductClassificationCode("");
        productResource.setMiscellaneousType(MiscellaneousType.適用);
        productResource.setStockManagementTargetType(StockManagementTargetType.対象);
        productResource.setStockAllocationType(StockAllocationType.引当済);
        productResource.setSupplierCode("");
        productResource.setSupplierBranchNumber(0);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(productResource);
        executePost(PRODUCTS_API_URL, json);
    }

    @ならば(":UC005 {string} が表示される")
    public void toShow(String message) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        MessageResponse response = objectMapper.readValue(result, MessageResponse.class);
        Assertions.assertEquals(message, response.getMessage());
    }

    @もし(":UC005 商品コード {string} で検索する")
    public void toFind(String code) throws IOException {
        String url = PRODUCTS_API_URL + "/" + code;
        executeGet(url);
    }

    @ならば(":UC005 {string} の商品が取得できる")
    public void canFind(String name) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String result = latestResponse.getBody();
        Product product = objectMapper.readValue(result, Product.class);
        Assertions.assertEquals(name, product.getProductName().getProductFormalName());
    }

    @かつ(":UC005 商品コード {string} の情報を更新する \\(商品名 {string})")
    public void toUpdate(String code, String name) throws IOException {
        String url = PRODUCTS_API_URL + "/" + code;

        ProductResource productResource = new ProductResource();
        productResource.setProductFormalName(name);
        productResource.setProductAbbreviation("");
        productResource.setProductNameKana("");
        productResource.setProductType(ProductType.その他);
        productResource.setSellingPrice(0);
        productResource.setPurchasePrice(0);
        productResource.setCostOfSales(0);
        productResource.setTaxType(TaxType.その他);
        productResource.setProductClassificationCode("");
        productResource.setMiscellaneousType(MiscellaneousType.適用);
        productResource.setStockManagementTargetType(StockManagementTargetType.対象);
        productResource.setStockAllocationType(StockAllocationType.引当済);
        productResource.setSupplierCode("");
        productResource.setSupplierBranchNumber(0);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(productResource);
        executePut(url, json);
    }

    @かつ(":UC005 商品コード {string} を削除する")
    public void toDelete(String code) throws IOException {
        String url = PRODUCTS_API_URL + "/" + code;
        executeDelete(url);
    }

    @もし(":UC005 商品分類コード {string} 商品分類名 {string} で新規登録する")
    public void toRegistCategory(String code, String name) throws IOException {
        ProductCategoryResource productCategoryResource = new ProductCategoryResource();
        productCategoryResource.setProductCategoryCode(code);
        productCategoryResource.setProductCategoryName(name);
        productCategoryResource.setProductCategoryHierarchy(0);
        productCategoryResource.setProductCategoryPath("");
        productCategoryResource.setLowestLevelDivision(0);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(productCategoryResource);
        executePost(PRODUCT_CATEGORIES_API_URL, json);
    }

    @もし(":UC005 商品分離コード {string} で検索する")
    public void toFindCategory(String code) throws IOException {
        String url = PRODUCT_CATEGORIES_API_URL + "/" + code;
        executeGet(url);
    }

    @ならば(":UC005 {string} の商品分類が取得できる")
    public void canGetCategory(String name) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String result = latestResponse.getBody();
        ProductCategory productCategory = objectMapper.readValue(result, ProductCategory.class);
        Assertions.assertEquals(name, productCategory.getProductCategoryName());
    }

    @かつ(":UC005 商品分類コード {string} の情報を更新する \\(商品分類名 {string})")
    public void toUpdateCategory(String code, String name) throws IOException {
        String url = PRODUCT_CATEGORIES_API_URL + "/" + code;

        ProductCategoryResource productCategoryResource = new ProductCategoryResource();
        productCategoryResource.setProductCategoryName(name);
        productCategoryResource.setProductCategoryHierarchy(0);
        productCategoryResource.setProductCategoryPath("");
        productCategoryResource.setLowestLevelDivision(0);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(productCategoryResource);
        executePut(url, json);
    }

    @かつ(":UC005 商品分類コード {string} を削除する")
    public void toDeleteCategory(String code) throws IOException {
        String url = PRODUCT_CATEGORIES_API_URL + "/" + code;
        executeDelete(url);
    }

    @もし(":UC005 商品区分 {string} で検索する")
    public void searchByCriteria(String name) throws IOException {
        String url = PRODUCTS_API_URL + "/search";
        ProductCriteriaResource productCriteriaResource = new ProductCriteriaResource();
        productCriteriaResource.setProductType(name);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(productCriteriaResource);
        executePost(url, json);
    }

    @ならば(":UC005 商品検索結果一覧を取得できる")
    public void catFetch() throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ListResponse<Product> response = objectMapper.readValue(result, new TypeReference<>() {
        });
        List<Product> productList = response.getList();
        assertEquals(3, productList.size());
    }

    @もし(":UC005 商品分類パス {string} で検索する")
    public void searchByCriteria2(String name) throws IOException {
        String url = PRODUCT_CATEGORIES_API_URL + "/search";
        ProductCategoryCriteriaResource productCategoryCriteriaResource = new ProductCategoryCriteriaResource();
        productCategoryCriteriaResource.setProductCategoryPath(name);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(productCategoryCriteriaResource);
        executePost(url, json);
    }

    @ならば(":UC005 商品分類検索結果一覧を取得できる")
    public void catFetch2() throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ListResponse<ProductCategory> response = objectMapper.readValue(result, new TypeReference<>() {
        });
        List<ProductCategory> productCategoryList = response.getList();
        assertEquals(2, productCategoryList.size());
    }
}
