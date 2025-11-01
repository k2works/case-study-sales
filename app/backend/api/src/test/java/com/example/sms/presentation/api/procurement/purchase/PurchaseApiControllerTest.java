package com.example.sms.presentation.api.procurement.purchase;

import com.example.sms.PresentationTest;
import com.example.sms.domain.model.procurement.purchase.Purchase;
import com.example.sms.domain.model.procurement.purchase.PurchaseLine;
import com.example.sms.domain.model.procurement.purchase.rule.PurchaseRuleCheckList;
import com.example.sms.service.procurement.purchase.PurchaseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("仕入API")
@PresentationTest
@AutoConfigureMybatis
public class PurchaseApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PurchaseService purchaseService;

    @MockBean
    private com.example.sms.service.PageNationService pageNationService;

    @MockBean
    private com.example.sms.presentation.Message message;

    @Autowired
    private ObjectMapper objectMapper;

    private Purchase testPurchase;
    private PurchaseResource testPurchaseResource;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        List<PurchaseLine> lines = List.of(
            PurchaseLine.of(
                "PU25010001",
                1,
                1,
                1,
                "10101001",
                "W01",
                "商品1",
                1000,
                10
            )
        );

        testPurchase = Purchase.of(
            "PU25010001",
            now,
            "001",
            0,
            "EMP001",
            now,
            "PO25010001",
            "10001",
            10000,
            1000,
            "備考",
            lines
        );

        testPurchaseResource = PurchaseResource.from(testPurchase);

        // PageInfoのモック設定
        PageInfo<Purchase> mockPageInfo = new PageInfo<>(List.of(testPurchase));
        when(purchaseService.selectAllWithPageInfo()).thenReturn(mockPageInfo);

        PageInfo<PurchaseResource> mockResourcePageInfo = new PageInfo<>(List.of(testPurchaseResource));
        when(pageNationService.getPageInfo(any(PageInfo.class), any())).thenReturn(mockResourcePageInfo);

        // メッセージのモック設定
        when(message.getMessage(anyString())).thenReturn("テストメッセージ");

        // ルールチェックのモック設定
        PurchaseRuleCheckList mockRuleCheckList = new PurchaseRuleCheckList(List.of());
        when(purchaseService.checkRule()).thenReturn(mockRuleCheckList);
    }

    @Nested
    @DisplayName("仕入一覧取得")
    class SelectAllTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が仕入一覧を取得できる")
        void shouldRetrievePurchaseListAsAdmin() throws Exception {
            mockMvc.perform(get("/api/purchases")
                    .param("pageSize", "10")
                    .param("page", "1"))
                    .andExpect(status().isOk());

            verify(purchaseService).selectAllWithPageInfo();
        }

        @Test
        @WithMockUser(username = "user")
        @DisplayName("利用者が仕入一覧を取得できる")
        void shouldRetrievePurchaseListAsUser() throws Exception {
            mockMvc.perform(get("/api/purchases")
                    .param("pageSize", "10")
                    .param("page", "1"))
                    .andExpect(status().isOk());

            verify(purchaseService).selectAllWithPageInfo();
        }
    }

    @Nested
    @DisplayName("仕入詳細取得")
    class SelectTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が仕入詳細を取得できる")
        void shouldRetrievePurchaseAsAdmin() throws Exception {
            when(purchaseService.find("PU25010001")).thenReturn(testPurchase);

            mockMvc.perform(get("/api/purchases/PU25010001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.purchaseNumber").value("PU25010001"));

            verify(purchaseService).find("PU25010001");
        }

        @Test
        @WithMockUser(username = "user")
        @DisplayName("利用者が仕入詳細を取得できる")
        void shouldRetrievePurchaseAsUser() throws Exception {
            when(purchaseService.find("PU25010001")).thenReturn(testPurchase);

            mockMvc.perform(get("/api/purchases/PU25010001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.purchaseNumber").value("PU25010001"));

            verify(purchaseService).find("PU25010001");
        }

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("存在しない仕入番号でエラーが返される")
        void shouldReturnErrorWhenPurchaseNotFound() throws Exception {
            when(purchaseService.find("PU99999999")).thenReturn(null);

            mockMvc.perform(get("/api/purchases/PU99999999"))
                    .andExpect(status().isBadRequest());

            verify(purchaseService).find("PU99999999");
        }
    }

    @Nested
    @DisplayName("仕入登録")
    class CreateTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が仕入を登録できる")
        void shouldCreatePurchaseAsAdmin() throws Exception {
            when(purchaseService.find(anyString())).thenReturn(null);
            doNothing().when(purchaseService).register(any(Purchase.class));

            mockMvc.perform(post("/api/purchases")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testPurchaseResource)))
                    .andExpect(status().isOk());

            verify(purchaseService).register(any(Purchase.class));
        }

        @Test
        @WithMockUser(username = "user")
        @DisplayName("利用者が仕入を登録できる")
        void shouldCreatePurchaseAsUser() throws Exception {
            when(purchaseService.find(anyString())).thenReturn(null);
            doNothing().when(purchaseService).register(any(Purchase.class));

            mockMvc.perform(post("/api/purchases")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testPurchaseResource)))
                    .andExpect(status().isOk());

            verify(purchaseService).register(any(Purchase.class));
        }
    }

    @Nested
    @DisplayName("仕入更新")
    class UpdateTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が仕入を更新できる")
        void shouldUpdatePurchaseAsAdmin() throws Exception {
            when(purchaseService.find("PU25010001")).thenReturn(testPurchase);
            doNothing().when(purchaseService).save(any(Purchase.class));

            mockMvc.perform(put("/api/purchases/PU25010001")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testPurchaseResource)))
                    .andExpect(status().isOk());

            verify(purchaseService).save(any(Purchase.class));
        }

        @Test
        @WithMockUser(username = "user")
        @DisplayName("利用者が仕入を更新できる")
        void shouldUpdatePurchaseAsUser() throws Exception {
            when(purchaseService.find("PU25010001")).thenReturn(testPurchase);
            doNothing().when(purchaseService).save(any(Purchase.class));

            mockMvc.perform(put("/api/purchases/PU25010001")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testPurchaseResource)))
                    .andExpect(status().isOk());

            verify(purchaseService).save(any(Purchase.class));
        }
    }

    @Nested
    @DisplayName("仕入削除")
    class DeleteTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が仕入を削除できる")
        void shouldDeletePurchaseAsAdmin() throws Exception {
            when(purchaseService.find("PU25010001")).thenReturn(testPurchase);
            doNothing().when(purchaseService).delete(any(Purchase.class));

            mockMvc.perform(delete("/api/purchases/PU25010001"))
                    .andExpect(status().isOk());

            verify(purchaseService).delete(any(Purchase.class));
        }

        @Test
        @WithMockUser(username = "user")
        @DisplayName("利用者が仕入を削除できる")
        void shouldDeletePurchaseAsUser() throws Exception {
            when(purchaseService.find("PU25010001")).thenReturn(testPurchase);
            doNothing().when(purchaseService).delete(any(Purchase.class));

            mockMvc.perform(delete("/api/purchases/PU25010001"))
                    .andExpect(status().isOk());

            verify(purchaseService).delete(any(Purchase.class));
        }
    }

    @Nested
    @DisplayName("仕入検索")
    class SearchTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が仕入を検索できる")
        void shouldSearchPurchasesAsAdmin() throws Exception {
            PurchaseCriteriaResource criteria = new PurchaseCriteriaResource();
            criteria.setPurchaseNumber("PU25010001");

            mockMvc.perform(post("/api/purchases/search")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(criteria))
                    .param("pageSize", "10")
                    .param("page", "1"))
                    .andExpect(status().isOk());

            verify(purchaseService).searchPurchaseWithPageInfo(any());
        }

        @Test
        @WithMockUser(username = "user")
        @DisplayName("利用者が仕入を検索できる")
        void shouldSearchPurchasesAsUser() throws Exception {
            PurchaseCriteriaResource criteria = new PurchaseCriteriaResource();
            criteria.setPurchaseNumber("PU25010001");

            mockMvc.perform(post("/api/purchases/search")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(criteria))
                    .param("pageSize", "10")
                    .param("page", "1"))
                    .andExpect(status().isOk());

            verify(purchaseService).searchPurchaseWithPageInfo(any());
        }
    }

    @Nested
    @DisplayName("仕入ルールチェック")
    class CheckRuleTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が仕入ルールチェックを実行できる")
        void shouldCheckPurchaseRulesAsAdmin() throws Exception {
            mockMvc.perform(post("/api/purchases/check"))
                    .andExpect(status().isOk());

            verify(purchaseService).checkRule();
        }

        @Test
        @WithMockUser(username = "user")
        @DisplayName("利用者が仕入ルールチェックを実行できる")
        void shouldCheckPurchaseRulesAsUser() throws Exception {
            mockMvc.perform(post("/api/purchases/check"))
                    .andExpect(status().isOk());

            verify(purchaseService).checkRule();
        }
    }
}
