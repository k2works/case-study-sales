import React from "react";
import Modal from "react-modal";
import {WarehouseSearchSingleView} from "../../../../views/master/inventory/warehouse/WarehouseSearch.tsx";
import {showErrorMessage} from "../../../application/utils.ts";
import {useWarehouseContext} from "../../../../providers/master/Warehouse.tsx";
import {WarehouseCriteriaType} from "../../../../models/master/warehouse.ts";

export const WarehouseSearchModal: React.FC = () => {
    const {
        searchWarehouseCriteria,
        setSearchWarehouseCriteria,
        searchModalIsOpen,
        setSearchModalIsOpen,
        setMessage,
        setError,
        fetchWarehouses
    } = useWarehouseContext();

    const handleCloseSearchModal = () => {
        setSearchModalIsOpen(false);
    }

    const handleSelectSearchModal = async () => {
        if (!searchWarehouseCriteria) {
            return;
        }
        try {
            const mappedCriteria: WarehouseCriteriaType = {
                ...searchWarehouseCriteria
            };

            await fetchWarehouses.load(1, mappedCriteria);
            setMessage("");
            setError("");
            setSearchModalIsOpen(false);
        } catch (error: any) {
            showErrorMessage(`倉庫の検索に失敗しました: ${error?.message}`, setError);
        }
    }

    const handleClearSearch = () => {
        setSearchWarehouseCriteria({});
    };

    return (
        <Modal
            isOpen={searchModalIsOpen}
            onRequestClose={handleCloseSearchModal}
            contentLabel="検索情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <WarehouseSearchSingleView
                criteria={searchWarehouseCriteria}
                setCondition={setSearchWarehouseCriteria}
                handleSelect={handleSelectSearchModal}
                handleClose={handleCloseSearchModal}
                onClearSearch={handleClearSearch}
            />
        </Modal>
    )
}