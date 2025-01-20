package com.example.sms.stepdefinitions;

import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.master.partner.MiscellaneousType;
import com.example.sms.domain.model.master.partner.TradeProhibitedFlag;
import com.example.sms.domain.model.master.partner.vendor.Vendor;
import com.example.sms.domain.model.master.partner.vendor.VendorType;
import com.example.sms.presentation.api.master.partner.PartnerResource;
import com.example.sms.presentation.api.master.partner.VendorResource;
import com.example.sms.service.master.partner.VendorCriteria;
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

import static com.example.sms.stepdefinitions.UC011StepDefs.PARTNER_API_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class UC013StepDefs extends SpringAcceptanceTest {

    private static final String PORT = "8079";
    private static final String HOST = "http://localhost:" + PORT;
    private static final String VENDOR_API_URL = HOST + "/api/vendors";

    @Autowired
    TestDataFactory testDataFactory;

    @前提(":UC013 {string} である")
    public void login(String user) {
        String url = HOST + "/api/auth/signin";
        if (user.equals("管理者")) {
            signin("U888888", "demo", url);
        } else {
            signin("U999999", "demo", url);
        }
    }

    @前提(":UC013 {string} が登録されている")
    public void init(String data) {
        if (data.equals("仕入先データ")) {
            testDataFactory.setUpForVendorService();
        }
    }

    @もし(":UC013 {string} を取得する")
    public void toGet(String list) throws IOException {
        if (list.equals("仕入先一覧")) {
            executeGet(VENDOR_API_URL);
        }
    }

    @ならば(":UC013 {string} を取得できる")
    public void vendorListGet(String list) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        if (list.equals("仕入先一覧")) {
            String result = latestResponse.getBody();
            ListResponse<Vendor> response = objectMapper.readValue(result, new TypeReference<>() {
            });
            List<Vendor> actual = response.getList();
            assertEquals(3, actual.size()); // 登録済みの仕入先数に応じて変更してください
        }
    }

    @もし(":UC013 取引先コード {string} 名前 {string} で新規登録する")
    public void toRegisterPartner(String code, String name) throws IOException {
        PartnerResource partnerResource = new PartnerResource();
        partnerResource.setPartnerCode(code);
        partnerResource.setPartnerName(name);
        partnerResource.setPartnerNameKana("テスト");
        partnerResource.setVendorType(VendorType.仕入先);
        partnerResource.setPostalCode("123-4567");
        partnerResource.setPrefecture("東京都");
        partnerResource.setAddress1("千代田区");
        partnerResource.setAddress2("丸の内1-1-1");
        partnerResource.setTradeProhibitedFlag(TradeProhibitedFlag.OFF);
        partnerResource.setMiscellaneousType(MiscellaneousType.対象外);
        partnerResource.setPartnerGroupCode("9999");
        partnerResource.setCreditLimit(100000);
        partnerResource.setTemporaryCreditIncrease(0);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(partnerResource);
        executePost(PARTNER_API_URL, json);
    }

    @もし(":UC013 仕入先コード {string} 名前 {string} で新規登録する")
    public void toRegisterVendor(String code, String name) throws IOException {
        VendorResource vendorResource = new VendorResource();
        vendorResource.setVendorCode(code);
        vendorResource.setVendorBranchNumber(1);
        vendorResource.setVendorName(name);
        vendorResource.setVendorNameKana("シリヒキサキメイエー");
        vendorResource.setVendorContactName("担当者名A");
        vendorResource.setVendorDepartmentName("部門名A");
        vendorResource.setVendorPostalCode("123-4567");
        vendorResource.setVendorPrefecture("東京都");
        vendorResource.setVendorAddress1("新宿区1-1-1");
        vendorResource.setVendorAddress2("マンション101号室");
        vendorResource.setVendorPhoneNumber("03-1234-5678");
        vendorResource.setVendorFaxNumber("03-1234-5679");
        vendorResource.setVendorEmailAddress("test@example.com");
        vendorResource.setVendorClosingDate(10);
        vendorResource.setVendorPaymentMonth(1);
        vendorResource.setVendorPaymentDate(20);
        vendorResource.setVendorPaymentMethod(2);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(vendorResource);
        executePost(VENDOR_API_URL, json);
    }

    @ならば(":UC013 {string} が表示される")
    public void toShow(String message) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        MessageResponse response = objectMapper.readValue(result, MessageResponse.class);
        assertEquals(message, response.getMessage());
    }

    @もし(":UC013 仕入先コード {string} で検索する")
    public void toFindVendor(String code) throws IOException {
        String url = VENDOR_API_URL + "/" + code + "/1";
        executeGet(url);
    }

    @ならば(":UC013 {string} の仕入先が取得できる")
    public void canFindVendor(String name) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = latestResponse.getBody();
        Vendor vendor = objectMapper.readValue(result, Vendor.class);
        assertEquals(name, vendor.getVendorName().getValue().getName());
    }

    @かつ(":UC013 仕入先コード {string} の情報を更新する \\(名前 {string})")
    public void toUpdate(String code, String name) throws IOException {
        String url = VENDOR_API_URL + "/" + code;
        VendorResource vendorResource = new VendorResource();
        vendorResource.setVendorCode(code);
        vendorResource.setVendorBranchNumber(1);
        vendorResource.setVendorName(name);
        vendorResource.setVendorNameKana("シリヒキサキメイエー");
        vendorResource.setVendorContactName("担当者名A");
        vendorResource.setVendorDepartmentName("部門名A");
        vendorResource.setVendorPostalCode("123-4567");
        vendorResource.setVendorPrefecture("東京都");
        vendorResource.setVendorAddress1("新宿区1-1-1");
        vendorResource.setVendorAddress2("マンション101号室");
        vendorResource.setVendorPhoneNumber("03-1234-5678");
        vendorResource.setVendorFaxNumber("03-1234-5679");
        vendorResource.setVendorEmailAddress("test@example.com");
        vendorResource.setVendorClosingDate(10);
        vendorResource.setVendorPaymentMonth(1);
        vendorResource.setVendorPaymentDate(20);
        vendorResource.setVendorPaymentMethod(2);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(vendorResource);
        executePut(url, json);
    }

    @かつ(":UC013 仕入先コード {string} を削除する")
    public void toDelete(String code) throws IOException {
        String url = VENDOR_API_URL + "/" + code + "/1";
        executeDelete(url);
    }

    @もし(":UC013 名前 {string} で検索する")
    public void toSearch(String name) throws IOException {
        String url = VENDOR_API_URL + "/search";
        VendorCriteria criteria = VendorCriteria.builder().vendorName(name).build();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(criteria);
        executePost(url, json);
    }

    @ならば(":UC013 仕入先検索結果一覧を取得できる")
    public void vendorSearchResultListFetch() throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        ListResponse<Vendor> response = objectMapper.readValue(result, new TypeReference<>() {
        });
        List<Vendor> vendorList = response.getList();
        assertEquals(3, vendorList.size()); // 検索結果の期待する件数を適宜変更してください
    }
}