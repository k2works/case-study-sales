import React, { useEffect } from "react";
import { showErrorMessage } from "../../../application/utils.ts";
import LoadingIndicator from "../../../../views/application/LoadingIndicatior.tsx";
import { SalesOrderProvider, useSalesOrderContext } from "../../../../providers/sales/Order.tsx";
import {OrderUploadCollection} from "./OrderUploadCollection.tsx";

export const OrderUploadContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            setError,
            fetchSalesOrders,
        } = useSalesOrderContext();

        useEffect(() => {
            (async () => {
                try {
                    await Promise.all([
                        fetchSalesOrders.load(),
                    ]);
                } catch (error: any) {
                    showErrorMessage(`受注情報の取得に失敗しました: ${error?.message}`, setError);
                }
            })();
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    <OrderUploadCollection/>
                )}
            </>
        );
    };

    return (
        <SalesOrderProvider>
            <Content />
        </SalesOrderProvider>
    );
};