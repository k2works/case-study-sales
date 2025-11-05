package com.example.sms.stepdefinitions;

import com.example.sms.TestDataFactory;
import com.example.sms.presentation.api.master.locationnumber.LocationNumberCriteriaResource;
import com.example.sms.presentation.api.master.locationnumber.LocationNumberResource;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UC028StepDefs extends SpringAcceptanceTest {
    private static final String PORT = "8079";
    private static final String HOST = "http://localhost:" + PORT;
    private static final String AUTH_API_URL = HOST + "/api/auth";
    private static final String LOCATIONNUMBER_API_URL = HOST + "/api/locationnumbers";

    @Autowired
    TestDataFactory testDataFactory;

    @前提(":UC028 {string} である")
    public void login(String user) {
        String url = AUTH_API_URL + "/" + "signin";
        if (user.equals("管理者")) {
            signin("U888888", "demo", url);
        } else {
            signin("U999999", "demo", url);
        }
    }

    @前提(":UC028 {string} が登録されている")
    public void init(String data) {
        // 棚番データ初期化
        testDataFactory.setUpForLocationNumberService();
    }

    @もし(":UC028 {string} を取得する")
    public void toGet(String list) throws IOException {
        if (list.equals("棚番一覧")) {
            executeGet(LOCATIONNUMBER_API_URL);
        }
    }

    @ならば(":UC028 {string} を取得できる")
    public void canGet(String list) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String result;
        if (list.equals("棚番一覧")) {
            result = latestResponse.getBody();
            ListResponse<LocationNumberResource> response = objectMapper.readValue(result, new TypeReference<>() {});
            List<LocationNumberResource> locationNumbers = response.getList();
            assertEquals(3, locationNumbers.size()); // 初期データ3件
        }
    }

    @ならば(":UC028 {string} が表示される")
    public void toShow(String message) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        MessageResponse response = objectMapper.readValue(result, MessageResponse.class);
        assertEquals(message, response.getMessage());
    }

    @もし(":UC028 倉庫コード {string} 棚番コード {string} 商品コード {string} で新規登録する")
    public void toRegister(String warehouseCode, String locationNumberCode, String productCode) throws IOException {
        LocationNumberResource resource = LocationNumberResource.builder()
                .warehouseCode(warehouseCode)
                .locationNumberCode(locationNumberCode)
                .productCode(productCode)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(resource);
        executePost(LOCATIONNUMBER_API_URL, json);
    }

    @もし(":UC028 倉庫コード {string} 棚番コード {string} 商品コード {string} で検索する")
    public void toFind(String warehouseCode, String locationNumberCode, String productCode) throws IOException {
        String url = LOCATIONNUMBER_API_URL + "/" + warehouseCode + "/" + locationNumberCode + "/" + productCode;
        executeGet(url);
    }

    @ならば(":UC028 倉庫コード {string} 棚番コード {string} 商品コード {string} の棚番が取得できる")
    public void canFind(String warehouseCode, String locationNumberCode, String productCode) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String result = latestResponse.getBody();
        LocationNumberResource locationNumber = objectMapper.readValue(result, LocationNumberResource.class);
        assertEquals(warehouseCode, locationNumber.getWarehouseCode());
        assertEquals(locationNumberCode, locationNumber.getLocationNumberCode());
        assertEquals(productCode, locationNumber.getProductCode());
    }

    @かつ(":UC028 倉庫コード {string} 棚番コード {string} 商品コード {string} の情報を更新する")
    public void toUpdate(String warehouseCode, String locationNumberCode, String productCode) throws IOException {
        String url = LOCATIONNUMBER_API_URL + "/" + warehouseCode + "/" + locationNumberCode + "/" + productCode;
        LocationNumberResource resource = LocationNumberResource.builder()
                .warehouseCode(warehouseCode)
                .locationNumberCode(locationNumberCode)
                .productCode(productCode)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(resource);
        executePut(url, json);
    }

    @かつ(":UC028 倉庫コード {string} 棚番コード {string} 商品コード {string} を削除する")
    public void toDelete(String warehouseCode, String locationNumberCode, String productCode) throws IOException {
        String url = LOCATIONNUMBER_API_URL + "/" + warehouseCode + "/" + locationNumberCode + "/" + productCode;
        executeDelete(url);
    }

    @もし(":UC028 倉庫コード {string} で棚番を検索する")
    public void searchByWarehouseCode(String warehouseCode) throws IOException {
        String url = LOCATIONNUMBER_API_URL + "/by-warehouse/" + warehouseCode;
        executeGet(url);
    }

    @ならば(":UC028 倉庫コード検索結果一覧を取得できる")
    public void canFetchByWarehouseCode() throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        List<LocationNumberResource> list = objectMapper.readValue(result, new TypeReference<>() {});
        // 初期データから倉庫コード「W01」で検索すると3件のはず
        assertEquals(3, list.size());
    }

    @もし(":UC028 棚番コード {string} で棚番を検索する")
    public void searchByLocationNumberCode(String locationNumberCode) throws IOException {
        String url = LOCATIONNUMBER_API_URL + "/by-location/" + locationNumberCode;
        executeGet(url);
    }

    @ならば(":UC028 棚番コード検索結果一覧を取得できる")
    public void canFetchByLocationNumberCode() throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        List<LocationNumberResource> list = objectMapper.readValue(result, new TypeReference<>() {});
        // 初期データから棚番コード「A001」で検索すると1件のはず
        assertEquals(1, list.size());
    }

    @もし(":UC028 倉庫コード {string} を条件に検索する")
    public void searchByCriteria(String warehouseCode) throws IOException {
        String url = LOCATIONNUMBER_API_URL + "/search";
        LocationNumberCriteriaResource criteria = new LocationNumberCriteriaResource();
        criteria.setWarehouseCode(warehouseCode);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(criteria);
        executePost(url, json);
    }

    @ならば(":UC028 条件検索結果一覧を取得できる")
    public void canFetchByCriteria() throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ListResponse<LocationNumberResource> response = objectMapper.readValue(result, new TypeReference<>() {});
        List<LocationNumberResource> list = response.getList();
        // 初期データから倉庫コード「W01」で検索すると3件のはず
        assertEquals(3, list.size());
    }
}