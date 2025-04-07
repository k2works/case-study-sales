import React from 'react';
import { Message } from "../../../../components/application/Message.tsx";
import { ShippingType } from "../../../../models/shipping/shipping";
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
    handleDepartmentSelect,
    handleEmployeeSelect,
    handleCustomerSelect,
    handleProductSelect
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
                label="受注日"
                id="orderDate"
                type="date"
                className="single-view-content-item-form-item-input"
                value={convertToDateInputFormat(newShipping.orderDate)}
                onChange={(e) => setNewShipping({
                    ...newShipping,
                    orderDate: e.target.value
                })}
            />
            <FormInput
                label="部門コード"
                id="departmentCode"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newShipping.departmentCode}
                onChange={(e) => setNewShipping({
                    ...newShipping,
                    departmentCode: e.target.value
                })}
                onClick={handleDepartmentSelect}
            />
            <FormInput
                label="部門開始日"
                id="departmentStartDate"
                type="date"
                className="single-view-content-item-form-item-input"
                value={convertToDateInputFormat(newShipping.departmentStartDate)}
                onChange={(e) => setNewShipping({
                    ...newShipping,
                    departmentStartDate: e.target.value
                })}
                disabled={true}
            />
            <FormInput
                label="顧客コード"
                id="customerCode"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newShipping.customerCode}
                onChange={(e) => setNewShipping({
                    ...newShipping,
                    customerCode: e.target.value
                })}
                onClick={handleCustomerSelect}
            />
            <FormInput
                label="顧客枝番"
                id="customerBranchNumber"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newShipping.customerBranchNumber}
                onChange={(e) => setNewShipping({
                    ...newShipping,
                    customerBranchNumber: Number(e.target.value)
                })}
                disabled={true}
            />
            <FormInput
                label="社員コード"
                id="employeeCode"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newShipping.employeeCode}
                onChange={(e) => setNewShipping({
                    ...newShipping,
                    employeeCode: e.target.value
                })}
                onClick={handleEmployeeSelect}
            />
            <FormInput
                label="希望納期"
                id="desiredDeliveryDate"
                type="date"
                className="single-view-content-item-form-item-input"
                value={convertToDateInputFormat(newShipping.desiredDeliveryDate)}
                onChange={(e) => setNewShipping({
                    ...newShipping,
                    desiredDeliveryDate: e.target.value
                })}
            />
            <FormInput
                label="客先注文番号"
                id="customerOrderNumber"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newShipping.customerOrderNumber}
                onChange={(e) => setNewShipping({
                    ...newShipping,
                    customerOrderNumber: e.target.value
                })}
            />
            <FormInput
                label="倉庫コード"
                id="warehouseCode"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newShipping.warehouseCode}
                onChange={(e) => setNewShipping({
                    ...newShipping,
                    warehouseCode: e.target.value
                })}
            />
            <FormInput
                label="受注金額合計"
                id="totalOrderAmount"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newShipping.totalOrderAmount}
                disabled={true}
                onChange={() =>{}}
            />
            <FormInput
                label="消費税合計"
                id="totalConsumptionTax"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newShipping.totalConsumptionTax}
                disabled={true}
                onChange={() =>{}}
            />
            <FormInput
                label="備考"
                id="remarks"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newShipping.remarks}
                onChange={(e) => setNewShipping({
                    ...newShipping,
                    remarks: e.target.value
                })}
            />
            <FormInput
                label="明細番号"
                id="orderLineNumber"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newShipping.orderLineNumber}
                onChange={(e) => setNewShipping({
                    ...newShipping,
                    orderLineNumber: Number(e.target.value)
                })}
                disabled={true}
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
                onClick={handleProductSelect}
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
                label="販売単価"
                id="salesUnitPrice"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newShipping.salesUnitPrice}
                onChange={(e) => setNewShipping({
                    ...newShipping,
                    salesUnitPrice: Number(e.target.value)
                })}
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
            />
            <FormInput
                label="税率"
                id="taxRate"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newShipping.taxRate}
                onChange={(e) => setNewShipping({
                    ...newShipping,
                    taxRate: Number(e.target.value)
                })}
            />
            <FormInput
                label="引当数量"
                id="allocationQuantity"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newShipping.allocationQuantity}
                onChange={(e) => setNewShipping({
                    ...newShipping,
                    allocationQuantity: Number(e.target.value)
                })}
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
                label="完了フラグ"
                id="completionFlag"
                type="checkbox"
                className="single-view-content-item-form-item-input"
                checked={newShipping.completionFlag}
                onChange={(e) => setNewShipping({
                    ...newShipping,
                    completionFlag: e.target.checked
                })}
            />
            <FormInput
                label="値引金額"
                id="discountAmount"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newShipping.discountAmount}
                onChange={(e) => setNewShipping({
                    ...newShipping,
                    discountAmount: Number(e.target.value)
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
            />
            <FormInput
                label="売上金額"
                id="salesAmount"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newShipping.salesAmount}
                onChange={(e) => setNewShipping({
                    ...newShipping,
                    salesAmount: Number(e.target.value)
                })}
            />
            <FormInput
                label="消費税額"
                id="consumptionTaxAmount"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newShipping.consumptionTaxAmount}
                onChange={(e) => setNewShipping({
                    ...newShipping,
                    consumptionTaxAmount: Number(e.target.value)
                })}
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
