package com.example.sms.presentation.api.sales.invoice;

import com.example.sms.domain.model.sales.invoice.Invoice;
import com.example.sms.domain.model.sales.invoice.InvoiceLine;
import com.example.sms.service.sales.invoice.InvoiceCriteria;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceResourceDTOMapperTest {

    @Test
    void testConvertToEntity_validResource_shouldReturnInvoice() {
        // Arrange
        InvoiceResource resource = new InvoiceResource();
        resource.setInvoiceNumber("IV00000001");
        resource.setInvoiceDate(LocalDateTime.of(2024, 1, 15, 10, 0));
        resource.setPartnerCode("001");
        resource.setCustomerCode("001");
        resource.setCustomerBranchNumber(1);
        resource.setPreviousPaymentAmount(10000);
        resource.setCurrentMonthSalesAmount(20000);
        resource.setCurrentMonthPaymentAmount(15000);
        resource.setCurrentMonthInvoiceAmount(25000);
        resource.setConsumptionTaxAmount(2500);
        resource.setInvoiceReconciliationAmount(5000);

        List<InvoiceLineResource> lineResources = new ArrayList<>();
        InvoiceLineResource lineResource = new InvoiceLineResource();
        lineResource.setInvoiceNumber("IV00000001");
        lineResource.setSalesNumber("SA00000001");
        lineResource.setSalesLineNumber(1);
        lineResources.add(lineResource);
        resource.setInvoiceLines(lineResources);

        // Act
        Invoice invoice = InvoiceResourceDTOMapper.convertToEntity(resource);

        // Assert
        assertNotNull(invoice);
        assertEquals("IV00000001", invoice.getInvoiceNumber().getValue());
        assertEquals(LocalDateTime.of(2024, 1, 15, 10, 0), invoice.getInvoiceDate());
        assertEquals("001", invoice.getPartnerCode());
        assertEquals("001", invoice.getCustomerCode().getCode().getValue());
        assertEquals(1, invoice.getCustomerCode().getBranchNumber());
        assertEquals(10000, invoice.getPreviousPaymentAmount().getAmount());
        assertEquals(20000, invoice.getCurrentMonthSalesAmount().getAmount());
        assertEquals(15000, invoice.getCurrentMonthPaymentAmount().getAmount());
        assertEquals(25000, invoice.getCurrentMonthInvoiceAmount().getAmount());
        assertEquals(2500, invoice.getConsumptionTaxAmount().getAmount());
        assertEquals(5000, invoice.getInvoiceReconciliationAmount().getAmount());
        assertNotNull(invoice.getInvoiceLines());
        assertEquals(1, invoice.getInvoiceLines().size());
        assertEquals("IV00000001", invoice.getInvoiceLines().get(0).getInvoiceNumber().getValue());
        assertEquals("SA00000001", invoice.getInvoiceLines().get(0).getSalesNumber().getValue());
        assertEquals(1, invoice.getInvoiceLines().get(0).getSalesLineNumber());
    }

    @Test
    void testConvertToResource_validEntity_shouldReturnResource() {
        // Arrange
        Invoice invoice = Invoice.of(
                "IV00000001",
                LocalDateTime.of(2024, 1, 15, 10, 0),
                "001",
                1,
                10000,
                20000,
                15000,
                25000,
                2500,
                5000,
                List.of(InvoiceLine.of("IV00000001", "SA00000001", 1))
        );

        // Act
        InvoiceResource resource = InvoiceResourceDTOMapper.convertToResource(invoice);

        // Assert
        assertNotNull(resource);
        assertEquals("IV00000001", resource.getInvoiceNumber());
        assertEquals(LocalDateTime.of(2024, 1, 15, 10, 0), resource.getInvoiceDate());
        assertEquals("001", resource.getPartnerCode());
        assertEquals("001", resource.getCustomerCode());
        assertEquals(1, resource.getCustomerBranchNumber());
        assertEquals(10000, resource.getPreviousPaymentAmount());
        assertEquals(20000, resource.getCurrentMonthSalesAmount());
        assertEquals(15000, resource.getCurrentMonthPaymentAmount());
        assertEquals(25000, resource.getCurrentMonthInvoiceAmount());
        assertEquals(2500, resource.getConsumptionTaxAmount());
        assertEquals(5000, resource.getInvoiceReconciliationAmount());
        assertNotNull(resource.getInvoiceLines());
        assertEquals(1, resource.getInvoiceLines().size());
        assertEquals("IV00000001", resource.getInvoiceLines().get(0).getInvoiceNumber());
        assertEquals("SA00000001", resource.getInvoiceLines().get(0).getSalesNumber());
        assertEquals(1, resource.getInvoiceLines().get(0).getSalesLineNumber());
    }

    @Test
    void testConvertToCriteria_validResource_shouldReturnInvoiceCriteria() {
        // Arrange
        InvoiceCriteriaResource resource = new InvoiceCriteriaResource();
        resource.setInvoiceNumber("IV00000001");
        resource.setInvoiceDate("2024-01-15");
        resource.setPartnerCode("001");
        resource.setCustomerCode("001");

        // Act
        InvoiceCriteria criteria = InvoiceResourceDTOMapper.convertToCriteria(resource);

        // Assert
        assertNotNull(criteria);
        assertEquals("IV00000001", criteria.getInvoiceNumber());
        assertEquals("2024-01-15", criteria.getInvoiceDate());
        assertEquals("001", criteria.getPartnerCode());
        assertEquals("001", criteria.getCustomerCode());
    }

    @Test
    void testConvertToEntity_nullResource_shouldReturnNull() {
        // Act
        Invoice invoice = InvoiceResourceDTOMapper.convertToEntity(null);

        // Assert
        assertNull(invoice);
    }

    @Test
    void testConvertToResource_nullEntity_shouldReturnNull() {
        // Act
        InvoiceResource resource = InvoiceResourceDTOMapper.convertToResource(null);

        // Assert
        assertNull(resource);
    }

    @Test
    void testConvertToCriteria_nullResource_shouldReturnNull() {
        // Act
        InvoiceCriteria criteria = InvoiceResourceDTOMapper.convertToCriteria(null);

        // Assert
        assertNull(criteria);
    }
}