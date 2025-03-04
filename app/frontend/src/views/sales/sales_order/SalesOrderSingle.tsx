import React from 'react';
import { Message } from "../../../components/application/Message.tsx";
import { SalesOrderType, SalesOrderLineType } from "../../../models/sales/sales_order";
import { convertToDateInputFormat } from "../../../components/application/utils.ts";
import {FormInput, SingleViewHeaderActions, SingleViewHeaderItem} from "../../Common.tsx";
import "./SalesOrder.css";

interface HeaderProps {
    title: string;
    subtitle: string;
    isEditing: boolean;
    handleCreateOrUpdateSalesOrder: () => void;
    handleCloseModal: () => void;
}

const Header = ({
    title,
    subtitle,
    isEditing,
    handleCreateOrUpdateSalesOrder,
    handleCloseModal
}: HeaderProps) => (
    <div>
        <SingleViewHeaderItem title={title} subtitle={subtitle}/>
        <SingleViewHeaderActions
            isEditing={isEditing}
            handleCreateOrUpdateUser={handleCreateOrUpdateSalesOrder}
            handleCloseModal={handleCloseModal}
        />
    </div>
);


interface FormProps {
    isEditing: boolean;
    newSalesOrder: SalesOrderType;
    setNewSalesOrder: React.Dispatch<React.SetStateAction<SalesOrderType>>;
}

