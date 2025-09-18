package com.example.sms.presentation.api.master.locationnumber;

import com.example.sms.PresentationTest;
import com.example.sms.domain.model.master.locationnumber.LocationNumber;
import com.example.sms.domain.model.master.locationnumber.LocationNumberKey;
import com.example.sms.domain.model.master.locationnumber.LocationNumberList;
import com.example.sms.service.master.locationnumber.LocationNumberService;
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

@DisplayName("棚番API")
@PresentationTest
@AutoConfigureMybatis
public class LocationNumberApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationNumberService locationNumberService;

    @MockBean
    private com.example.sms.service.PageNationService pageNationService;

    @MockBean
    private com.example.sms.presentation.Message message;

    @Autowired
    private ObjectMapper objectMapper;

    private LocationNumber testLocationNumber;
    private LocationNumberResource testLocationNumberResource;
    private LocationNumberKey testKey;

    @BeforeEach
    void setUp() {
        testLocationNumber = LocationNumber.of("W01", "A001", "P001");
        testLocationNumberResource = LocationNumberResource.from(testLocationNumber);

        testKey = LocationNumberKey.of("W01", "A001", "P001");

        // PageInfoのモック設定
        PageInfo<LocationNumber> mockPageInfo = new PageInfo<>(List.of(testLocationNumber));
        when(locationNumberService.selectAllWithPageInfo()).thenReturn(mockPageInfo);

        PageInfo<LocationNumberResource> mockResourcePageInfo = new PageInfo<>(List.of(testLocationNumberResource));
        when(pageNationService.getPageInfo(any(PageInfo.class), any())).thenReturn(mockResourcePageInfo);

        // メッセージのモック設定
        when(message.getMessage(anyString())).thenReturn("テストメッセージ");
    }

    @Nested
    @DisplayName("棚番一覧取得")
    class SelectAllTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が棚番一覧を取得できる")
        void shouldRetrieveLocationNumberListAsAdmin() throws Exception {
            mockMvc.perform(get("/api/locationnumbers")
                    .param("pageSize", "10")
                    .param("page", "1"))
                    .andExpect(status().isOk());

            verify(locationNumberService).selectAllWithPageInfo();
        }

        @Test
        @WithMockUser(username = "user")
        @DisplayName("利用者が棚番一覧を取得できる")
        void shouldRetrieveLocationNumberListAsUser() throws Exception {
            mockMvc.perform(get("/api/locationnumbers")
                    .param("pageSize", "10")
                    .param("page", "1"))
                    .andExpect(status().isOk());

            verify(locationNumberService).selectAllWithPageInfo();
        }
    }

    @Nested
    @DisplayName("棚番詳細取得")
    class SelectTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が棚番詳細を取得できる")
        void shouldRetrieveLocationNumberAsAdmin() throws Exception {
            when(locationNumberService.find(any(LocationNumberKey.class))).thenReturn(testLocationNumber);

            mockMvc.perform(get("/api/locationnumbers/W01/A001/P001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.warehouseCode").value("W01"))
                    .andExpect(jsonPath("$.locationNumberCode").value("A001"))
                    .andExpect(jsonPath("$.productCode").value("P001"));

            verify(locationNumberService).find(any(LocationNumberKey.class));
        }

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("存在しない棚番を取得しようとした場合400エラーが返る")
        void shouldReturn400WhenLocationNumberNotFound() throws Exception {
            when(locationNumberService.find(any(LocationNumberKey.class))).thenReturn(null);

            mockMvc.perform(get("/api/locationnumbers/W99/Z999/P999"))
                    .andExpect(status().isBadRequest());

            verify(locationNumberService).find(any(LocationNumberKey.class));
        }
    }

    @Nested
    @DisplayName("棚番登録")
    class CreateTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が棚番を登録できる")
        void shouldCreateLocationNumberAsAdmin() throws Exception {
            when(locationNumberService.find(any(LocationNumberKey.class))).thenReturn(null);

            mockMvc.perform(post("/api/locationnumbers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testLocationNumberResource)))
                    .andExpect(status().isOk());

            verify(locationNumberService).register(any(LocationNumber.class));
        }

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("既存の棚番を登録しようとした場合400エラーが返る")
        void shouldReturn400WhenLocationNumberAlreadyExists() throws Exception {
            when(locationNumberService.find(any(LocationNumberKey.class))).thenReturn(testLocationNumber);

            mockMvc.perform(post("/api/locationnumbers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testLocationNumberResource)))
                    .andExpect(status().isBadRequest());

            verify(locationNumberService, never()).register(any(LocationNumber.class));
        }
    }

    @Nested
    @DisplayName("棚番更新")
    class UpdateTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が棚番を更新できる")
        void shouldUpdateLocationNumberAsAdmin() throws Exception {
            mockMvc.perform(put("/api/locationnumbers/W01/A001/P001")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testLocationNumberResource)))
                    .andExpect(status().isOk());

            verify(locationNumberService).save(any(LocationNumber.class));
        }
    }

    @Nested
    @DisplayName("棚番削除")
    class DeleteTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が棚番を削除できる")
        void shouldDeleteLocationNumberAsAdmin() throws Exception {
            when(locationNumberService.find(any(LocationNumberKey.class))).thenReturn(testLocationNumber);

            mockMvc.perform(delete("/api/locationnumbers/W01/A001/P001"))
                    .andExpect(status().isOk());

            verify(locationNumberService).delete(any(LocationNumberKey.class));
        }

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("存在しない棚番を削除しようとした場合400エラーが返る")
        void shouldReturn400WhenLocationNumberNotFoundForDelete() throws Exception {
            when(locationNumberService.find(any(LocationNumberKey.class))).thenReturn(null);

            mockMvc.perform(delete("/api/locationnumbers/W99/Z999/P999"))
                    .andExpect(status().isBadRequest());

            verify(locationNumberService, never()).delete(any(LocationNumberKey.class));
        }
    }

    @Nested
    @DisplayName("棚番検索")
    class SearchTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が条件で棚番を検索できる")
        void shouldSearchLocationNumbersAsAdmin() throws Exception {
            LocationNumberCriteriaResource criteria = new LocationNumberCriteriaResource();
            criteria.setWarehouseCode("W01");

            PageInfo<LocationNumber> mockSearchPageInfo = new PageInfo<>(List.of(testLocationNumber));
            when(locationNumberService.searchWithPageInfo(any())).thenReturn(mockSearchPageInfo);

            mockMvc.perform(post("/api/locationnumbers/search")
                    .param("pageSize", "10")
                    .param("page", "1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(criteria)))
                    .andExpect(status().isOk());

            verify(locationNumberService).searchWithPageInfo(any());
        }
    }

    @Nested
    @DisplayName("倉庫コード検索")
    class FindByWarehouseCodeTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が倉庫コードで棚番を検索できる")
        void shouldFindLocationNumbersByWarehouseCodeAsAdmin() throws Exception {
            LocationNumberList locationNumbers = new LocationNumberList(List.of(testLocationNumber));
            when(locationNumberService.findByWarehouseCode("W01")).thenReturn(locationNumbers);

            mockMvc.perform(get("/api/locationnumbers/by-warehouse/W01"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].warehouseCode").value("W01"));

            verify(locationNumberService).findByWarehouseCode("W01");
        }

        @Test
        @WithMockUser(username = "user")
        @DisplayName("利用者が倉庫コードで棚番を検索できる")
        void shouldFindLocationNumbersByWarehouseCodeAsUser() throws Exception {
            LocationNumberList locationNumbers = new LocationNumberList(List.of(testLocationNumber));
            when(locationNumberService.findByWarehouseCode("W01")).thenReturn(locationNumbers);

            mockMvc.perform(get("/api/locationnumbers/by-warehouse/W01"))
                    .andExpect(status().isOk());

            verify(locationNumberService).findByWarehouseCode("W01");
        }
    }

    @Nested
    @DisplayName("棚番コード検索")
    class FindByLocationNumberCodeTest {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("管理者が棚番コードで棚番を検索できる")
        void shouldFindLocationNumbersByLocationNumberCodeAsAdmin() throws Exception {
            LocationNumberList locationNumbers = new LocationNumberList(List.of(testLocationNumber));
            when(locationNumberService.findByLocationNumberCode("A001")).thenReturn(locationNumbers);

            mockMvc.perform(get("/api/locationnumbers/by-location/A001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].locationNumberCode").value("A001"));

            verify(locationNumberService).findByLocationNumberCode("A001");
        }

        @Test
        @WithMockUser(username = "user")
        @DisplayName("利用者が棚番コードで棚番を検索できる")
        void shouldFindLocationNumbersByLocationNumberCodeAsUser() throws Exception {
            LocationNumberList locationNumbers = new LocationNumberList(List.of(testLocationNumber));
            when(locationNumberService.findByLocationNumberCode("A001")).thenReturn(locationNumbers);

            mockMvc.perform(get("/api/locationnumbers/by-location/A001"))
                    .andExpect(status().isOk());

            verify(locationNumberService).findByLocationNumberCode("A001");
        }
    }
}