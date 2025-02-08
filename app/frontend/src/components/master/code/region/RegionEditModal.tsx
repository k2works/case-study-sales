import React from "react";
import {useRegionContext} from "../../../../providers/master/code/Region.tsx";
import Modal from "react-modal";
import {RegionSingle} from "./RegionSingle.tsx";

export const RegionEditModal: React.FC = () => {
    const {
        setError,
        modalIsOpen,
        setModalIsOpen,
        setEditId,
    } = useRegionContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    return (
        <Modal
            isOpen={modalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="地域情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <RegionSingle/>
        </Modal>
    );
}
