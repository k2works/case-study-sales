import React, {Dispatch, SetStateAction} from 'react';
import {Message} from "../../../../components/application/Message.tsx";
import {
    PurchaseLineType,
    PurchaseType,
} from "../../../../models/procurement/purchase.ts";
import {convertToDateInputFormat} from "../../../../components/application/utils.ts";
import {FormInput, SingleViewHeaderActions, SingleViewHeaderItem} from "../../../Common.tsx";

interface HeaderProps {
    title: string;
    subtitle: string;
    isEditing: boolean;
    handleCreateOrUpdatePurchase: () => void;
    handleCloseModal: () => void;
}

const Header = ({
    title,
    subtitle,
    isEditing,
    handleCreateOrUpdatePurchase,
    handleCloseModal
}: HeaderProps) => (
    <div>
        <SingleViewHeaderItem title={title} subtitle={subtitle}/>
        <SingleViewHeaderActions
            isEditing={isEditing}
            handleCreateOrUpdateUser={handleCreateOrUpdatePurchase}
            handleCloseModal={handleCloseModal}
        />
    </div>
);


interface FormProps {
    isEditing: boolean;
    newPurchase: PurchaseType;
    setNewPurchase: React.Dispatch<React.SetStateAction<PurchaseType>>;
    setSelectedLineIndex: Dispatch<SetStateAction<number | null>>;
    handleEmployeeSelect: () => void;
    handleVendorSelect: () => void;
    handleProductSelect: () => void;
    handleDepartmentSelect: () => void;
    handleWarehouseSelect: () => void;
}

const calculateLineAmount = (line: PurchaseLineType): number => {
    return line.purchaseQuantity * line.purchaseUnitPrice;
};

const calculateLineTax = (line: PurchaseLineType): number => {
    const amount = calculateLineAmount(line);
    return Math.floor(amount * 0.1);
};

const calculateTotalAmount = (lines: PurchaseLineType[]): number => {
    return lines.reduce((sum, line) => sum + calculateLineAmount(line), 0);
};

const calculateTotalTax = (lines: PurchaseLineType[]): number => {
    return lines.reduce((sum, line) => sum + calculateLineTax(line), 0);
};

