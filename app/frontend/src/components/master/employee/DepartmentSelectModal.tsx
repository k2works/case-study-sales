import React from "react";
import Modal from "react-modal";
import {DepartmentCollectionSelectView} from "../../../views/master/department/DepartmentSelect.tsx";
import {useDepartmentContext} from "../../../providers/Department.tsx";
import {useEmployeeContext} from "../../../providers/Employee.tsx";

export const DepartmentSelectModal: React.FC = () => {
    const {
        newEmployee,
        setNewEmployee,
    } = useEmployeeContext();

    const {
        pageNation: departmentPageNation,
        modalIsOpen: departmentModalIsOpen,
        setModalIsOpen: setDepartmentModalIsOpen,
        departments,
        fetchDepartments,
    } = useDepartmentContext();

    return (
        <Modal
            isOpen={departmentModalIsOpen}
            onRequestClose={() => setDepartmentModalIsOpen(false)}
            contentLabel="部門情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            {
                <DepartmentCollectionSelectView
                    departments={departments}
                    handleSelect={(department) => {
                        setNewEmployee({
                            ...newEmployee,
                            department: department
                        });
                        setDepartmentModalIsOpen(false);
                    }}
                    handleClose={() => setDepartmentModalIsOpen(false)}
                    pageNation={departmentPageNation}
                    fetchDepartments={fetchDepartments.load}
                />
            }
        </Modal>
    )
}
