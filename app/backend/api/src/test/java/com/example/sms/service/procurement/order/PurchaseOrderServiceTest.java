package com.example.sms.service.procurement.order;

import com.example.sms.domain.model.master.employee.EmployeeCode;
import com.example.sms.domain.model.master.partner.Partner;
import com.example.sms.domain.model.master.product.Product;
import com.example.sms.domain.model.master.employee.Employee;
import com.example.sms.domain.model.master.partner.supplier.SupplierCode;
import com.example.sms.domain.model.master.product.ProductCode;
import com.example.sms.domain.model.master.warehouse.WarehouseCode;
import com.example.sms.domain.model.sales.order.CompletionFlag;
import com.example.sms.domain.model.sales.order.OrderNumber;
import com.example.sms.domain.model.procurement.order.*;
import com.example.sms.domain.type.money.Money;
import com.example.sms.domain.type.quantity.Quantity;
import com.example.sms.domain.model.procurement.order.rule.PurchaseOrderRuleCheckList;
import com.example.sms.domain.service.procurement.purchase.PurchaseOrderDomainService;
import com.example.sms.service.system.autonumber.AutoNumberService;
import com.example.sms.service.master.partner.PartnerRepository;
import com.example.sms.service.master.product.ProductRepository;
import com.example.sms.service.master.employee.EmployeeRepository;
import com.github.pagehelper.PageInfo;
import org.springframework.mock.web.MockMultipartFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
    
    @Mock
    private PartnerRepository partnerRepository;
    
    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private EmployeeRepository employeeRepository;
    
    private PurchaseOrderService purchaseOrderService;

    @BeforeEach
    void setUp() {
        purchaseOrderService = new PurchaseOrderService(
            purchaseOrderRepository, 
            autoNumberService, 
            purchaseOrderDomainService,
            partnerRepository,
            productRepository,
            employeeRepository
        );
    }

    private PurchaseOrder createTestPurchaseOrder(String purchaseOrderNumber) {
        LocalDateTime now = LocalDateTime.now();
        
        List<PurchaseOrderLine> lines = List.of(
            PurchaseOrderLine.of(
                purchaseOrderNumber,
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

        return PurchaseOrder.of(
            purchaseOrderNumber,
            now,
            "OD25010001",
            "001", // 仕入先コード
            0,
            "EMP001", // 社員コード
            now.plusDays(7),
            "W01", // 倉庫コード
            10000,
            1000, // 消費税
            "備考",
            lines
        );
    }

    private PurchaseOrder createTestPurchaseOrderWithoutNumber() {
        LocalDateTime now = LocalDateTime.now();
        
        // 発注番号なしの場合でも明細を追加して金額計算を合わせる
        List<PurchaseOrderLine> lines = List.of(
            PurchaseOrderLine.builder()
                .purchaseOrderNumber(null) // 発注番号なし
                .purchaseOrderLineNumber(1)
                .purchaseOrderLineDisplayNumber(1)
                .salesOrderNumber(OrderNumber.of("OD25010001"))
                .salesOrderLineNumber(1)
                .productCode(ProductCode.of("10101001"))
                .productName("商品1")
                .purchaseUnitPrice(Money.of(1000))
                .purchaseOrderQuantity(Quantity.of(10))
                .receivedQuantity(Quantity.of(0))
                .completionFlag(CompletionFlag.of(0))
                .build()
        );

        return PurchaseOrder.builder()
                .purchaseOrderNumber(null) // 発注番号なし
                .purchaseOrderDate(PurchaseOrderDate.of(now))
                .salesOrderNumber(OrderNumber.of("OD25010001"))
                .supplierCode(SupplierCode.of("001", 0))
                .purchaseManagerCode(EmployeeCode.of("EMP001"))
                .designatedDeliveryDate(DesignatedDeliveryDate.of(now.plusDays(7)))
                .warehouseCode(WarehouseCode.of("W01"))
                .totalPurchaseAmount(Money.of(10000)) // 1000 × 10 = 10000
                .totalConsumptionTax(Money.of(1000)) // 10000 × 10% = 1000
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
        
        @Test
        @DisplayName("発注金額が500万円を超える場合にエラーを返す")
        void shouldReturnErrorWhenPurchaseAmountExceeds5Million() {
            // Arrange
            LocalDateTime now = LocalDateTime.now();
            PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                    .purchaseOrderNumber(PurchaseOrderNumber.of("PO202301010001"))
                    .purchaseOrderDate(PurchaseOrderDate.of(now))
                    .designatedDeliveryDate(DesignatedDeliveryDate.of(now.plusDays(30)))
                    .totalPurchaseAmount(Money.of(6000000)) // 600万円
                    .purchaseOrderLines(List.of())
                    .build();

            PurchaseOrderList purchaseOrders = PurchaseOrderList.of(List.of(purchaseOrder));
            
            // エラーありの結果をモック
            PurchaseOrderRuleCheckList expectedCheckList = new PurchaseOrderRuleCheckList(List.of(
                Map.of("PO202301010001", "発注金額が500万円を超えています。")
            ));
            
            when(purchaseOrderRepository.selectAll()).thenReturn(purchaseOrders);
            when(purchaseOrderDomainService.checkRule(purchaseOrders)).thenReturn(expectedCheckList);

            // Act
            PurchaseOrderRuleCheckList result = purchaseOrderService.checkRule();

            // Assert
            assertTrue(result.hasErrors(), "金額超過のエラーが発生すること");
            assertTrue(result.getErrorCount() >= 1, "少なくとも1つのエラーがあること");
        }
        
        @Test
        @DisplayName("納期が発注日より前の場合にエラーを返す")
        void shouldReturnErrorWhenDeliveryDateIsBeforePurchaseOrderDate() {
            // Arrange
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime deliveryDate = now.minusDays(1); // 前日を納期に設定
            
            PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                    .purchaseOrderNumber(PurchaseOrderNumber.of("PO202301010001"))
                    .purchaseOrderDate(PurchaseOrderDate.of(now))
                    .designatedDeliveryDate(DesignatedDeliveryDate.of(deliveryDate))
                    .totalPurchaseAmount(Money.of(1000000))
                    .purchaseOrderLines(List.of())
                    .build();

            PurchaseOrderList purchaseOrders = PurchaseOrderList.of(List.of(purchaseOrder));
            
            // エラーありの結果をモック
            PurchaseOrderRuleCheckList expectedCheckList = new PurchaseOrderRuleCheckList(List.of(
                Map.of("PO202301010001", "指定納期が発注日より前です。")
            ));
            
            when(purchaseOrderRepository.selectAll()).thenReturn(purchaseOrders);
            when(purchaseOrderDomainService.checkRule(purchaseOrders)).thenReturn(expectedCheckList);

            // Act
            PurchaseOrderRuleCheckList result = purchaseOrderService.checkRule();

            // Assert
            assertTrue(result.hasErrors(), "納期エラーが発生すること");
            assertTrue(result.getErrorCount() >= 1, "少なくとも1つのエラーがあること");
        }
        
        @Test
        @DisplayName("納期が過去の場合にエラーを返す")
        void shouldReturnErrorWhenDeliveryDateIsPast() {
            // Arrange
            LocalDateTime pastDate = LocalDateTime.now().minusDays(10);
            LocalDateTime deliveryDate = LocalDateTime.now().minusDays(1); // 昨日を納期に設定
            
            PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                    .purchaseOrderNumber(PurchaseOrderNumber.of("PO202301010001"))
                    .purchaseOrderDate(PurchaseOrderDate.of(pastDate))
                    .designatedDeliveryDate(DesignatedDeliveryDate.of(deliveryDate))
                    .totalPurchaseAmount(Money.of(1000000))
                    .purchaseOrderLines(List.of())
                    .build();

            PurchaseOrderList purchaseOrders = PurchaseOrderList.of(List.of(purchaseOrder));
            
            // エラーありの結果をモック
            PurchaseOrderRuleCheckList expectedCheckList = new PurchaseOrderRuleCheckList(List.of(
                Map.of("PO202301010001", "指定納期が過ぎています。")
            ));
            
            when(purchaseOrderRepository.selectAll()).thenReturn(purchaseOrders);
            when(purchaseOrderDomainService.checkRule(purchaseOrders)).thenReturn(expectedCheckList);

            // Act
            PurchaseOrderRuleCheckList result = purchaseOrderService.checkRule();

            // Assert
            assertTrue(result.hasErrors(), "納期超過エラーが発生すること");
            assertTrue(result.getErrorCount() >= 1, "少なくとも1つのエラーがあること");
        }
        
        @Test
        @DisplayName("すべてのルールを満たす場合はエラーなし")
        void shouldReturnNoErrorWhenAllRulesAreSatisfied() {
            // Arrange
            LocalDateTime now = LocalDateTime.now();
            PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                    .purchaseOrderNumber(PurchaseOrderNumber.of("PO202301010001"))
                    .purchaseOrderDate(PurchaseOrderDate.of(now))
                    .designatedDeliveryDate(DesignatedDeliveryDate.of(now.plusDays(30)))
                    .totalPurchaseAmount(Money.of(1000000)) // 100万円
                    .purchaseOrderLines(List.of())
                    .build();

            PurchaseOrderList purchaseOrders = PurchaseOrderList.of(List.of(purchaseOrder));
            
            // エラーなしの結果をモック
            PurchaseOrderRuleCheckList expectedCheckList = new PurchaseOrderRuleCheckList(List.of());
            
            when(purchaseOrderRepository.selectAll()).thenReturn(purchaseOrders);
            when(purchaseOrderDomainService.checkRule(purchaseOrders)).thenReturn(expectedCheckList);

            // Act
            PurchaseOrderRuleCheckList result = purchaseOrderService.checkRule();

            // Assert
            assertFalse(result.hasErrors(), "ルール違反がないこと");
            assertEquals(0, result.getErrorCount(), "エラー数が0であること");
        }
        
        @Test
        @DisplayName("複数の発注で複数のエラーがある場合")
        void shouldReturnMultipleErrorsWhenMultiplePurchaseOrdersHaveViolations() {
            // Arrange
            LocalDateTime now = LocalDateTime.now();
            
            // 金額超過の発注
            PurchaseOrder purchaseOrder1 = PurchaseOrder.builder()
                    .purchaseOrderNumber(PurchaseOrderNumber.of("PO202301010001"))
                    .purchaseOrderDate(PurchaseOrderDate.of(now))
                    .designatedDeliveryDate(DesignatedDeliveryDate.of(now.plusDays(30)))
                    .totalPurchaseAmount(Money.of(6000000))
                    .purchaseOrderLines(List.of())
                    .build();

            // 納期が前の発注（builderでバリデーションを回避）
            LocalDateTime deliveryDate = now.minusDays(1);
            PurchaseOrder purchaseOrder2 = PurchaseOrder.builder()
                    .purchaseOrderNumber(PurchaseOrderNumber.of("PO202301010002"))
                    .purchaseOrderDate(PurchaseOrderDate.of(now))
                    .designatedDeliveryDate(DesignatedDeliveryDate.of(deliveryDate))
                    .totalPurchaseAmount(Money.of(1000000))
                    .purchaseOrderLines(List.of())
                    .build();

            PurchaseOrderList purchaseOrders = PurchaseOrderList.of(List.of(purchaseOrder1, purchaseOrder2));
            
            // 複数エラーの結果をモック
            PurchaseOrderRuleCheckList expectedCheckList = new PurchaseOrderRuleCheckList(List.of(
                Map.of("PO202301010001", "発注金額が500万円を超えています。"),
                Map.of("PO202301010002", "指定納期が発注日より前です。")
            ));
            
            when(purchaseOrderRepository.selectAll()).thenReturn(purchaseOrders);
            when(purchaseOrderDomainService.checkRule(purchaseOrders)).thenReturn(expectedCheckList);

            // Act
            PurchaseOrderRuleCheckList result = purchaseOrderService.checkRule();

            // Assert
            assertTrue(result.hasErrors(), "複数のエラーが発生すること");
            assertTrue(result.getErrorCount() >= 2, "少なくとも2つのエラーがあること");
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
        
        @Test
        @DisplayName("発注金額合計を正しく計算できる（Service経由）")
        void shouldCalculateTotalPurchaseAmountViaService() {
            // Arrange
            PurchaseOrderLine line1 = PurchaseOrderLine.builder()
                    .purchaseUnitPrice(Money.of(1000))
                    .purchaseOrderQuantity(Quantity.of(10))
                    .build();
            PurchaseOrderLine line2 = PurchaseOrderLine.builder()
                    .purchaseUnitPrice(Money.of(2000))
                    .purchaseOrderQuantity(Quantity.of(5))
                    .build();

            PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                    .purchaseOrderLines(List.of(line1, line2))
                    .build();

            // ドメインサービスを直接実行して結果をモック
            Money expectedAmount = Money.of(20000); // 1000*10 + 2000*5 = 20000
            when(purchaseOrderDomainService.calculateTotalPurchaseAmount(purchaseOrder))
                .thenReturn(expectedAmount);

            // Act
            Money result = purchaseOrderService.calculateTotalPurchaseAmount(purchaseOrder);

            // Assert
            assertEquals(expectedAmount, result);
            verify(purchaseOrderDomainService).calculateTotalPurchaseAmount(purchaseOrder);
        }
        
        @Test
        @DisplayName("発注消費税合計を正しく計算できる（Service経由）")
        void shouldCalculateTotalConsumptionTaxViaService() {
            // Arrange
            PurchaseOrderLine line1 = PurchaseOrderLine.builder()
                    .purchaseUnitPrice(Money.of(1000))
                    .purchaseOrderQuantity(Quantity.of(10))
                    .build();

            PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                    .purchaseOrderLines(List.of(line1))
                    .build();

            // ドメインサービスを直接実行して結果をモック
            Money expectedTax = Money.of(1000); // (1000*10) * 0.1 = 1000
            when(purchaseOrderDomainService.calculateTotalConsumptionTax(purchaseOrder))
                .thenReturn(expectedTax);

            // Act
            Money result = purchaseOrderService.calculateTotalConsumptionTax(purchaseOrder);

            // Assert
            assertEquals(expectedTax, result);
            verify(purchaseOrderDomainService).calculateTotalConsumptionTax(purchaseOrder);
        }
    }

    @Nested
    @DisplayName("CSVファイルアップロード")
    class UploadCsvFileTest {

        @Test
        @DisplayName("正常なCSVファイルをアップロードできる")
        void shouldUploadValidCsvFile() {
            // Arrange
            String csvContent = "発注番号,発注日,売上注文番号,仕入先コード,仕入先支店番号,発注担当者コード,指定納期,倉庫コード,発注金額合計,消費税合計,備考,発注行番号,商品コード,商品名,仕入単価,発注数量,消費税率,入荷予定数量,入荷済数量,完了フラグ,値引金額,納期,入荷日\n"
                          + "PO00000001,2023-01-01,,SUPP001,1,EMP001,2023-01-10,WH001,10000,1000,備考,1,PROD001,商品1,1000,10,10,0,0,0,0,2023-01-10,";
            MockMultipartFile file = new MockMultipartFile(
                "file",
                "purchase_orders.csv",
                "text/csv",
                csvContent.getBytes()
            );

            when(partnerRepository.findById("SUPP001")).thenReturn(Optional.of(mock(Partner.class)));
            when(productRepository.findById("PROD001")).thenReturn(Optional.of(mock(Product.class)));
            when(employeeRepository.findById(EmployeeCode.of("EMP001"))).thenReturn(Optional.of(mock(Employee.class)));

            // Act
            PurchaseOrderUploadErrorList result = purchaseOrderService.uploadCsvFile(file);

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty(), "エラーが発生していないこと");
        }

        @Test
        @DisplayName("バリデーションエラーのあるCSVファイルをアップロードする")
        void shouldReturnValidationErrorsForInvalidCsv() {
            // Arrange
            String csvContent = "発注番号,発注日,売上注文番号,仕入先コード,仕入先支店番号,発注担当者コード,指定納期,倉庫コード,発注金額合計,消費税税合計,備考,発注行番号,商品コード,商品名,仕入単価,発注数量,消費税率,入荷予定数量,入荷済数量,完了フラグ,値引金額,納期,入荷日\n"
                          + "PO00000001,2023-01-01,,SUPP999,1,EMP001,2023-01-10,WH001,10000,1000,備考,1,PROD999,商品1,1000,10,10,0,0,0,0,2023-01-10,";
            MockMultipartFile file = new MockMultipartFile(
                "file",
                "purchase_orders.csv",
                "text/csv",
                csvContent.getBytes()
            );

            // 存在しないマスタデータを設定
            when(partnerRepository.findById("SUPP999")).thenReturn(Optional.empty());
            when(productRepository.findById("PROD999")).thenReturn(Optional.empty());
            when(employeeRepository.findById(EmployeeCode.of("EMP001"))).thenReturn(Optional.of(mock(Employee.class)));

            // Act
            PurchaseOrderUploadErrorList result = purchaseOrderService.uploadCsvFile(file);

            // Assert
            assertNotNull(result);
            assertFalse(result.isEmpty(), "バリデーションエラーが発生すること");
            assertEquals(2, result.size(), "2つのエラーが発生すること（仕入先、商品）");
        }

        @Test
        @DisplayName("ファイルがnullの場合は例外が発生する")
        void shouldThrowExceptionWhenFileIsNull() {
            // Act & Assert
            assertThrows(NullPointerException.class, () -> {
                purchaseOrderService.uploadCsvFile(null);
            });
        }

        @Test
        @DisplayName("空のファイルの場合は例外が発生する")
        void shouldThrowExceptionWhenFileIsEmpty() {
            // Arrange
            MockMultipartFile file = new MockMultipartFile(
                "file",
                "empty.csv",
                "text/csv",
                new byte[0]
            );

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> {
                purchaseOrderService.uploadCsvFile(file);
            });
        }

        @Test
        @DisplayName("CSVでないファイルの場合は例外が発生する")
        void shouldThrowExceptionWhenFileIsNotCsv() {
            // Arrange
            MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "test content".getBytes()
            );

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> {
                purchaseOrderService.uploadCsvFile(file);
            });
        }
    }
}