import { useEffect } from "react";
import { showErrorMessage } from "../../../application/utils";
import LoadingIndicator from "../../../../views/application/LoadingIndicatior";
import { InvoiceProvider, useInvoiceContext } from "../../../../providers/sales/Invoice";
import { InvoiceAggregateCollection } from "./InvoiceAggregateCollection";

export const InvoiceAggregateContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            setError,
            fetchInvoices,
        } = useInvoiceContext();

        useEffect(() => {
            (async () => {
                try {
                    await Promise.all([
                        fetchInvoices.load(),
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
                    <InvoiceAggregateCollection/>
                )}
            </>
        );
    };

    return (
        <InvoiceProvider>
            <Content />
        </InvoiceProvider>
    );
};