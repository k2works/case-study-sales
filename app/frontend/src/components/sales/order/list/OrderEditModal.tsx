import React from "react";
import Modal from "react-modal";
import { OrderSingle } from "./OrderSingle.tsx";
import { useSalesOrderContext } from "../../../../providers/sales/Order.tsx";
import {DepartmentSelectModal} from "./DepartmentSelectModal.tsx";
import {CustomerSelectModal} from "./CustomerSelectModal.tsx";
import {EmployeeSelectModal} from "./EmployeeSelectModal.tsx";
import {ProductSelectModal} from "./ProductSelectModal.tsx";

export const OrderEditModal: React.FC = () => {
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
            <OrderSingle/>
            <DepartmentSelectModal type={"edit"}/>
            <CustomerSelectModal type={"edit"}/>
            <EmployeeSelectModal type={"edit"}/>
            <ProductSelectModal/>
        </Modal>
    )
}
