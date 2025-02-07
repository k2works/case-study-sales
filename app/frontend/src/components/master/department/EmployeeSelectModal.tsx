import React from "react";
import Modal from "react-modal";
import {EmployeeType} from "../../../models";
import {EmployeeCollectionSelectView} from "../../../views/master/employee/EmployeeSelect.tsx";
import {useDepartmentContext} from "../../../providers/Department.tsx";
import {useEmployeeContext} from "../../../providers/Employee.tsx";

export const EmployeeSelectModal: React.FC = () => {
    const {
        setMessage,
        setError,
        newDepartment,
        setNewDepartment
    } = useDepartmentContext();

    const {
        modalIsOpen: employeeModalIsOpen,
        setModalIsOpen: setEmployeeModalIsOpen,
        setIsEditing: setEmployeeIsEditing,
        setEditId: setEmployeeEditId,
        employees,
        fetchEmployees,
        pageNation: employeePageNation,
    } = useEmployeeContext();

    const handleOpenEmployeeModal = () => {
        setMessage("");
        setError("");
        setEmployeeIsEditing(true);
        setEmployeeModalIsOpen(true);
    };

    const handleCloseEmployeeModal = () => {
        setError("");
        setEmployeeModalIsOpen(false);
        setEmployeeEditId(null);
    };

    const handleEmployeeCollectionSelect = (employee: EmployeeType) => {
        const newEmployees = newDepartment.employees.filter((e: EmployeeType) => e.empCode.value !== employee.empCode.value);
        if (employee.empCode.value) {
            newEmployees.push({
                ...employee,
                addFlag: true,
                deleteFlag: false
            });
        }
        setNewDepartment({
            ...newDepartment,
            employees: newEmployees
        });
    }

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

    return (
        <Modal
            isOpen={employeeModalIsOpen}
            onRequestClose={handleCloseEmployeeModal}
            contentLabel="社員情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            {
                <EmployeeCollectionSelectView
                    employees={employees}
                    handleSelect={handleEmployeeCollectionSelect}
                    handleClose={handleCloseEmployeeModal}
                    pageNation={employeePageNation}
                    fetchEmployees={fetchEmployees.load}
                />
            }
        </Modal>
    )
}