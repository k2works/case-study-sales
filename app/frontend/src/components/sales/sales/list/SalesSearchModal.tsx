import React from "react";
import Modal from "react-modal";
import { SalesSearchSingleView } from "../../../../views/sales/sales/list/SalesSearch.tsx";
import { showErrorMessage } from "../../../application/utils.ts";
import { useSalesContext } from "../../../../providers/sales/Sales.tsx";
import { useDepartmentContext } from "../../../../providers/master/Department.tsx";

export const SalesSearchModal: React.FC = () => {
    const {
        searchSalesCriteria,
        setSearchSalesCriteria,
        searchModalIsOpen,
        setSearchModalIsOpen,
        setSales,
        setCriteria,
        setPageNation,
        setLoading,
        setMessage,
        setError,
        salesService
    } = useSalesContext();

    const {
        setSearchModalIsOpen: setDepartmentSearchModalIsOpen,
    } = useDepartmentContext();

    const handleCloseSearchModal = () => {
        setSearchModalIsOpen(false);
    }

    const handleSelectSearchModal = async () => {
        if (!searchSalesCriteria) {
            return;
        }
        setLoading(true);
        try {
            const result = await salesService.search(searchSalesCriteria);
            setSales(result ? result.list : []);
            if (result.list.length === 0) {
                showErrorMessage(`検索結果は0件です`, setError);
            } else {
                setCriteria(searchSalesCriteria);
                setPageNation(result);
                setMessage("");
                setError("");
            }
        } catch (error: unknown) {
            const errorMessage = error instanceof Error ? error.message : '不明なエラーが発生しました';
            showErrorMessage(`売上の検索に失敗しました: ${errorMessage}`, setError);
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
                <SalesSearchSingleView
                    criteria={searchSalesCriteria}
                    setCondition={setSearchSalesCriteria}
                    handleSelect={handleSelectSearchModal}
                    handleClose={handleCloseSearchModal}
                    handleDepartmentSelect={() => setDepartmentSearchModalIsOpen(true)}
                />
            }
        </Modal>
    )
}
