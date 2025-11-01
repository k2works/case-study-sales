import { useEffect } from "react";
import { showErrorMessage } from "../../../application/utils";
import LoadingIndicator from "../../../../views/application/LoadingIndicatior";
import { PaymentProvider, usePaymentContext } from "../../../../providers/procurement/Payment";
import { PurchasePaymentAggregateCollection } from "./PurchasePaymentAggregateCollection";

export const PurchasePaymentAggregateContainer: React.FC = () => {
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
                } catch (error) {
                    const errorMessage = error instanceof Error ? error.message : '不明なエラーが発生しました';
                    showErrorMessage(`支払情報の取得に失敗しました: ${errorMessage}`, setError);
                }
            })();
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    <PurchasePaymentAggregateCollection/>
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
