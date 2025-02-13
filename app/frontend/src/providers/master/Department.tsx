import React, {createContext, useContext, ReactNode, Dispatch, SetStateAction, useState, useMemo} from "react";
import {PageNationType, usePageNation} from "../../views/application/PageNation.tsx";
import {DepartmentCriteriaType, DepartmentType} from "../../models";
import { useModal } from "../../components/application/hooks.ts";
import { useDepartment, useFetchDepartments } from "../../components/master/department/hooks";
import { showErrorMessage } from "../../components/application/utils.ts";
import { useMessage } from "../../components/application/Message.tsx";
import {DepartmentServiceType} from "../../services/master/department.ts";

type DepartmentContextType = {
    loading: boolean;
    setLoading: Dispatch<SetStateAction<boolean>>;
    message: string | null;
    setMessage: Dispatch<SetStateAction<string | null>>;
    error: string | null;
    setError: Dispatch<SetStateAction<string | null>>;
    pageNation: PageNationType | null;
    setPageNation: Dispatch<SetStateAction<PageNationType | null>>;
    criteria: DepartmentCriteriaType | null;
    setCriteria: Dispatch<SetStateAction<DepartmentCriteriaType | null>>;
    searchModalIsOpen: boolean;
    setSearchModalIsOpen: Dispatch<SetStateAction<boolean>>;
    modalIsOpen: boolean;
    setModalIsOpen: Dispatch<SetStateAction<boolean>>;
    isEditing: boolean;
    setIsEditing: Dispatch<SetStateAction<boolean>>;
    editId: string | null;
    setEditId: Dispatch<SetStateAction<string | null>>;
    initialDepartment: DepartmentType;
    departments: DepartmentType[];
    setDepartments: Dispatch<SetStateAction<DepartmentType[]>>;
    newDepartment: DepartmentType;
    setNewDepartment: Dispatch<SetStateAction<DepartmentType>>;
    searchDepartmentCriteria: DepartmentCriteriaType;
    setSearchDepartmentCriteria: Dispatch<SetStateAction<DepartmentCriteriaType>>;
    fetchDepartments: { load: (page?: number, criteria?: DepartmentCriteriaType) => Promise<void> };
    departmentService: DepartmentServiceType;
};

const DepartmentContext = createContext<DepartmentContextType | undefined>(undefined);

export const useDepartmentContext = () => {
    const context = useContext(DepartmentContext);
    if (!context) {
        throw new Error("useDepartmentContext must be used within a DepartmentProvider");
    }
    return context;
};

type Props = {
    children: ReactNode;
};

export const DepartmentProvider: React.FC<Props> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);
    const { message, setMessage, error, setError } = useMessage();
    const { pageNation, setPageNation, criteria, setCriteria } = usePageNation<DepartmentCriteriaType | null>();
    const { modalIsOpen: searchModalIsOpen, setModalIsOpen: setSearchModalIsOpen } = useModal();

    const { modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId } = useModal();
    const {
        initialDepartment,
        departments,
        setDepartments,
        newDepartment,
        setNewDepartment,
        searchDepartmentCriteria,
        setSearchDepartmentCriteria,
        departmentService
    } = useDepartment();
    const fetchDepartments = useFetchDepartments(
        setLoading,
        setDepartments,
        setPageNation,
        setError,
        showErrorMessage,
        departmentService
    );
    const defaultCriteria: DepartmentCriteriaType = {};

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
        initialDepartment,
        departments,
        setDepartments,
        newDepartment,
        setNewDepartment,
        searchDepartmentCriteria,
        setSearchDepartmentCriteria,
        fetchDepartments,
        departmentService
    }), [criteria, defaultCriteria, departmentService, departments, editId, error, fetchDepartments, initialDepartment, isEditing, loading, message, modalIsOpen, newDepartment, pageNation, searchDepartmentCriteria, searchModalIsOpen, setCriteria, setDepartments, setEditId, setError, setIsEditing, setMessage, setModalIsOpen, setNewDepartment, setPageNation, setSearchDepartmentCriteria, setSearchModalIsOpen])

    return (
        <DepartmentContext.Provider value={value}>
            {children}
        </DepartmentContext.Provider>
    );
};