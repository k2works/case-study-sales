import React from "react";
import {showErrorMessage} from "../../application/utils.ts";
import {DepartmentSingleView} from "../../../views/master/department/DepartmentSingle.tsx";
import {EmployeeCollectionAddListView} from "../../../views/master/employee/EmployeeCollection.tsx";
import {EmployeeType} from "../../../models";
import {useDepartmentContext} from "../../../providers/master/Department.tsx";
import {useEmployeeContext} from "../../../providers/master/Employee.tsx";
import {EmployeeSelectModal} from "./EmployeeSelectModal.tsx";

export const DepartmentSingle: React.FC = () => {
    const {
        error,
        setError,
        message,
        setMessage,
        isEditing,
        newDepartment,
        setNewDepartment,
        initialDepartment,
        fetchDepartments,
        departmentService,
        setModalIsOpen,
        editId,
        setEditId
    } = useDepartmentContext();

    const {
        setModalIsOpen:setEmployeeModalIsOpen,
        setIsEditing:setEmployeeIsEditing
    } = useEmployeeContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    const handleOpenEmployeeModal = () => {
        setMessage("");
        setError("");
        setEmployeeIsEditing(true);
        setEmployeeModalIsOpen(true);
    };

    const handleDeleteEmployee = (employee: EmployeeType) => {
        const newEmployees = newDepartment.employees.filter((e: EmployeeType) => e.empCode.value !== employee.empCode.value);
        if (employee.empCode.value) {
            newEmployees.push({
                ...employee,
                addFlag: false,
                deleteFlag: true
            });
        }
        setNewDepartment({
            ...newDepartment,
            employees: newEmployees
        });
    }

    const handleCreateOrUpdateDepartment = async () => {
        const validateDepartment = (): boolean => {
            if (!newDepartment.departmentCode.trim() || !newDepartment.departmentName.trim()) {
                setError("部門コード、部門名は必須項目です。");
                return false;
            }
            return true;
        };

        if (!validateDepartment()) {
            return;
        }

        try {
            if (isEditing && editId) {
                await departmentService.update(newDepartment);
            } else {
                await departmentService.create(newDepartment);
            }
            setNewDepartment(initialDepartment);
            await fetchDepartments.load();
            if (isEditing) {
                setMessage("部門を更新しました。");
            } else {
                setMessage("部門を作成しました。");
            }
            handleCloseModal();
        } catch (error: any) {
            showErrorMessage(`部門の作成に失敗しました: ${error?.message}`, setError);
        }
    };

    return (
        <>
            <DepartmentSingleView
                error={error}
                message={message}
                isEditing={isEditing}
                headerItems={{handleCreateOrUpdateDepartment, handleCloseModal}}
                formItems={{newDepartment, setNewDepartment}}
            />

            <EmployeeSelectModal/>

            {isEditing && (
                <EmployeeCollectionAddListView
                    employees={newDepartment.employees.filter((e: EmployeeType) => !e.deleteFlag)}
                    handleAdd={handleOpenEmployeeModal}
                    handleDelete={handleDeleteEmployee}
                />
            )
            }
        </>
    );
}
