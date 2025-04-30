import { useEffect } from "react";
import { showErrorMessage } from "../../../application/utils";
import LoadingIndicator from "../../../../views/application/LoadingIndicatior";
import { SalesOrderProvider, useSalesOrderContext } from "../../../../providers/sales/Order.tsx";
import { OrderRuleCollection } from "./OrderRuleCollection.tsx";

export const OrderRuleContainer: React.FC = () => {
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
                } catch (error) {
                    const errorMessage = error instanceof Error ? error.message : '不明なエラーが発生しました';
                    showErrorMessage(`受注情報の取得に失敗しました: ${errorMessage}`, setError);
                }
            })();
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    <OrderRuleCollection/>
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
