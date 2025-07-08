import React, { useEffect } from "react";
import { showErrorMessage } from "../../../application/utils.ts";
import LoadingIndicator from "../../../../views/application/LoadingIndicatior.tsx";
import { PaymentProvider, usePaymentContext } from "../../../../providers/sales/Payment.tsx";
import { PaymentCollection } from "./PaymentCollection.tsx";
import { DepartmentProvider, useDepartmentContext } from "../../../../providers/master/Department.tsx";
import { CustomerProvider, useCustomerContext } from "../../../../providers/master/partner/Customer.tsx";

export const PaymentContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            setError,
            fetchPayments,
        } = usePaymentContext();

        const {
            fetchDepartments,
        } = useDepartmentContext();

        const {
            fetchCustomers,
        } = useCustomerContext();

        useEffect(() => {
            (async () => {
                try {
                    await Promise.all([
                        await fetchPayments.load(),
                        fetchDepartments.load(),
                        fetchCustomers.load(),
                    ]);
                } catch (error: unknown) {
                    const errorMessage = error instanceof Error ? error.message : '不明なエラーが発生しました';
                    showErrorMessage(`入金情報の取得に失敗しました: ${errorMessage}`, setError);
                }
            })();
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    <PaymentCollection/>
                )}
            </>
        );
    };

    return (
        <PaymentProvider>
            <DepartmentProvider>
                <CustomerProvider>
                    <Content />
                </CustomerProvider>
            </DepartmentProvider>
        </PaymentProvider>
    );
};