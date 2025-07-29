import { useEffect } from "react";
import { showErrorMessage } from "../../../application/utils";
import LoadingIndicator from "../../../../views/application/LoadingIndicatior";
import {PaymentProvider, usePaymentContext} from "../../../../providers/sales/Payment.tsx";
import {PaymentAggregateCollection} from "./PaymentAggregateCollection.tsx";

export const PaymentAggregateContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            setError,
            fetchPayments,
        } = usePaymentContext();

        useEffect(() => {
            (async () => {
                try {
                    await Promise.all([
                        fetchPayments.load(),
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
                    <PaymentAggregateCollection/>
                )}
            </>
        );
    };

    return (
        <PaymentProvider>
            <Content />
        </PaymentProvider>
    );
};