import React, { useEffect } from "react";
import { showErrorMessage } from "../../../application/utils.ts";
import LoadingIndicator from "../../../../views/application/LoadingIndicatior.tsx";
import { PurchaseProvider, usePurchaseContext } from "../../../../providers/procurement/Purchase.tsx";
import { PurchaseOrderUploadCollection } from "./PurchaseOrderUploadCollection.tsx";

export const PurchaseOrderUploadContainer: React.FC = () => {
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
                } catch (error: any) {
                    showErrorMessage(`発注情報の取得に失敗しました: ${error?.message}`, setError);
                }
            })();
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    <PurchaseOrderUploadCollection/>
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