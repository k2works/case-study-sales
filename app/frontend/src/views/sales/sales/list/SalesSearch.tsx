import React, { MouseEventHandler } from "react";
import { FormInput, SingleViewHeaderItem } from "../../../Common.tsx";
import { SalesCriteriaType } from "../../../../models/sales/sales";

interface FormProps {
    criteria: SalesCriteriaType,
    setCondition: (criteria: SalesCriteriaType) => void,
    handleClick: (e: React.MouseEvent<HTMLButtonElement>) => void,
    handleClose: (e: React.MouseEvent<HTMLButtonElement>) => void
    handleDepartmentSelect: () => void;
}

const Form = ({criteria, setCondition, handleClick, handleClose, handleDepartmentSelect}: FormProps) => {
    return (
        <div className="single-view-content-item-form">
            <FormInput
                id={"search-sales-number"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"売上番号"}
                value={criteria.salesNumber}
                onChange={(e) => setCondition(
                    {...criteria, salesNumber: e.target.value}
                )}/>
            <FormInput
                id={"search-order-number"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"受注番号"}
                value={criteria.orderNumber}
                onChange={(e) => setCondition(
                    {...criteria, orderNumber: e.target.value}
                )}/>
            <FormInput
                id={"search-sales-date"}
                type="date"
                className="single-view-content-item-form-item-input"
                label={"売上日"}
                value={criteria.salesDate}
                onChange={(e) => setCondition(
                    {...criteria, salesDate: e.target.value}
                )}/>
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
            <FormInput
                id={"search-remarks"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"備考"}
                value={criteria.remarks}
                onChange={(e) => setCondition(
                    {...criteria, remarks: e.target.value}
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

interface SalesSearchSingleViewProps {
    criteria: SalesCriteriaType,
    setCondition: (criteria: SalesCriteriaType) => void,
    handleSelect: (criteria: SalesCriteriaType) => Promise<void>,
    handleClose: () => void
    handleDepartmentSelect: () => void;
}

export const SalesSearchSingleView: React.FC<SalesSearchSingleViewProps> = ({
    criteria,
    setCondition,
    handleSelect,
    handleClose,
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
                    <SingleViewHeaderItem title={"売上"} subtitle={"検索"}/>
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
                            handleDepartmentSelect={handleDepartmentSelect}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
};