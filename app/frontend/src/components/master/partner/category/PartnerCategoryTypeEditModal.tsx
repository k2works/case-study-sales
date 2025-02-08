import React from "react";
import {usePartnerCategoryContext} from "../../../../providers/PartnerCategory.tsx";
import Modal from "react-modal";
import {PartnerCategorySingle} from "./PartnerCategorySingle.tsx";

export const PartnerCategoryTypeEditModal: React.FC = () => {
    const {
        setError,
        modalIsOpen,
        setModalIsOpen,
        setEditId,
    } = usePartnerCategoryContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    return (
        <Modal
            isOpen={modalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="取引先分類種別情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <PartnerCategorySingle/>
        </Modal>
    );
}
