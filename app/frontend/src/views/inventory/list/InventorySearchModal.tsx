import React, { MouseEventHandler } from "react";
import { FormInput, SingleViewHeaderItem } from "../../Common.tsx";
import { InventorySearchCriteriaType, StockCategoryEnumType, QualityCategoryEnumType } from "../../../models/inventory/inventory.ts";
import "./Inventory.css";

interface FormProps {
    criteria: InventorySearchCriteriaType,
    setCondition: (criteria: InventorySearchCriteriaType) => void,
    handleClick: (e: React.MouseEvent<HTMLButtonElement>) => void,
    handleClose: (e: React.MouseEvent<HTMLButtonElement>) => void,
    onClearSearch: () => void,
    handleWarehouseSelect: () => void,
    handleProductSelect: () => void
}

const Form = ({criteria, setCondition, handleClick, handleClose, onClearSearch, handleWarehouseSelect, handleProductSelect}: FormProps) => {
    return (
        <div className="single-view-content-item-form">
            <FormInput
                id={"search-warehouse-code"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"倉庫コード"}
                value={criteria.warehouseCode || ""}
                onChange={(e) => setCondition(
                    {...criteria, warehouseCode: e.target.value}
                )}
                onClick={handleWarehouseSelect}
            />
            <FormInput
                id={"search-product-code"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"商品コード"}
                value={criteria.productCode || ""}
                onChange={(e) => setCondition(
                    {...criteria, productCode: e.target.value}
                )}
                onClick={handleProductSelect}
            />
            <FormInput
                id={"search-lot-number"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"ロット番号"}
                value={criteria.lotNumber || ""}
                onChange={(e) => setCondition(
                    {...criteria, lotNumber: e.target.value}
                )}/>
            <div className="single-view-content-item-form-item">
                <label htmlFor="search-stock-category" className="single-view-content-item-form-item-label">
                    在庫区分
                </label>
                <select
                    id="search-stock-category"
                    className="single-view-content-item-form-item-input"
                    value={criteria.stockCategory || ""}
                    onChange={(e) => setCondition({
                        ...criteria,
                        stockCategory: e.target.value
                    })}
                >
                    <option value="">選択してください</option>
                    <option value={StockCategoryEnumType.通常在庫}>通常在庫</option>
                    <option value={StockCategoryEnumType.安全在庫}>安全在庫</option>
                    <option value={StockCategoryEnumType.廃棄予定}>廃棄予定</option>
                </select>
            </div>
            <div className="single-view-content-item-form-item">
                <label htmlFor="search-quality-category" className="single-view-content-item-form-item-label">
                    良品区分
                </label>
                <select
                    id="search-quality-category"
                    className="single-view-content-item-form-item-input"
                    value={criteria.qualityCategory || ""}
                    onChange={(e) => setCondition({
                        ...criteria,
                        qualityCategory: e.target.value
                    })}
                >
                    <option value="">選択してください</option>
                    <option value={QualityCategoryEnumType.良品}>良品</option>
                    <option value={QualityCategoryEnumType.不良品}>不良品</option>
                    <option value={QualityCategoryEnumType.返品}>返品</option>
                </select>
            </div>

            <div className="button-container">
                <button className="action-button" id="search-all" onClick={handleClick}>
                    検索
                </button>
                <button className="action-button" onClick={() => onClearSearch()} id="clear">
                    クリア
                </button>
                <button className="action-button" onClick={handleClose} id="cancel">
                    キャンセル
                </button>
            </div>
        </div>
    )
};

interface InventorySearchModalViewProps {
    isOpen: boolean;
    onClose: () => void;
    searchCriteria: InventorySearchCriteriaType;
    setSearchCriteria: (criteria: InventorySearchCriteriaType) => void;
    onSearch: () => Promise<void>;
    onClearSearch: () => void;
    handleWarehouseSelect: () => void;
    handleProductSelect: () => void;
}

export const InventorySearchModalView: React.FC<InventorySearchModalViewProps> = ({
    onClose,
    searchCriteria,
    setSearchCriteria,
    onSearch,
    onClearSearch,
    handleWarehouseSelect,
    handleProductSelect,
}) => {
    const handleClick: MouseEventHandler<HTMLButtonElement> = async(e) => {
        e.preventDefault();
        await onSearch();
        onClose();
    }

    const handleCancel: MouseEventHandler<HTMLButtonElement> = (e) => {
        e.preventDefault();
        onClose();
    }

    return (
        <div className="single-view-object-container">
            <div className="single-view-header">
                <div>
                    <SingleViewHeaderItem title={"在庫"} subtitle={"詳細検索"}/>
                </div>
            </div>
            <div className="single-view-container">
                <div className="single-view-content">
                    <div className="single-view-content-item">
                        <Form
                            criteria={searchCriteria}
                            setCondition={setSearchCriteria}
                            handleClick={handleClick}
                            handleClose={handleCancel}
                            onClearSearch={onClearSearch}
                            handleWarehouseSelect={handleWarehouseSelect}
                            handleProductSelect={handleProductSelect}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
};