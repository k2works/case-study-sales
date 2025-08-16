package com.example.sms.service.sales.purchase.order;

import com.example.sms.domain.model.master.employee.EmployeeCode;
import com.example.sms.domain.model.master.partner.supplier.SupplierCode;
import com.example.sms.domain.model.sales.purchase.order.*;
import com.example.sms.domain.model.sales.purchase.order.rule.PurchaseOrderRuleCheckList;
import com.example.sms.domain.service.sales.purchase.order.PurchaseOrderDomainService;
import com.example.sms.domain.type.money.Money;
import com.example.sms.service.system.autonumber.AutoNumberService;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("発注サービステスト")
class PurchaseOrderServiceTest {

    @Mock
    private PurchaseOrderRepository purchaseOrderRepository;
    
    @Mock
    private AutoNumberService autoNumberService;
    
    @Mock
    private PurchaseOrderDomainService purchaseOrderDomainService;
    
    private PurchaseOrderService purchaseOrderService;

    @BeforeEach
    void setUp() {
        purchaseOrderService = new PurchaseOrderService(
            purchaseOrderRepository, 
            autoNumberService, 
            purchaseOrderDomainService
        );
    }

    private PurchaseOrder createTestPurchaseOrder(String purchaseOrderNumber) {
        LocalDateTime now = LocalDateTime.now();
        
        List<PurchaseOrderLine> lines = List.of(
            PurchaseOrderLine.of(
                purchaseOrderNumber,
                1,
                1,
                "SO000001",
                1,
                "10101001",
                "商品1",
                1000,
                10,
                0,
                0
            )
        );

        return PurchaseOrder.of(
            purchaseOrderNumber,
            now,
            "SO000001",
            "001", // 仕入先コード
            0,
            "EMP001", // 社員コード
            now.plusDays(7),
            "001", // 倉庫コード
            10000,
            1000, // 消費税
            "備考",
            lines
        );
    }

    private PurchaseOrder createTestPurchaseOrderWithoutNumber() {
        LocalDateTime now = LocalDateTime.now();
        
        // 発注番号なしの場合はbuilderを使用
        List<PurchaseOrderLine> lines = List.of();

        return PurchaseOrder.builder()
                .purchaseOrderNumber(null) // 発注番号なし
                .purchaseOrderDate(PurchaseOrderDate.of(now))
                .salesOrderNumber("SO000001")
                .supplierCode(SupplierCode.of("001"))
                .supplierBranchNumber(0)
                .purchaseManagerCode(EmployeeCode.of("EMP001"))
                .designatedDeliveryDate(DesignatedDeliveryDate.of(now.plusDays(7)))
                .warehouseCode("001")
                .totalPurchaseAmount(Money.of(10000))
                .totalConsumptionTax(Money.of(1000))
                .remarks("備考")
                .purchaseOrderLines(lines)
                .build();
    }

    @Nested
    @DisplayName("発注一覧取得")
    class SelectAllTest {
        
        @Test
        @DisplayName("発注一覧を取得できる")
        void shouldRetrieveAllPurchaseOrders() {
            // Arrange
            PurchaseOrderList expectedList = PurchaseOrderList.of(List.of(
                createTestPurchaseOrder("PO00000001"),
                createTestPurchaseOrder("PO00000002")
            ));
            when(purchaseOrderRepository.selectAll()).thenReturn(expectedList);

            // Act
            PurchaseOrderList result = purchaseOrderService.selectAll();

            // Assert
            assertEquals(2, result.asList().size());
            verify(purchaseOrderRepository).selectAll();
        }

        @Test
        @DisplayName("発注一覧をPageInfoで取得できる")
        void shouldRetrieveAllPurchaseOrdersWithPageInfo() {
            // Arrange
            PageInfo<PurchaseOrder> expectedPageInfo = new PageInfo<>(List.of(
                createTestPurchaseOrder("PO00000001"),
                createTestPurchaseOrder("PO00000002")
            ));
            when(purchaseOrderRepository.selectAllWithPageInfo()).thenReturn(expectedPageInfo);

            // Act
            PageInfo<PurchaseOrder> result = purchaseOrderService.selectAllWithPageInfo();

            // Assert
            assertEquals(2, result.getList().size());
            verify(purchaseOrderRepository).selectAllWithPageInfo();
        }
    }

    @Nested
    @DisplayName("発注新規登録")
    class RegisterTest {
        
