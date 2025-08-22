import React, {Dispatch, SetStateAction} from 'react';
import {Message} from "../../../../components/application/Message.tsx";
import {
    CompletionFlagEnumType,
    PurchaseOrderLineType,
    PurchaseOrderType,
} from "../../../../models/sales/purchaseOrder.ts";
import {convertToDateInputFormat} from "../../../../components/application/utils.ts";
import {FormInput, SingleViewHeaderActions, SingleViewHeaderItem} from "../../../Common.tsx";
import "./PurchaseOrder.css";

interface HeaderProps {
    title: string;
    subtitle: string;
    isEditing: boolean;
    handleCreateOrUpdatePurchaseOrder: () => void;
    handleCloseModal: () => void;
}

const Header = ({
    title,
    subtitle,
    isEditing,
    handleCreateOrUpdatePurchaseOrder,
    handleCloseModal
}: HeaderProps) => (
    <div>
        <SingleViewHeaderItem title={title} subtitle={subtitle}/>
        <SingleViewHeaderActions
            isEditing={isEditing}
            handleCreateOrUpdateUser={handleCreateOrUpdatePurchaseOrder}
            handleCloseModal={handleCloseModal}
        />
    </div>
);


interface FormProps {
    isEditing: boolean;
    newPurchaseOrder: PurchaseOrderType;
    setNewPurchaseOrder: React.Dispatch<React.SetStateAction<PurchaseOrderType>>;
    setSelectedLineIndex: Dispatch<SetStateAction<number | null>>;
    handleEmployeeSelect: () => void;
    handleVendorSelect: () => void;
    handleProductSelect: () => void;
}

const calculateLineAmount = (line: PurchaseOrderLineType): number => {
    return line.purchaseOrderQuantity * line.purchaseUnitPrice;
};

const calculateLineTax = (line: PurchaseOrderLineType): number => {
    const amount = calculateLineAmount(line);
    return Math.floor(amount * 0.1);
};

const calculateTotalAmount = (lines: PurchaseOrderLineType[]): number => {
    return lines.reduce((sum, line) => sum + calculateLineAmount(line), 0);
};

const calculateTotalTax = (lines: PurchaseOrderLineType[]): number => {
    return lines.reduce((sum, line) => sum + calculateLineTax(line), 0);
};

