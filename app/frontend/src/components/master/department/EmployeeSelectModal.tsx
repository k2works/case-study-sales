import React from "react";
import Modal from "react-modal";
import {EmployeeType} from "../../../models";
import {EmployeeCollectionSelectView} from "../../../views/master/employee/EmployeeSelect.tsx";
import {useDepartmentContext} from "../../../providers/master/Department.tsx";
import {useEmployeeContext} from "../../../providers/master/Employee.tsx";

export const EmployeeSelectModal: React.FC = () => {
    const {
        setError,
        newDepartment,
        setNewDepartment
    } = useDepartmentContext();

    const {
        modalIsOpen: employeeModalIsOpen,
        setModalIsOpen: setEmployeeModalIsOpen,
        setEditId: setEmployeeEditId,
        employees,
        fetchEmployees,
        pageNation: employeePageNation,
    } = useEmployeeContext();

    const handleCloseEmployeeModal = () => {
        setError("");
        setEmployeeModalIsOpen(false);
        setEmployeeEditId(null);
    };

    const handleEmployeeCollectionSelect = (employee: EmployeeType) => {
        const newEmployees = newDepartment.employees.filter((e: EmployeeType) => e.empCode !== employee.empCode);
        if (employee.empCode) {
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
