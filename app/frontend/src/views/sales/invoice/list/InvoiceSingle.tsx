import React, {Dispatch, SetStateAction} from 'react';
import {Message} from "../../../../components/application/Message.tsx";
import {InvoiceLineType, InvoiceType} from "../../../../models/sales/invoice";
import {convertToDateInputFormat} from "../../../../components/application/utils.ts";
import {FormInput, SingleViewHeaderActions, SingleViewHeaderItem} from "../../../Common.tsx";

interface HeaderProps {
    title: string;
    subtitle: string;
    isEditing: boolean;
    handleCreateOrUpdateInvoice: () => void;
    handleCloseModal: () => void;
}

const Header = ({
    title,
    subtitle,
    isEditing,
    handleCreateOrUpdateInvoice,
    handleCloseModal
}: HeaderProps) => (
    <div>
        <SingleViewHeaderItem title={title} subtitle={subtitle}/>
        <SingleViewHeaderActions
            isEditing={isEditing}
            handleCreateOrUpdateUser={handleCreateOrUpdateInvoice}
            handleCloseModal={handleCloseModal}
        />
    </div>
);

interface FormProps {
    isEditing: boolean;
    newInvoice: InvoiceType;
    setNewInvoice: React.Dispatch<React.SetStateAction<InvoiceType>>;
    setSelectedLineIndex: Dispatch<SetStateAction<number | null>>;
    handleCustomerSelect: () => void;
    selectedLineIndex: number | null;
}

