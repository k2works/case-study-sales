import {
    ClosingDateEnumType,
    ClosingInvoiceType,
    EmailType,
    PaymentDayEnumType, PaymentMethodEnumType,
    PaymentMonthEnumType,
    PhoneNumberType
} from "./customer.ts";
import {AddressType, PartnerCodeType, PartnerNameType} from "./partner.ts";
import {PageNationType} from "../../../views/application/PageNation.tsx"; // 必要な型をインポート

// 仕入先コード型
export type VendorCodeType = {
    code: PartnerCodeType; // 仕入先コード (取引先コード)
    branchNumber: number; // 支店番号
};

// 仕入先名型
export type VendorNameType = {
    value: PartnerNameType; // 仕入先名 (取引先名)
};

// 仕入先型
export type VendorType = {
    vendorCode: VendorCodeType; // 仕入先コード
    vendorName: VendorNameType; // 仕入先名
    vendorContactName: string; // 仕入先担当者名
    vendorDepartmentName: string; // 仕入先部門名
    vendorAddress: AddressType; // 仕入先住所
    vendorPhoneNumber: PhoneNumberType; // 仕入先電話番号
    vendorFaxNumber: PhoneNumberType; // 仕入先FAX番号
    vendorEmailAddress: EmailType; // 仕入先メールアドレス
    vendorClosingInvoice: ClosingInvoiceType; // 仕入先締請求
    checked: boolean;
};

export type VendorFetchType = {
    list: VendorType[]; // 仕入先リスト
} & PageNationType;

// 仕入先検索条件型
export type VendorCriteriaType = {
    vendorCode?: string; // 仕入先コード
    vendorName?: string; // 仕入先名
    vendorContactName?: string; // 仕入先担当者名
    vendorDepartmentName?: string; // 仕入先部門名
    postalCode?: string; // 郵便番号
    prefecture?: string; // 都道府県
    address1?: string; // 住所1
    address2?: string; // 住所2
    vendorPhoneNumber?: string; // 仕入先電話番号
    vendorFaxNumber?: string; // 仕入先FAX番号
    vendorEmailAddress?: string; // 仕入先メールアドレス
};

// 仕入先リソース型
export type VendorResourceType = {
    vendorCode: string; // 仕入先コード
    vendorBranchNumber: number; // 仕入先枝番号
    vendorName: string; // 仕入先名
    vendorNameKana: string; // 仕入先名カナ
    vendorContactName: string; // 仕入先担当者名
    vendorDepartmentName: string; // 仕入先部門名
    vendorPostalCode: string; // 郵便番号
    vendorPrefecture: string; // 都道府県
    vendorAddress1: string; // 住所1
    vendorAddress2: string; // 住所2
    vendorPhoneNumber: string; // 仕入先電話番号
    vendorFaxNumber: string; // 仕入先FAX番号
    vendorEmailAddress: string; // 仕入先メールアドレス
    vendorClosingDate: ClosingDateEnumType; // 締日
    vendorPaymentMonth: PaymentMonthEnumType; // 支払月
    vendorPaymentDate: PaymentDayEnumType; // 支払日
    vendorPaymentMethod: PaymentMethodEnumType; // 支払方法
};

// 仕入先検索条件リソース型
export type VendorCriteriaResourceType = {
    vendorCode?: string; // 仕入先コード
    vendorName?: string; // 仕入先名
    vendorContactName?: string; // 仕入先担当者名
    vendorDepartmentName?: string; // 仕入先部門名
    postalCode?: string; // 郵便番号
    prefecture?: string; // 都道府県
    address1?: string; // 住所1
    address2?: string; // 住所2
    vendorPhoneNumber?: string; // 仕入先電話番号
    vendorFaxNumber?: string; // 仕入先FAX番号
    vendorEmailAddress?: string; // 仕入先メールアドレス
};

export const mapToVendorResourceType = (vendor: VendorType): VendorResourceType => {
    return {
        vendorCode: vendor.vendorCode.code.value,
        vendorBranchNumber: vendor.vendorCode.branchNumber,
        vendorName: vendor.vendorName.value.name,
        vendorNameKana: vendor.vendorName.value.nameKana,
        vendorContactName: vendor.vendorContactName,
        vendorDepartmentName: vendor.vendorDepartmentName,
        vendorPostalCode: vendor.vendorAddress.postalCode.value,
        vendorPrefecture: vendor.vendorAddress.prefecture,
        vendorAddress1: vendor.vendorAddress.address1,
        vendorAddress2: vendor.vendorAddress.address2,
        vendorPhoneNumber: vendor.vendorPhoneNumber.value,
        vendorFaxNumber: vendor.vendorFaxNumber.value,
        vendorEmailAddress: vendor.vendorEmailAddress.value,
        vendorClosingDate: vendor.vendorClosingInvoice.closingDay,
        vendorPaymentMonth: vendor.vendorClosingInvoice.paymentMonth,
        vendorPaymentDate: vendor.vendorClosingInvoice.paymentDay,
        vendorPaymentMethod: vendor.vendorClosingInvoice.paymentMethod,
    };
}

export const mapToCriteriaResourceType = (criteria: VendorCriteriaType): VendorCriteriaResourceType => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    return {
        ...(isEmpty(criteria.vendorCode) ? {} : { vendorCode: criteria.vendorCode }),
        ...(isEmpty(criteria.vendorName) ? {} : { vendorName: criteria.vendorName }),
        ...(isEmpty(criteria.vendorContactName) ? {} : { vendorContactName: criteria.vendorContactName }),
        ...(isEmpty(criteria.vendorDepartmentName) ? {} : { vendorDepartmentName: criteria.vendorDepartmentName }),
        ...(isEmpty(criteria.postalCode) ? {} : { postalCode: criteria.postalCode }),
        ...(isEmpty(criteria.prefecture) ? {} : { prefecture: criteria.prefecture }),
        ...(isEmpty(criteria.address1) ? {} : { address1: criteria.address1 }),
        ...(isEmpty(criteria.address2) ? {} : { address2: criteria.address2 }),
        ...(isEmpty(criteria.vendorPhoneNumber) ? {} : { vendorPhoneNumber: criteria.vendorPhoneNumber }),
        ...(isEmpty(criteria.vendorFaxNumber) ? {} : { vendorFaxNumber: criteria.vendorFaxNumber }),
        ...(isEmpty(criteria.vendorEmailAddress) ? {} : { vendorEmailAddress: criteria.vendorEmailAddress }),
    };
}