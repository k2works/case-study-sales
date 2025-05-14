package com.example.sms.presentation.api.sales.sales;

import com.example.sms.domain.model.sales.sales.Sales;
import com.example.sms.domain.model.sales.sales.SalesType;
import com.example.sms.domain.model.sales.order.TaxRateType;
import com.example.sms.service.sales.sales.SalesCriteria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("売上DTOマッパーテスト")
class SalesResourceDTOMapperTest {

    @Test
    @DisplayName("売上リソースを売上エンティティに変換する")
    void testConvertToEntity_validResource_shouldReturnSales() {
        // Arrange
        String salesNumber = "SA12345678";
        String orderNumber = "OD12345678";
        LocalDateTime salesDate = LocalDateTime.now();
        SalesType salesCategory = SalesType.現金;
        String departmentCode = "10000";
        LocalDateTime departmentStartDate = LocalDateTime.now().minusDays(30);
        String customerCode = "001";
        Integer customerBranchNumber = 1;
        String employeeCode = "EMP001";
        Integer totalSalesAmount = 100000;
        Integer totalConsumptionTax = 10000;
        String remarks = "テスト売上";
        Integer voucherNumber = 12345;
        String originalVoucherNumber = "ORIG123";

        SalesLineResource lineResource = SalesLineResource.builder()
                .salesNumber(salesNumber)
                .salesLineNumber(1)
                .productCode("P001")
                .productName("商品001")
                .salesUnitPrice(10000)
                .salesQuantity(10)
                .shippedQuantity(10)
                .discountAmount(0)
                .billingDate(salesDate)
                .billingNumber("BILL123")
                .billingDelayCategory(0)
                .autoJournalDate(salesDate.plusDays(1))
                .taxRate(TaxRateType.標準税率)
                .build();

        SalesResource resource = new SalesResource();
        resource.setSalesNumber(salesNumber);
        resource.setOrderNumber(orderNumber);
        resource.setSalesDate(salesDate);
        resource.setSalesType(salesCategory);
        resource.setDepartmentCode(departmentCode);
        resource.setDepartmentStartDate(departmentStartDate);
        resource.setCustomerCode(customerCode);
        resource.setCustomerBranchNumber(customerBranchNumber);
        resource.setEmployeeCode(employeeCode);
        resource.setTotalSalesAmount(totalSalesAmount);
        resource.setTotalConsumptionTax(totalConsumptionTax);
        resource.setRemarks(remarks);
        resource.setVoucherNumber(voucherNumber);
        resource.setOriginalVoucherNumber(originalVoucherNumber);
        resource.setSalesLines(List.of(lineResource));

        // Act
        Sales sales = SalesResourceDTOMapper.convertToEntity(resource);

        // Assert
        assertNotNull(sales);
        assertEquals(salesNumber, sales.getSalesNumber().getValue());
        assertEquals(orderNumber, sales.getOrderNumber().getValue());
        assertEquals(salesDate, sales.getSalesDate().getValue());
        assertEquals(salesCategory, sales.getSalesType());
        assertEquals(departmentCode, sales.getDepartmentId().getDeptCode().getValue());
        assertEquals(departmentStartDate, sales.getDepartmentId().getDepartmentStartDate().getValue());
        assertEquals(customerCode, sales.getPartnerCode().getValue());
        assertEquals(employeeCode, sales.getEmployeeCode().getValue());
        assertEquals(totalSalesAmount, sales.getTotalSalesAmount().getAmount());
        assertEquals(totalConsumptionTax, sales.getTotalConsumptionTax().getAmount());
        assertEquals(remarks, sales.getRemarks());
        assertNotNull(sales.getSalesLines());
        assertEquals(1, sales.getSalesLines().size());
    }

    @Test
    @DisplayName("売上検索条件リソースを売上検索条件に変換する")
    void testConvertToCriteria_validResource_shouldReturnSalesCriteria() {
        // Arrange
        String salesNumber = "SA12345678";
        String orderNumber = "OD12345678";
        String salesDate = "2023-10-01";
        String departmentCode = "DPT001";
        String remarks = "テスト条件";

        SalesCriteriaResource resource = new SalesCriteriaResource();
        resource.setSalesNumber(salesNumber);
        resource.setOrderNumber(orderNumber);
        resource.setSalesDate(salesDate);
        resource.setDepartmentCode(departmentCode);
        resource.setRemarks(remarks);

        // Act
        SalesCriteria criteria = SalesResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(criteria);
        assertEquals(salesNumber, criteria.getSalesNumber());
        assertEquals(orderNumber, criteria.getOrderNumber());
        assertEquals(salesDate, criteria.getSalesDate());
        assertEquals(departmentCode, criteria.getDepartmentCode());
        assertEquals(remarks, criteria.getRemarks());
    }

    @Test
    @DisplayName("売上リソースの売上明細がnullの場合は例外をスローする")
    void testConvertToEntity_nullSalesLines_shouldThrowException() {
        // Arrange
        SalesResource resource = new SalesResource();
        resource.setSalesLines(null);

        // Act & Assert
        Exception exception = assertThrows(NullPointerException.class, () -> SalesResourceDTOMapper.convertToEntity(resource));
        assertNotNull(exception.getMessage());
    }
}