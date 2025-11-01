import { PageNationType } from "../../views/application/PageNation.tsx";
import { toISOStringLocal } from "../../components/application/utils.ts";

export enum CompletionFlagEnumType {
    未完了 = "未完了",
    完了 = "完了"
}

export type PurchaseOrderLineType = {
    purchaseOrderNumber: string;
    purchaseOrderLineNumber: number;
    purchaseOrderLineDisplayNumber: number;
    salesOrderNumber?: string;
    salesOrderLineNumber?: number;
    productCode: string;
    productName?: string;
    purchaseUnitPrice: number;
    purchaseOrderQuantity: number;
    receivedQuantity: number;
    completionFlag: CompletionFlagEnumType;
    checked?: boolean;
};

export type PurchaseOrderType = {
    purchaseOrderNumber: string;
    purchaseOrderDate: string;
    salesOrderNumber?: string;
    supplierCode: string;
    supplierName?: string;
    supplierBranchNumber: number;
    purchaseManagerCode: string;
    purchaseManagerName?: string;
    designatedDeliveryDate: string;
    warehouseCode?: string;
    totalPurchaseAmount: number;
    totalConsumptionTax: number;
    remarks?: string;
    purchaseOrderLines: PurchaseOrderLineType[];
    checked?: boolean;
};

export type PurchaseOrderCriteriaType = {
    purchaseOrderNumber?: string;
    purchaseOrderDate?: string;
    salesOrderNumber?: string;
    supplierCode?: string;
    supplierName?: string;
    purchaseManagerCode?: string;
    designatedDeliveryDate?: string;
    warehouseCode?: string;
    completionFlag?: boolean;
};

export type PurchaseOrderSearchCriteriaType = {
    purchaseOrderNumber?: string;
    purchaseOrderDate?: string;
    salesOrderNumber?: string;
    supplierCode?: string;
    supplierName?: string;
    purchaseManagerCode?: string;
    designatedDeliveryDate?: string;
    warehouseCode?: string;
    completionFlag?: string;
};

export type PurchaseOrderFetchType = {
    list: PurchaseOrderType[];
} & PageNationType;

export type PurchaseOrderPageNationType = PageNationType<PurchaseOrderCriteriaType>;

export const mapToPurchaseOrderResource = (purchaseOrder: PurchaseOrderType): PurchaseOrderType => {
    return {
        purchaseOrderNumber: purchaseOrder.purchaseOrderNumber,
        purchaseOrderDate: toISOStringLocal(new Date(purchaseOrder.purchaseOrderDate)),
        salesOrderNumber: purchaseOrder.salesOrderNumber,
        supplierCode: purchaseOrder.supplierCode,
        supplierName: purchaseOrder.supplierName,
        supplierBranchNumber: purchaseOrder.supplierBranchNumber,
        purchaseManagerCode: purchaseOrder.purchaseManagerCode,
        purchaseManagerName: purchaseOrder.purchaseManagerName,
        designatedDeliveryDate: toISOStringLocal(new Date(purchaseOrder.designatedDeliveryDate)),
        warehouseCode: purchaseOrder.warehouseCode,
        totalPurchaseAmount: purchaseOrder.totalPurchaseAmount,
        totalConsumptionTax: purchaseOrder.totalConsumptionTax,
        remarks: purchaseOrder.remarks,
        purchaseOrderLines: purchaseOrder.purchaseOrderLines
    };
};

export const mapToPurchaseOrderSearchResource = (criteria: PurchaseOrderSearchCriteriaType) => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    return {
        ...(isEmpty(criteria.purchaseOrderNumber) ? {} : { purchaseOrderNumber: criteria.purchaseOrderNumber }),
        ...(isEmpty(criteria.purchaseOrderDate) ? {} : { purchaseOrderDate: toISOStringLocal(new Date(criteria.purchaseOrderDate)) }),
        ...(isEmpty(criteria.salesOrderNumber) ? {} : { salesOrderNumber: criteria.salesOrderNumber }),
        ...(isEmpty(criteria.supplierCode) ? {} : { supplierCode: criteria.supplierCode }),
        ...(isEmpty(criteria.supplierName) ? {} : { supplierName: criteria.supplierName }),
        ...(isEmpty(criteria.purchaseManagerCode) ? {} : { purchaseManagerCode: criteria.purchaseManagerCode }),
        ...(isEmpty(criteria.designatedDeliveryDate) ? {} : { designatedDeliveryDate: toISOStringLocal(new Date(criteria.designatedDeliveryDate)) }),
        ...(isEmpty(criteria.warehouseCode) ? {} : { warehouseCode: criteria.warehouseCode }),
        ...(isEmpty(criteria.completionFlag) ? {} : { completionFlag: criteria.completionFlag === "true" })
    };
};

export const mapToPurchaseOrderCriteriaResource = (criteria: PurchaseOrderCriteriaType) => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    return {
        ...(isEmpty(criteria.purchaseOrderNumber) ? {} : { purchaseOrderNumber: criteria.purchaseOrderNumber }),
        ...(isEmpty(criteria.purchaseOrderDate) ? {} : { purchaseOrderDate: toISOStringLocal(new Date(criteria.purchaseOrderDate)) }),
        ...(isEmpty(criteria.salesOrderNumber) ? {} : { salesOrderNumber: criteria.salesOrderNumber }),
        ...(isEmpty(criteria.supplierCode) ? {} : { supplierCode: criteria.supplierCode }),
        ...(isEmpty(criteria.supplierName) ? {} : { supplierName: criteria.supplierName }),
        ...(isEmpty(criteria.purchaseManagerCode) ? {} : { purchaseManagerCode: criteria.purchaseManagerCode }),
        ...(isEmpty(criteria.designatedDeliveryDate) ? {} : { designatedDeliveryDate: toISOStringLocal(new Date(criteria.designatedDeliveryDate)) }),
        ...(isEmpty(criteria.warehouseCode) ? {} : { warehouseCode: criteria.warehouseCode }),
        ...(isEmpty(criteria.completionFlag) ? {} : { completionFlag: criteria.completionFlag })
    };
};
