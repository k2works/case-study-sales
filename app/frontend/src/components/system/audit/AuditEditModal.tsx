import React from "react";
import {useAuditContext} from "../../../providers/Audit.tsx";
import Modal from "react-modal";
import {AuditSingle} from "./AuditSingle.tsx";

export const AuditEditModal: React.FC = () => {
    const {
        setError,
        modalIsOpen,
        setModalIsOpen,
        setEditId,
    } = useAuditContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    return (
        <Modal
            isOpen={modalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="アプリケーション実行履歴情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <AuditSingle/>
        </Modal>
    )
}