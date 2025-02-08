import React, { createContext, useContext, ReactNode, useState, useMemo, Dispatch, SetStateAction } from "react";
import { PageNationType, usePageNation } from "../views/application/PageNation.tsx";
import { useModal } from "../components/application/hooks.ts";
import { showErrorMessage } from "../components/application/utils.ts";
import { useMessage } from "../components/application/Message.tsx";
import { ProductCategoryServiceType } from "../services/master/productCategory.ts";
import {ProductCategoryCriteriaType, ProductCategoryType} from "../models/master/product";
import {
    useFetchProductCategories,
    useProductCategory
} from "../components/master/product/hooks";

type ProductCategoryContextType = {
    loading: boolean;
    setLoading: Dispatch<SetStateAction<boolean>>;
    message: string | null;
    setMessage: Dispatch<SetStateAction<string | null>>;
    error: string | null;
    setError: Dispatch<SetStateAction<string | null>>;
    pageNation: PageNationType | null;
    setPageNation: Dispatch<SetStateAction<PageNationType | null>>;
    criteria: ProductCategoryCriteriaType | null;
    setCriteria: Dispatch<SetStateAction<ProductCategoryCriteriaType | null>>;
    searchModalIsOpen: boolean;
    setSearchModalIsOpen: Dispatch<SetStateAction<boolean>>;
    modalIsOpen: boolean;
    setModalIsOpen: Dispatch<SetStateAction<boolean>>;
    isEditing: boolean;
    setIsEditing: Dispatch<SetStateAction<boolean>>;
    editId: string | null;
    setEditId: Dispatch<SetStateAction<string | null>>;
    initialProductCategory: ProductCategoryType;
    productCategories: ProductCategoryType[];
    setProductCategories: Dispatch<SetStateAction<ProductCategoryType[]>>;
    newProductCategory: ProductCategoryType;
    setNewProductCategory: Dispatch<SetStateAction<ProductCategoryType>>;
    searchProductCategoryCriteria: ProductCategoryCriteriaType;
    setSearchProductCategoryCriteria: Dispatch<SetStateAction<ProductCategoryCriteriaType>>;
    fetchProductCategories: { load: (page?: number, criteria?: ProductCategoryCriteriaType) => Promise<void> };
    productCategoryService: ProductCategoryServiceType;
};

const ProductCategoryContext = createContext<ProductCategoryContextType | undefined>(undefined);

export const useProductCategoryContext = () => {
    const context = useContext(ProductCategoryContext);
    if (!context) {
        throw new Error("useProductCategoryContext must be used within a ProductCategoryProvider");
    }
    return context;
};

type Props = {
    children: ReactNode;
};

export const ProductCategoryProvider: React.FC<Props> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);
    const { message, setMessage, error, setError } = useMessage();
    const { pageNation, setPageNation, criteria, setCriteria } = usePageNation<ProductCategoryCriteriaType | null>();
    const { modalIsOpen: searchModalIsOpen, setModalIsOpen: setSearchModalIsOpen } = useModal();
    const { modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId } = useModal();
    const {
        initialProductCategory,
        productCategories,
        setProductCategories,
        newProductCategory,
        setNewProductCategory,
        searchProductCategoryCriteria,
        setSearchProductCategoryCriteria,
        productCategoryService
    } = useProductCategory();
    const fetchProductCategories = useFetchProductCategories(
        setLoading,
        setProductCategories,
        setPageNation,
        setError,
        showErrorMessage,
        productCategoryService
    );

    const defaultCriteria: ProductCategoryCriteriaType = {};
    const value = useMemo(() => ({
        loading,
        setLoading,
        message,
        setMessage,
        error,
        setError,
        pageNation,
        setPageNation,
        criteria: criteria ?? defaultCriteria,
        setCriteria,
        searchModalIsOpen,
        setSearchModalIsOpen,
        modalIsOpen,
        setModalIsOpen,
        isEditing,
        setIsEditing,
        editId,
        setEditId,
        initialProductCategory,
        productCategories,
        setProductCategories,
        newProductCategory,
        setNewProductCategory,
        searchProductCategoryCriteria,
        setSearchProductCategoryCriteria,
        fetchProductCategories,
        productCategoryService,
    }), [
        loading,
        message,
        setMessage,
        error,
        setError,
        pageNation,
        setPageNation,
        criteria,
        defaultCriteria,
        setCriteria,
        searchModalIsOpen,
        setSearchModalIsOpen,
        modalIsOpen,
        setModalIsOpen,
        isEditing,
        setIsEditing,
        editId,
        setEditId,
        initialProductCategory,
        productCategories,
        setProductCategories,
        newProductCategory,
        setNewProductCategory,
        searchProductCategoryCriteria,
        setSearchProductCategoryCriteria,
        fetchProductCategories,
        productCategoryService,
    ]);

    return (
        <ProductCategoryContext.Provider value={value}>
            {children}
        </ProductCategoryContext.Provider>
    );
};