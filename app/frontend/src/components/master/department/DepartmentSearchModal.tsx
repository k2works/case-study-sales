import React from "react";
import Modal from "react-modal";
import {DepartmentSearchSingleView} from "../../../views/master/department/DepartmentSearch.tsx";
import {showErrorMessage} from "../../application/utils.ts";
import {useDepartmentContext} from "../../../providers/master/Department.tsx";

export const DepartmentSearchModal: React.FC = () => {
    const {
        searchDepartmentCriteria,
        setSearchDepartmentCriteria,
        searchModalIsOpen,
        setSearchModalIsOpen,
        setDepartments,
        setCriteria,
        setPageNation,
        setLoading,
        setMessage,
        setError,
        departmentService
    } = useDepartmentContext();

    const handleCloseSearchModal = () => {
        setSearchModalIsOpen(false);
    }

    const handleSelectSearchModal = async () => {
        if (!searchDepartmentCriteria) {
            return;
        }
        setLoading(true);
        try {
            const result = await departmentService.search(searchDepartmentCriteria);
            setDepartments(result ? result.list : []);
            if (result.list.length === 0) {
                showErrorMessage(`検索結果は0件です`, setError);
            } else {
                setCriteria(searchDepartmentCriteria);
                setPageNation(result);
                setMessage("");
                setError("");
            }
        } catch (error: any) {
            showErrorMessage(`実行履歴情報の検索に失敗しました: ${error?.message}`, setError);
        } finally {
            setLoading(false);
        }
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
                <DepartmentSearchSingleView
                    criteria={searchDepartmentCriteria}
                    setCondition={setSearchDepartmentCriteria}
                    handleSelect={handleSelectSearchModal}
                    handleClose={handleCloseSearchModal}
                />
            }
        </Modal>
    )
}
