import React from "react";
import { usePaymentContext } from "../../../../providers/procurement/Payment.tsx";
import { PaymentSingleView } from "../../../../views/procurement/payment/list/PaymentSingle.tsx";
import { showErrorMessage } from "../../../application/utils.ts";
import { useDepartmentContext } from "../../../../providers/master/Department.tsx";
import { useVendorContext } from "../../../../providers/master/partner/Vendor.tsx";

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
        setModalIsOpen: setVendorModalIsOpen,
    } = useVendorContext();

    const handleCloseModal = () => {
        setModalIsOpen(false);
        setEditId(null);
    };

    const handleCreateOrUpdatePayment = async () => {
        try {
            if (isEditing) {
                await paymentService.update(newPayment);
                setMessage("支払を更新しました。");
            } else {
                await paymentService.create(newPayment);
                setMessage("支払を作成しました。");
            }
            await fetchPayments.load();
            handleCloseModal();
        } catch (error: any) {
            showErrorMessage(`支払の更新に失敗しました: ${error?.message}`, setError);
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
            handleVendorSelect={() => setVendorModalIsOpen(true)}
        />
    );
};
