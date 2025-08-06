import React from "react";
import Modal from "react-modal";
import {AccountSearchSingleView} from "../../../views/master/account/AccountSearch.tsx";
import {showErrorMessage} from "../../application/utils.ts";
import {useAccountContext} from "../../../providers/master/Account.tsx";
import {DepartmentSelectModal} from "./DepartmentSelectModal.tsx";
import {useDepartmentContext} from "../../../providers/master/Department.tsx";

export const AccountSearchModal: React.FC = () => {
    const {
        searchAccountCriteria,
        setSearchAccountCriteria,
        searchModalIsOpen,
        setSearchModalIsOpen,
        setAccounts,
        setCriteria,
        setPageNation,
        setLoading,
        setMessage,
        setError,
        accountService
    } = useAccountContext();

    const {
        setSearchModalIsOpen: setDepartmentSearchModalIsOpen,
    } = useDepartmentContext();

    const handleCloseSearchModal = () => {
        setSearchModalIsOpen(false);
    }

    const handleSelectSearchModal = async () => {
        if (!searchAccountCriteria) {
            return;
        }
        setLoading(true);
        try {
            const result = await accountService.search(searchAccountCriteria);
            setAccounts(result ? result.list : []);
            if (result.list.length === 0) {
                showErrorMessage(`検索結果は0件です`, setError);
            } else {
                setCriteria(searchAccountCriteria);
                setPageNation(result);
                setMessage("");
                setError("");
            }
        } catch (error: unknown) {
            showErrorMessage(`口座情報の検索に失敗しました: ${error instanceof Error ? error.message : String(error)}`, setError);
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
            <>
                <AccountSearchSingleView
                    criteria={searchAccountCriteria}
                    setCondition={setSearchAccountCriteria}
                    handleSelect={handleSelectSearchModal}
                    handleClose={handleCloseSearchModal}
                    handleDepartmentSelect={() => setDepartmentSearchModalIsOpen(true)}
                />
                <DepartmentSelectModal type="search" />
            </>
        </Modal>
    )
}
