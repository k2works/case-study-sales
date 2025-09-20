import React from "react";
import Modal from "react-modal";
import { WarehouseCollectionSelectView } from "../../../views/master/warehouse/WarehouseSelect.tsx";
import { useWarehouseContext } from "../../../providers/master/Warehouse.tsx";
import { useInventoryContext } from "../../../providers/inventory/Inventory.tsx";
import { WarehouseType } from "../../../models/master/warehouse.ts";

type WarehouseSelectModalProps = {
    type: "edit" | "search";
};

export const WarehouseSelectModal: React.FC<WarehouseSelectModalProps> = ({ type }) => {
    const {
        newInventory,
        setNewInventory,
        searchInventoryCriteria,
        setSearchInventoryCriteria,
    } = useInventoryContext();

    const {
        modalIsOpen: warehouseModalIsOpen,
        setModalIsOpen: setWarehouseModalIsOpen,
        searchModalIsOpen: warehouseSearchModalIsOpen,
        setSearchModalIsOpen: setWarehouseSearchModalIsOpen,
        warehouses,
        fetchWarehouses,
        pageNation: warehousePageNation,
    } = useWarehouseContext();

    // 編集モーダルを閉じる
    const handleCloseEditModal = () => {
        setWarehouseModalIsOpen(false);
    };

    // 検索モーダルを閉じる
    const handleCloseSearchModal = () => {
        setWarehouseSearchModalIsOpen(false);
    };

    // 編集モード用モーダルビュー
    const warehouseEditModalView = () => (
        <Modal
            isOpen={warehouseModalIsOpen}
            onRequestClose={handleCloseEditModal}
            contentLabel="倉庫情報を選択"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <WarehouseCollectionSelectView
                warehouses={warehouses}
                handleSelect={(warehouse: WarehouseType) => {
                    setNewInventory({
                        ...newInventory,
                        warehouseCode: warehouse.warehouseCode,
                        warehouseName: warehouse.warehouseName
                    });
                    setWarehouseModalIsOpen(false);
                }}
                handleClose={handleCloseEditModal}
                pageNation={warehousePageNation}
                fetchWarehouses={fetchWarehouses.load}
            />
        </Modal>
    );

    // 検索モード用モーダルビュー
    const warehouseSearchModalView = () => (
        <Modal
            isOpen={warehouseSearchModalIsOpen}
            onRequestClose={handleCloseSearchModal}
            contentLabel="倉庫情報を検索"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <WarehouseCollectionSelectView
                warehouses={warehouses}
                handleSelect={(warehouse: WarehouseType) => {
                    setSearchInventoryCriteria({
                        ...searchInventoryCriteria,
                        warehouseCode: warehouse.warehouseCode,
                        warehouseName: warehouse.warehouseName
                    });
                    setWarehouseSearchModalIsOpen(false);
                }}
                handleClose={handleCloseSearchModal}
                pageNation={warehousePageNation}
                fetchWarehouses={fetchWarehouses.load}
            />
        </Modal>
    );

    return (
        <>
            {type === "edit" ? warehouseEditModalView() : null}
            {type === "search" ? warehouseSearchModalView() : null}
        </>
    );
};