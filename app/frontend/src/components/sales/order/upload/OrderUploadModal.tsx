import React from "react";
import Modal from "react-modal";
import { useSalesOrderContext } from "../../../../providers/sales/Order.tsx";
import {OrderUploadSingle} from "./OrderUploadSingle.tsx";

export const OrderUploadModal: React.FC = () => {
    const {
        setMessage,
        setError,
        uploadModalIsOpen,
        setUploadModalIsOpen,
    } = useSalesOrderContext();

    const handleCloseModal = () => {
        setError("");
        setMessage("");
        setUploadModalIsOpen(false);
    };

    return (
        <Modal
            isOpen={uploadModalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="受注データアップロード"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <OrderUploadSingle/>
        </Modal>
    );
};