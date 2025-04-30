import React from "react";
import Modal from "react-modal";
import { SalesSingle } from "./SalesSingle.tsx";
import { useSalesContext } from "../../../../providers/sales/Sales.tsx";
import {DepartmentSelectModal} from "./DepartmentSelectModal.tsx";
import {EmployeeSelectModal} from "./EmployeeSelectModal.tsx";
import {PartnerSelectModal} from "./PartnerSelectModal.tsx";
import {ProductSelectModal} from "./ProductSelectModal.tsx";

export const SalesEditModal: React.FC = () => {
    const {
        modalIsOpen,
        setModalIsOpen,
        setEditId,
        setError
    } = useSalesContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    return (
        <Modal
            isOpen={modalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="売上情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <SalesSingle/>
            <DepartmentSelectModal type={"edit"}/>
            <EmployeeSelectModal type={"edit"}/>
            <PartnerSelectModal type={"edit"}/>
            <ProductSelectModal/>
        </Modal>
    )
}
