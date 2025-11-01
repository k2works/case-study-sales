package com.example.sms.presentation.api.procurement.purchase;

import com.example.sms.domain.model.procurement.purchase.Purchase;
import com.example.sms.service.procurement.purchase.PurchaseCriteria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("仕入リソースDTOマッパーテスト")
class PurchaseResourceDTOMapperTest {

    @Test
    @DisplayName("PurchaseResourceからPurchaseに変換できる（仕入番号あり）")
    void shouldConvertResourceToEntityWithPurchaseNumber() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = LocalDateTime.of(2021, 1, 1, 0, 0);

        PurchaseLineResource lineResource = new PurchaseLineResource();
        lineResource.setPurchaseNumber("PU00000001");
        lineResource.setPurchaseLineNumber(1);
        lineResource.setPurchaseLineDisplayNumber(1);
        lineResource.setPurchaseOrderLineNumber(1);
        lineResource.setProductCode("10101001");
        lineResource.setWarehouseCode("W01");
        lineResource.setProductName("商品1");
        lineResource.setPurchaseUnitPrice(1000);
        lineResource.setPurchaseQuantity(10);

        PurchaseResource resource = new PurchaseResource();
        resource.setPurchaseNumber("PU00000001");
        resource.setPurchaseDate(now);
        resource.setSupplierCode("001");
        resource.setSupplierBranchNumber(1);
        resource.setPurchaseManagerCode("EMP001");
        resource.setStartDate(startDate);
        resource.setPurchaseOrderNumber("PO25010001");
        resource.setDepartmentCode("10000");
        resource.setTotalPurchaseAmount(10000);
        resource.setTotalConsumptionTax(1000);
        resource.setRemarks("備考");
        resource.setPurchaseLines(List.of(lineResource));

