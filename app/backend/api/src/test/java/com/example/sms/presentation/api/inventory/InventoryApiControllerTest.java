package com.example.sms.presentation.api.inventory;

import com.example.sms.PresentationTest;
import com.example.sms.domain.model.inventory.Inventory;
import com.example.sms.domain.model.inventory.InventoryKey;
import com.example.sms.domain.model.inventory.rule.InventoryRuleCheckList;
import com.example.sms.service.inventory.InventoryCriteria;
import com.example.sms.service.inventory.InventoryService;
import com.example.sms.service.inventory.InventoryUploadErrorList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("在庫API")
@PresentationTest
@AutoConfigureMybatis
public class InventoryApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService inventoryService;

    @MockBean
    private com.example.sms.service.PageNationService pageNationService;

    @MockBean
    private com.example.sms.presentation.Message message;

    @Autowired
    private ObjectMapper objectMapper;

    private Inventory testInventory;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        testInventory = Inventory.of(
                "WH001",
                "PR001",
                "LOT001",
                "1",
                "G",
                100,
                90,
                now
        );

        // メッセージのモック設定
        when(message.getMessage(anyString())).thenReturn("テストメッセージ");

        // ルールチェックのモック設定
        InventoryRuleCheckList mockRuleCheckList = new InventoryRuleCheckList(List.of());
        when(inventoryService.checkRule()).thenReturn(mockRuleCheckList);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("管理者が在庫一覧を取得できる")
    void shouldRetrieveInventoryListAsAdmin() throws Exception {
        PageInfo<Inventory> mockPageInfo = new PageInfo<>(List.of(testInventory));
        when(inventoryService.findAllWithPageInfo()).thenReturn(mockPageInfo);

        mockMvc.perform(get("/api/inventory")
                .param("pageSize", "10")
                .param("page", "1"))
                .andExpect(status().isOk());

        verify(inventoryService).findAllWithPageInfo();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("管理者が在庫詳細を取得できる")
    void shouldRetrieveInventoryAsAdmin() throws Exception {
        when(inventoryService.findByKey(any(InventoryKey.class))).thenReturn(Optional.of(testInventory));

        mockMvc.perform(get("/api/inventory/WH001/PR001/LOT001/1/G"))
                .andExpect(status().isOk());

        verify(inventoryService).findByKey(any(InventoryKey.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("管理者が在庫を登録できる")
    void shouldCreateInventoryAsAdmin() throws Exception {
        when(inventoryService.register(any(Inventory.class))).thenReturn(testInventory);

        InventoryResource resource = new InventoryResource();
        resource.setWarehouseCode("WH001");
        resource.setProductCode("PR001");
        resource.setLotNumber("LOT001");
        resource.setStockCategory("1");
        resource.setQualityCategory("G");

        mockMvc.perform(post("/api/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk());

        verify(inventoryService).register(any(Inventory.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("管理者が在庫を更新できる")
    void shouldUpdateInventoryAsAdmin() throws Exception {
        // 既存在庫の存在をモック
        when(inventoryService.findByKey(any(InventoryKey.class))).thenReturn(Optional.of(testInventory));
        when(inventoryService.update(any(Inventory.class))).thenReturn(testInventory);

        InventoryResource resource = new InventoryResource();
        resource.setWarehouseCode("WH001");
        resource.setProductCode("PR001");
        resource.setLotNumber("LOT001");
        resource.setStockCategory("1");
        resource.setQualityCategory("G");

        mockMvc.perform(put("/api/inventory/WH001/PR001/LOT001/1/G")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk());

        verify(inventoryService).findByKey(any(InventoryKey.class));
        verify(inventoryService).update(any(Inventory.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("管理者が在庫を削除できる")
    void shouldDeleteInventoryAsAdmin() throws Exception {
        // 既存在庫の存在をモック
        when(inventoryService.findByKey(any(InventoryKey.class))).thenReturn(Optional.of(testInventory));
        doNothing().when(inventoryService).delete(any(InventoryKey.class));

        mockMvc.perform(delete("/api/inventory/WH001/PR001/LOT001/1/G"))
                .andExpect(status().isOk());

        verify(inventoryService).findByKey(any(InventoryKey.class));
        verify(inventoryService).delete(any(InventoryKey.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("管理者が在庫検索できる")
    void shouldSearchInventoryAsAdmin() throws Exception {
        PageInfo<Inventory> searchPageInfo = new PageInfo<>(List.of(testInventory));
        when(inventoryService.searchWithPageInfo(any(InventoryCriteria.class))).thenReturn(searchPageInfo);

        InventoryCriteriaResource criteria = new InventoryCriteriaResource();
        criteria.setWarehouseCode("WH001");

        mockMvc.perform(post("/api/inventory/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(criteria))
                .param("pageSize", "10")
                .param("page", "1"))
                .andExpect(status().isOk());

        verify(inventoryService).searchWithPageInfo(any(InventoryCriteria.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("管理者がCSVファイルをアップロードできる")
    void shouldUploadInventoryAsAdmin() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "inventory.csv",
                "text/csv",
                "warehouseCode,productCode,lotNumber,stockCategory,qualityCategory,quantity,availableQuantity\nWH001,PR001,LOT001,1,G,100,90".getBytes()
        );

        InventoryUploadErrorList errorList = new InventoryUploadErrorList(List.of());
        when(inventoryService.uploadCsvFile(any())).thenReturn(errorList);

        mockMvc.perform(multipart("/api/inventory/upload")
                .file(file))
                .andExpect(status().isOk());

        verify(inventoryService).uploadCsvFile(any());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("管理者が在庫ルールチェックを実行できる")
    void shouldCheckInventoryRuleAsAdmin() throws Exception {
        mockMvc.perform(post("/api/inventory/check")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());

        verify(inventoryService).checkRule();
    }
}