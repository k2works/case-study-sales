import React from "react";
import Modal from "react-modal";
import {DepartmentType} from "../../../models";
import {DepartmentSingle} from "./DepartmentSingle.tsx";
import {useDepartmentContext} from "../../../providers/Department.tsx";

export const DepartmentEditModal: React.FC = () => {
    const {
        modalIsOpen,
        setModalIsOpen,
        setNewDepartment,
        setIsEditing,
        setEditId,
        initialDepartment,
        setMessage,
        setError
    } = useDepartmentContext();

    const handleOpenModal = (department?: DepartmentType) => {
        setMessage("");
        setError("");
        if (department) {
            setNewDepartment(department);
            setEditId(department.departmentId.deptCode.value);
            setIsEditing(true);
        } else {
            setNewDepartment(initialDepartment);
            setIsEditing(false);
        }
        setModalIsOpen(true);
    };

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    return (
        <Modal
            isOpen={modalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="部門情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <DepartmentSingle/>
        </Modal>
    )
}