import React from "react";
import { useInvoiceContext } from "../../../../providers/sales/Invoice.tsx";
import { InvoiceCollectionView } from "../../../../views/sales/invoice/list/InvoiceCollection.tsx";
import { InvoiceType } from "../../../../models/sales/invoice";
import { showErrorMessage } from "../../../application/utils.ts";
import { InvoiceSearchModal } from "./InvoiceSearchModal.tsx";
import { InvoiceEditModal } from "./InvoiceEditModal.tsx";

export const InvoiceCollection: React.FC = () => {
    const {
        message,
        setMessage,
        error,
        setError,
        pageNation,
        criteria,
        setModalIsOpen,
        setIsEditing,
        setEditId,
        initialInvoice,
        invoices,
        setInvoices,
        setNewInvoice,
        searchInvoiceCriteria,
        setSearchInvoiceCriteria,
        fetchInvoices,
        invoiceService,
        setSearchModalIsOpen,
    } = useInvoiceContext();

    const handleOpenModal = (invoiceItem?: InvoiceType) => {
        setMessage("");
        setError("");
        if (invoiceItem) {
            setNewInvoice(invoiceItem);
            setEditId(invoiceItem.invoiceNumber);
            setIsEditing(true);
        } else {
            setNewInvoice(initialInvoice);
            setIsEditing(false);
        }
        setModalIsOpen(true);
    };

    const handleOpenSearchModal = () => {
        setSearchModalIsOpen(true);
    }

    const handleDeleteInvoice = async (invoiceNumber: string) => {
        try {
            if (!window.confirm(`請求番号:${invoiceNumber} を削除しますか？`)) return;
            await invoiceService.destroy(invoiceNumber);
            await fetchInvoices.load();
            setMessage("請求を削除しました。");
        } catch (error: unknown) {
            const errorMessage = error instanceof Error ? error.message : '不明なエラーが発生しました';
            showErrorMessage(`請求の削除に失敗しました: ${errorMessage}`, setError);
        }
    };

    const handleCheckInvoice = (invoiceItem: InvoiceType) => {
        const newInvoices = invoices.map((i: InvoiceType) => {
            if (i.invoiceNumber === invoiceItem.invoiceNumber) {
                return {
                    ...i,
                    checked: !i.checked
                };
            }
            return i;
        });
        setInvoices(newInvoices);
    }

    const handleCheckAllInvoices = () => {
        const newInvoices = invoices.map((i: InvoiceType) => {
            return {
                ...i,
                checked: !invoices.every((i: InvoiceType) => i.checked)
            };
        });
        setInvoices(newInvoices);
    }

    const handleDeleteCheckedCollection = async () => {
        const checkedInvoices = invoices.filter((i: InvoiceType) => i.checked);
        if (!checkedInvoices.length) {
            setError("削除する請求を選択してください。");
            return;
        }

        try {
            if (!window.confirm("選択した請求を削除しますか？")) return;
            await Promise.all(checkedInvoices.map((i: InvoiceType) => invoiceService.destroy(i.invoiceNumber)));
            await fetchInvoices.load();
            setMessage("選択した請求を削除しました。");
        } catch (error: unknown) {
            const errorMessage = error instanceof Error ? error.message : '不明なエラーが発生しました';
            showErrorMessage(`請求の削除に失敗しました: ${errorMessage}`, setError);
        }
    }

    return (
        <>
            <InvoiceSearchModal/>
            <InvoiceEditModal/>
            <InvoiceCollectionView
                error={error}
                message={message}
                searchItems={{searchInvoiceCriteria, setSearchInvoiceCriteria, handleOpenSearchModal}}
                headerItems={{handleOpenModal, handleCheckToggleCollection: handleCheckAllInvoices, handleDeleteCheckedCollection}}
                collectionItems={{invoices, handleOpenModal, handleDeleteInvoice, handleCheckInvoice}}
                pageNationItems={{pageNation, fetchInvoices: fetchInvoices.load, criteria}}
            />
        </>
    );
}
