import React, {MouseEventHandler} from "react";
import {FormInput, SingleViewHeaderItem} from "../../../Common.tsx";
import {WarehouseSearchCriteriaType} from "../../../../models/master/warehouse.ts";

interface FormProps {
    criteria: WarehouseSearchCriteriaType,
    setCondition:(criteria: WarehouseSearchCriteriaType) => void,
    handleClick: (e: React.MouseEvent<HTMLButtonElement>) => void,
    handleClose: (e: React.MouseEvent<HTMLButtonElement>) => void,
    onClearSearch: () => void
}

const Form = ({criteria, setCondition, handleClick, handleClose, onClearSearch}: FormProps) => {
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
                )}/>
            <FormInput
                id={"search-warehouse-name"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"倉庫名"}
                value={criteria.warehouseName || ""}
                onChange={(e) => setCondition(
                    {...criteria, warehouseName: e.target.value}
                )}/>

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

interface WarehouseSearchSingleViewProps {
    criteria: WarehouseSearchCriteriaType,
    setCondition:(criteria: WarehouseSearchCriteriaType) => void,
    handleSelect: () => Promise<void>,
    handleClose: () => void,
    onClearSearch: () => void
}

export const WarehouseSearchSingleView: React.FC<WarehouseSearchSingleViewProps> = ({
                                                                                  criteria,
                                                                                  setCondition,
                                                                                  handleSelect,
                                                                                  handleClose,
                                                                                  onClearSearch,
                                                                              }) => {
    const handleClick: MouseEventHandler<HTMLButtonElement> = async(e) => {
        e.preventDefault();
        await handleSelect();
    }

    const handleCancel: MouseEventHandler<HTMLButtonElement> = (e) => {
        e.preventDefault();
        handleClose();
    }

    return (
        <div className="single-view-object-container">
            <div className="single-view-header">
                <div>
                    <SingleViewHeaderItem title={"倉庫"} subtitle={"検索"}/>
                </div>
            </div>
            <div className="single-view-container">
                <div className="single-view-content">
                    <div className="single-view-content-item">
                        <Form
                            criteria={criteria}
                            setCondition={setCondition}
                            handleClick={handleClick}
                            handleClose={handleCancel}
                            onClearSearch={onClearSearch}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
};