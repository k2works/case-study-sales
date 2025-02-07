import React, { createContext, useContext, ReactNode, useState, useMemo, Dispatch, SetStateAction } from "react";
import { PageNationType, usePageNation } from "../views/application/PageNation.tsx";
import { useModal } from "../components/application/hooks.ts";
import { showErrorMessage } from "../components/application/utils.ts";
import { useMessage } from "../components/application/Message.tsx";
import { VendorServiceType } from "../services/master/vendor.ts";
import {VendorCriteriaType, VendorType} from "../models/master/partner";
import {useFetchVendors, useVendor} from "../components/master/partner/hooks";

type VendorContextType = {
    loading: boolean;
    setLoading: Dispatch<SetStateAction<boolean>>;
    message: string | null;
    setMessage: Dispatch<SetStateAction<string | null>>;
    error: string | null;
    setError: Dispatch<SetStateAction<string | null>>;
    pageNation: PageNationType | null;
    setPageNation: Dispatch<SetStateAction<PageNationType | null>>;
    criteria: VendorCriteriaType | null;
    setCriteria: Dispatch<SetStateAction<VendorCriteriaType | null>>;
    searchModalIsOpen: boolean;
    setSearchModalIsOpen: Dispatch<SetStateAction<boolean>>;
    modalIsOpen: boolean;
    setModalIsOpen: Dispatch<SetStateAction<boolean>>;
    isEditing: boolean;
    setIsEditing: Dispatch<SetStateAction<boolean>>;
    editId: string | null;
    setEditId: Dispatch<SetStateAction<string | null>>;
    initialVendor: VendorType;
    vendors: VendorType[];
    setVendors: Dispatch<SetStateAction<VendorType[]>>;
    newVendor: VendorType;
    setNewVendor: Dispatch<SetStateAction<VendorType>>;
    searchVendorCriteria: VendorCriteriaType;
    setSearchVendorCriteria: Dispatch<SetStateAction<VendorCriteriaType>>;
    fetchVendors: { load: (page?: number, criteria?: VendorCriteriaType) => Promise<void> };
    vendorService: VendorServiceType;
};

const VendorContext = createContext<VendorContextType | undefined>(undefined);

export const useVendorContext = () => {
    const context = useContext(VendorContext);
    if (!context) {
        throw new Error("useVendorContext must be used within a VendorProvider");
    }
    return context;
};

type Props = {
    children: ReactNode;
};

export const VendorProvider: React.FC<Props> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);
    const { message, setMessage, error, setError } = useMessage();
    const { pageNation, setPageNation, criteria, setCriteria } = usePageNation<VendorCriteriaType>();
    const { modalIsOpen: searchModalIsOpen, setModalIsOpen: setSearchModalIsOpen } = useModal();
    const { modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId } = useModal();
    const {
        initialVendor,
        vendors,
        setVendors,
        newVendor,
        setNewVendor,
        searchVendorCriteria,
        setSearchVendorCriteria,
        vendorService
    } = useVendor();
    const fetchVendors = useFetchVendors(
        setLoading,
        setVendors,
        setPageNation,
        setError,
        showErrorMessage,
        vendorService
    );

    const defaultCriteria: VendorCriteriaType = {}; // 必要であれば具体的なデフォルト値を設定してください。

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
        initialVendor,
        vendors,
        setVendors,
        newVendor,
        setNewVendor,
        searchVendorCriteria,
        setSearchVendorCriteria,
        fetchVendors,
        vendorService
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
        initialVendor,
        vendors,
        setVendors,
        newVendor,
        setNewVendor,
        searchVendorCriteria,
        setSearchVendorCriteria,
        fetchVendors,
        vendorService
    ]);

    return (
        <VendorContext.Provider value={value}>
            {children}
        </VendorContext.Provider>
    );
};