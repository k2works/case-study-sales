import React from "react";
import Modal from "react-modal";
import { usePurchaseOrderContext } from "../../../../providers/procurement/PurchaseOrder.tsx";
import { PurchaseOrderUploadSingle } from "./PurchaseOrderUploadSingle.tsx";

export const PurchaseOrderUploadModal: React.FC = () => {
    const {
        setMessage,
        setError,
        uploadModalIsOpen,
        setUploadModalIsOpen,
    } = usePurchaseOrderContext();

    const handleCloseModal = () => {
        setError("");
        setMessage("");
        setUploadModalIsOpen(false);
    };

    return (
        <Modal
            isOpen={uploadModalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="発注データアップロード"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <PurchaseOrderUploadSingle/>
        </Modal>
    );
};