package com.example.sms.presentation.api.procurement.order;

import com.example.sms.PresentationTest;
import com.example.sms.domain.model.procurement.order.PurchaseOrder;
import com.example.sms.domain.model.procurement.order.PurchaseOrderLine;
import com.example.sms.domain.model.procurement.order.rule.PurchaseOrderRuleCheckList;
import com.example.sms.service.procurement.order.PurchaseOrderService;
import com.example.sms.service.procurement.order.PurchaseOrderUploadErrorList;
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
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.mock.web.MockMultipartFile;

@DisplayName("発注API")
@PresentationTest
@AutoConfigureMybatis
public class PurchaseOrderApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PurchaseOrderService purchaseOrderService;

    @MockBean
    private com.example.sms.service.PageNationService pageNationService;

    @MockBean
    private com.example.sms.presentation.Message message;

    @Autowired
    private ObjectMapper objectMapper;

    private PurchaseOrder testPurchaseOrder;
    private PurchaseOrderResource testPurchaseOrderResource;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        
        List<PurchaseOrderLine> lines = List.of(
            PurchaseOrderLine.of(
                "PO25010001",
                1,
                1,
                "OD25010001",
                1,
                "10101001",
                "商品1",
                1000,
                10,
                0,
                0
            )
        );

        testPurchaseOrder = PurchaseOrder.of(
            "PO25010001",
            now,
            "OD25010001",
            "001",
            0,
            "EMP001",
            now.plusDays(7),
            "W01",
            10000,
            1000,
            "備考",
            lines
        );

        testPurchaseOrderResource = PurchaseOrderResource.from(testPurchaseOrder);

        // PageInfoのモック設定
        PageInfo<PurchaseOrder> mockPageInfo = new PageInfo<>(List.of(testPurchaseOrder));
        when(purchaseOrderService.selectAllWithPageInfo()).thenReturn(mockPageInfo);
        
        PageInfo<PurchaseOrderResource> mockResourcePageInfo = new PageInfo<>(List.of(testPurchaseOrderResource));
        when(pageNationService.getPageInfo(any(PageInfo.class), any())).thenReturn(mockResourcePageInfo);
        
        // メッセージのモック設定
        when(message.getMessage(anyString())).thenReturn("テストメッセージ");
        
        // ルールチェックのモック設定
        PurchaseOrderRuleCheckList mockRuleCheckList = new PurchaseOrderRuleCheckList(List.of());
        when(purchaseOrderService.checkRule()).thenReturn(mockRuleCheckList);
    }

    @Nested
    @DisplayName("発注一覧取得")
    class SelectAllTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が発注一覧を取得できる")
        void shouldRetrievePurchaseOrderListAsAdmin() throws Exception {
            mockMvc.perform(get("/api/purchase-orders")
                    .param("pageSize", "10")
                    .param("page", "1"))
                    .andExpect(status().isOk());

            verify(purchaseOrderService).selectAllWithPageInfo();
        }

        @Test
        @WithMockUser(username = "user")
        @DisplayName("利用者が発注一覧を取得できる")
        void shouldRetrievePurchaseOrderListAsUser() throws Exception {
            mockMvc.perform(get("/api/purchase-orders")
                    .param("pageSize", "10")
                    .param("page", "1"))
                    .andExpect(status().isOk());

            verify(purchaseOrderService).selectAllWithPageInfo();
        }
    }

    @Nested
    @DisplayName("発注詳細取得")
    class SelectTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が発注詳細を取得できる")
        void shouldRetrievePurchaseOrderAsAdmin() throws Exception {
            when(purchaseOrderService.find("PO25010001")).thenReturn(testPurchaseOrder);

            mockMvc.perform(get("/api/purchase-orders/PO25010001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.purchaseOrderNumber").value("PO25010001"));

            verify(purchaseOrderService).find("PO25010001");
        }

        @Test
        @WithMockUser(username = "user")
        @DisplayName("利用者が発注詳細を取得できる")
        void shouldRetrievePurchaseOrderAsUser() throws Exception {
            when(purchaseOrderService.find("PO25010001")).thenReturn(testPurchaseOrder);

            mockMvc.perform(get("/api/purchase-orders/PO25010001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.purchaseOrderNumber").value("PO25010001"));

            verify(purchaseOrderService).find("PO25010001");
        }

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("存在しない発注番号でエラーが返される")
        void shouldReturnErrorWhenPurchaseOrderNotFound() throws Exception {
            when(purchaseOrderService.find("PO99999999")).thenReturn(null);

            mockMvc.perform(get("/api/purchase-orders/PO99999999"))
                    .andExpect(status().isBadRequest());

            verify(purchaseOrderService).find("PO99999999");
        }
    }

    @Nested
    @DisplayName("発注登録")
    class CreateTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が発注を登録できる")
        void shouldCreatePurchaseOrderAsAdmin() throws Exception {
            when(purchaseOrderService.find(anyString())).thenReturn(null);
            doNothing().when(purchaseOrderService).register(any(PurchaseOrder.class));

            mockMvc.perform(post("/api/purchase-orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testPurchaseOrderResource)))
                    .andExpect(status().isOk());

            verify(purchaseOrderService).register(any(PurchaseOrder.class));
        }

        @Test
        @WithMockUser(username = "user")
        @DisplayName("利用者が発注を登録できる")
        void shouldCreatePurchaseOrderAsUser() throws Exception {
            when(purchaseOrderService.find(anyString())).thenReturn(null);
            doNothing().when(purchaseOrderService).register(any(PurchaseOrder.class));

            mockMvc.perform(post("/api/purchase-orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testPurchaseOrderResource)))
                    .andExpect(status().isOk());

            verify(purchaseOrderService).register(any(PurchaseOrder.class));
        }
    }

    @Nested
    @DisplayName("発注更新")
    class UpdateTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が発注を更新できる")
        void shouldUpdatePurchaseOrderAsAdmin() throws Exception {
            when(purchaseOrderService.find("PO25010001")).thenReturn(testPurchaseOrder);
            doNothing().when(purchaseOrderService).save(any(PurchaseOrder.class));

            mockMvc.perform(put("/api/purchase-orders/PO25010001")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testPurchaseOrderResource)))
                    .andExpect(status().isOk());

            verify(purchaseOrderService).save(any(PurchaseOrder.class));
        }

        @Test
        @WithMockUser(username = "user")
        @DisplayName("利用者が発注を更新できる")
        void shouldUpdatePurchaseOrderAsUser() throws Exception {
            when(purchaseOrderService.find("PO25010001")).thenReturn(testPurchaseOrder);
            doNothing().when(purchaseOrderService).save(any(PurchaseOrder.class));

            mockMvc.perform(put("/api/purchase-orders/PO25010001")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testPurchaseOrderResource)))
                    .andExpect(status().isOk());

            verify(purchaseOrderService).save(any(PurchaseOrder.class));
        }
    }

    @Nested
    @DisplayName("発注削除")
    class DeleteTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が発注を削除できる")
        void shouldDeletePurchaseOrderAsAdmin() throws Exception {
            when(purchaseOrderService.find("PO25010001")).thenReturn(testPurchaseOrder);
            doNothing().when(purchaseOrderService).delete("PO25010001");

            mockMvc.perform(delete("/api/purchase-orders/PO25010001"))
                    .andExpect(status().isOk());

            verify(purchaseOrderService).delete("PO25010001");
        }

        @Test
        @WithMockUser(username = "user")
        @DisplayName("利用者が発注を削除できる")
        void shouldDeletePurchaseOrderAsUser() throws Exception {
            when(purchaseOrderService.find("PO25010001")).thenReturn(testPurchaseOrder);
            doNothing().when(purchaseOrderService).delete("PO25010001");

            mockMvc.perform(delete("/api/purchase-orders/PO25010001"))
                    .andExpect(status().isOk());

            verify(purchaseOrderService).delete("PO25010001");
        }
    }

    @Nested
    @DisplayName("発注検索")
    class SearchTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が発注を検索できる")
        void shouldSearchPurchaseOrdersAsAdmin() throws Exception {
            PurchaseOrderCriteriaResource criteria = new PurchaseOrderCriteriaResource();
            criteria.setPurchaseOrderNumber("PO25010001");

            mockMvc.perform(post("/api/purchase-orders/search")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(criteria))
                    .param("pageSize", "10")
                    .param("page", "1"))
                    .andExpect(status().isOk());

            verify(purchaseOrderService).searchPurchaseOrderWithPageInfo(any());
        }

        @Test
        @WithMockUser(username = "user")
        @DisplayName("利用者が発注を検索できる")
        void shouldSearchPurchaseOrdersAsUser() throws Exception {
            PurchaseOrderCriteriaResource criteria = new PurchaseOrderCriteriaResource();
            criteria.setPurchaseOrderNumber("PO25010001");

            mockMvc.perform(post("/api/purchase-orders/search")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(criteria))
                    .param("pageSize", "10")
                    .param("page", "1"))
                    .andExpect(status().isOk());

            verify(purchaseOrderService).searchPurchaseOrderWithPageInfo(any());
        }
    }

    @Nested
    @DisplayName("発注ルールチェック")
    class CheckRuleTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が発注ルールチェックを実行できる")
        void shouldCheckPurchaseOrderRulesAsAdmin() throws Exception {
            mockMvc.perform(post("/api/purchase-orders/check"))
                    .andExpect(status().isOk());

            verify(purchaseOrderService).checkRule();
        }

        @Test
        @WithMockUser(username = "user")
        @DisplayName("利用者が発注ルールチェックを実行できる")
        void shouldCheckPurchaseOrderRulesAsUser() throws Exception {
            mockMvc.perform(post("/api/purchase-orders/check"))
                    .andExpect(status().isOk());

            verify(purchaseOrderService).checkRule();
        }
    }

    @Nested
    @DisplayName("発注アップロード")
    class UploadTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が発注をアップロードできる")
        void shouldUploadPurchaseOrdersAsAdmin() throws Exception {
            // Arrange
            String csvContent = "発注番号,発注日,売上注文番号,仕入先コード,仕入先支店番号,発注担当者コード,指定納期,倉庫コード,発注金額合計,消費税合計,備考,発注行番号,商品コード,商品名,仕入単価,発注数量,消費税率,入荷予定数量,入荷済数量,完了フラグ,値引金額,納期,入荷日\n"
                            + "PO00000001,2023-01-01,,SUPP001,1,EMP001,2023-01-10,WH001,10000,1000,備考,1,PROD001,商品1,1000,10,10,0,0,0,0,2023-01-10,";
            MockMultipartFile file = new MockMultipartFile(
                "file",
                "purchase_orders.csv",
                "text/csv",
                csvContent.getBytes()
            );

            PurchaseOrderUploadErrorList mockResult = new PurchaseOrderUploadErrorList();
            when(purchaseOrderService.uploadCsvFile(any())).thenReturn(mockResult);
            when(message.getMessage("success.purchase.order.upload")).thenReturn("発注アップロードが成功しました");

            // Act & Assert
            mockMvc.perform(multipart("/api/purchase-orders/upload")
                    .file(file))
                    .andExpect(status().isOk());

            verify(purchaseOrderService).uploadCsvFile(any());
        }

        @Test
        @WithMockUser(username = "user")
        @DisplayName("利用者が発注をアップロードできる")
        void shouldUploadPurchaseOrdersAsUser() throws Exception {
            // Arrange
            String csvContent = "発注番号,発注日,売上注文番号,仕入先コード,仕入先支店番号,発注担当者コード,指定納期,倉庫コード,発注金額合計,消費税合計,備考,発注行番号,商品コード,商品名,仕入単価,発注数量,消費税率,入荷予定数量,入荷済数量,完了フラグ,値引金額,納期,入荷日\n"
                            + "PO00000001,2023-01-01,,SUPP001,1,EMP001,2023-01-10,WH001,10000,1000,備考,1,PROD001,商品1,1000,10,10,0,0,0,0,2023-01-10,";
            MockMultipartFile file = new MockMultipartFile(
                "file",
                "purchase_orders.csv",
                "text/csv",
                csvContent.getBytes()
            );

            PurchaseOrderUploadErrorList mockResult = new PurchaseOrderUploadErrorList();
            when(purchaseOrderService.uploadCsvFile(any())).thenReturn(mockResult);
            when(message.getMessage("success.purchase.order.upload")).thenReturn("発注アップロードが成功しました");

            // Act & Assert
            mockMvc.perform(multipart("/api/purchase-orders/upload")
                    .file(file))
                    .andExpect(status().isOk());

            verify(purchaseOrderService).uploadCsvFile(any());
        }

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("バリデーションエラーがある場合はエラーレスポンスを返す")
        void shouldReturnErrorResponseWhenValidationFails() throws Exception {
            // Arrange
            String csvContent = "発注番号,発注日,売上注文番号,仕入先コード,仕入先支店番号,発注担当者コード,指定納期,倉庫コード,発注金額合計,消費税合計,備考,発注行番号,商品コード,商品名,仕入単価,発注数量,消費税率,入荷予定数量,入荷済数量,完了フラグ,値引金額,納期,入荷日\n"
                            + "PO00000001,2023-01-01,,SUPP999,1,EMP001,2023-01-10,WH001,10000,1000,備考,1,PROD999,商品1,1000,10,10,0,0,0,0,2023-01-10,";
            MockMultipartFile file = new MockMultipartFile(
                "file",
                "purchase_orders.csv",
                "text/csv",
                csvContent.getBytes()
            );

            PurchaseOrderUploadErrorList mockResult = new PurchaseOrderUploadErrorList();
            mockResult = mockResult.add(Map.of("PO00000001", "仕入先マスタに存在しません:SUPP999"));
            when(purchaseOrderService.uploadCsvFile(any())).thenReturn(mockResult);
            when(message.getMessage("error.purchase.order.upload")).thenReturn("発注アップロードでエラーが発生しました");

            // Act & Assert
            mockMvc.perform(multipart("/api/purchase-orders/upload")
                    .file(file))
                    .andExpect(status().isOk());

            verify(purchaseOrderService).uploadCsvFile(any());
        }

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("ランタイム例外が発生した場合はエラーレスポンスを返す")
        void shouldReturnErrorResponseWhenRuntimeExceptionOccurs() throws Exception {
            // Arrange
            MockMultipartFile file = new MockMultipartFile(
                "file",
                "purchase_orders.csv",
                "text/csv",
                "invalid csv content".getBytes()
            );

            when(purchaseOrderService.uploadCsvFile(any())).thenThrow(new RuntimeException("CSVファイルの読み込みに失敗しました"));

            // Act & Assert
            mockMvc.perform(multipart("/api/purchase-orders/upload")
                    .file(file))
                    .andExpect(status().isBadRequest());

            verify(purchaseOrderService).uploadCsvFile(any());
        }
    }
}