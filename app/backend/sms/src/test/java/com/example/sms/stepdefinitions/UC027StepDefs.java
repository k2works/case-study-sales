package com.example.sms.stepdefinitions;

import com.example.sms.TestDataFactory;
import com.example.sms.presentation.api.master.warehouse.WarehouseCriteriaResource;
import com.example.sms.presentation.api.master.warehouse.WarehouseResource;
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

public class UC027StepDefs extends SpringAcceptanceTest {
    private static final String PORT = "8079";
    private static final String HOST = "http://localhost:" + PORT;
    private static final String AUTH_API_URL = HOST + "/api/auth";
    private static final String WAREHOUSE_API_URL = HOST + "/api/warehouses";

    @Autowired
    TestDataFactory testDataFactory;

    @前提(":UC027 {string} である")
    public void login(String user) {
        String url = AUTH_API_URL + "/" + "signin";
        if (user.equals("管理者")) {
            signin("U888888", "demo", url);
        } else {
            signin("U999999", "demo", url);
        }
    }

    @前提(":UC027 {string} が登録されている")
    public void init(String data) {
        // 倉庫データ初期化
        testDataFactory.setUpForWarehouseService();
    }

    @もし(":UC027 {string} を取得する")
    public void toGet(String list) throws IOException {
        if (list.equals("倉庫一覧")) {
            executeGet(WAREHOUSE_API_URL);
        }
    }

    @ならば(":UC027 {string} を取得できる")
    public void canGet(String list) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String result;
        if (list.equals("倉庫一覧")) {
            result = latestResponse.getBody();
            ListResponse<WarehouseResource> response = objectMapper.readValue(result, new TypeReference<>() {});
            List<WarehouseResource> warehouses = response.getList();
            assertEquals(1, warehouses.size());
        }
    }

    @ならば(":UC027 {string} が表示される")
    public void toShow(String message) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        MessageResponse response = objectMapper.readValue(result, MessageResponse.class);
        assertEquals(message, response.getMessage());
    }

    @もし(":UC027 倉庫コード {string} 倉庫名 {string} で新規登録する")
    public void toRegister(String code, String name) throws IOException {
        WarehouseResource resource = WarehouseResource.builder()
                .warehouseCode(code)
                .warehouseName(name)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(resource);
        executePost(WAREHOUSE_API_URL, json);
    }

    @もし(":UC027 倉庫コード {string} で検索する")
    public void toFind(String code) throws IOException {
        String url = WAREHOUSE_API_URL + "/" + code;
        executeGet(url);
    }

    @かつ(":UC027 倉庫コード {string} の情報を更新する \\(" +
            "倉庫名 {string})")
    public void toUpdate(String code, String name) throws IOException {
        String url = WAREHOUSE_API_URL + "/" + code;
        WarehouseResource resource = WarehouseResource.builder()
                .warehouseCode(code)
                .warehouseName(name)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(resource);
        executePut(url, json);
    }

    @ならば(":UC027 {string} の倉庫が取得できる")
    public void canFind(String name) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String result = latestResponse.getBody();
        WarehouseResource warehouse = objectMapper.readValue(result, WarehouseResource.class);
        assertEquals(name, warehouse.getWarehouseName());
    }

    @かつ(":UC027 倉庫コード {string} を削除する")
    public void toDelete(String code) throws IOException {
        String url = WAREHOUSE_API_URL + "/" + code;
        executeDelete(url);
    }

    @もし(":UC027 倉庫名を {string} で検索する")
    public void searchByCriteria(String name) throws IOException {
        String url = WAREHOUSE_API_URL + "/search";
        WarehouseCriteriaResource criteria = new WarehouseCriteriaResource();
        criteria.setWarehouseName(name);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(criteria);
        executePost(url, json);
    }

    @ならば(":UC027 検索結果一覧を取得できる")
    public void canFetch() throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ListResponse<WarehouseResource> response = objectMapper.readValue(result, new TypeReference<>() {});
        List<WarehouseResource> list = response.getList();
        // 初期データ（本社倉庫）に対して「本社」で検索すると1件のはず
        assertEquals(1, list.size());
    }
}
