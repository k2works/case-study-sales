import React from "react";
import Modal from "react-modal";
import { PurchaseOrderSingle } from "./PurchaseOrderSingle.tsx";
import { usePurchaseOrderContext } from "../../../../providers/procurement/PurchaseOrder.tsx";
import {EmployeeSelectModal} from "./EmployeeSelectModal.tsx";
import {VendorSelectModal} from "./VendorSelectModal.tsx";
import {ProductSelectModal} from "./ProductSelectModal.tsx";

export const PurchaseOrderEditModal: React.FC = () => {
    const {
        modalIsOpen,
        setModalIsOpen,
        setEditId,
        setError
    } = usePurchaseOrderContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    return (
        <Modal
            isOpen={modalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="発注情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <PurchaseOrderSingle/>
            <EmployeeSelectModal type={"edit"}/>
            <VendorSelectModal type={"edit"}/>
            <ProductSelectModal/>
        </Modal>
    )
};