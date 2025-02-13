import React from "react";
import {usePartnerListContext} from "../../../../providers/master/partner/PartnerList.tsx";
import Modal from "react-modal";
import {PartnerListSingle} from "./PartnerListSingle.tsx";

export const PartnerListEditModal: React.FC = () => {
    const {
        setError,
        modalIsOpen,
        setModalIsOpen,
        setEditId,
    } = usePartnerListContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    return (
        <Modal
            isOpen={modalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="取引先情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <PartnerListSingle/>
        </Modal>
    )
}
