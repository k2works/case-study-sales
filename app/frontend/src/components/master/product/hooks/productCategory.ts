import {useState} from "react";
import {ProductCategoryCriteriaType, ProductCategoryType} from "../../../../models/master/product";
import {ProductCategoryService, ProductCategoryServiceType} from "../../../../services/master/productCategory.ts";
import {PageNationType} from "../../../../views/application/PageNation.tsx";
import {useFetchEntities} from "../../../application/hooks.ts";

export const useProductCategory = () => {
    const initialProductCategory: ProductCategoryType = {
        productCategoryCode: {value: ""},
        productCategoryName: "",
        productCategoryHierarchy: 0,
        productCategoryPath: "",
        lowestLevelDivision: 0,
        products: [],
        checked: false
    };

    const [productCategories, setProductCategories] = useState<ProductCategoryType[]>([]);
    const [newProductCategory, setNewProductCategory] = useState<ProductCategoryType>(initialProductCategory);
    const [searchProductCategoryCriteria, setSearchProductCategoryCriteria] = useState<ProductCategoryCriteriaType>({});
    const productCategoryService = ProductCategoryService();

    return {
        initialProductCategory,
        productCategories,
        newProductCategory,
        setNewProductCategory,
        setProductCategories,
        searchProductCategoryCriteria,
        setSearchProductCategoryCriteria,
        productCategoryService
    };
};

export const useFetchProductCategories = (
    setLoading: (loading: boolean) => void,
    setList: (list: ProductCategoryType[]) => void,
    setPageNation: (pageNation: PageNationType) => void,
    setError: (error: string) => void,
    showErrorMessage: (message: string, callback: (error: string) => void) => void,
    service: ProductCategoryServiceType
) => useFetchEntities<ProductCategoryType, ProductCategoryServiceType, ProductCategoryCriteriaType>(setLoading, setList, setPageNation, setError, showErrorMessage, service, "商品分類情報の取得に失敗しました:");