const Form = ({
    isEditing, 
    newInvoice, 
    setNewInvoice, 
    setSelectedLineIndex, 
    handleCustomerSelect,
    selectedLineIndex
}: FormProps) => {
    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setNewInvoice({
            ...newInvoice,
            [name]: value
        });
    };

    const handleNumberChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setNewInvoice({
            ...newInvoice,
            [name]: value === "" ? 0 : parseInt(value, 10)
        });
    };

    const handleLineChange = (index: number, field: string, value: string | number) => {
        const updatedLines = [...newInvoice.invoiceLines];
        updatedLines[index] = {
            ...updatedLines[index],
            [field]: value
        };
        setNewInvoice({
            ...newInvoice,
            invoiceLines: updatedLines
        });
    };

    const handleDeleteLine = (index: number) => {
        const updatedLines = newInvoice.invoiceLines.filter((_, i) => i !== index);
        setNewInvoice({
            ...newInvoice,
            invoiceLines: updatedLines
        });
        setSelectedLineIndex(null);
    };

    const handleSelectLine = (index: number) => {
        setSelectedLineIndex(index);
    };

    const handleAddLine = () => {
        const newLine: InvoiceLineType = {
            invoiceNumber: newInvoice.invoiceNumber,
            salesNumber: "",
            salesLineNumber: 0,
            checked: false
        };
        setNewInvoice({
            ...newInvoice,
            invoiceLines: [...newInvoice.invoiceLines, newLine]
        });
    };

    return (
        <div className="single-view-content-item-form">
            <FormInput
                label="請求番号"
                id="invoiceNumber"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newInvoice.invoiceNumber}
                onChange={handleChange}
                disabled={isEditing}
            />
            <FormInput
                label="請求日"
                id="invoiceDate"
                type="date"
                className="single-view-content-item-form-item-input"
                value={convertToDateInputFormat(newInvoice.invoiceDate)}
                onChange={handleChange}
            />
            <FormInput
                label="顧客コード"
                id="customerCode"
                type="text"
                className="single-view-content-item-form-item-input"
                value={newInvoice.customerCode}
                onChange={(e) => setNewInvoice({
                    ...newInvoice,
                    partnerCode: e.target.value,
                    customerCode: e.target.value
                })}
                onClick={handleCustomerSelect}
            />
            <FormInput
                label="顧客枝番"
                id="customerBranchNumber"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newInvoice.customerBranchNumber}
                onChange={(e) => setNewInvoice({
                    ...newInvoice,
                    customerBranchNumber: Number(e.target.value)
                })}
                disabled={true}
            />
            <FormInput
                label="前回入金額"
                id="previousPaymentAmount"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newInvoice.previousPaymentAmount}
                onChange={handleNumberChange}
            />
            <FormInput
                label="当月売上額"
                id="currentMonthSalesAmount"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newInvoice.currentMonthSalesAmount}
                onChange={handleNumberChange}
            />
            <FormInput
                label="当月入金額"
                id="currentMonthPaymentAmount"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newInvoice.currentMonthPaymentAmount}
                onChange={handleNumberChange}
            />
            <FormInput
                label="当月請求額"
                id="currentMonthInvoiceAmount"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newInvoice.currentMonthInvoiceAmount}
                onChange={handleNumberChange}
            />
            <FormInput
                label="消費税金額"
                id="consumptionTaxAmount"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newInvoice.consumptionTaxAmount}
                onChange={handleNumberChange}
            />
            <FormInput
                label="請求消込金額"
                id="invoiceReconciliationAmount"
                type="number"
                className="single-view-content-item-form-item-input"
                value={newInvoice.invoiceReconciliationAmount}
                onChange={handleNumberChange}
            />

            <div className="single-view-content-item-form-lines sales-detail">
                <h3>
                    請求明細
                    <button className="add-line-button" onClick={handleAddLine}>
                        <span>＋</span> 明細追加
                    </button>
                </h3>
                <div className="table-container">
                    <table className="sales-lines-table">
                        <thead>
                            <tr>
                                <th>売上番号</th>
                                <th>売上行番号</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            {newInvoice.invoiceLines.map((line, index) => (
                                <tr 
                                    key={index} 
                                    className="sales-line-row"
                                    style={{
                                        backgroundColor: selectedLineIndex === index ? '#e6f7ff' : (index % 2 === 0 ? '#ffffff' : '#f9f9f9')
                                    }}
                                    onClick={() => handleSelectLine(index)}
                                >
                                    <td className="table-cell">
                                        <input
                                            type="text"
                                            value={line.salesNumber}
                                            onChange={(e) => handleLineChange(index, 'salesNumber', e.target.value)}
                                            className="table-input"
                                        />
                                    </td>
                                    <td className="table-cell">
                                        <input
                                            type="number"
                                            value={line.salesLineNumber}
                                            onChange={(e) => handleLineChange(index, 'salesLineNumber', parseInt(e.target.value, 10) || 0)}
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
                    </table>
                </div>
            </div>
        </div>
    );
};

interface InvoiceSingleViewProps {
    error: string | null;
    message: string | null;
    isEditing: boolean;
    newInvoice: InvoiceType;
    setNewInvoice: React.Dispatch<React.SetStateAction<InvoiceType>>;
    setSelectedLineIndex: Dispatch<SetStateAction<number | null>>;
    handleCreateOrUpdateInvoice: () => void;
    handleCloseModal: () => void;
    handleCustomerSelect: () => void;
    selectedLineIndex: number | null;
}

export const InvoiceSingleView: React.FC<InvoiceSingleViewProps> = ({
    error,
    message,
    isEditing,
    newInvoice,
    setNewInvoice,
    setSelectedLineIndex,
    handleCreateOrUpdateInvoice,
    handleCloseModal,
    handleCustomerSelect,
    selectedLineIndex
}) => {
    return (
        <div className="single-view-object-container">
            <Message error={error} message={message}/>
            <div className="single-view-header">
                <Header
                    title="請求"
                    subtitle={isEditing ? "編集" : "新規作成"}
                    isEditing={isEditing}
                    handleCreateOrUpdateInvoice={handleCreateOrUpdateInvoice}
                    handleCloseModal={handleCloseModal}
                />
            </div>
            <div className="single-view-container">
                <div className="single-view-content">
                    <div className="single-view-content-item">
                        <Form
                            isEditing={isEditing}
                            newInvoice={newInvoice}
                            setNewInvoice={setNewInvoice}
                            setSelectedLineIndex={setSelectedLineIndex}
                            handleCustomerSelect={handleCustomerSelect}
                            selectedLineIndex={selectedLineIndex}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
};
