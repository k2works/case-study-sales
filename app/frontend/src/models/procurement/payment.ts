import { PageNationType } from "../../views/application/PageNation.tsx";
import { toISOStringLocal } from "../../components/application/utils.ts";

export type PaymentType = {
    paymentNumber: string;
    paymentDate: number | string;  // バックエンドはInteger、フォームではstring
    departmentCode: string;
    departmentStartDate: string;  // バックエンドはLocalDateTime、フォームではstring
    supplierCode: string;
    supplierBranchNumber: number;
    paymentMethodType: number;
    paymentAmount: number;
    totalConsumptionTax: number;
    paymentCompletedFlag: boolean;
    supplierName?: string;
    checked?: boolean;
};

export type PaymentCriteriaType = {
    paymentNumber?: string;
    paymentDate?: string;
    departmentCode?: string;
    supplierCode?: string;
    paymentMethodType?: number;
    paymentCompletedFlag?: boolean;
};

export type PaymentSearchCriteriaType = {
    paymentNumber?: string;
    paymentDate?: string;
    departmentCode?: string;
    supplierCode?: string;
    paymentMethodType?: string;
    paymentCompletedFlag?: string;
};

export type PaymentFetchType = {
    list: PaymentType[];
} & PageNationType;

export type PaymentPageNationType = PageNationType<PaymentCriteriaType>;

export const initialPayment: PaymentType = {
    paymentNumber: "",
    paymentDate: new Date().toISOString().split('T')[0],
    departmentCode: "",
    departmentStartDate: new Date().toISOString().split('T')[0],
    supplierCode: "",
    supplierBranchNumber: 1,
    paymentMethodType: 1,
    paymentAmount: 0,
    totalConsumptionTax: 0,
    paymentCompletedFlag: false,
    supplierName: "",
    checked: false
};

export const initialPaymentCriteria: PaymentCriteriaType = {
    paymentNumber: "",
    paymentDate: "",
    departmentCode: "",
    supplierCode: "",
    paymentMethodType: undefined,
    paymentCompletedFlag: undefined
};

// YYYY-MM-DD形式の日付をYYYYMMDD形式の整数に変換
const convertDateToInteger = (dateStr: number | string): number => {
    if (typeof dateStr === 'number') return dateStr;
    const cleanDate = dateStr.replace(/-/g, '');
    return parseInt(cleanDate, 10);
};

export const mapToPaymentResource = (payment: PaymentType) => {
    return {
        paymentNumber: payment.paymentNumber,
        paymentDate: payment.paymentDate ? convertDateToInteger(payment.paymentDate) : null,
        departmentCode: payment.departmentCode,
        departmentStartDate: payment.departmentStartDate ? toISOStringLocal(new Date(payment.departmentStartDate)) : null,
        supplierCode: payment.supplierCode,
        supplierBranchNumber: payment.supplierBranchNumber,
        paymentMethodType: payment.paymentMethodType,
        paymentAmount: payment.paymentAmount,
        totalConsumptionTax: payment.totalConsumptionTax,
        paymentCompletedFlag: payment.paymentCompletedFlag
    };
};

export const mapToPaymentSearchResource = (criteria: PaymentSearchCriteriaType) => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    return {
        ...(isEmpty(criteria.paymentNumber) ? {} : { paymentNumber: criteria.paymentNumber }),
        ...(isEmpty(criteria.paymentDate) ? {} : { paymentDate: convertDateToInteger(criteria.paymentDate) }),
        ...(isEmpty(criteria.departmentCode) ? {} : { departmentCode: criteria.departmentCode }),
        ...(isEmpty(criteria.supplierCode) ? {} : { supplierCode: criteria.supplierCode }),
        ...(isEmpty(criteria.paymentMethodType) ? {} : { paymentMethodType: Number(criteria.paymentMethodType) }),
        ...(isEmpty(criteria.paymentCompletedFlag) ? {} : { paymentCompletedFlag: criteria.paymentCompletedFlag === "true" })
    };
};

export const mapToPaymentCriteriaResource = (criteria: PaymentCriteriaType) => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    return {
        ...(isEmpty(criteria.paymentNumber) ? {} : { paymentNumber: criteria.paymentNumber }),
        ...(isEmpty(criteria.paymentDate) ? {} : { paymentDate: convertDateToInteger(criteria.paymentDate) }),
        ...(isEmpty(criteria.departmentCode) ? {} : { departmentCode: criteria.departmentCode }),
        ...(isEmpty(criteria.supplierCode) ? {} : { supplierCode: criteria.supplierCode }),
        ...(isEmpty(criteria.paymentMethodType) ? {} : { paymentMethodType: criteria.paymentMethodType }),
        ...(isEmpty(criteria.paymentCompletedFlag) ? {} : { paymentCompletedFlag: criteria.paymentCompletedFlag })
    };
};
