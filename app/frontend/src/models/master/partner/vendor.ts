import {PageNationType} from "../../../views/application/PageNation.tsx";
import {
    ClosingDateEnumType,
    PaymentDayEnumType, PaymentMethodEnumType,
    PaymentMonthEnumType,
} from "../shared.ts"; // 必要な型をインポート

export type VendorType = {
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

export const mapToVendorResourceType = (vendor: VendorType): VendorType => {
    return {
        ...vendor
    };
}

export const mapToCriteriaResourceType = (criteria: VendorCriteriaType): VendorCriteriaType => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    // 空の値を除外したオブジェクトを返す
    const result: Record<string, string> = {};

    if (!isEmpty(criteria.vendorCode)) result.vendorCode = criteria.vendorCode!;
    if (!isEmpty(criteria.vendorName)) result.vendorName = criteria.vendorName!;
    if (!isEmpty(criteria.vendorContactName)) result.vendorContactName = criteria.vendorContactName!;
    if (!isEmpty(criteria.vendorDepartmentName)) result.vendorDepartmentName = criteria.vendorDepartmentName!;
    if (!isEmpty(criteria.postalCode)) result.postalCode = criteria.postalCode!;
    if (!isEmpty(criteria.prefecture)) result.prefecture = criteria.prefecture!;
    if (!isEmpty(criteria.address1)) result.address1 = criteria.address1!;
    if (!isEmpty(criteria.address2)) result.address2 = criteria.address2!;
    if (!isEmpty(criteria.vendorPhoneNumber)) result.vendorPhoneNumber = criteria.vendorPhoneNumber!;
    if (!isEmpty(criteria.vendorFaxNumber)) result.vendorFaxNumber = criteria.vendorFaxNumber!;
    if (!isEmpty(criteria.vendorEmailAddress)) result.vendorEmailAddress = criteria.vendorEmailAddress!;

    return result as VendorCriteriaType;
}
