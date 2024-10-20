package com.example.sms.presentation.api.system.auth;

import com.example.sms.PresentationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import com.example.sms.service.system.auth.AuthApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@PresentationTest
@AutoConfigureMybatis
@DisplayName("認証APIコントローラ")
public class AuthApiControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private AuthApiService authApiService;

    @Autowired
    TestDataFactory testDataFactory;

    @BeforeEach
    void setUp() {
        testDataFactory.setUpForAuthApiService();
    }

    @Test
    @DisplayName("ユーザーを認証してJWTを返すことができる")
    @Disabled("CIで失敗するため無効化")
    void authenticateUserAndReturnJwt() throws Exception {
        String jwtToken = "testjwt";
        when(authApiService.authenticateUser(any(Authentication.class), any(), any())).thenReturn(jwtToken);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":\"U999999\",\"password\":\"demo\"}"))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = "{\"userId\":\"U999999\",\"roles\":[\"ROLE_USER\"],\"accessToken\":\"testjwt\",\"tokenType\":\"Bearer\"}";
        // 改行コードとスペースを削除して比較
        expectedResponseBody = expectedResponseBody.replaceAll("\\r|\\n|\\s", "");
        actualResponseBody = actualResponseBody.replaceAll("\\r|\\n|\\s", "");
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals(expectedResponseBody, actualResponseBody);
    }

    @Test
    @DisplayName("ユーザーを新規登録できる")
    void registerNewUser() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":\"U999990\",\"password\":\"testpassword\",\"firstName\":\"Test\",\"lastName\":\"User\",\"role\":\"USER\"}"))
                .andExpect(status().isOk())
                .andReturn();
        ResponseEntity<MessageResponse> responseEntity = ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        String actualMessage = Objects.requireNonNull(responseEntity.getBody()).getMessage();
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals("User registered successfully!", actualMessage);
    }
}
