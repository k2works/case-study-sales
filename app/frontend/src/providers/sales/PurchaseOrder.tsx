import React, { createContext, useContext, useState, useMemo, useEffect, Dispatch, SetStateAction } from "react";
import { 
    PurchaseOrderType, 
    PurchaseOrderLineType,
    PurchaseOrderCriteriaType, 
    PurchaseOrderSearchCriteriaType,
    PurchaseOrderFetchType,
    PurchaseOrderPageNationType,
    CompletionFlagEnumType
} from "../../models/sales/purchaseOrder.ts";
import { PurchaseOrderService } from "../../services/sales/purchaseOrder.ts";
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
};

const PurchaseOrderContext = createContext<PurchaseOrderContextType | undefined>(undefined);

export const PurchaseOrderProvider: React.FC<Props> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);
    const { message, setMessage, error, setError } = useMessage();
    const [modalIsOpen, setModalIsOpen] = useState<boolean>(false);
    const [searchModalIsOpen, setSearchModalIsOpen] = useState<boolean>(false);
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
        purchaseOrderLines: [initialPurchaseOrderLine]
    };

    const [newPurchaseOrder, setNewPurchaseOrder] = useState<PurchaseOrderType>(initialPurchaseOrder);
    const [selectedLineIndex, setSelectedLineIndex] = useState<number | null>(null);
    const [searchPurchaseOrderCriteria, setSearchPurchaseOrderCriteria] = useState<PurchaseOrderSearchCriteriaType>({});
    const [searchCriteria, setSearchCriteria] = useState<PurchaseOrderSearchCriteriaType>({});
    const [criteria, setCriteria] = useState<PurchaseOrderCriteriaType>({});

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
        }),
        [loading, message, setMessage, error, setError, pageNation, criteria, modalIsOpen, searchModalIsOpen, isEditing, editId, newPurchaseOrder, selectedLineIndex, purchaseOrders, searchPurchaseOrderCriteria, searchCriteria, purchaseOrderService]
    );

    return <PurchaseOrderContext.Provider value={value}>{children}</PurchaseOrderContext.Provider>;
};

export const usePurchaseOrderContext = () => {
    const context = useContext(PurchaseOrderContext);
    if (context === undefined) {
        throw new Error("usePurchaseOrderContext must be used within a PurchaseOrderProvider");
    }
    return context;
};