import React from "react";
import Modal from "react-modal";
import { useSalesOrderContext } from "../../../../providers/sales/SalesOrder";
import {SalesOrderUploadSingle} from "./SalesOrderUploadSingle.tsx";

export const SalesOrderUploadModal: React.FC = () => {
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
            <SalesOrderUploadSingle/>
        </Modal>
    );
};