import React, { createContext, useContext, ReactNode, useState, useMemo, Dispatch, SetStateAction } from "react";
import { PageNationType, usePageNation } from "../views/application/PageNation.tsx";
import { EmployeeCriteriaType, EmployeeType } from "../models";
import { useModal } from "../components/application/hooks.ts";
import { useEmployee, useFetchEmployees } from "../components/master/employee/hooks"; // 必要に応じてパスを修正
import { showErrorMessage } from "../components/application/utils.ts";
import { useMessage } from "../components/application/Message.tsx";
import { EmployeeServiceType } from "../services/master/employee.ts";

type EmployeeContextType = {
    loading: boolean;
    setLoading: Dispatch<SetStateAction<boolean>>;
    message: string | null;
    setMessage: Dispatch<SetStateAction<string | null>>;
    error: string | null;
    setError: Dispatch<SetStateAction<string | null>>;
    pageNation: PageNationType | null;
    setPageNation: Dispatch<SetStateAction<PageNationType | null>>;
    criteria: EmployeeCriteriaType | null;
    setCriteria: Dispatch<SetStateAction<EmployeeCriteriaType | null>>;
    searchModalIsOpen: boolean;
    setSearchModalIsOpen: Dispatch<SetStateAction<boolean>>;
    modalIsOpen: boolean;
    setModalIsOpen: Dispatch<SetStateAction<boolean>>;
    isEditing: boolean;
    setIsEditing: Dispatch<SetStateAction<boolean>>;
    editId: string | null;
    setEditId: Dispatch<SetStateAction<string | null>>;
    initialEmployee: EmployeeType;
    employees: EmployeeType[];
    setEmployees: Dispatch<SetStateAction<EmployeeType[]>>;
    newEmployee: EmployeeType;
    setNewEmployee: Dispatch<SetStateAction<EmployeeType>>;
    searchEmployeeCriteria: EmployeeCriteriaType;
    setSearchEmployeeCriteria: Dispatch<SetStateAction<EmployeeCriteriaType>>;
    fetchEmployees: { load: (page?: number, criteria?: EmployeeCriteriaType) => Promise<void> };
    employeeService: EmployeeServiceType;
};

const EmployeeContext = createContext<EmployeeContextType | undefined>(undefined);

export const useEmployeeContext = () => {
    const context = useContext(EmployeeContext);
    if (!context) {
        throw new Error("useEmployeeContext must be used within an EmployeeProvider");
    }
    return context;
};

type Props = {
    children: ReactNode;
};

export const EmployeeProvider: React.FC<Props> = ({ children }) => {
    const [loading, setLoading] = useState<boolean>(false);
    const { message, setMessage, error, setError } = useMessage();
    const { pageNation, setPageNation, criteria, setCriteria } = usePageNation<EmployeeCriteriaType | null>();
    const { modalIsOpen: searchModalIsOpen, setModalIsOpen: setSearchModalIsOpen } = useModal();
    const { modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId } = useModal();
    const {
        initialEmployee,
        employees,
        setEmployees,
        newEmployee,
        setNewEmployee,
        searchEmployeeCriteria,
        setSearchEmployeeCriteria,
        employeeService
    } = useEmployee();
    const fetchEmployees = useFetchEmployees(
        setLoading,
        setEmployees,
        setPageNation,
        setError,
        showErrorMessage,
        employeeService
    );
    const defaultCriteria: EmployeeCriteriaType = {};
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
        initialEmployee,
        employees,
        setEmployees,
        newEmployee,
        setNewEmployee,
        searchEmployeeCriteria,
        setSearchEmployeeCriteria,
        fetchEmployees,
        employeeService
    }), [loading, message, setMessage, error, setError, pageNation, setPageNation, criteria, defaultCriteria, setCriteria, searchModalIsOpen, setSearchModalIsOpen, modalIsOpen, setModalIsOpen, isEditing, setIsEditing, editId, setEditId, initialEmployee, employees, setEmployees, newEmployee, setNewEmployee, searchEmployeeCriteria, setSearchEmployeeCriteria, fetchEmployees, employeeService]);
    return (
        <EmployeeContext.Provider value={value}>
            {children}
        </EmployeeContext.Provider>
    );
};