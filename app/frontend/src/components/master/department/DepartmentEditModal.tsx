import React from "react";
import Modal from "react-modal";
import {DepartmentSingle} from "./DepartmentSingle.tsx";
import {useDepartmentContext} from "../../../providers/master/Department.tsx";

export const DepartmentEditModal: React.FC = () => {
    const {
        modalIsOpen,
        setModalIsOpen,
        setEditId,
        setError
    } = useDepartmentContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    return (
        <Modal
            isOpen={modalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="部門情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <DepartmentSingle/>
        </Modal>
    )
}