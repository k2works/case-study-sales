package com.example.sms.stepdefinitions;

import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.system.download.DownloadTarget;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.presentation.api.system.download.DownloadConditionResource;
import com.example.sms.stepdefinitions.utils.SpringAcceptanceTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.cucumber.java.ja.ならば;
import io.cucumber.java.ja.もし;
import io.cucumber.java.ja.前提;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UC007StepDefs extends SpringAcceptanceTest {
    private static final String PORT = "8079";
    private static final String HOST = "http://localhost:" + PORT;
    private static final String AUTH_API_URL = HOST + "/api/auth";
    private static final String DOWNLOAD_API_URL = HOST + "/api/downloads";

    @Autowired
    TestDataFactory testDataFactory;

    @前提(":UC007 {string} である")
    public void login(String user) {
        String url = AUTH_API_URL + "/" + "signin";

        if (user.equals("管理者")) {
            signin("U888888", "demo", url);
        } else {
            signin("U999999", "demo", url);
        }
    }

    @前提(":UC007 ダウンロードデータが存在する")
    public void setUp() {
        testDataFactory.setUpForDownloadService();
    }

    @もし(":UC007 {string} ダウンロード件数を取得する")
    public void toGet(String target) throws IOException {
        String url = DOWNLOAD_API_URL + "/count";
        DownloadConditionResource downloadConditionResource = new DownloadConditionResource();
        if (target.equals("部門")) {
            downloadConditionResource = new DownloadConditionResource();
            downloadConditionResource.setTarget(DownloadTarget.部門);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(downloadConditionResource);
        executePost(url, json);
    }

    @ならば(":UC007 ダウンロード件数を取得できる")
    public void canGet() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String result = latestResponse.getBody();
        assertEquals("3", result);
    }

    @もし(":UC007 {string} ダウンロードを実行する")
    public void exec(String target) throws IOException {
        String url = DOWNLOAD_API_URL + "/download";
        DownloadConditionResource downloadConditionResource = new DownloadConditionResource();
        if (target.equals("部門")) {
            downloadConditionResource = new DownloadConditionResource();
            downloadConditionResource.setTarget(DownloadTarget.部門);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(downloadConditionResource);
        executePost(url, json);
    }

    @ならば(":UC007 ダウンロードデータが作成される")
    public void toShow() throws JsonProcessingException {
        String result = latestResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        MessageResponse response = objectMapper.readValue(result, MessageResponse.class);
        assertNotNull(response);
    }
}
