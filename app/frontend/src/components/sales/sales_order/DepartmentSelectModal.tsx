import React from "react";
import Modal from "react-modal";
import {DepartmentCollectionSelectView} from "../../../views/master/department/DepartmentSelect.tsx";
import {useDepartmentContext} from "../../../providers/master/Department.tsx";
import {useSalesOrderContext} from "../../../providers/sales/SalesOrder.tsx";

export const DepartmentSelectModal: React.FC = () => {
    const {
        newSalesOrder,
        setNewSalesOrder,
    } = useSalesOrderContext();

    const {
        pageNation: departmentPageNation,
        modalIsOpen: departmentModalIsOpen,
        setModalIsOpen: setDepartmentModalIsOpen,
        departments,
        fetchDepartments,
    } = useDepartmentContext();

    return (
        <Modal
            isOpen={departmentModalIsOpen}
            onRequestClose={() => setDepartmentModalIsOpen(false)}
            contentLabel="部門情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            {
                <DepartmentCollectionSelectView
                    departments={departments}
                    handleSelect={(department) => {
                        setNewSalesOrder({
                            ...newSalesOrder,
                            departmentCode: department.departmentId.deptCode.value,
                            departmentStartDate: department.departmentId.departmentStartDate.value,
                        });
                        setDepartmentModalIsOpen(false);
                    }}
                    handleClose={() => setDepartmentModalIsOpen(false)}
                    pageNation={departmentPageNation}
                    fetchDepartments={fetchDepartments.load}
                />
            }
        </Modal>
    )
}
