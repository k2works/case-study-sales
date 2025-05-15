import React from "react";
import { useInvoiceContext } from "../../../../providers/sales/Invoice.tsx";
import { InvoiceSingleView } from "../../../../views/sales/invoice/list/InvoiceSingle.tsx";
import { showErrorMessage } from "../../../application/utils.ts";
import { useCustomerContext } from "../../../../providers/master/partner/Customer.tsx";

export const InvoiceSingle: React.FC = () => {
    const {
        message,
        setMessage,
        error,
        setError,
        setModalIsOpen,
        isEditing,
        setEditId,
        newInvoice,
        setNewInvoice,
        fetchInvoices,
        invoiceService,
        setSelectedLineIndex,
        selectedLineIndex
    } = useInvoiceContext();

    const {
        setModalIsOpen: setCustomerModalIsOpen,
    } = useCustomerContext();

    const handleCloseModal = () => {
        setModalIsOpen(false);
        setEditId(null);
    };

    const handleCreateOrUpdateInvoice = async () => {
        const validate = () => {
            if (!newInvoice.invoiceNumber) {
                setError("請求番号を入力してください。");
                return false;
            }
            if (!newInvoice.customerCode) {
                setError("顧客を選択してください。");
                return false;
            }
            if (!newInvoice.invoiceLines.map(line => line.salesNumber).every(code => code)) {
                setError("売上番号を入力してください。");
                return false;
            }
            return true;
        }

        if (!validate()) {
            return;
        }

        try {
            if (isEditing) {
                await invoiceService.update(newInvoice);
                setMessage("請求を更新しました。");
            } else {
                await invoiceService.create(newInvoice);
                setMessage("請求を作成しました。");
            }
            await fetchInvoices.load();
            handleCloseModal();
        } catch (error: unknown) {
            const errorMessage = error instanceof Error ? error.message : '不明なエラーが発生しました';
            showErrorMessage(`請求の${isEditing ? '更新' : '登録'}に失敗しました: ${errorMessage}`, setError);
        }
    };

    return (
        <InvoiceSingleView
            error={error}
            message={message}
            isEditing={isEditing}
            newInvoice={newInvoice}
            setNewInvoice={setNewInvoice}
            setSelectedLineIndex={setSelectedLineIndex}
            handleCreateOrUpdateInvoice={handleCreateOrUpdateInvoice}
            handleCloseModal={handleCloseModal}
            handleCustomerSelect={() => setCustomerModalIsOpen(true)}
            selectedLineIndex={selectedLineIndex}
        />
    );
};
