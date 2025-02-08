import React from "react";
import {useCustomerContext} from "../../../../providers/Customer.tsx";
import Modal from "react-modal";
import {CustomerSingle} from "./CustomerSingle.tsx";

export const CustomerEditModal: React.FC = () => {
    const {
        setError,
        modalIsOpen,
        setModalIsOpen,
        setEditId,
    } = useCustomerContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    return (
        <Modal
            isOpen={modalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="顧客情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <CustomerSingle/>
        </Modal>
    );
}
