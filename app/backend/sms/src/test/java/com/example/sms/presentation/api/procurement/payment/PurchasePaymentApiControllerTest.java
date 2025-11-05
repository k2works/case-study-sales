package com.example.sms.presentation.api.procurement.payment;

import com.example.sms.PresentationTest;
import com.example.sms.domain.model.procurement.payment.PurchasePayment;
import com.example.sms.service.procurement.payment.PurchasePaymentService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("支払API")
@PresentationTest
@AutoConfigureMybatis
public class PurchasePaymentApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PurchasePaymentService purchasePaymentService;

    @MockBean
    private com.example.sms.service.PageNationService pageNationService;

    @MockBean
    private com.example.sms.presentation.Message message;

    @Autowired
    private ObjectMapper objectMapper;

    private PurchasePayment testPurchasePayment;
    private PurchasePaymentResource testPurchasePaymentResource;

    @BeforeEach
    void setUp() {
        LocalDateTime departmentStartDate = LocalDateTime.of(2021, 1, 1, 0, 0);

        testPurchasePayment = PurchasePayment.of(
            "PAY0000001",
            20231001,
            "10000",
            departmentStartDate,
            "001",
            1,
            1,
            100000,
            10000,
            false
        );

        testPurchasePaymentResource = PurchasePaymentResource.from(testPurchasePayment);

        // PageInfoのモック設定
        PageInfo<PurchasePayment> mockPageInfo = new PageInfo<>(List.of(testPurchasePayment));
        when(purchasePaymentService.selectAllWithPageInfo()).thenReturn(mockPageInfo);

        PageInfo<PurchasePaymentResource> mockResourcePageInfo = new PageInfo<>(List.of(testPurchasePaymentResource));
        when(pageNationService.getPageInfo(any(PageInfo.class), any())).thenReturn(mockResourcePageInfo);

        // メッセージのモック設定
        when(message.getMessage(anyString())).thenReturn("テストメッセージ");
    }

    @Nested
    @DisplayName("支払一覧取得")
    class SelectAllTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が支払一覧を取得できる")
        void shouldRetrievePaymentListAsAdmin() throws Exception {
            mockMvc.perform(get("/api/purchase-payments")
                    .param("pageSize", "10")
                    .param("page", "1"))
                    .andExpect(status().isOk());

            verify(purchasePaymentService).selectAllWithPageInfo();
        }

        @Test
        @WithMockUser(username = "user")
        @DisplayName("利用者が支払一覧を取得できる")
        void shouldRetrievePaymentListAsUser() throws Exception {
            mockMvc.perform(get("/api/purchase-payments")
                    .param("pageSize", "10")
                    .param("page", "1"))
                    .andExpect(status().isOk());

            verify(purchasePaymentService).selectAllWithPageInfo();
        }
    }

    @Nested
    @DisplayName("支払詳細取得")
    class SelectTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が支払詳細を取得できる")
        void shouldRetrievePaymentAsAdmin() throws Exception {
            when(purchasePaymentService.findByPaymentNumber("PAY0000001")).thenReturn(Optional.of(testPurchasePayment));

            mockMvc.perform(get("/api/purchase-payments/PAY0000001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.paymentNumber").value("PAY0000001"));

            verify(purchasePaymentService).findByPaymentNumber("PAY0000001");
        }

        @Test
        @WithMockUser(username = "user")
        @DisplayName("利用者が支払詳細を取得できる")
        void shouldRetrievePaymentAsUser() throws Exception {
            when(purchasePaymentService.findByPaymentNumber("PAY0000001")).thenReturn(Optional.of(testPurchasePayment));

            mockMvc.perform(get("/api/purchase-payments/PAY0000001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.paymentNumber").value("PAY0000001"));

            verify(purchasePaymentService).findByPaymentNumber("PAY0000001");
        }

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("存在しない支払番号でエラーが返される")
        void shouldReturnErrorWhenPaymentNotFound() throws Exception {
            when(purchasePaymentService.findByPaymentNumber("PAY9999999")).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/purchase-payments/PAY9999999"))
                    .andExpect(status().isNotFound());

            verify(purchasePaymentService).findByPaymentNumber("PAY9999999");
        }
    }

    @Nested
    @DisplayName("支払登録")
    class CreateTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が支払を登録できる")
        void shouldCreatePaymentAsAdmin() throws Exception {
            when(purchasePaymentService.findByPaymentNumber(anyString())).thenReturn(Optional.empty());
            doNothing().when(purchasePaymentService).register(any(PurchasePayment.class));

            mockMvc.perform(post("/api/purchase-payments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testPurchasePaymentResource)))
                    .andExpect(status().isOk());

            verify(purchasePaymentService).register(any(PurchasePayment.class));
        }

        @Test
        @WithMockUser(username = "user")
        @DisplayName("利用者が支払を登録できる")
        void shouldCreatePaymentAsUser() throws Exception {
            when(purchasePaymentService.findByPaymentNumber(anyString())).thenReturn(Optional.empty());
            doNothing().when(purchasePaymentService).register(any(PurchasePayment.class));

            mockMvc.perform(post("/api/purchase-payments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testPurchasePaymentResource)))
                    .andExpect(status().isOk());

            verify(purchasePaymentService).register(any(PurchasePayment.class));
        }

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("既存の支払を登録しようとするとエラーが返される")
        void shouldReturnErrorWhenPaymentAlreadyExists() throws Exception {
            when(purchasePaymentService.findByPaymentNumber(anyString())).thenReturn(Optional.of(testPurchasePayment));

            mockMvc.perform(post("/api/purchase-payments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testPurchasePaymentResource)))
                    .andExpect(status().isBadRequest());

            verify(purchasePaymentService, never()).register(any(PurchasePayment.class));
        }
    }

    @Nested
    @DisplayName("支払更新")
    class UpdateTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が支払を更新できる")
        void shouldUpdatePaymentAsAdmin() throws Exception {
            doNothing().when(purchasePaymentService).save(any(PurchasePayment.class));

            mockMvc.perform(put("/api/purchase-payments/PAY0000001")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testPurchasePaymentResource)))
                    .andExpect(status().isOk());

            verify(purchasePaymentService).save(any(PurchasePayment.class));
        }

        @Test
        @WithMockUser(username = "user")
        @DisplayName("利用者が支払を更新できる")
        void shouldUpdatePaymentAsUser() throws Exception {
            doNothing().when(purchasePaymentService).save(any(PurchasePayment.class));

            mockMvc.perform(put("/api/purchase-payments/PAY0000001")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testPurchasePaymentResource)))
                    .andExpect(status().isOk());

            verify(purchasePaymentService).save(any(PurchasePayment.class));
        }
    }

    @Nested
    @DisplayName("支払削除")
    class DeleteTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が支払を削除できる")
        void shouldDeletePaymentAsAdmin() throws Exception {
            when(purchasePaymentService.findByPaymentNumber("PAY0000001")).thenReturn(Optional.of(testPurchasePayment));
            doNothing().when(purchasePaymentService).delete(anyString());

            mockMvc.perform(delete("/api/purchase-payments/PAY0000001"))
                    .andExpect(status().isOk());

            verify(purchasePaymentService).delete("PAY0000001");
        }

        @Test
        @WithMockUser(username = "user")
        @DisplayName("利用者が支払を削除できる")
        void shouldDeletePaymentAsUser() throws Exception {
            when(purchasePaymentService.findByPaymentNumber("PAY0000001")).thenReturn(Optional.of(testPurchasePayment));
            doNothing().when(purchasePaymentService).delete(anyString());

            mockMvc.perform(delete("/api/purchase-payments/PAY0000001"))
                    .andExpect(status().isOk());

            verify(purchasePaymentService).delete("PAY0000001");
        }

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("存在しない支払を削除しようとするとエラーが返される")
        void shouldReturnErrorWhenDeletingNonExistentPayment() throws Exception {
            when(purchasePaymentService.findByPaymentNumber("PAY9999999")).thenReturn(Optional.empty());

            mockMvc.perform(delete("/api/purchase-payments/PAY9999999"))
                    .andExpect(status().isBadRequest());

            verify(purchasePaymentService, never()).delete(anyString());
        }
    }

    @Nested
    @DisplayName("支払検索")
    class SearchTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が支払を検索できる")
        void shouldSearchPaymentsAsAdmin() throws Exception {
            PurchasePaymentCriteriaResource criteria = new PurchasePaymentCriteriaResource();
            criteria.setPaymentNumber("PAY0000001");

            mockMvc.perform(post("/api/purchase-payments/search")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(criteria))
                    .param("pageSize", "10")
                    .param("page", "1"))
                    .andExpect(status().isOk());

            verify(purchasePaymentService).searchWithPageInfo(any());
        }

        @Test
        @WithMockUser(username = "user")
        @DisplayName("利用者が支払を検索できる")
        void shouldSearchPaymentsAsUser() throws Exception {
            PurchasePaymentCriteriaResource criteria = new PurchasePaymentCriteriaResource();
            criteria.setPaymentNumber("PAY0000001");

            mockMvc.perform(post("/api/purchase-payments/search")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(criteria))
                    .param("pageSize", "10")
                    .param("page", "1"))
                    .andExpect(status().isOk());

            verify(purchasePaymentService).searchWithPageInfo(any());
        }
    }

    @Nested
    @DisplayName("支払集計")
    class AggregateTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が支払を集計できる")
        void shouldAggregatePaymentsAsAdmin() throws Exception {
            PurchasePaymentCriteriaResource criteria = new PurchasePaymentCriteriaResource();
            doNothing().when(purchasePaymentService).aggregate();

            mockMvc.perform(post("/api/purchase-payments/aggregate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(criteria)))
                    .andExpect(status().isOk());

            verify(purchasePaymentService).aggregate();
        }

        @Test
        @WithMockUser(username = "user")
        @DisplayName("利用者が支払を集計できる")
        void shouldAggregatePaymentsAsUser() throws Exception {
            PurchasePaymentCriteriaResource criteria = new PurchasePaymentCriteriaResource();
            doNothing().when(purchasePaymentService).aggregate();

            mockMvc.perform(post("/api/purchase-payments/aggregate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(criteria)))
                    .andExpect(status().isOk());

            verify(purchasePaymentService).aggregate();
        }
    }
}
