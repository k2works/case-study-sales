import {PageNationType} from "../../../views/application/PageNation.tsx";
import {CustomerType} from "./customer.ts";
import {VendorType} from "./vendor.ts";
import {PrefectureEnumType} from "../shared.ts";

export type PartnerType = {
    partnerCode: string; // 取引先コード
    partnerName: string; // 取引先名
    partnerNameKana: string; // 取引先名カナ
    vendorType: VendorEnumType; // 仕入先区分
    postalCode: string; // 郵便番号
    prefecture: PrefectureEnumType; // 都道府県
    address1: string; // 住所1
    address2: string; // 住所2
    tradeProhibitedFlag: TradeProhibitedFlagEnumType; // 取引禁止フラグ
    miscellaneousType: MiscellaneousEnumType; // 雑区分
    partnerGroupCode: string; // 取引先グループコード
    creditLimit: number; // 与信限度額
    temporaryCreditIncrease: number; // 与信一時増加枠
    customers: CustomerType[]; // 取引先顧客リスト
    vendors: VendorType[]; // 取引先仕入先リスト
    checked: boolean;
};


export enum VendorEnumType {
    仕入先でない = "仕入先でない",
    仕入先 = "仕入先",
}

export enum TradeProhibitedFlagEnumType {
    OFF = "OFF", ON = "ON"
}

export enum MiscellaneousEnumType {
    対象外 = "対象外", 対象 = "対象"
}

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

export const mapToPartnerResource = (partner: PartnerType): PartnerType => {
    return {
        ...partner,
    }
}

export const mapToPartnerCriteriaResource = (partnerCriteria: PartnerCriteriaType): PartnerCriteriaType => {
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
