import {PageNationType} from "../../views/application/PageNation";
import {TaxRateEnumType} from "./order.ts";
import {toISOStringLocal} from "../../components/application/utils.ts";

export enum SalesTypeEnumType {
    現金 = "現金",
    掛 = "掛",
    その他 = "その他"
}

export const SalesTypeValues = {
    [SalesTypeEnumType.現金]: 0,
    [SalesTypeEnumType.掛]: 1,
    [SalesTypeEnumType.その他]: 2
};

// 売上明細情報
export interface SalesLineType {
    salesNumber: string;
    salesLineNumber: number;
    productCode: string;
    productName: string;
    salesUnitPrice: number;
    salesQuantity: number;
    shippedQuantity: number;
    discountAmount: number;
    billingDate: string;
    billingNumber: string;
    billingDelayCategory: number;
    autoJournalDate: string;
    taxRate: TaxRateEnumType;
    checked?: boolean;
}

// 売上情報
export interface SalesType {
    salesNumber: string;
    orderNumber: string;
    salesDate: string;
    salesType: SalesTypeEnumType;
    departmentCode: string;
    departmentStartDate: string;
    customerCode: string;
    employeeCode: string;
    totalSalesAmount: number;
    totalConsumptionTax: number;
    remarks: string;
    voucherNumber: number;
    originalVoucherNumber: string;
    salesLines: SalesLineType[];
    checked?: boolean;
}

// 売上検索条件
export interface SalesCriteriaType {
    salesNumber?: string;
    orderNumber?: string;
    salesDate?: string;
    departmentCode?: string;
    remarks?: string;
}

// 売上リソースをAPIリクエスト用に変換
export const mapToSalesResource = (sales: SalesType) => {
    return {
        salesNumber: sales.salesNumber,
        orderNumber: sales.orderNumber,
        salesDate: toISOStringLocal(new Date(sales.salesDate)),
        salesType: sales.salesType ? SalesTypeValues[sales.salesType] : null,
        departmentCode: sales.departmentCode,
        departmentStartDate: toISOStringLocal(new Date(sales.departmentStartDate)),
        customerCode: sales.customerCode,
        employeeCode: sales.employeeCode,
        totalSalesAmount: sales.totalSalesAmount,
        totalConsumptionTax: sales.totalConsumptionTax,
        remarks: sales.remarks,
        voucherNumber: sales.voucherNumber,
        originalVoucherNumber: sales.originalVoucherNumber,
        salesLines: sales.salesLines.map(line => ({
            salesNumber: line.salesNumber,
            salesLineNumber: line.salesLineNumber,
            productCode: line.productCode,
            productName: line.productName,
            salesUnitPrice: line.salesUnitPrice,
            salesQuantity: line.salesQuantity,
            shippedQuantity: line.shippedQuantity,
            discountAmount: line.discountAmount,
            billingDate: line.billingDate ? toISOStringLocal(new Date(line.billingDate)) : null,
            billingNumber: line.billingNumber,
            billingDelayCategory: line.billingDelayCategory,
            autoJournalDate: line.autoJournalDate ? toISOStringLocal(new Date(line.autoJournalDate)) : null,
            taxRate: line.taxRate,
        }))
    };
};

// 検索条件をAPIリクエスト用に変換
export const mapToSalesCriteriaResource = (criteria: SalesCriteriaType) => {
    return {
        salesNumber: criteria.salesNumber ?? null,
        orderNumber: criteria.orderNumber ?? null,
        salesDate: criteria.salesDate ?? null,
        departmentCode: criteria.departmentCode ?? null,
        remarks: criteria.remarks ?? null
    };
};

// 売上一覧のページネーション情報
export interface SalesPageInfoType {
    list: SalesType[];
    pageNum: number;
    pageSize: number;
    size: number;
    startRow: number;
    endRow: number;
    pages: number;
    prePage: number;
    nextPage: number;
    isFirstPage: boolean;
    isLastPage: boolean;
    hasPreviousPage: boolean;
    hasNextPage: boolean;
    navigatePages: number;
    navigatepageNums: number[];
    navigateFirstPage: number;
    navigateLastPage: number;
    total: number;
}

// 初期値
export const initialSales: SalesType = {
    salesNumber: "",
    orderNumber: "",
    salesDate: new Date().toISOString().split('T')[0],
    salesType: SalesTypeEnumType.現金,
    departmentCode: "",
    departmentStartDate: "",
    customerCode: "",
    employeeCode: "",
    totalSalesAmount: 0,
    totalConsumptionTax: 0,
    remarks: "",
    voucherNumber: 0,
    originalVoucherNumber: "",
    salesLines: [],
    checked: false
};

export const initialSalesLine: SalesLineType = {
    salesNumber: "",
    salesLineNumber: 0,
    productCode: "",
    productName: "",
    salesUnitPrice: 0,
    salesQuantity: 0,
    shippedQuantity: 0,
    discountAmount: 0,
    billingDate: "",
    billingNumber: "",
    billingDelayCategory: 0,
    autoJournalDate: "",
    taxRate: TaxRateEnumType.標準税率,
    checked: false
};

export const initialSalesCriteria: SalesCriteriaType = {
    salesNumber: "",
    orderNumber: "",
    salesDate: "",
    departmentCode: "",
    remarks: ""
};

export const initialSalesPageNation: PageNationType = {
    pageNum: 1,
    pageSize: 10,
    size: 0,
    startRow: 0,
    endRow: 0,
    total: 0,
    pages: 0,
    list: [],
    prePage: 0,
    nextPage: 0,
    isFirstPage: true,
    isLastPage: true,
    hasPreviousPage: false,
    hasNextPage: false,
    navigatePages: 8,
    navigatepageNums: [1],
    navigateFirstPage: 1,
    navigateLastPage: 1
};