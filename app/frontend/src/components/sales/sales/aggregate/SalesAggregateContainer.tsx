import { useEffect } from "react";
import { showErrorMessage } from "../../../application/utils";
import LoadingIndicator from "../../../../views/application/LoadingIndicatior";
import { SalesProvider, useSalesContext } from "../../../../providers/sales/Sales";
import { SalesAggregateCollection } from "./SalesAggregateCollection";

export const SalesAggregateContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            setError,
            fetchSales,
        } = useSalesContext();

        useEffect(() => {
            (async () => {
                try {
                    await Promise.all([
                        fetchSales.load(),
                    ]);
                } catch (error) {
                    const errorMessage = error instanceof Error ? error.message : '不明なエラーが発生しました';
                    showErrorMessage(`売上情報の取得に失敗しました: ${errorMessage}`, setError);
                }
            })();
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    <SalesAggregateCollection/>
                )}
            </>
        );
    };

    return (
        <SalesProvider>
            <Content />
        </SalesProvider>
    );
};
