import React, { MouseEventHandler } from "react";
import { FormInput, SingleViewHeaderItem } from "../../../Common.tsx";
import { SalesOrderCriteriaType } from "../../../../models/sales/sales_order";

interface FormProps {
    criteria: SalesOrderCriteriaType,
    setCondition: (criteria: SalesOrderCriteriaType) => void,
    handleClick: (e: React.MouseEvent<HTMLButtonElement>) => void,
    handleClose: (e: React.MouseEvent<HTMLButtonElement>) => void
    handleDepartmentSelect: () => void;
    handleCustomerSelect: () => void;
    handleEmployeeSelect: () => void;
}

const Form = ({criteria, setCondition, handleClick, handleClose, handleDepartmentSelect, handleEmployeeSelect, handleCustomerSelect}: FormProps) => {
    return (
        <div className="single-view-content-item-form">
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
                id={"search-order-date"}
                type="date"
                className="single-view-content-item-form-item-input"
                label={"受注日"}
                value={criteria.orderDate}
                onChange={(e) => setCondition(
                    {...criteria, orderDate: e.target.value}
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
                id={"search-customer-code"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"顧客コード"}
                value={criteria.customerCode}
                onChange={(e) => setCondition(
                    {...criteria, customerCode: e.target.value}
                )}
                onClick={handleCustomerSelect}
            />
            <FormInput
                id={"search-employee-code"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"社員コード"}
                value={criteria.employeeCode}
                onChange={(e) => setCondition(
                    {...criteria, employeeCode: e.target.value}
                )}
                onClick={handleEmployeeSelect}
            />
            <FormInput
                id={"search-customer-order-number"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"客先注文番号"}
                value={criteria.customerOrderNumber}
                onChange={(e) => setCondition(
                    {...criteria, customerOrderNumber: e.target.value}
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

interface SalesOrderSearchSingleViewProps {
    criteria: SalesOrderCriteriaType,
    setCondition: (criteria: SalesOrderCriteriaType) => void,
    handleSelect: (criteria: SalesOrderCriteriaType) => Promise<void>,
    handleClose: () => void
    handleDepartmentSelect: () => void;
    handleCustomerSelect: () => void;
    handleEmployeeSelect: () => void;
}

export const SalesOrderSearchSingleView: React.FC<SalesOrderSearchSingleViewProps> = ({
    criteria,
    setCondition,
    handleSelect,
    handleClose,
    handleDepartmentSelect,
    handleCustomerSelect,
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
                    <SingleViewHeaderItem title={"受注"} subtitle={"検索"}/>
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
                            handleCustomerSelect={handleCustomerSelect}
                            handleEmployeeSelect={handleEmployeeSelect}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
};