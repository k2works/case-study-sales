import React from "react";
import Modal from "react-modal";
import { SalesSingle } from "./SalesSingle.tsx";
import { useSalesContext } from "../../../../providers/sales/Sales.tsx";

export const SalesEditModal: React.FC = () => {
    const {
        modalIsOpen,
        setModalIsOpen,
        setEditId,
        setError
    } = useSalesContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    return (
        <Modal
            isOpen={modalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="売上情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <SalesSingle/>
        </Modal>
    )
}
