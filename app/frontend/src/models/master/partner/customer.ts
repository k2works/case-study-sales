import {PageNationType} from "../../../views/application/PageNation.tsx";
import {
    ClosingDateEnumType,
    PaymentDayEnumType, PaymentMethodEnumType,
    PaymentMonthEnumType,
} from "../shared.ts";

// 顧客区分列挙型
export enum CustomerEnumType {
    顧客でない = "顧客でない",
    顧客 = "顧客",
}

export enum CustomerBillingCategoryEnumType {
    都度請求 = "都度請求",
    締請求 = "締請求",
}

export type ShippingType = {
    customerCode: string; // 顧客コード
    customerBranchNumber: number; // 顧客枝番
    destinationNumber: number; // 出荷先番号
    destinationName: string; // 出荷先名
    regionCode: string; // 地域コード
    postalCode: string; // 郵便番号
    prefecture: string; // 都道府県
    address1: string; // 住所1
    address2: string; // 住所2
};

export type CustomerType = {
    customerCode: string; // 顧客コード
    customerBranchNumber: number; // 顧客枝番号
    customerType: CustomerEnumType; // 顧客区分
    billingCode: string; // 請求先コード
    billingBranchNumber: number; // 請求枝番号
    collectionCode: string; // 回収先コード
    collectionBranchNumber: number; // 回収枝番号
    customerName: string; // 顧客名
    customerNameKana: string; // 顧客名カナ
    companyRepresentativeCode: string; // 自社担当者コード
    customerRepresentativeName: string; // 顧客担当者名
    customerDepartmentName: string; // 顧客部門名
    customerPostalCode: string; // 顧客郵便番号
    customerPrefecture: string; // 顧客都道府県
    customerAddress1: string; // 顧客住所1
    customerAddress2: string; // 顧客住所2
    customerPhoneNumber: string; // 顧客電話番号
    customerFaxNumber: string; // 顧客FAX番号
    customerEmailAddress: string; // 顧客メールアドレス
    customerBillingType: CustomerBillingCategoryEnumType; // 顧客請求区分
    customerClosingDay1: ClosingDateEnumType; // 顧客締日1
    customerPaymentMonth1: PaymentMonthEnumType; // 顧客支払月1
    customerPaymentDay1: PaymentDayEnumType; // 顧客支払日1
    customerPaymentMethod1: PaymentMethodEnumType; // 顧客支払方法1
    customerClosingDay2: ClosingDateEnumType; // 顧客締日2
    customerPaymentMonth2: PaymentMonthEnumType; // 顧客支払月2
    customerPaymentDay2: PaymentDayEnumType; // 顧客支払日2
    customerPaymentMethod2: PaymentMethodEnumType; // 顧客支払方法2
    shippings: ShippingType[]; // 配送先リスト
    checked: boolean;
};

export type CustomerCriteriaType = {
    customerCode?: string; // 顧客コード
    customerName?: string; // 顧客名
    customerNameKana?: string; // 顧客名カナ
    customerType?: string; // 顧客区分
    billingCode?: string; // 請求先コード
    collectionCode?: string; // 回収先コード
    companyRepresentativeCode?: string; // 自社担当者コード
    customerRepresentativeName?: string; // 顧客担当者名
    customerDepartmentName?: string; // 顧客部門名
    postalCode?: string; // 郵便番号
    prefecture?: string; // 都道府県
    address1?: string; // 住所1
    address2?: string; // 住所2
    customerPhoneNumber?: string; // 顧客電話番号
    customerFaxNumber?: string; // 顧客ＦＡＸ番号
    customerEmailAddress?: string; // 顧客メールアドレス
    customerBillingCategory?: string; // 顧客請求区分
};

export type CustomerFetchType = {
    list: CustomerType[];
} & PageNationType;

export const mapToCustomerCriteriaType = (criteria: CustomerCriteriaType): CustomerCriteriaType => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    return {
        ...(isEmpty(criteria.customerCode) ? {} : { customerCode: criteria.customerCode }),
        ...(isEmpty(criteria.customerName) ? {} : { customerName: criteria.customerName }),
        ...(isEmpty(criteria.customerNameKana) ? {} : { customerNameKana: criteria.customerNameKana }),
        ...(isEmpty(criteria.customerType) ? {} : { customerType: criteria.customerType }),
        ...(isEmpty(criteria.billingCode) ? {} : { billingCode: criteria.billingCode }),
        ...(isEmpty(criteria.collectionCode) ? {} : { collectionCode: criteria.collectionCode }),
        ...(isEmpty(criteria.companyRepresentativeCode) ? {} : { companyRepresentativeCode: criteria.companyRepresentativeCode }),
        ...(isEmpty(criteria.customerRepresentativeName) ? {} : { customerRepresentativeName: criteria.customerRepresentativeName }),
        ...(isEmpty(criteria.customerDepartmentName) ? {} : { customerDepartmentName: criteria.customerDepartmentName }),
        ...(isEmpty(criteria.postalCode) ? {} : { postalCode: criteria.postalCode }),
        ...(isEmpty(criteria.prefecture) ? {} : { prefecture: criteria.prefecture }),
        ...(isEmpty(criteria.address1) ? {} : { address1: criteria.address1 }),
        ...(isEmpty(criteria.address2) ? {} : { address2: criteria.address2 }),
        ...(isEmpty(criteria.customerPhoneNumber) ? {} : { customerPhoneNumber: criteria.customerPhoneNumber }),
        ...(isEmpty(criteria.customerFaxNumber) ? {} : { customerFaxNumber: criteria.customerFaxNumber }),
        ...(isEmpty(criteria.customerEmailAddress) ? {} : { customerEmailAddress: criteria.customerEmailAddress }),
        ...(isEmpty(criteria.customerBillingCategory) ? {} : { customerBillingCategory: criteria.customerBillingCategory }),
    }
}
