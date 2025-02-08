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
import {
    PartnerCategoryCriteriaType,
    PartnerCategoryType,
    PartnerCategoryItemType,
} from "../models/master/partner";
import {
    useFetchPartnerCategories,
    usePartnerCategory,
} from "../components/master/partner/hooks";
import {PartnerCategoryServiceType} from "../services/master/partnerCategory.ts";

type PartnerCategoryContextType = {
    loading: boolean;
    setLoading: Dispatch<SetStateAction<boolean>>;
    message: string | null;
    setMessage: Dispatch<SetStateAction<string | null>>;
    error: string | null;
    setError: Dispatch<SetStateAction<string | null>>;
    pageNation: PageNationType | null;
    setPageNation: Dispatch<SetStateAction<PageNationType | null>>;
    criteria: PartnerCategoryCriteriaType | null;
    setCriteria: Dispatch<SetStateAction<PartnerCategoryCriteriaType | null>>;
    searchModalIsOpen: boolean;
    setSearchModalIsOpen: Dispatch<SetStateAction<boolean>>;
    modalIsOpen: boolean;
    setModalIsOpen: Dispatch<SetStateAction<boolean>>;
    isEditing: boolean;
    setIsEditing: Dispatch<SetStateAction<boolean>>;
    editId: string | null;
    setEditId: Dispatch<SetStateAction<string | null>>;
    categoryItemModalIsOpen: boolean;
    setCategoryItemModalIsOpen: Dispatch<SetStateAction<boolean>>;
    setCategoryItemIsEditing: Dispatch<SetStateAction<boolean>>;
    setCategoryItemEditId: Dispatch<SetStateAction<string | null>>;
    initialPartnerCategory: PartnerCategoryType;
    partnerCategories: PartnerCategoryType[];
    setPartnerCategories: Dispatch<SetStateAction<PartnerCategoryType[]>>;
    newPartnerCategory: PartnerCategoryType;
    setNewPartnerCategory: Dispatch<SetStateAction<PartnerCategoryType>>;
    newPartnerCategoryItem: PartnerCategoryItemType;
    setNewPartnerCategoryItem: Dispatch<SetStateAction<PartnerCategoryItemType>>;
    searchPartnerCategoryCriteria: PartnerCategoryCriteriaType;
    setSearchPartnerCategoryCriteria: Dispatch<SetStateAction<PartnerCategoryCriteriaType>>;
    fetchPartnerCategories: { load: (page?: number, criteria?: PartnerCategoryCriteriaType) => Promise<void> };
    partnerCategoryService: PartnerCategoryServiceType;
};

const PartnerCategoryContext = createContext<PartnerCategoryContextType | undefined>(undefined);

export const usePartnerCategoryContext = () => {
    const context = useContext(PartnerCategoryContext);
    if (!context) {
        throw new Error("usePartnerCategoryContext must be used within a PartnerCategoryProvider");
    }
    return context;
};

type Props = {
    children: ReactNode;
};

export const PartnerCategoryProvider: React.FC<Props> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);
    const { message, setMessage, error, setError } = useMessage();

    const { pageNation, setPageNation, criteria, setCriteria } = usePageNation<PartnerCategoryCriteriaType>();
    const { modalIsOpen: searchModalIsOpen, setModalIsOpen: setSearchModalIsOpen } = useModal();
    const { modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId } = useModal();
    const {
        modalIsOpen: categoryItemModalIsOpen,
        setModalIsOpen: setCategoryItemModalIsOpen,
        setIsEditing: setCategoryItemIsEditing,
        setEditId: setCategoryItemEditId
    } = useModal();

    const {
        initialPartnerCategory,
        partnerCategories,
        setPartnerCategories,
        newPartnerCategory,
        newPartnerCategoryItem,
        setNewPartnerCategoryItem,
        setNewPartnerCategory,
        searchPartnerCategoryCriteria,
        setSearchPartnerCategoryCriteria,
        partnerCategoryService
    } = usePartnerCategory();

    const fetchPartnerCategories = useFetchPartnerCategories(
        setLoading,
        setPartnerCategories,
        setPageNation,
        setError,
        showErrorMessage,
        partnerCategoryService
    );

    const defaultCriteria: PartnerCategoryCriteriaType = {};

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
            categoryItemModalIsOpen,
            setCategoryItemModalIsOpen,
            setCategoryItemIsEditing,
            setCategoryItemEditId,
            initialPartnerCategory,
            partnerCategories,
            setPartnerCategories,
            newPartnerCategory,
            setNewPartnerCategory,
            newPartnerCategoryItem,
            setNewPartnerCategoryItem,
            searchPartnerCategoryCriteria,
            setSearchPartnerCategoryCriteria,
            fetchPartnerCategories,
            partnerCategoryService,
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
            categoryItemModalIsOpen,
            setCategoryItemModalIsOpen,
            setCategoryItemIsEditing,
            setCategoryItemEditId,
            initialPartnerCategory,
            partnerCategories,
            setPartnerCategories,
            newPartnerCategory,
            setNewPartnerCategory,
            newPartnerCategoryItem,
            setNewPartnerCategoryItem,
            searchPartnerCategoryCriteria,
            setSearchPartnerCategoryCriteria,
            fetchPartnerCategories,
            partnerCategoryService,
        ]
    );

    return <PartnerCategoryContext.Provider value={value}>{children}</PartnerCategoryContext.Provider>;
};