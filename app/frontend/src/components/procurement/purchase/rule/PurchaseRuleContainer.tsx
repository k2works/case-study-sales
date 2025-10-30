import React, { useEffect } from "react";
import { showErrorMessage } from "../../../application/utils.ts";
import LoadingIndicator from "../../../../views/application/LoadingIndicatior.tsx";
import { PurchaseReceiptProvider, usePurchaseReceiptContext } from "../../../../providers/procurement/Purchase.tsx";
import { PurchaseRuleCollection } from "./PurchaseRuleCollection.tsx";

export const PurchaseRuleContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            setError,
            fetchPurchases,
        } = usePurchaseReceiptContext();

        useEffect(() => {
            (async () => {
                try {
                    await fetchPurchases.load();
                } catch (error: any) {
                    showErrorMessage(`仕入情報の取得に失敗しました: ${error?.message}`, setError);
                }
            })();
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    <PurchaseRuleCollection/>
                )}
            </>
        );
    };

    return (
        <PurchaseReceiptProvider>
            <Content />
        </PurchaseReceiptProvider>
    );
};
