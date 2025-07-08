import React, { createContext, useContext, ReactNode, Dispatch, SetStateAction, useState, useMemo } from "react";
import { PageNationType, usePageNation } from "../../views/application/PageNation.tsx";
import { PaymentCriteriaType, PaymentType, initialPayment, initialPaymentCriteria } from "../../models/sales/payment";
import { useModal } from "../../components/application/hooks.ts";
import { useMessage } from "../../components/application/Message.tsx";
import { PaymentService, PaymentServiceType } from "../../services/sales/payment";
import { showErrorMessage } from "../../components/application/utils.ts";

type PaymentContextType = {
    loading: boolean;
    setLoading: Dispatch<SetStateAction<boolean>>;
    message: string | null;
    setMessage: Dispatch<SetStateAction<string | null>>;
    error: string | null;
    setError: Dispatch<SetStateAction<string | null>>;
    pageNation: PageNationType | null;
    setPageNation: Dispatch<SetStateAction<PageNationType | null>>;
    criteria: PaymentCriteriaType | null;
    setCriteria: Dispatch<SetStateAction<PaymentCriteriaType | null>>;
    searchModalIsOpen: boolean;
    setSearchModalIsOpen: Dispatch<SetStateAction<boolean>>;
    modalIsOpen: boolean;
    setModalIsOpen: Dispatch<SetStateAction<boolean>>;
    isEditing: boolean;
    setIsEditing: Dispatch<SetStateAction<boolean>>;
    editId: string | null;
    setEditId: Dispatch<SetStateAction<string | null>>;
    initialPayment: PaymentType;
    payments: PaymentType[];
    setPayments: Dispatch<SetStateAction<PaymentType[]>>;
    newPayment: PaymentType;
    setNewPayment: Dispatch<SetStateAction<PaymentType>>;
    searchPaymentCriteria: PaymentCriteriaType;
    setSearchPaymentCriteria: Dispatch<SetStateAction<PaymentCriteriaType>>;
    fetchPayments: { load: (page?: number, criteria?: PaymentCriteriaType) => Promise<void> };
    paymentService: PaymentServiceType;
};

const PaymentContext = createContext<PaymentContextType | undefined>(undefined);

export const usePaymentContext = () => {
    const context = useContext(PaymentContext);
    if (!context) {
        throw new Error("usePaymentContext must be used within a PaymentProvider");
    }
    return context;
};

type Props = {
    children: ReactNode;
};

// Custom hook for Payment management
const usePayment = () => {
    const [payments, setPayments] = useState<PaymentType[]>([]);
    const [newPayment, setNewPayment] = useState<PaymentType>(initialPayment);
    const [searchPaymentCriteria, setSearchPaymentCriteria] = useState<PaymentCriteriaType>(initialPaymentCriteria);
    const paymentService = PaymentService();

    return {
        payments,
        setPayments,
        newPayment,
        setNewPayment,
        searchPaymentCriteria,
        setSearchPaymentCriteria,
        paymentService
    };
};

// Custom hook for fetching Payments
const useFetchPayments = (
    setLoading: Dispatch<SetStateAction<boolean>>,
    setPayments: Dispatch<SetStateAction<PaymentType[]>>,
    setPageNation: Dispatch<SetStateAction<PageNationType | null>>,
    setError: Dispatch<SetStateAction<string | null>>,
    showErrorMessage: (message: string, setError: Dispatch<SetStateAction<string | null>>) => void,
    paymentService: PaymentServiceType
) => {
    const load = async (page?: number, criteria?: PaymentCriteriaType) => {
        setLoading(true);
        try {
            let response;
            if (criteria) {
                response = await paymentService.search(criteria, page);
            } else {
                response = await paymentService.select(page);
            }
            setPayments(response.list);
            setPageNation(response);
        } catch (error) {
            const errorMessage = error instanceof Error ? error.message : '不明なエラーが発生しました';
            showErrorMessage(`入金情報の取得に失敗しました: ${errorMessage}`, setError);
        } finally {
            setLoading(false);
        }
    };

    return { load };
};

export const PaymentProvider: React.FC<Props> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);
    const { message, setMessage, error, setError } = useMessage();
    const { pageNation, setPageNation, criteria, setCriteria } = usePageNation<PaymentCriteriaType | null>();
    const { modalIsOpen: searchModalIsOpen, setModalIsOpen: setSearchModalIsOpen } = useModal();
    const { modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId } = useModal();

    const {
        payments,
        setPayments,
        newPayment,
        setNewPayment,
        searchPaymentCriteria,
        setSearchPaymentCriteria,
        paymentService
    } = usePayment();

    const fetchPayments = useFetchPayments(
        setLoading,
        setPayments,
        setPageNation,
        setError,
        showErrorMessage,
        paymentService
    );

    const defaultCriteria: PaymentCriteriaType = {};

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
        initialPayment,
        payments,
        setPayments,
        newPayment,
        setNewPayment,
        searchPaymentCriteria,
        setSearchPaymentCriteria,
        fetchPayments,
        paymentService
    }), [criteria, defaultCriteria, paymentService, payments, editId, error, fetchPayments, isEditing, loading, message, modalIsOpen, newPayment, pageNation, searchPaymentCriteria, searchModalIsOpen]);

    return (
        <PaymentContext.Provider value={value}>
            {children}
        </PaymentContext.Provider>
    );
};