const Form = ({isEditing, newSalesOrder, setNewSalesOrder}: FormProps) => {
    const handleUpdateLine = (index: number, line: SalesOrderLineType) => {
        const newLines = [...newSalesOrder.salesOrderLines];
        newLines[index] = {
            ...line,
            amount: line.quantity * line.unitPrice
        };
        setNewSalesOrder({
            ...newSalesOrder,
            salesOrderLines: newLines,
            totalOrderAmount: newLines.reduce((sum, line) => sum + line.amount, 0)
        });
    };

    const handleDeleteLine = (index: number) => {
        const newLines = newSalesOrder.salesOrderLines.filter((_, i) => i !== index);
        setNewSalesOrder({
            ...newSalesOrder,
            salesOrderLines: newLines,
            totalOrderAmount: newLines.reduce((sum, line) => sum + line.amount, 0)
        });
    };

    const handleAddLine = () => {
        const newLine: SalesOrderLineType = {
            lineNumber: newSalesOrder.salesOrderLines.length + 1,
            productCode: '',
            productName: '',
            quantity: 0,
            unitPrice: 0,
            amount: 0,
            remarks: ''
        };
        setNewSalesOrder({
            ...newSalesOrder,
            salesOrderLines: [...newSalesOrder.salesOrderLines, newLine]
        });
    };

    return (
        <div className="single-view-content-item-form">
            <FormInput
                label="受注番号"
                id="orderNumber"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newSalesOrder.orderNumber}
                onChange={(e) => setNewSalesOrder({
                    ...newSalesOrder,
                    orderNumber: e.target.value
                })}
                disabled={isEditing}
            />
            <FormInput
                label="受注日"
                id="orderDate"
                type="date"
                className="single-view-content-item-form-item-input"
                value={convertToDateInputFormat(newSalesOrder.orderDate)}
                onChange={(e) => setNewSalesOrder({
                    ...newSalesOrder,
                    orderDate: e.target.value
                })}
            />
            <FormInput
                label="部門コード"
                id="departmentCode"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newSalesOrder.departmentCode}
                onChange={(e) => setNewSalesOrder({
                    ...newSalesOrder,
                    departmentCode: e.target.value
                })}
            />
            <FormInput
                label="顧客コード"
                id="customerCode"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newSalesOrder.customerCode}
                onChange={(e) => setNewSalesOrder({
                    ...newSalesOrder,
                    customerCode: e.target.value
                })}
            />
            <FormInput
                label="顧客枝番"
                id="customerBranchNumber"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newSalesOrder.customerBranchNumber}
                onChange={(e) => setNewSalesOrder({
                    ...newSalesOrder,
                    customerBranchNumber: Number(e.target.value)
                })}
            />
            <FormInput
                label="社員コード"
                id="employeeCode"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newSalesOrder.employeeCode}
                onChange={(e) => setNewSalesOrder({
                    ...newSalesOrder,
                    employeeCode: e.target.value
                })}
            />
            <FormInput
                label="希望納期"
                id="desiredDeliveryDate"
                type="date"
                className="single-view-content-item-form-item-input"
                value={convertToDateInputFormat(newSalesOrder.desiredDeliveryDate)}
                onChange={(e) => setNewSalesOrder({
                    ...newSalesOrder,
                    desiredDeliveryDate: e.target.value
                })}
            />
            <FormInput
                label="客先注文番号"
                id="customerOrderNumber"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newSalesOrder.customerOrderNumber}
                onChange={(e) => setNewSalesOrder({
                    ...newSalesOrder,
                    customerOrderNumber: e.target.value
                })}
            />
            <FormInput
                label="倉庫コード"
                id="warehouseCode"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newSalesOrder.warehouseCode}
                onChange={(e) => setNewSalesOrder({
                    ...newSalesOrder,
                    warehouseCode: e.target.value
                })}
            />
            <FormInput
                label="受注金額合計"
                id="totalOrderAmount"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newSalesOrder.totalOrderAmount}
                disabled={true}
                onChange={() =>{}}
            />
            <FormInput
                label="消費税合計"
                id="totalConsumptionTax"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newSalesOrder.totalConsumptionTax}
                disabled={true}
                onChange={() =>{}}
            />
            <FormInput
                label="備考"
                id="remarks"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newSalesOrder.remarks}
                onChange={(e) => setNewSalesOrder({
                    ...newSalesOrder,
                    remarks: e.target.value
                })}
            />

            <div className="single-view-content-item-form-lines sales-order-detail">
                <h3>受注明細</h3>
                <button className="add-line-button" onClick={handleAddLine}>明細追加</button>
                <div className="table-container">
                    <table className="order-lines-table">
                        <thead>
                            <tr>
                                <th>商品コード</th>
                                <th>商品名</th>
                                <th>数量</th>
                                <th>単価</th>
                                <th>金額</th>
                                <th>備考</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            {newSalesOrder.salesOrderLines.map((line, index) => (
                                <tr 
                                    key={index} 
                                    className="order-line-row"
                                    style={{
                                        backgroundColor: index % 2 === 0 ? '#ffffff' : '#f9f9f9'
                                    }}>
                                    <td className="table-cell">
                                        <input
                                            type="text"
                                            value={line.productCode}
                                            onChange={(e) => handleUpdateLine(index, { ...line, productCode: e.target.value })}
                                            className="table-input"
                                        />
                                    </td>
                                    <td className="table-cell">
                                        <input
                                            type="text"
                                            value={line.productName}
                                            onChange={(e) => handleUpdateLine(index, { ...line, productName: e.target.value })}
                                            className="table-input"
                                        />
                                    </td>
                                    <td className="table-cell">
                                        <input
                                            type="number"
                                            value={line.quantity}
                                            onChange={(e) => handleUpdateLine(index, { ...line, quantity: Number(e.target.value) })}
                                            className="table-input"
                                        />
                                    </td>
                                    <td className="table-cell">
                                        <input
                                            type="number"
                                            value={line.unitPrice}
                                            onChange={(e) => handleUpdateLine(index, { ...line, unitPrice: Number(e.target.value) })}
                                            className="table-input"
                                        />
                                    </td>
                                    <td className="table-cell">
                                        <input
                                            type="number"
                                            value={line.amount}
                                            disabled={true}
                                            className="table-input"
                                        />
                                    </td>
                                    <td className="table-cell">
                                        <input
                                            type="text"
                                            value={line.remarks}
                                            onChange={(e) => handleUpdateLine(index, { ...line, remarks: e.target.value })}
                                            className="table-input"
                                        />
                                    </td>
                                    <td className="table-cell">
                                        <button 
                                            className="delete-line-button"
                                            onClick={() => handleDeleteLine(index)}
                                        >
                                            削除
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                        <tfoot>
                            <tr>
                                <td colSpan={4} className="total-label">合計</td>
                                <td className="total-amount">{newSalesOrder.totalOrderAmount.toLocaleString()}</td>
                                <td colSpan={2}></td>
                            </tr>
                        </tfoot>
                    </table>
                </div>
            </div>
        </div>
    );
};


interface SalesOrderSingleViewProps {
    error: string | null;
    message: string | null;
    isEditing: boolean;
    newSalesOrder: SalesOrderType;
    setNewSalesOrder: React.Dispatch<React.SetStateAction<SalesOrderType>>;
    handleCreateOrUpdateSalesOrder: () => void;
    handleCloseModal: () => void;
}

export const SalesOrderSingleView: React.FC<SalesOrderSingleViewProps> = ({
    error,
    message,
    isEditing,
    newSalesOrder,
    setNewSalesOrder,
    handleCreateOrUpdateSalesOrder,
    handleCloseModal
}) => {
    return (
        <div className="single-view-object-container">
            <Message error={error} message={message}/>
            <div className="single-view-header">
                <Header
                    title="受注"
                    subtitle={isEditing ? "編集" : "新規作成"}
                    isEditing={isEditing}
                    handleCreateOrUpdateSalesOrder={handleCreateOrUpdateSalesOrder}
                    handleCloseModal={handleCloseModal}
                />
            </div>
            <div className="single-view-container">
                <div className="single-view-content">
                    <div className="single-view-content-item">
                        <Form
                            isEditing={isEditing}
                            newSalesOrder={newSalesOrder}
                            setNewSalesOrder={setNewSalesOrder}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
};
