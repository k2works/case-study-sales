import { useState } from "react";
import {
    PartnerCategoryType,
    PartnerCategoryCriteriaType,
    PartnerCategoryItemType,
    PartnerGroupType,
    PartnerGroupCriteriaType,
    PartnerType,
    VendorEnumType,
    PrefectureEnumType,
    TradeProhibitedFlagEnumType, MiscellaneousEnumType, PartnerCriteriaType
} from "../../../models/master/partner"; // モデルからインポート
import { useFetchEntities } from "../../application/hooks.ts"; // 共通フックのインポート
import { PageNationType } from "../../../views/application/PageNation.tsx"; // ページネーションモデル
import { PartnerCategoryService, PartnerCategoryServiceType } from "../../../services/master/partner_category.ts";
import {PartnerGroupService, PartnerGroupServiceType} from "../../../services/master/partner_group.ts";
import {PartnerService, PartnerServiceType} from "../../../services/master/partner.ts"; // サービス

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

/** 初期状態で利用するPartnerの実装 */
export const usePartner = () => {
    const initialPartner: PartnerType = {
        partnerCode: { value: "" },
        partnerName: { name: "", nameKana: "" },
        vendorType: VendorEnumType.仕入先でない,
        address: {
            postalCode: { value: "", regionCode: "" },
            prefecture: PrefectureEnumType.東京都,
            address1: "",
            address2: ""
        },
        tradeProhibitedFlag: TradeProhibitedFlagEnumType.OFF,
        miscellaneousType: MiscellaneousEnumType.対象外,
        partnerGroupCode: { value: "" },
        credit: {
            creditLimit: { amount: 0, currency: "JPY" },
            temporaryCreditIncrease: { amount: 0, currency: "JPY" }
        }
    };

    const [partners, setPartners] = useState<PartnerType[]>([]);
    const [newPartner, setNewPartner] = useState<PartnerType>(initialPartner);
    const [searchPartnerCriteria, setSearchPartnerCriteria] = useState<PartnerCriteriaType>({});
    const partnerService = PartnerService();

    return {
        initialPartner,
        partners,
        newPartner,
        setNewPartner,
        searchPartnerCriteria,
        setSearchPartnerCriteria,
        setPartners,
        partnerService,
    };
};

/**
 * Partnerデータをフェッチするための共通フック
 */
export const useFetchPartners = (
    setLoading: (loading: boolean) => void,
    setList: (list: PartnerType[]) => void,
    setPageNation: (pageNation: PageNationType) => void,
    setError: (error: string) => void,
    showErrorMessage: (message: string, callback: (error: string) => void) => void,
    service: PartnerServiceType
) => {
    return useFetchEntities<PartnerType, PartnerServiceType, PartnerCriteriaType>(
        setLoading,
        setList,
        setPageNation,
        setError,
        showErrorMessage,
        service,
        "取引先情報の取得に失敗しました:"
    );
};