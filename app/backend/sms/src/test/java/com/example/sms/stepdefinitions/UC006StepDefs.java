package com.example.sms.stepdefinitions;

import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.system.audit.ApplicationExecutionHistory;
import com.example.sms.domain.model.system.audit.ApplicationExecutionProcess;
import com.example.sms.presentation.api.system.audit.AuditCriteriaResource;
import com.example.sms.stepdefinitions.utils.ListResponse;
import com.example.sms.stepdefinitions.utils.MessageResponse;
import com.example.sms.stepdefinitions.utils.SpringAcceptanceTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.cucumber.java.ja.ならば;
import io.cucumber.java.ja.もし;
import io.cucumber.java.ja.前提;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UC006StepDefs extends SpringAcceptanceTest {
    private static final String PORT = "8079";
    private static final String HOST = "http://localhost:" + PORT;
    private static final String AUTH_API_URL = HOST + "/api/auth";
    private static final String AUDIT_API_URL = HOST + "/api/audits";

    @Autowired
    TestDataFactory testDataFactory;

    @前提(":UC006 {string} である")
    public void login(String user) {
        String url = AUTH_API_URL + "/" + "signin";

        if (user.equals("管理者")) {
            signin("U888888", "demo", url);
        } else {
            signin("U999999", "demo", url);
        }
    }

    @前提(":UC006 アプリケーションが実行されている")
    public void exec() {
        testDataFactory.setUpForAuditService();
    }

    @もし(":UC006 {string} を取得する")
    public void toGet(String list) throws IOException {
        if (list.equals("アプリケーション実行履歴")) {
            executeGet(AUDIT_API_URL);
        }
    }

    @ならば(":UC006 {string} を取得できる")
    public void catGet(String list) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String result;

        if (list.equals("アプリケーション実行履歴一覧")) {
            result = latestResponse.getBody();
            ListResponse<ApplicationExecutionHistory> response = objectMapper.readValue(result, new TypeReference<>() {
            });
            List<ApplicationExecutionHistory> applicationExecutionHistoryList = response.getList();
            assertEquals(2, applicationExecutionHistoryList.size());
        }
    }

    @もし(":UC006 アプリケーション実行履歴を検索する")
    public void search() throws IOException {
        String url = AUDIT_API_URL + "/search";
        AuditCriteriaResource auditCriteriaResource = new AuditCriteriaResource();
        auditCriteriaResource.setProcess(ApplicationExecutionProcess.of("その他", "9999"));

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(auditCriteriaResource);
        executePost(url, json);
    }

    @ならば(":UC006 アプリケーション実行履歴を取得できる")
    public void fetch() throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ListResponse<ApplicationExecutionHistory> response = objectMapper.readValue(result, new TypeReference<>() {
        });
        List<ApplicationExecutionHistory> applicationExecutionHistoryList = response.getList();
        assertEquals(2, applicationExecutionHistoryList.size());
    }

    @もし(":UC006 アプリケーション実行履歴を削除する")
    public void delete() throws IOException {
        String url = AUDIT_API_URL + "/" + "1";
        executeDelete(url);
    }

    @ならば(":UC006 {string} が表示される")
    public void toShow(String message) throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        MessageResponse response = objectMapper.readValue(result, MessageResponse.class);
        assertEquals(message, response.getMessage());
    }
}
