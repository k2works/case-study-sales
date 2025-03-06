import { useEffect } from "react";
import { showErrorMessage } from "../../../application/utils";
import LoadingIndicator from "../../../../views/application/LoadingIndicatior";
import { SalesOrderProvider, useSalesOrderContext } from "../../../../providers/sales/SalesOrder";
import { SalesOrderRuleCollection } from "./SalesOrderRuleCollection";

export const SalesOrderRuleContainer: React.FC = () => {
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
                    <SalesOrderRuleCollection/>
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
