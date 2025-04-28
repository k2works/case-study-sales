import React, {Dispatch, SetStateAction} from 'react';
import {Message} from "../../../../components/application/Message.tsx";
import {
    CompletionFlagEnumType,
    SalesOrderLineType,
    SalesOrderType,
    TaxRateEnumType,
    TaxRateValues
} from "../../../../models/sales/order.ts";
import {convertToDateInputFormat} from "../../../../components/application/utils.ts";
import {FormInput, SingleViewHeaderActions, SingleViewHeaderItem} from "../../../Common.tsx";
import "./Order.css";

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
    setSelectedLineIndex: Dispatch<SetStateAction<number | null>>;
    handleDepartmentSelect: () => void;
    handleEmployeeSelect: () => void;
    handleCustomerSelect: () => void;
    handleProductSelect: () => void;
}

const calculateLineAmount = (line: SalesOrderLineType): number => {
    return line.orderQuantity * line.salesUnitPrice - line.discountAmount;
};

const calculateLineTax = (line: SalesOrderLineType): number => {
    const amount = calculateLineAmount(line);
    const taxRate = line.taxRate === TaxRateEnumType.標準税率 ? TaxRateValues[TaxRateEnumType.標準税率] : line.taxRate === TaxRateEnumType.軽減税率 ? TaxRateValues[TaxRateEnumType.軽減税率] : 0;
    return line.taxRate === TaxRateEnumType.その他 ? 0 : Math.floor(amount * taxRate);
};

const calculateTotalAmount = (lines: SalesOrderLineType[]): number => {
    return lines.reduce((sum, line) => sum + calculateLineAmount(line) + calculateLineTax(line), 0);
};

const calculateTotalTax = (lines: SalesOrderLineType[]): number => {
    return lines.reduce((sum, line) => sum + calculateLineTax(line), 0);
};

