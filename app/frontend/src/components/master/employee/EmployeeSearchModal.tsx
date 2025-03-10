import React from "react";
import Modal from "react-modal";
import {EmployeeSearchSingleView} from "../../../views/master/employee/EmployeeSearch.tsx";
import {showErrorMessage} from "../../application/utils.ts";
import {
    DepartmentSelectView
} from "../../../views/master/department/DepartmentSelect.tsx";
import {useEmployeeContext} from "../../../providers/master/Employee.tsx";
import {DepartmentSelectModal} from "./DepartmentSelectModal.tsx";
import {useDepartmentContext} from "../../../providers/master/Department.tsx";

export const EmployeeSearchModal: React.FC = () => {
    const {
        searchEmployeeCriteria,
        setSearchEmployeeCriteria,
        searchModalIsOpen,
        setSearchModalIsOpen,
        setEmployees,
        setPageNation,
        setCriteria,
        setLoading,
        setMessage,
        setError,
        employeeService,
    } = useEmployeeContext();

    const {
        setSearchModalIsOpen: setDepartmentSearchModalIsOpen,
    } = useDepartmentContext();

    const handleCloseSearchModal = () => {
        setSearchModalIsOpen(false);
    }

    return (
        <Modal
            isOpen={searchModalIsOpen}
            onRequestClose={handleCloseSearchModal}
            contentLabel="検索情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            {
                <>
                    <EmployeeSearchSingleView
                        criteria={searchEmployeeCriteria}
                        setCondition={setSearchEmployeeCriteria}
                        handleSelect={async () => {
                            if (!searchEmployeeCriteria) {
                                return;
                            }
                            setLoading(true);
                            try {
                                const result = await employeeService.search(searchEmployeeCriteria);
                                setEmployees(result ? result.list : []);
                                if (result.list.length === 0) {
                                    showErrorMessage(`検索結果は0件です`, setError);
                                } else {
                                    setCriteria(searchEmployeeCriteria);
                                    setPageNation(result);
                                    setMessage("");
                                    setError("");
                                }
                            } catch (error: any) {
                                showErrorMessage(`実行履歴情報の検索に失敗しました: ${error?.message}`, setError);
                            } finally {
                                setLoading(false);
                            }
                        }}
                        handleClose={handleCloseSearchModal}
                    />

                    <DepartmentSelectModal type={"search"}/>
                    <DepartmentSelectView
                        handleSelect={() => setDepartmentSearchModalIsOpen(true)}
                    />
                </>
            }
        </Modal>
    )
}
