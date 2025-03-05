import React from "react";
import Modal from "react-modal";
import { EmployeeCollectionSelectView } from "../../../../views/master/employee/EmployeeSelect.tsx";
import { useEmployeeContext } from "../../../../providers/master/Employee.tsx";
import {useSalesOrderContext} from "../../../../providers/sales/SalesOrder.tsx";

export const EmployeeSelectModal: React.FC = () => {
    const {
        newSalesOrder,
        setNewSalesOrder,
    } = useSalesOrderContext();
    
    const {
        pageNation: employeePageNation,
        modalIsOpen: employeeModalIsOpen,
        setModalIsOpen: setEmployeeModalIsOpen,
        employees,
        fetchEmployees,
    } = useEmployeeContext();

    return (
        <Modal
            isOpen={employeeModalIsOpen}
            onRequestClose={() => setEmployeeModalIsOpen(false)}
            contentLabel="従業員情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            {
                <EmployeeCollectionSelectView
                    employees={employees}
                    handleSelect={(employee) => {
                        setNewSalesOrder({
                            ...newSalesOrder,
                            employeeCode: employee.empCode.value
                        });
                        setEmployeeModalIsOpen(false);
                    }}
                    handleClose={() => setEmployeeModalIsOpen(false)}
                    pageNation={employeePageNation}
                    fetchEmployees={fetchEmployees.load}
                />
            }
        </Modal>
    );
};