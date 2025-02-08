import React, { createContext, useContext, ReactNode, useState, useMemo, Dispatch, SetStateAction } from "react";
import { PageNationType, usePageNation } from "../../../views/application/PageNation.tsx";
import { useModal } from "../../../components/application/hooks.ts";
import { showErrorMessage } from "../../../components/application/utils.ts";
import { useMessage } from "../../../components/application/Message.tsx";
import { PartnerServiceType } from "../../../services/master/partner.ts";
import { PartnerCriteriaType, PartnerType } from "../../../models/master/partner";
import { useFetchPartners, usePartner } from "../../../components/master/partner/hooks";

type PartnerListContextType = {
    loading: boolean;
    setLoading: Dispatch<SetStateAction<boolean>>;
    message: string | null;
    setMessage: Dispatch<SetStateAction<string | null>>;
    error: string | null;
    setError: Dispatch<SetStateAction<string | null>>;
    pageNation: PageNationType | null;
    setPageNation: Dispatch<SetStateAction<PageNationType | null>>;
    criteria: PartnerCriteriaType | null;
    setCriteria: Dispatch<SetStateAction<PartnerCriteriaType | null>>;
    searchModalIsOpen: boolean;
    setSearchModalIsOpen: Dispatch<SetStateAction<boolean>>;
    modalIsOpen: boolean;
    setModalIsOpen: Dispatch<SetStateAction<boolean>>;
    isEditing: boolean;
    setIsEditing: Dispatch<SetStateAction<boolean>>;
    editId: string | null;
    setEditId: Dispatch<SetStateAction<string | null>>;
    initialPartner: PartnerType;
    partners: PartnerType[];
    setPartners: Dispatch<SetStateAction<PartnerType[]>>;
    newPartner: PartnerType;
    setNewPartner: Dispatch<SetStateAction<PartnerType>>;
    searchPartnerCriteria: PartnerCriteriaType;
    setSearchPartnerCriteria: Dispatch<SetStateAction<PartnerCriteriaType>>;
    fetchPartners: { load: (page?: number, criteria?: PartnerCriteriaType) => Promise<void> };
    partnerService: PartnerServiceType;
};

const PartnerListContext = createContext<PartnerListContextType | undefined>(undefined);

export const usePartnerListContext = () => {
    const context = useContext(PartnerListContext);
    if (!context) {
        throw new Error("usePartnerContext must be used within a PartnerProvider");
    }
    return context;
};

type Props = {
    children: ReactNode;
};

export const PartnerListProvider: React.FC<Props> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);
    const { message, setMessage, error, setError } = useMessage();
    const { pageNation, setPageNation, criteria, setCriteria } = usePageNation<PartnerCriteriaType>();
    const { modalIsOpen: searchModalIsOpen, setModalIsOpen: setSearchModalIsOpen } = useModal();
    const { modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId } = useModal();
    const {
        initialPartner,
        partners,
        setPartners,
        newPartner,
        setNewPartner,
        searchPartnerCriteria,
        setSearchPartnerCriteria,
        partnerService
    } = usePartner();
    const fetchPartners = useFetchPartners(
        setLoading,
        setPartners,
        setPageNation,
        setError,
        showErrorMessage,
        partnerService
    );
    const defaultCriteria: PartnerCriteriaType = {}; // 必要に応じてデフォルト値を設定
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
        initialPartner,
        partners,
        setPartners,
        newPartner,
        setNewPartner,
        searchPartnerCriteria,
        setSearchPartnerCriteria,
        fetchPartners,
        partnerService
    }), [
        loading,
        message,
        error,
        pageNation,
        criteria,
        searchModalIsOpen,
        modalIsOpen,
        isEditing,
        editId,
        initialPartner,
        partners,
        newPartner,
        searchPartnerCriteria,
        fetchPartners,
        partnerService
    ]);

    return (
        <PartnerListContext.Provider value={value}>
            {children}
        </PartnerListContext.Provider>
    );
};