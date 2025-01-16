package com.example.sms.stepdefinitions;

import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.master.partner.Partner;
import com.example.sms.presentation.api.master.partner.PartnerResource;
import com.example.sms.service.master.partner.PartnerCriteria;
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
public class UC011StepDefs extends SpringAcceptanceTest {

    private static final String PORT = "8079";
    private static final String HOST = "http://localhost:" + PORT;
    private static final String PARTNER_API_URL = HOST + "/api/partners";

    @Autowired
    TestDataFactory testDataFactory;

    @前提(":UC011 {string} である")
    public void login(String user) {
        String url = HOST + "/api/auth/signin";
        if (user.equals("管理者")) {
            signin("U888888", "demo", url);
        } else {
            signin("U999999", "demo", url);
        }
    }

    @前提(":UC011 {string} が登録されている")
    public void init(String data) {
        if (data.equals("取引先データ")) {
            testDataFactory.setUpForPartnerService();
        }
    }

    @もし(":UC011 {string} を取得する")
    public void toGet(String list) throws IOException {
        if (list.equals("取引先一覧")) {
            executeGet(PARTNER_API_URL);
        }
    }

    @ならば(":UC011 {string} を取得できる")
    public void partnerListGet(String list) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        if (list.equals("取引先一覧")) {
            String result = latestResponse.getBody();
            ListResponse<Partner> response = objectMapper.readValue(result, new TypeReference<>() {
            });
            List<Partner> actual = response.getList();
            assertEquals(4, actual.size());
        }
    }

    @もし(":UC011 取引先コード {string} 名前 {string} で新規登録する")
    public void toRegister(String code, String name) throws IOException {
        PartnerResource partnerResource = new PartnerResource();
        partnerResource.setPartnerCode(code);
        partnerResource.setPartnerName(name);
        partnerResource.setPartnerNameKana("テスト");
        partnerResource.setVendorType(1);
        partnerResource.setPostalCode("123-4567");
        partnerResource.setPrefecture("東京都");
        partnerResource.setAddress1("千代田区");
        partnerResource.setAddress2("丸の内1-1-1");
        partnerResource.setTradeProhibitedFlag(0);
        partnerResource.setMiscellaneousType(1);
        partnerResource.setPartnerGroupCode("9999");
        partnerResource.setCreditLimit(100000);
        partnerResource.setTemporaryCreditIncrease(0);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(partnerResource);
        executePost(PARTNER_API_URL, json);
    }

    @ならば(":UC011 {string} が表示される")
    public void toShow(String message) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        MessageResponse response = objectMapper.readValue(result, MessageResponse.class);
        assertEquals(message, response.getMessage());
    }

    @もし(":UC011 取引先コード {string} で検索する")
    public void toFind(String code) throws IOException {
        String url = PARTNER_API_URL + "/" + code;
        executeGet(url);
    }

    @ならば(":UC011 {string} の取引先が取得できる")
    public void canFind(String name) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String result = latestResponse.getBody();
        Partner partner = objectMapper.readValue(result, Partner.class);
        assertEquals(name, partner.getPartnerName().getName());
    }

    @かつ(":UC011 取引先コード {string} の情報を更新する \\(名前 {string})")
    public void toUpdate(String code, String name) throws IOException {
        String url = PARTNER_API_URL + "/" + code;
        PartnerResource partnerResource = new PartnerResource();
        partnerResource.setPartnerCode(code);
        partnerResource.setPartnerName(name);
        partnerResource.setPartnerNameKana("テスト");
        partnerResource.setVendorType(1);
        partnerResource.setPostalCode("123-4567");
        partnerResource.setPrefecture("東京都");
        partnerResource.setAddress1("千代田区");
        partnerResource.setAddress2("丸の内1-1-1");
        partnerResource.setTradeProhibitedFlag(0);
        partnerResource.setMiscellaneousType(1);
        partnerResource.setPartnerGroupCode("9999");
        partnerResource.setCreditLimit(100000);
        partnerResource.setTemporaryCreditIncrease(0);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(partnerResource);
        executePut(url, json);
    }

    @かつ(":UC011 取引先コード {string} を削除する")
    public void toDelete(String code) throws IOException {
        String url = PARTNER_API_URL + "/" + code;
        executeDelete(url);
    }

    @もし(":UC011 名前 {string} で検索する")
    public void toSearch(String name) throws IOException {
        String url = PARTNER_API_URL + "/search";
        PartnerCriteria criteria = PartnerCriteria.builder().partnerName(name).build();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(criteria);
        executePost(url, json);
    }

    @ならば(":UC011 取引先検索結果一覧を取得できる")
    public void partnerSearchResultListFetch() throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ListResponse<Partner> response = objectMapper.readValue(result, new TypeReference<>() {
        });
        List<Partner> partnerList = response.getList();
        assertEquals(4, partnerList.size());
    }
}