package com.example.sms.stepdefinitions.utils;

import com.example.sms.SmsApplication;
import com.example.sms.presentation.api.system.auth.payload.response.JwtResponse;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@CucumberContextConfiguration
@SpringBootTest(classes = SmsApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class SpringAcceptanceTest {
    protected static ResponseResults latestResponse = null;

    @Autowired
    protected RestTemplate restTemplate;

    String authHeader = null;

    protected void signin(String userId, String password, String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("password", password);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<JwtResponse> response = restTemplate.postForEntity(url, entity, JwtResponse.class);

        JwtResponse jwtResponse = response.getBody();
        authHeader = Objects.requireNonNull(jwtResponse).getTokenType() + " " + jwtResponse.getAccessToken();
    }

    protected void executeGet(String url) {
        final Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", authHeader);
        final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback(headers);
        final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();

        restTemplate.setErrorHandler(errorHandler);
        latestResponse = restTemplate.execute(url, HttpMethod.GET, requestCallback, response -> {
            if (errorHandler.hadError) {
                return (errorHandler.getResults());
            } else {
                return (new ResponseResults(response));
            }
        });
    }

    protected void executePost(String url, String resource) {
        final Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", authHeader);
        final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback(headers);
        requestCallback.setBody(resource);
        final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();

        if (restTemplate == null) {
            restTemplate = new RestTemplate();
        }

        restTemplate.setErrorHandler(errorHandler);
        latestResponse = restTemplate
                .execute(url, HttpMethod.POST, requestCallback, response -> {
                    if (errorHandler.hadError) {
                        return (errorHandler.getResults());
                    } else {
                        return (new ResponseResults(response));
                    }
                });
    }

    protected void executePost(String url, MultipartFile multipartFile) {
        // RestTemplateの初期化（初回のみ）
        if (restTemplate == null) {
            restTemplate = new RestTemplate();
        }

        // エラーハンドラー設定
        final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();
        restTemplate.setErrorHandler(errorHandler);

        // ヘッダー設定
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", authHeader);
        headers.set("Accept", "application/json");

        // executeの使用
        // レスポンス処理
        // ResponseResults型へ変換
        latestResponse = restTemplate.execute(
                url,
                HttpMethod.POST,
                request -> { // リクエストのカスタマイズ
                    // ヘッダーを設定
                    request.getHeaders().addAll(headers);

                    // ファイル部分を設定
                    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                    body.add("file", new org.springframework.core.io.ByteArrayResource(multipartFile.getBytes()) {
                        @Override
                        public String getFilename() {
                            return multipartFile.getOriginalFilename(); // ファイル名を設定
                        }
                    });

                    // Multipartのコンテンツを出力
                    new org.springframework.http.converter.FormHttpMessageConverter()
                            .write(body, MediaType.MULTIPART_FORM_DATA, request);
                },
                ResponseResults::new
        );
    }

    protected void executePut(String url, String resource) {
        final Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", authHeader);
        final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback(headers);
        requestCallback.setBody(resource);
        final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();

        if (restTemplate == null) {
            restTemplate = new RestTemplate();
        }

        restTemplate.setErrorHandler(errorHandler);
        latestResponse = restTemplate
                .execute(url, HttpMethod.PUT, requestCallback, response -> {
                    if (errorHandler.hadError) {
                        return (errorHandler.getResults());
                    } else {
                        return (new ResponseResults(response));
                    }
                });
    }

    protected void executeDelete(String url) {
        final Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", authHeader);
        final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback(headers);
        final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();

        if (restTemplate == null) {
            restTemplate = new RestTemplate();
        }

        restTemplate.setErrorHandler(errorHandler);
        latestResponse = restTemplate
                .execute(url, HttpMethod.DELETE, requestCallback, response -> {
                    if (errorHandler.hadError) {
                        return (errorHandler.getResults());
                    } else {
                        return (new ResponseResults(response));
                    }
                });
    }

    private class ResponseResultErrorHandler implements ResponseErrorHandler {
        private ResponseResults results = null;
        private Boolean hadError = false;

        private ResponseResults getResults() {
            return results;
        }

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            hadError = response.getStatusCode().value() >= 400;
            return hadError;
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            results = new ResponseResults(response);
        }
    }
}