const Form = ({isEditing, newPurchaseOrder, setNewPurchaseOrder, setSelectedLineIndex, handleEmployeeSelect, handleVendorSelect, handleProductSelect}: FormProps) => {
    const handleUpdateLine = (index: number, line: PurchaseOrderLineType) => {
        const newLines = [...newPurchaseOrder.purchaseOrderLines];
        newLines[index] = {
            ...line,
            purchaseOrderNumber: newPurchaseOrder.purchaseOrderNumber
        };

        const totalAmount = calculateTotalAmount(newLines);
        const totalTax = calculateTotalTax(newLines);

        setNewPurchaseOrder({
            ...newPurchaseOrder,
            purchaseOrderLines: newLines,
            totalPurchaseAmount: totalAmount,
            totalConsumptionTax: totalTax
        });
    };

    const handleDeleteLine = (index: number) => {
        const newLines = newPurchaseOrder.purchaseOrderLines.filter((_, i) => i !== index)
            .map((line, i) => ({ ...line, purchaseOrderLineNumber: i + 1 }));

        const totalAmount = calculateTotalAmount(newLines);
        const totalTax = calculateTotalTax(newLines);

        setNewPurchaseOrder({
            ...newPurchaseOrder,
            purchaseOrderLines: newLines,
            totalPurchaseAmount: totalAmount,
            totalConsumptionTax: totalTax
        });
    };

    const handleAddLine = () => {
        const newLine: PurchaseOrderLineType = {
            purchaseOrderNumber: newPurchaseOrder.purchaseOrderNumber,
            purchaseOrderLineNumber: newPurchaseOrder.purchaseOrderLines.length + 1,
            purchaseOrderLineDisplayNumber: newPurchaseOrder.purchaseOrderLines.length + 1,
            orderNumber: '',
            orderLineNumber: 0,
            productCode: '',
            productName: '',
            purchaseUnitPrice: 0,
            purchaseOrderQuantity: 0,
            receivedQuantity: 0,
            completionFlag: CompletionFlagEnumType.未完了
        };
        setNewPurchaseOrder({
            ...newPurchaseOrder,
            purchaseOrderLines: [...newPurchaseOrder.purchaseOrderLines, newLine]
        });
    };

    const handleProductSelectEvent = (index: number|null) => {
        setSelectedLineIndex(index);
        handleProductSelect();
    }

    return (
        <div className="single-view-content-item-form">
            <FormInput
                label="発注番号"
                id="purchaseOrderNumber"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newPurchaseOrder.purchaseOrderNumber}
                onChange={(e) => setNewPurchaseOrder({
                    ...newPurchaseOrder,
                    purchaseOrderNumber: e.target.value
                })}
                disabled={isEditing}
            />
            <FormInput
                label="発注日"
                id="purchaseOrderDate"
                type="date"
                className="single-view-content-item-form-item-input"
                value={convertToDateInputFormat(newPurchaseOrder.purchaseOrderDate)}
                onChange={(e) => setNewPurchaseOrder({
                    ...newPurchaseOrder,
                    purchaseOrderDate: e.target.value
                })}
            />
            <FormInput
                label="受注番号"
                id="orderNumber"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newPurchaseOrder.orderNumber}
                onChange={(e) => setNewPurchaseOrder({
                    ...newPurchaseOrder,
                    orderNumber: e.target.value
                })}
            />
            <FormInput
                label="仕入先コード"
                id="supplierCode"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newPurchaseOrder.supplierCode}
                onChange={(e) => setNewPurchaseOrder({
                    ...newPurchaseOrder,
                    supplierCode: e.target.value
                })}
                onClick={handleVendorSelect}
            />
            <FormInput
                label="仕入先枝番"
                id="supplierBranchNumber"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newPurchaseOrder.supplierBranchNumber}
                onChange={(e) => setNewPurchaseOrder({
                    ...newPurchaseOrder,
                    supplierBranchNumber: Number(e.target.value)
                })}
                disabled={true}
            />
            <FormInput
                label="発注担当者コード"
                id="purchaseManagerCode"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newPurchaseOrder.purchaseManagerCode}
                onChange={(e) => setNewPurchaseOrder({
                    ...newPurchaseOrder,
                    purchaseManagerCode: e.target.value
                })}
                onClick={handleEmployeeSelect}
            />
            <FormInput
                label="指定納期"
                id="designatedDeliveryDate"
                type="date"
                className="single-view-content-item-form-item-input"
                value={convertToDateInputFormat(newPurchaseOrder.designatedDeliveryDate)}
                onChange={(e) => setNewPurchaseOrder({
                    ...newPurchaseOrder,
                    designatedDeliveryDate: e.target.value
                })}
            />
            <FormInput
                label="倉庫コード"
                id="warehouseCode"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newPurchaseOrder.warehouseCode}
                onChange={(e) => setNewPurchaseOrder({
                    ...newPurchaseOrder,
                    warehouseCode: e.target.value
                })}
            />
            <FormInput
                label="発注金額合計"
                id="totalPurchaseAmount"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newPurchaseOrder.totalPurchaseAmount}
                disabled={true}
                onChange={() =>{}}
            />
            <FormInput
                label="消費税合計"
                id="totalConsumptionTax"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newPurchaseOrder.totalConsumptionTax}
                disabled={true}
                onChange={() =>{}}
            />
            <FormInput
                label="備考"
                id="remarks"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newPurchaseOrder.remarks}
                onChange={(e) => setNewPurchaseOrder({
                    ...newPurchaseOrder,
                    remarks: e.target.value
                })}
            />

            <div className="single-view-content-item-form-lines purchase-order-detail">
                <h3>
                    発注明細
                    <button className="add-line-button" onClick={handleAddLine}>
                        <span>＋</span> 明細追加
                    </button>
                </h3>
                <div className="table-container">
                    <table className="purchase-order-lines-table">
                        <thead>
                            <tr>
                                <th>発注行番号</th>
                                <th>発注行表示番号</th>
                                <th>受注番号</th>
                                <th>受注行番号</th>
                                <th>商品コード</th>
                                <th>商品名</th>
                                <th>発注単価</th>
                                <th>発注数量</th>
                                <th>入荷数量</th>
                                <th>完了フラグ</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            {newPurchaseOrder.purchaseOrderLines.map((line, index) => (
                                <tr 
                                    key={index} 
                                    className="purchase-order-line-row"
                                    style={{
                                        backgroundColor: index % 2 === 0 ? '#ffffff' : '#f9f9f9'
                                    }}>
                                    <td className="table-cell">
                                        <input
                                            type="number"
                                            value={line.purchaseOrderLineNumber}
                                            onChange={(e) => handleUpdateLine(index, { ...line, purchaseOrderLineNumber: Number(e.target.value) })}
                                            className="table-input"
                                            disabled={true}
                                        />
                                    </td>
                                    <td className="table-cell">
                                        <input
                                            type="number"
                                            value={line.purchaseOrderLineDisplayNumber}
                                            onChange={(e) => handleUpdateLine(index, { ...line, purchaseOrderLineDisplayNumber: Number(e.target.value) })}
                                            className="table-input"
                                        />
                                    </td>
                                    <td className="table-cell">
                                        <input
                                            type="text"
                                            value={line.orderNumber}
                                            onChange={(e) => handleUpdateLine(index, { ...line, orderNumber: e.target.value })}
                                            className="table-input"
                                        />
                                    </td>
                                    <td className="table-cell">
                                        <input
                                            type="number"
                                            value={line.orderLineNumber}
                                            onChange={(e) => handleUpdateLine(index, { ...line, orderLineNumber: Number(e.target.value) })}
                                            className="table-input"
                                        />
                                    </td>
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
                                            value={line.purchaseUnitPrice}
                                            onChange={(e) => handleUpdateLine(index, { ...line, purchaseUnitPrice: Number(e.target.value) })}
                                            className="table-input"
                                        />
                                    </td>
                                    <td className="table-cell">
                                        <input
                                            type="number"
                                            value={line.purchaseOrderQuantity}
                                            onChange={(e) => handleUpdateLine(index, { ...line, purchaseOrderQuantity: Number(e.target.value) })}
                                            className="table-input"
                                        />
                                    </td>
                                    <td className="table-cell">
                                        <input
                                            type="number"
                                            value={line.receivedQuantity}
                                            onChange={(e) => handleUpdateLine(index, { ...line, receivedQuantity: Number(e.target.value) })}
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
                                <td colSpan={1} className="total-label">小計</td>
                                <td className="total-amount">{(newPurchaseOrder.totalPurchaseAmount || 0).toLocaleString()}</td>
                                <td colSpan={1}></td>
                            </tr>
                            <tr>
                                <td colSpan={1} className="total-label">消費税</td>
                                <td className="total-amount">{(newPurchaseOrder.totalConsumptionTax || 0).toLocaleString()}</td>
                                <td colSpan={1}></td>
                            </tr>
                            <tr>
                                <td colSpan={1} className="total-label">合計金額</td>
                                <td className="total-amount">{((newPurchaseOrder.totalPurchaseAmount || 0) + (newPurchaseOrder.totalConsumptionTax || 0)).toLocaleString()}</td>
                                <td colSpan={1}></td>
                            </tr>
                        </tfoot>
                    </table>
                </div>
            </div>
        </div>
    );
};


interface PurchaseOrderSingleViewProps {
    error: string | null;
    message: string | null;
    isEditing: boolean;
    newPurchaseOrder: PurchaseOrderType;
    setNewPurchaseOrder: React.Dispatch<React.SetStateAction<PurchaseOrderType>>;
    setSelectedLineIndex: Dispatch<SetStateAction<number | null>>;
    handleCreateOrUpdatePurchaseOrder: () => void;
    handleCloseModal: () => void;
    handleEmployeeSelect: () => void;
    handleVendorSelect: () => void;
    handleProductSelect: () => void;
}

export const PurchaseOrderSingleView: React.FC<PurchaseOrderSingleViewProps> = ({
    error,
    message,
    isEditing,
    newPurchaseOrder,
    setNewPurchaseOrder,
    setSelectedLineIndex,
    handleCreateOrUpdatePurchaseOrder,
    handleCloseModal,
    handleEmployeeSelect,
    handleVendorSelect,
    handleProductSelect,
}) => {
    return (
        <div className="single-view-object-container">
            <Message error={error} message={message}/>
            <div className="single-view-header">
                <Header
                    title="発注"
                    subtitle={isEditing ? "編集" : "新規作成"}
                    isEditing={isEditing}
                    handleCreateOrUpdatePurchaseOrder={handleCreateOrUpdatePurchaseOrder}
                    handleCloseModal={handleCloseModal}
                />
            </div>
            <div className="single-view-container">
                <div className="single-view-content">
                    <div className="single-view-content-item">
                        <Form
                            isEditing={isEditing}
                            newPurchaseOrder={newPurchaseOrder}
                            setNewPurchaseOrder={setNewPurchaseOrder}
                            setSelectedLineIndex={setSelectedLineIndex}
                            handleEmployeeSelect={handleEmployeeSelect}
                            handleVendorSelect={handleVendorSelect}
                            handleProductSelect={handleProductSelect}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
};