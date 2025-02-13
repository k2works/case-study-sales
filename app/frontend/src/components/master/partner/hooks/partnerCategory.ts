import {useState} from "react";
import {
    PartnerCategoryCriteriaType,
    PartnerCategoryItemType,
    PartnerCategoryType,
} from "../../../../models/master/partner";
import {useFetchEntities} from "../../../application/hooks.ts";
import {PageNationType} from "../../../../views/application/PageNation.tsx";
import {PartnerCategoryService, PartnerCategoryServiceType} from "../../../../services/master/partnerCategory.ts";

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
