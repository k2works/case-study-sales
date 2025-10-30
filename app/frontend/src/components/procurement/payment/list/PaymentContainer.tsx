import React, { useEffect } from "react";
import { showErrorMessage } from "../../../application/utils.ts";
import LoadingIndicator from "../../../../views/application/LoadingIndicatior.tsx";
import { PaymentProvider, usePaymentContext } from "../../../../providers/procurement/Payment.tsx";
import { PaymentCollection } from "./PaymentCollection.tsx";
import { DepartmentProvider, useDepartmentContext } from "../../../../providers/master/Department.tsx";
import { VendorProvider, useVendorContext } from "../../../../providers/master/partner/Vendor.tsx";

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
            fetchVendors,
        } = useVendorContext();

        useEffect(() => {
            (async () => {
                try {
                    await Promise.all([
                        fetchPayments.load(),
                        fetchDepartments.load(),
                        fetchVendors.load(),
                    ]);
                } catch (error: any) {
                    showErrorMessage(`支払情報の取得に失敗しました: ${error?.message}`, setError);
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
                <VendorProvider>
                    <Content />
                </VendorProvider>
            </DepartmentProvider>
        </PaymentProvider>
    );
};
