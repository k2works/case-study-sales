import React, { createContext, useContext, useState, useMemo, useEffect, Dispatch, SetStateAction } from "react";
import {
    PurchaseOrderType,
    PurchaseOrderLineType,
    PurchaseOrderCriteriaType,
    PurchaseOrderSearchCriteriaType,
    PurchaseOrderFetchType,
    CompletionFlagEnumType,
    PurchaseType,
    PurchaseLineType,
    PurchaseCriteriaType,
    PurchaseSearchCriteriaType,
    PurchaseFetchType,
    initialPurchase,
    initialPurchaseLine,
    initialPurchaseCriteria
} from "../../models/procurement/purchase.ts";
import { PurchaseOrderService, PurchaseService, UploadResultType, RuleCheckResultType } from "../../services/procurement/purchase.ts";
import { useMessage } from "../../components/application/Message.tsx";

type Props = {
    children: React.ReactNode;
};

type PurchaseOrderContextType = {
    loading: boolean;
    setLoading: Dispatch<SetStateAction<boolean>>;
    message: string | null;
    setMessage: Dispatch<SetStateAction<string | null>>;
    error: string | null;
    setError: Dispatch<SetStateAction<string | null>>;
    pageNation: PurchaseOrderFetchType;
    criteria: PurchaseOrderCriteriaType;
    setCriteria: Dispatch<SetStateAction<PurchaseOrderCriteriaType>>;
    setPageNation: Dispatch<SetStateAction<PurchaseOrderFetchType>>;
    modalIsOpen: boolean;
    setModalIsOpen: Dispatch<SetStateAction<boolean>>;
    searchModalIsOpen: boolean;
    setSearchModalIsOpen: Dispatch<SetStateAction<boolean>>;
    isEditing: boolean;
    setIsEditing: Dispatch<SetStateAction<boolean>>;
    editId: string | null;
    setEditId: Dispatch<SetStateAction<string | null>>;
    initialPurchaseOrder: PurchaseOrderType;
    newPurchaseOrder: PurchaseOrderType;
    setNewPurchaseOrder: Dispatch<SetStateAction<PurchaseOrderType>>;
    selectedLineIndex: number | null;
    setSelectedLineIndex: Dispatch<SetStateAction<number | null>>;
    purchaseOrders: PurchaseOrderType[];
    setPurchaseOrders: Dispatch<SetStateAction<PurchaseOrderType[]>>;
    searchPurchaseOrderCriteria: PurchaseOrderSearchCriteriaType;
    setSearchPurchaseOrderCriteria: Dispatch<SetStateAction<PurchaseOrderSearchCriteriaType>>;
    searchCriteria: PurchaseOrderSearchCriteriaType;
    setSearchCriteria: Dispatch<SetStateAction<PurchaseOrderSearchCriteriaType>>;
    fetchPurchaseOrders: { load: (pageNumber?: number) => Promise<void> };
    purchaseOrderService: ReturnType<typeof PurchaseOrderService>;
    uploadModalIsOpen: boolean;
    setUploadModalIsOpen: Dispatch<SetStateAction<boolean>>;
    uploadResults: UploadResultType[];
    setUploadResults: Dispatch<SetStateAction<UploadResultType[]>>;
    uploadPurchaseOrders: (file: File) => Promise<void>;
};

const PurchaseContext = createContext<PurchaseOrderContextType | undefined>(undefined);

