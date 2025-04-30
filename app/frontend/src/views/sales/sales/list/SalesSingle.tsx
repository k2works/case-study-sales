import React, {Dispatch, SetStateAction} from 'react';
import {Message} from "../../../../components/application/Message.tsx";
import {SalesLineType, SalesType, SalesTypeEnumType} from "../../../../models/sales/sales";
import {convertToDateInputFormat} from "../../../../components/application/utils.ts";
import {FormInput, FormSelect, SingleViewHeaderActions, SingleViewHeaderItem} from "../../../Common.tsx";
import "./Sales.css";
import {getTaxRate, TaxRateEnumType} from "../../../../models/sales/order.ts";

interface HeaderProps {
    title: string;
    subtitle: string;
    isEditing: boolean;
    handleCreateOrUpdateSales: () => void;
    handleCloseModal: () => void;
}

const Header = ({
    title,
    subtitle,
    isEditing,
    handleCreateOrUpdateSales,
    handleCloseModal
}: HeaderProps) => (
    <div>
        <SingleViewHeaderItem title={title} subtitle={subtitle}/>
        <SingleViewHeaderActions
            isEditing={isEditing}
            handleCreateOrUpdateUser={handleCreateOrUpdateSales}
            handleCloseModal={handleCloseModal}
        />
    </div>
);

interface FormProps {
    isEditing: boolean;
    newSales: SalesType;
    setNewSales: React.Dispatch<React.SetStateAction<SalesType>>;
    setSelectedLineIndex: Dispatch<SetStateAction<number | null>>;
    handleDepartmentSelect: () => void;
    handleEmployeeSelect: () => void;
    handleCustomerSelect: () => void;
    handleProductSelect: () => void;
}

const calculateLineAmount = (line: SalesLineType): number => {
    return line.salesQuantity * line.salesUnitPrice - (line.discountAmount || 0);
};

const calculateLineTax = (line: SalesLineType) => {
    const amount = calculateLineAmount(line);
    const taxRate = getTaxRate(line);
    return line.taxRate === TaxRateEnumType.その他 ? 0 : Math.floor(amount * taxRate);
};

const calculateTotalAmount = (lines: SalesLineType[]): number => {
    return lines.reduce((sum, line) => sum + calculateLineAmount(line), 0);
};

const calculateTotalTax = (lines : SalesLineType[]): number => {
    return lines.reduce((sum, line) => sum + calculateLineTax(line), 0);

}

