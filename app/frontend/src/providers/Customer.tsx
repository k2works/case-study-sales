import React, { createContext, useContext, ReactNode, useState, useMemo, Dispatch, SetStateAction } from "react";
import { PageNationType, usePageNation } from "../views/application/PageNation.tsx";
import { useModal } from "../components/application/hooks.ts";
import { showErrorMessage } from "../components/application/utils.ts";
import { useMessage } from "../components/application/Message.tsx";
import { CustomerServiceType } from "../services/master/customer.ts";
import {CustomerCriteriaType, CustomerType, ShippingType} from "../models/master/partner";
import {useCustomer, useFetchCustomers} from "../components/master/partner/hooks";

type CustomerContextType = {
    loading: boolean;
    setLoading: Dispatch<SetStateAction<boolean>>;
    message: string | null;
    setMessage: Dispatch<SetStateAction<string | null>>;
    error: string | null;
    setError: Dispatch<SetStateAction<string | null>>;
    pageNation: PageNationType | null;
    setPageNation: Dispatch<SetStateAction<PageNationType | null>>;
    criteria: CustomerCriteriaType | null;
    setCriteria: Dispatch<SetStateAction<CustomerCriteriaType | null>>;
    searchModalIsOpen: boolean;
    setSearchModalIsOpen: Dispatch<SetStateAction<boolean>>;
    modalIsOpen: boolean;
    setModalIsOpen: Dispatch<SetStateAction<boolean>>;
    isEditing: boolean;
    setIsEditing: Dispatch<SetStateAction<boolean>>;
    editId: string | null;
    setEditId: Dispatch<SetStateAction<string | null>>;
    initialCustomer: CustomerType;
    customers: CustomerType[];
    setCustomers: Dispatch<SetStateAction<CustomerType[]>>;
    newCustomer: CustomerType;
    setNewCustomer: Dispatch<SetStateAction<CustomerType>>;
    searchCustomerCriteria: CustomerCriteriaType;
    setSearchCustomerCriteria: Dispatch<SetStateAction<CustomerCriteriaType>>;
    newShipping: ShippingType;
    setNewShipping: Dispatch<SetStateAction<ShippingType>>;
    fetchCustomers: { load: (page?: number, criteria?: CustomerCriteriaType) => Promise<void> };
    customerService: CustomerServiceType;
};

const CustomerContext = createContext<CustomerContextType | undefined>(undefined);

export const useCustomerContext = () => {
    const context = useContext(CustomerContext);
    if (!context) {
        throw new Error("useCustomerContext must be used within a CustomerProvider");
    }
    return context;
};

type Props = {
    children: ReactNode;
};

export const CustomerProvider: React.FC<Props> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);
    const { message, setMessage, error, setError } = useMessage();
    const { pageNation, setPageNation, criteria, setCriteria } = usePageNation<CustomerCriteriaType>();
    const { modalIsOpen: searchModalIsOpen, setModalIsOpen: setSearchModalIsOpen } = useModal();
    const { modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId } = useModal();
    const {
        initialCustomer,
        customers,
        setCustomers,
        newCustomer,
        setNewCustomer,
        searchCustomerCriteria,
        setSearchCustomerCriteria,
        customerService,
        newShipping,
        setNewShipping
    } = useCustomer();
    const fetchCustomers = useFetchCustomers(
        setLoading,
        setCustomers,
        setPageNation,
        setError,
        showErrorMessage,
        customerService
    );

    const defaultCriteria: CustomerCriteriaType = {}; // 必要であれば具体的なデフォルト値を設定してください。

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
        initialCustomer,
        customers,
        setCustomers,
        newCustomer,
        setNewCustomer,
        searchCustomerCriteria,
        setSearchCustomerCriteria,
        newShipping,
        setNewShipping,
        fetchCustomers,
        customerService
    }), [
        loading,
        setLoading,
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
        initialCustomer,
        customers,
        setCustomers,
        newCustomer,
        setNewCustomer,
        searchCustomerCriteria,
        setSearchCustomerCriteria,
        newShipping,
        setNewShipping,
        fetchCustomers,
        customerService
    ]);

    return (
        <CustomerContext.Provider value={value}>
            {children}
        </CustomerContext.Provider>
    );
};