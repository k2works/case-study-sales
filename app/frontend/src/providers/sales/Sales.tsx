import React, { createContext, useContext, ReactNode, Dispatch, SetStateAction, useState, useMemo } from "react";
import { PageNationType, usePageNation } from "../../views/application/PageNation.tsx";
import { SalesCriteriaType, SalesType, initialSales, initialSalesCriteria } from "../../models/sales/sales";
import { useModal } from "../../components/application/hooks.ts";
import { useMessage } from "../../components/application/Message.tsx";
import { SalesService, SalesServiceType } from "../../services/sales/sales";
import { showErrorMessage } from "../../components/application/utils.ts";

type SalesContextType = {
    loading: boolean;
    setLoading: Dispatch<SetStateAction<boolean>>;
    message: string | null;
    setMessage: Dispatch<SetStateAction<string | null>>;
    error: string | null;
    setError: Dispatch<SetStateAction<string | null>>;
    pageNation: PageNationType | null;
    setPageNation: Dispatch<SetStateAction<PageNationType | null>>;
    criteria: SalesCriteriaType | null;
    setCriteria: Dispatch<SetStateAction<SalesCriteriaType | null>>;
    searchModalIsOpen: boolean;
    setSearchModalIsOpen: Dispatch<SetStateAction<boolean>>;
    modalIsOpen: boolean;
    setModalIsOpen: Dispatch<SetStateAction<boolean>>;
    isEditing: boolean;
    setIsEditing: Dispatch<SetStateAction<boolean>>;
    editId: string | null;
    setEditId: Dispatch<SetStateAction<string | null>>;
    initialSales: SalesType;
    sales: SalesType[];
    setSales: Dispatch<SetStateAction<SalesType[]>>;
    newSales: SalesType;
    setNewSales: Dispatch<SetStateAction<SalesType>>;
    searchSalesCriteria: SalesCriteriaType;
    setSearchSalesCriteria: Dispatch<SetStateAction<SalesCriteriaType>>;
    selectedLineIndex: number | null;
    setSelectedLineIndex: Dispatch<SetStateAction<number | null>>;
    fetchSales: { load: (page?: number, criteria?: SalesCriteriaType) => Promise<void> };
    salesService: SalesServiceType;
};

const SalesContext = createContext<SalesContextType | undefined>(undefined);

export const useSalesContext = () => {
    const context = useContext(SalesContext);
    if (!context) {
        throw new Error("useSalesContext must be used within a SalesProvider");
    }
    return context;
};

type Props = {
    children: ReactNode;
};

// Custom hook for Sales management
const useSales = () => {
    const [sales, setSales] = useState<SalesType[]>([]);
    const [newSales, setNewSales] = useState<SalesType>(initialSales);
    const [searchSalesCriteria, setSearchSalesCriteria] = useState<SalesCriteriaType>(initialSalesCriteria);
    const [selectedLineIndex, setSelectedLineIndex] = useState<number | null>(null);
    const salesService = SalesService();

    return {
        sales,
        setSales,
        newSales,
        setNewSales,
        searchSalesCriteria,
        setSearchSalesCriteria,
        selectedLineIndex,
        setSelectedLineIndex,
        salesService
    };
};

// Custom hook for fetching Sales
const useFetchSales = (
    setLoading: Dispatch<SetStateAction<boolean>>,
    setSales: Dispatch<SetStateAction<SalesType[]>>,
    setPageNation: Dispatch<SetStateAction<PageNationType | null>>,
    setError: Dispatch<SetStateAction<string | null>>,
    showErrorMessage: (message: string, setError: Dispatch<SetStateAction<string | null>>) => void,
    salesService: SalesServiceType
) => {
    const load = async (page?: number, criteria?: SalesCriteriaType) => {
        setLoading(true);
        try {
            let response;
            if (criteria) {
                response = await salesService.search(criteria, page);
            } else {
                response = await salesService.select(page);
            }
            setSales(response.list);
            setPageNation(response);
        } catch (error) {
            const errorMessage = error instanceof Error ? error.message : '不明なエラーが発生しました';
            showErrorMessage(`売上情報の取得に失敗しました: ${errorMessage}`, setError);
        } finally {
            setLoading(false);
        }
    };

    return { load };
};

export const SalesProvider: React.FC<Props> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);
    const { message, setMessage, error, setError } = useMessage();
    const { pageNation, setPageNation, criteria, setCriteria } = usePageNation<SalesCriteriaType | null>();
    const { modalIsOpen: searchModalIsOpen, setModalIsOpen: setSearchModalIsOpen } = useModal();
    const { modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId } = useModal();

    const {
        sales,
        setSales,
        newSales,
        setNewSales,
        searchSalesCriteria,
        setSearchSalesCriteria,
        selectedLineIndex,
        setSelectedLineIndex,
        salesService
    } = useSales();

    const fetchSales = useFetchSales(
        setLoading,
        setSales,
        setPageNation,
        setError,
        showErrorMessage,
        salesService
    );

    const defaultCriteria: SalesCriteriaType = {};

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
        initialSales,
        sales,
        setSales,
        newSales,
        setNewSales,
        searchSalesCriteria,
        setSearchSalesCriteria,
        selectedLineIndex,
        setSelectedLineIndex,
        fetchSales,
        salesService
    }), [criteria, defaultCriteria, salesService, sales, editId, error, fetchSales, isEditing, loading, message, modalIsOpen, newSales, pageNation, searchSalesCriteria, searchModalIsOpen, selectedLineIndex]);

    return (
        <SalesContext.Provider value={value}>
            {children}
        </SalesContext.Provider>
    );
};