import React from "react";
import {useEmployeeContext} from "../../../providers/master/Employee.tsx";
import {showErrorMessage} from "../../application/utils.ts";
import {EmployeeType} from "../../../models";
import {EmployeeCollectionView} from "../../../views/master/employee/EmployeeCollection.tsx";
import {EmployeeSearchModal} from "./EmployeeSearchModal.tsx";
import {EmployeeEditModal} from "./EmployeeEditModal.tsx";

export const EmployeeCollection: React.FC = () => {
    const {
        message,
        setMessage,
        error,
        setError,
        pageNation,
        criteria,
        setSearchModalIsOpen,
        setModalIsOpen,
        setIsEditing,
        setEditId,
        initialEmployee,
        employees,
        setEmployees,
        setNewEmployee,
        searchEmployeeCriteria,
        setSearchEmployeeCriteria,
        employeeService,
        fetchEmployees,
    } = useEmployeeContext();

    const handleOpenModal = (employee?: EmployeeType) => {
        setMessage("");
        setError("");
        if (employee) {
            employee.loginPassword = "";
            setNewEmployee(employee);
            setEditId(employee.empCode);
            setIsEditing(true);
        } else {
            setNewEmployee(initialEmployee);
            setIsEditing(false);
        }
        setModalIsOpen(true);
    };

    const handleOpenSearchModal = () => {
        setSearchModalIsOpen(true);
    }

    const handleDeleteEmployee = async (empCode: string) => {
        try {
            if (!window.confirm(`社員コード:${empCode} を削除しますか？`)) return;
            await employeeService.destroy(empCode);
            await fetchEmployees.load();
            setMessage("社員を削除しました。");
        } catch (error: unknown) {
            showErrorMessage(error, setError, "社員の削除に失敗しました");
        }
    };

    const handleCheckEmployee = (employee: EmployeeType) => {
        const newEmployee = employees.map((emp) => {
            if (emp.empCode === employee.empCode) {
                return {
                    ...emp,
                    checked: !emp.checked
                };
            }
            return emp;
        });
        setEmployees(newEmployee);
    }

    const handleCheckAllEmployees = () => {
        const newEmployee = employees.map((emp) => {
            return {
                ...emp,
                checked: !employees.every((emp) => emp.checked)
            };
        });
        setEmployees(newEmployee);
    }

    const handleDeleteCheckedEmployees = async () => {
        if (!employees.some((emp) => emp.checked)) {
            setError("削除する社員を選択してください。");
            return;
        }

        try {
            if (!window.confirm(`選択した社員を削除しますか？`)) return;
            const checkedEmployees = employees.filter((emp) => emp.checked);
            await Promise.all(checkedEmployees.map((emp) => employeeService.destroy(emp.empCode)));
            await fetchEmployees.load();
            setMessage("選択した社員を削除しました。");
        } catch (error: unknown) {
            showErrorMessage(error, setError, "社員の削除に失敗しました");
        }
    }

    return (
        <>
            <EmployeeSearchModal/>
            <EmployeeEditModal/>
            <EmployeeCollectionView
                error={error}
                message={message}
                searchItems={{searchEmployeeCriteria, setSearchEmployeeCriteria, handleOpenSearchModal}}
                headerItems={{handleOpenModal, handleCheckToggleCollection:handleCheckAllEmployees, handleDeleteCheckedCollection:handleDeleteCheckedEmployees}}
                collectionItems={{employees, handleDeleteEmployee, handleCheckEmployee}}
                pageNationItems={{pageNation, fetchEmployees:fetchEmployees.load, criteria}}
            />
        </>
    )
}
