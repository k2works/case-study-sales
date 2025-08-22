import { PageNationType } from "../../views/application/PageNation.tsx";

export enum CompletionFlagEnumType {
    未完了 = "0",
    完了 = "1"
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

export const mapToPurchaseOrderResource = (purchaseOrder: PurchaseOrderType) => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;

    type Resource = {
        purchaseOrderNumber: string;
        purchaseOrderDate: string;
        salesOrderNumber?: string;
        supplierCode: string;
        supplierBranchNumber: number;
        purchaseManagerCode: string;
        designatedDeliveryDate: string;
        warehouseCode?: string;
        totalPurchaseAmount: number;
        totalConsumptionTax: number;
        remarks?: string;
        purchaseOrderLines: {
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
        }[];
    };

    if (isEmpty(purchaseOrder.purchaseOrderNumber)) {
        throw new Error("Purchase order number is required.");
    }
    if (isEmpty(purchaseOrder.supplierCode)) {
        throw new Error("Supplier code is required.");
    }

    const resource: Resource = {
        purchaseOrderNumber: purchaseOrder.purchaseOrderNumber,
        purchaseOrderDate: purchaseOrder.purchaseOrderDate,
        salesOrderNumber: purchaseOrder.salesOrderNumber,
        supplierCode: purchaseOrder.supplierCode,
        supplierBranchNumber: purchaseOrder.supplierBranchNumber,
        purchaseManagerCode: purchaseOrder.purchaseManagerCode,
        designatedDeliveryDate: purchaseOrder.designatedDeliveryDate,
        warehouseCode: purchaseOrder.warehouseCode,
        totalPurchaseAmount: purchaseOrder.totalPurchaseAmount,
        totalConsumptionTax: purchaseOrder.totalConsumptionTax,
        remarks: purchaseOrder.remarks,
        purchaseOrderLines: purchaseOrder.purchaseOrderLines.map(line => ({
            purchaseOrderNumber: line.purchaseOrderNumber,
            purchaseOrderLineNumber: line.purchaseOrderLineNumber,
            purchaseOrderLineDisplayNumber: line.purchaseOrderLineDisplayNumber,
            salesOrderNumber: line.salesOrderNumber,
            salesOrderLineNumber: line.salesOrderLineNumber,
            productCode: line.productCode,
            productName: line.productName,
            purchaseUnitPrice: line.purchaseUnitPrice,
            purchaseOrderQuantity: line.purchaseOrderQuantity,
            receivedQuantity: line.receivedQuantity,
            completionFlag: line.completionFlag
        }))
    };

    return resource;
};

export const mapToPurchaseOrderSearchResource = (criteria: PurchaseOrderSearchCriteriaType) => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;

    type Resource = {
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

    const resource: Resource = {};

    if (!isEmpty(criteria.purchaseOrderNumber)) {
        resource.purchaseOrderNumber = criteria.purchaseOrderNumber;
    }
    if (!isEmpty(criteria.purchaseOrderDate)) {
        resource.purchaseOrderDate = criteria.purchaseOrderDate;
    }
    if (!isEmpty(criteria.salesOrderNumber)) {
        resource.salesOrderNumber = criteria.salesOrderNumber;
    }
    if (!isEmpty(criteria.supplierCode)) {
        resource.supplierCode = criteria.supplierCode;
    }
    if (!isEmpty(criteria.supplierName)) {
        resource.supplierName = criteria.supplierName;
    }
    if (!isEmpty(criteria.purchaseManagerCode)) {
        resource.purchaseManagerCode = criteria.purchaseManagerCode;
    }
    if (!isEmpty(criteria.designatedDeliveryDate)) {
        resource.designatedDeliveryDate = criteria.designatedDeliveryDate;
    }
    if (!isEmpty(criteria.warehouseCode)) {
        resource.warehouseCode = criteria.warehouseCode;
    }
    if (!isEmpty(criteria.completionFlag)) {
        resource.completionFlag = criteria.completionFlag === "true";
    }

    return resource;
};

export const mapToPurchaseOrderCriteriaResource = (criteria: PurchaseOrderCriteriaType) => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;

    type Resource = {
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

    const resource: Resource = {};

    if (!isEmpty(criteria.purchaseOrderNumber)) {
        resource.purchaseOrderNumber = criteria.purchaseOrderNumber;
    }
    if (!isEmpty(criteria.purchaseOrderDate)) {
        resource.purchaseOrderDate = criteria.purchaseOrderDate;
    }
    if (!isEmpty(criteria.salesOrderNumber)) {
        resource.salesOrderNumber = criteria.salesOrderNumber;
    }
    if (!isEmpty(criteria.supplierCode)) {
        resource.supplierCode = criteria.supplierCode;
    }
    if (!isEmpty(criteria.supplierName)) {
        resource.supplierName = criteria.supplierName;
    }
    if (!isEmpty(criteria.purchaseManagerCode)) {
        resource.purchaseManagerCode = criteria.purchaseManagerCode;
    }
    if (!isEmpty(criteria.designatedDeliveryDate)) {
        resource.designatedDeliveryDate = criteria.designatedDeliveryDate;
    }
    if (!isEmpty(criteria.warehouseCode)) {
        resource.warehouseCode = criteria.warehouseCode;
    }
    if (!isEmpty(criteria.completionFlag)) {
        resource.completionFlag = criteria.completionFlag;
    }

    return resource;
};