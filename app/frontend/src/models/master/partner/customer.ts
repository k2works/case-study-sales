import {AddressType, PartnerCodeType, PartnerNameType} from "./partner.ts";
import {PageNationType} from "../../../views/application/PageNation.tsx";

export type CustomerType = {
    customerCode: CustomerCodeType; // 顧客コード
    customerType: CustomerEnumType; // 顧客区分
    billingCode: BillingCodeType; // 請求先コード
    collectionCode: CollectionCodeType; // 回収先コード
    customerName: CustomerNameType; // 顧客名
    companyRepresentativeCode: string; // 自社担当者コード
    customerRepresentativeName: string; // 顧客担当者名
    customerDepartmentName: string; // 顧客部門名
    customerAddress: AddressType; // 顧客住所
    customerPhoneNumber: PhoneNumberType; // 顧客電話番号
    customerFaxNumber: PhoneNumberType; // 顧客FAX番号
    customerEmailAddress: EmailType; // 顧客メールアドレス
    invoice: InvoiceType; // 請求
    shippings: ShippingType[]; // 出荷先リスト
    checked: boolean;
};

// 顧客コード型
export type CustomerCodeType = {
    code: PartnerCodeType; // 顧客コード (取引先コード)
    branchNumber: number; // 枝番
};

// 顧客区分列挙型
export enum CustomerEnumType {
    顧客でない = "顧客でない",
    顧客 = "顧客",
}

// 請求先コード型
export type BillingCodeType = {
    code: PartnerCodeType; // 請求先コード (取引先コード)
    branchNumber: number; // 枝番
};

// 回収先コード型
export type CollectionCodeType = {
    code: PartnerCodeType; // 回収先コード (取引先コード)
    branchNumber: number; // 支店番号
};

// 顧客名型
export type CustomerNameType = {
    value: PartnerNameType; // 顧客名 (取引先名)
};

// 電話番号型
export type PhoneNumberType = {
    value: string; // 電話番号全体の値
    areaCode: string; // 市外局番
    localExchange: string; // 市内局番
    subscriberNumber: string; // 加入者番号
};

// メールアドレス型
export type EmailType = {
    value: string; // メールアドレス
};

// 請求型
export type InvoiceType = {
    customerBillingCategory: CustomerBillingCategoryEnumType; // 顧客請求区分
    closingInvoice1: ClosingInvoiceType; // 締請求1
    closingInvoice2: ClosingInvoiceType; // 締請求2
};

export enum CustomerBillingCategoryEnumType {
    都度請求 = "都度請求",
    締請求 = "締請求",
}

// 締請求型 (既存の定義を流用)
export type ClosingInvoiceType = {
    closingDay: ClosingDateEnumType; // 締日
    paymentMonth: PaymentMonthEnumType; // 支払月
    paymentDay: PaymentDayEnumType; // 支払日
    paymentMethod: PaymentMethodEnumType; // 支払方法
};

// 締日型 (既存の定義を流用、または参考に作成)
export enum ClosingDateEnumType {
    十日 = "十日",
    二十日 = "二十日",
    末日 = "末日",
}

// 支払月型
export enum PaymentMonthEnumType {
    当月 = "当月",
    翌月 = "翌月",
    翌々月 = "翌々月",
}

// 支払日型
export enum PaymentDayEnumType {
    十日 = "十日",
    二十日 = "二十日",
    末日 = "末日",
}

// 支払方法型
export enum PaymentMethodEnumType {
    振込 = "振込",
    手形 = "手形",
}

export type ShippingType = {
    shippingCode: ShippingCodeType; // 出荷先コード
    destinationName: string; // 出荷先名
    regionCode: RegionCodeType; // 地域コード
    shippingAddress: AddressType; // 出荷先住所
};

// 出荷先コード型
export type ShippingCodeType = {
    customerCode: CustomerCodeType;
    destinationNumber: number; // 出荷先番号
};

// 地域コード型
export type RegionCodeType = {
    value: string; // 地域コードの値
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

export type CustomerResourceType = {
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
};

export type CustomerCriteriaResourceType = {
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
    customerFaxNumber?: string; // 顧客FAX番号
    customerEmailAddress?: string; // 顧客メールアドレス
    customerBillingCategory?: string; // 顧客請求区分
};

export type CustomerFetchType = {
    list: CustomerType[];
} & PageNationType;

export const mapToCustomerType = (customer: CustomerType): CustomerResourceType => {
    return {
        customerCode: customer.customerCode.code.value,
        customerBranchNumber: customer.customerCode.branchNumber,
        customerType: customer.customerType,
        billingCode: customer.billingCode.code.value,
        billingBranchNumber: customer.billingCode.branchNumber,
        collectionCode: customer.collectionCode.code.value,
        collectionBranchNumber: customer.collectionCode.branchNumber,
        customerName: customer.customerName.value.name,
        customerNameKana: customer.customerName.value.nameKana,
        companyRepresentativeCode: customer.companyRepresentativeCode,
        customerRepresentativeName: customer.customerRepresentativeName,
        customerDepartmentName: customer.customerDepartmentName,
        customerPostalCode: customer.customerAddress.postalCode.value,
        customerPrefecture: customer.customerAddress.prefecture,
        customerAddress1: customer.customerAddress.address1,
        customerAddress2: customer.customerAddress.address2,
        customerPhoneNumber: customer.customerPhoneNumber.value,
        customerFaxNumber: customer.customerFaxNumber.value,
        customerEmailAddress: customer.customerEmailAddress.value,
        customerBillingType: customer.invoice.customerBillingCategory,
        customerClosingDay1: customer.invoice.closingInvoice1.closingDay,
        customerPaymentMonth1: customer.invoice.closingInvoice1.paymentMonth,
        customerPaymentDay1: customer.invoice.closingInvoice1.paymentDay,
        customerPaymentMethod1: customer.invoice.closingInvoice1.paymentMethod,
        customerClosingDay2: customer.invoice.closingInvoice2.closingDay,
        customerPaymentMonth2: customer.invoice.closingInvoice2.paymentMonth,
        customerPaymentDay2: customer.invoice.closingInvoice2.paymentDay,
        customerPaymentMethod2: customer.invoice.closingInvoice2.paymentMethod,
        shippings: customer.shippings,
    }
}

export const mapToCustomerCriteriaType = (criteria: CustomerCriteriaType): CustomerCriteriaResourceType => {
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