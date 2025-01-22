import { useState } from "react";
import {PartnerCategoryType, PartnerCategoryCriteriaType, PartnerCategoryItemType} from "../../../models"; // モデルからインポート
import { useFetchEntities } from "../../application/hooks.ts"; // 共通フックのインポート
import { PageNationType } from "../../../views/application/PageNation.tsx"; // ページネーションモデル
import { PartnerCategoryService, PartnerCategoryServiceType } from "../../../services/master/partner_category.ts"; // サービス

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