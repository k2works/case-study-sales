import {useState} from "react";
import {
    ProductType
} from "../../../../models/master/product";
import {PageNationType} from "../../../../views/application/PageNation.tsx";
import {ProductService, ProductServiceType} from "../../../../services/master/product.ts";
import {useFetchEntities} from "../../../application/hooks.ts";

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
    const [searchProductCode, setSearchProductCode] = useState<string>("");
    const productService = ProductService();

    return {
        initialProduct,
        products,
        newProduct,
        setNewProduct,
        setProducts,
        searchProductCode,
        setSearchProductCode,
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
) => useFetchEntities(setLoading, setList, setPageNation, setError, showErrorMessage, service, "商品情報の取得に失敗しました:");

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
) => useFetchEntities(setLoading, setList, setPageNation, setError, showErrorMessage, service, "代替商品情報の取得に失敗しました:");

