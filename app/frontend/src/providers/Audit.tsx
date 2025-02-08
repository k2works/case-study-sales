import React, {
    createContext,
    useContext,
    ReactNode,
    useState,
    useMemo,
    Dispatch,
    SetStateAction
} from "react";
import { PageNationType, usePageNation } from "../views/application/PageNation.tsx";
import { useModal } from "../components/application/hooks.ts";
import { showErrorMessage } from "../components/application/utils.ts";
import { useMessage } from "../components/application/Message.tsx";
import {AuditCriteriaType, AuditType} from "../models/system/audit.ts";
import {AuditServiceType} from "../services/system/audit.ts";
import {useAudit, useFetchAudits} from "../components/system/audit/hooks";

// AuditContext の型を定義
type AuditContextType = {
    loading: boolean;
    setLoading: Dispatch<SetStateAction<boolean>>;
    message: string | null;
    setMessage: Dispatch<SetStateAction<string | null>>;
    error: string | null;
    setError: Dispatch<SetStateAction<string | null>>;
    pageNation: PageNationType | null;
    setPageNation: Dispatch<SetStateAction<PageNationType | null>>;
    criteria: AuditCriteriaType | null;
    setCriteria: Dispatch<SetStateAction<AuditCriteriaType | null>>;
    searchModalIsOpen: boolean;
    setSearchModalIsOpen: Dispatch<SetStateAction<boolean>>;
    modalIsOpen: boolean;
    setModalIsOpen: Dispatch<SetStateAction<boolean>>;
    isEditing: boolean;
    setIsEditing: Dispatch<SetStateAction<boolean>>;
    editId: string | null;
    setEditId: Dispatch<SetStateAction<string | null>>;
    initialAudit: AuditType;
    audits: AuditType[];
    setAudits: Dispatch<SetStateAction<AuditType[]>>;
    newAudit: AuditType;
    setNewAudit: Dispatch<SetStateAction<AuditType>>;
    searchAuditCriteria: AuditCriteriaType;
    setSearchAuditCriteria: Dispatch<SetStateAction<AuditCriteriaType>>;
    fetchAudits: { load: (page?: number, criteria?: AuditCriteriaType) => Promise<void> };
    auditService: AuditServiceType;
};

// Context を初期化
const AuditContext = createContext<AuditContextType | undefined>(undefined);

// useAuditContext フックを作成
export const useAuditContext = () => {
    const context = useContext(AuditContext);
    if (!context) {
        throw new Error("useAuditContext must be used within an AuditProvider");
    }
    return context;
};

// Provider の Props を定義
type Props = {
    children: ReactNode;
};

// AuditProvider コンポーネントの作成
export const AuditProvider: React.FC<Props> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);
    const { message, setMessage, error, setError } = useMessage();
    const { pageNation, setPageNation, criteria, setCriteria } = usePageNation<AuditCriteriaType>();
    const { modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId } = useModal();
    const {
        initialAudit,
        audits,
        setAudits,
        newAudit,
        setNewAudit,
        searchAuditCriteria,
        setSearchAuditCriteria,
        auditService
    } = useAudit();
    const fetchAudits = useFetchAudits(
        setLoading,
        setAudits,
        setPageNation,
        setError,
        showErrorMessage,
        auditService
    );
    const { modalIsOpen: searchModalIsOpen, setModalIsOpen: setSearchModalIsOpen } = useModal();
    const defaultCriteria: AuditCriteriaType = {}; // 必要に応じてデフォルト値を設定
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
            initialAudit,
            audits,
            setAudits,
            newAudit,
            setNewAudit,
            searchAuditCriteria,
            setSearchAuditCriteria,
            fetchAudits,
            auditService,
        }),
        [
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
            initialAudit,
            audits,
            setAudits,
            newAudit,
            setNewAudit,
            searchAuditCriteria,
            setSearchAuditCriteria,
            fetchAudits,
            auditService,
        ]
    );
    return <AuditContext.Provider value={value}>{children}</AuditContext.Provider>;
};