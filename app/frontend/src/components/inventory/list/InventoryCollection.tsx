import React from "react";
import { useInventoryContext } from "../../../providers/inventory/Inventory.tsx";
import { InventoryCollectionView } from "../../../views/inventory/list/InventoryCollection.tsx";
import { InventoryType } from "../../../models/inventory/inventory.ts";
import { showErrorMessage } from "../../application/utils.ts";
import { InventorySearchModal } from "./InventorySearchModal.tsx";
import { InventoryEditModal } from "./InventoryEditModal.tsx";

export const InventoryCollection: React.FC = () => {
    const {
        message,
        setMessage,
        error,
        setError,
        pageNation,
        criteria,
        setModalIsOpen,
        setIsEditing,
        setEditId,
        initialInventory,
        inventories,
        setInventories,
        setNewInventory,
        searchInventoryCriteria,
        setSearchInventoryCriteria,
        fetchInventories,
        inventoryService,
        setSearchModalIsOpen,
        downloadInventories,
    } = useInventoryContext();

    const handleOpenModal = (inventory?: InventoryType) => {
        setMessage("");
        setError("");
        if (inventory) {
            setNewInventory(inventory);
            setEditId(`${inventory.warehouseCode}-${inventory.productCode}-${inventory.lotNumber}`);
            setIsEditing(true);
        } else {
            setNewInventory(initialInventory);
            setIsEditing(false);
        }
        setModalIsOpen(true);
    };

    const handleOpenSearchModal = () => {
        setSearchModalIsOpen(true);
    };

    const handleDeleteInventory = async (warehouseCode: string, productCode: string, lotNumber: string, stockCategory: string, qualityCategory: string) => {
        if (window.confirm("本当に削除しますか？")) {
            try {
                await inventoryService.destroy(warehouseCode, productCode, lotNumber, stockCategory, qualityCategory);
                setInventories(inventories.filter(inv =>
                    !(inv.warehouseCode === warehouseCode &&
                      inv.productCode === productCode &&
                      inv.lotNumber === lotNumber &&
                      inv.stockCategory === stockCategory &&
                      inv.qualityCategory === qualityCategory)
                ));
                setMessage("在庫データを削除しました。");
            } catch (error: any) {
                showErrorMessage(`在庫データの削除に失敗しました: ${error?.message}`, setError);
            }
        }
    };

    const handleCheckInventory = (inventory: InventoryType) => {
        setInventories(inventories.map(inv =>
            (inv.warehouseCode === inventory.warehouseCode &&
             inv.productCode === inventory.productCode &&
             inv.lotNumber === inventory.lotNumber)
                ? { ...inv, checked: !inv.checked }
                : inv
        ));
    };

    const handleCheckToggleCollection = () => {
        const allChecked = inventories.every(inv => inv.checked);
        setInventories(inventories.map(inv => ({ ...inv, checked: !allChecked })));
    };

    const handleDeleteCheckedCollection = async () => {
        const checkedInventories = inventories.filter(inv => inv.checked);
        if (checkedInventories.length === 0) {
            setMessage("削除する在庫が選択されていません。");
            return;
        }

        if (window.confirm(`選択された${checkedInventories.length}件の在庫を削除しますか？`)) {
            try {
                for (const inventory of checkedInventories) {
                    await inventoryService.destroy(inventory.warehouseCode, inventory.productCode, inventory.lotNumber, inventory.stockCategory, inventory.qualityCategory);
                }
                setInventories(inventories.filter(inv => !inv.checked));
                setMessage(`${checkedInventories.length}件の在庫データを削除しました。`);
            } catch (error: any) {
                showErrorMessage(`在庫データの一括削除に失敗しました: ${error?.message}`, setError);
            }
        }
    };

    return (
        <>
            <InventoryCollectionView
                error={error}
                message={message}
                searchItems={{
                    searchInventoryCriteria,
                    setSearchInventoryCriteria,
                    handleOpenSearchModal
                }}
                headerItems={{
                    handleOpenModal,
                    handleCheckToggleCollection,
                    handleDeleteCheckedCollection,
                    handleDownloadInventories: downloadInventories
                }}
                collectionItems={{
                    inventories,
                    handleOpenModal,
                    handleDeleteInventory,
                    handleCheckInventory
                }}
                pageNationItems={{
                    pageNation,
                    criteria,
                    fetchInventories
                }}
            />
            <InventorySearchModal />
            <InventoryEditModal />
        </>
    );
};