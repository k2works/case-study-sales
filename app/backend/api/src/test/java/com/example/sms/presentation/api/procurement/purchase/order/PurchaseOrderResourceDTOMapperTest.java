package com.example.sms.presentation.api.procurement.purchase.order;

import com.example.sms.domain.model.sales.order.CompletionFlag;
import com.example.sms.domain.model.procurement.purchase.order.PurchaseOrder;
import com.example.sms.service.procurement.purchase.order.PurchaseOrderCriteria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("発注リソースDTOマッパーテスト")
class PurchaseOrderResourceDTOMapperTest {

    @Test
    @DisplayName("PurchaseOrderResourceからPurchaseOrderに変換できる")
    void shouldConvertResourceToEntity() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        PurchaseOrderResource resource = new PurchaseOrderResource();
        resource.setPurchaseOrderNumber("PO25010001");
        resource.setPurchaseOrderDate(now);
        resource.setSalesOrderNumber("SO000001");
        resource.setSupplierCode("001");
        resource.setSupplierBranchNumber(0);
        resource.setPurchaseManagerCode("EMP001");
        resource.setDesignatedDeliveryDate(now.plusDays(1)); // 発注日より後に設定
        resource.setWarehouseCode("001");
        resource.setTotalPurchaseAmount(10000);
        resource.setTotalConsumptionTax(1000); // 10000円 × 10% = 1000円
        resource.setRemarks("備考");
        
        // 発注明細を追加（合計金額10000円になるように）
        PurchaseOrderLineResource lineResource = new PurchaseOrderLineResource();
        lineResource.setPurchaseOrderNumber("PO25010001");
        lineResource.setPurchaseOrderLineNumber(1);
        lineResource.setPurchaseOrderLineDisplayNumber(1);
        lineResource.setSalesOrderNumber("SO000001");
        lineResource.setSalesOrderLineNumber(1);
        lineResource.setProductCode("10101001");
        lineResource.setProductName("商品1");
        lineResource.setPurchaseUnitPrice(1000);
        lineResource.setPurchaseOrderQuantity(10); // 1000円 × 10 = 10000円
        lineResource.setReceivedQuantity(0);
        lineResource.setCompletionFlag(CompletionFlag.未完了);
        
        resource.setPurchaseOrderLines(List.of(lineResource));

        // Act
        PurchaseOrder result = PurchaseOrderResourceDTOMapper.convertToEntity(resource);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getPurchaseOrderNumber());
        assertEquals("PO25010001", result.getPurchaseOrderNumber().getValue());
        assertEquals(now, result.getPurchaseOrderDate().getValue());
        assertEquals("SO000001", result.getSalesOrderNumber());
        assertEquals("001", result.getSupplierCode().getValue());
        assertEquals(0, result.getSupplierBranchNumber());
        assertEquals("EMP001", result.getPurchaseManagerCode().getValue());
        assertEquals(now.plusDays(1), result.getDesignatedDeliveryDate().getValue());
        assertEquals("001", result.getWarehouseCode());
        assertEquals(10000, result.getTotalPurchaseAmount().getAmount());
        assertEquals(1000, result.getTotalConsumptionTax().getAmount()); // 10000円 × 10% = 1000円
        assertEquals("備考", result.getRemarks());
        assertEquals(1, result.getPurchaseOrderLines().size());
        
        // 発注明細の検証
        var line = result.getPurchaseOrderLines().get(0);
        assertEquals("PO25010001", line.getPurchaseOrderNumber().getValue());
        assertEquals(1, line.getPurchaseOrderLineNumber());
        assertEquals(1, line.getPurchaseOrderLineDisplayNumber());
        assertEquals("SO000001", line.getSalesOrderNumber());
        assertEquals(1, line.getSalesOrderLineNumber());
        assertEquals("10101001", line.getProductCode().getValue());
        assertEquals("商品1", line.getProductName());
        assertEquals(1000, line.getPurchaseUnitPrice().getAmount());
        assertEquals(10, line.getPurchaseOrderQuantity().getAmount());
        assertEquals(0, line.getReceivedQuantity().getAmount());
    }

    @Test
    @DisplayName("PurchaseOrderCriteriaResourceからPurchaseOrderCriteriaに変換できる")
    void shouldConvertCriteriaResourceToCriteria() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        PurchaseOrderCriteriaResource resource = new PurchaseOrderCriteriaResource();
        resource.setPurchaseOrderNumber("PO25010001");
        resource.setPurchaseOrderDate(now);
        resource.setSalesOrderNumber("SO000001");
        resource.setSupplierCode("001");
        resource.setSupplierBranchNumber(0);
        resource.setPurchaseManagerCode("EMP001");
        resource.setDesignatedDeliveryDate(now.plusDays(1)); // 発注日より後に設定
        resource.setWarehouseCode("001");
        resource.setRemarks("備考");

        // Act
        PurchaseOrderCriteria result = PurchaseOrderResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(result);
        assertEquals("PO25010001", result.getPurchaseOrderNumber());
        assertEquals(now, result.getPurchaseOrderDate());
        assertEquals("SO000001", result.getSalesOrderNumber());
        assertEquals("001", result.getSupplierCode());
        assertEquals(0, result.getSupplierBranchNumber());
        assertEquals("EMP001", result.getPurchaseManagerCode());
        assertEquals(now.plusDays(1), result.getDesignatedDeliveryDate());
        assertEquals("001", result.getWarehouseCode());
        assertEquals("備考", result.getRemarks());
    }

    @Test
    @DisplayName("nullのリソースを渡すと例外が発生する")
    void shouldThrowExceptionWhenResourceIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            PurchaseOrderResourceDTOMapper.convertToEntity(null);
        });
    }

    @Test
    @DisplayName("null値を含む検索条件リソースでも変換できる")
    void shouldConvertCriteriaResourceWithNullValues() {
        // Arrange
        PurchaseOrderCriteriaResource resource = new PurchaseOrderCriteriaResource();
        resource.setPurchaseOrderNumber("PO25010001");
        // 他のフィールドはnull

        // Act
        PurchaseOrderCriteria result = PurchaseOrderResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(result);
        assertEquals("PO25010001", result.getPurchaseOrderNumber());
        assertNull(result.getPurchaseOrderDate());
        assertNull(result.getSalesOrderNumber());
        assertNull(result.getSupplierCode());
        assertNull(result.getSupplierBranchNumber());
        assertNull(result.getPurchaseManagerCode());
        assertNull(result.getDesignatedDeliveryDate());
        assertNull(result.getWarehouseCode());
        assertNull(result.getRemarks());
    }
}