const Form = ({
    isEditing, 
    newSales, 
    setNewSales, 
    setSelectedLineIndex, 
    handleDepartmentSelect, 
    handleEmployeeSelect, 
    handleCustomerSelect, 
    handleProductSelect
}: FormProps) => {
    const handleUpdateLine = (index: number, line: SalesLineType) => {
        const newLines = [...newSales.salesLines];
        newLines[index] = {
            ...line,
            salesNumber: newSales.salesNumber
        };

        const totalAmount = calculateTotalAmount(newLines);
        const totalTax = calculateTotalTax(newLines);

        setNewSales({
            ...newSales,
            salesLines: newLines,
            totalSalesAmount: totalAmount,
            totalConsumptionTax: totalTax
        });
    };

    const handleDeleteLine = (index: number) => {
        const newLines = newSales.salesLines.filter((_, i) => i !== index)
            .map((line, i) => ({ ...line, salesLineNumber: i + 1 }));

        const totalAmount = calculateTotalAmount(newLines);
        const totalTax = Math.floor(totalAmount * 0.1);

        setNewSales({
            ...newSales,
            salesLines: newLines,
            totalSalesAmount: totalAmount + totalTax,
            totalConsumptionTax: totalTax
        });
    };

    const handleAddLine = () => {
        const newLine: SalesLineType = {
            salesNumber: newSales.salesNumber,
            salesLineNumber: newSales.salesLines.length + 1,
            productCode: '',
            productName: '',
            salesUnitPrice: 0,
            salesQuantity: 0,
            shippedQuantity: 0,
            discountAmount: 0,
            billingDate: '',
            billingNumber: '',
            billingDelayCategory: 0,
            autoJournalDate: '',
            taxRate: TaxRateEnumType.標準税率
        };
        setNewSales({
            ...newSales,
            salesLines: [...newSales.salesLines, newLine]
        });
    };

    const handleProductSelectEvent = (index: number | null) => {
        setSelectedLineIndex(index);
        handleProductSelect();
    }

    return (
        <div className="single-view-content-item-form">
            <FormInput
                label="売上番号"
                id="salesNumber"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newSales.salesNumber}
                onChange={(e) => setNewSales({
                    ...newSales,
                    salesNumber: e.target.value
                })}
                disabled={isEditing}
            />
            <FormInput
                label="受注番号"
                id="orderNumber"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newSales.orderNumber}
                onChange={(e) => setNewSales({
                    ...newSales,
                    orderNumber: e.target.value
                })}
            />
            <FormInput
                label="売上日"
                id="salesDate"
                type="date"
                className="single-view-content-item-form-item-input"
                value={convertToDateInputFormat(newSales.salesDate)}
                onChange={(e) => setNewSales({
                    ...newSales,
                    salesDate: e.target.value
                })}
            />
            <FormSelect
                id="salesType"
                label="売上区分"
                value={newSales.salesType}
                options={SalesTypeEnumType}
                onChange={(e) => {
                    setNewSales({
                        ...newSales,
                        salesType: e,
                    });
                }}
            />
            <FormInput
                label="部門コード"
                id="departmentCode"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newSales.departmentCode}
                onChange={(e) => setNewSales({
                    ...newSales,
                    departmentCode: e.target.value
                })}
                onClick={handleDepartmentSelect}
            />
            <FormInput
                label="部門開始日"
                id="departmentStartDate"
                type="date"
                className="single-view-content-item-form-item-input"
                value={convertToDateInputFormat(newSales.departmentStartDate)}
                onChange={(e) => setNewSales({
                    ...newSales,
                    departmentStartDate: e.target.value
                })}
                disabled={true}
            />
            <FormInput
                label="顧客コード"
                id="customerCode"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newSales.customerCode}
                onChange={(e) => setNewSales({
                    ...newSales,
                    customerCode: e.target.value
                })}
                onClick={handleCustomerSelect}
            />
            <FormInput
                label="社員コード"
                id="employeeCode"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newSales.employeeCode}
                onChange={(e) => setNewSales({
                    ...newSales,
                    employeeCode: e.target.value
                })}
                onClick={handleEmployeeSelect}
            />
            <FormInput
                label="売上金額合計"
                id="totalSalesAmount"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newSales.totalSalesAmount}
                disabled={true}
                onChange={() => {}}
            />
            <FormInput
                label="消費税合計"
                id="totalConsumptionTax"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newSales.totalConsumptionTax}
                disabled={true}
                onChange={() => {}}
            />
            <FormInput
                label="備考"
                id="remarks"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newSales.remarks}
                onChange={(e) => setNewSales({
                    ...newSales,
                    remarks: e.target.value
                })}
            />
            <FormInput
                label="伝票番号"
                id="voucherNumber"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newSales.voucherNumber}
                onChange={(e) => setNewSales({
                    ...newSales,
                    voucherNumber: Number(e.target.value)
                })}
            />
            <FormInput
                label="原伝票番号"
                id="originalVoucherNumber"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newSales.originalVoucherNumber}
                onChange={(e) => setNewSales({
                    ...newSales,
                    originalVoucherNumber: e.target.value
                })}
            />

            <div className="single-view-content-item-form-lines sales-detail">
                <h3>
                    売上明細
                    <button className="add-line-button" onClick={handleAddLine}>
                        <span>＋</span> 明細追加
                    </button>
                </h3>
                <div className="table-container">
                    <table className="sales-lines-table">
                        <thead>
                            <tr>
                                <th>商品コード</th>
                                <th>商品名</th>
                                <th>販売単価</th>
                                <th>消費税率</th>
                                <th>売上数量</th>
                                <th>出荷済数量</th>
                                <th>値引金額</th>
                                <th>請求日</th>
                                <th>請求番号</th>
                                <th>請求遅延区分</th>
                                <th>自動仕訳日</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            {newSales.salesLines.map((line, index) => (
                                <tr 
                                    key={index} 
                                    className="sales-line-row"
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
                                            value={line.salesQuantity}
                                            onChange={(e) => handleUpdateLine(index, { ...line, salesQuantity: Number(e.target.value) })}
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
                                        <input
                                            type="number"
                                            value={line.discountAmount}
                                            onChange={(e) => handleUpdateLine(index, { ...line, discountAmount: Number(e.target.value) })}
                                            className="table-input"
                                        />
                                    </td>
                                    <td className="table-cell">
                                        <input
                                            type="date"
                                            value={convertToDateInputFormat(line.billingDate)}
                                            onChange={(e) => handleUpdateLine(index, { ...line, billingDate: e.target.value })}
                                            className="table-input"
                                        />
                                    </td>
                                    <td className="table-cell">
                                        <input
                                            type="text"
                                            value={line.billingNumber}
                                            onChange={(e) => handleUpdateLine(index, { ...line, billingNumber: e.target.value })}
                                            className="table-input"
                                        />
                                    </td>
                                    <td className="table-cell">
                                        <input
                                            type="number"
                                            value={line.billingDelayCategory}
                                            onChange={(e) => handleUpdateLine(index, { ...line, billingDelayCategory: Number(e.target.value) })}
                                            className="table-input"
                                        />
                                    </td>
                                    <td className="table-cell">
                                        <input
                                            type="date"
                                            value={convertToDateInputFormat(line.autoJournalDate)}
                                            onChange={(e) => handleUpdateLine(index, { ...line, autoJournalDate: e.target.value })}
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
                                <td className="total-amount">{(newSales.totalSalesAmount).toLocaleString()}</td>
                                <td colSpan={1}></td>
                            </tr>
                            <tr>
                                <td colSpan={1} className="total-label">消費税</td>
                                <td className="total-amount">{newSales.totalConsumptionTax.toLocaleString()}</td>
                                <td colSpan={1}></td>
                            </tr>
                            <tr>
                                <td colSpan={1} className="total-label">合計金額</td>
                                <td className="total-amount">{(newSales.totalSalesAmount + newSales.totalConsumptionTax).toLocaleString()}</td>
                                <td colSpan={1}></td>
                            </tr>
                        </tfoot>
                    </table>
                </div>
            </div>
        </div>
    );
};

interface SalesSingleViewProps {
    error: string | null;
    message: string | null;
    isEditing: boolean;
    newSales: SalesType;
    setNewSales: React.Dispatch<React.SetStateAction<SalesType>>;
    setSelectedLineIndex: Dispatch<SetStateAction<number | null>>;
    handleCreateOrUpdateSales: () => void;
    handleCloseModal: () => void;
    handleDepartmentSelect: () => void;
    handleEmployeeSelect: () => void;
    handlePartnerSelect: () => void;
    handleProductSelect: () => void;
}

export const SalesSingleView: React.FC<SalesSingleViewProps> = ({
    error,
    message,
    isEditing,
    newSales,
    setNewSales,
    setSelectedLineIndex,
    handleCreateOrUpdateSales,
    handleCloseModal,
    handlePartnerSelect,
    handleDepartmentSelect,
    handleEmployeeSelect,
    handleProductSelect,
}) => {
    return (
        <div className="single-view-object-container">
            <Message error={error} message={message}/>
            <div className="single-view-header">
                <Header
                    title="売上"
                    subtitle={isEditing ? "編集" : "新規作成"}
                    isEditing={isEditing}
                    handleCreateOrUpdateSales={handleCreateOrUpdateSales}
                    handleCloseModal={handleCloseModal}
                />
            </div>
            <div className="single-view-container">
                <div className="single-view-content">
                    <div className="single-view-content-item">
                        <Form
                            isEditing={isEditing}
                            newSales={newSales}
                            setNewSales={setNewSales}
                            setSelectedLineIndex={setSelectedLineIndex}
                            handleDepartmentSelect={handleDepartmentSelect}
                            handleEmployeeSelect={handleEmployeeSelect}
                            handleCustomerSelect={handlePartnerSelect}
                            handleProductSelect={handleProductSelect}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
};
