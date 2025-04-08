import React from "react";
import Modal from "react-modal";
import { ShippingSingle } from "./ShippingSingle.tsx";
import { useShippingContext } from "../../../../providers/sales/Shipping.tsx";

export const ShippingEditModal: React.FC = () => {
    const {
        modalIsOpen,
        setModalIsOpen,
        setEditId,
        setError
    } = useShippingContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    return (
        <Modal
            isOpen={modalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="出荷情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <ShippingSingle/>
        </Modal>
    )
}