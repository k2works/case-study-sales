package com.example.sms.service.master.locationnumber;

import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.master.locationnumber.LocationNumber;
import com.example.sms.domain.model.master.locationnumber.LocationNumberList;
import com.example.sms.infrastructure.datasource.autogen.model.棚番マスタKey;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("棚番サービス")
public class LocationNumberServiceTest {
    @Mock
    LocationNumberRepository locationNumberRepository;

    @InjectMocks
    LocationNumberService locationNumberService;

    @Nested
    @DisplayName("棚番管理")
    class LocationNumberManagementTests {
        @Test
        @DisplayName("棚番一覧を取得できる")
        void getAllLocationNumbers() {
            // Given
            LocationNumber locationNumber = TestDataFactoryImpl.getLocationNumber("W01", "A001", "P001");
            LocationNumberList expectedList = new LocationNumberList(Arrays.asList(locationNumber));
            when(locationNumberRepository.selectAll()).thenReturn(expectedList);

            // When
            LocationNumberList result = locationNumberService.selectAll();

            // Then
            assertEquals(1, result.asList().size());
            verify(locationNumberRepository).selectAll();
        }

        @Test
        @DisplayName("棚番を新規登録できる")
        void registerNewLocationNumber() {
            // Given
            LocationNumber newLocationNumber = TestDataFactoryImpl.getLocationNumber("W02", "B002", "P002");

            // When
            locationNumberService.register(newLocationNumber);

            // Then
            verify(locationNumberRepository).save(newLocationNumber);
        }

        @Test
        @DisplayName("棚番の登録情報を編集できる")
        void editLocationNumberDetails() {
            // Given
            LocationNumber updateLocationNumber = LocationNumber.of("W01", "A001", "P001");

            // When
            locationNumberService.save(updateLocationNumber);

            // Then
            verify(locationNumberRepository).save(updateLocationNumber);
        }

        @Test
        @DisplayName("棚番を削除できる")
        void deleteLocationNumber() {
            // Given
            棚番マスタKey key = TestDataFactoryImpl.getLocationNumberKey("W01", "A001", "P001");

            // When
            locationNumberService.delete(key);

            // Then
            verify(locationNumberRepository).deleteById(key);
        }

        @Test
        @DisplayName("棚番を検索できる")
        void findLocationNumber() {
            // Given
            LocationNumber locationNumber = TestDataFactoryImpl.getLocationNumber("W02", "B002", "P002");
            棚番マスタKey key = TestDataFactoryImpl.getLocationNumberKey("W02", "B002", "P002");
            when(locationNumberRepository.findById(key)).thenReturn(Optional.of(locationNumber));

            // When
            LocationNumber result = locationNumberService.find(key);

            // Then
            assertEquals(locationNumber, result);
            verify(locationNumberRepository).findById(key);
        }

        @Test
        @DisplayName("存在しない棚番を検索した場合nullが返る")
        void findNonExistentLocationNumber() {
            // Given
            棚番マスタKey nonExistentKey = TestDataFactoryImpl.getLocationNumberKey("W99", "Z999", "P999");
            when(locationNumberRepository.findById(nonExistentKey)).thenReturn(Optional.empty());

            // When
            LocationNumber result = locationNumberService.find(nonExistentKey);

            // Then
            assertNull(result);
            verify(locationNumberRepository).findById(nonExistentKey);
        }

        @Test
        @DisplayName("倉庫コードで棚番を検索できる")
        void findLocationNumbersByWarehouseCode() {
            // Given
            LocationNumber locationNumber = TestDataFactoryImpl.getLocationNumber("W01", "A001", "P001");
            LocationNumberList expectedList = new LocationNumberList(Arrays.asList(locationNumber));
            when(locationNumberRepository.findByWarehouseCode("W01")).thenReturn(expectedList);

            // When
            LocationNumberList result = locationNumberService.findByWarehouseCode("W01");

            // Then
            assertEquals(1, result.asList().size());
            verify(locationNumberRepository).findByWarehouseCode("W01");
        }

        @Test
        @DisplayName("棚番コードで棚番を検索できる")
        void findLocationNumbersByLocationNumberCode() {
            // Given
            LocationNumber locationNumber = TestDataFactoryImpl.getLocationNumber("W01", "A001", "P001");
            LocationNumberList expectedList = new LocationNumberList(Arrays.asList(locationNumber));
            when(locationNumberRepository.findByLocationNumberCode("A001")).thenReturn(expectedList);

            // When
            LocationNumberList result = locationNumberService.findByLocationNumberCode("A001");

            // Then
            assertEquals(1, result.asList().size());
            verify(locationNumberRepository).findByLocationNumberCode("A001");
        }

        @Test
        @DisplayName("検索条件で棚番を検索できる")
        void searchLocationNumbersWithCriteria() {
            // Given
            LocationNumber locationNumber = TestDataFactoryImpl.getLocationNumber("W01", "A001", "P001");
            LocationNumberCriteria criteria = new LocationNumberCriteria("W01", null, null);
            PageInfo<LocationNumber> expectedPageInfo = new PageInfo<>(Arrays.asList(locationNumber));
            when(locationNumberRepository.searchWithPageInfo(criteria)).thenReturn(expectedPageInfo);

            // When
            PageInfo<LocationNumber> result = locationNumberService.searchWithPageInfo(criteria);

            // Then
            assertEquals(1, result.getList().size());
            verify(locationNumberRepository).searchWithPageInfo(criteria);
        }
    }
}