import {useState} from "react";
import {
    ProductCategoryType,
} from "../../../../models/master/product";
import {PageNationType} from "../../../../views/application/PageNation.tsx";
import {ProductCategoryService, ProductCategoryServiceType} from "../../../../services/master/productCategory.ts";
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
    const [searchProductCategoryCode, setSearchProductCategoryCode] = useState<string>("");
    const productCategoryService = ProductCategoryService();

    return {
        initialProductCategory,
        productCategories,
        newProductCategory,
        setNewProductCategory,
        setProductCategories,
        searchProductCategoryCode,
        setSearchProductCategoryCode,
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
) => useFetchEntities(setLoading, setList, setPageNation, setError, showErrorMessage, service, "商品分類情報の取得に失敗しました:");

