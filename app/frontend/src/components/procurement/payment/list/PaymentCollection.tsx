import React from "react";
import { usePaymentContext } from "../../../../providers/procurement/Payment.tsx";
import { PaymentCollectionView } from "../../../../views/procurement/payment/list/PaymentCollection.tsx";
import { PaymentType } from "../../../../models/procurement/payment.ts";
import { showErrorMessage } from "../../../application/utils.ts";
import { PaymentSearchModal } from "./PaymentSearchModal.tsx";
import { PaymentEditModal } from "./PaymentEditModal.tsx";

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

    const handleOpenModal = (payment?: PaymentType) => {
        setMessage("");
        setError("");
        if (payment) {
            setNewPayment(payment);
            setEditId(payment.paymentNumber);
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

    const handleDeletePayment = async (paymentNumber: string) => {
        try {
            if (!window.confirm(`支払番号:${paymentNumber} を削除しますか？`)) return;
            await paymentService.destroy(paymentNumber);
            await fetchPayments.load();
            setMessage("支払を削除しました。");
        } catch (error: any) {
            showErrorMessage(`支払の削除に失敗しました: ${error?.message}`, setError);
        }
    };

    const handleCheckPayment = (payment: PaymentType) => {
        const newPayments = payments.map((p: PaymentType) => {
            if (p.paymentNumber === payment.paymentNumber) {
                return {
                    ...p,
                    checked: !p.checked
                };
            }
            return p;
        });
        setPayments(newPayments);
    }

    const handleCheckToggleCollection = () => {
        const newPayments = payments.map((p: PaymentType) => {
            return {
                ...p,
                checked: !payments.every((p: PaymentType) => p.checked)
            };
        });
        setPayments(newPayments);
    }

    const handleDeleteCheckedCollection = async () => {
        const checkedPayments = payments.filter((p: PaymentType) => p.checked);
        if (!checkedPayments.length) {
            setError("削除する支払を選択してください。");
            return;
        }

        try {
            if (!window.confirm("選択した支払を削除しますか？")) return;
            await Promise.all(checkedPayments.map((p: PaymentType) => paymentService.destroy(p.paymentNumber)));
            await fetchPayments.load();
            setMessage("選択した支払を削除しました。");
        } catch (error: any) {
            showErrorMessage(`支払の削除に失敗しました: ${error?.message}`, setError);
        }
    }

    return (
        <>
            <PaymentSearchModal/>
            <PaymentEditModal/>
            <PaymentCollectionView
                error={error}
                message={message}
                searchItems={{
                    searchPaymentCriteria,
                    setSearchPaymentCriteria,
                    handleOpenSearchModal
                }}
                headerItems={{handleOpenModal, handleCheckToggleCollection, handleDeleteCheckedCollection}}
                collectionItems={{payments, handleOpenModal, handleDeletePayment: handleDeletePayment, handleCheckPayment}}
                pageNationItems={{pageNation, fetchPayments: fetchPayments.load, criteria}}
            />
        </>
    );
};
