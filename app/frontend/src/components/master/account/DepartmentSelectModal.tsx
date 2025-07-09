import React from "react";
import Modal from "react-modal";
import { DepartmentCollectionSelectView } from "../../../views/master/department/DepartmentSelect.tsx";
import { useDepartmentContext } from "../../../providers/master/Department.tsx";
import { useAccountContext } from "../../../providers/master/Account.tsx";
import { DepartmentType } from "../../../models/master/department";

export const DepartmentSelectModal: React.FC = () => {
    const {
        newAccount,
        setNewAccount,
    } = useAccountContext();

    const {
        pageNation: departmentPageNation,
        modalIsOpen: departmentModalIsOpen,
        setModalIsOpen: setDepartmentModalIsOpen,
        departments,
        fetchDepartments,
    } = useDepartmentContext();

    const handleCloseModal = () => {
        setDepartmentModalIsOpen(false);
    };

    return (
        <Modal
            isOpen={departmentModalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="部門情報を選択"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <DepartmentCollectionSelectView
                departments={departments}
                handleSelect={(department: DepartmentType) => {
                    setNewAccount({
                        ...newAccount,
                        departmentCode: department.departmentCode,
                    });
                    setDepartmentModalIsOpen(false);
                }}
                handleClose={handleCloseModal}
                pageNation={departmentPageNation}
                fetchDepartments={fetchDepartments.load}
            />
        </Modal>
    );
};