package com.example.sms.stepdefinitions;

import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.master.partner.PartnerGroup;
import com.example.sms.presentation.api.master.partner.PartnerGroupResource;
import com.example.sms.service.master.partner.PartnerGroupCriteria;
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

public class UC008StepDefs extends SpringAcceptanceTest {
    private static final String PORT = "8079";
    private static final String HOST = "http://localhost:" + PORT;
    private static final String AUTH_API_URL = HOST + "/api/auth";
    private static final String PARTNER_GROUP_API_URL = HOST + "/api/partner-groups";
    @Autowired
    TestDataFactory testDataFactory;

    @前提(":UC008 {string} である")
    public void login(String user) {
        String url = AUTH_API_URL + "/" + "signin";

        if (user.equals("管理者")) {
            signin("U888888", "demo", url);
        } else {
            signin("U999999", "demo", url);
        }
    }

    @前提(":UC008 {string} が登録されている")
    public void init(String data) {
        switch (data) {
            case "取引先グループデータ":
                testDataFactory.setUpForPartnerGroupService();
                break;
            default:
                break;
        }
    }

    @もし(":UC008 {string} を取得する")
    public void toGet(String list) throws IOException {
        if (list.equals("取引先グループ一覧")) {
            executeGet(PARTNER_GROUP_API_URL);
        }
    }

    @ならば(":UC008 {string} を取得できる")
    public void catGet(String list) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        if (list.equals("取引先グループ一覧")) {
            String result = latestResponse.getBody();
            ListResponse<PartnerGroup> response = objectMapper.readValue(result, new TypeReference<>() {
            });
            List<PartnerGroup> actual = response.getList();
            assertEquals(10, actual.size());
        }
    }

    @もし(":UC008 取引先グループコード {string} 取引先グループ名 {string} で新規登録する")
    public void toRegist(String code, String name) throws IOException {
        PartnerGroupResource partnerGroupResource = new PartnerGroupResource();
        partnerGroupResource.setPartnerGroupCode(code);
        partnerGroupResource.setPartnerGroupName(name);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(partnerGroupResource);
        executePost(PARTNER_GROUP_API_URL, json);
    }

    @ならば(":UC008 {string} が表示される")
    public void toShow(String message) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        MessageResponse response = objectMapper.readValue(result, MessageResponse.class);
        Assertions.assertEquals(message, response.getMessage());
    }

    @もし(":UC008 取引先グループコード {string} で検索する")
    public void toFind(String code) throws IOException {
        String url = PARTNER_GROUP_API_URL + "/" + code;
        executeGet(url);
    }

    @ならば(":UC008 {string} の取引先グループが取得できる")
    public void canFind(String name) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String result = latestResponse.getBody();
        PartnerGroup partnerGroup = objectMapper.readValue(result, PartnerGroup.class);
        Assertions.assertEquals(name, partnerGroup.getPartnerGroupName());
    }

    @かつ(":UC008 取引先グループコード {string} の情報を更新する \\(取引先グループ名 {string})")
    public void toUpdate(String code, String name) throws IOException {
        String url = PARTNER_GROUP_API_URL + "/" + code;

        PartnerGroupResource partnerGroupResource = new PartnerGroupResource();
        partnerGroupResource.setPartnerGroupCode(code);
        partnerGroupResource.setPartnerGroupName(name);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(partnerGroupResource);
        executePut(url, json);
    }

    @かつ(":UC008 取引先グループコード {string} を削除する")
    public void toDelete(String code) throws IOException {
        String url = PARTNER_GROUP_API_URL + "/" + code;
        executeDelete(url);
    }

    @もし(":UC008 取引先グループ名 {string} で検索する")
    public void toSearch(String name) throws IOException {
        String url = PARTNER_GROUP_API_URL + "/search";
        PartnerGroupCriteria criteria = PartnerGroupCriteria.builder().partnerGroupName(name).build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(criteria);
        executePost(url, json);
    }

    @ならば(":UC008 取引先グループ検索結果一覧を取得できる")
    public void catFetch() throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ListResponse<PartnerGroup> response = objectMapper.readValue(result, new TypeReference<>() {
        });
        List<PartnerGroup> productList = response.getList();
        assertEquals(10, productList.size());
    }
}
