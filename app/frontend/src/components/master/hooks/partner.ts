import { useState } from "react";
import {
    PartnerCategoryType,
    PartnerCategoryCriteriaType,
    PartnerCategoryItemType,
    PartnerGroupType, PartnerGroupCriteriaType
} from "../../../models"; // モデルからインポート
import { useFetchEntities } from "../../application/hooks.ts"; // 共通フックのインポート
import { PageNationType } from "../../../views/application/PageNation.tsx"; // ページネーションモデル
import { PartnerCategoryService, PartnerCategoryServiceType } from "../../../services/master/partner_category.ts";
import {PartnerGroupService, PartnerGroupServiceType} from "../../../services/master/partner_group.ts"; // サービス

/** 初期状態で利用するPartnerCategoryの実装 */
export const usePartnerCategory = () => {
    const initialPartnerCategory: PartnerCategoryType = {
        partnerCategoryTypeCode: "",
        partnerCategoryTypeName: "",
        partnerCategoryItems: [],
        checked: false
    };

    const initialPartnerCategoryItem: PartnerCategoryItemType = {
        partnerCategoryTypeCode: "",
        partnerCategoryItemCode: "",
        partnerCategoryItemName: "",
        partnerCategoryAffiliations: [],
    }

    const [partnerCategories, setPartnerCategories] = useState<PartnerCategoryType[]>([]);
    const [newPartnerCategory, setNewPartnerCategory] = useState<PartnerCategoryType>(initialPartnerCategory);
    const [newPartnerCategoryItem, setNewPartnerCategoryItem] = useState<PartnerCategoryItemType>(initialPartnerCategoryItem);
    const [searchPartnerCategoryCriteria, setSearchPartnerCategoryCriteria] = useState<PartnerCategoryCriteriaType>({});
    const partnerCategoryService = PartnerCategoryService();

    return {
        initialPartnerCategory,
        partnerCategories,
        newPartnerCategory,
        newPartnerCategoryItem,
        setNewPartnerCategory,
        setNewPartnerCategoryItem,
        searchPartnerCategoryCriteria,
        setSearchPartnerCategoryCriteria,
        setPartnerCategories,
        partnerCategoryService,
    };
};

/**
 * PartnerCategoryデータをフェッチするための共通フック
 */
export const useFetchPartnerCategories = (
    setLoading: (loading: boolean) => void,
    setList: (list: PartnerCategoryType[]) => void,
    setPageNation: (pageNation: PageNationType) => void,
    setError: (error: string) => void,
    showErrorMessage: (message: string, callback: (error: string) => void) => void,
    service: PartnerCategoryServiceType
) => {
    return useFetchEntities<PartnerCategoryType, PartnerCategoryServiceType, PartnerCategoryCriteriaType>(
        setLoading,
        setList,
        setPageNation,
        setError,
        showErrorMessage,
        service,
        "取引先分類情報の取得に失敗しました:"
    );
};

/** 初期状態で利用するPartnerGroupの実装 */
export const usePartnerGroup = () => {
    const initialPartnerGroup: PartnerGroupType = {
        partnerGroupCode: { value: "" },
        partnerGroupName: "",
        checked: false
    };

    const [partnerGroups, setPartnerGroups] = useState<PartnerGroupType[]>([]);
    const [newPartnerGroup, setNewPartnerGroup] = useState<PartnerGroupType>(initialPartnerGroup);
    const [searchPartnerGroupCriteria, setSearchPartnerGroupCriteria] = useState<PartnerGroupCriteriaType>({});
    const partnerGroupService = PartnerGroupService();

    return {
        initialPartnerGroup,
        partnerGroups,
        newPartnerGroup,
        setNewPartnerGroup,
        searchPartnerGroupCriteria,
        setSearchPartnerGroupCriteria,
        setPartnerGroups,
        partnerGroupService,
    };
};

/**
 * PartnerGroupデータをフェッチするための共通フック
 */
export const useFetchPartnerGroups = (
    setLoading: (loading: boolean) => void,
    setList: (list: PartnerGroupType[]) => void,
    setPageNation: (pageNation: PageNationType) => void,
    setError: (error: string) => void,
    showErrorMessage: (message: string, callback: (error: string) => void) => void,
    service: PartnerGroupServiceType
) => {
    return useFetchEntities<PartnerGroupType, PartnerGroupServiceType, PartnerGroupCriteriaType>(
        setLoading,
        setList,
        setPageNation,
        setError,
        showErrorMessage,
        service,
        "取引先グループ情報の取得に失敗しました:"
    );
};