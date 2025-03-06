import React from "react";
import Modal from "react-modal";
import { SalesOrderSingle } from "./SalesOrderSingle.tsx";
import { useSalesOrderContext } from "../../../../providers/sales/SalesOrder.tsx";
import {DepartmentSelectModal} from "./DepartmentSelectModal.tsx";
import {CustomerSelectModal} from "./CustomerSelectModal.tsx";
import {EmployeeSelectModal} from "./EmployeeSelectModal.tsx";
import {ProductSelectModal} from "./ProductSelectModal.tsx";

export const SalesOrderEditModal: React.FC = () => {
    const {
        modalIsOpen,
        setModalIsOpen,
        setEditId,
        setError
    } = useSalesOrderContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    return (
        <Modal
            isOpen={modalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="受注情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <SalesOrderSingle/>
            <DepartmentSelectModal type={"edit"}/>
            <CustomerSelectModal type={"edit"}/>
            <EmployeeSelectModal type={"edit"}/>
            <ProductSelectModal/>
        </Modal>
    )
}
