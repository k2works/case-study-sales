package com.example.sms.stepdefinitions;

import com.example.sms.TestDataFactory;
import com.example.sms.domain.model.system.download.DownloadTarget;
import com.example.sms.presentation.api.system.download.DownloadCriteriaResource;
import com.example.sms.stepdefinitions.utils.SpringAcceptanceTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.cucumber.java.ja.ならば;
import io.cucumber.java.ja.もし;
import io.cucumber.java.ja.前提;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        DownloadCriteriaResource downloadCriteriaResource = new DownloadCriteriaResource();
        if (target.equals("部門")) {
            downloadCriteriaResource = new DownloadCriteriaResource();
            downloadCriteriaResource.setTarget(DownloadTarget.部門);
        }
        if (target.equals("社員")) {
            downloadCriteriaResource = new DownloadCriteriaResource();
            downloadCriteriaResource.setTarget(DownloadTarget.社員);
        }
        if (target.equals("商品分類")) {
            downloadCriteriaResource = new DownloadCriteriaResource();
            downloadCriteriaResource.setTarget(DownloadTarget.商品分類);
        }
        if (target.equals("商品")) {
            downloadCriteriaResource = new DownloadCriteriaResource();
            downloadCriteriaResource.setTarget(DownloadTarget.商品);
        }
        if (target.equals("取引先グループ")) {
            downloadCriteriaResource = new DownloadCriteriaResource();
            downloadCriteriaResource.setTarget(DownloadTarget.取引先グループ);
        }
        if (target.equals("取引先")) {
            downloadCriteriaResource = new DownloadCriteriaResource();
            downloadCriteriaResource.setTarget(DownloadTarget.取引先);
        }
        if (target.equals("顧客")) {
            downloadCriteriaResource = new DownloadCriteriaResource();
            downloadCriteriaResource.setTarget(DownloadTarget.顧客);
        }
        if (target.equals("仕入先")) {
            downloadCriteriaResource = new DownloadCriteriaResource();
            downloadCriteriaResource.setTarget(DownloadTarget.仕入先);
        }
        if (target.equals("受注")) {
            downloadCriteriaResource = new DownloadCriteriaResource();
            downloadCriteriaResource.setTarget(DownloadTarget.受注);
        }
        if (target.equals("出荷")) {
            downloadCriteriaResource = new DownloadCriteriaResource();
            downloadCriteriaResource.setTarget(DownloadTarget.出荷);
        }
        if (target.equals("売上")) {
            downloadCriteriaResource = new DownloadCriteriaResource();
            downloadCriteriaResource.setTarget(DownloadTarget.売上);
        }
        if (target.equals("請求")) {
            downloadCriteriaResource = new DownloadCriteriaResource();
            downloadCriteriaResource.setTarget(DownloadTarget.請求);
        }
        if (target.equals("在庫")) {
            downloadCriteriaResource = new DownloadCriteriaResource();
            downloadCriteriaResource.setTarget(DownloadTarget.在庫);
        }
        if (target.equals("倉庫")) {
            downloadCriteriaResource = new DownloadCriteriaResource();
            downloadCriteriaResource.setTarget(DownloadTarget.倉庫);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(downloadCriteriaResource);
        executePost(url, json);
    }

    @ならば(":UC007 {int}件のダウンロード件数を取得できる")
    public void canGet(int count) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String result = latestResponse.getBody();
        assertEquals(String.valueOf(count), result);
    }

    @もし(":UC007 {string} ダウンロードを実行する")
    public void exec(String target) throws IOException {
        String url = DOWNLOAD_API_URL + "/download";
        DownloadCriteriaResource downloadCriteriaResource = new DownloadCriteriaResource();
        if (target.equals("部門")) {
            downloadCriteriaResource = new DownloadCriteriaResource();
            downloadCriteriaResource.setTarget(DownloadTarget.部門);
        }
        if (target.equals("社員")) {
            downloadCriteriaResource = new DownloadCriteriaResource();
            downloadCriteriaResource.setTarget(DownloadTarget.社員);
        }
        if (target.equals("商品分類")) {
            downloadCriteriaResource = new DownloadCriteriaResource();
            downloadCriteriaResource.setTarget(DownloadTarget.商品分類);
        }
        if (target.equals("商品")) {
            downloadCriteriaResource = new DownloadCriteriaResource();
            downloadCriteriaResource.setTarget(DownloadTarget.商品);
        }
        if (target.equals("取引先グループ")) {
            downloadCriteriaResource = new DownloadCriteriaResource();
            downloadCriteriaResource.setTarget(DownloadTarget.取引先グループ);
        }
        if (target.equals("取引先")) {
            downloadCriteriaResource = new DownloadCriteriaResource();
            downloadCriteriaResource.setTarget(DownloadTarget.取引先);
        }
        if (target.equals("顧客")) {
            downloadCriteriaResource = new DownloadCriteriaResource();
            downloadCriteriaResource.setTarget(DownloadTarget.顧客);
        }
        if (target.equals("仕入先")) {
            downloadCriteriaResource = new DownloadCriteriaResource();
            downloadCriteriaResource.setTarget(DownloadTarget.仕入先);
        }
        if (target.equals("受注")) {
            downloadCriteriaResource = new DownloadCriteriaResource();
            downloadCriteriaResource.setTarget(DownloadTarget.受注);
        }
        if (target.equals("在庫")) {
            downloadCriteriaResource = new DownloadCriteriaResource();
            downloadCriteriaResource.setTarget(DownloadTarget.在庫);
        }
        if (target.equals("倉庫")) {
            downloadCriteriaResource = new DownloadCriteriaResource();
            downloadCriteriaResource.setTarget(DownloadTarget.倉庫);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(downloadCriteriaResource);
        executePost(url, json);
    }
}