export const PurchaseProvider: React.FC<Props> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);
    const { message, setMessage, error, setError } = useMessage();
    const [modalIsOpen, setModalIsOpen] = useState<boolean>(false);
    const [searchModalIsOpen, setSearchModalIsOpen] = useState<boolean>(false);
    const [uploadModalIsOpen, setUploadModalIsOpen] = useState<boolean>(false);
    const [isEditing, setIsEditing] = useState<boolean>(false);
    const [editId, setEditId] = useState<string | null>(null);
    const [purchaseOrders, setPurchaseOrders] = useState<PurchaseOrderType[]>([]);
    const [pageNation, setPageNation] = useState<PurchaseOrderFetchType>({
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

    const initialPurchaseOrderLine: PurchaseOrderLineType = {
        purchaseOrderNumber: "",
        purchaseOrderLineNumber: 1,
        purchaseOrderLineDisplayNumber: 1,
        productCode: "",
        productName: "",
        purchaseUnitPrice: 0,
        purchaseOrderQuantity: 0,
        receivedQuantity: 0,
        completionFlag: CompletionFlagEnumType.未完了
    };

    const initialPurchaseOrder: PurchaseOrderType = {
        purchaseOrderNumber: "",
        purchaseOrderDate: new Date().toISOString().split('T')[0],
        supplierCode: "",
        supplierBranchNumber: 1,
        purchaseManagerCode: "",
        designatedDeliveryDate: new Date().toISOString().split('T')[0],
        totalPurchaseAmount: 0,
        totalConsumptionTax: 0,
        purchaseOrderLines: []
    };

    const [newPurchaseOrder, setNewPurchaseOrder] = useState<PurchaseOrderType>(initialPurchaseOrder);
    const [selectedLineIndex, setSelectedLineIndex] = useState<number | null>(null);
    const [searchPurchaseOrderCriteria, setSearchPurchaseOrderCriteria] = useState<PurchaseOrderSearchCriteriaType>({});
    const [searchCriteria, setSearchCriteria] = useState<PurchaseOrderSearchCriteriaType>({});
    const [criteria, setCriteria] = useState<PurchaseOrderCriteriaType>({});
    const [uploadResults, setUploadResults] = useState<UploadResultType[]>([]);

    const purchaseOrderService = PurchaseOrderService();

    const fetchPurchaseOrders = {
        load: async (pageNumber: number = 0) => {
            setLoading(true);
            try {
                const response = await purchaseOrderService.search(criteria, pageNumber, 20);
                setPurchaseOrders(response.list.map((purchaseOrder: PurchaseOrderType) => ({ ...purchaseOrder, checked: false })));
                setPageNation(response);
            } finally {
                setLoading(false);
            }
        }
    };

    useEffect(() => {
        const mappedCriteria: PurchaseOrderCriteriaType = {
            ...searchPurchaseOrderCriteria,
            completionFlag: searchPurchaseOrderCriteria.completionFlag === "true"
        };
        setCriteria(mappedCriteria);
    }, [searchPurchaseOrderCriteria]);

    const uploadPurchaseOrders = async (file: File) => {
        try {
            setLoading(true);
            const results = await purchaseOrderService.upload(file);
            setUploadResults(prev => [...prev, ...results]);
            setMessage("発注データのアップロードが完了しました。");
        } catch (error: any) {
            setError(`発注データのアップロードに失敗しました: ${error?.message}`);
            throw error;
        } finally {
            setLoading(false);
        }
    };

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
            initialPurchaseOrder,
            newPurchaseOrder,
            setNewPurchaseOrder,
            selectedLineIndex,
            setSelectedLineIndex,
            purchaseOrders,
            setPurchaseOrders,
            searchPurchaseOrderCriteria,
            setSearchPurchaseOrderCriteria,
            searchCriteria,
            setSearchCriteria,
            fetchPurchaseOrders,
            purchaseOrderService,
            uploadModalIsOpen,
            setUploadModalIsOpen,
            uploadResults,
            setUploadResults,
            uploadPurchaseOrders,
        }),
        [loading, message, setMessage, error, setError, pageNation, criteria, modalIsOpen, searchModalIsOpen, uploadModalIsOpen, isEditing, editId, newPurchaseOrder, selectedLineIndex, purchaseOrders, searchPurchaseOrderCriteria, searchCriteria, uploadResults, purchaseOrderService]
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

// ===== Purchase Receipt Provider =====

type PurchaseReceiptContextType = {
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

const PurchaseReceiptContext = createContext<PurchaseReceiptContextType | undefined>(undefined);

export const PurchaseReceiptProvider: React.FC<Props> = ({ children }) => {
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

    return <PurchaseReceiptContext.Provider value={value}>{children}</PurchaseReceiptContext.Provider>;
};

export const usePurchaseReceiptContext = () => {
    const context = useContext(PurchaseReceiptContext);
    if (context === undefined) {
        throw new Error("usePurchaseReceiptContext must be used within a PurchaseReceiptProvider");
    }
    return context;
};