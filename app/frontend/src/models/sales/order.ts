import { PageNationType } from "../../views/application/PageNation.tsx";
import {toISOStringLocal } from "../../components/application/utils.ts";
import {SalesLineType} from "./sales.ts";

export enum TaxRateEnumType {
    標準税率 = "標準税率",
    軽減税率 = "軽減税率",
    非課税 = "非課税"
}

export const TaxRateValues = {
    [TaxRateEnumType.標準税率]: 0.10,
    [TaxRateEnumType.軽減税率]: 0.08
};

export enum CompletionFlagEnumType {
    未完了 = "未完了",
    完了 = "完了"
}

export type SalesOrderLineType = {
    orderNumber: string;
    orderLineNumber: number;
    productCode: string;
    productName: string;
    salesUnitPrice: number;
    orderQuantity: number;
    taxRate: TaxRateEnumType;
    allocationQuantity: number;
    shipmentInstructionQuantity: number;
    shippedQuantity: number;
    completionFlag: CompletionFlagEnumType;
    discountAmount: number;
    deliveryDate: string;
    shippingDate?: string;
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

export const mapToSalesOrderResource = (salesOrder: SalesOrderType): SalesOrderType => {
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
    return  {
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
};

export const getTaxRate = (line: SalesOrderLineType | SalesLineType) => {
    return line.taxRate === TaxRateEnumType.標準税率 ? TaxRateValues[TaxRateEnumType.標準税率] : line.taxRate === TaxRateEnumType.軽減税率 ? TaxRateValues[TaxRateEnumType.軽減税率] : 0;
}
