package com.example.sms.presentation.api.master.warehouse;

import com.example.sms.PresentationTest;
import com.example.sms.domain.model.master.warehouse.Warehouse;
import com.example.sms.domain.model.master.warehouse.WarehouseCode;
import com.example.sms.service.master.warehouse.WarehouseService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("倉庫API")
@PresentationTest
@AutoConfigureMybatis
public class WarehouseApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WarehouseService warehouseService;

    @MockBean
    private com.example.sms.service.PageNationService pageNationService;

    @MockBean
    private com.example.sms.presentation.Message message;

    @Autowired
    private ObjectMapper objectMapper;

    private Warehouse testWarehouse;
    private WarehouseResource testWarehouseResource;

    @BeforeEach
    void setUp() {
        testWarehouse = Warehouse.of(WarehouseCode.of("W01"), "本社倉庫");
        testWarehouseResource = WarehouseResource.from(testWarehouse);

        // PageInfoのモック設定
        PageInfo<Warehouse> mockPageInfo = new PageInfo<>(List.of(testWarehouse));
        when(warehouseService.selectAllWithPageInfo()).thenReturn(mockPageInfo);

        PageInfo<WarehouseResource> mockResourcePageInfo = new PageInfo<>(List.of(testWarehouseResource));
        when(pageNationService.getPageInfo(any(PageInfo.class), any())).thenReturn(mockResourcePageInfo);

        // メッセージのモック設定
        when(message.getMessage(anyString())).thenReturn("テストメッセージ");
    }

    @Nested
    @DisplayName("倉庫一覧取得")
    class SelectAllTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が倉庫一覧を取得できる")
        void shouldRetrieveWarehouseListAsAdmin() throws Exception {
            mockMvc.perform(get("/api/warehouses")
                    .param("pageSize", "10")
                    .param("page", "1"))
                    .andExpect(status().isOk());

            verify(warehouseService).selectAllWithPageInfo();
        }

        @Test
        @WithMockUser(username = "user")
        @DisplayName("利用者が倉庫一覧を取得できる")
        void shouldRetrieveWarehouseListAsUser() throws Exception {
            mockMvc.perform(get("/api/warehouses")
                    .param("pageSize", "10")
                    .param("page", "1"))
                    .andExpect(status().isOk());

            verify(warehouseService).selectAllWithPageInfo();
        }
    }

    @Nested
    @DisplayName("倉庫詳細取得")
    class SelectTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が倉庫詳細を取得できる")
        void shouldRetrieveWarehouseAsAdmin() throws Exception {
            when(warehouseService.find(WarehouseCode.of("W01"))).thenReturn(testWarehouse);

            mockMvc.perform(get("/api/warehouses/W01"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.warehouseCode").value("W01"))
                    .andExpect(jsonPath("$.warehouseName").value("本社倉庫"));

            verify(warehouseService).find(WarehouseCode.of("W01"));
        }

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("存在しない倉庫を取得しようとした場合400エラーが返る")
        void shouldReturn400WhenWarehouseNotFound() throws Exception {
            when(warehouseService.find(WarehouseCode.of("W99"))).thenReturn(null);

            mockMvc.perform(get("/api/warehouses/W99"))
                    .andExpect(status().isBadRequest());

            verify(warehouseService).find(WarehouseCode.of("W99"));
        }
    }

    @Nested
    @DisplayName("倉庫登録")
    class CreateTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が倉庫を登録できる")
        void shouldCreateWarehouseAsAdmin() throws Exception {
            when(warehouseService.find(any(WarehouseCode.class))).thenReturn(null);

            mockMvc.perform(post("/api/warehouses")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testWarehouseResource)))
                    .andExpect(status().isOk());

            verify(warehouseService).register(any(Warehouse.class));
        }

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("既存の倉庫を登録しようとした場合400エラーが返る")
        void shouldReturn400WhenWarehouseAlreadyExists() throws Exception {
            when(warehouseService.find(any(WarehouseCode.class))).thenReturn(testWarehouse);

            mockMvc.perform(post("/api/warehouses")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testWarehouseResource)))
                    .andExpect(status().isBadRequest());

            verify(warehouseService, never()).register(any(Warehouse.class));
        }
    }

    @Nested
    @DisplayName("倉庫更新")
    class UpdateTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が倉庫を更新できる")
        void shouldUpdateWarehouseAsAdmin() throws Exception {
            testWarehouseResource.setWarehouseName("更新された倉庫名");

            mockMvc.perform(put("/api/warehouses/W01")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testWarehouseResource)))
                    .andExpect(status().isOk());

            verify(warehouseService).save(any(Warehouse.class));
        }
    }

    @Nested
    @DisplayName("倉庫削除")
    class DeleteTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が倉庫を削除できる")
        void shouldDeleteWarehouseAsAdmin() throws Exception {
            when(warehouseService.find(WarehouseCode.of("W01"))).thenReturn(testWarehouse);

            mockMvc.perform(delete("/api/warehouses/W01"))
                    .andExpect(status().isOk());

            verify(warehouseService).delete(WarehouseCode.of("W01"));
        }

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("存在しない倉庫を削除しようとした場合400エラーが返る")
        void shouldReturn400WhenWarehouseNotFoundForDelete() throws Exception {
            when(warehouseService.find(WarehouseCode.of("W99"))).thenReturn(null);

            mockMvc.perform(delete("/api/warehouses/W99"))
                    .andExpect(status().isBadRequest());

            verify(warehouseService, never()).delete(any(WarehouseCode.class));
        }
    }

    @Nested
    @DisplayName("倉庫検索")
    class SearchTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が条件で倉庫を検索できる")
        void shouldSearchWarehousesAsAdmin() throws Exception {
            WarehouseCriteriaResource criteria = new WarehouseCriteriaResource();
            criteria.setWarehouseName("本社");

            PageInfo<Warehouse> mockSearchPageInfo = new PageInfo<>(List.of(testWarehouse));
            when(warehouseService.searchWithPageInfo(any())).thenReturn(mockSearchPageInfo);

            mockMvc.perform(post("/api/warehouses/search")
                    .param("pageSize", "10")
                    .param("page", "1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(criteria)))
                    .andExpect(status().isOk());

            verify(warehouseService).searchWithPageInfo(any());
        }
    }
}