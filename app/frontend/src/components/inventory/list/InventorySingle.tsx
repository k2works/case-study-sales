import React from "react";
import { useInventoryContext } from "../../../providers/inventory/Inventory.tsx";
import { InventoryEditModalView } from "../../../views/inventory/list/InventoryEditModal.tsx";
import { showErrorMessage } from "../../application/utils.ts";

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

    const handleCloseModal = () => {
        setModalIsOpen(false);
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
        />
    );
};