const Form = ({
    isEditing,
    newPurchase,
    setNewPurchase,
    setSelectedLineIndex,
    handleEmployeeSelect,
    handleVendorSelect,
    handleProductSelect,
    handleDepartmentSelect,
    handleWarehouseSelect
}: FormProps) => {
    const handleUpdateLine = (index: number, line: PurchaseLineType) => {
        const newLines = [...newPurchase.purchaseLines];
        newLines[index] = {
            ...line,
            purchaseNumber: newPurchase.purchaseNumber
        };

        const totalAmount = calculateTotalAmount(newLines);
        const totalTax = calculateTotalTax(newLines);

        setNewPurchase({
            ...newPurchase,
            purchaseLines: newLines,
            totalPurchaseAmount: totalAmount,
            totalConsumptionTax: totalTax
        });
    };

    const handleDeleteLine = (index: number) => {
        const newLines = newPurchase.purchaseLines.filter((_, i) => i !== index)
            .map((line, i) => ({ ...line, purchaseLineNumber: i + 1 }));

        const totalAmount = calculateTotalAmount(newLines);
        const totalTax = calculateTotalTax(newLines);

        setNewPurchase({
            ...newPurchase,
            purchaseLines: newLines,
            totalPurchaseAmount: totalAmount,
            totalConsumptionTax: totalTax
        });
    };

    const handleAddLine = () => {
        const newLine: PurchaseLineType = {
            purchaseNumber: newPurchase.purchaseNumber,
            purchaseLineNumber: newPurchase.purchaseLines.length + 1,
            purchaseLineDisplayNumber: newPurchase.purchaseLines.length + 1,
            purchaseOrderLineNumber: 0,
            productCode: '',
            warehouseCode: '',
            productName: '',
            purchaseUnitPrice: 0,
            purchaseQuantity: 0
        };
        setNewPurchase({
            ...newPurchase,
            purchaseLines: [...newPurchase.purchaseLines, newLine]
        });
    };

    const handleProductSelectEvent = (index: number|null) => {
        setSelectedLineIndex(index);
        handleProductSelect();
    }

    const handleWarehouseSelectEvent = (index: number|null) => {
        setSelectedLineIndex(index);
        handleWarehouseSelect();
    }

    return (
        <div className="single-view-content-item-form">
            <FormInput
                label="仕入番号"
                id="purchaseNumber"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newPurchase.purchaseNumber}
                onChange={(e) => setNewPurchase({
                    ...newPurchase,
                    purchaseNumber: e.target.value
                })}
                disabled={isEditing}
            />
            <FormInput
                label="仕入日"
                id="purchaseDate"
                type="date"
                className="single-view-content-item-form-item-input"
                value={convertToDateInputFormat(newPurchase.purchaseDate)}
                onChange={(e) => setNewPurchase({
                    ...newPurchase,
                    purchaseDate: e.target.value
                })}
            />
            <FormInput
                label="仕入先コード"
                id="supplierCode"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newPurchase.supplierCode}
                onChange={(e) => setNewPurchase({
                    ...newPurchase,
                    supplierCode: e.target.value
                })}
                onClick={handleVendorSelect}
            />
            <FormInput
                label="仕入先枝番"
                id="supplierBranchNumber"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newPurchase.supplierBranchNumber}
                onChange={(e) => setNewPurchase({
                    ...newPurchase,
                    supplierBranchNumber: Number(e.target.value)
                })}
                disabled={true}
            />
            <FormInput
                label="仕入担当者コード"
                id="purchaseManagerCode"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newPurchase.purchaseManagerCode}
                onChange={(e) => setNewPurchase({
                    ...newPurchase,
                    purchaseManagerCode: e.target.value
                })}
                onClick={handleEmployeeSelect}
            />
            <FormInput
                label="計上日"
                id="startDate"
                type="date"
                className="single-view-content-item-form-item-input"
                value={convertToDateInputFormat(newPurchase.startDate)}
                onChange={(e) => setNewPurchase({
                    ...newPurchase,
                    startDate: e.target.value
                })}
            />
            <FormInput
                label="発注番号"
                id="purchaseOrderNumber"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newPurchase.purchaseOrderNumber}
                onChange={(e) => setNewPurchase({
                    ...newPurchase,
                    purchaseOrderNumber: e.target.value
                })}
            />
            <FormInput
                label="部門コード"
                id="departmentCode"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newPurchase.departmentCode}
                onChange={(e) => setNewPurchase({
                    ...newPurchase,
                    departmentCode: e.target.value
                })}
                onClick={handleDepartmentSelect}
            />
            <FormInput
                label="仕入金額合計"
                id="totalPurchaseAmount"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newPurchase.totalPurchaseAmount}
                disabled={true}
                onChange={() =>{}}
            />
            <FormInput
                label="消費税合計"
                id="totalConsumptionTax"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newPurchase.totalConsumptionTax}
                disabled={true}
                onChange={() =>{}}
            />
            <FormInput
                label="備考"
                id="remarks"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newPurchase.remarks}
                onChange={(e) => setNewPurchase({
                    ...newPurchase,
                    remarks: e.target.value
                })}
            />

            <div className="single-view-content-item-form-lines purchase-order-detail">
                <h3>
                    仕入明細
                    <button className="add-line-button" onClick={handleAddLine}>
                        <span>＋</span> 明細追加
                    </button>
                </h3>
                <div className="table-container">
                    <table className="purchase-order-lines-table">
                        <thead>
                            <tr>
                                <th>仕入行番号</th>
                                <th>仕入行表示番号</th>
                                <th>発注行番号</th>
                                <th>商品コード</th>
                                <th>商品名</th>
                                <th>倉庫コード</th>
                                <th>仕入単価</th>
                                <th>仕入数量</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            {newPurchase.purchaseLines.map((line, index) => (
                                <tr
                                    key={index}
                                    className="purchase-order-line-row"
                                    style={{
                                        backgroundColor: index % 2 === 0 ? '#ffffff' : '#f9f9f9'
                                    }}>
                                    <td className="table-cell">
                                        <input
                                            type="number"
                                            value={line.purchaseLineNumber}
                                            onChange={(e) => handleUpdateLine(index, { ...line, purchaseLineNumber: Number(e.target.value) })}
                                            className="table-input"
                                            disabled={true}
                                        />
                                    </td>
                                    <td className="table-cell">
                                        <input
                                            type="number"
                                            value={line.purchaseLineDisplayNumber}
                                            onChange={(e) => handleUpdateLine(index, { ...line, purchaseLineDisplayNumber: Number(e.target.value) })}
                                            className="table-input"
                                        />
                                    </td>
                                    <td className="table-cell">
                                        <input
                                            type="number"
                                            value={line.purchaseOrderLineNumber}
                                            onChange={(e) => handleUpdateLine(index, { ...line, purchaseOrderLineNumber: Number(e.target.value) })}
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
                                        <div style={{ display: 'flex', alignItems: 'center' }}>
                                            <input
                                                type="text"
                                                value={line.warehouseCode}
                                                className="table-input"
                                                style={{ marginRight: '4px' }}
                                                onChange={(e) => handleUpdateLine(index, { ...line, warehouseCode: e.target.value })}
                                            />
                                            <button
                                                onClick={() => handleWarehouseSelectEvent(index)}
                                                className="select-button"
                                                type="button"
                                            >
                                                選択
                                            </button>
                                        </div>
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
                                            value={line.purchaseQuantity}
                                            onChange={(e) => handleUpdateLine(index, { ...line, purchaseQuantity: Number(e.target.value) })}
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
                                <td colSpan={1} className="total-label">小計</td>
                                <td className="total-amount">{(newPurchase.totalPurchaseAmount || 0).toLocaleString()}</td>
                                <td colSpan={1}></td>
                            </tr>
                            <tr>
                                <td colSpan={1} className="total-label">消費税</td>
                                <td className="total-amount">{(newPurchase.totalConsumptionTax || 0).toLocaleString()}</td>
                                <td colSpan={1}></td>
                            </tr>
                            <tr>
                                <td colSpan={1} className="total-label">合計金額</td>
                                <td className="total-amount">{((newPurchase.totalPurchaseAmount || 0) + (newPurchase.totalConsumptionTax || 0)).toLocaleString()}</td>
                                <td colSpan={1}></td>
                            </tr>
                        </tfoot>
                    </table>
                </div>
            </div>
        </div>
    );
};


interface PurchaseSingleViewProps {
    error: string | null;
    message: string | null;
    isEditing: boolean;
    newPurchase: PurchaseType;
    setNewPurchase: React.Dispatch<React.SetStateAction<PurchaseType>>;
    setSelectedLineIndex: Dispatch<SetStateAction<number | null>>;
    handleCreateOrUpdatePurchase: () => void;
    handleCloseModal: () => void;
    handleEmployeeSelect: () => void;
    handleVendorSelect: () => void;
    handleProductSelect: () => void;
    handleDepartmentSelect: () => void;
    handleWarehouseSelect: () => void;
}

export const PurchaseSingleView: React.FC<PurchaseSingleViewProps> = ({
    error,
    message,
    isEditing,
    newPurchase,
    setNewPurchase,
    setSelectedLineIndex,
    handleCreateOrUpdatePurchase,
    handleCloseModal,
    handleEmployeeSelect,
    handleVendorSelect,
    handleProductSelect,
    handleDepartmentSelect,
    handleWarehouseSelect,
}) => {
    return (
        <div className="single-view-object-container">
            <Message error={error} message={message}/>
            <div className="single-view-header">
                <Header
                    title="仕入"
                    subtitle={isEditing ? "編集" : "新規作成"}
                    isEditing={isEditing}
                    handleCreateOrUpdatePurchase={handleCreateOrUpdatePurchase}
                    handleCloseModal={handleCloseModal}
                />
            </div>
            <div className="single-view-container">
                <div className="single-view-content">
                    <div className="single-view-content-item">
                        <Form
                            isEditing={isEditing}
                            newPurchase={newPurchase}
                            setNewPurchase={setNewPurchase}
                            setSelectedLineIndex={setSelectedLineIndex}
                            handleEmployeeSelect={handleEmployeeSelect}
                            handleVendorSelect={handleVendorSelect}
                            handleProductSelect={handleProductSelect}
                            handleDepartmentSelect={handleDepartmentSelect}
                            handleWarehouseSelect={handleWarehouseSelect}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
};
