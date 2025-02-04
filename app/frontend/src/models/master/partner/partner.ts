import {PartnerGroupCodeType} from "./partnerGroup.ts";
import {PageNationType} from "../../../views/application/PageNation.tsx";
import {CustomerType} from "./customer.ts";
import {VendorType} from "./vendor.ts";

export type PartnerType = {
    partnerCode: PartnerCodeType; // 取引先コード
    partnerName: PartnerNameType; // 取引先名
    vendorType: VendorEnumType; // 仕入先区分
    address: AddressType; // 住所
    tradeProhibitedFlag: TradeProhibitedFlagEnumType; // 取引禁止フラグ
    miscellaneousType: MiscellaneousEnumType; // 雑区分
    partnerGroupCode: PartnerGroupCodeType; // 取引先グループコード
    credit: CreditType; // 与信
    customers: CustomerType[]; // 取引先顧客リスト
    vendors: VendorType[]; // 取引先仕入先リスト
    checked: boolean;
};

export type PartnerCodeType = {
    value: string; // 取引先コードの値
};

export type PartnerNameType = {
    name: string;
    nameKana: string;
};

export enum VendorEnumType {
    仕入先でない = "仕入先でない",
    仕入先 = "仕入先",
}

export type AddressType = {
    postalCode: PostalCodeType; // 郵便番号
    prefecture: PrefectureEnumType; // 都道府県
    address1: string; // 住所1
    address2: string; // 住所2
};

export type PostalCodeType = {
    value: string; // 郵便番号の値
    regionCode: string; // 地域コード
};

export enum PrefectureEnumType {
    北海道 = "北海道",
    青森県 = "青森県",
    岩手県 = "岩手県",
    宮城県 = "宮城県",
    秋田県 = "秋田県",
    山形県 = "山形県",
    福島県 = "福島県",
    茨城県 = "茨城県",
    栃木県 = "栃木県",
    群馬県 = "群馬県",
    埼玉県 = "埼玉県",
    千葉県 = "千葉県",
    東京都 = "東京都",
    神奈川県 = "神奈川県",
    新潟県 = "新潟県",
    富山県 = "富山県",
    石川県 = "石川県",
    福井県 = "福井県",
    山梨県 = "山梨県",
    長野県 = "長野県",
    岐阜県 = "岐阜県",
    静岡県 = "静岡県",
    愛知県 = "愛知県",
    三重県 = "三重県",
    滋賀県 = "滋賀県",
    京都府 = "京都府",
    大阪府 = "大阪府",
    兵庫県 = "兵庫県",
    奈良県 = "奈良県",
    和歌山県 = "和歌山県",
    鳥取県 = "鳥取県",
    島根県 = "島根県",
    岡山県 = "岡山県",
    広島県 = "広島県",
    山口県 = "山口県",
    徳島県 = "徳島県",
    香川県 = "香川県",
    愛媛県 = "愛媛県",
    高知県 = "高知県",
    福岡県 = "福岡県",
    佐賀県 = "佐賀県",
    長崎県 = "長崎県",
    熊本県 = "熊本県",
    大分県 = "大分県",
    宮崎県 = "宮崎県",
    鹿児島県 = "鹿児島県",
    沖縄県 = "沖縄県",
}

export enum TradeProhibitedFlagEnumType {
    OFF = "OFF", ON = "ON"
}

export enum MiscellaneousEnumType {
    対象外 = "対象外", 対象 = "対象"
}

export type CreditType = {
    creditLimit: MoneyType; // 与信限度額
    temporaryCreditIncrease: MoneyType; // 与信一時増加枠
};

export type MoneyType = {
    amount: number; // 金額の値
    currency: string; // 通貨単位 (例: "JPY")
};

export type PartnerCriteriaType = {
    partnerCode?: string; // 取引先コード
    partnerName?: string; // 取引先名
    partnerNameKana?: string; // 取引先名カナ
    vendorType?: number; // 仕入先区分 (数値)
    postalCode?: string; // 郵便番号
    prefecture?: string; // 都道府県
    address1?: string; // 住所1
    address2?: string; // 住所2
    tradeProhibitedFlag?: string; // 取引禁止フラグ (数値)
    miscellaneousType?: string; // 雑区分 (数値)
    partnerGroupCode?: string; // 取引先グループコード
    creditLimit?: string; // 与信限度額 (数値)
    temporaryCreditIncrease?: number; // 与信一時増加枠 (数値)
};

