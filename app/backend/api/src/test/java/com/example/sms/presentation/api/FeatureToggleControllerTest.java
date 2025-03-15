package com.example.sms.presentation.api;

import com.example.sms.FeatureToggleProperties;
import com.example.sms.PresentationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("フィーチャートグルAPI")
@PresentationTest
@AutoConfigureMybatis
class FeatureToggleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeatureToggleProperties featureToggleProperties;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("フィーチャートグル一覧取得")
    void getFeatureTogglesTest() throws Exception {
        // MockされたFeatureTogglePropertiesの振る舞いを設定
        when(featureToggleProperties.isNewFeature()).thenReturn(true);
        when(featureToggleProperties.isBetaFeature()).thenReturn(false);
        when(featureToggleProperties.isLegacyFeature()).thenReturn(true);

        // APIリクエストを実行
        mockMvc.perform(MockMvcRequestBuilders.get("/api/features"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.newFeature").value(true))
                .andExpect(jsonPath("$.betaFeature").value(false))
                .andExpect(jsonPath("$.legacyFeature").value(true));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("新機能トグル: ON→OFF")
    void toggleFeatureNewFeatureTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/features/newFeature/toggle")
                        .param("enabled", "false"))
                .andExpect(status().isOk())
                .andExpect(content().string("Feature newFeature set to false"));

        // ログや動作の確認のため、configが呼び出されるかを検証
        Mockito.verify(featureToggleProperties).setNewFeature(false);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("存在しないフィーチャー指定")
    void toggleFeatureNotFoundTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/features/unknownFeature/toggle")
                        .param("enabled", "true"))
                .andExpect(status().isOk())
                .andExpect(content().string("Feature not found: unknownFeature"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("アクセス拒否")
    void accessDeniedTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/features"))
                .andExpect(status().isForbidden());
    }
}