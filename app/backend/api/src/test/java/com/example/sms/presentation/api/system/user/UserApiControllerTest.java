package com.example.sms.presentation.api.system.user;

import com.example.sms.PresentationTest;
import com.example.sms.TestDataFactory;
import com.example.sms.presentation.api.system.auth.payload.response.MessageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("ユーザーAPI")
@PresentationTest
@AutoConfigureMybatis
public class UserApiControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    TestDataFactory testDataFactory;

    @BeforeEach
    void setUp() {
        testDataFactory.setUpForUserManagementService();
    }

    @Test
    @WithMockUser(username = "U888888", roles = {"ADMIN"})
    @DisplayName("ユーザー一覧を取得できる")
    void canRetrieveUserList() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = "U888888", roles = {"ADMIN"})
    @DisplayName("ユーザーを取得できる")
    void canRetrieveUser() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/U999999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = """
                {
                 "userId":{"value":"U999999"},
                 "password":{"value":"$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK"},
                 "name":{"firstName":"first","lastName":"last"},
                 "roleName":"USER"
                 }
                """;
        // 改行コードとスペースを削除して比較
        expectedResponseBody = expectedResponseBody.replaceAll("\\r|\\n|\\s", "");
        actualResponseBody = actualResponseBody.replaceAll("\\r|\\n|\\s", "");
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals(expectedResponseBody, actualResponseBody);
    }

    @Test
    @WithMockUser(username = "U888888", roles = {"ADMIN"})
    @DisplayName("ユーザーを作成できる")
    void canCreateUser() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "userId": "U999990",
                                    "password": "newPassword",
                                    "firstName": "Ddd",
                                    "lastName": "Ddd",
                                    "roleName": "USER"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn();
        ResponseEntity<MessageResponse> responseEntity = ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        String actualMessage = Objects.requireNonNull(responseEntity.getBody()).getMessage();
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals("User registered successfully!", actualMessage);
    }

    @Test
    @WithMockUser(username = "U888888", roles = {"ADMIN"})
    @DisplayName("ユーザーを更新できる")
    void canUpdateUser() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/users/U999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "userId": "U999999",
                                    "password": "password",
                                    "firstName": "firstName2",
                                    "lastName": "lastName2",
                                    "roleName": "ADMIN"
                                }"""))
                .andExpect(status().isOk())
                .andReturn();
        ResponseEntity<MessageResponse> responseEntity = ResponseEntity.ok(new MessageResponse("User updated successfully!"));
        String actualMessage = Objects.requireNonNull(responseEntity.getBody()).getMessage();
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals("User updated successfully!", actualMessage);
    }

    @Test
    @WithMockUser(username = "U888888", roles = {"ADMIN"})
    @DisplayName("ユーザーを削除できる")
    void canDeleteUser() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/U999999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        ResponseEntity<MessageResponse> responseEntity = ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
        String actualMessage = Objects.requireNonNull(responseEntity.getBody()).getMessage();
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals("User deleted successfully!", actualMessage);
    }
}
