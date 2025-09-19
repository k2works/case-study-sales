import React from "react";
import Modal from "react-modal";
import {LocationNumberSingle} from "./LocationNumberSingle.tsx";
import {useLocationNumberContext} from "../../../providers/master/LocationNumber.tsx";

export const LocationNumberEditModal: React.FC = () => {
    const {
        modalIsOpen,
        setModalIsOpen,
        setEditId,
        setError
    } = useLocationNumberContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    return (
        <Modal
            isOpen={modalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="棚番情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <LocationNumberSingle/>
        </Modal>
    )
}