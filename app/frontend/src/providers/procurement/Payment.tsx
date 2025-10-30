import React, { createContext, useContext, useState, useMemo, useEffect, Dispatch, SetStateAction } from "react";
import {
    PaymentType,
    PaymentCriteriaType,
    PaymentSearchCriteriaType,
    PaymentFetchType,
} from "../../models/procurement/payment.ts";
import { PaymentService } from "../../services/procurement/payment.ts";
import { useMessage } from "../../components/application/Message.tsx";

type Props = {
    children: React.ReactNode;
};

type PaymentContextType = {
    loading: boolean;
    setLoading: Dispatch<SetStateAction<boolean>>;
    message: string | null;
    setMessage: Dispatch<SetStateAction<string | null>>;
    error: string | null;
    setError: Dispatch<SetStateAction<string | null>>;
    pageNation: PaymentFetchType;
    criteria: PaymentCriteriaType;
    setCriteria: Dispatch<SetStateAction<PaymentCriteriaType>>;
    setPageNation: Dispatch<SetStateAction<PaymentFetchType>>;
    modalIsOpen: boolean;
    setModalIsOpen: Dispatch<SetStateAction<boolean>>;
    searchModalIsOpen: boolean;
    setSearchModalIsOpen: Dispatch<SetStateAction<boolean>>;
    isEditing: boolean;
    setIsEditing: Dispatch<SetStateAction<boolean>>;
    editId: string | null;
    setEditId: Dispatch<SetStateAction<string | null>>;
    initialPayment: PaymentType;
    newPayment: PaymentType;
    setNewPayment: Dispatch<SetStateAction<PaymentType>>;
    payments: PaymentType[];
    setPayments: Dispatch<SetStateAction<PaymentType[]>>;
    searchPaymentCriteria: PaymentSearchCriteriaType;
    setSearchPaymentCriteria: Dispatch<SetStateAction<PaymentSearchCriteriaType>>;
    searchCriteria: PaymentSearchCriteriaType;
    setSearchCriteria: Dispatch<SetStateAction<PaymentSearchCriteriaType>>;
    fetchPayments: { load: (pageNumber?: number) => Promise<void> };
    paymentService: ReturnType<typeof PaymentService>;
};

const PaymentContext = createContext<PaymentContextType | undefined>(undefined);

export const PaymentProvider: React.FC<Props> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);
    const { message, setMessage, error, setError } = useMessage();
    const [modalIsOpen, setModalIsOpen] = useState<boolean>(false);
    const [searchModalIsOpen, setSearchModalIsOpen] = useState<boolean>(false);
    const [isEditing, setIsEditing] = useState<boolean>(false);
    const [editId, setEditId] = useState<string | null>(null);
    const [payments, setPayments] = useState<PaymentType[]>([]);
    const [pageNation, setPageNation] = useState<PaymentFetchType>({
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

    const initialPayment: PaymentType = {
        paymentNumber: "",
        paymentDate: new Date().toISOString().split('T')[0],
        departmentCode: "",
        departmentStartDate: new Date().toISOString().split('T')[0],
        supplierCode: "",
        supplierBranchNumber: 1,
        paymentMethodType: 1,
        paymentAmount: 0,
        totalConsumptionTax: 0,
        paymentCompletedFlag: false,
        supplierName: "",
        checked: false
    };

    const [newPayment, setNewPayment] = useState<PaymentType>(initialPayment);
    const [searchPaymentCriteria, setSearchPaymentCriteria] = useState<PaymentSearchCriteriaType>({});
    const [searchCriteria, setSearchCriteria] = useState<PaymentSearchCriteriaType>({});
    const [criteria, setCriteria] = useState<PaymentCriteriaType>({});

    const paymentService = PaymentService();

    const fetchPayments = {
        load: async (pageNumber: number = 0) => {
            setLoading(true);
            try {
                const response = await paymentService.search(criteria, pageNumber, 20);
                setPayments(response.list.map((payment: PaymentType) => ({ ...payment, checked: false })));
                setPageNation(response);
            } finally {
                setLoading(false);
            }
        }
    };

    useEffect(() => {
        const mappedCriteria: PaymentCriteriaType = {
            ...searchPaymentCriteria,
            paymentCompletedFlag: searchPaymentCriteria.paymentCompletedFlag === "true" ? true :
                                   searchPaymentCriteria.paymentCompletedFlag === "false" ? false : undefined,
            paymentMethodType: searchPaymentCriteria.paymentMethodType ? Number(searchPaymentCriteria.paymentMethodType) : undefined
        };
        setCriteria(mappedCriteria);
    }, [searchPaymentCriteria]);

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
            initialPayment,
            newPayment,
            setNewPayment,
            payments,
            setPayments,
            searchPaymentCriteria,
            setSearchPaymentCriteria,
            searchCriteria,
            setSearchCriteria,
            fetchPayments,
            paymentService,
        }),
        [loading, message, setMessage, error, setError, pageNation, criteria, modalIsOpen, searchModalIsOpen, isEditing, editId, newPayment, payments, searchPaymentCriteria, searchCriteria, paymentService]
    );

    return <PaymentContext.Provider value={value}>{children}</PaymentContext.Provider>;
};

export const usePaymentContext = () => {
    const context = useContext(PaymentContext);
    if (context === undefined) {
        throw new Error("usePaymentContext must be used within a PaymentProvider");
    }
    return context;
};
