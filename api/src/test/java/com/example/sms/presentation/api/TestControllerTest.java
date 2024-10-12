package com.example.sms.presentation.api;

import com.example.sms.PresentationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("JWT認可API")
@PresentationTest
@AutoConfigureMybatis
public class TestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("未登録者アクセス")
    public void accessAsUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/test/all"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Public Content"));
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("ユーザーアクセス")
    public void accessAsUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/test/user"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("User Content"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("管理者アクセス")
    public void accessAsAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/test/admin"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Admin Board."));
    }
}
