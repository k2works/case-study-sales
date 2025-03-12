package com.example.sms.presentation.api.sales_order;

import com.example.sms.domain.model.sales_order.*;
import com.example.sms.service.sales_order.SalesOrderCriteria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("受注DTOマッパーテスト")
class SalesOrderResourceDTOMapperTest {

    @Test
    @DisplayName("受注リソースを受注エンティティに変換する")
    void testConvertToEntity_validResource_shouldReturnSalesOrder() {
        // Arrange
        String orderNumber = "1234567890";
        LocalDateTime orderDate = LocalDateTime.now();
        String departmentCode = "12345";
        LocalDateTime departmentStartDate = LocalDateTime.now().minusDays(30);
        String customerCode = "001";
        Integer customerBranchNumber = 1;
        String employeeCode = "EMP001";
        LocalDateTime desiredDeliveryDate = LocalDateTime.now().plusDays(7);
        String customerOrderNumber = "CO001";
        String warehouseCode = "W001";
        Integer totalOrderAmount = 10000;
        Integer totalConsumptionTax = 1000;
        String remarks = "テスト備考";

        // Create sales order line resource
        SalesOrderLineResource lineResource = SalesOrderLineResource.builder()
                .orderNumber(orderNumber)
                .orderLineNumber(1)
                .productCode("P001")
                .productName("テスト商品")
                .salesUnitPrice(1000)
                .orderQuantity(10)
                .taxRate(TaxRateType.標準税率)
                .allocationQuantity(10)
                .shipmentInstructionQuantity(10)
                .shippedQuantity(0)
                .completionFlag(CompletionFlag.未完了)
                .discountAmount(0)
                .deliveryDate(desiredDeliveryDate)
                .build();

        SalesOrderResource resource = new SalesOrderResource();
        resource.setOrderNumber(orderNumber);
        resource.setOrderDate(orderDate);
        resource.setDepartmentCode(departmentCode);
        resource.setDepartmentStartDate(departmentStartDate);
        resource.setCustomerCode(customerCode);
        resource.setCustomerBranchNumber(customerBranchNumber);
        resource.setEmployeeCode(employeeCode);
        resource.setDesiredDeliveryDate(desiredDeliveryDate);
        resource.setCustomerOrderNumber(customerOrderNumber);
        resource.setWarehouseCode(warehouseCode);
        resource.setTotalOrderAmount(totalOrderAmount);
        resource.setTotalConsumptionTax(totalConsumptionTax);
        resource.setRemarks(remarks);
        resource.setSalesOrderLines(List.of(lineResource));

        // Act
        SalesOrder salesOrder = SalesOrderResourceDTOMapper.convertToEntity(resource);

        // Assert
        assertNotNull(salesOrder);
        assertEquals(orderNumber, salesOrder.getOrderNumber().getValue());
        assertEquals(orderDate, salesOrder.getOrderDate().getValue());
        assertEquals(departmentCode, salesOrder.getDepartmentCode().getValue());
        assertEquals(departmentStartDate, salesOrder.getDepartmentStartDate());
        assertEquals(customerCode, salesOrder.getCustomerCode().getCode().getValue());
        assertEquals(customerBranchNumber, salesOrder.getCustomerCode().getBranchNumber());
        assertEquals(employeeCode, salesOrder.getEmployeeCode().getValue());
        assertEquals(desiredDeliveryDate, salesOrder.getDesiredDeliveryDate().getValue());
        assertEquals(customerOrderNumber, salesOrder.getCustomerOrderNumber());
        assertEquals(warehouseCode, salesOrder.getWarehouseCode());
        assertEquals(remarks, salesOrder.getRemarks());

        // Check sales order lines
        assertNotNull(salesOrder.getSalesOrderLines());
        assertEquals(1, salesOrder.getSalesOrderLines().size());

        SalesOrderLine line = salesOrder.getSalesOrderLines().get(0);
        assertEquals(orderNumber, line.getOrderNumber().getValue());
        assertEquals(1, line.getOrderLineNumber());
        assertEquals("P001", line.getProductCode().getValue());
        assertEquals("テスト商品", line.getProductName());
        assertEquals(1000, line.getSalesUnitPrice().getAmount());
        assertEquals(10, line.getOrderQuantity().getAmount());
        assertEquals(TaxRateType.標準税率, line.getTaxRate());
        assertEquals(10, line.getAllocationQuantity().getAmount());
        assertEquals(10, line.getShipmentInstructionQuantity().getAmount());
        assertEquals(0, line.getShippedQuantity().getAmount());
        assertEquals(CompletionFlag.未完了, line.getCompletionFlag());
        assertEquals(0, line.getDiscountAmount().getAmount());
        assertEquals(desiredDeliveryDate, line.getDeliveryDate().getValue());
    }

