package com.example.sms.service.inventory;

import com.example.sms.domain.model.inventory.Inventory;
import com.example.sms.domain.model.inventory.InventoryKey;
import com.example.sms.domain.model.inventory.InventoryList;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("在庫サービス")
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    private Inventory getInventory() {
        return Inventory.of(
                "001",
                "10101001", 
                "LOT001",
                "1",
                "1",
                100,
                90,
                LocalDateTime.now()
        );
    }

    private InventoryKey getInventoryKey() {
        return InventoryKey.of("001", "10101001", "LOT001", "1", "1");
    }

    @Nested
    @DisplayName("登録")
    class RegisterTest {

        @Test
        @DisplayName("在庫を登録できる")
        void shouldRegisterInventory() {
            Inventory inventory = getInventory();
            when(inventoryRepository.findByKey(inventory.getKey())).thenReturn(Optional.empty());
            when(inventoryRepository.save(inventory)).thenReturn(inventory);

            Inventory result = inventoryService.register(inventory);

            assertNotNull(result);
            assertEquals(inventory.getKey(), result.getKey());
            verify(inventoryRepository).findByKey(inventory.getKey());
            verify(inventoryRepository).save(inventory);
        }

        @Test
        @DisplayName("既存の在庫キーで登録すると例外が発生する")
        void shouldThrowExceptionWhenRegisteringExistingKey() {
            Inventory inventory = getInventory();
            when(inventoryRepository.findByKey(inventory.getKey())).thenReturn(Optional.of(inventory));

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> inventoryService.register(inventory)
            );

            assertTrue(exception.getMessage().contains("既に存在する在庫キーです"));
            verify(inventoryRepository).findByKey(inventory.getKey());
            verify(inventoryRepository, never()).save(any());
        }

        @Test
        @DisplayName("null在庫で登録すると例外が発生する")
        void shouldThrowExceptionWhenRegisteringNullInventory() {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> inventoryService.register(null)
            );

            assertEquals("在庫情報は必須です", exception.getMessage());
            verify(inventoryRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("更新")
    class UpdateTest {

        @Test
        @DisplayName("在庫を更新できる")
        void shouldUpdateInventory() {
            Inventory inventory = getInventory();
            when(inventoryRepository.findByKey(inventory.getKey())).thenReturn(Optional.of(inventory));
            when(inventoryRepository.save(inventory)).thenReturn(inventory);

            Inventory result = inventoryService.update(inventory);

            assertNotNull(result);
            assertEquals(inventory.getKey(), result.getKey());
            verify(inventoryRepository).findByKey(inventory.getKey());
            verify(inventoryRepository).save(inventory);
        }

        @Test
        @DisplayName("存在しない在庫を更新すると例外が発生する")
        void shouldThrowExceptionWhenUpdatingNonExistentInventory() {
            Inventory inventory = getInventory();
            when(inventoryRepository.findByKey(inventory.getKey())).thenReturn(Optional.empty());

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> inventoryService.update(inventory)
            );

            assertTrue(exception.getMessage().contains("更新対象の在庫が見つかりません"));
            verify(inventoryRepository).findByKey(inventory.getKey());
            verify(inventoryRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("検索")
    class SearchTest {

        @Test
        @DisplayName("在庫キーで検索できる")
        void shouldFindByKey() {
            InventoryKey key = getInventoryKey();
            Inventory inventory = getInventory();
            when(inventoryRepository.findByKey(key)).thenReturn(Optional.of(inventory));

            Optional<Inventory> result = inventoryService.findByKey(key);

            assertTrue(result.isPresent());
            assertEquals(inventory.getKey(), result.get().getKey());
            verify(inventoryRepository).findByKey(key);
        }

        @Test
        @DisplayName("存在しないキーで検索すると空を返す")
        void shouldReturnEmptyWhenKeyNotFound() {
            InventoryKey key = getInventoryKey();
            when(inventoryRepository.findByKey(key)).thenReturn(Optional.empty());

            Optional<Inventory> result = inventoryService.findByKey(key);

            assertFalse(result.isPresent());
            verify(inventoryRepository).findByKey(key);
        }

        @Test
        @DisplayName("全在庫を取得できる")
        void shouldFindAll() {
            InventoryList inventoryList = InventoryList.of(List.of(getInventory()));
            when(inventoryRepository.selectAll()).thenReturn(inventoryList);

            InventoryList result = inventoryService.findAll();

            assertNotNull(result);
            assertEquals(1, result.size());
            verify(inventoryRepository).selectAll();
        }

        @Test
        @DisplayName("全在庫をページ情報付きで取得できる")
        void shouldFindAllWithPageInfo() {
            PageInfo<Inventory> pageInfo = new PageInfo<>(List.of(getInventory()));
            when(inventoryRepository.selectAllWithPageInfo()).thenReturn(pageInfo);

            PageInfo<Inventory> result = inventoryService.findAllWithPageInfo();

            assertNotNull(result);
            assertEquals(1, result.getList().size());
            verify(inventoryRepository).selectAllWithPageInfo();
        }

        @Test
        @DisplayName("条件で在庫を検索できる")
        void shouldSearchByCriteria() {
            InventoryCriteria criteria = InventoryCriteria.byWarehouseCode("001");
            InventoryList inventoryList = InventoryList.of(List.of(getInventory()));
            when(inventoryRepository.searchByCriteria(criteria)).thenReturn(inventoryList);

            InventoryList result = inventoryService.searchByCriteria(criteria);

            assertNotNull(result);
            assertEquals(1, result.size());
            verify(inventoryRepository).searchByCriteria(criteria);
        }

        @Test
        @DisplayName("条件で在庫をページ情報付きで検索できる")
        void shouldSearchWithPageInfo() {
            InventoryCriteria criteria = InventoryCriteria.byProductCode("10101001");
            PageInfo<Inventory> pageInfo = new PageInfo<>(List.of(getInventory()));
            when(inventoryRepository.searchWithPageInfo(criteria)).thenReturn(pageInfo);

            PageInfo<Inventory> result = inventoryService.searchWithPageInfo(criteria);

            assertNotNull(result);
            assertEquals(1, result.getList().size());
            verify(inventoryRepository).searchWithPageInfo(criteria);
        }
    }

    @Nested
    @DisplayName("在庫操作")
    class StockOperationTest {

        @Test
        @DisplayName("在庫を調整できる")
        void shouldAdjustStock() {
            InventoryKey key = getInventoryKey();
            Inventory inventory = getInventory();
            Inventory adjusted = inventory.adjustStock(50);
            
            when(inventoryRepository.findByKey(key)).thenReturn(Optional.of(inventory));
            when(inventoryRepository.save(adjusted)).thenReturn(adjusted);

            Inventory result = inventoryService.adjustStock(key, 50);

            assertNotNull(result);
            assertEquals(150, result.getActualStockQuantity()); // 100 + 50
            assertEquals(140, result.getAvailableStockQuantity()); // 90 + 50
            verify(inventoryRepository).findByKey(key);
            verify(inventoryRepository).save(adjusted);
        }

        @Test
        @DisplayName("在庫を予約できる")
        void shouldReserveStock() {
            InventoryKey key = getInventoryKey();
            Inventory inventory = getInventory();
            Inventory reserved = inventory.reserve(30);
            
            when(inventoryRepository.findByKey(key)).thenReturn(Optional.of(inventory));
            when(inventoryRepository.save(reserved)).thenReturn(reserved);

            Inventory result = inventoryService.reserveStock(key, 30);

            assertNotNull(result);
            assertEquals(100, result.getActualStockQuantity()); // 変化なし
            assertEquals(60, result.getAvailableStockQuantity()); // 90 - 30
            verify(inventoryRepository).findByKey(key);
            verify(inventoryRepository).save(reserved);
        }

        @Test
        @DisplayName("在庫を出荷できる")
        void shouldShipStock() {
            InventoryKey key = getInventoryKey();
            Inventory inventory = getInventory();
            LocalDateTime shipmentDate = LocalDateTime.now();
            Inventory shipped = inventory.ship(20, shipmentDate);
            
            when(inventoryRepository.findByKey(key)).thenReturn(Optional.of(inventory));
            when(inventoryRepository.save(shipped)).thenReturn(shipped);

            Inventory result = inventoryService.shipStock(key, 20, shipmentDate);

            assertNotNull(result);
            assertEquals(80, result.getActualStockQuantity()); // 100 - 20
            assertEquals(80, result.getAvailableStockQuantity()); // Math.min(90, 80) = 80
            assertEquals(shipmentDate, result.getLastShipmentDate());
            verify(inventoryRepository).findByKey(key);
            verify(inventoryRepository).save(shipped);
        }

        @Test
        @DisplayName("在庫を入荷できる")
        void shouldReceiveStock() {
            InventoryKey key = getInventoryKey();
            Inventory inventory = getInventory();
            Inventory received = inventory.receive(40);
            
            when(inventoryRepository.findByKey(key)).thenReturn(Optional.of(inventory));
            when(inventoryRepository.save(received)).thenReturn(received);

            Inventory result = inventoryService.receiveStock(key, 40);

            assertNotNull(result);
            assertEquals(140, result.getActualStockQuantity()); // 100 + 40
            assertEquals(130, result.getAvailableStockQuantity()); // 90 + 40
            verify(inventoryRepository).findByKey(key);
            verify(inventoryRepository).save(received);
        }

        @Test
        @DisplayName("存在しない在庫を調整すると例外が発生する")
        void shouldThrowExceptionWhenAdjustingNonExistentStock() {
            InventoryKey key = getInventoryKey();
            when(inventoryRepository.findByKey(key)).thenReturn(Optional.empty());

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> inventoryService.adjustStock(key, 50)
            );

            assertTrue(exception.getMessage().contains("在庫が見つかりません"));
            verify(inventoryRepository).findByKey(key);
            verify(inventoryRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("削除")
    class DeleteTest {

        @Test
        @DisplayName("在庫を削除できる")
        void shouldDeleteInventory() {
            InventoryKey key = getInventoryKey();
            Inventory inventory = getInventory();
            when(inventoryRepository.findByKey(key)).thenReturn(Optional.of(inventory));

            inventoryService.delete(key);

            verify(inventoryRepository).findByKey(key);
            verify(inventoryRepository).delete(key);
        }

        @Test
        @DisplayName("存在しない在庫を削除すると例外が発生する")
        void shouldThrowExceptionWhenDeletingNonExistentInventory() {
            InventoryKey key = getInventoryKey();
            when(inventoryRepository.findByKey(key)).thenReturn(Optional.empty());

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> inventoryService.delete(key)
            );

            assertTrue(exception.getMessage().contains("削除対象の在庫が見つかりません"));
            verify(inventoryRepository).findByKey(key);
            verify(inventoryRepository, never()).delete(any());
        }

        @Test
        @DisplayName("全在庫を削除できる")
        void shouldDeleteAllInventory() {
            inventoryService.deleteAll();

            verify(inventoryRepository).deleteAll();
        }
    }
}