import React, { createContext, useContext, ReactNode, Dispatch, SetStateAction, useState, useMemo } from "react";
import { PageNationType, usePageNation } from "../../views/application/PageNation.tsx";
import { InvoiceCriteriaType, InvoiceType, initialInvoice, initialInvoiceCriteria } from "../../models/sales/invoice";
import { useModal } from "../../components/application/hooks.ts";
import { useMessage } from "../../components/application/Message.tsx";
import { InvoiceService, InvoiceServiceType } from "../../services/sales/invoice";
import { showErrorMessage } from "../../components/application/utils.ts";

type InvoiceContextType = {
    loading: boolean;
    setLoading: Dispatch<SetStateAction<boolean>>;
    message: string | null;
    setMessage: Dispatch<SetStateAction<string | null>>;
    error: string | null;
    setError: Dispatch<SetStateAction<string | null>>;
    pageNation: PageNationType | null;
    setPageNation: Dispatch<SetStateAction<PageNationType | null>>;
    criteria: InvoiceCriteriaType | null;
    setCriteria: Dispatch<SetStateAction<InvoiceCriteriaType | null>>;
    searchModalIsOpen: boolean;
    setSearchModalIsOpen: Dispatch<SetStateAction<boolean>>;
    modalIsOpen: boolean;
    setModalIsOpen: Dispatch<SetStateAction<boolean>>;
    isEditing: boolean;
    setIsEditing: Dispatch<SetStateAction<boolean>>;
    editId: string | null;
    setEditId: Dispatch<SetStateAction<string | null>>;
    initialInvoice: InvoiceType;
    invoices: InvoiceType[];
    setInvoices: Dispatch<SetStateAction<InvoiceType[]>>;
    newInvoice: InvoiceType;
    setNewInvoice: Dispatch<SetStateAction<InvoiceType>>;
    searchInvoiceCriteria: InvoiceCriteriaType;
    setSearchInvoiceCriteria: Dispatch<SetStateAction<InvoiceCriteriaType>>;
    selectedLineIndex: number | null;
    setSelectedLineIndex: Dispatch<SetStateAction<number | null>>;
    fetchInvoices: { load: (page?: number, criteria?: InvoiceCriteriaType) => Promise<void> };
    invoiceService: InvoiceServiceType;
};

const InvoiceContext = createContext<InvoiceContextType | undefined>(undefined);

export const useInvoiceContext = () => {
    const context = useContext(InvoiceContext);
    if (!context) {
        throw new Error("useInvoiceContext must be used within an InvoiceProvider");
    }
    return context;
};

type Props = {
    children: ReactNode;
};

// Custom hook for Invoice management
const useInvoices = () => {
    const [invoices, setInvoices] = useState<InvoiceType[]>([]);
    const [newInvoice, setNewInvoice] = useState<InvoiceType>(initialInvoice);
    const [searchInvoiceCriteria, setSearchInvoiceCriteria] = useState<InvoiceCriteriaType>(initialInvoiceCriteria);
    const [selectedLineIndex, setSelectedLineIndex] = useState<number | null>(null);
    const invoiceService = InvoiceService();

    return {
        invoices,
        setInvoices,
        newInvoice,
        setNewInvoice,
        searchInvoiceCriteria,
        setSearchInvoiceCriteria,
        selectedLineIndex,
        setSelectedLineIndex,
        invoiceService
    };
};

// Custom hook for fetching Invoices
const useFetchInvoices = (
    setLoading: Dispatch<SetStateAction<boolean>>,
    setInvoices: Dispatch<SetStateAction<InvoiceType[]>>,
    setPageNation: Dispatch<SetStateAction<PageNationType | null>>,
    setError: Dispatch<SetStateAction<string | null>>,
    showErrorMessage: (message: string, setError: Dispatch<SetStateAction<string | null>>) => void,
    invoiceService: InvoiceServiceType
) => {
    const load = async (page?: number, criteria?: InvoiceCriteriaType) => {
        setLoading(true);
        try {
            let response;
            if (criteria) {
                response = await invoiceService.search(criteria, page);
            } else {
                response = await invoiceService.select(page);
            }
            setInvoices(response.list);
            setPageNation(response);
        } catch (error) {
            const errorMessage = error instanceof Error ? error.message : '不明なエラーが発生しました';
            showErrorMessage(`請求情報の取得に失敗しました: ${errorMessage}`, setError);
        } finally {
            setLoading(false);
        }
    };

    return { load };
};

export const InvoiceProvider: React.FC<Props> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);
    const { message, setMessage, error, setError } = useMessage();
    const { pageNation, setPageNation, criteria, setCriteria } = usePageNation<InvoiceCriteriaType | null>();
    const { modalIsOpen: searchModalIsOpen, setModalIsOpen: setSearchModalIsOpen } = useModal();
    const { modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId } = useModal();

    const {
        invoices,
        setInvoices,
        newInvoice,
        setNewInvoice,
        searchInvoiceCriteria,
        setSearchInvoiceCriteria,
        selectedLineIndex,
        setSelectedLineIndex,
        invoiceService
    } = useInvoices();

    const fetchInvoices = useFetchInvoices(
        setLoading,
        setInvoices,
        setPageNation,
        setError,
        showErrorMessage,
        invoiceService
    );

    const defaultCriteria: InvoiceCriteriaType = {};

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
        initialInvoice,
        invoices,
        setInvoices,
        newInvoice,
        setNewInvoice,
        searchInvoiceCriteria,
        setSearchInvoiceCriteria,
        selectedLineIndex,
        setSelectedLineIndex,
        fetchInvoices,
        invoiceService
    }), [criteria, defaultCriteria, invoiceService, invoices, editId, error, fetchInvoices, isEditing, loading, message, modalIsOpen, newInvoice, pageNation, searchInvoiceCriteria, searchModalIsOpen, selectedLineIndex]);

    return (
        <InvoiceContext.Provider value={value}>
            {children}
        </InvoiceContext.Provider>
    );
};