const Form = ({isEditing, newSalesOrder, setNewSalesOrder, setSelectedLineIndex, handleDepartmentSelect, handleEmployeeSelect, handleCustomerSelect, handleProductSelect}: FormProps) => {
    const handleUpdateLine = (index: number, line: SalesOrderLineType) => {
        const newLines = [...newSalesOrder.salesOrderLines];
        newLines[index] = {
            ...line,
            orderNumber: newSalesOrder.orderNumber
        };

        const totalAmount = calculateTotalAmount(newLines);
        const totalTax = calculateTotalTax(newLines);

        setNewSalesOrder({
            ...newSalesOrder,
            salesOrderLines: newLines,
            totalOrderAmount: totalAmount,
            totalConsumptionTax: totalTax
        });
    };

    const handleDeleteLine = (index: number) => {
        const newLines = newSalesOrder.salesOrderLines.filter((_, i) => i !== index)
            .map((line, i) => ({ ...line, orderLineNumber: i + 1 }));

        const totalAmount = calculateTotalAmount(newLines);
        const totalTax = calculateTotalTax(newLines);

        setNewSalesOrder({
            ...newSalesOrder,
            salesOrderLines: newLines,
            totalOrderAmount: totalAmount,
            totalConsumptionTax: totalTax
        });
    };

    const handleAddLine = () => {
        const newLine: SalesOrderLineType = {
            orderNumber: newSalesOrder.orderNumber,
            orderLineNumber: newSalesOrder.salesOrderLines.length + 1,
            productCode: '',
            productName: '',
            salesUnitPrice: 0,
            orderQuantity: 0,
            taxRate: TaxRateEnumType.標準税率,
            allocationQuantity: 0,
            shipmentInstructionQuantity: 0,
            shippedQuantity: 0,
            completionFlag: CompletionFlagEnumType.未完了,
            discountAmount: 0,
            deliveryDate: new Date().toISOString().slice(0, 16)
        };
        setNewSalesOrder({
            ...newSalesOrder,
            salesOrderLines: [...newSalesOrder.salesOrderLines, newLine]
        });
    };

    const handleProductSelectEvent = (index: number|null) => {
        setSelectedLineIndex(index);
        handleProductSelect();
    }

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
                onClick={handleDepartmentSelect}
            />
            <FormInput
                label="部門開始日"
                id="departmentStartDate"
                type="date"
                className="single-view-content-item-form-item-input"
                value={convertToDateInputFormat(newSalesOrder.departmentStartDate)}
                onChange={(e) => setNewSalesOrder({
                    ...newSalesOrder,
                    departmentStartDate: e.target.value
                })}
                disabled={true}
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
                onClick={handleCustomerSelect}
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
                disabled={true}
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
                onClick={handleEmployeeSelect}
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

            <div className="single-view-content-item-form-lines order-detail">
                <h3>
                    受注明細
                    <button className="add-line-button" onClick={handleAddLine}>
                        <span>＋</span> 明細追加
                    </button>
                </h3>
                <div className="table-container">
                    <table className="order-lines-table">
                        <thead>
                            <tr>
                                <th>商品コード</th>
                                <th>商品名</th>
                                <th>販売単価</th>
                                <th>受注数量</th>
                                <th>消費税率</th>
                                <th>引当数量</th>
                                <th>出荷指示数量</th>
                                <th>出荷済数量</th>
                                <th>完了フラグ</th>
                                <th>値引金額</th>
                                <th>納期</th>
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
                                        <div style={{ display: 'flex', alignItems: 'center' }}>
                                            <input
                                                type="text"
                                                value={line.productCode}
                                                className="table-input"
                                                style={{ marginRight: '4px' }}
                                                onChange={(e) => handleUpdateLine(index, { ...line, productCode: e.target.value })}
                                            />
                                            <button
                                                onClick={() => handleProductSelectEvent(index)}
                                                className="select-button"
                                                type="button"
                                            >
                                                選択
                                            </button>
                                        </div>
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
                                            value={line.salesUnitPrice}
                                            onChange={(e) => handleUpdateLine(index, { ...line, salesUnitPrice: Number(e.target.value) })}
                                            className="table-input"
                                        />
                                    </td>
                                    <td className="table-cell">
                                        <input
                                            type="number"
                                            value={line.orderQuantity}
                                            onChange={(e) => handleUpdateLine(index, { ...line, orderQuantity: Number(e.target.value) })}
                                            className="table-input"
                                        />
                                    </td>
                                    <td className="table-cell">
                                        <select
                                            value={line.taxRate}
                                            onChange={(e) => handleUpdateLine(index, { ...line, taxRate: e.target.value as TaxRateEnumType })}
                                            className="table-input"
                                        >
                                            <option value={TaxRateEnumType.標準税率}>標準</option>
                                            <option value={TaxRateEnumType.軽減税率}>軽減</option>
                                            <option value={TaxRateEnumType.その他}>非課税</option>
                                        </select>
                                    </td>
                                    <td className="table-cell">
                                        <input
                                            type="number"
                                            value={line.allocationQuantity}
                                            onChange={(e) => handleUpdateLine(index, { ...line, allocationQuantity: Number(e.target.value) })}
                                            className="table-input"
                                        />
                                    </td>
                                    <td className="table-cell">
                                        <input
                                            type="number"
                                            value={line.shipmentInstructionQuantity}
                                            onChange={(e) => handleUpdateLine(index, { ...line, shipmentInstructionQuantity: Number(e.target.value) })}
                                            className="table-input"
                                        />
                                    </td>
                                    <td className="table-cell">
                                        <input
                                            type="number"
                                            value={line.shippedQuantity}
                                            onChange={(e) => handleUpdateLine(index, { ...line, shippedQuantity: Number(e.target.value) })}
                                            className="table-input"
                                        />
                                    </td>
                                    <td className="table-cell">
                                        <select
                                            value={line.completionFlag}
                                            onChange={(e) => handleUpdateLine(index, { ...line, completionFlag: e.target.value as CompletionFlagEnumType })}
                                            className="table-input"
                                        >
                                            <option value={CompletionFlagEnumType.完了}>完了</option>
                                            <option value={CompletionFlagEnumType.未完了}>未完了</option>
                                        </select>
                                    </td>
                                    <td className="table-cell">
                                        <input
                                            type="number"
                                            value={line.discountAmount}
                                            onChange={(e) => handleUpdateLine(index, { ...line, discountAmount: Number(e.target.value) })}
                                            className="table-input"
                                        />
                                    </td>
                                    <td className="table-cell">
                                        <input
                                            type="datetime-local"
                                            value={line.deliveryDate}
                                            onChange={(e) => handleUpdateLine(index, { ...line, deliveryDate: e.target.value })}
                                            className="table-input"
                                        />
                                    </td>
                                    <td className="table-cell">
                                        <button 
                                            className="delete-line-button"
                                            onClick={() => handleDeleteLine(index)}
                                        >
                                            <span>✕</span> 削除
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                        <tfoot>
                            <tr>
                                <td colSpan={10} className="total-label">小計</td>
                                <td className="total-amount">{(newSalesOrder.totalOrderAmount - newSalesOrder.totalConsumptionTax).toLocaleString()}</td>
                                <td colSpan={2}></td>
                            </tr>
                            <tr>
                                <td colSpan={10} className="total-label">消費税</td>
                                <td className="total-amount">{newSalesOrder.totalConsumptionTax.toLocaleString()}</td>
                                <td colSpan={2}></td>
                            </tr>
                            <tr>
                                <td colSpan={10} className="total-label">合計金額</td>
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
    setSelectedLineIndex: Dispatch<SetStateAction<number | null>>;
    handleCreateOrUpdateSalesOrder: () => void;
    handleCloseModal: () => void;
    handleDepartmentSelect: () => void;
    handleEmployeeSelect: () => void;
    handleCustomerSelect: () => void;
    handleProductSelect: () => void;
}

export const SalesOrderSingleView: React.FC<SalesOrderSingleViewProps> = ({
    error,
    message,
    isEditing,
    newSalesOrder,
    setNewSalesOrder,
    setSelectedLineIndex,
    handleCreateOrUpdateSalesOrder,
    handleCloseModal,
    handleCustomerSelect,
    handleDepartmentSelect,
    handleEmployeeSelect,
    handleProductSelect,
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
                            setSelectedLineIndex={setSelectedLineIndex}
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
