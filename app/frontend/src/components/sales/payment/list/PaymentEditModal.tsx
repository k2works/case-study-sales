import React from "react";
import Modal from "react-modal";
import { PaymentSingle } from "./PaymentSingle.tsx";
import { usePaymentContext } from "../../../../providers/sales/Payment.tsx";

export const PaymentEditModal: React.FC = () => {
    const {
        modalIsOpen,
        setModalIsOpen,
        setEditId,
        setError
    } = usePaymentContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    return (
        <Modal
            isOpen={modalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="入金情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <PaymentSingle/>
        </Modal>
    )
}