export type PartnerFetchType = {
    list: PartnerType[]; // 取引先リスト
} & PageNationType;

export type PartnerResourceType = {
    partnerCode: string; // 取引先コード
    partnerName: string; // 取引先名
    partnerNameKana: string; // 取引先名カナ
    vendorType: VendorEnumType; // 仕入先区分 (Enum型)
    postalCode: string; // 郵便番号
    prefecture: string; // 都道府県
    address1: string; // 住所1
    address2: string; // 住所2
    tradeProhibitedFlag: TradeProhibitedFlagEnumType; // 取引禁止フラグ (Enum型)
    miscellaneousType: MiscellaneousEnumType; // 雑区分 (Enum型)
    partnerGroupCode: string; // 取引先グループコード
    creditLimit: number; // 与信限度額
    temporaryCreditIncrease: number; // 与信一時増加枠
    customers: CustomerType[]; // 取引先顧客リスト
    vendors: VendorType[]; // 取引先仕入先リスト
};

export type PartnerCriteriaResourceType = {
    partnerCode?: string; // 取引先コード
    partnerName?: string; // 取引先名
    partnerNameKana?: string; // 取引先名カナ
    vendorType?: number; // 仕入先区分
    postalCode?: string; // 郵便番号
    prefecture?: string; // 都道府県
    address1?: string; // 住所1
    address2?: string; // 住所2
    tradeProhibitedFlag?: string; // 取引禁止フラグ
    miscellaneousType?: string; // 雑区分
    partnerGroupCode?: string; // 取引先グループコード
    creditLimit?: string; // 与信限度額
    temporaryCreditIncrease?: number; // 与信一時増加枠
};

export const mapToPartnerResource = (partner: PartnerType): PartnerResourceType => {
    return {
        partnerCode: partner.partnerCode.value,
        partnerName: partner.partnerName.name,
        partnerNameKana: partner.partnerName.nameKana,
        vendorType: partner.vendorType,
        postalCode: partner.address.postalCode.value,
        prefecture: partner.address.prefecture,
        address1: partner.address.address1,
        address2: partner.address.address2,
        tradeProhibitedFlag: partner.tradeProhibitedFlag,
        miscellaneousType: partner.miscellaneousType,
        partnerGroupCode: partner.partnerGroupCode.value,
        creditLimit: partner.credit.creditLimit.amount,
        temporaryCreditIncrease: partner.credit.temporaryCreditIncrease.amount,
        customers: partner.customers,
        vendors: partner.vendors
    }
}

export const mapToPartnerCriteriaResource = (partnerCriteria: PartnerCriteriaType): PartnerCriteriaResourceType => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    return {
        ...(isEmpty(partnerCriteria.partnerCode) ? {} : { partnerCode: partnerCriteria.partnerCode }),
        ...(isEmpty(partnerCriteria.partnerName) ? {} : { partnerName: partnerCriteria.partnerName }),
        ...(isEmpty(partnerCriteria.partnerNameKana) ? {} : { partnerNameKana: partnerCriteria.partnerNameKana }),
        ...(isEmpty(partnerCriteria.vendorType) ? {} : { vendorType: partnerCriteria.vendorType }),
        ...(isEmpty(partnerCriteria.postalCode) ? {} : { postalCode: partnerCriteria.postalCode }),
        ...(isEmpty(partnerCriteria.prefecture) ? {} : { prefecture: partnerCriteria.prefecture }),
        ...(isEmpty(partnerCriteria.address1) ? {} : { address1: partnerCriteria.address1 }),
        ...(isEmpty(partnerCriteria.address2) ? {} : { address2: partnerCriteria.address2 }),
        ...(isEmpty(partnerCriteria.tradeProhibitedFlag) ? {} : { tradeProhibitedFlag: partnerCriteria.tradeProhibitedFlag }),
        ...(isEmpty(partnerCriteria.miscellaneousType) ? {} : { miscellaneousType: partnerCriteria.miscellaneousType }),
        ...(isEmpty(partnerCriteria.partnerGroupCode) ? {} : { partnerGroupCode: partnerCriteria.partnerGroupCode }),
        ...(isEmpty(partnerCriteria.creditLimit) ? {} : { creditLimit: partnerCriteria.creditLimit }),
        ...(isEmpty(partnerCriteria.temporaryCreditIncrease) ? {} : { temporaryCreditIncrease: partnerCriteria.temporaryCreditIncrease }),
    }
}