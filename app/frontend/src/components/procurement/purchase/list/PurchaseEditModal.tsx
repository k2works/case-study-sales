import React from "react";
import Modal from "react-modal";
import { PurchaseSingle } from "./PurchaseSingle.tsx";
import { usePurchaseContext } from "../../../../providers/procurement/Purchase.tsx";
import {EmployeeSelectModal} from "./EmployeeSelectModal.tsx";
import {VendorSelectModal} from "./VendorSelectModal.tsx";
import {ProductSelectModal} from "./ProductSelectModal.tsx";
import {DepartmentSelectModal} from "./DepartmentSelectModal.tsx";
import {WarehouseSelectModal} from "./WarehouseSelectModal.tsx";

export const PurchaseEditModal: React.FC = () => {
    const {
        modalIsOpen,
        setModalIsOpen,
        setEditId,
        setError
    } = usePurchaseContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    return (
        <Modal
            isOpen={modalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="仕入情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <PurchaseSingle/>
            <EmployeeSelectModal type={"edit"}/>
            <VendorSelectModal type={"edit"}/>
            <ProductSelectModal/>
            <DepartmentSelectModal type={"edit"}/>
            <WarehouseSelectModal type={"edit"}/>
        </Modal>
    )
};
