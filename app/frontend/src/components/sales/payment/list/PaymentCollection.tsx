import React from "react";
import { usePaymentContext } from "../../../../providers/sales/Payment.tsx";
import { PaymentCollectionView } from "../../../../views/sales/payment/list/PaymentCollection.tsx";
import { PaymentSearchModal } from "./PaymentSearchModal.tsx";
import { PaymentEditModal } from "./PaymentEditModal.tsx";
import { PaymentType } from "../../../../models/sales/payment.ts";
import { showErrorMessage } from "../../../application/utils.ts";

export const PaymentCollection: React.FC = () => {
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
        initialPayment,
        payments,
        setPayments,
        setNewPayment,
        searchPaymentCriteria,
        setSearchPaymentCriteria,
        fetchPayments,
        paymentService,
        setSearchModalIsOpen,
    } = usePaymentContext();

    const handleOpenModal = (paymentItem?: PaymentType) => {
        setMessage("");
        setError("");
        if (paymentItem) {
            setNewPayment(paymentItem);
            setEditId(paymentItem.paymentNumber);
            setIsEditing(true);
        } else {
            setNewPayment(initialPayment);
            setIsEditing(false);
        }
        setModalIsOpen(true);
    };

    const handleOpenSearchModal = () => {
        setSearchModalIsOpen(true);
    }

    const handleCheckPayment = (paymentItem: PaymentType) => {
        const newPayments = payments.map((p: PaymentType) => {
            if (p.paymentNumber === paymentItem.paymentNumber) {
                return {
                    ...p,
                    checked: !p.checked
                };
            }
            return p;
        });
        setPayments(newPayments);
    }

    const handleCheckAllPayments = () => {
        const newPayments = payments.map((p: PaymentType) => {
            return {
                ...p,
                checked: !payments.every((p: PaymentType) => p.checked)
            };
        });
        setPayments(newPayments);
    }

    const handleDeletePayment = async (paymentNumber: string) => {
        try {
            if (!window.confirm(`入金番号:${paymentNumber} を削除しますか？`)) return;
            await paymentService.destroy(paymentNumber);
            await fetchPayments.load();
            setMessage("入金情報を削除しました。");
        } catch (error: unknown) {
            const errorMessage = error instanceof Error ? error.message : '不明なエラーが発生しました';
            showErrorMessage(`入金情報の削除に失敗しました: ${errorMessage}`, setError);
        }
    };

    const handleDeleteCheckedCollection = async () => {
        const checkedPayments = payments.filter((p: PaymentType) => p.checked);
        if (!checkedPayments.length) {
            setError("削除する入金情報を選択してください。");
            return;
        }

        try {
            if (!window.confirm("選択した入金情報を削除しますか？")) return;
            await Promise.all(checkedPayments.map((p: PaymentType) => paymentService.destroy(p.paymentNumber)));
            await fetchPayments.load();
            setMessage("選択した入金情報を削除しました。");
        } catch (error: unknown) {
            const errorMessage = error instanceof Error ? error.message : '不明なエラーが発生しました';
            showErrorMessage(`入金情報の削除に失敗しました: ${errorMessage}`, setError);
        }
    };

    return (
        <>
            <PaymentSearchModal/>
            <PaymentEditModal/>
            <PaymentCollectionView
                error={error}
                message={message}
                searchItems={{searchPaymentCriteria, setSearchPaymentCriteria, handleOpenSearchModal}}
                headerItems={{
                    handleOpenModal, 
                    handleCheckToggleCollection: handleCheckAllPayments, 
                    handleDeleteCheckedCollection
                }}
                collectionItems={{payments, handleOpenModal, handleDeletePayment, handleCheckPayment}}
                pageNationItems={{pageNation, fetchPayments: fetchPayments.load, criteria}}
            />
        </>
    );
}
