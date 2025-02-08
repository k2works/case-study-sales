import React, { createContext, useContext, ReactNode, useState, useMemo, Dispatch, SetStateAction } from "react";
import { PageNationType, usePageNation } from "../../../views/application/PageNation.tsx";
import { useModal } from "../../../components/application/hooks.ts";
import { showErrorMessage } from "../../../components/application/utils.ts";
import { useMessage } from "../../../components/application/Message.tsx";
import { ProductServiceType } from "../../../services/master/product.ts";
import {ProductType} from "../../../models/master/product";
import {useFetchBoms, useProduct} from "../../../components/master/product/hooks";

type ProductBomContextType = {
    loading: boolean;
    setLoading: Dispatch<SetStateAction<boolean>>;
    message: string | null;
    setMessage: Dispatch<SetStateAction<string | null>>;
    error: string | null;
    setError: Dispatch<SetStateAction<string | null>>;
    bomPageNation: PageNationType | null;
    setBomPageNation: Dispatch<SetStateAction<PageNationType | null>>;
    bomModalIsOpen: boolean;
    setBomModalIsOpen: Dispatch<SetStateAction<boolean>>;
    boms: ProductType[];
    setBoms: Dispatch<SetStateAction<ProductType[]>>;
    fetchBoms: { load: (page?: number) => Promise<void> };
    bomService: ProductServiceType;
    setBomIsEditing: Dispatch<SetStateAction<boolean>>;
    setBomEditId: Dispatch<SetStateAction<string | null>>;
};

const ProductBomContext = createContext<ProductBomContextType | undefined>(undefined);

export const useProductBomContext = () => {
    const context = useContext(ProductBomContext);
    if (!context) {
        throw new Error("useProductBomContext must be used within a ProductBomProvider");
    }
    return context;
};

type Props = {
    children: ReactNode;
};

export const ProductBomProvider: React.FC<Props> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);
    const { message, setMessage, error, setError } = useMessage();
    const { pageNation: bomPageNation, setPageNation: setBomPageNation } = usePageNation<ProductType | null>();
    const { modalIsOpen: bomModalIsOpen, setModalIsOpen: setBomModalIsOpen, setIsEditing: setBomIsEditing, setEditId: setBomEditId } = useModal();
    const { products: boms, setProducts: setBoms, productService: bomService } = useProduct();
    const fetchBoms = useFetchBoms(
        setLoading,
        setBoms,
        setBomPageNation,
        setError,
        showErrorMessage,
        bomService
    );

    const value = useMemo(() => ({
        loading,
        setLoading,
        message,
        setMessage,
        error,
        setError,
        bomPageNation,
        setBomPageNation,
        bomModalIsOpen,
        setBomModalIsOpen,
        boms,
        setBoms,
        fetchBoms,
        bomService,
        setBomIsEditing,
        setBomEditId
    }), [
        loading,
        setLoading,
        message,
        setMessage,
        error,
        setError,
        bomPageNation,
        setBomPageNation,
        bomModalIsOpen,
        setBomModalIsOpen,
        boms,
        setBoms,
        fetchBoms,
        bomService,
        setBomIsEditing,
        setBomEditId
    ]);

    return (
        <ProductBomContext.Provider value={value}>
            {children}
        </ProductBomContext.Provider>
    );
};