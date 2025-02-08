import React from "react";
import {useEmployeeContext} from "../../../providers/master/Employee.tsx";
import {showErrorMessage} from "../../application/utils.ts";
import {EmployeeSingleView} from "../../../views/master/employee/EmployeeSingle.tsx";
import {DepartmentSelectView} from "../../../views/master/department/DepartmentSelect.tsx";
import {UserSelectView} from "../../../views/system/user/UserSelect.tsx";
import {useUserContext} from "../../../providers/system/User.tsx";
import {useDepartmentContext} from "../../../providers/master/Department.tsx";

export const EmployeeSingle: React.FC = () => {
    const {
        message,
        setMessage,
        error,
        setError,
        setModalIsOpen,
        isEditing,
        editId,
        setEditId,
        initialEmployee,
        newEmployee,
        setNewEmployee,
        employeeService,
        fetchEmployees,
    } = useEmployeeContext();

    const {
        setModalIsOpen: setDepartmentModalIsOpen,
    } = useDepartmentContext();

    const {
        setModalIsOpen: setUserModalIsOpen,
    } = useUserContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    const handleCreateOrUpdateEmployee = async () => {
        const validateEmployee = (): boolean => {
            if (!newEmployee.empCode.value.trim() || !newEmployee.empName.firstName.trim() || !newEmployee.empName.lastName.trim()) {
                setError("社員コード、姓、名は必須項目です。");
                return false;
            }
            if (!newEmployee.department) {
                setError("部門は必須項目です。");
                return false;
            }
            return true;
        };

        if (!validateEmployee()) return;

        try {
            if (isEditing && editId) {
                await employeeService.update(newEmployee);
            } else {
                await employeeService.create(newEmployee);
            }
            setNewEmployee({...initialEmployee});
            await fetchEmployees.load();
            if (isEditing) {
                setMessage("社員を更新しました。");
            } else {
                setMessage("社員を作成しました。");
            }
            handleCloseModal();
        } catch (error: any) {
            showErrorMessage(`社員の作成に失敗しました: ${error?.message}`, setError);
        }
    };

    return (
        <>
            <EmployeeSingleView
                error={error}
                message={message}
                newEmployee={newEmployee}
                setNewEmployee={setNewEmployee}
                isEditing={isEditing}
                handleCreateOrUpdateEmployee={handleCreateOrUpdateEmployee}
                handleCloseModal={handleCloseModal}
            />

            <DepartmentSelectView
                handleSelect={() => setDepartmentModalIsOpen(true)}
            />

            <UserSelectView
                handleSelect={() => setUserModalIsOpen(true)}
            />
        </>
    )
}
