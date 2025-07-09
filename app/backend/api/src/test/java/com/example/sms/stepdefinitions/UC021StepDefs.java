package com.example.sms.stepdefinitions;

import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.master.payment.account.incoming.BankAccountType;
import com.example.sms.domain.model.master.payment.account.incoming.PaymentAccount;
import com.example.sms.domain.model.master.payment.account.incoming.PaymentAccountType;
import com.example.sms.presentation.api.master.payment.PaymentAccountResource;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class UC021StepDefs extends SpringAcceptanceTest {
    private static final String PORT = "8079";
    private static final String HOST = "http://localhost:" + PORT;
    private static final String AUTH_API_URL = HOST + "/api/auth";
    private static final String PAYMENT_ACCOUNTS_API_URL = HOST + "/api/payment-accounts";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_ZONED_DATE_TIME;
    private static final DateTimeFormatter LOCAL_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Autowired
    TestDataFactory testDataFactory;

    @前提(":UC021 {string} である")
    public void login(String user) {
        String url = AUTH_API_URL + "/" + "signin";

        if (user.equals("管理者")) {
            signin("U888888", "demo", url);
        } else {
            signin("U999999", "demo", url);
        }
    }

    @前提(":UC021 {string} が登録されている")
    public void init(String data) {
        switch (data) {
            case "入金口座データ":
                // テストデータの初期化処理
                testDataFactory.setUpForPaymentAccountService();
                break;
            default:
                break;
        }
    }

    @もし(":UC021 {string} を取得する")
    public void toGet(String list) throws IOException {
        if (list.equals("入金口座一覧")) {
            executeGet(PAYMENT_ACCOUNTS_API_URL);
        } else if (list.equals("全ての入金口座")) {
            executeGet(PAYMENT_ACCOUNTS_API_URL + "/all");
        }
    }

    @ならば(":UC021 {string} を取得できる")
    public void canGet(String list) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        if (list.equals("入金口座一覧")) {
            String result = latestResponse.getBody();
            com.github.pagehelper.PageInfo<PaymentAccountResource> response = objectMapper.readValue(result, new TypeReference<>() {
            });
            List<PaymentAccountResource> actual = response.getList();
            assertEquals(3, actual.size());
        } else if (list.equals("全ての入金口座")) {
            String result = latestResponse.getBody();
            List<PaymentAccountResource> actual = objectMapper.readValue(result, new TypeReference<>() {
            });
            assertEquals(3, actual.size());
        }
    }

    @もし(":UC021 入金口座コード {string} 入金口座名 {string} で新規登録する")
    public void toRegist(String code, String name) throws IOException {
        PaymentAccountResource resource = new PaymentAccountResource();
        resource.setAccountCode(code);
        resource.setAccountName(name);
        
        // 現在時刻をISO_ZONED_DATE_TIME形式で設定
        ZonedDateTime now = LocalDateTime.now().atZone(ZoneId.systemDefault());
        ZonedDateTime future = now.plusYears(10);
        resource.setStartDate(now.format(FORMATTER));
        resource.setEndDate(future.format(FORMATTER));
        
        // その他の必須項目を設定
        resource.setAccountNameAfterStart(name);
        resource.setAccountType(PaymentAccountType.銀行);
        resource.setAccountNumber("12345678");
        resource.setBankAccountType(BankAccountType.普通);
        resource.setAccountHolder("テスト口座名義");
        resource.setDepartmentCode("10000");
        resource.setDepartmentStartDate(now.format(FORMATTER));
        resource.setBankCode("0001");
        resource.setBranchCode("001");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(resource);
        executePost(PAYMENT_ACCOUNTS_API_URL, json);
    }

    @ならば(":UC021 {string} が表示される")
    public void toShow(String message) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        MessageResponse response = objectMapper.readValue(result, MessageResponse.class);
        Assertions.assertEquals(message, response.getMessage());
    }

    @もし(":UC021 入金口座コード {string} で検索する")
    public void toFind(String code) throws IOException {
        String url = PAYMENT_ACCOUNTS_API_URL + "/" + code;
        executeGet(url);
    }

    @ならば(":UC021 {string} の入金口座が取得できる")
    public void canFind(String name) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String result = latestResponse.getBody();
        PaymentAccountResource account = objectMapper.readValue(result, PaymentAccountResource.class);
        Assertions.assertEquals(name, account.getAccountName());
    }

    @かつ(":UC021 入金口座コード {string} の情報を更新する \\(入金口座名 {string})")
    public void toUpdate(String code, String name) throws IOException {
        String url = PAYMENT_ACCOUNTS_API_URL + "/" + code;

        // 現在の入金口座情報を取得
        executeGet(url);
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        PaymentAccountResource currentAccount = objectMapper.readValue(result, PaymentAccountResource.class);

        // 入金口座名を更新
        currentAccount.setAccountName(name);

        // 日付文字列を安全に解析してから適切な形式で設定
        ZonedDateTime startDate = parseDateTime(currentAccount.getStartDate());
        ZonedDateTime endDate = parseDateTime(currentAccount.getEndDate());
        currentAccount.setStartDate(startDate.format(FORMATTER));
        currentAccount.setEndDate(endDate.format(FORMATTER));
        ZonedDateTime departmentStartDate = parseDateTime(currentAccount.getDepartmentStartDate());
        currentAccount.setDepartmentStartDate(departmentStartDate.format(FORMATTER));

        String json = objectMapper.writeValueAsString(currentAccount);
        executePut(url, json);
    }


    @かつ(":UC021 入金口座コード {string} を削除する")
    public void toDelete(String code) throws IOException {
        String url = PAYMENT_ACCOUNTS_API_URL + "/" + code;
        executeDelete(url);
    }

    /**
     * 日付文字列を適切な形式で解析し、ZonedDateTimeを返すヘルパーメソッド
     */
    public static ZonedDateTime parseDateTime(String dateTimeString) {
        try {
            // まずZonedDateTimeとして解析を試行
            return ZonedDateTime.parse(dateTimeString, FORMATTER);
        } catch (Exception e) {
            try {
                // ZonedDateTimeで解析できない場合、LocalDateTimeとして解析してからZonedDateTimeに変換
                LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, LOCAL_FORMATTER);
                return localDateTime.atZone(ZoneId.systemDefault());
            } catch (Exception ex) {
                // ISO形式のマイクロ秒付きLocalDateTimeの場合
                LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString);
                return localDateTime.atZone(ZoneId.systemDefault());
            }
        }
    }

}