import React from "react";
import Modal from "react-modal";
import { WarehouseCollectionSelectView } from "../../../views/master/warehouse/WarehouseSelect.tsx";
import { useWarehouseContext } from "../../../providers/master/Warehouse.tsx";
import { useInventoryContext } from "../../../providers/inventory/Inventory.tsx";
import { WarehouseType } from "../../../models/master/warehouse.ts";

type WarehouseSelectModalProps = {
    type: "search";
};

export const WarehouseSelectModal: React.FC<WarehouseSelectModalProps> = ({ type }) => {
    const {
        searchInventoryCriteria,
        setSearchInventoryCriteria,
    } = useInventoryContext();

    const {
        searchModalIsOpen: warehouseSearchModalIsOpen,
        setSearchModalIsOpen: setWarehouseSearchModalIsOpen,
        warehouses,
        fetchWarehouses,
        pageNation: warehousePageNation,
    } = useWarehouseContext();

    // 検索モーダルを閉じる
    const handleCloseSearchModal = () => {
        setWarehouseSearchModalIsOpen(false);
    };

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
            {type === "search" ? warehouseSearchModalView() : null}
        </>
    );
};