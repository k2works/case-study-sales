import React from 'react';
import { Message } from "../../../../components/application/Message.tsx";
import {PaymentMethodType, PaymentType} from "../../../../models/sales/payment";
import { convertToDateInputFormat } from "../../../../components/application/utils.ts";
import {FormInput, FormSelect, SingleViewHeaderActions, SingleViewHeaderItem} from "../../../Common.tsx";

interface HeaderProps {
    title: string;
    subtitle: string;
    isEditing: boolean;
    handleCreateOrUpdatePayment: () => void;
    handleCloseModal: () => void;
}

const Header = ({
    title,
    subtitle,
    isEditing,
    handleCreateOrUpdatePayment,
    handleCloseModal
}: HeaderProps) => (
    <div>
        <SingleViewHeaderItem title={title} subtitle={subtitle}/>
        <SingleViewHeaderActions
            isEditing={isEditing}
            handleCreateOrUpdateUser={handleCreateOrUpdatePayment}
            handleCloseModal={handleCloseModal}
        />
    </div>
);

interface FormProps {
    isEditing: boolean;
    newPayment: PaymentType;
    setNewPayment: React.Dispatch<React.SetStateAction<PaymentType>>;
    handleDepartmentSelect: () => void;
    handleCustomerSelect: () => void;
}

const Form = ({
    isEditing,
    newPayment,
    setNewPayment,
    handleDepartmentSelect,
    handleCustomerSelect
}: FormProps) => {
    return (
        <div className="single-view-content-item-form">
            <FormInput
                label="入金番号"
                id="paymentNumber"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newPayment.paymentNumber}
                onChange={(e) => setNewPayment({
                    ...newPayment,
                    paymentNumber: e.target.value
                })}
                disabled={isEditing}
            />
            <FormInput
                label="入金日"
                id="paymentDate"
                type="date"
                className="single-view-content-item-form-item-input"
                value={convertToDateInputFormat(newPayment.paymentDate)}
                onChange={(e) => setNewPayment({
                    ...newPayment,
                    paymentDate: e.target.value
                })}
            />
            <div className="single-view-content-item-form-item">
                <label htmlFor="departmentCode" className="single-view-content-item-form-item-label">部門コード</label>
                <div className="single-view-content-item-form-item-input-with-button">
                    <input
                        id="departmentCode"
                        type="text"
                        className="single-view-content-item-form-item-input"
                        value={newPayment.departmentCode}
                        onChange={(e) => setNewPayment({
                            ...newPayment,
                            departmentCode: e.target.value
                        })}
                    />
                    <button
                        type="button"
                        className="single-view-content-item-form-item-button"
                        onClick={handleDepartmentSelect}
                    >
                        選択
                    </button>
                </div>
            </div>
            <FormInput
                label="部門開始日"
                id="departmentStartDate"
                type="date"
                className="single-view-content-item-form-item-input"
                value={convertToDateInputFormat(newPayment.departmentStartDate)}
                onChange={(e) => setNewPayment({
                    ...newPayment,
                    departmentStartDate: e.target.value
                })}
            />
            <div className="single-view-content-item-form-item">
                <label htmlFor="customerCode" className="single-view-content-item-form-item-label">顧客コード</label>
                <div className="single-view-content-item-form-item-input-with-button">
                    <input
                        id="customerCode"
                        type="text"
                        className="single-view-content-item-form-item-input"
                        value={newPayment.customerCode}
                        onChange={(e) => setNewPayment({
                            ...newPayment,
                            customerCode: e.target.value
                        })}
                    />
                    <button
                        type="button"
                        className="single-view-content-item-form-item-button"
                        onClick={handleCustomerSelect}
                    >
                        選択
                    </button>
                </div>
            </div>
            <FormInput
                label="顧客枝番"
                id="customerBranchNumber"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newPayment.customerBranchNumber}
                onChange={(e) => setNewPayment({
                    ...newPayment,
                    customerBranchNumber: Number(e.target.value)
                })}
            />
            <FormSelect
                label="支払方法区分"
                id="paymentMethodType"
                value={newPayment.paymentMethodType}
                options={PaymentMethodType}
                onChange={(e) => setNewPayment({
                    ...newPayment,
                    paymentMethodType: e
                })}
            />
            <FormInput
                label="入金口座コード"
                id="paymentAccountCode"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newPayment.paymentAccountCode}
                onChange={(e) => setNewPayment({
                    ...newPayment,
                    paymentAccountCode: e.target.value
                })}
            />
            <FormInput
                label="入金金額"
                id="paymentAmount"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newPayment.paymentAmount}
                onChange={(e) => setNewPayment({
                    ...newPayment,
                    paymentAmount: Number(e.target.value)
                })}
            />
            <FormInput
                label="消込金額"
                id="offsetAmount"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newPayment.offsetAmount}
                onChange={(e) => setNewPayment({
                    ...newPayment,
                    offsetAmount: Number(e.target.value)
                })}
            />
        </div>
    );
};

interface PaymentSingleViewProps {
    error: string | null;
    message: string | null;
    isEditing: boolean;
    newPayment: PaymentType;
    setNewPayment: React.Dispatch<React.SetStateAction<PaymentType>>;
    handleCreateOrUpdatePayment: () => void;
    handleCloseModal: () => void;
    handleDepartmentSelect: () => void;
    handleCustomerSelect: () => void;
}

export const PaymentSingleView: React.FC<PaymentSingleViewProps> = ({
    error,
    message,
    isEditing,
    newPayment,
    setNewPayment,
    handleCreateOrUpdatePayment,
    handleCloseModal,
    handleDepartmentSelect,
    handleCustomerSelect,
}) => {
    return (
        <div className="single-view-object-container">
            <Message error={error} message={message}/>
            <div className="single-view-header">
                <Header
                    title="入金"
                    subtitle={isEditing ? "編集" : "新規作成"}
                    isEditing={isEditing}
                    handleCreateOrUpdatePayment={handleCreateOrUpdatePayment}
                    handleCloseModal={handleCloseModal}
                />
            </div>
            <div className="single-view-container">
                <div className="single-view-content">
                    <div className="single-view-content-item">
                        <Form
                            isEditing={isEditing}
                            newPayment={newPayment}
                            setNewPayment={setNewPayment}
                            handleDepartmentSelect={handleDepartmentSelect}
                            handleCustomerSelect={handleCustomerSelect}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
};