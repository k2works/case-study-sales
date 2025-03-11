import {PageNationType} from "../../../views/application/PageNation.tsx";

export type PartnerGroupType = {
    partnerGroupCode: string; // 取引先グループコード
    partnerGroupName: string; // 取引先グループ名
    checked: boolean; // チェック状態
};

export type PartnerGroupCriteriaType = {
    partnerGroupCode?: string; // 取引先グループコード（オプション）
    partnerGroupName?: string; // 取引先グループ名（オプション）
};

export type PartnerGroupFetchType = {
    list: PartnerGroupType[]; // 取引先グループリスト
} & PageNationType;

export const mapToPartnerGroupResource = (type: PartnerGroupType): PartnerGroupType => {
    return {
        ...type,
    }
}

export const mapToPartnerGroupCriteriaResource = (criteria: PartnerGroupCriteriaType): PartnerGroupCriteriaType => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    return {
        ...(isEmpty(criteria.partnerGroupCode) ? {} : {partnerGroupCode: criteria.partnerGroupCode}),
        ...(isEmpty(criteria.partnerGroupName) ? {} : {partnerGroupName: criteria.partnerGroupName}),
    }
}
