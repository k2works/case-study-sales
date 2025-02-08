import React from "react";
import {usePartnerGroupContext} from "../../../../providers/PartnerGroup.tsx";
import Modal from "react-modal";
import {PartnerGroupSingle} from "./PartnerGroupSingle.tsx";

export const PartnerGroupEditModal: React.FC = () => {
    const {
        setError,
        modalIsOpen,
        setModalIsOpen,
        setEditId,
    } = usePartnerGroupContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    return(
        <Modal
            isOpen={modalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="取引先グループ情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <PartnerGroupSingle/>
        </Modal>
    )
}
