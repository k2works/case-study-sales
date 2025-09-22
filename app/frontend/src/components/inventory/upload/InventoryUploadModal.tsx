import React from "react";
import Modal from "react-modal";
import { useInventoryContext } from "../../../providers/inventory/Inventory.tsx";
import { InventoryUploadSingle } from "./InventoryUploadSingle.tsx";

export const InventoryUploadModal: React.FC = () => {
    const {
        setMessage,
        setError,
        uploadModalIsOpen,
        setUploadModalIsOpen,
    } = useInventoryContext();

    const handleCloseModal = () => {
        setError("");
        setMessage("");
        setUploadModalIsOpen(false);
    };

    return (
        <Modal
            isOpen={uploadModalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="在庫データアップロード"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <InventoryUploadSingle/>
        </Modal>
    );
};