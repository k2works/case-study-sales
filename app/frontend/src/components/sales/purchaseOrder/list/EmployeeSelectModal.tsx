import React from "react";
import Modal from "react-modal";
import {useEmployeeContext} from "../../../../providers/master/Employee.tsx";
import {usePurchaseOrderContext} from "../../../../providers/sales/PurchaseOrder.tsx";

interface Props {
    type: "edit" | "search";
}

export const EmployeeSelectModal: React.FC<Props> = ({ type }) => {
    const {
        employees,
        modalIsOpen,
        setModalIsOpen,
        fetchEmployees
    } = useEmployeeContext();

    const {
        newPurchaseOrder,
        setNewPurchaseOrder,
        searchCriteria,
        setSearchCriteria
    } = usePurchaseOrderContext();

    React.useEffect(() => {
        if (modalIsOpen && employees.length === 0) {
            fetchEmployees.load();
        }
    }, [modalIsOpen]);

    const handleEmployeeSelect = (employeeCode: string) => {
        if (type === "edit") {
            setNewPurchaseOrder({
                ...newPurchaseOrder,
                purchaseOrderManagerCode: employeeCode
            });
        } else {
            setSearchCriteria({
                ...searchCriteria,
                purchaseOrderManagerCode: employeeCode
            });
        }
        setModalIsOpen(false);
    };

    const handleCloseModal = () => {
        setModalIsOpen(false);
    };

    return (
        <Modal
            isOpen={modalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="社員を選択"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <div className="single-view-object-container">
                <div className="single-view-header">
                    <div className="single-view-header-title">
                        <h2>社員選択</h2>
                        <button onClick={handleCloseModal} className="close-button">
                            ✕
                        </button>
                    </div>
                </div>
                <div className="single-view-container">
                    <div className="select-modal-content">
                        <div className="select-modal-list">
                            {employees.map((employee) => (
                                <div
                                    key={employee.empCode}
                                    className="select-modal-item"
                                    onClick={() => handleEmployeeSelect(employee.empCode)}
                                >
                                    <span className="select-modal-item-code">
                                        {employee.empCode}
                                    </span>
                                    <span className="select-modal-item-name">
                                        {employee.empFirstName} {employee.empLastName}
                                    </span>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
            </div>
        </Modal>
    );
};