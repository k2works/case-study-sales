import {useState} from "react";
import {ProductCategoryCriteriaType, ProductCategoryType, ProductCriteriaType, ProductType} from "../../../models";
import {ProductCategoryService, ProductCategoryServiceType} from "../../../services/master/product_category.ts";
import {PageNationType} from "../../../views/application/PageNation.tsx";
import {useFetchEntities} from "../../application/hooks.ts";
import {ProductService, ProductServiceType} from "../../../services/master/product.ts";

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

export const useProduct = () => {
    const initialProduct: ProductType = {
        productCode: {
            value: "",
            businessType: "",
            itemType: "",
            livestockType: "",
            serialNumber: 0,
        },
        productName: {
            productFormalName: "",
            productAbbreviation: "",
            productNameKana: "",
        },
        productType: "",
        sellingPrice: {amount: 0, currency: ""},
        purchasePrice: {amount: 0, currency: ""},
        costOfSales: {amount: 0, currency: ""},
        taxType: "",
        productCategoryCode: {value: ""},
        miscellaneousType: "",
        stockManagementTargetType: "",
        stockAllocationType: "",
        supplierCode: {
            value: "",
            branchNumber: 0,
        },
        substituteProduct: [],
        boms: [],
        customerSpecificSellingPrices: [],
        checked: false,
        addFlag: false,
        deleteFlag: false,
    };

    const [products, setProducts] = useState<ProductType[]>([]);
    const [newProduct, setNewProduct] = useState<ProductType>(initialProduct);
    const [searchProductCriteria, setSearchProductCriteria] = useState<ProductCriteriaType>({});
    const productService = ProductService();

    return {
        initialProduct,
        products,
        newProduct,
        setNewProduct,
        setProducts,
        searchProductCriteria,
        setSearchProductCriteria,
        productService
    };
};

export const useFetchProducts = (
    setLoading: (loading: boolean) => void,
    setList: (list: ProductType[]) => void,
    setPageNation: (pageNation: PageNationType) => void,
    setError: (error: string) => void,
    showErrorMessage: (message: string, callback: (error: string) => void) => void,
    service: ProductServiceType
) => useFetchEntities<ProductType, ProductServiceType, ProductCriteriaType>(setLoading, setList, setPageNation, setError, showErrorMessage, service, "商品情報の取得に失敗しました:");

export const useFetchBoms = (
    setLoading: (loading: boolean) => void,
    setList: (list: ProductType[]) => void,
    setPageNation: (pageNation: PageNationType) => void,
    setError: (error: string) => void,
    showErrorMessage: (message: string, callback: (error: string) => void) => void,
    service: ProductServiceType
) => useFetchEntities(setLoading, setList, setPageNation, setError, showErrorMessage, {select: service.selectBoms}, "部品情報の取得に失敗しました:");

export const useFetchSubstitutes = (
    setLoading: (loading: boolean) => void,
    setList: (list: ProductType[]) => void,
    setPageNation: (pageNation: PageNationType) => void,
    setError: (error: string) => void,
    showErrorMessage: (message: string, callback: (error: string) => void) => void,
    service: ProductServiceType
) => useFetchEntities<ProductType, ProductServiceType, ProductCriteriaType>(setLoading, setList, setPageNation, setError, showErrorMessage, service, "代替商品情報の取得に失敗しました:");