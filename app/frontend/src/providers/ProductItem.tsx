import React, { createContext, useContext, ReactNode, useState, useMemo, Dispatch, SetStateAction } from "react";
import { PageNationType, usePageNation } from "../views/application/PageNation.tsx";
import { useModal } from "../components/application/hooks.ts";
import { showErrorMessage } from "../components/application/utils.ts";
import { useMessage } from "../components/application/Message.tsx";
import { ProductServiceType } from "../services/master/product.ts";
import {ProductCriteriaType, ProductType} from "../models/master/product";
import {useFetchProducts, useProduct} from "../components/master/product/hooks";

type ProductItemContextType = {
    loading: boolean;
    setLoading: Dispatch<SetStateAction<boolean>>;
    message: string | null;
    setMessage: Dispatch<SetStateAction<string | null>>;
    error: string | null;
    setError: Dispatch<SetStateAction<string | null>>;
    pageNation: PageNationType | null;
    setPageNation: Dispatch<SetStateAction<PageNationType | null>>;
    criteria: ProductCriteriaType | null;
    setCriteria: Dispatch<SetStateAction<ProductCriteriaType | null>>;
    searchModalIsOpen: boolean;
    setSearchModalIsOpen: Dispatch<SetStateAction<boolean>>;
    modalIsOpen: boolean;
    setModalIsOpen: Dispatch<SetStateAction<boolean>>;
    isEditing: boolean;
    setIsEditing: Dispatch<SetStateAction<boolean>>;
    editId: string | null;
    setEditId: Dispatch<SetStateAction<string | null>>;
    initialProduct: ProductType;
    products: ProductType[];
    setProducts: Dispatch<SetStateAction<ProductType[]>>;
    newProduct: ProductType;
    setNewProduct: Dispatch<SetStateAction<ProductType>>;
    searchProductCriteria: ProductCriteriaType;
    setSearchProductCriteria: Dispatch<SetStateAction<ProductCriteriaType>>;
    fetchProducts: { load: (page?: number, criteria?: ProductCriteriaType) => Promise<void> };
    productService: ProductServiceType;
};

const ProductItemContext = createContext<ProductItemContextType | undefined>(undefined);

export const useProductItemContext = () => {
    const context = useContext(ProductItemContext);
    if (!context) {
        throw new Error("useProductItemContext must be used within a ProductItemProvider");
    }
    return context;
};

type Props = {
    children: ReactNode;
};

export const ProductItemProvider: React.FC<Props> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);
    const { message, setMessage, error, setError } = useMessage();
    const { pageNation, setPageNation, criteria, setCriteria } = usePageNation<ProductCriteriaType | null>();
    const { modalIsOpen: searchModalIsOpen, setModalIsOpen: setSearchModalIsOpen } = useModal();
    const { modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId } = useModal();
    const {
        initialProduct,
        products,
        setProducts,
        newProduct,
        setNewProduct,
        searchProductCriteria,
        setSearchProductCriteria,
        productService
    } = useProduct();
    const fetchProducts = useFetchProducts(
        setLoading,
        setProducts,
        setPageNation,
        setError,
        showErrorMessage,
        productService
    );

    const defaultCriteria: ProductCriteriaType = {};
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
        initialProduct,
        products,
        setProducts,
        newProduct,
        setNewProduct,
        searchProductCriteria,
        setSearchProductCriteria,
        fetchProducts,
        productService
    }), [
        loading,
        setLoading,
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
        initialProduct,
        products,
        setProducts,
        newProduct,
        setNewProduct,
        searchProductCriteria,
        setSearchProductCriteria,
        fetchProducts,
        productService
    ]);

    return (
        <ProductItemContext.Provider value={value}>
            {children}
        </ProductItemContext.Provider>
    );
};