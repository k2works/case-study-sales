package com.example.sms.service.master.warehouse;

import com.example.sms.TestDataFactoryImpl;
import com.example.sms.domain.model.master.warehouse.Warehouse;
import com.example.sms.domain.model.master.warehouse.WarehouseCode;
import com.example.sms.domain.model.master.warehouse.WarehouseList;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("倉庫サービス")
public class WarehouseServiceTest {
    @Mock
    WarehouseRepository warehouseRepository;

    @InjectMocks
    WarehouseService warehouseService;

    @Nested
    @DisplayName("倉庫管理")
    class WarehouseManagementTests {
        @Test
        @DisplayName("倉庫一覧を取得できる")
        void getAllWarehouses() {
            // Given
            Warehouse warehouse = TestDataFactoryImpl.getWarehouse("W01", "本社倉庫");
            WarehouseList expectedList = new WarehouseList(Arrays.asList(warehouse));
            when(warehouseRepository.selectAll()).thenReturn(expectedList);

            // When
            WarehouseList result = warehouseService.selectAll();

            // Then
            assertEquals(1, result.asList().size());
            verify(warehouseRepository).selectAll();
        }

        @Test
        @DisplayName("倉庫を新規登録できる")
        void registerNewWarehouse() {
            // Given
            Warehouse newWarehouse = TestDataFactoryImpl.getWarehouse("W02", "支社倉庫");

            // When
            warehouseService.register(newWarehouse);

            // Then
            verify(warehouseRepository).save(newWarehouse);
        }

        @Test
        @DisplayName("倉庫の登録情報を編集できる")
        void editWarehouseDetails() {
            // Given
            Warehouse updateWarehouse = Warehouse.of("W01", "編集された倉庫名", "N", "1234567", "東京都", "千代田区", "1-1-1");

            // When
            warehouseService.save(updateWarehouse);

            // Then
            verify(warehouseRepository).save(updateWarehouse);
        }

        @Test
        @DisplayName("倉庫を削除できる")
        void deleteWarehouse() {
            // Given
            WarehouseCode warehouseCode = WarehouseCode.of("W02");

            // When
            warehouseService.delete(warehouseCode);

            // Then
            verify(warehouseRepository).deleteById(warehouseCode);
        }

        @Test
        @DisplayName("倉庫を検索できる")
        void findWarehouse() {
            // Given
            Warehouse warehouse = TestDataFactoryImpl.getWarehouse("W02", "支社倉庫");
            when(warehouseRepository.findById(warehouse.getWarehouseCode())).thenReturn(Optional.of(warehouse));

            // When
            Warehouse result = warehouseService.find(warehouse.getWarehouseCode());

            // Then
            assertEquals(warehouse, result);
            verify(warehouseRepository).findById(warehouse.getWarehouseCode());
        }

        @Test
        @DisplayName("存在しない倉庫を検索した場合nullが返る")
        void findNonExistentWarehouse() {
            // Given
            WarehouseCode nonExistentCode = WarehouseCode.of("W99");
            when(warehouseRepository.findById(nonExistentCode)).thenReturn(Optional.empty());

            // When
            Warehouse result = warehouseService.find(nonExistentCode);

            // Then
            assertNull(result);
            verify(warehouseRepository).findById(nonExistentCode);
        }
    }
}