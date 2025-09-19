import React from "react";
import Modal from "react-modal";
import { InventorySearchModalView } from "../../../views/inventory/list/InventorySearchModal.tsx";
import { showErrorMessage } from "../../application/utils.ts";
import { useInventoryContext } from "../../../providers/inventory/Inventory.tsx";
import { InventoryCriteriaType } from "../../../models/inventory/inventory.ts";
import { WarehouseSelectModal } from "./WarehouseSelectModal.tsx";
import { ProductSelectModal } from "./ProductSelectModal.tsx";
import { useWarehouseContext } from "../../../providers/master/Warehouse.tsx";
import { useProductItemContext } from "../../../providers/master/product/ProductItem.tsx";

export const InventorySearchModal: React.FC = () => {
    const {
        searchInventoryCriteria,
        setSearchInventoryCriteria,
        searchModalIsOpen,
        setSearchModalIsOpen,
        setInventories,
        setCriteria,
        setPageNation,
        setLoading,
        setMessage,
        setError,
        inventoryService
    } = useInventoryContext();

    const {
        setSearchModalIsOpen: setWarehouseSearchModalIsOpen,
    } = useWarehouseContext();

    const {
        setSearchModalIsOpen: setProductSearchModalIsOpen,
    } = useProductItemContext();

    const handleCloseSearchModal = () => {
        setSearchModalIsOpen(false);
    }

    const handleWarehouseSelect = () => {
        setWarehouseSearchModalIsOpen(true);
    };

    const handleProductSelect = () => {
        setProductSearchModalIsOpen(true);
    };

    const handleSelectSearchModal = async () => {
        if (!searchInventoryCriteria) {
            return;
        }
        setLoading(true);
        try {
            // 検索条件をマッピング
            const mappedCriteria: InventoryCriteriaType = {
                ...searchInventoryCriteria,
                hasStock: searchInventoryCriteria.hasStock === "true",
                isAvailable: searchInventoryCriteria.isAvailable === "true"
            };
            const result = await inventoryService.search(mappedCriteria);
            setInventories(result ? result.list : []);
            if (result.list.length === 0) {
                showErrorMessage(`検索結果は0件です`, setError);
            } else {
                setCriteria(mappedCriteria);
                setPageNation(result);
                setMessage("");
                setError("");
            }
            setSearchModalIsOpen(false);
        } catch (error: any) {
            showErrorMessage(`在庫の検索に失敗しました: ${error?.message}`, setError);
        } finally {
            setLoading(false);
        }
    }

    const handleClearSearch = () => {
        setSearchInventoryCriteria({});
    };

    return (
        <>
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
                    handleWarehouseSelect={handleWarehouseSelect}
                    handleProductSelect={handleProductSelect}
                />
            </Modal>
            <WarehouseSelectModal type="search" />
            <ProductSelectModal type="search" />
        </>
    )
}