import React from "react";
import {EmployeeSingle} from "./EmployeeSingle.tsx";
import {useEmployeeContext} from "../../../providers/master/Employee.tsx";
import Modal from "react-modal";
import {DepartmentSelectModal} from "./DepartmentSelectModal.tsx";
import {UserSelectModal} from "./UserSelectModal.tsx";

export const EmployeeEditModal: React.FC = () => {
    const {
        setError,
        modalIsOpen,
        setModalIsOpen,
        setEditId,
    } = useEmployeeContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    return(
        <Modal
            isOpen={modalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="社員情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <DepartmentSelectModal type={"edit"}/>
            <UserSelectModal/>
            <EmployeeSingle/>
        </Modal>
    )
}
