import {useState} from "react";
import {ProductCriteriaType, ProductType} from "../../../../models/master/product";
import {PageNationType} from "../../../../views/application/PageNation.tsx";
import {useFetchEntities} from "../../../application/hooks.ts";
import {ProductService, ProductServiceType} from "../../../../services/master/product.ts";

export const useProduct = () => {
    const initialProduct: ProductType = {
        productCode: "",
        productFormalName: "",
        productAbbreviation: "",
        productNameKana: "",
        productType: "",
        sellingPrice: 0,
        purchasePrice: 0,
        costOfSales: 0,
        taxType: "",
        productClassificationCode: "",
        miscellaneousType: "",
        stockManagementTargetType: "",
        stockAllocationType: "",
        vendorCode: "",
        vendorBranchNumber: 0,
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
