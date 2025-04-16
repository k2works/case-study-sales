package com.example.sms.presentation.api.sales;

import com.example.sms.domain.model.sales.Sales;
import com.example.sms.domain.model.sales.SalesLine;
import com.example.sms.service.sales.SalesCriteria;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SalesResourceDTOMapper {

    /**
     * SalesResource を Sales エンティティに変換
     */
    public static Sales convertToEntity(SalesResource resource) {
        if (resource == null) {
            return null; // resourceがnullの場合はnullを返す
        }

        List<SalesLine> salesLines = resource.getSalesLines() != null
                ? resource.getSalesLines().stream()
                .map(line -> line != null
                                ? SalesLine.of(
                                line.getSalesNumber(),
                                line.getSalesLineNumber(),
                                line.getProductCode(),
                                line.getProductName(),
                                Optional.ofNullable(line.getSalesUnitPrice()).orElse(0),
                                Optional.ofNullable(line.getSalesQuantity()).orElse(0),
                                Optional.ofNullable(line.getShippedQuantity()).orElse(0),
                                Optional.ofNullable(line.getDiscountAmount()).orElse(0),
                                line.getBillingDate(),
                                line.getBillingNumber(),
                                line.getBillingDelayCategory(),
                                line.getAutoJournalDate(),
                        null
                        )
                                : null
                )
                .filter(Objects::nonNull) // null要素を除外
                .toList()
                : null; // getSalesLinesがnullの場合

        return Sales.of(
                resource.getSalesNumber(),
                resource.getOrderNumber(),
                resource.getSalesDate(),
                resource.getSalesType(),
                resource.getDepartmentCode(),
                resource.getDepartmentStartDate(),
                resource.getCustomerCode(),
                resource.getEmployeeCode(),
                resource.getVoucherNumber(),
                resource.getOriginalVoucherNumber(),
                resource.getRemarks(),
                salesLines
        );
    }

    /**
     * Sales エンティティを SalesResource に変換
     */
    public static SalesResource convertToResource(Sales sales) {
        SalesResource resource = new SalesResource();
        resource.setSalesNumber(sales.getSalesNumber().getValue());
        resource.setOrderNumber(sales.getOrderNumber().getValue());
        resource.setSalesDate(sales.getSalesDate().getValue());
        resource.setSalesType(sales.getSalesType().getCode());
        resource.setDepartmentCode(sales.getDepartmentId().getDeptCode().getValue());
        resource.setDepartmentStartDate(sales.getDepartmentId().getDepartmentStartDate().getValue());
        resource.setCustomerCode(sales.getCustomerCode().getValue());
        resource.setEmployeeCode(sales.getEmployeeCode().getValue());
        resource.setTotalSalesAmount(sales.getTotalSalesAmount().getAmount());
        resource.setTotalConsumptionTax(sales.getTotalConsumptionTax().getAmount());
        resource.setRemarks(sales.getRemarks());
        resource.setVoucherNumber(sales.getVoucherNumber());
        resource.setOriginalVoucherNumber(sales.getOriginalVoucherNumber());
        resource.setSalesLines(SalesLineResource.from(sales.getSalesLines()));
        return resource;
    }

    public static SalesCriteria convertToCriteria(SalesCriteriaResource resource) {
        LocalDate salesDate = resource.getSalesDate() != null ? LocalDate.parse(resource.getSalesDate()) : null;

        return SalesCriteria.builder()
                .salesNumber(resource.getSalesNumber())
                .orderNumber(resource.getOrderNumber())
                .salesDate(salesDate != null ? salesDate.toString() : null) // SalesCriteria では String
                .departmentCode(resource.getDepartmentCode())
                .remarks(resource.getRemarks())
                .build();
    }
}
