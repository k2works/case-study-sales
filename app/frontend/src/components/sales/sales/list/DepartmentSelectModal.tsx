import React from "react";
import Modal from "react-modal";
import { DepartmentCollectionSelectView } from "../../../../views/master/department/DepartmentSelect.tsx";
import { useDepartmentContext } from "../../../../providers/master/Department.tsx";
import { DepartmentType } from "../../../../models/master/department";
import {useSalesContext} from "../../../../providers/sales/Sales.tsx";

type DepartmentSelectModalProps = {
    type: "edit" | "search";
};

export const DepartmentSelectModal: React.FC<DepartmentSelectModalProps> = ({ type }) => {
    const {
        newSales,
        setNewSales,
        searchSalesCriteria,
        setSearchSalesCriteria,
    } = useSalesContext();

    const {
        pageNation: departmentPageNation,
        modalIsOpen: departmentModalIsOpen,
        setModalIsOpen: setDepartmentModalIsOpen,
        searchModalIsOpen: departmentSearchModalIsOpen,
        setSearchModalIsOpen: setDepartmentSearchModalIsOpen,
        departments,
        fetchDepartments,
    } = useDepartmentContext();

    const handleCloseEditModal = () => {
        setDepartmentModalIsOpen(false);
    };

    const handleCloseSearchModal = () => {
        setDepartmentSearchModalIsOpen(false);
    };

    const departmentEditModalView = () => (
        <Modal
            isOpen={departmentModalIsOpen}
            onRequestClose={handleCloseEditModal}
            contentLabel="部門情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <DepartmentCollectionSelectView
                departments={departments}
                handleSelect={(department: DepartmentType) => {
                    setNewSales({
                        ...newSales,
                        departmentCode: department.departmentCode,
                        departmentStartDate: department.startDate,
                    });
                    setDepartmentModalIsOpen(false);
                }}
                handleClose={handleCloseEditModal}
                pageNation={departmentPageNation}
                fetchDepartments={fetchDepartments.load}
            />
        </Modal>
    );

    const departmentSearchModalView = () => (
        <Modal
            isOpen={departmentSearchModalIsOpen}
            onRequestClose={handleCloseSearchModal}
            contentLabel="部門情報を検索"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <DepartmentCollectionSelectView
                departments={departments}
                handleSelect={(department: DepartmentType) => {
                    setSearchSalesCriteria({
                        ...searchSalesCriteria,
                        departmentCode: department.departmentCode,
                    });
                    setDepartmentSearchModalIsOpen(false);
                }}
                handleClose={handleCloseSearchModal}
                pageNation={departmentPageNation}
                fetchDepartments={fetchDepartments.load}
            />
        </Modal>
    );

    return (
        <>
            {type === "edit" ? departmentEditModalView() : null}
            {type === "search" ? departmentSearchModalView() : null}
        </>
    );
};