        @Test
        @DisplayName("発注番号なしで新規登録できる")
        void shouldRegisterNewPurchaseOrderWithoutNumber() {
            // Arrange
            PurchaseOrder purchaseOrder = createTestPurchaseOrderWithoutNumber();
            String generatedNumber = "PO25010001";
            
            when(autoNumberService.getNextDocumentNumber(anyString(), any(LocalDateTime.class))).thenReturn(1);
            when(purchaseOrderDomainService.calculateTotalPurchaseAmount(any())).thenReturn(Money.of(10000));
            when(purchaseOrderDomainService.calculateTotalConsumptionTax(any())).thenReturn(Money.of(1000));

            // Act
            purchaseOrderService.register(purchaseOrder);

            // Assert
            verify(autoNumberService).getNextDocumentNumber(eq("PO"), any(LocalDateTime.class));
            verify(autoNumberService).save(any());
            verify(autoNumberService).incrementDocumentNumber(eq("PO"), any(LocalDateTime.class));
            verify(purchaseOrderRepository).save(any(PurchaseOrder.class));
            verify(purchaseOrderDomainService).calculateTotalPurchaseAmount(any());
            verify(purchaseOrderDomainService).calculateTotalConsumptionTax(any());
        }

        @Test
        @DisplayName("発注番号ありで登録できる")
        void shouldRegisterPurchaseOrderWithNumber() {
            // Arrange
            PurchaseOrder purchaseOrder = createTestPurchaseOrder("PO00000001");

            // Act
            purchaseOrderService.register(purchaseOrder);

            // Assert
            verify(purchaseOrderRepository).save(purchaseOrder);
            verify(autoNumberService, never()).getNextDocumentNumber(anyString(), any());
        }

        @Test
        @DisplayName("発注データがnullの場合は例外が発生する")
        void shouldThrowExceptionWhenPurchaseOrderIsNull() {
            // Act & Assert
            assertThrows(NullPointerException.class, () -> {
                purchaseOrderService.register(null);
            });
        }
    }

    @Nested
    @DisplayName("発注編集")
    class SaveTest {
        
        @Test
        @DisplayName("発注を編集できる")
        void shouldSavePurchaseOrder() {
            // Arrange
            PurchaseOrder purchaseOrder = createTestPurchaseOrder("PO00000001");

            // Act
            purchaseOrderService.save(purchaseOrder);

            // Assert
            verify(purchaseOrderRepository).save(purchaseOrder);
        }

        @Test
        @DisplayName("発注データがnullの場合は例外が発生する")
        void shouldThrowExceptionWhenPurchaseOrderIsNullForSave() {
            // Act & Assert
            assertThrows(NullPointerException.class, () -> {
                purchaseOrderService.save(null);
            });
        }
    }

    @Nested
    @DisplayName("発注削除")
    class DeleteTest {
        
        @Test
        @DisplayName("発注を削除できる")
        void shouldDeletePurchaseOrder() {
            // Arrange
            String purchaseOrderNumber = "PO00000001";

            // Act
            purchaseOrderService.delete(purchaseOrderNumber);

            // Assert
            verify(purchaseOrderRepository).delete(purchaseOrderNumber);
        }

        @Test
        @DisplayName("発注番号がnullの場合は例外が発生する")
        void shouldThrowExceptionWhenPurchaseOrderNumberIsNull() {
            // Act & Assert
            assertThrows(NullPointerException.class, () -> {
                purchaseOrderService.delete(null);
            });
        }
    }

    @Nested
    @DisplayName("発注検索")
    class FindTest {
        
        @Test
        @DisplayName("発注番号で検索できる")
        void shouldFindPurchaseOrderByNumber() {
            // Arrange
            String purchaseOrderNumber = "PO00000001";
            PurchaseOrder expectedOrder = createTestPurchaseOrder(purchaseOrderNumber);
            when(purchaseOrderRepository.findByPurchaseOrderNumber(purchaseOrderNumber))
                .thenReturn(Optional.of(expectedOrder));

            // Act
            PurchaseOrder result = purchaseOrderService.find(purchaseOrderNumber);

            // Assert
            assertEquals(expectedOrder, result);
            verify(purchaseOrderRepository).findByPurchaseOrderNumber(purchaseOrderNumber);
        }

        @Test
        @DisplayName("存在しない発注番号で検索した場合nullを返す")
        void shouldReturnNullWhenPurchaseOrderNotFound() {
            // Arrange
            String purchaseOrderNumber = "PO99999999";
            when(purchaseOrderRepository.findByPurchaseOrderNumber(purchaseOrderNumber))
                .thenReturn(Optional.empty());

            // Act
            PurchaseOrder result = purchaseOrderService.find(purchaseOrderNumber);

            // Assert
            assertNull(result);
            verify(purchaseOrderRepository).findByPurchaseOrderNumber(purchaseOrderNumber);
        }

