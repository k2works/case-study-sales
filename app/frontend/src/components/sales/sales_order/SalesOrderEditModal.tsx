import React from "react";
import Modal from "react-modal";
import { SalesOrderSingle } from "./SalesOrderSingle.tsx";
import { useSalesOrderContext } from "../../../providers/sales/SalesOrder.tsx";

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
        </Modal>
    )
}