        // Act
        Purchase result = PurchaseResourceDTOMapper.convertToEntity(resource);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getPurchaseNumber());
        assertEquals("PU00000001", result.getPurchaseNumber().getValue());
        assertEquals(now, result.getPurchaseDate().getValue());
        assertEquals("001", result.getSupplierCode().getCode().getValue());
        assertEquals(1, result.getSupplierCode().getBranchNumber());
        assertEquals("EMP001", result.getPurchaseManagerCode().getValue());
        assertEquals(startDate, result.getStartDate());
        assertEquals("PO25010001", result.getPurchaseOrderNumber().getValue());
        assertEquals("10000", result.getDepartmentCode().getValue());
        assertEquals(10000, result.getTotalPurchaseAmount().getAmount());
        assertEquals(1000, result.getTotalConsumptionTax().getAmount());
        assertEquals("備考", result.getRemarks());
        assertEquals(1, result.getPurchaseLines().size());

        // 仕入明細の検証
        var line = result.getPurchaseLines().get(0);
        assertEquals("PU00000001", line.getPurchaseNumber().getValue());
        assertEquals(1, line.getPurchaseLineNumber());
        assertEquals(1, line.getPurchaseLineDisplayNumber());
        assertEquals(1, line.getPurchaseOrderLineNumber());
        assertEquals("10101001", line.getProductCode().getValue());
        assertEquals("W01", line.getWarehouseCode().getValue());
        assertEquals("商品1", line.getProductName());
        assertEquals(1000, line.getPurchaseUnitPrice().getAmount());
        assertEquals(10, line.getPurchaseQuantity().getAmount());
    }

    @Test
    @DisplayName("PurchaseResourceからPurchaseに変換できる（仕入番号なし）")
    void shouldConvertResourceToEntityWithoutPurchaseNumber() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = LocalDateTime.of(2021, 1, 1, 0, 0);

        PurchaseResource resource = new PurchaseResource();
        resource.setPurchaseNumber(null);
        resource.setPurchaseDate(now);
        resource.setSupplierCode("001");
        resource.setSupplierBranchNumber(1);
        resource.setPurchaseManagerCode("EMP001");
        resource.setStartDate(startDate);
        resource.setPurchaseOrderNumber("PO25010001");
        resource.setDepartmentCode("10000");
        resource.setTotalPurchaseAmount(10000);
        resource.setTotalConsumptionTax(1000);
        resource.setRemarks("備考");
        resource.setPurchaseLines(List.of());

        // Act
        Purchase result = PurchaseResourceDTOMapper.convertToEntity(resource);

        // Assert
        assertNotNull(result);
        assertNull(result.getPurchaseNumber());
        assertEquals(now, result.getPurchaseDate().getValue());
        assertEquals("001", result.getSupplierCode().getCode().getValue());
        assertEquals(1, result.getSupplierCode().getBranchNumber());
        assertEquals("EMP001", result.getPurchaseManagerCode().getValue());
        assertEquals(startDate, result.getStartDate());
        assertEquals("PO25010001", result.getPurchaseOrderNumber().getValue());
        assertEquals("10000", result.getDepartmentCode().getValue());
        assertEquals(10000, result.getTotalPurchaseAmount().getAmount());
        assertEquals(1000, result.getTotalConsumptionTax().getAmount());
        assertEquals("備考", result.getRemarks());
        assertTrue(result.getPurchaseLines().isEmpty());
    }

    @Test
    @DisplayName("PurchaseResourceからPurchaseに変換できる（仕入番号が空文字列）")
    void shouldConvertResourceToEntityWithEmptyPurchaseNumber() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = LocalDateTime.of(2021, 1, 1, 0, 0);

        PurchaseResource resource = new PurchaseResource();
        resource.setPurchaseNumber("");
        resource.setPurchaseDate(now);
        resource.setSupplierCode("001");
        resource.setSupplierBranchNumber(1);
        resource.setPurchaseManagerCode("EMP001");
        resource.setStartDate(startDate);
        resource.setPurchaseOrderNumber("PO25010001");
        resource.setDepartmentCode("10000");
        resource.setTotalPurchaseAmount(10000);
        resource.setTotalConsumptionTax(1000);
        resource.setRemarks("備考");
        resource.setPurchaseLines(List.of());

        // Act
        Purchase result = PurchaseResourceDTOMapper.convertToEntity(resource);

        // Assert
        assertNotNull(result);
        assertNull(result.getPurchaseNumber());
    }

    @Test
    @DisplayName("PurchaseResourceからPurchaseに変換できる（発注番号なし）")
    void shouldConvertResourceToEntityWithoutPurchaseOrderNumber() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = LocalDateTime.of(2021, 1, 1, 0, 0);

        PurchaseResource resource = new PurchaseResource();
        resource.setPurchaseNumber("PU00000001");
        resource.setPurchaseDate(now);
        resource.setSupplierCode("001");
        resource.setSupplierBranchNumber(1);
        resource.setPurchaseManagerCode("EMP001");
        resource.setStartDate(startDate);
        resource.setPurchaseOrderNumber(null);
        resource.setDepartmentCode("10000");
        resource.setTotalPurchaseAmount(10000);
        resource.setTotalConsumptionTax(1000);
        resource.setRemarks("備考");
        resource.setPurchaseLines(List.of());

        // Act
        Purchase result = PurchaseResourceDTOMapper.convertToEntity(resource);

        // Assert
        assertNotNull(result);
        assertNull(result.getPurchaseOrderNumber());
    }

    @Test
    @DisplayName("PurchaseCriteriaResourceからPurchaseCriteriaに変換できる")
    void shouldConvertCriteriaResourceToCriteria() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        PurchaseCriteriaResource resource = new PurchaseCriteriaResource();
        resource.setPurchaseNumber("PU00000001");
        resource.setPurchaseDate(now);
        resource.setPurchaseOrderNumber("PO25010001");
        resource.setSupplierCode("001");
        resource.setSupplierBranchNumber(1);
        resource.setPurchaseManagerCode("EMP001");
        resource.setDepartmentCode("10000");

        // Act
        PurchaseCriteria result = PurchaseResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(result);
        assertEquals("PU00000001", result.getPurchaseNumber());
        assertEquals(now, result.getPurchaseDate());
        assertEquals("PO25010001", result.getPurchaseOrderNumber());
        assertEquals("001", result.getSupplierCode());
        assertEquals(1, result.getSupplierBranchNumber());
        assertEquals("EMP001", result.getPurchaseManagerCode());
        assertEquals("10000", result.getDepartmentCode());
    }

    @Test
    @DisplayName("nullのリソースを渡すと例外が発生する")
    void shouldThrowExceptionWhenResourceIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            PurchaseResourceDTOMapper.convertToEntity(null);
        });
    }

    @Test
    @DisplayName("null値を含む検索条件リソースでも変換できる")
    void shouldConvertCriteriaResourceWithNullValues() {
        // Arrange
        PurchaseCriteriaResource resource = new PurchaseCriteriaResource();
        resource.setPurchaseNumber("PU00000001");
        // 他のフィールドはnull

        // Act
        PurchaseCriteria result = PurchaseResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(result);
        assertEquals("PU00000001", result.getPurchaseNumber());
        assertNull(result.getPurchaseDate());
        assertNull(result.getPurchaseOrderNumber());
        assertNull(result.getSupplierCode());
        assertNull(result.getSupplierBranchNumber());
        assertNull(result.getPurchaseManagerCode());
        assertNull(result.getDepartmentCode());
    }

    @Test
    @DisplayName("仕入明細がnullでも変換できる")
    void shouldConvertResourceToEntityWithNullPurchaseLines() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = LocalDateTime.of(2021, 1, 1, 0, 0);

        PurchaseResource resource = new PurchaseResource();
        resource.setPurchaseNumber("PU00000001");
        resource.setPurchaseDate(now);
        resource.setSupplierCode("001");
        resource.setSupplierBranchNumber(1);
        resource.setPurchaseManagerCode("EMP001");
        resource.setStartDate(startDate);
        resource.setPurchaseOrderNumber("PO25010001");
        resource.setDepartmentCode("10000");
        resource.setTotalPurchaseAmount(10000);
        resource.setTotalConsumptionTax(1000);
        resource.setRemarks("備考");
        resource.setPurchaseLines(null);

        // Act
        Purchase result = PurchaseResourceDTOMapper.convertToEntity(resource);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getPurchaseLines());
        assertTrue(result.getPurchaseLines().isEmpty());
    }
}
