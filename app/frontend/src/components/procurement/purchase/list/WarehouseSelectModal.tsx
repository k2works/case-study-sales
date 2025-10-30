import React from "react";
import Modal from "react-modal";
import { WarehouseCollectionSelectView } from "../../../../views/master/inventory/warehouse/WarehouseSelect.tsx";
import { useWarehouseContext } from "../../../../providers/master/Warehouse.tsx";
import { usePurchaseContext } from "../../../../providers/procurement/Purchase.tsx";
import { WarehouseType } from "../../../../models/master/warehouse.ts";

type WarehouseSelectModalProps = {
    type: "edit" | "search";
};

export const WarehouseSelectModal: React.FC<WarehouseSelectModalProps> = ({ type }) => {
    const {
        newPurchase,
        setNewPurchase,
        selectedLineIndex,
    } = usePurchaseContext();

    const {
        modalIsOpen: warehouseModalIsOpen,
        setModalIsOpen: setWarehouseModalIsOpen,
        warehouses,
        fetchWarehouses,
        pageNation: warehousePageNation,
    } = useWarehouseContext();

    // 編集モーダルを閉じる
    const handleCloseEditModal = () => {
        setWarehouseModalIsOpen(false);
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
                    if (selectedLineIndex !== null) {
                        const newLines = [...newPurchase.purchaseLines];
                        newLines[selectedLineIndex] = {
                            ...newLines[selectedLineIndex],
                            warehouseCode: warehouse.warehouseCode,
                        };
                        setNewPurchase({
                            ...newPurchase,
                            purchaseLines: newLines
                        });
                    }
                    setWarehouseModalIsOpen(false);
                }}
                handleClose={handleCloseEditModal}
                pageNation={warehousePageNation}
                fetchWarehouses={fetchWarehouses.load}
            />
        </Modal>
    );

    return (
        <>
            {type === "edit" ? warehouseEditModalView() : null}
        </>
    );
};
