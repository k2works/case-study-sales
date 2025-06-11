import {PageNationType} from "../../views/application/PageNation";
import {toISOStringLocal} from "../../components/application/utils.ts";

// 請求明細情報
export interface InvoiceLineType {
    invoiceNumber: string;
    salesNumber: string;
    salesLineNumber: number;
    checked?: boolean;
}

// 請求情報
export interface InvoiceType {
    invoiceNumber: string;
    invoiceDate: string;
    partnerCode: string;
    customerCode: string;
    customerBranchNumber: number;
    previousPaymentAmount: number;
    currentMonthSalesAmount: number;
    currentMonthPaymentAmount: number;
    currentMonthInvoiceAmount: number;
    consumptionTaxAmount: number;
    invoiceReconciliationAmount: number;
    invoiceLines: InvoiceLineType[];
    checked?: boolean;
}

// 請求検索条件
export interface InvoiceCriteriaType {
    invoiceNumber?: string;
    invoiceDate?: string;
    customerCode?: string;
}

// 請求リソースをAPIリクエスト用に変換
export const mapToInvoiceResource = (invoice: InvoiceType) => {
    return {
        invoiceNumber: invoice.invoiceNumber,
        invoiceDate: invoice.invoiceDate ? toISOStringLocal(new Date(invoice.invoiceDate)) : null,
        partnerCode: invoice.partnerCode,
        customerCode: invoice.customerCode,
        customerBranchNumber: invoice.customerBranchNumber,
        previousPaymentAmount: invoice.previousPaymentAmount,
        currentMonthSalesAmount: invoice.currentMonthSalesAmount,
        currentMonthPaymentAmount: invoice.currentMonthPaymentAmount,
        currentMonthInvoiceAmount: invoice.currentMonthInvoiceAmount,
        consumptionTaxAmount: invoice.consumptionTaxAmount,
        invoiceReconciliationAmount: invoice.invoiceReconciliationAmount,
        invoiceLines: invoice.invoiceLines.map(line => ({
            invoiceNumber: line.invoiceNumber,
            salesNumber: line.salesNumber,
            salesLineNumber: line.salesLineNumber,
        }))
    };
};

// 検索条件をAPIリクエスト用に変換
export const mapToInvoiceCriteriaResource = (criteria: InvoiceCriteriaType) => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    return {
        ...(isEmpty(criteria.invoiceNumber) ? {} : { invoiceNumber: criteria.invoiceNumber }),
        ...(isEmpty(criteria.invoiceDate) ? {} : { invoiceDate: criteria.invoiceDate }),
        ...(isEmpty(criteria.customerCode) ? {} : { customerCode: criteria.customerCode }),
    };
};

// 請求一覧のページネーション情報
export interface InvoicePageInfoType {
    list: InvoiceType[];
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
export const initialInvoice: InvoiceType = {
    invoiceNumber: "",
    invoiceDate: new Date().toISOString().split('T')[0],
    partnerCode: "",
    customerCode: "",
    customerBranchNumber: 0,
    previousPaymentAmount: 0,
    currentMonthSalesAmount: 0,
    currentMonthPaymentAmount: 0,
    currentMonthInvoiceAmount: 0,
    consumptionTaxAmount: 0,
    invoiceReconciliationAmount: 0,
    invoiceLines: [],
    checked: false
};

export const initialInvoiceLine: InvoiceLineType = {
    invoiceNumber: "",
    salesNumber: "",
    salesLineNumber: 0,
    checked: false
};

export const initialInvoiceCriteria: InvoiceCriteriaType = {
    invoiceNumber: "",
    invoiceDate: "",
    customerCode: ""
};

export const initialInvoicePageNation: PageNationType = {
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