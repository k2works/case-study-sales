import React from "react";
import Modal from "react-modal";
import {WarehouseSingle} from "./WarehouseSingle.tsx";
import {useWarehouseContext} from "../../../../providers/master/Warehouse.tsx";

export const WarehouseEditModal: React.FC = () => {
    const {
        modalIsOpen,
        setModalIsOpen,
        setEditId,
        setError
    } = useWarehouseContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    return (
        <Modal
            isOpen={modalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="倉庫情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <WarehouseSingle/>
        </Modal>
    )
}