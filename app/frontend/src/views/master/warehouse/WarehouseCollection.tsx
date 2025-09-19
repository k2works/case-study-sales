import React from "react";
import {WarehouseCriteriaType, WarehouseType, WarehouseFetchType, WarehouseSearchCriteriaType} from "../../../models/master/warehouse.ts";
import {Message} from "../../../components/application/Message.tsx";
import {PageNation} from "../../application/PageNation.tsx";
import {Search} from "../../Common.tsx";

interface WarehouseItemProps {
    warehouse: WarehouseType;
    onEdit: (warehouse: WarehouseType) => void;
    onDelete: (warehouseCode: string) => void;
    onCheck: (warehouse: WarehouseType) => void;
}

const WarehouseItem: React.FC<WarehouseItemProps> = ({warehouse, onEdit, onDelete, onCheck}) => (
    <li className="collection-object-item" key={warehouse.warehouseCode}>
        <div className="collection-object-item-content" data-id={warehouse.warehouseCode}>
            <input type="checkbox" className="collection-object-item-checkbox" checked={warehouse.checked}
                   onChange={() => onCheck(warehouse)}/>
        </div>
        <div className="collection-object-item-content" data-id={warehouse.warehouseCode}>
            <div className="collection-object-item-content-details">倉庫コード</div>
            <div className="collection-object-item-content-name">{warehouse.warehouseCode}</div>
        </div>
        <div className="collection-object-item-content" data-id={warehouse.warehouseCode}>
            <div className="collection-object-item-content-details">倉庫名</div>
            <div className="collection-object-item-content-name">{warehouse.warehouseName}</div>
        </div>
        <div className="collection-object-item-actions" data-id={warehouse.warehouseCode}>
            <button className="action-button" onClick={() => onEdit(warehouse)} id="edit">編集</button>
        </div>
        <div className="collection-object-item-actions" data-id={warehouse.warehouseCode}>
            <button className="action-button" onClick={() => onDelete(warehouse.warehouseCode)} id="delete">削除
            </button>
        </div>
    </li>
);

interface WarehouseListProps {
    warehouses: WarehouseType[];
    onEdit: (warehouse: WarehouseType) => void;
    onDelete: (warehouseCode: string) => void;
    onCheck: (warehouse: WarehouseType) => void;
}

const WarehouseList: React.FC<WarehouseListProps> = ({warehouses, onEdit, onDelete, onCheck}) => (
    <div className="collection-object-container">
        <ul className="collection-object-list">
            {warehouses.map(warehouse => (
                <WarehouseItem
                    key={warehouse.warehouseCode}
                    warehouse={warehouse}
                    onEdit={onEdit}
                    onDelete={onDelete}
                    onCheck={onCheck}
                />
            ))}
        </ul>
    </div>
);

interface WarehouseCollectionViewProps {
    error: string | null;
    message: string | null;
    searchItems: {
        searchWarehouseCriteria: WarehouseSearchCriteriaType;
        setSearchWarehouseCriteria: (value: WarehouseSearchCriteriaType) => void;
        handleOpenSearchModal: () => void;
    }
    headerItems: {
        handleOpenModal: (warehouse?: WarehouseType) => void;
        handleCheckToggleCollection: () => void;
        handleDeleteCheckedCollection: () => void;
    }
    collectionItems: {
        warehouses: WarehouseType[];
        handleOpenModal: (warehouse?: WarehouseType) => void;
        handleDeleteWarehouse: (warehouseCode: string) => void;
        handleCheckWarehouse: (warehouse: WarehouseType) => void;
    }
    pageNationItems: {
        pageNation: WarehouseFetchType | null;
        criteria: WarehouseCriteriaType | null;
        fetchWarehouses: (pageNumber?: number, searchCriteria?: WarehouseCriteriaType) => Promise<void>;
    }
}

export const WarehouseCollectionView: React.FC<WarehouseCollectionViewProps> = ({
                                                                                      error,
                                                                                      message,
                                                                                      searchItems: {searchWarehouseCriteria, setSearchWarehouseCriteria, handleOpenSearchModal},
                                                                                      headerItems: {handleOpenModal, handleCheckToggleCollection, handleDeleteCheckedCollection},
                                                                                      collectionItems: { warehouses, handleDeleteWarehouse, handleCheckWarehouse },
                                                                                      pageNationItems: { pageNation, criteria, fetchWarehouses }
                                                                                  }) => (
    <div className="collection-view-object-container">
        <Message error={error} message={message}/>
        <div className="collection-view-container">
            <div className="collection-view-header">
                <div className="single-view-header-item">
                    <h1 className="single-view-title">倉庫</h1>
                </div>
            </div>
            <div className="collection-view-content">
                <Search
                    searchCriteria={searchWarehouseCriteria}
                    setSearchCriteria={setSearchWarehouseCriteria}
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
                <WarehouseList
                    warehouses={warehouses}
                    onEdit={handleOpenModal}
                    onDelete={handleDeleteWarehouse}
                    onCheck={handleCheckWarehouse}
                />
                <PageNation
                    pageNation={pageNation}
                    callBack={(page: number, criteria?: WarehouseCriteriaType) => fetchWarehouses(page, criteria)}
                    criteria={criteria || undefined}
                />
            </div>
        </div>
    </div>
);