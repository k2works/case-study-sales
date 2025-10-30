import React, { createContext, useContext, useState, useMemo, useEffect, Dispatch, SetStateAction } from "react";
import {
    PurchaseType,
    PurchaseLineType,
    PurchaseCriteriaType,
    PurchaseSearchCriteriaType,
    PurchaseFetchType,
    initialPurchase,
    initialPurchaseLine,
    initialPurchaseCriteria
} from "../../models/procurement/purchase.ts";
import { PurchaseService, RuleCheckResultType } from "../../services/procurement/purchase.ts";
import { useMessage } from "../../components/application/Message.tsx";

type Props = {
    children: React.ReactNode;
};

type PurchaseContextType = {
    loading: boolean;
    setLoading: Dispatch<SetStateAction<boolean>>;
    message: string | null;
    setMessage: Dispatch<SetStateAction<string | null>>;
    error: string | null;
    setError: Dispatch<SetStateAction<string | null>>;
    pageNation: PurchaseFetchType;
    criteria: PurchaseCriteriaType;
    setCriteria: Dispatch<SetStateAction<PurchaseCriteriaType>>;
    setPageNation: Dispatch<SetStateAction<PurchaseFetchType>>;
    modalIsOpen: boolean;
    setModalIsOpen: Dispatch<SetStateAction<boolean>>;
    searchModalIsOpen: boolean;
    setSearchModalIsOpen: Dispatch<SetStateAction<boolean>>;
    isEditing: boolean;
    setIsEditing: Dispatch<SetStateAction<boolean>>;
    editId: string | null;
    setEditId: Dispatch<SetStateAction<string | null>>;
    initialPurchase: PurchaseType;
    newPurchase: PurchaseType;
    setNewPurchase: Dispatch<SetStateAction<PurchaseType>>;
    selectedLineIndex: number | null;
    setSelectedLineIndex: Dispatch<SetStateAction<number | null>>;
    purchases: PurchaseType[];
    setPurchases: Dispatch<SetStateAction<PurchaseType[]>>;
    searchPurchaseCriteria: PurchaseSearchCriteriaType;
    setSearchPurchaseCriteria: Dispatch<SetStateAction<PurchaseSearchCriteriaType>>;
    searchCriteria: PurchaseSearchCriteriaType;
    setSearchCriteria: Dispatch<SetStateAction<PurchaseSearchCriteriaType>>;
    fetchPurchases: { load: (pageNumber?: number) => Promise<void> };
    purchaseService: ReturnType<typeof PurchaseService>;
    ruleCheckResults: RuleCheckResultType[];
    setRuleCheckResults: Dispatch<SetStateAction<RuleCheckResultType[]>>;
};

const PurchaseContext = createContext<PurchaseContextType | undefined>(undefined);

export const PurchaseProvider: React.FC<Props> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);
    const { message, setMessage, error, setError } = useMessage();
    const [modalIsOpen, setModalIsOpen] = useState<boolean>(false);
    const [searchModalIsOpen, setSearchModalIsOpen] = useState<boolean>(false);
    const [isEditing, setIsEditing] = useState<boolean>(false);
    const [editId, setEditId] = useState<string | null>(null);
    const [purchases, setPurchases] = useState<PurchaseType[]>([]);
    const [pageNation, setPageNation] = useState<PurchaseFetchType>({
        list: [],
        endRow: 0,
        hasNextPage: false,
        hasPreviousPage: false,
        isFirstPage: true,
        isLastPage: true,
        navigateFirstPage: 1,
        navigateLastPage: 1,
        navigatePages: 8,
        navigatepageNums: [],
        nextPage: 0,
        pageNum: 1,
        pageSize: 20,
        pages: 0,
        prePage: 0,
        size: 0,
        startRow: 0,
        total: 0
    });

    const [newPurchase, setNewPurchase] = useState<PurchaseType>(initialPurchase);
    const [selectedLineIndex, setSelectedLineIndex] = useState<number | null>(null);
    const [searchPurchaseCriteria, setSearchPurchaseCriteria] = useState<PurchaseSearchCriteriaType>({});
    const [searchCriteria, setSearchCriteria] = useState<PurchaseSearchCriteriaType>({});
    const [criteria, setCriteria] = useState<PurchaseCriteriaType>({});
    const [ruleCheckResults, setRuleCheckResults] = useState<RuleCheckResultType[]>([]);

    const purchaseService = PurchaseService();

    const fetchPurchases = {
        load: async (pageNumber: number = 0) => {
            setLoading(true);
            try {
                const response = await purchaseService.search(criteria, pageNumber, 20);
                setPurchases(response.list.map((purchase: PurchaseType) => ({ ...purchase, checked: false })));
                setPageNation(response);
            } finally {
                setLoading(false);
            }
        }
    };

    useEffect(() => {
        const mappedCriteria: PurchaseCriteriaType = {
            ...searchPurchaseCriteria,
            supplierBranchNumber: searchPurchaseCriteria.supplierBranchNumber
                ? Number(searchPurchaseCriteria.supplierBranchNumber)
                : undefined
        };
        setCriteria(mappedCriteria);
    }, [searchPurchaseCriteria]);

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
            criteria,
            setCriteria,
            modalIsOpen,
            setModalIsOpen,
            searchModalIsOpen,
            setSearchModalIsOpen,
            isEditing,
            setIsEditing,
            editId,
            setEditId,
            initialPurchase,
            newPurchase,
            setNewPurchase,
            selectedLineIndex,
            setSelectedLineIndex,
            purchases,
            setPurchases,
            searchPurchaseCriteria,
            setSearchPurchaseCriteria,
            searchCriteria,
            setSearchCriteria,
            fetchPurchases,
            purchaseService,
            ruleCheckResults,
            setRuleCheckResults,
        }),
        [loading, message, setMessage, error, setError, pageNation, criteria, modalIsOpen, searchModalIsOpen, isEditing, editId, newPurchase, selectedLineIndex, purchases, searchPurchaseCriteria, searchCriteria, purchaseService, ruleCheckResults]
    );

    return <PurchaseContext.Provider value={value}>{children}</PurchaseContext.Provider>;
};

export const usePurchaseContext = () => {
    const context = useContext(PurchaseContext);
    if (context === undefined) {
        throw new Error("usePurchaseContext must be used within a PurchaseProvider");
    }
    return context;
};