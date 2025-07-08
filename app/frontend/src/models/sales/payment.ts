import { PageNationType } from "../../views/application/PageNation";
import {toISOStringLocal} from "../../components/application/utils.ts";

// 入金情報
export interface PaymentType {
    paymentNumber: string;
    paymentDate: string;
    departmentCode: string;
    departmentStartDate: string;
    customerCode: string;
    customerBranchNumber: number;
    paymentMethodType: string;
    paymentAccountCode: string;
    paymentAmount: number;
    offsetAmount: number;
    customerName?: string;
    paymentAccountName?: string;
    checked?: boolean;
}

// 入金検索条件
export interface PaymentCriteriaType {
    paymentNumber?: string;
    paymentDate?: string;
    departmentCode?: string;
    departmentStartDate?: string;
    customerCode?: string;
    customerBranchNumber?: number;
    paymentMethodType?: string;
    paymentAccountCode?: string;
}

// 入金リソースをAPIリクエスト用に変換
export const mapToPaymentResource = (payment: PaymentType) => {
    return {
        paymentNumber: payment.paymentNumber,
        paymentDate: toISOStringLocal(new Date(payment.paymentDate)),
        departmentCode: payment.departmentCode,
        departmentStartDate: toISOStringLocal(new Date(payment.departmentStartDate)),
        customerCode: payment.customerCode,
        customerBranchNumber: payment.customerBranchNumber,
        paymentMethodType: payment.paymentMethodType,
        paymentAccountCode: payment.paymentAccountCode,
        paymentAmount: payment.paymentAmount,
        offsetAmount: payment.offsetAmount
    };
};

// 検索条件をAPIリクエスト用に変換
export const mapToPaymentCriteriaResource = (criteria: PaymentCriteriaType) => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    return {
        ...(isEmpty(criteria.paymentNumber) ? {} : { paymentNumber: criteria.paymentNumber }),
        ...(isEmpty(criteria.paymentDate) ? {} : { paymentDate: toISOStringLocal(new Date(criteria.paymentDate)) }),
        ...(isEmpty(criteria.departmentCode) ? {} : { departmentCode: criteria.departmentCode }),
        ...(isEmpty(criteria.departmentStartDate) ? {} : { departmentStartDate: toISOStringLocal(new Date(criteria.departmentStartDate)) }),
        ...(isEmpty(criteria.customerCode) ? {} : { customerCode: criteria.customerCode }),
        ...(isEmpty(criteria.customerBranchNumber) ? {} : { customerBranchNumber: criteria.customerBranchNumber }),
        ...(isEmpty(criteria.paymentMethodType) ? {} : { paymentMethodType: criteria.paymentMethodType }),
        ...(isEmpty(criteria.paymentAccountCode) ? {} : { paymentAccountCode: criteria.paymentAccountCode })
    };
};

// 入金一覧のページネーション情報
export interface PaymentPageInfoType {
    list: PaymentType[];
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
export const initialPayment: PaymentType = {
    paymentNumber: "",
    paymentDate: new Date().toISOString().split('T')[0],
    departmentCode: "",
    departmentStartDate: "",
    customerCode: "",
    customerBranchNumber: 0,
    paymentMethodType: "",
    paymentAccountCode: "",
    paymentAmount: 0,
    offsetAmount: 0,
    checked: false
};

export const initialPaymentCriteria: PaymentCriteriaType = {
    paymentNumber: "",
    paymentDate: "",
    departmentCode: "",
    customerCode: "",
    paymentMethodType: "",
    paymentAccountCode: ""
};

export const initialPaymentPageNation: PageNationType = {
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