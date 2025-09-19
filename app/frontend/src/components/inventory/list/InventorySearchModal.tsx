import React from "react";
import Modal from "react-modal";
import { InventorySearchModalView } from "../../../views/inventory/list/InventorySearchModal.tsx";
import { showErrorMessage } from "../../application/utils.ts";
import { useInventoryContext } from "../../../providers/inventory/Inventory.tsx";
import { InventoryCriteriaType } from "../../../models/inventory/inventory.ts";

export const InventorySearchModal: React.FC = () => {
    const {
        searchInventoryCriteria,
        setSearchInventoryCriteria,
        searchModalIsOpen,
        setSearchModalIsOpen,
        setMessage,
        setError,
        fetchInventories
    } = useInventoryContext();

    const handleCloseSearchModal = () => {
        setSearchModalIsOpen(false);
    }

    const handleSelectSearchModal = async () => {
        if (!searchInventoryCriteria) {
            return;
        }
        try {
            // 検索条件をマッピング
            const mappedCriteria: InventoryCriteriaType = {
                ...searchInventoryCriteria,
                hasStock: searchInventoryCriteria.hasStock === "true",
                isAvailable: searchInventoryCriteria.isAvailable === "true"
            };

            await fetchInventories(1, mappedCriteria);
            setMessage("");
            setError("");
            setSearchModalIsOpen(false);
        } catch (error: any) {
            showErrorMessage(`在庫の検索に失敗しました: ${error?.message}`, setError);
        }
    }

    const handleClearSearch = () => {
        setSearchInventoryCriteria({});
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
            <InventorySearchModalView
                isOpen={true}
                onClose={handleCloseSearchModal}
                searchCriteria={searchInventoryCriteria}
                setSearchCriteria={setSearchInventoryCriteria}
                onSearch={handleSelectSearchModal}
                onClearSearch={handleClearSearch}
            />
        </Modal>
    )
}