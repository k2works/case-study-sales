import React from "react";
import {useWarehouseContext} from "../../../providers/master/Warehouse.tsx";
import {WarehouseCollectionView} from "../../../views/master/warehouse/WarehouseCollection.tsx";
import {WarehouseType} from "../../../models/master/warehouse.ts";
import {showErrorMessage} from "../../application/utils.ts";
import {WarehouseSearchModal} from "./WarehouseSearchModal.tsx";
import {WarehouseEditModal} from "./WarehouseEditModal.tsx";

export const WarehouseCollection: React.FC = () => {
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
        initialWarehouse,
        warehouses,
        setWarehouses,
        setNewWarehouse,
        searchWarehouseCriteria,
        setSearchWarehouseCriteria,
        fetchWarehouses,
        warehouseService,
        setSearchModalIsOpen,
    } = useWarehouseContext();

    const handleOpenModal = (warehouse?: WarehouseType) => {
        setMessage("");
        setError("");
        if (warehouse) {
            setNewWarehouse(warehouse);
            setEditId(warehouse.warehouseCode);
            setIsEditing(true);
        } else {
            setNewWarehouse(initialWarehouse);
            setIsEditing(false);
        }
        setModalIsOpen(true);
    };

    const handleOpenSearchModal = () => {
        setSearchModalIsOpen(true);
    }

    const handleDeleteWarehouse = async (warehouseCode: string) => {
        try {
            if (!window.confirm(`倉庫コード:${warehouseCode} を削除しますか？`)) return;
            await warehouseService.destroy(warehouseCode);
            await fetchWarehouses();
            setMessage("倉庫を削除しました。");
        } catch (error: any) {
            showErrorMessage(`倉庫の削除に失敗しました: ${error?.message}`, setError);
        }
    };

    const handleCheckWarehouse = (warehouse: WarehouseType) => {
        const newWarehouses = warehouses.map((w: WarehouseType) => {
            if (w.warehouseCode === warehouse.warehouseCode) {
                return {
                    ...w,
                    checked: !w.checked
                };
            }
            return w;
        });
        setWarehouses(newWarehouses);
    }

    const handleCheckAllWarehouse = () => {
        const newWarehouses = warehouses.map((w: WarehouseType) => {
            return {
                ...w,
                checked: !warehouses.every((w: WarehouseType) => w.checked)
            };
        });
        setWarehouses(newWarehouses);
    }

    const handleDeleteCheckedCollection = async () => {
        const checkedWarehouses = warehouses.filter((w: WarehouseType) => w.checked);
        if (!checkedWarehouses.length) {
            setError("削除する倉庫を選択してください。");
            return;
        }

        try {
            if (!window.confirm("選択した倉庫を削除しますか？")) return;
            await Promise.all(checkedWarehouses.map((w: WarehouseType) => warehouseService.destroy(w.warehouseCode)));
            await fetchWarehouses();
            setMessage("選択した倉庫を削除しました。");
        } catch (error: any) {
            showErrorMessage(`選択した倉庫の削除に失敗しました: ${error?.message}`, setError);
        }
    }

    return (
        <>
            <WarehouseSearchModal/>
            <WarehouseEditModal/>
            <WarehouseCollectionView
                error={error}
                message={message}
                searchItems={{searchWarehouseCriteria, setSearchWarehouseCriteria, handleOpenSearchModal}}
                headerItems={{handleOpenModal, handleCheckToggleCollection:handleCheckAllWarehouse, handleDeleteCheckedCollection}}
                collectionItems={{warehouses, handleOpenModal, handleDeleteWarehouse, handleCheckWarehouse}}
                pageNationItems={{pageNation, fetchWarehouses, criteria}}
            />
        </>
    );
}