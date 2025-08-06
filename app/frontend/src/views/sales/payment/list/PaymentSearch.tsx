import React, { MouseEventHandler } from "react";
import {FormInput, FormSelect, SingleViewHeaderItem} from "../../../Common.tsx";
import {PaymentCriteriaType, PaymentMethodType} from "../../../../models/sales/payment";

interface FormProps {
    criteria: PaymentCriteriaType,
    setCondition: (criteria: PaymentCriteriaType) => void,
    handleClick: (e: React.MouseEvent<HTMLButtonElement>) => void,
    handleClose: (e: React.MouseEvent<HTMLButtonElement>) => void
    handleDepartmentSelect: () => void;
    handleCustomerSelect: () => void;
}

const Form = ({criteria, setCondition, handleClick, handleClose, handleDepartmentSelect, handleCustomerSelect}: FormProps) => {
    return (
        <div className="single-view-content-item-form">
            <FormInput
                id={"search-payment-number"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"入金番号"}
                value={criteria.paymentNumber}
                onChange={(e) => setCondition(
                    {...criteria, paymentNumber: e.target.value}
                )}/>
            <FormInput
                id={"search-payment-date"}
                type="date"
                className="single-view-content-item-form-item-input"
                label={"入金日"}
                value={criteria.paymentDate}
                onChange={(e) => setCondition(
                    {...criteria, paymentDate: e.target.value}
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
            <FormSelect
                label="支払方法区分"
                id="paymentMethodType"
                value={criteria.paymentMethodType}
                options={PaymentMethodType}
                onChange={(e) => setCondition(
                    {...criteria, paymentMethodType: e}
                )}
            />
            <FormInput
                id={"search-payment-account-code"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"入金口座コード"}
                value={criteria.paymentAccountCode}
                onChange={(e) => setCondition(
                    {...criteria, paymentAccountCode: e.target.value}
                )}
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

interface PaymentSearchSingleViewProps {
    criteria: PaymentCriteriaType,
    setCondition: (criteria: PaymentCriteriaType) => void,
    handleSelect: (criteria: PaymentCriteriaType) => Promise<void>,
    handleClose: () => void
    handleDepartmentSelect: () => void;
    handleCustomerSelect: () => void;
}

export const PaymentSearchSingleView: React.FC<PaymentSearchSingleViewProps> = ({
    criteria,
    setCondition,
    handleSelect,
    handleClose,
    handleDepartmentSelect,
    handleCustomerSelect,
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
                    <SingleViewHeaderItem title={"入金"} subtitle={"検索"}/>
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
                        />
                    </div>
                </div>
            </div>
        </div>
    );
};