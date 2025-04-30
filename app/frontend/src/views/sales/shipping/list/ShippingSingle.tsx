import React from 'react';
import { Message } from "../../../../components/application/Message.tsx";
import { ShippingType } from "../../../../models/sales/shipping";
import { convertToDateInputFormat } from "../../../../components/application/utils.ts";
import { FormInput, SingleViewHeaderActions, SingleViewHeaderItem } from "../../../Common.tsx";

interface HeaderProps {
    title: string;
    subtitle: string;
    isEditing: boolean;
    handleCreateOrUpdateShipping: () => void;
    handleCloseModal: () => void;
}

const Header = ({
    title,
    subtitle,
    isEditing,
    handleCreateOrUpdateShipping,
    handleCloseModal
}: HeaderProps) => (
    <div>
        <SingleViewHeaderItem title={title} subtitle={subtitle}/>
        <SingleViewHeaderActions
            isEditing={isEditing}
            handleCreateOrUpdateUser={handleCreateOrUpdateShipping}
            handleCloseModal={handleCloseModal}
        />
    </div>
);

interface FormProps {
    isEditing: boolean;
    newShipping: ShippingType;
    setNewShipping: React.Dispatch<React.SetStateAction<ShippingType>>;
    handleDepartmentSelect: () => void;
    handleEmployeeSelect: () => void;
    handleCustomerSelect: () => void;
    handleProductSelect: () => void;
}

const Form = ({
    isEditing,
    newShipping,
    setNewShipping,
}: FormProps) => {
    return (
        <div className="single-view-content-item-form">
            <FormInput
                label="受注番号"
                id="orderNumber"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newShipping.orderNumber}
                onChange={(e) => setNewShipping({
                    ...newShipping,
                    orderNumber: e.target.value
                })}
                disabled={isEditing}
            />
            <FormInput
                label="商品コード"
                id="productCode"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newShipping.productCode}
                onChange={(e) => setNewShipping({
                    ...newShipping,
                    productCode: e.target.value
                })}
                disabled={true}
            />
            <FormInput
                label="商品名"
                id="productName"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newShipping.productName}
                onChange={(e) => setNewShipping({
                    ...newShipping,
                    productName: e.target.value
                })}
                disabled={true}
            />
            <FormInput
                label="受注数量"
                id="orderQuantity"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newShipping.orderQuantity}
                onChange={(e) => setNewShipping({
                    ...newShipping,
                    orderQuantity: Number(e.target.value)
                })}
                disabled={true}
            />
            <FormInput
                label="出荷指示数量"
                id="shipmentInstructionQuantity"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newShipping.shipmentInstructionQuantity}
                onChange={(e) => setNewShipping({
                    ...newShipping,
                    shipmentInstructionQuantity: Number(e.target.value)
                })}
            />
            <FormInput
                label="出荷済数量"
                id="shippedQuantity"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newShipping.shippedQuantity}
                onChange={(e) => setNewShipping({
                    ...newShipping,
                    shippedQuantity: Number(e.target.value)
                })}
            />
            <FormInput
                label="納期"
                id="deliveryDate"
                type="date"
                className="single-view-content-item-form-item-input"
                value={convertToDateInputFormat(newShipping.deliveryDate)}
                onChange={(e) => setNewShipping({
                    ...newShipping,
                    deliveryDate: e.target.value
                })}
                disabled={true}
            />
        </div>
    );
};

interface ShippingSingleViewProps {
    error: string | null;
    message: string | null;
    isEditing: boolean;
    newShipping: ShippingType;
    setNewShipping: React.Dispatch<React.SetStateAction<ShippingType>>;
    handleCreateOrUpdateShipping: () => void;
    handleCloseModal: () => void;
    handleDepartmentSelect: () => void;
    handleEmployeeSelect: () => void;
    handleCustomerSelect: () => void;
    handleProductSelect: () => void;
}

export const ShippingSingleView: React.FC<ShippingSingleViewProps> = ({
    error,
    message,
    isEditing,
    newShipping,
    setNewShipping,
    handleCreateOrUpdateShipping,
    handleCloseModal,
    handleDepartmentSelect,
    handleEmployeeSelect,
    handleCustomerSelect,
    handleProductSelect,
}) => {
    return (
        <div className="single-view-object-container">
            <Message error={error} message={message}/>
            <div className="single-view-header">
                <Header
                    title="出荷"
                    subtitle={isEditing ? "編集" : "新規作成"}
                    isEditing={isEditing}
                    handleCreateOrUpdateShipping={handleCreateOrUpdateShipping}
                    handleCloseModal={handleCloseModal}
                />
            </div>
            <div className="single-view-container">
                <div className="single-view-content">
                    <div className="single-view-content-item">
                        <Form
                            isEditing={isEditing}
                            newShipping={newShipping}
                            setNewShipping={setNewShipping}
                            handleDepartmentSelect={handleDepartmentSelect}
                            handleEmployeeSelect={handleEmployeeSelect}
                            handleCustomerSelect={handleCustomerSelect}
                            handleProductSelect={handleProductSelect}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
};
