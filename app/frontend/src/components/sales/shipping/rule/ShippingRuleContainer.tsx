import { useEffect } from "react";
import { showErrorMessage } from "../../../application/utils";
import LoadingIndicator from "../../../../views/application/LoadingIndicatior";
import { ShippingProvider, useShippingContext } from "../../../../providers/sales/Shipping";
import { ShippingRuleCollection } from "./ShippingRuleCollection";

export const ShippingRuleContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            setError,
            fetchShippings,
        } = useShippingContext();

        useEffect(() => {
            (async () => {
                try {
                    await Promise.all([
                        fetchShippings.load(),
                    ]);
                } catch (error) {
                    const errorMessage = error instanceof Error ? error.message : '不明なエラーが発生しました';
                    showErrorMessage(`出荷情報の取得に失敗しました: ${errorMessage}`, setError);
                }
            })();
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    <ShippingRuleCollection/>
                )}
            </>
        );
    };

    return (
        <ShippingProvider>
            <Content />
        </ShippingProvider>
    );
};