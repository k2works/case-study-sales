import { useEffect } from "react";
import { showErrorMessage } from "../../../application/utils";
import LoadingIndicator from "../../../../views/application/LoadingIndicatior";
import { PurchaseProvider, usePurchaseContext } from "../../../../providers/procurement/Purchase.tsx";
import { PurchaseOrderRuleCollection } from "./PurchaseOrderRuleCollection.tsx";

export const PurchaseOrderRuleContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            setError,
            fetchPurchaseOrders,
        } = usePurchaseContext();

        useEffect(() => {
            (async () => {
                try {
                    await Promise.all([
                        fetchPurchaseOrders.load(),
                    ]);
                } catch (error) {
                    const errorMessage = error instanceof Error ? error.message : '不明なエラーが発生しました';
                    showErrorMessage(`発注情報の取得に失敗しました: ${errorMessage}`, setError);
                }
            })();
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    <PurchaseOrderRuleCollection/>
                )}
            </>
        );
    };

    return (
        <PurchaseProvider>
            <Content />
        </PurchaseProvider>
    );
};