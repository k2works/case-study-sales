package com.example.sms.stepdefinitions;

import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.common.region.Region;
import com.example.sms.presentation.api.common.region.RegionResource;
import com.example.sms.service.master.common.RegionCriteria;
import com.example.sms.stepdefinitions.utils.MessageResponse;
import com.example.sms.stepdefinitions.utils.ListResponse;
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
public class UC010StepDefs extends SpringAcceptanceTest {

    private static final String PORT = "8079";
    private static final String HOST = "http://localhost:" + PORT;
    private static final String REGION_API_URL = HOST + "/api/regions";

    @Autowired
    TestDataFactory testDataFactory;

    @前提(":UC010 {string} である")
    public void login(String user) {
        String url = HOST + "/api/auth/signin";
        if (user.equals("管理者")) {
            signin("U888888", "demo", url);
        } else {
            signin("U999999", "demo", url);
        }
    }

    @前提(":UC010 {string} が登録されている")
    public void init(String data) {
        if (data.equals("地域コードデータ")) {
            testDataFactory.setUpForRegionService();
        }
    }

    @もし(":UC010 {string} を取得する")
    public void toGet(String list) throws IOException {
        if (list.equals("地域コード一覧")) {
            executeGet(REGION_API_URL);
        }
    }

    @ならば(":UC010 {string} を取得できる")
    public void regionCodeListGet(String list) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        if (list.equals("地域コード一覧")) {
            String result = latestResponse.getBody();
            ListResponse<Region> response = objectMapper.readValue(result, new TypeReference<>() {
            });
            List<Region> actual = response.getList();
            assertEquals(3, actual.size());
        }
    }

    @もし(":UC010 地域コード {string} 地域名 {string} で新規登録する")
    public void toRegist(String code, String name) throws IOException {
        RegionResource regionCodeResource = new RegionResource();
        regionCodeResource.setRegionCode(code);
        regionCodeResource.setRegionName(name);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(regionCodeResource);
        executePost(REGION_API_URL, json);
    }

    @ならば(":UC010 {string} が表示される")
    public void toShow(String message) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        MessageResponse response = objectMapper.readValue(result, MessageResponse.class);
        assertEquals(message, response.getMessage());
    }

    @もし(":UC010 地域コード {string} で検索する")
    public void toFind(String code) throws IOException {
        String url = REGION_API_URL + "/" + code;
        executeGet(url);
    }

    @ならば(":UC010 {string} の地域コードが取得できる")
    public void canFind(String name) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String result = latestResponse.getBody();
        Region regionCodeType = objectMapper.readValue(result, Region.class);
        assertEquals(name, regionCodeType.getRegionName());
    }

    @かつ(":UC010 地域コード {string} の情報を更新する \\(地域名 {string})")
    public void toUpdate(String code, String name) throws IOException {
        String url = REGION_API_URL + "/" + code;
        RegionResource regionCodeResource = new RegionResource();
        regionCodeResource.setRegionCode(code);
        regionCodeResource.setRegionName(name);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(regionCodeResource);
        executePut(url, json);
    }

    @かつ(":UC010 地域コード {string} を削除する")
    public void toDelete(String code) throws IOException {
        String url = REGION_API_URL + "/" + code;
        executeDelete(url);
    }

    @もし(":UC010 地域名 {string} で検索する")
    public void toSearch(String name) throws IOException {
        String url = REGION_API_URL + "/search";
        RegionCriteria criteria = RegionCriteria.builder().regionName(name).build();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(criteria);
        executePost(url, json);
    }

    @ならば(":UC010 地域コード検索結果一覧を取得できる")
    public void regionCodeSearchResultListFetch() throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ListResponse<Region> response = objectMapper.readValue(result, new TypeReference<>() {
        });
        List<Region> regionCodeList = response.getList();
        assertEquals(3, regionCodeList.size());
    }
}