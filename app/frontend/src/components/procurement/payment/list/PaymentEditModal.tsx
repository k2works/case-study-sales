import React from "react";
import Modal from "react-modal";
import { PaymentSingle } from "./PaymentSingle.tsx";
import { usePaymentContext } from "../../../../providers/procurement/Payment.tsx";
import { DepartmentSelectModal } from "./DepartmentSelectModal.tsx";
import { VendorSelectModal } from "./VendorSelectModal.tsx";

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
            contentLabel="支払情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <PaymentSingle/>
            <DepartmentSelectModal type={"edit"}/>
            <VendorSelectModal type={"edit"}/>
        </Modal>
    )
};
