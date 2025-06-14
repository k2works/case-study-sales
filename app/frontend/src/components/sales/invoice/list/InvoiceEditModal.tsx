import React from "react";
import Modal from "react-modal";
import { InvoiceSingle } from "./InvoiceSingle.tsx";
import { useInvoiceContext } from "../../../../providers/sales/Invoice.tsx";
import {CustomerSelectModal} from "./CustomerSelectModal.tsx";

export const InvoiceEditModal: React.FC = () => {
    const {
        modalIsOpen,
        setModalIsOpen,
        setError
    } = useInvoiceContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
    };

    return (
        <Modal
            isOpen={modalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="請求情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <InvoiceSingle/>
            <CustomerSelectModal type={"edit"}/>
        </Modal>
    );
};
