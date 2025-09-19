import React from "react";
import { FaTimes } from "react-icons/fa";
import { PageNationType, PageNation } from "../../application/PageNation.tsx";
import { WarehouseType } from "../../../models/master/warehouse.ts";

interface WarehouseCollectionSelectProps {
    warehouses: WarehouseType[];
    handleSelect: (warehouse: WarehouseType) => void;
    handleClose: () => void;
    pageNation: PageNationType | null;
    fetchWarehouses: () => void;
}

export const WarehouseCollectionSelectView: React.FC<WarehouseCollectionSelectProps> = ({
                                                                                          warehouses,
                                                                                          handleSelect,
                                                                                          handleClose,
                                                                                          pageNation,
                                                                                          fetchWarehouses,
                                                                                      }) => {
    return (
        <div className="collection-view-object-container">
            <div className="collection-view-container">
                <button className="close-modal-button" onClick={handleClose}>
                    <FaTimes aria-hidden="true" />
                </button>
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h2 className="single-view-title">倉庫</h2>
                    </div>
                </div>
                <div className="collection-view-content">
                    <div className="collection-object-container-modal">
                        <ul className="collection-object-list">
                            {warehouses.map((warehouse) => (
                                <li className="collection-object-item" key={warehouse.warehouseCode}>
                                    <div className="collection-object-item-content"
                                         data-id={warehouse.warehouseCode}>
                                        <div className="collection-object-item-content-details">倉庫コード</div>
                                        <div className="collection-object-item-content-name">
                                            {warehouse.warehouseCode}
                                        </div>
                                    </div>
                                    <div className="collection-object-item-content"
                                         data-id={warehouse.warehouseCode}>
                                        <div className="collection-object-item-content-details">倉庫名</div>
                                        <div className="collection-object-item-content-name">
                                            {warehouse.warehouseName}
                                        </div>
                                    </div>
                                    <div className="collection-object-item-actions"
                                         data-id={warehouse.warehouseCode}>
                                        <button
                                            className="action-button"
                                            onClick={() => handleSelect(warehouse)}
                                            id="select-warehouse">
                                            選択
                                        </button>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>
                <PageNation pageNation={pageNation} callBack={fetchWarehouses}/>
            </div>
        </div>
    );
};