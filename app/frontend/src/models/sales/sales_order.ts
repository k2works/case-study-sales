import { PageNationType } from "../../views/application/PageNation.tsx";
import {toISOStringLocal } from "../../components/application/utils.ts";

export type SalesOrderLineType = {
    lineNumber: number;
    productCode: string;
    productName: string;
    quantity: number;
    unitPrice: number;
    amount: number;
    remarks: string;
}

export type SalesOrderType = {
    orderNumber: string;
    orderDate: string;
    departmentCode: string;
    departmentStartDate: string;
    customerCode: string;
    customerBranchNumber: number;
    employeeCode: string;
    desiredDeliveryDate: string;
    customerOrderNumber: string;
    warehouseCode: string;
    totalOrderAmount: number;
    totalConsumptionTax: number;
    remarks: string;
    salesOrderLines: SalesOrderLineType[];
    checked?: boolean;
}

export type SalesOrderFetchType = {
    list: SalesOrderType[];
} & PageNationType;

export type SalesOrderResourceType = {
    orderNumber: string;
    orderDate: string;
    departmentCode: string;
    departmentStartDate: string;
    customerCode: string;
    customerBranchNumber: number;
    employeeCode: string;
    desiredDeliveryDate: string;
    customerOrderNumber: string;
    warehouseCode: string;
    totalOrderAmount: number;
    totalConsumptionTax: number;
    remarks: string;
    salesOrderLines: SalesOrderLineType[];
}

export type SalesOrderCriteriaType = {
    orderNumber?: string;
    orderDate?: string;
    departmentCode?: string;
    departmentStartDate?: string;
    customerCode?: string;
    customerBranchNumber?: number;
    employeeCode?: string;
    desiredDeliveryDate?: string;
    customerOrderNumber?: string;
    warehouseCode?: string;
    remarks?: string;
}

export const mapToSalesOrderResource = (salesOrder: SalesOrderType): SalesOrderResourceType => {
    return {
        orderNumber: salesOrder.orderNumber,
        orderDate: toISOStringLocal(new Date(salesOrder.orderDate)),
        departmentCode: salesOrder.departmentCode,
        departmentStartDate: toISOStringLocal(new Date(salesOrder.departmentStartDate)),
        customerCode: salesOrder.customerCode,
        customerBranchNumber: salesOrder.customerBranchNumber,
        employeeCode: salesOrder.employeeCode,
        desiredDeliveryDate: toISOStringLocal(new Date(salesOrder.desiredDeliveryDate)),
        customerOrderNumber: salesOrder.customerOrderNumber,
        warehouseCode: salesOrder.warehouseCode,
        totalOrderAmount: salesOrder.totalOrderAmount,
        totalConsumptionTax: salesOrder.totalConsumptionTax,
        remarks: salesOrder.remarks,
        salesOrderLines: salesOrder.salesOrderLines
    };
};

export const mapToSalesOrderCriteriaResource = (criteria: SalesOrderCriteriaType) => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    type Resource = {
        orderNumber?: string;
        orderDate?: string;
        departmentCode?: string;
        departmentStartDate?: string;
        customerCode?: string;
        customerBranchNumber?: number;
        employeeCode?: string;
        desiredDeliveryDate?: string;
        customerOrderNumber?: string;
        warehouseCode?: string;
        remarks?: string;
    };
    const resource: Resource = {
        ...(isEmpty(criteria.orderNumber) ? {} : { orderNumber: criteria.orderNumber }),
        ...(isEmpty(criteria.orderDate) ? {} : { orderDate: toISOStringLocal(new Date(criteria.orderDate)) }),
        ...(isEmpty(criteria.departmentCode) ? {} : { departmentCode: criteria.departmentCode }),
        ...(isEmpty(criteria.departmentStartDate) ? {} : { departmentStartDate: toISOStringLocal(new Date(criteria.departmentStartDate)) }),
        ...(isEmpty(criteria.customerCode) ? {} : { customerCode: criteria.customerCode }),
        ...(isEmpty(criteria.customerBranchNumber) ? {} : { customerBranchNumber: criteria.customerBranchNumber }),
        ...(isEmpty(criteria.employeeCode) ? {} : { employeeCode: criteria.employeeCode }),
        ...(isEmpty(criteria.desiredDeliveryDate) ? {} : { desiredDeliveryDate: toISOStringLocal(new Date(criteria.desiredDeliveryDate)) }),
        ...(isEmpty(criteria.customerOrderNumber) ? {} : { customerOrderNumber: criteria.customerOrderNumber }),
        ...(isEmpty(criteria.warehouseCode) ? {} : { warehouseCode: criteria.warehouseCode }),
        ...(isEmpty(criteria.remarks) ? {} : { remarks: criteria.remarks })
    };

    return resource;
};