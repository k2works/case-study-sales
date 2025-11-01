import React, { MouseEventHandler } from "react";
import { FormInput, SingleViewHeaderItem } from "../../../Common.tsx";
import { PaymentSearchCriteriaType } from "../../../../models/procurement/payment.ts";

interface FormProps {
    criteria: PaymentSearchCriteriaType,
    setCondition: (criteria: PaymentSearchCriteriaType) => void,
    handleClick: (e: React.MouseEvent<HTMLButtonElement>) => void,
    handleClose: (e: React.MouseEvent<HTMLButtonElement>) => void
    handleVendorSelect: () => void;
    handleDepartmentSelect: () => void;
}

const Form = ({
                  criteria,
                  setCondition,
                  handleClick,
                  handleClose,
                  handleVendorSelect,
                  handleDepartmentSelect
              }: FormProps) => {
    return (
        <div className="single-view-content-item-form">
            <FormInput
                id={"search-payment-number"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"支払番号"}
                value={criteria.paymentNumber}
                onChange={(e) => setCondition(
                    {...criteria, paymentNumber: e.target.value}
                )}/>
            <FormInput
                id={"search-payment-date"}
                type="date"
                className="single-view-content-item-form-item-input"
                label={"支払日"}
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
                id={"search-payment-method-type"}
                type="text"
                className="single-view-content-item-form-item-input"
                label={"支払方法区分"}
                value={criteria.paymentMethodType}
                onChange={(e) => setCondition(
                    {...criteria, paymentMethodType: e.target.value}
                )}
            />
            <div className="single-view-content-item-form-item">
                <label htmlFor="search-payment-completed-flag" className="single-view-content-item-form-item-label">支払完了フラグ</label>
                <select
                    id="search-payment-completed-flag"
                    className="single-view-content-item-form-item-input"
                    value={criteria.paymentCompletedFlag || ""}
                    onChange={(e) => setCondition(
                        {...criteria, paymentCompletedFlag: e.target.value}
                    )}
                >
                    <option value="">すべて</option>
                    <option value="true">完了</option>
                    <option value="false">未完了</option>
                </select>
            </div>

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
    criteria: PaymentSearchCriteriaType,
    setCondition: (criteria: PaymentSearchCriteriaType) => void,
    handleSelect: (criteria: PaymentSearchCriteriaType) => Promise<void>,
    handleClose: () => void
    handleVendorSelect: () => void;
    handleDepartmentSelect: () => void;
}

export const PaymentSearchSingleView: React.FC<PaymentSearchSingleViewProps> = ({
                                                                                     criteria,
                                                                                     setCondition,
                                                                                     handleSelect,
                                                                                     handleClose,
                                                                                     handleVendorSelect,
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
                    <SingleViewHeaderItem title={"支払"} subtitle={"検索"}/>
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
                            handleDepartmentSelect={handleDepartmentSelect}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
};
