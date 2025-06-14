import React, { useEffect } from "react";
import { showErrorMessage } from "../../../application/utils.ts";
import LoadingIndicator from "../../../../views/application/LoadingIndicatior.tsx";
import { InvoiceProvider, useInvoiceContext } from "../../../../providers/sales/Invoice.tsx";
import { InvoiceCollection } from "./InvoiceCollection.tsx";
import { CustomerProvider, useCustomerContext } from "../../../../providers/master/partner/Customer.tsx";

export const InvoiceContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            setError,
            fetchInvoices,
        } = useInvoiceContext();

        const {
            fetchCustomers,
        } = useCustomerContext();

        useEffect(() => {
            (async () => {
                try {
                    await Promise.all([
                        await fetchInvoices.load(),
                        fetchCustomers.load(),
                    ]);
                } catch (error: unknown) {
                    const errorMessage = error instanceof Error ? error.message : '不明なエラーが発生しました';
                    showErrorMessage(`請求情報の取得に失敗しました: ${errorMessage}`, setError);
                }
            })();
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    <InvoiceCollection/>
                )}
            </>
        );
    };

    return (
        <InvoiceProvider>
            <CustomerProvider>
                <Content />
            </CustomerProvider>
        </InvoiceProvider>
    );
};