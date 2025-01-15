package com.example.sms.stepdefinitions;

import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.master.partner.PartnerCategoryType;
import com.example.sms.presentation.api.master.partner.PartnerCategoryResource;
import com.example.sms.stepdefinitions.utils.MessageResponse;
import com.example.sms.service.master.partner.PartnerCategoryCriteria;
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

public class UC009StepDefs extends SpringAcceptanceTest {
    
    private static final String PORT = "8079";
    private static final String HOST = "http://localhost:" + PORT;
    private static final String PARTNER_CATEGORY_API_URL = HOST + "/api/partner-categories";
    
    @Autowired
    TestDataFactory testDataFactory;

    @前提(":UC009 {string} である")
    public void login(String user) {
        String url = HOST + "/api/auth/signin";
        if (user.equals("管理者")) {
            signin("U888888", "demo", url);
        } else {
            signin("U999999", "demo", url);
        }
    }

    @前提(":UC009 {string} が登録されている")
    public void init(String data) {
        if (data.equals("取引先分類データ")) {
            testDataFactory.setUpForPartnerCategoryService();
        }
    }

    @もし(":UC009 {string} を取得する")
    public void toGet(String list) throws IOException {
        if (list.equals("取引先分類一覧")) {
            executeGet(PARTNER_CATEGORY_API_URL);
        }
    }

    @ならば(":UC009 {string} を取得できる")
    public void partnerCategoryListGet(String list) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        if (list.equals("取引先分類一覧")) {
            String result = latestResponse.getBody();
            ListResponse<PartnerCategoryType> response = objectMapper.readValue(result, new TypeReference<>() {
            });
            List<PartnerCategoryType> actual = response.getList();
            assertEquals(1, actual.size());
        }
    }

    @もし(":UC009 取引先分類コード {string} 取引先分類名 {string} で新規登録する")
    public void toRegist(String code, String name) throws IOException {
        PartnerCategoryResource partnerCategoryResource = new PartnerCategoryResource();
        partnerCategoryResource.setPartnerCategoryCode(code);
        partnerCategoryResource.setPartnerCategoryName(name);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(partnerCategoryResource);
        executePost(PARTNER_CATEGORY_API_URL, json);
    }

    @ならば(":UC009 {string} が表示される")
    public void toShow(String message) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        MessageResponse response = objectMapper.readValue(result, MessageResponse.class);
        assertEquals(message, response.getMessage());
    }

    @もし(":UC009 取引先分類コード {string} で検索する")
    public void toFind(String code) throws IOException {
        String url = PARTNER_CATEGORY_API_URL + "/" + code;
        executeGet(url);
    }

    @ならば(":UC009 {string} の取引先分類が取得できる")
    public void canFind(String name) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String result = latestResponse.getBody();
        PartnerCategoryType partnerCategoryType = objectMapper.readValue(result, PartnerCategoryType.class);
        assertEquals(name, partnerCategoryType.getPartnerCategoryTypeName());
    }

    @かつ(":UC009 取引先分類コード {string} の情報を更新する \\(取引先分類名 {string})")
    public void toUpdate(String code, String name) throws IOException {
        String url = PARTNER_CATEGORY_API_URL + "/" + code;
        PartnerCategoryResource partnerCategoryResource = new PartnerCategoryResource();
        partnerCategoryResource.setPartnerCategoryCode(code);
        partnerCategoryResource.setPartnerCategoryName(name);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(partnerCategoryResource);
        executePut(url, json);
    }

    @かつ(":UC009 取引先分類コード {string} を削除する")
    public void toDelete(String code) throws IOException {
        String url = PARTNER_CATEGORY_API_URL + "/" + code;
        executeDelete(url);
    }

    @もし(":UC009 取引先分類名 {string} で検索する")
    public void toSearch(String name) throws IOException {
        String url = PARTNER_CATEGORY_API_URL + "/search";
        PartnerCategoryCriteria criteria = PartnerCategoryCriteria.builder().partnerCategoryTypeName(name).build();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(criteria);
        executePost(url, json);
    }

    @ならば(":UC009 取引先分類検索結果一覧を取得できる")
    public void partnerCategorySearchResultListFetch() throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ListResponse<PartnerCategoryType> response = objectMapper.readValue(result, new TypeReference<>() {
        });
        List<PartnerCategoryType> categoryList = response.getList();
        assertEquals(4, categoryList.size());
    }
}