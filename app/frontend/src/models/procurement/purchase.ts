import { PageNationType } from "../../views/application/PageNation.tsx";
import { toISOStringLocal } from "../../components/application/utils.ts";

export type PurchaseLineType = {
    purchaseNumber: string;
    purchaseLineNumber: number;
    purchaseLineDisplayNumber: number;
    purchaseOrderLineNumber: number;
    productCode: string;
    warehouseCode: string;
    productName?: string;
    purchaseUnitPrice: number;
    purchaseQuantity: number;
    checked?: boolean;
};

export type PurchaseType = {
    purchaseNumber: string;
    purchaseDate: string;
    supplierCode: string;
    supplierBranchNumber: number;
    purchaseManagerCode: string;
    startDate: string;
    purchaseOrderNumber?: string;
    departmentCode: string;
    totalPurchaseAmount: number;
    totalConsumptionTax: number;
    remarks?: string;
    purchaseLines: PurchaseLineType[];
    checked?: boolean;
};

export type PurchaseCriteriaType = {
    purchaseNumber?: string;
    purchaseDate?: string;
    purchaseOrderNumber?: string;
    supplierCode?: string;
    supplierBranchNumber?: number;
    purchaseManagerCode?: string;
    departmentCode?: string;
};

export type PurchaseSearchCriteriaType = {
    purchaseNumber?: string;
    purchaseDate?: string;
    purchaseOrderNumber?: string;
    supplierCode?: string;
    supplierBranchNumber?: string;
    purchaseManagerCode?: string;
    departmentCode?: string;
};

export type PurchaseFetchType = {
    list: PurchaseType[];
} & PageNationType;

export type PurchasePageNationType = PageNationType<PurchaseCriteriaType>;

export const initialPurchase: PurchaseType = {
    purchaseNumber: "",
    purchaseDate: new Date().toISOString().split('T')[0],
    supplierCode: "",
    supplierBranchNumber: 0,
    purchaseManagerCode: "",
    startDate: new Date().toISOString().split('T')[0],
    purchaseOrderNumber: "",
    departmentCode: "",
    totalPurchaseAmount: 0,
    totalConsumptionTax: 0,
    remarks: "",
    purchaseLines: [],
    checked: false
};

export const initialPurchaseLine: PurchaseLineType = {
    purchaseNumber: "",
    purchaseLineNumber: 0,
    purchaseLineDisplayNumber: 0,
    purchaseOrderLineNumber: 0,
    productCode: "",
    warehouseCode: "",
    productName: "",
    purchaseUnitPrice: 0,
    purchaseQuantity: 0,
    checked: false
};

export const initialPurchaseCriteria: PurchaseCriteriaType = {
    purchaseNumber: "",
    purchaseDate: "",
    purchaseOrderNumber: "",
    supplierCode: "",
    supplierBranchNumber: 0,
    purchaseManagerCode: "",
    departmentCode: ""
};

export const mapToPurchaseResource = (purchase: PurchaseType) => {
    return {
        purchaseNumber: purchase.purchaseNumber,
        purchaseDate: purchase.purchaseDate ? toISOStringLocal(new Date(purchase.purchaseDate)) : null,
        supplierCode: purchase.supplierCode,
        supplierBranchNumber: purchase.supplierBranchNumber,
        purchaseManagerCode: purchase.purchaseManagerCode,
        startDate: purchase.startDate ? toISOStringLocal(new Date(purchase.startDate)) : null,
        purchaseOrderNumber: purchase.purchaseOrderNumber,
        departmentCode: purchase.departmentCode,
        totalPurchaseAmount: purchase.totalPurchaseAmount,
        totalConsumptionTax: purchase.totalConsumptionTax,
        remarks: purchase.remarks,
        purchaseLines: purchase.purchaseLines.map(line => ({
            purchaseNumber: line.purchaseNumber,
            purchaseLineNumber: line.purchaseLineNumber,
            purchaseLineDisplayNumber: line.purchaseLineDisplayNumber,
            purchaseOrderLineNumber: line.purchaseOrderLineNumber,
            productCode: line.productCode,
            warehouseCode: line.warehouseCode,
            productName: line.productName,
            purchaseUnitPrice: line.purchaseUnitPrice,
            purchaseQuantity: line.purchaseQuantity
        }))
    };
};

export const mapToPurchaseSearchResource = (criteria: PurchaseSearchCriteriaType) => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    return {
        ...(isEmpty(criteria.purchaseNumber) ? {} : { purchaseNumber: criteria.purchaseNumber }),
        ...(isEmpty(criteria.purchaseDate) ? {} : { purchaseDate: toISOStringLocal(new Date(criteria.purchaseDate)) }),
        ...(isEmpty(criteria.purchaseOrderNumber) ? {} : { purchaseOrderNumber: criteria.purchaseOrderNumber }),
        ...(isEmpty(criteria.supplierCode) ? {} : { supplierCode: criteria.supplierCode }),
        ...(isEmpty(criteria.supplierBranchNumber) ? {} : { supplierBranchNumber: Number(criteria.supplierBranchNumber) }),
        ...(isEmpty(criteria.purchaseManagerCode) ? {} : { purchaseManagerCode: criteria.purchaseManagerCode }),
        ...(isEmpty(criteria.departmentCode) ? {} : { departmentCode: criteria.departmentCode })
    };
};

export const mapToPurchaseCriteriaResource = (criteria: PurchaseCriteriaType) => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    return {
        ...(isEmpty(criteria.purchaseNumber) ? {} : { purchaseNumber: criteria.purchaseNumber }),
        ...(isEmpty(criteria.purchaseDate) ? {} : { purchaseDate: toISOStringLocal(new Date(criteria.purchaseDate)) }),
        ...(isEmpty(criteria.purchaseOrderNumber) ? {} : { purchaseOrderNumber: criteria.purchaseOrderNumber }),
        ...(isEmpty(criteria.supplierCode) ? {} : { supplierCode: criteria.supplierCode }),
        ...(isEmpty(criteria.supplierBranchNumber) ? {} : { supplierBranchNumber: criteria.supplierBranchNumber }),
        ...(isEmpty(criteria.purchaseManagerCode) ? {} : { purchaseManagerCode: criteria.purchaseManagerCode }),
        ...(isEmpty(criteria.departmentCode) ? {} : { departmentCode: criteria.departmentCode })
    };
};
