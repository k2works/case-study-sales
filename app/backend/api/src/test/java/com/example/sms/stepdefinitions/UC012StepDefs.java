package com.example.sms.stepdefinitions;

import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.master.partner.customer.Customer;
import com.example.sms.domain.type.partner.CustomerBillingCategory;
import com.example.sms.domain.type.partner.CustomerType;
import com.example.sms.presentation.api.master.partner.CustomerResource;
import com.example.sms.presentation.api.master.partner.PartnerResource;
import com.example.sms.service.master.partner.CustomerCriteria;
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

public class UC012StepDefs extends SpringAcceptanceTest {

    private static final String PORT = "8079";
    private static final String HOST = "http://localhost:" + PORT;
    private static final String CUSTOMER_API_URL = HOST + "/api/customers";

    @Autowired
    TestDataFactory testDataFactory;

    @前提(":UC012 {string} である")
    public void login(String user) {
        String url = HOST + "/api/auth/signin";
        if (user.equals("管理者")) {
            signin("U888888", "demo", url);
        } else {
            signin("U999999", "demo", url);
        }
    }

    @前提(":UC012 {string} が登録されている")
    public void init(String data) {
        if (data.equals("顧客データ")) {
            testDataFactory.setUpForCustomerService();
        }
    }

    @もし(":UC012 {string} を取得する")
    public void toGet(String list) throws IOException {
        if (list.equals("顧客一覧")) {
            executeGet(CUSTOMER_API_URL);
        }
    }

    @ならば(":UC012 {string} を取得できる")
    public void customerListGet(String list) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        if (list.equals("顧客一覧")) {
            String result = latestResponse.getBody();
            ListResponse<Customer> response = objectMapper.readValue(result, new TypeReference<>() {
            });
            List<Customer> actual = response.getList();
            assertEquals(3, actual.size());
        }
    }

    @もし(":UC012 取引先コード {string} 名前 {string} で新規登録する")
    public void toRegisterPartner(String code, String name) throws IOException {
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

    @もし(":UC012 顧客コード {string} 名前 {string} で新規登録する")
    public void toRegister(String code, String name) throws IOException {
        CustomerResource customerResource = new CustomerResource();
        customerResource.setCustomerCode(code);
        customerResource.setCustomerBranchNumber(1);
        customerResource.setCustomerType(CustomerType.顧客);
        customerResource.setBillingCode("001");
        customerResource.setBillingBranchNumber(1);
        customerResource.setCollectionCode("001");
        customerResource.setCollectionBranchNumber(1);
        customerResource.setCustomerName(name);
        customerResource.setCustomerNameKana("テスト");
        customerResource.setCompanyRepresentativeCode("001");
        customerResource.setCustomerRepresentativeName("代表者");
        customerResource.setCustomerDepartmentName("部署名");
        customerResource.setCustomerPostalCode("123-4567");
        customerResource.setCustomerPrefecture("東京都");
        customerResource.setCustomerAddress1("千代田区");
        customerResource.setCustomerAddress2("丸の内1-1-1");
        customerResource.setCustomerPhoneNumber("03-1234-5678");
        customerResource.setCustomerFaxNumber("03-1234-5679");
        customerResource.setCustomerEmailAddress("hoge@example.com");
        customerResource.setCustomerBillingType(CustomerBillingCategory.都度請求);
        customerResource.setCustomerClosingDay1(10);
        customerResource.setCustomerPaymentMonth1(0);
        customerResource.setCustomerPaymentDay1(10);
        customerResource.setCustomerPaymentMethod1(1);
        customerResource.setCustomerClosingDay2(20);
        customerResource.setCustomerPaymentMonth2(1);
        customerResource.setCustomerPaymentDay2(99);
        customerResource.setCustomerPaymentMethod2(2);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(customerResource);
        executePost(CUSTOMER_API_URL, json);
    }

    @ならば(":UC012 {string} が表示される")
    public void toShow(String message) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        MessageResponse response = objectMapper.readValue(result, MessageResponse.class);
        assertEquals(message, response.getMessage());
    }

    @もし(":UC012 顧客コード {string} で検索する")
    public void toFind(String code) throws IOException {
        String url = CUSTOMER_API_URL + "/" + code + "/1";
        executeGet(url);
    }

    @ならば(":UC012 {string} の顧客が取得できる")
    public void canFind(String name) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String result = latestResponse.getBody();
        Customer customer = objectMapper.readValue(result, Customer.class);
        assertEquals(name, customer.getCustomerName().getValue().getName());
    }

    @かつ(":UC012 顧客コード {string} の情報を更新する \\(名前 {string})")
    public void toUpdate(String code, String name) throws IOException {
        String url = CUSTOMER_API_URL + "/" + code;
        CustomerResource customerResource = new CustomerResource();
        customerResource.setCustomerCode(code);
        customerResource.setCustomerBranchNumber(1);
        customerResource.setCustomerType(CustomerType.顧客);
        customerResource.setBillingCode("001");
        customerResource.setBillingBranchNumber(1);
        customerResource.setCollectionCode("001");
        customerResource.setCollectionBranchNumber(1);
        customerResource.setCustomerName(name);
        customerResource.setCustomerNameKana("テスト");
        customerResource.setCompanyRepresentativeCode("001");
        customerResource.setCustomerRepresentativeName("代表者");
        customerResource.setCustomerDepartmentName("部署名");
        customerResource.setCustomerPostalCode("123-4567");
        customerResource.setCustomerPrefecture("東京都");
        customerResource.setCustomerAddress1("千代田区");
        customerResource.setCustomerAddress2("丸の内1-1-1");
        customerResource.setCustomerPhoneNumber("03-1234-5678");
        customerResource.setCustomerFaxNumber("03-1234-5679");
        customerResource.setCustomerEmailAddress("hoge@example.com");
        customerResource.setCustomerBillingType(CustomerBillingCategory.都度請求);
        customerResource.setCustomerClosingDay1(10);
        customerResource.setCustomerPaymentMonth1(0);
        customerResource.setCustomerPaymentDay1(10);
        customerResource.setCustomerPaymentMethod1(1);
        customerResource.setCustomerClosingDay2(20);
        customerResource.setCustomerPaymentMonth2(1);
        customerResource.setCustomerPaymentDay2(99);
        customerResource.setCustomerPaymentMethod2(2);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(customerResource);
        executePut(url, json);
    }

    @かつ(":UC012 顧客コード {string} を削除する")
    public void toDelete(String code) throws IOException {
        String url = CUSTOMER_API_URL + "/" + code + "/1";
        executeDelete(url);
    }

    @もし(":UC012 名前 {string} で検索する")
    public void toSearch(String name) throws IOException {
        String url = CUSTOMER_API_URL + "/search";
        CustomerCriteria criteria = CustomerCriteria.builder().customerName(name).build();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(criteria);
        executePost(url, json);
    }

    @ならば(":UC012 顧客検索結果一覧を取得できる")
    public void customerSearchResultListFetch() throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ListResponse<Customer> response = objectMapper.readValue(result, new TypeReference<>() {
        });
        List<Customer> customerList = response.getList();
        assertEquals(3, customerList.size());
    }
}