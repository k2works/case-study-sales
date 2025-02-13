import React, { createContext, useContext, ReactNode, useState, useMemo, Dispatch, SetStateAction } from "react";
import { PageNationType, usePageNation } from "../../../views/application/PageNation.tsx";
import { useModal } from "../../../components/application/hooks.ts";
import { showErrorMessage } from "../../../components/application/utils.ts";
import { useMessage } from "../../../components/application/Message.tsx";
import { ProductServiceType } from "../../../services/master/product.ts";
import {ProductType} from "../../../models/master/product";
import {useFetchSubstitutes, useProduct} from "../../../components/master/product/hooks";

type ProductSubstituteContextType = {
    loading: boolean;
    setLoading: Dispatch<SetStateAction<boolean>>;
    message: string | null;
    setMessage: Dispatch<SetStateAction<string | null>>;
    error: string | null;
    setError: Dispatch<SetStateAction<string | null>>;
    substitutePageNation: PageNationType | null;
    setSubstitutePageNation: Dispatch<SetStateAction<PageNationType | null>>;
    productModalIsOpen: boolean;
    setProductModalIsOpen: Dispatch<SetStateAction<boolean>>;
    substitutes: ProductType[];
    setSubstitutes: Dispatch<SetStateAction<ProductType[]>>;
    fetchSubstitutes: { load: (page?: number) => Promise<void> };
    substituteService: ProductServiceType;
    setProductIsEditing: Dispatch<SetStateAction<boolean>>;
    setProductEditId: Dispatch<SetStateAction<string | null>>;
};

const ProductSubstituteContext = createContext<ProductSubstituteContextType | undefined>(undefined);

export const useProductSubstituteContext = () => {
    const context = useContext(ProductSubstituteContext);
    if (!context) {
        throw new Error("useProductSubstituteContext must be used within a ProductSubstituteProvider");
    }
    return context;
};

type Props = {
    children: ReactNode;
};

export const ProductSubstituteProvider: React.FC<Props> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);
    const { message, setMessage, error, setError } = useMessage();
    const { pageNation: substitutePageNation, setPageNation: setSubstitutePageNation } = usePageNation<ProductType | null>();
    const { modalIsOpen: productModalIsOpen, setModalIsOpen: setProductModalIsOpen, setIsEditing: setProductIsEditing, setEditId: setProductEditId } = useModal();
    const { products: substitutes, setProducts: setSubstitutes, productService: substituteService } = useProduct();
    const fetchSubstitutes = useFetchSubstitutes(
        setLoading,
        setSubstitutes,
        setSubstitutePageNation,
        setError,
        showErrorMessage,
        substituteService
    );

    const value = useMemo(() => ({
        loading,
        setLoading,
        message,
        setMessage,
        error,
        setError,
        substitutePageNation,
        setSubstitutePageNation,
        productModalIsOpen,
        setProductModalIsOpen,
        substitutes,
        setSubstitutes,
        fetchSubstitutes,
        substituteService,
        setProductIsEditing,
        setProductEditId
    }), [
        loading,
        setLoading,
        message,
        setMessage,
        error,
        setError,
        substitutePageNation,
        setSubstitutePageNation,
        productModalIsOpen,
        setProductModalIsOpen,
        substitutes,
        setSubstitutes,
        fetchSubstitutes,
        substituteService,
        setProductIsEditing,
        setProductEditId
    ]);

    return (
        <ProductSubstituteContext.Provider value={value}>
            {children}
        </ProductSubstituteContext.Provider>
    );
};