import React, { createContext, useContext, ReactNode, Dispatch, SetStateAction, useState, useMemo } from "react";
import { PageNationType, usePageNation } from "../../views/application/PageNation.tsx";
import { SalesOrderCriteriaType, SalesOrderType } from "../../models/sales/sales_order";
import { useModal } from "../../components/application/hooks.ts";
import { useMessage } from "../../components/application/Message.tsx";
import { SalesOrderService, SalesOrderServiceType } from "../../services/sales/sales_order";
import { showErrorMessage } from "../../components/application/utils.ts";

type SalesOrderContextType = {
    loading: boolean;
    setLoading: Dispatch<SetStateAction<boolean>>;
    message: string | null;
    setMessage: Dispatch<SetStateAction<string | null>>;
    error: string | null;
    setError: Dispatch<SetStateAction<string | null>>;
    pageNation: PageNationType | null;
    setPageNation: Dispatch<SetStateAction<PageNationType | null>>;
    criteria: SalesOrderCriteriaType | null;
    setCriteria: Dispatch<SetStateAction<SalesOrderCriteriaType | null>>;
    searchModalIsOpen: boolean;
    setSearchModalIsOpen: Dispatch<SetStateAction<boolean>>;
    modalIsOpen: boolean;
    setModalIsOpen: Dispatch<SetStateAction<boolean>>;
    isEditing: boolean;
    setIsEditing: Dispatch<SetStateAction<boolean>>;
    editId: string | null;
    setEditId: Dispatch<SetStateAction<string | null>>;
    initialSalesOrder: SalesOrderType;
    salesOrders: SalesOrderType[];
    setSalesOrders: Dispatch<SetStateAction<SalesOrderType[]>>;
    newSalesOrder: SalesOrderType;
    setNewSalesOrder: Dispatch<SetStateAction<SalesOrderType>>;
    searchSalesOrderCriteria: SalesOrderCriteriaType;
    setSearchSalesOrderCriteria: Dispatch<SetStateAction<SalesOrderCriteriaType>>;
    selectedLineIndex: number | null;
    setSelectedLineIndex: Dispatch<SetStateAction<number | null>>;
    fetchSalesOrders: { load: (page?: number, criteria?: SalesOrderCriteriaType) => Promise<void> };
    salesOrderService: SalesOrderServiceType;
};

const SalesOrderContext = createContext<SalesOrderContextType | undefined>(undefined);

export const useSalesOrderContext = () => {
    const context = useContext(SalesOrderContext);
    if (!context) {
        throw new Error("useSalesOrderContext must be used within a SalesOrderProvider");
    }
    return context;
};

type Props = {
    children: ReactNode;
};

// Custom hook for SalesOrder management
const useSalesOrder = () => {
    const initialSalesOrder: SalesOrderType = {
        orderNumber: '',
        orderDate: '',
        departmentCode: '',
        departmentStartDate: '',
        customerCode: '',
        customerBranchNumber: 0,
        employeeCode: '',
        desiredDeliveryDate: '',
        customerOrderNumber: '',
        warehouseCode: '',
        totalOrderAmount: 0,
        totalConsumptionTax: 0,
        remarks: '',
        salesOrderLines: [],
        checked: false
    };

    const [salesOrders, setSalesOrders] = useState<SalesOrderType[]>([]);
    const [newSalesOrder, setNewSalesOrder] = useState<SalesOrderType>(initialSalesOrder);
    const [searchSalesOrderCriteria, setSearchSalesOrderCriteria] = useState<SalesOrderCriteriaType>({});
    const [selectedLineIndex, setSelectedLineIndex] = useState<number | null>(null);
    const salesOrderService = SalesOrderService();

    return {
        initialSalesOrder,
        salesOrders,
        setSalesOrders,
        newSalesOrder,
        setNewSalesOrder,
        searchSalesOrderCriteria,
        setSearchSalesOrderCriteria,
        selectedLineIndex,
        setSelectedLineIndex,
        salesOrderService
    };
};

// Custom hook for fetching SalesOrders
const useFetchSalesOrders = (
    setLoading: Dispatch<SetStateAction<boolean>>,
    setSalesOrders: Dispatch<SetStateAction<SalesOrderType[]>>,
    setPageNation: Dispatch<SetStateAction<PageNationType | null>>,
    setError: Dispatch<SetStateAction<string | null>>,
    showErrorMessage: (message: string, setError: Dispatch<SetStateAction<string | null>>) => void,
    salesOrderService: SalesOrderServiceType
) => {
    const load = async (page?: number, criteria?: SalesOrderCriteriaType) => {
        setLoading(true);
        try {
            let response;
            if (criteria) {
                response = await salesOrderService.search(criteria, page);
            } else {
                response = await salesOrderService.select(page);
            }
            setSalesOrders(response.list);
            setPageNation(response);
        } catch (error: any) {
            showErrorMessage(`受注情報の取得に失敗しました: ${error?.message}`, setError);
        } finally {
            setLoading(false);
        }
    };

    return { load };
};

export const SalesOrderProvider: React.FC<Props> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);
    const { message, setMessage, error, setError } = useMessage();
    const { pageNation, setPageNation, criteria, setCriteria } = usePageNation<SalesOrderCriteriaType | null>();
    const { modalIsOpen: searchModalIsOpen, setModalIsOpen: setSearchModalIsOpen } = useModal();
    const { modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId } = useModal();

    const {
        initialSalesOrder,
        salesOrders,
        setSalesOrders,
        newSalesOrder,
        setNewSalesOrder,
        searchSalesOrderCriteria,
        setSearchSalesOrderCriteria,
        selectedLineIndex,
        setSelectedLineIndex,
        salesOrderService
    } = useSalesOrder();

    const fetchSalesOrders = useFetchSalesOrders(
        setLoading,
        setSalesOrders,
        setPageNation,
        setError,
        showErrorMessage,
        salesOrderService
    );

    const defaultCriteria: SalesOrderCriteriaType = {};

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
        initialSalesOrder,
        salesOrders,
        setSalesOrders,
        newSalesOrder,
        setNewSalesOrder,
        searchSalesOrderCriteria,
        setSearchSalesOrderCriteria,
        selectedLineIndex,
        setSelectedLineIndex,
        fetchSalesOrders,
        salesOrderService
    }), [criteria, defaultCriteria, salesOrderService, salesOrders, editId, error, fetchSalesOrders, initialSalesOrder, isEditing, loading, message, modalIsOpen, newSalesOrder, pageNation, searchSalesOrderCriteria, searchModalIsOpen, selectedLineIndex]);

    return (
        <SalesOrderContext.Provider value={value}>
            {children}
        </SalesOrderContext.Provider>
    );
};