    @Test
    @DisplayName("受注リソースに明細がnullの場合は例外をスローする")
    void testConvertToEntity_nullOrderLines_shouldThrowException() {
        // Arrange
        String orderNumber = "O001";
        LocalDateTime orderDate = LocalDateTime.now();
        String departmentCode = "D001";
        LocalDateTime departmentStartDate = LocalDateTime.now().minusDays(30);
        String customerCode = "C001";
        Integer customerBranchNumber = 1;
        String employeeCode = "E001";
        LocalDateTime desiredDeliveryDate = LocalDateTime.now().plusDays(7);
        String customerOrderNumber = "CO001";
        String warehouseCode = "W001";
        Integer totalOrderAmount = 10000;
        Integer totalConsumptionTax = 1000;
        String remarks = "テスト備考";

        SalesOrderResource resource = new SalesOrderResource();
        resource.setOrderNumber(orderNumber);
        resource.setOrderDate(orderDate);
        resource.setDepartmentCode(departmentCode);
        resource.setDepartmentStartDate(departmentStartDate);
        resource.setCustomerCode(customerCode);
        resource.setCustomerBranchNumber(customerBranchNumber);
        resource.setEmployeeCode(employeeCode);
        resource.setDesiredDeliveryDate(desiredDeliveryDate);
        resource.setCustomerOrderNumber(customerOrderNumber);
        resource.setWarehouseCode(warehouseCode);
        resource.setTotalOrderAmount(totalOrderAmount);
        resource.setTotalConsumptionTax(totalConsumptionTax);
        resource.setRemarks(remarks);
        resource.setSalesOrderLines(null);

        // Act & Assert
        try {
            SalesOrderResourceDTOMapper.convertToEntity(resource);
            fail("Expected exception was not thrown");
        } catch (Exception e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    @DisplayName("受注検索条件リソースを受注検索条件に変換する")
    void testConvertToCriteria_validResource_shouldReturnSalesOrderCriteria() {
        // Arrange
        String orderNumber = "O001";
        LocalDateTime orderDate = LocalDateTime.now();
        String departmentCode = "D001";
        LocalDateTime departmentStartDate = LocalDateTime.now().minusDays(30);
        String customerCode = "C001";
        Integer customerBranchNumber = 1;
        String employeeCode = "E001";
        LocalDateTime desiredDeliveryDate = LocalDateTime.now().plusDays(7);
        String customerOrderNumber = "CO001";
        String warehouseCode = "W001";
        String remarks = "テスト備考";

        SalesOrderCriteriaResource resource = new SalesOrderCriteriaResource();
        resource.setOrderNumber(orderNumber);
        resource.setOrderDate(orderDate);
        resource.setDepartmentCode(departmentCode);
        resource.setDepartmentStartDate(departmentStartDate);
        resource.setCustomerCode(customerCode);
        resource.setCustomerBranchNumber(customerBranchNumber);
        resource.setEmployeeCode(employeeCode);
        resource.setDesiredDeliveryDate(desiredDeliveryDate);
        resource.setCustomerOrderNumber(customerOrderNumber);
        resource.setWarehouseCode(warehouseCode);
        resource.setRemarks(remarks);

        // Act
        SalesOrderCriteria criteria = SalesOrderResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(criteria);
        assertEquals(orderNumber, criteria.getOrderNumber());
        assertEquals(orderDate, criteria.getOrderDate());
        assertEquals(departmentCode, criteria.getDepartmentCode());
        assertEquals(departmentStartDate, criteria.getDepartmentStartDate());
        assertEquals(customerCode, criteria.getCustomerCode());
        assertEquals(customerBranchNumber, criteria.getCustomerBranchNumber());
        assertEquals(employeeCode, criteria.getEmployeeCode());
        assertEquals(desiredDeliveryDate, criteria.getDesiredDeliveryDate());
        assertEquals(customerOrderNumber, criteria.getCustomerOrderNumber());
        assertEquals(warehouseCode, criteria.getWarehouseCode());
        assertEquals(remarks, criteria.getRemarks());
    }

    @Test
    @DisplayName("受注検索条件リソースの日付がnullの場合もnullとして変換する")
    void testConvertToCriteria_nullDates_shouldReturnCriteriaWithNullDates() {
        // Arrange
        String orderNumber = "O001";
        String departmentCode = "D001";
        String customerCode = "C001";
        Integer customerBranchNumber = 1;
        String employeeCode = "E001";
        String customerOrderNumber = "CO001";
        String warehouseCode = "W001";
        String remarks = "テスト備考";

        SalesOrderCriteriaResource resource = new SalesOrderCriteriaResource();
        resource.setOrderNumber(orderNumber);
        resource.setOrderDate(null);
        resource.setDepartmentCode(departmentCode);
        resource.setDepartmentStartDate(null);
        resource.setCustomerCode(customerCode);
        resource.setCustomerBranchNumber(customerBranchNumber);
        resource.setEmployeeCode(employeeCode);
        resource.setDesiredDeliveryDate(null);
        resource.setCustomerOrderNumber(customerOrderNumber);
        resource.setWarehouseCode(warehouseCode);
        resource.setRemarks(remarks);

        // Act
        SalesOrderCriteria criteria = SalesOrderResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(criteria);
        assertEquals(orderNumber, criteria.getOrderNumber());
        assertNull(criteria.getOrderDate());
        assertEquals(departmentCode, criteria.getDepartmentCode());
        assertNull(criteria.getDepartmentStartDate());
        assertEquals(customerCode, criteria.getCustomerCode());
        assertEquals(customerBranchNumber, criteria.getCustomerBranchNumber());
        assertEquals(employeeCode, criteria.getEmployeeCode());
        assertNull(criteria.getDesiredDeliveryDate());
        assertEquals(customerOrderNumber, criteria.getCustomerOrderNumber());
        assertEquals(warehouseCode, criteria.getWarehouseCode());
        assertEquals(remarks, criteria.getRemarks());
    }
}
