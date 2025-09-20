import React from "react";
import { Message } from "../../../components/application/Message.tsx";
import { InventoryType, StockCategoryEnumType, QualityCategoryEnumType } from "../../../models/inventory/inventory.ts";
import { FormInput, SingleViewHeaderActions, SingleViewHeaderItem } from "../../Common.tsx";
import "./Inventory.css";

interface HeaderProps {
    title: string;
    subtitle: string;
    isEditing: boolean;
    onSave: () => void;
    onClose: () => void;
}

const Header = ({
    title,
    subtitle,
    isEditing,
    onSave,
    onClose
}: HeaderProps) => (
    <div>
        <SingleViewHeaderItem title={title} subtitle={subtitle}/>
        <SingleViewHeaderActions
            isEditing={isEditing}
            handleCreateOrUpdateUser={onSave}
            handleCloseModal={onClose}
        />
    </div>
);

interface FormProps {
    isEditing: boolean;
    inventory: InventoryType;
    setInventory: React.Dispatch<React.SetStateAction<InventoryType>>;
    handleWarehouseSelect?: () => void;
    handleProductSelect?: () => void;
}

const Form = ({ isEditing, inventory, setInventory, handleWarehouseSelect, handleProductSelect }: FormProps) => {
    return (
        <div className="single-view-content-item-form">
            <FormInput
                label="倉庫コード"
                id="warehouseCode"
                type="text"
                className="single-view-content-item-form-item-input"
                value={inventory.warehouseCode}
                onChange={(e) => setInventory({
                    ...inventory,
                    warehouseCode: e.target.value
                })}
                onClick={handleWarehouseSelect}
                disabled={isEditing}
            />
            <FormInput
                label="倉庫名"
                id="warehouseName"
                type="text"
                className="single-view-content-item-form-item-input"
                value={inventory.warehouseName || ''}
                onChange={(e) => setInventory({
                    ...inventory,
                    warehouseName: e.target.value
                })}
                disabled={isEditing}
            />
            <FormInput
                label="商品コード"
                id="productCode"
                type="text"
                className="single-view-content-item-form-item-input"
                value={inventory.productCode}
                onChange={(e) => setInventory({
                    ...inventory,
                    productCode: e.target.value
                })}
                onClick={handleProductSelect}
                disabled={isEditing}
            />
            <FormInput
                label="商品名"
                id="productName"
                type="text"
                className="single-view-content-item-form-item-input"
                value={inventory.productName || ''}
                onChange={(e) => setInventory({
                    ...inventory,
                    productName: e.target.value
                })}
                disabled={isEditing}
            />
            <FormInput
                label="ロット番号"
                id="lotNumber"
                type="text"
                className="single-view-content-item-form-item-input"
                value={inventory.lotNumber}
                onChange={(e) => setInventory({
                    ...inventory,
                    lotNumber: e.target.value
                })}
                disabled={isEditing}
            />
            <div className="single-view-content-item-form-item">
                <label htmlFor="stockCategory" className="single-view-content-item-form-item-label">
                    在庫区分
                </label>
                <select
                    id="stockCategory"
                    className="single-view-content-item-form-item-input"
                    value={inventory.stockCategory}
                    onChange={(e) => setInventory({
                        ...inventory,
                        stockCategory: e.target.value
                    })}
                    disabled={isEditing}
                >
                    <option value={StockCategoryEnumType.通常在庫}>通常在庫</option>
                    <option value={StockCategoryEnumType.安全在庫}>安全在庫</option>
                    <option value={StockCategoryEnumType.廃棄予定}>廃棄予定</option>
                </select>
            </div>
            <div className="single-view-content-item-form-item">
                <label htmlFor="qualityCategory" className="single-view-content-item-form-item-label">
                    良品区分
                </label>
                <select
                    id="qualityCategory"
                    className="single-view-content-item-form-item-input"
                    value={inventory.qualityCategory}
                    onChange={(e) => setInventory({
                        ...inventory,
                        qualityCategory: e.target.value
                    })}
                    disabled={isEditing}
                >
                    <option value={QualityCategoryEnumType.良品}>良品</option>
                    <option value={QualityCategoryEnumType.不良品}>不良品</option>
                    <option value={QualityCategoryEnumType.返品}>返品</option>
                </select>
            </div>
            <FormInput
                label="実在庫数"
                id="actualStockQuantity"
                type="number"
                className="single-view-content-item-form-item-input"
                value={inventory.actualStockQuantity}
                onChange={(e) => setInventory({
                    ...inventory,
                    actualStockQuantity: Number(e.target.value)
                })}
            />
            <FormInput
                label="有効在庫数"
                id="availableStockQuantity"
                type="number"
                className="single-view-content-item-form-item-input"
                value={inventory.availableStockQuantity}
                onChange={(e) => setInventory({
                    ...inventory,
                    availableStockQuantity: Number(e.target.value)
                })}
            />
            <FormInput
                label="最終出荷日"
                id="lastShipmentDate"
                type="date"
                className="single-view-content-item-form-item-input"
                value={inventory.lastShipmentDate || ''}
                onChange={(e) => setInventory({
                    ...inventory,
                    lastShipmentDate: e.target.value
                })}
            />
        </div>
    );
};

interface InventoryEditModalViewProps {
    isOpen: boolean;
    onClose: () => void;
    isEditing: boolean;
    inventory: InventoryType;
    setInventory: React.Dispatch<React.SetStateAction<InventoryType>>;
    onSave: () => void;
    error: string | null;
    message: string | null;
    handleWarehouseSelect?: () => void;
    handleProductSelect?: () => void;
}

export const InventoryEditModalView: React.FC<InventoryEditModalViewProps> = ({
    onClose,
    isEditing,
    inventory,
    setInventory,
    onSave,
    error,
    message,
    handleWarehouseSelect,
    handleProductSelect,
}) => {
    return (
        <div className="single-view-object-container">
            <Message error={error} message={message}/>
            <div className="single-view-header">
                <Header
                    title="在庫"
                    subtitle={isEditing ? "編集" : "新規登録"}
                    isEditing={isEditing}
                    onSave={onSave}
                    onClose={onClose}
                />
            </div>
            <div className="single-view-container">
                <div className="single-view-content">
                    <div className="single-view-content-item">
                        <Form
                            isEditing={isEditing}
                            inventory={inventory}
                            setInventory={setInventory}
                            handleWarehouseSelect={handleWarehouseSelect}
                            handleProductSelect={handleProductSelect}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
};