        @Test
        @DisplayName("発注番号がnullの場合は例外が発生する")
        void shouldThrowExceptionWhenPurchaseOrderNumberIsNullForFind() {
            // Act & Assert
            assertThrows(NullPointerException.class, () -> {
                purchaseOrderService.find(null);
            });
        }
    }

    @Nested
    @DisplayName("発注条件検索")
    class SearchTest {
        
        @Test
        @DisplayName("条件で発注を検索できる")
        void shouldSearchPurchaseOrdersWithCriteria() {
            // Arrange
            PurchaseOrderCriteria criteria = PurchaseOrderCriteria.builder()
                .purchaseOrderNumber("PO00000001")
                .build();
            PageInfo<PurchaseOrder> expectedPageInfo = new PageInfo<>(List.of(
                createTestPurchaseOrder("PO00000001")
            ));
            when(purchaseOrderRepository.searchWithPageInfo(criteria)).thenReturn(expectedPageInfo);

            // Act
            PageInfo<PurchaseOrder> result = purchaseOrderService.searchPurchaseOrderWithPageInfo(criteria);

            // Assert
            assertEquals(1, result.getList().size());
            verify(purchaseOrderRepository).searchWithPageInfo(criteria);
        }

        @Test
        @DisplayName("検索条件がnullの場合は例外が発生する")
        void shouldThrowExceptionWhenCriteriaIsNull() {
            // Act & Assert
            assertThrows(NullPointerException.class, () -> {
                purchaseOrderService.searchPurchaseOrderWithPageInfo(null);
            });
        }
    }

    @Nested
    @DisplayName("発注ルールチェック")
    class RuleCheckTest {
        
        @Test
        @DisplayName("発注ルールチェックを実行できる")
        void shouldCheckPurchaseOrderRules() {
            // Arrange
            PurchaseOrderList purchaseOrders = PurchaseOrderList.of(List.of(
                createTestPurchaseOrder("PO00000001")
            ));
            PurchaseOrderRuleCheckList expectedCheckList = new PurchaseOrderRuleCheckList(List.of());
            
            when(purchaseOrderRepository.selectAll()).thenReturn(purchaseOrders);
            when(purchaseOrderDomainService.checkRule(purchaseOrders)).thenReturn(expectedCheckList);

            // Act
            PurchaseOrderRuleCheckList result = purchaseOrderService.checkRule();

            // Assert
            assertEquals(expectedCheckList, result);
            verify(purchaseOrderRepository).selectAll();
            verify(purchaseOrderDomainService).checkRule(purchaseOrders);
        }
    }

    @Nested
    @DisplayName("金額計算")
    class CalculationTest {
        
        @Test
        @DisplayName("発注金額合計を計算できる")
        void shouldCalculateTotalPurchaseAmount() {
            // Arrange
            PurchaseOrder purchaseOrder = createTestPurchaseOrder("PO00000001");
            Money expectedAmount = Money.of(10000);
            when(purchaseOrderDomainService.calculateTotalPurchaseAmount(purchaseOrder))
                .thenReturn(expectedAmount);

            // Act
            Money result = purchaseOrderService.calculateTotalPurchaseAmount(purchaseOrder);

            // Assert
            assertEquals(expectedAmount, result);
            verify(purchaseOrderDomainService).calculateTotalPurchaseAmount(purchaseOrder);
        }

        @Test
        @DisplayName("発注消費税合計を計算できる")
        void shouldCalculateTotalConsumptionTax() {
            // Arrange
            PurchaseOrder purchaseOrder = createTestPurchaseOrder("PO00000001");
            Money expectedTax = Money.of(1000);
            when(purchaseOrderDomainService.calculateTotalConsumptionTax(purchaseOrder))
                .thenReturn(expectedTax);

            // Act
            Money result = purchaseOrderService.calculateTotalConsumptionTax(purchaseOrder);

            // Assert
            assertEquals(expectedTax, result);
            verify(purchaseOrderDomainService).calculateTotalConsumptionTax(purchaseOrder);
        }

        @Test
        @DisplayName("発注データがnullの場合は例外が発生する（金額計算）")
        void shouldThrowExceptionWhenPurchaseOrderIsNullForCalculation() {
            // Act & Assert
            assertThrows(NullPointerException.class, () -> {
                purchaseOrderService.calculateTotalPurchaseAmount(null);
            });
            
            assertThrows(NullPointerException.class, () -> {
                purchaseOrderService.calculateTotalConsumptionTax(null);
            });
        }
    }
}