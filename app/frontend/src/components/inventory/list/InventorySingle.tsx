import React from "react";
import { useInventoryContext } from "../../../providers/inventory/Inventory.tsx";
import { InventoryEditModalView } from "../../../views/inventory/list/InventoryEditModal.tsx";
import { showErrorMessage } from "../../application/utils.ts";
import { useWarehouseContext } from "../../../providers/master/Warehouse.tsx";
import { useProductItemContext } from "../../../providers/master/product/ProductItem.tsx";

export const InventorySingle: React.FC = () => {
    const {
        message,
        setMessage,
        error,
        setError,
        isEditing,
        newInventory,
        setNewInventory,
        inventoryService,
        fetchInventories,
        setModalIsOpen,
    } = useInventoryContext();

    const {
        setModalIsOpen: setWarehouseModalIsOpen,
    } = useWarehouseContext();

    const {
        setModalIsOpen: setProductModalIsOpen,
    } = useProductItemContext();

    const handleCloseModal = () => {
        setModalIsOpen(false);
    };

    const handleWarehouseSelect = () => {
        setWarehouseModalIsOpen(true);
    };

    const handleProductSelect = () => {
        setProductModalIsOpen(true);
    };

    const handleSaveInventory = async () => {
        try {
            if (isEditing) {
                await inventoryService.update(newInventory);
                setMessage("在庫データを更新しました。");
            } else {
                await inventoryService.create(newInventory);
                setMessage("在庫データを登録しました。");
            }
            await fetchInventories.load();
            setModalIsOpen(false);
        } catch (error: any) {
            showErrorMessage(`在庫データの${isEditing ? '更新' : '登録'}に失敗しました: ${error?.message}`, setError);
        }
    };

    return (
        <InventoryEditModalView
            isOpen={true}
            onClose={handleCloseModal}
            isEditing={isEditing}
            inventory={newInventory}
            setInventory={setNewInventory}
            onSave={handleSaveInventory}
            error={error}
            message={message}
            handleWarehouseSelect={handleWarehouseSelect}
            handleProductSelect={handleProductSelect}
        />
    );
};