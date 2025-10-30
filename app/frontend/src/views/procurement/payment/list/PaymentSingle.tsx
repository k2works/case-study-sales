import React from 'react';
import { Message } from "../../../../components/application/Message.tsx";
import { PaymentType } from "../../../../models/procurement/payment.ts";
import { convertToDateInputFormat } from "../../../../components/application/utils.ts";
import { FormInput, SingleViewHeaderActions, SingleViewHeaderItem } from "../../../Common.tsx";

// 日付を整数からYYYY-MM-DD形式に変換
const formatDateFromInteger = (dateInt: number | string): string => {
    const dateStr = String(dateInt);
    if (dateStr.length !== 8) return dateStr;
    return `${dateStr.slice(0, 4)}-${dateStr.slice(4, 6)}-${dateStr.slice(6, 8)}`;
};

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
    handleVendorSelect: () => void;
}

const Form = ({
                  isEditing,
                  newPayment,
                  setNewPayment,
                  handleDepartmentSelect,
                  handleVendorSelect
              }: FormProps) => {

    return (
        <div className="single-view-content-item-form">
            <FormInput
                label="支払番号"
                id="paymentNumber"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newPayment.paymentNumber}
                onChange={(e) => setNewPayment({...newPayment, paymentNumber: e.target.value})}
                disabled={isEditing}
            />
            <FormInput
                label="支払日"
                id="paymentDate"
                type="date"
                className="single-view-content-item-form-item-input"
                value={typeof newPayment.paymentDate === 'number'
                    ? formatDateFromInteger(newPayment.paymentDate)
                    : convertToDateInputFormat(newPayment.paymentDate)}
                onChange={(e) => setNewPayment({...newPayment, paymentDate: e.target.value})}
            />
            <div className="single-view-content-item-form-item">
                <label htmlFor="departmentCode" className="single-view-content-item-form-item-label">部門コード</label>
                <div className="single-view-content-item-form-item-input-with-button">
                    <input
                        id="departmentCode"
                        type="text"
                        className="single-view-content-item-form-item-input"
                        value={newPayment.departmentCode}
                        onChange={(e) => setNewPayment({...newPayment, departmentCode: e.target.value})}
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
                onChange={(e) => setNewPayment({...newPayment, departmentStartDate: e.target.value})}
            />
            <div className="single-view-content-item-form-item">
                <label htmlFor="supplierCode" className="single-view-content-item-form-item-label">仕入先コード</label>
                <div className="single-view-content-item-form-item-input-with-button">
                    <input
                        id="supplierCode"
                        type="text"
                        className="single-view-content-item-form-item-input"
                        value={newPayment.supplierCode}
                        onChange={(e) => setNewPayment({...newPayment, supplierCode: e.target.value})}
                    />
                    <button
                        type="button"
                        className="single-view-content-item-form-item-button"
                        onClick={handleVendorSelect}
                    >
                        選択
                    </button>
                </div>
            </div>
            <FormInput
                label="仕入先枝番"
                id="supplierBranchNumber"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newPayment.supplierBranchNumber}
                onChange={(e) => setNewPayment({...newPayment, supplierBranchNumber: Number(e.target.value)})}
            />
            <FormInput
                label="支払方法区分"
                id="paymentMethodType"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newPayment.paymentMethodType}
                onChange={(e) => setNewPayment({...newPayment, paymentMethodType: Number(e.target.value)})}
            />
            <FormInput
                label="支払金額"
                id="paymentAmount"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newPayment.paymentAmount}
                onChange={(e) => setNewPayment({...newPayment, paymentAmount: Number(e.target.value)})}
            />
            <FormInput
                label="消費税合計"
                id="totalConsumptionTax"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newPayment.totalConsumptionTax}
                onChange={(e) => setNewPayment({...newPayment, totalConsumptionTax: Number(e.target.value)})}
            />
            <div className="single-view-content-item-form-item">
                <label htmlFor="paymentCompletedFlag" className="single-view-content-item-form-item-label">支払完了フラグ</label>
                <input
                    id="paymentCompletedFlag"
                    type="checkbox"
                    checked={newPayment.paymentCompletedFlag}
                    onChange={(e) => setNewPayment({...newPayment, paymentCompletedFlag: e.target.checked})}
                />
            </div>
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
    handleVendorSelect: () => void;
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
    handleVendorSelect,
}) => {
    return (
        <div className="single-view-object-container">
            <Message error={error} message={message}/>
            <div className="single-view-header">
                <Header
                    title="支払"
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
                            handleVendorSelect={handleVendorSelect}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
};
