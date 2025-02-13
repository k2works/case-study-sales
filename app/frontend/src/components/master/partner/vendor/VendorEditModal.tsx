import React from "react";
import {useVendorContext} from "../../../../providers/master/partner/Vendor.tsx";
import Modal from "react-modal";
import {VendorSingle} from "./VendorSingle.tsx";

export const VendorEditModal: React.FC = () => {
    const {
        setError,
        modalIsOpen,
        setModalIsOpen,
        setEditId,
    } = useVendorContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    return (
        <Modal
            isOpen={modalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="仕入先情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <VendorSingle/>
        </Modal>
    );
}
