import React from "react";
import {useUserContext} from "../../../providers/system/User.tsx";
import Modal from "react-modal";
import {UserSingle} from "./UserSingle.tsx";

export const UserEditModal: React.FC = () => {
    const {
        setError,
        modalIsOpen,
        setModalIsOpen,
        setEditId,
    } = useUserContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    return (
        <Modal
            isOpen={modalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="ユーザー情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <UserSingle/>
        </Modal>
    );
}
