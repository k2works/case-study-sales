import { PageNationType } from "../../views/application/PageNation.tsx";
import { toISOStringLocal } from "../../components/application/utils.ts";

export enum PaymentMethodType {
    現金 = "現金",
    小切手 = "小切手",
    手形 = "手形",
    振込 = "振込",
    相殺 = "相殺",
    その他 = "その他"
}

export type PaymentType = {
    paymentNumber: string;
    paymentDate: number | string;  // バックエンドはInteger、フォームではstring
    departmentCode: string;
    departmentStartDate: string;  // バックエンドはLocalDateTime、フォームではstring
    supplierCode: string;
    supplierBranchNumber: number;
    paymentMethodType: string;  // バックエンドはInteger、フォームではstring
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
    paymentMethodType?: string;
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
    paymentMethodType: "振込",
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

// 支払方法区分をコードから文字列に変換
export const convertCodeToPaymentMethodType = (code: number | string): string => {
    const codeMap: { [key: number]: string } = {
        1: "現金",
        2: "小切手",
        3: "手形",
        4: "振込",
        5: "相殺",
        9: "その他"
    };
    const numCode = typeof code === 'string' ? parseInt(code, 10) : code;
    return codeMap[numCode] || "振込"; // デフォルトは振込
};

// 支払方法区分を文字列からコードに変換
const convertPaymentMethodTypeToCode = (paymentMethodType: string): number => {
    const paymentMethodMap: { [key: string]: number } = {
        "現金": 1,
        "小切手": 2,
        "手形": 3,
        "振込": 4,
        "相殺": 5,
        "その他": 9
    };
    return paymentMethodMap[paymentMethodType] || 4; // デフォルトは振込
};

export const mapToPaymentResource = (payment: PaymentType) => {
    return {
        paymentNumber: payment.paymentNumber,
        paymentDate: payment.paymentDate ? convertDateToInteger(payment.paymentDate) : null,
        departmentCode: payment.departmentCode,
        departmentStartDate: payment.departmentStartDate ? toISOStringLocal(new Date(payment.departmentStartDate)) : null,
        supplierCode: payment.supplierCode,
        supplierBranchNumber: payment.supplierBranchNumber,
        paymentMethodType: convertPaymentMethodTypeToCode(payment.paymentMethodType),
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
        ...(isEmpty(criteria.paymentMethodType) ? {} : { paymentMethodType: convertPaymentMethodTypeToCode(criteria.paymentMethodType) }),
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
        ...(isEmpty(criteria.paymentMethodType) ? {} : { paymentMethodType: convertPaymentMethodTypeToCode(criteria.paymentMethodType) }),
        ...(isEmpty(criteria.paymentCompletedFlag) ? {} : { paymentCompletedFlag: criteria.paymentCompletedFlag })
    };
};
