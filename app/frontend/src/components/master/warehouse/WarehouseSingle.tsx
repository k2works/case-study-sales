import React from "react";
import {showErrorMessage} from "../../application/utils.ts";
import {WarehouseSingleView} from "../../../views/master/warehouse/WarehouseSingle.tsx";
import {useWarehouseContext} from "../../../providers/master/Warehouse.tsx";

export const WarehouseSingle: React.FC = () => {
    const {
        error,
        setError,
        message,
        setMessage,
        isEditing,
        newWarehouse,
        setNewWarehouse,
        initialWarehouse,
        fetchWarehouses,
        warehouseService,
        setModalIsOpen,
        editId,
        setEditId
    } = useWarehouseContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    const handleCreateOrUpdateWarehouse = async () => {
        const validateWarehouse = (): boolean => {
            if (!newWarehouse.warehouseCode.trim() || !newWarehouse.warehouseName.trim()) {
                setError("倉庫コード、倉庫名は必須項目です。");
                return false;
            }
            return true;
        };

        if (!validateWarehouse()) {
            return;
        }

        try {
            if (isEditing && editId) {
                await warehouseService.update(newWarehouse);
            } else {
                await warehouseService.create(newWarehouse);
            }
            setNewWarehouse(initialWarehouse);
            await fetchWarehouses();
            if (isEditing) {
                setMessage("倉庫を更新しました。");
            } else {
                setMessage("倉庫を作成しました。");
            }
            handleCloseModal();
        } catch (error: unknown) {
            showErrorMessage(error, setError, "倉庫の作成に失敗しました");
        }
    }

    return (
        <WarehouseSingleView
            error={error}
            message={message}
            isEditing={isEditing}
            headerItems={{handleCreateOrUpdateWarehouse, handleCloseModal}}
            formItems={{newWarehouse, setNewWarehouse}}
        />
    );
}