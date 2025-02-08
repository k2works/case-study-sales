import React, {
    createContext,
    useContext,
    ReactNode,
    useState,
    useMemo,
    Dispatch,
    SetStateAction,
} from "react";
import { PageNationType, usePageNation } from "../views/application/PageNation.tsx";
import { useModal } from "../components/application/hooks.ts";
import { showErrorMessage } from "../components/application/utils.ts";
import { useMessage } from "../components/application/Message.tsx";
import {PartnerGroupCriteriaType, PartnerGroupType} from "../models/master/partner";
import {PartnerGroupServiceType} from "../services/master/partnerGroup.ts";
import {useFetchPartnerGroups, usePartnerGroup} from "../components/master/partner/hooks";

type PartnerGroupContextType = {
    loading: boolean;
    setLoading: Dispatch<SetStateAction<boolean>>;
    message: string | null;
    setMessage: Dispatch<SetStateAction<string | null>>;
    error: string | null;
    setError: Dispatch<SetStateAction<string | null>>;
    pageNation: PageNationType | null;
    setPageNation: Dispatch<SetStateAction<PageNationType | null>>;
    criteria: PartnerGroupCriteriaType | null;
    setCriteria: Dispatch<SetStateAction<PartnerGroupCriteriaType | null>>;
    searchModalIsOpen: boolean;
    setSearchModalIsOpen: Dispatch<SetStateAction<boolean>>;
    modalIsOpen: boolean;
    setModalIsOpen: Dispatch<SetStateAction<boolean>>;
    isEditing: boolean;
    setIsEditing: Dispatch<SetStateAction<boolean>>;
    editId: string | null;
    setEditId: Dispatch<SetStateAction<string | null>>;
    initialPartnerGroup: PartnerGroupType;
    partnerGroups: PartnerGroupType[];
    setPartnerGroups: Dispatch<SetStateAction<PartnerGroupType[]>>;
    newPartnerGroup: PartnerGroupType;
    setNewPartnerGroup: Dispatch<SetStateAction<PartnerGroupType>>;
    searchPartnerGroupCriteria: PartnerGroupCriteriaType;
    setSearchPartnerGroupCriteria: Dispatch<SetStateAction<PartnerGroupCriteriaType>>;
    fetchPartnerGroups: { load: (page?: number, criteria?: PartnerGroupCriteriaType) => Promise<void> };
    partnerGroupService: PartnerGroupServiceType;
};

const PartnerGroupContext = createContext<PartnerGroupContextType | undefined>(undefined);

export const usePartnerGroupContext = () => {
    const context = useContext(PartnerGroupContext);
    if (!context) {
        throw new Error("usePartnerGroupContext must be used within a PartnerGroupProvider");
    }
    return context;
};

type Props = {
    children: ReactNode;
};

export const PartnerGroupProvider: React.FC<Props> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);
    const { message, setMessage, error, setError } = useMessage();
    const { pageNation, setPageNation, criteria, setCriteria } = usePageNation<PartnerGroupCriteriaType>();
    const { modalIsOpen: searchModalIsOpen, setModalIsOpen: setSearchModalIsOpen } = useModal();
    const { modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId } = useModal();
    const {
        initialPartnerGroup,
        partnerGroups,
        setPartnerGroups,
        newPartnerGroup,
        setNewPartnerGroup,
        searchPartnerGroupCriteria,
        setSearchPartnerGroupCriteria,
        partnerGroupService
    } = usePartnerGroup();
    const fetchPartnerGroups = useFetchPartnerGroups(
        setLoading,
        setPartnerGroups,
        setPageNation,
        setError,
        showErrorMessage,
        partnerGroupService
    );

    const defaultCriteria: PartnerGroupCriteriaType = {};

    const value = useMemo(
        () => ({
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
            initialPartnerGroup,
            partnerGroups,
            setPartnerGroups,
            newPartnerGroup,
            setNewPartnerGroup,
            searchPartnerGroupCriteria,
            setSearchPartnerGroupCriteria,
            fetchPartnerGroups,
            partnerGroupService,
        }),
        [
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
            initialPartnerGroup,
            partnerGroups,
            setPartnerGroups,
            newPartnerGroup,
            setNewPartnerGroup,
            searchPartnerGroupCriteria,
            setSearchPartnerGroupCriteria,
            fetchPartnerGroups,
            partnerGroupService,
        ]
    );

    return <PartnerGroupContext.Provider value={value}>{children}</PartnerGroupContext.Provider>;
};