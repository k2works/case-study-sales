package com.example.sms.presentation.api.shipping;

import com.example.sms.domain.model.sales.order.CompletionFlag;
import com.example.sms.domain.model.sales.shipping.Shipping;
import com.example.sms.presentation.api.sales.shipping.ShippingCriteriaResource;
import com.example.sms.presentation.api.sales.shipping.ShippingResource;
import com.example.sms.presentation.api.sales.shipping.ShippingResourceDTOMapper;
import com.example.sms.service.sales.shipping.ShippingCriteria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("出荷DTOマッパーテスト")
class ShippingResourceDTOMapperTest {

    @Test
    @DisplayName("出荷リソースを出荷エンティティに変換する")
    void testConvertToEntity_validResource_shouldReturnShipping() {
        // Arrange
        String orderNumber = "OD00000001";
        LocalDateTime orderDate = LocalDateTime.now();
        String departmentCode = "10000";
        LocalDateTime departmentStartDate = LocalDateTime.now().minusDays(30);
        String customerCode = "001";
        Integer customerBranchNumber = 1;
        String employeeCode = "EMP001";
        LocalDateTime desiredDeliveryDate = LocalDateTime.now().plusDays(7);
        String customerOrderNumber = "ORD001";
        String warehouseCode = "W001";
        Integer totalOrderAmount = 100000;
        Integer totalConsumptionTax = 10000;
        String remarks = "テスト出荷";
        Integer orderLineNumber = 1;
        String productCode = "P0001";
        String productName = "テスト商品";
        Integer salesUnitPrice = 10000;
        Integer orderQuantity = 10;
        Integer taxRate = 10;
        Integer allocationQuantity = 10;
        Integer shipmentInstructionQuantity = 10;
        Integer shippedQuantity = 5;
        Boolean completionFlag = false;
        Integer discountAmount = 0;
        LocalDateTime deliveryDate = LocalDateTime.now().plusDays(14);
        Integer salesAmount = 100000;
        Integer consumptionTaxAmount = 10000;

        ShippingResource resource = new ShippingResource();
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
        resource.setOrderLineNumber(orderLineNumber);
        resource.setProductCode(productCode);
        resource.setProductName(productName);
        resource.setSalesUnitPrice(salesUnitPrice);
        resource.setOrderQuantity(orderQuantity);
        resource.setTaxRate(taxRate);
        resource.setAllocationQuantity(allocationQuantity);
        resource.setShipmentInstructionQuantity(shipmentInstructionQuantity);
        resource.setShippedQuantity(shippedQuantity);
        resource.setCompletionFlag(completionFlag);
        resource.setDiscountAmount(discountAmount);
        resource.setDeliveryDate(deliveryDate);
        resource.setSalesAmount(salesAmount);
        resource.setConsumptionTaxAmount(consumptionTaxAmount);

        // Act
        Shipping shipping = ShippingResourceDTOMapper.convertToEntity(resource);

        // Assert
        assertNotNull(shipping);
        assertEquals(orderNumber, shipping.getOrderNumber().getValue());
        assertEquals(orderDate, shipping.getOrderDate().getValue());
        assertEquals(departmentCode, shipping.getDepartmentCode().getValue());
        assertEquals(departmentStartDate, shipping.getDepartmentStartDate());
        assertEquals(customerCode, shipping.getCustomerCode().getCode().getValue());
        assertEquals(customerBranchNumber, shipping.getCustomerCode().getBranchNumber());
        assertEquals(employeeCode, shipping.getEmployeeCode().getValue());
        assertEquals(desiredDeliveryDate, shipping.getDesiredDeliveryDate().getValue());
        assertEquals(customerOrderNumber, shipping.getCustomerOrderNumber());
        assertEquals(warehouseCode, shipping.getWarehouseCode());
        assertEquals(totalOrderAmount, shipping.getTotalOrderAmount().getAmount());
        assertEquals(totalConsumptionTax, shipping.getTotalConsumptionTax().getAmount());
        assertEquals(remarks, shipping.getRemarks());
        assertEquals(orderLineNumber, shipping.getOrderLineNumber());
        assertEquals(productCode, shipping.getProductCode().getValue());
        assertEquals(productName, shipping.getProductName());
        assertEquals(salesUnitPrice, shipping.getSalesUnitPrice().getAmount());
        assertEquals(orderQuantity, shipping.getOrderQuantity().getAmount());
        assertEquals(taxRate, shipping.getTaxRate().getRate());
        assertEquals(allocationQuantity, shipping.getAllocationQuantity().getAmount());
        assertEquals(shipmentInstructionQuantity, shipping.getShipmentInstructionQuantity().getAmount());
        assertEquals(shippedQuantity, shipping.getShippedQuantity().getAmount());
        assertEquals(CompletionFlag.未完了, shipping.getCompletionFlag());
        assertEquals(discountAmount, shipping.getDiscountAmount().getAmount());
        assertEquals(deliveryDate, shipping.getDeliveryDate().getValue());
    }

    @Test
    @DisplayName("出荷検索条件リソースを出荷検索条件に変換する")
    void testConvertToCriteria_validResource_shouldReturnShippingCriteria() {
        // Arrange
        String orderNumber = "1000000001";
        String orderDate = "2023-10-01";
        String departmentCode = "D0001";
        String departmentStartDate = "2023-01-01";
        String customerCode = "C0001";
        Integer customerBranchNumber = 1;
        String employeeCode = "E0001";
        String desiredDeliveryDate = "2023-10-15";
        String customerOrderNumber = "ORD001";
        String warehouseCode = "W001";
        String remarks = "テスト条件";
        Integer orderLineNumber = 1;
        String productCode = "P0001";
        String productName = "テスト商品";
        String deliveryDate = "2023-10-20";
        Boolean completionFlag = false;

        ShippingCriteriaResource resource = new ShippingCriteriaResource();
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
        resource.setOrderLineNumber(orderLineNumber);
        resource.setProductCode(productCode);
        resource.setProductName(productName);
        resource.setDeliveryDate(deliveryDate);
        resource.setCompletionFlag(completionFlag);

        // Act
        ShippingCriteria criteria = ShippingResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(criteria);
        assertEquals(orderNumber, criteria.getOrderNumber());
        assertEquals(departmentCode, criteria.getDepartmentCode());
        assertEquals(customerCode, criteria.getCustomerCode());
        assertEquals(employeeCode, criteria.getEmployeeCode());
        assertEquals(customerOrderNumber, criteria.getCustomerOrderNumber());
        assertEquals(warehouseCode, criteria.getWarehouseCode());
        assertEquals(remarks, criteria.getRemarks());
        assertEquals(orderLineNumber, criteria.getOrderLineNumber());
        assertEquals(productCode, criteria.getProductCode());
        assertEquals(productName, criteria.getProductName());
        assertEquals(completionFlag, criteria.getCompletionFlag());
    }

    @Test
    @DisplayName("出荷リソースがnullの場合はnullを返す")
    void testConvertToEntity_nullResource_shouldReturnNull() {
        // Arrange
        ShippingResource resource = null;

        // Act
        Shipping shipping = ShippingResourceDTOMapper.convertToEntity(resource);

        // Assert
        assertNull(shipping);
    }

    @Test
    @DisplayName("出荷検索条件リソースがnullの場合はnullを返す")
    void testConvertToCriteria_nullResource_shouldReturnNull() {
        // Arrange
        ShippingCriteriaResource resource = null;

        // Act
        ShippingCriteria criteria = ShippingResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNull(criteria);
    }
}