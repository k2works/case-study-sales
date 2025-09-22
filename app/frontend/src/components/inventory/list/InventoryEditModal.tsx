import React from "react";
import Modal from "react-modal";
import { InventorySingle } from "./InventorySingle.tsx";
import { useInventoryContext } from "../../../providers/inventory/Inventory.tsx";
import { WarehouseSelectModal } from "./WarehouseSelectModal.tsx";
import { ProductSelectModal } from "./ProductSelectModal.tsx";

export const InventoryEditModal: React.FC = () => {
    const {
        modalIsOpen,
        setModalIsOpen,
        setEditId,
        setError
    } = useInventoryContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    return (
        <>
            <Modal
                isOpen={modalIsOpen}
                onRequestClose={handleCloseModal}
                contentLabel="在庫情報を入力"
                className="modal"
                overlayClassName="modal-overlay"
                bodyOpenClassName="modal-open"
            >
                <InventorySingle/>
            </Modal>
            <WarehouseSelectModal type="edit" />
            <ProductSelectModal type="edit" />
        </>
    )
};