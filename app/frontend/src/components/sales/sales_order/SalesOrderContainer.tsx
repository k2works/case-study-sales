import React, { useEffect } from "react";
import { showErrorMessage } from "../../application/utils.ts";
import LoadingIndicator from "../../../views/application/LoadingIndicatior.tsx";
import { SalesOrderProvider, useSalesOrderContext } from "../../../providers/sales/SalesOrder.tsx";
import {SalesOrderCollection} from "./SalesOrderCollection.tsx";

export const SalesOrderContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            setError,
            fetchSalesOrders,
        } = useSalesOrderContext();

        useEffect(() => {
            (async () => {
                try {
                    await fetchSalesOrders.load();
                } catch (error: Error | unknown) {
                    const errorMessage = error instanceof Error ? error.message : 'Unknown error';
                    showErrorMessage(`受注情報の取得に失敗しました: ${errorMessage}`, setError);
                }
            })();
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    <SalesOrderCollection/>
                )}
            </>
        );
    };

    return (
        <SalesOrderProvider>
            <Content/>
        </SalesOrderProvider>
    );
};