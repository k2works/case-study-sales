import React from "react";
import { usePaymentContext } from "../../../../providers/sales/Payment.tsx";
import { showErrorMessage } from "../../../application/utils.ts";
import { useDepartmentContext } from "../../../../providers/master/Department.tsx";
import { useCustomerContext } from "../../../../providers/master/partner/Customer.tsx";
import { PaymentSingleView } from "../../../../views/sales/payment/list/PaymentSingle.tsx";

export const PaymentSingle: React.FC = () => {
    const {
        message,
        setMessage,
        error,
        setError,
        setModalIsOpen,
        isEditing,
        setEditId,
        newPayment,
        setNewPayment,
        fetchPayments,
        paymentService,
    } = usePaymentContext();

    const {
        setModalIsOpen: setDepartmentModalIsOpen,
    } = useDepartmentContext();

    const {
        setModalIsOpen: setCustomerModalIsOpen,
    } = useCustomerContext();

    const handleCloseModal = () => {
        setModalIsOpen(false);
        setEditId(null);
    };

    const handleCreateOrUpdatePayment = async () => {
        try {
            if (isEditing) {
                await paymentService.update(newPayment);
                setMessage("入金を更新しました。");
            } else {
                await paymentService.create(newPayment);
                setMessage("入金を作成しました。");
            }
            await fetchPayments.load();
            handleCloseModal();
        } catch (error: any) {
            showErrorMessage(`入金の更新に失敗しました: ${error?.message}`, setError);
        }
    };

    return (
        <PaymentSingleView
            error={error}
            message={message}
            isEditing={isEditing}
            newPayment={newPayment}
            setNewPayment={setNewPayment}
            handleCreateOrUpdatePayment={handleCreateOrUpdatePayment}
            handleCloseModal={handleCloseModal}
            handleDepartmentSelect={() => setDepartmentModalIsOpen(true)}
            handleCustomerSelect={() => setCustomerModalIsOpen(true)}
        />
    );
};
