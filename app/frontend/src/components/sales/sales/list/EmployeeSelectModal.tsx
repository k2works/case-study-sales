import React from "react";
import Modal from "react-modal";
import { EmployeeCollectionSelectView } from "../../../../views/master/employee/EmployeeSelect.tsx";
import { useEmployeeContext } from "../../../../providers/master/Employee.tsx";
import { EmployeeType } from "../../../../models";
import {useSalesContext} from "../../../../providers/sales/Sales.tsx";

type EmployeeSelectModalProps = {
    type: "edit" | "search";
};

export const EmployeeSelectModal: React.FC<EmployeeSelectModalProps> = ({ type }) => {
    const {
        newSales,
        setNewSales,
        searchSalesCriteria,
        setSearchSalesCriteria,
    } = useSalesContext();

    const {
        pageNation: employeePageNation,
        modalIsOpen: employeeModalIsOpen,
        setModalIsOpen: setEmployeeModalIsOpen,
        searchModalIsOpen: employeeSearchModalIsOpen,
        setSearchModalIsOpen: setEmployeeSearchModalIsOpen,
        employees,
        fetchEmployees,
    } = useEmployeeContext();

    // 編集モーダルを閉じる
    const handleCloseEditModal = () => {
        setEmployeeModalIsOpen(false);
    };

    // 検索モーダルを閉じる
    const handleCloseSearchModal = () => {
        setEmployeeSearchModalIsOpen(false);
    };

    // 編集モード用モーダルビュー
    const employeeEditModalView = () => (
        <Modal
            isOpen={employeeModalIsOpen}
            onRequestClose={handleCloseEditModal}
            contentLabel="従業員情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <EmployeeCollectionSelectView
                employees={employees}
                handleSelect={(employee: EmployeeType) => {
                    setNewSales({
                        ...newSales,
                        employeeCode: employee.empCode,
                    });
                    setEmployeeModalIsOpen(false);
                }}
                handleClose={handleCloseEditModal}
                pageNation={employeePageNation}
                fetchEmployees={fetchEmployees.load}
            />
        </Modal>
    );

    // 検索モード用モーダルビュー
    const employeeSearchModalView = () => (
        <Modal
            isOpen={employeeSearchModalIsOpen}
            onRequestClose={handleCloseSearchModal}
            contentLabel="従業員情報を検索"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <EmployeeCollectionSelectView
                employees={employees}
                handleSelect={() => {
                    setSearchSalesCriteria({
                        ...searchSalesCriteria,
                    });
                    setEmployeeSearchModalIsOpen(false);
                }}
                handleClose={handleCloseSearchModal}
                pageNation={employeePageNation}
                fetchEmployees={fetchEmployees.load}
            />
        </Modal>
    );

    return (
        <>
            {type === "edit" ? employeeEditModalView() : null}
            {type === "search" ? employeeSearchModalView() : null}
        </>
    );
};
