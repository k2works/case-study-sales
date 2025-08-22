import React, { MouseEventHandler } from "react";
import { FormInput, SingleViewHeaderItem } from "../../../Common.tsx";
import { PurchaseOrderSearchCriteriaType } from "../../../../models/sales/purchaseOrder.ts";

interface FormProps {
    criteria: PurchaseOrderSearchCriteriaType,
    setCondition: (criteria: PurchaseOrderSearchCriteriaType) => void,
    handleClick: (e: React.MouseEvent<HTMLButtonElement>) => void,
    handleClose: (e: React.MouseEvent<HTMLButtonElement>) => void
    handleVendorSelect: () => void;
    handleEmployeeSelect: () => void;
}

const Form = ({criteria, setCondition, handleClick, handleClose, handleVendorSelect, handleEmployeeSelect}: FormProps) => {
    return (
        <div className="single-view-content-item-form">
            <FormInput
                id={"search-purchase-order-number"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"発注番号"}
                value={criteria.purchaseOrderNumber}
                onChange={(e) => setCondition(
                    {...criteria, purchaseOrderNumber: e.target.value}
                )}/>
            <FormInput
                id={"search-purchase-order-date"}
                type="date"
                className="single-view-content-item-form-item-input"
                label={"発注日"}
                value={criteria.purchaseOrderDate}
                onChange={(e) => setCondition(
                    {...criteria, purchaseOrderDate: e.target.value}
                )}/>
            <FormInput
                id={"search-order-number"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"受注番号"}
                value={criteria.salesOrderNumber}
                onChange={(e) => setCondition(
                    {...criteria, salesOrderNumber: e.target.value}
                )}/>
            <FormInput
                id={"search-vendor-code"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"仕入先コード"}
                value={criteria.supplierCode}
                onChange={(e) => setCondition(
                    {...criteria, supplierCode: e.target.value}
                )}
                onClick={handleVendorSelect}
            />
            <FormInput
                id={"search-purchase-order-manager-code"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"発注担当者コード"}
                value={criteria.purchaseManagerCode}
                onChange={(e) => setCondition(
                    {...criteria, purchaseManagerCode: e.target.value}
                )}
                onClick={handleEmployeeSelect}
            />
            <FormInput
                id={"search-specified-delivery-date"}
                type="date"
                className="single-view-content-item-form-item-input"
                label={"指定納期"}
                value={criteria.designatedDeliveryDate}
                onChange={(e) => setCondition(
                    {...criteria, designatedDeliveryDate: e.target.value}
                )}/>
            <FormInput
                id={"search-warehouse-code"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"倉庫コード"}
                value={criteria.warehouseCode}
                onChange={(e) => setCondition(
                    {...criteria, warehouseCode: e.target.value}
                )}/>

            <div className="button-container">
                <button className="action-button" id="search-all" onClick={handleClick}>
                    検索
                </button>
                <button className="action-button" onClick={handleClose} id="cancel">
                    キャンセル
                </button>
            </div>
        </div>
    )
};

interface PurchaseOrderSearchSingleViewProps {
    criteria: PurchaseOrderSearchCriteriaType,
    setCondition: (criteria: PurchaseOrderSearchCriteriaType) => void,
    handleSelect: (criteria: PurchaseOrderSearchCriteriaType) => Promise<void>,
    handleClose: () => void
    handleVendorSelect: () => void;
    handleEmployeeSelect: () => void;
}

export const PurchaseOrderSearchSingleView: React.FC<PurchaseOrderSearchSingleViewProps> = ({
    criteria,
    setCondition,
    handleSelect,
    handleClose,
    handleVendorSelect,
    handleEmployeeSelect,
}) => {
    const handleClick: MouseEventHandler<HTMLButtonElement> = async(e) => {
        e.preventDefault();
        await handleSelect(criteria);
        handleClose();
    }

    const handleCancel: MouseEventHandler<HTMLButtonElement> = (e) => {
        e.preventDefault();
        handleClose();
    }

    return (
        <div className="single-view-object-container">
            <div className="single-view-header">
                <div>
                    <SingleViewHeaderItem title={"発注"} subtitle={"検索"}/>
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
                            handleVendorSelect={handleVendorSelect}
                            handleEmployeeSelect={handleEmployeeSelect}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
};