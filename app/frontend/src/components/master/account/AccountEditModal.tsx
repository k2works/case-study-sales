import React from "react";
import Modal from "react-modal";
import {AccountSingle} from "./AccountSingle.tsx";
import {useAccountContext} from "../../../providers/master/Account.tsx";
import {DepartmentSelectModal} from "./DepartmentSelectModal.tsx";

export const AccountEditModal: React.FC = () => {
    const {
        modalIsOpen,
        setModalIsOpen,
        setEditId,
        setError
    } = useAccountContext();

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
                contentLabel="口座情報を入力"
                className="modal"
                overlayClassName="modal-overlay"
                bodyOpenClassName="modal-open"
            >
                <AccountSingle/>
            </Modal>
            <DepartmentSelectModal type="edit" />
        </>
    )
}
