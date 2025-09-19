import React from "react";
import { InventoryType, InventorySearchCriteriaType, InventoryCriteriaType } from "../../../models/inventory/inventory.ts";
import { Message } from "../../../components/application/Message.tsx";
import { PageNation, PageNationType } from "../../application/PageNation.tsx";
import { Search } from "../../Common.tsx";
import "./Inventory.css";

interface InventoryItemProps {
    inventory: InventoryType;
    onEdit: (inventory: InventoryType) => void;
    onDelete: (warehouseCode: string, productCode: string, lotNumber: string, stockCategory: string, qualityCategory: string) => void;
    onCheck: (inventory: InventoryType) => void;
}

const InventoryItem: React.FC<InventoryItemProps> = ({inventory, onEdit, onDelete, onCheck}) => {
    const getStockCategoryLabel = (code: string) => {
        switch (code) {
            case "1": return "通常在庫";
            case "2": return "安全在庫";
            case "3": return "廃棄予定";
            default: return code;
        }
    };

    const getQualityCategoryLabel = (code: string) => {
        switch (code) {
            case "G": return "良品";
            case "B": return "不良品";
            case "R": return "返品";
            default: return code;
        }
    };

    return (
        <li className="collection-object-item" key={`${inventory.warehouseCode}-${inventory.productCode}-${inventory.lotNumber}-${inventory.stockCategory}-${inventory.qualityCategory}`}>
            <div className="collection-object-item-content" data-id={`${inventory.warehouseCode}-${inventory.productCode}-${inventory.lotNumber}-${inventory.stockCategory}-${inventory.qualityCategory}`}>
                <input type="checkbox" className="collection-object-item-checkbox" checked={inventory.checked}
                       onChange={() => onCheck(inventory)}/>
            </div>
            <div className="collection-object-item-content" data-id={`${inventory.warehouseCode}-${inventory.productCode}-${inventory.lotNumber}`}>
                <div className="collection-object-item-content-details">倉庫コード</div>
                <div className="collection-object-item-content-name">{inventory.warehouseCode}</div>
            </div>
            <div className="collection-object-item-content" data-id={`${inventory.warehouseCode}-${inventory.productCode}-${inventory.lotNumber}`}>
                <div className="collection-object-item-content-details">商品コード</div>
                <div className="collection-object-item-content-name">{inventory.productCode}</div>
            </div>
            <div className="collection-object-item-content" data-id={`${inventory.warehouseCode}-${inventory.productCode}-${inventory.lotNumber}`}>
                <div className="collection-object-item-content-details">ロット番号</div>
                <div className="collection-object-item-content-name">{inventory.lotNumber}</div>
            </div>
            <div className="collection-object-item-content" data-id={`${inventory.warehouseCode}-${inventory.productCode}-${inventory.lotNumber}`}>
                <div className="collection-object-item-content-details">在庫区分</div>
                <div className="collection-object-item-content-name">{getStockCategoryLabel(inventory.stockCategory)}</div>
            </div>
            <div className="collection-object-item-content" data-id={`${inventory.warehouseCode}-${inventory.productCode}-${inventory.lotNumber}`}>
                <div className="collection-object-item-content-details">実在庫数</div>
                <div className="collection-object-item-content-name">{inventory.actualStockQuantity?.toLocaleString()}</div>
            </div>
            <div className="collection-object-item-actions" data-id={`${inventory.warehouseCode}-${inventory.productCode}-${inventory.lotNumber}`}>
                <button className="action-button" onClick={() => onEdit(inventory)} id="edit">編集</button>
            </div>
            <div className="collection-object-item-actions" data-id={`${inventory.warehouseCode}-${inventory.productCode}-${inventory.lotNumber}`}>
                <button className="action-button" onClick={() => onDelete(inventory.warehouseCode, inventory.productCode, inventory.lotNumber, inventory.stockCategory, inventory.qualityCategory)} id="delete">削除</button>
            </div>
        </li>
    );
};

interface InventoryListProps {
    inventories: InventoryType[];
    onEdit: (inventory: InventoryType) => void;
    onDelete: (warehouseCode: string, productCode: string, lotNumber: string, stockCategory: string, qualityCategory: string) => void;
    onCheck: (inventory: InventoryType) => void;
}

const InventoryList: React.FC<InventoryListProps> = ({inventories, onEdit, onDelete, onCheck}) => (
    <div className="collection-object-container">
        <ul className="collection-object-list">
            {inventories.map(inventory => (
                <InventoryItem
                    key={`${inventory.warehouseCode}-${inventory.productCode}-${inventory.lotNumber}-${inventory.stockCategory}-${inventory.qualityCategory}`}
                    inventory={inventory}
                    onEdit={onEdit}
                    onDelete={onDelete}
                    onCheck={onCheck}
                />
            ))}
        </ul>
    </div>
);

interface InventoryCollectionViewProps {
    error: string | null;
    message: string | null;
    searchItems: {
        searchInventoryCriteria: InventorySearchCriteriaType;
        setSearchInventoryCriteria: (value: InventorySearchCriteriaType) => void;
        handleOpenSearchModal: () => void;
    }
    headerItems: {
        handleOpenModal: (inventory?: InventoryType) => void;
        handleCheckToggleCollection: () => void;
        handleDeleteCheckedCollection: () => void;
    }
    collectionItems: {
        inventories: InventoryType[];
        handleOpenModal: (inventory?: InventoryType) => void;
        handleDeleteInventory: (warehouseCode: string, productCode: string, lotNumber: string, stockCategory: string, qualityCategory: string) => void;
        handleCheckInventory: (inventory: InventoryType) => void;
    }
    pageNationItems: {
        pageNation: PageNationType | null;
        criteria: InventoryCriteriaType | null;
        fetchInventories: { load: (page?: number, criteria?: InventoryCriteriaType) => Promise<void> };
    }
}

export const InventoryCollectionView: React.FC<InventoryCollectionViewProps> = ({
    error,
    message,
    searchItems: {searchInventoryCriteria, setSearchInventoryCriteria, handleOpenSearchModal},
    headerItems: {handleOpenModal, handleCheckToggleCollection, handleDeleteCheckedCollection},
    collectionItems: { inventories, handleDeleteInventory, handleCheckInventory },
    pageNationItems: { pageNation, criteria, fetchInventories }
}) => (
    <div className="collection-view-object-container">
        <Message error={error} message={message}/>
        <div className="collection-view-container">
            <div className="collection-view-header">
                <div className="single-view-header-item">
                    <h1 className="single-view-title">在庫一覧</h1>
                </div>
            </div>
            <div className="collection-view-content">
                <Search
                    searchCriteria={searchInventoryCriteria}
                    setSearchCriteria={setSearchInventoryCriteria}
                    handleSearchAudit={handleOpenSearchModal}
                />
                <div className="button-container">
                    <button className="action-button" onClick={() => handleOpenModal()} id="new">
                        新規
                    </button>
                    <button className="action-button" onClick={() => handleCheckToggleCollection()} id="checkAll">
                        一括選択
                    </button>
                    <button className="action-button" onClick={() => handleDeleteCheckedCollection()} id="deleteAll">
                        一括削除
                    </button>
                </div>
                <InventoryList
                    inventories={inventories}
                    onEdit={handleOpenModal}
                    onDelete={handleDeleteInventory}
                    onCheck={handleCheckInventory}
                />
                <PageNation pageNation={pageNation} callBack={fetchInventories.load} criteria={criteria || undefined}/>
            </div>
        </div>
    </div>
);