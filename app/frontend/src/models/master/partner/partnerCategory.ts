import {PageNationType} from "../../../views/application/PageNation.tsx";

export type PartnerCategoryType = {
    partnerCategoryTypeCode: string; // 取引先分類種別コード
    partnerCategoryTypeName: string; // 取引先分類種別名
    partnerCategoryItems: PartnerCategoryItemType[]; // 取引先分類
    checked: boolean; // チェック状態
};

export type PartnerCategoryItemType = {
    partnerCategoryTypeCode: string; // 取引先分類種別コード
    partnerCategoryItemCode: string; // 取引先分類コード
    partnerCategoryItemName: string; // 取引先分類名
    partnerCategoryAffiliations: PartnerCategoryAffiliationType[]; // 取引先分類所属
};

export type PartnerCategoryAffiliationType = {
    partnerCategoryTypeCode: string; // 取引先分類種別コード
    partnerCode: string;    // 取引先コード
    partnerCategoryItemCode: string; // 取引先分類コード
};

export type PartnerCategoryCriteriaType = {
    partnerCategoryTypeCode?: string; // 取引先分類種別コード
    partnerCategoryTypeName?: string; // 取引先分類種別名
    partnerCategoryItemCode?: string; // 取引先分類コード
    partnerCategoryItemName?: string; // 取引先分類名
    partnerCode?: string;            // 取引先コード
}

export type PartnerCategoryFetchType = {
    list: PartnerCategoryType[]; // 取引先分類リスト
} & PageNationType;

export const mapToPartnerCategoryTypeResource = (partnerCategoryType: PartnerCategoryType): PartnerCategoryType => {
    return {
        ...partnerCategoryType,
        partnerCategoryItems: partnerCategoryType.partnerCategoryItems.map(partnerCategoryItem => {
            return {
                partnerCategoryTypeCode: partnerCategoryItem.partnerCategoryTypeCode,
                partnerCategoryItemCode: partnerCategoryItem.partnerCategoryItemCode,
                partnerCategoryItemName: partnerCategoryItem.partnerCategoryItemName,
                partnerCategoryAffiliations: partnerCategoryItem.partnerCategoryAffiliations.map(partnerCategoryAffiliation => {
                    return {
                        partnerCategoryTypeCode: partnerCategoryAffiliation.partnerCategoryTypeCode,
                        partnerCode: partnerCategoryAffiliation.partnerCode,
                        partnerCategoryItemCode: partnerCategoryAffiliation.partnerCategoryItemCode,
                    }
                })
            }
        }),
    }
}

export const mapToPartnerCategoryCriteriaResource = (partnerCategoryCriteria: PartnerCategoryCriteriaType): PartnerCategoryCriteriaType => {
    const isEmpty = (value: unknown) => value === "" || value === null || value === undefined;
    return {
        ...(isEmpty(partnerCategoryCriteria.partnerCategoryTypeCode) ? {} : { partnerCategoryTypeCode: partnerCategoryCriteria.partnerCategoryTypeCode }),
        ...(isEmpty(partnerCategoryCriteria.partnerCategoryTypeName) ? {} : { partnerCategoryTypeName: partnerCategoryCriteria.partnerCategoryTypeName }),
        ...(isEmpty(partnerCategoryCriteria.partnerCategoryItemCode) ? {} : { partnerCategoryItemCode: partnerCategoryCriteria.partnerCategoryItemCode }),
        ...(isEmpty(partnerCategoryCriteria.partnerCategoryItemName) ? {} : { partnerCategoryItemName: partnerCategoryCriteria.partnerCategoryItemName }),
        ...(isEmpty(partnerCategoryCriteria.partnerCode) ? {} : { partnerCode: partnerCategoryCriteria.partnerCode }),
    }
}
