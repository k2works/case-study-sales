import React, { MouseEventHandler } from "react";
import { FormInput, SingleViewHeaderItem } from "../../../Common.tsx";
import { PurchaseSearchCriteriaType } from "../../../../models/procurement/purchase.ts";

interface FormProps {
    criteria: PurchaseSearchCriteriaType,
    setCondition: (criteria: PurchaseSearchCriteriaType) => void,
    handleClick: (e: React.MouseEvent<HTMLButtonElement>) => void,
    handleClose: (e: React.MouseEvent<HTMLButtonElement>) => void
    handleVendorSelect: () => void;
    handleEmployeeSelect: () => void;
    handleDepartmentSelect: () => void;
}

const Form = ({
    criteria,
    setCondition,
    handleClick,
    handleClose,
    handleVendorSelect,
    handleEmployeeSelect,
    handleDepartmentSelect
}: FormProps) => {
    return (
        <div className="single-view-content-item-form">
            <FormInput
                id={"search-purchase-number"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"仕入番号"}
                value={criteria.purchaseNumber}
                onChange={(e) => setCondition(
                    {...criteria, purchaseNumber: e.target.value}
                )}/>
            <FormInput
                id={"search-purchase-date"}
                type="date"
                className="single-view-content-item-form-item-input"
                label={"仕入日"}
                value={criteria.purchaseDate}
                onChange={(e) => setCondition(
                    {...criteria, purchaseDate: e.target.value}
                )}/>
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
                id={"search-supplier-code"}
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
                id={"search-supplier-branch-number"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"仕入先枝番"}
                value={criteria.supplierBranchNumber}
                onChange={(e) => setCondition(
                    {...criteria, supplierBranchNumber: e.target.value}
                )}
            />
            <FormInput
                id={"search-purchase-manager-code"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"仕入担当者コード"}
                value={criteria.purchaseManagerCode}
                onChange={(e) => setCondition(
                    {...criteria, purchaseManagerCode: e.target.value}
                )}
                onClick={handleEmployeeSelect}
            />
            <FormInput
                id={"search-department-code"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"部門コード"}
                value={criteria.departmentCode}
                onChange={(e) => setCondition(
                    {...criteria, departmentCode: e.target.value}
                )}
                onClick={handleDepartmentSelect}
            />

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

interface PurchaseSearchSingleViewProps {
    criteria: PurchaseSearchCriteriaType,
    setCondition: (criteria: PurchaseSearchCriteriaType) => void,
    handleSelect: (criteria: PurchaseSearchCriteriaType) => Promise<void>,
    handleClose: () => void
    handleVendorSelect: () => void;
    handleEmployeeSelect: () => void;
    handleDepartmentSelect: () => void;
}

export const PurchaseSearchSingleView: React.FC<PurchaseSearchSingleViewProps> = ({
    criteria,
    setCondition,
    handleSelect,
    handleClose,
    handleVendorSelect,
    handleEmployeeSelect,
    handleDepartmentSelect,
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
                    <SingleViewHeaderItem title={"仕入"} subtitle={"検索"}/>
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
                            handleDepartmentSelect={handleDepartmentSelect}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
};
