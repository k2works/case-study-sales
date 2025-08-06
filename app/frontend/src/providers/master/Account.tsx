import React, {createContext, useContext, ReactNode, Dispatch, SetStateAction, useState, useMemo} from "react";
import {PageNationType, usePageNation} from "../../views/application/PageNation.tsx";
import {AccountCriteriaType, AccountType} from "../../models/master/account.ts";
import { useModal } from "../../components/application/hooks.ts";
import { useAccount, useFetchAccounts } from "../../components/master/account/hooks";
import { showErrorMessage } from "../../components/application/utils.ts";
import { useMessage } from "../../components/application/Message.tsx";
import {AccountServiceType} from "../../services/master/account.ts";

type AccountContextType = {
    loading: boolean;
    setLoading: Dispatch<SetStateAction<boolean>>;
    message: string | null;
    setMessage: Dispatch<SetStateAction<string | null>>;
    error: string | null;
    setError: Dispatch<SetStateAction<string | null>>;
    pageNation: PageNationType | null;
    setPageNation: Dispatch<SetStateAction<PageNationType | null>>;
    criteria: AccountCriteriaType | null;
    setCriteria: Dispatch<SetStateAction<AccountCriteriaType | null>>;
    searchModalIsOpen: boolean;
    setSearchModalIsOpen: Dispatch<SetStateAction<boolean>>;
    modalIsOpen: boolean;
    setModalIsOpen: Dispatch<SetStateAction<boolean>>;
    isEditing: boolean;
    setIsEditing: Dispatch<SetStateAction<boolean>>;
    editId: string | null;
    setEditId: Dispatch<SetStateAction<string | null>>;
    initialAccount: AccountType;
    accounts: AccountType[];
    setAccounts: Dispatch<SetStateAction<AccountType[]>>;
    newAccount: AccountType;
    setNewAccount: Dispatch<SetStateAction<AccountType>>;
    searchAccountCriteria: AccountCriteriaType;
    setSearchAccountCriteria: Dispatch<SetStateAction<AccountCriteriaType>>;
    fetchAccounts: { load: (page?: number, criteria?: AccountCriteriaType) => Promise<void> };
    accountService: AccountServiceType;
};

const AccountContext = createContext<AccountContextType | undefined>(undefined);

export const useAccountContext = () => {
    const context = useContext(AccountContext);
    if (!context) {
        throw new Error("useAccountContext must be used within an AccountProvider");
    }
    return context;
};

type Props = {
    children: ReactNode;
};

export const AccountProvider: React.FC<Props> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);
    const { message, setMessage, error, setError } = useMessage();
    const { pageNation, setPageNation, criteria, setCriteria } = usePageNation<AccountCriteriaType | null>();
    const { modalIsOpen: searchModalIsOpen, setModalIsOpen: setSearchModalIsOpen } = useModal();

    const { modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId } = useModal();
    const {
        initialAccount,
        accounts,
        setAccounts,
        newAccount,
        setNewAccount,
        searchAccountCriteria,
        setSearchAccountCriteria,
        accountService
    } = useAccount();
    const fetchAccounts = useFetchAccounts(
        setLoading,
        setAccounts,
        setPageNation,
        setError,
        showErrorMessage,
        accountService
    );
    const defaultCriteria: AccountCriteriaType = {};

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
        initialAccount,
        accounts,
        setAccounts,
        newAccount,
        setNewAccount,
        searchAccountCriteria,
        setSearchAccountCriteria,
        fetchAccounts,
        accountService
    }), [criteria, defaultCriteria, accountService, accounts, editId, error, fetchAccounts, initialAccount, isEditing, loading, message, modalIsOpen, newAccount, pageNation, searchAccountCriteria, searchModalIsOpen, setCriteria, setAccounts, setEditId, setError, setIsEditing, setMessage, setModalIsOpen, setNewAccount, setPageNation, setSearchAccountCriteria, setSearchModalIsOpen])

    return (
        <AccountContext.Provider value={value}>
            {children}
        </AccountContext.Provider>
    );
};