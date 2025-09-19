import React, {useEffect} from "react";
import {showErrorMessage} from "../../application/utils.ts";
import LoadingIndicator from "../../../views/application/LoadingIndicatior.tsx";
import { InventoryProvider, useInventoryContext } from "../../../providers/inventory/Inventory.tsx";
import { InventoryCollection } from "./InventoryCollection.tsx";

export const InventoryContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            setError,
            fetchInventories,
        } = useInventoryContext();

        useEffect(() => {
            (async () => {
                try {
                    await fetchInventories.load();
                } catch (error: any) {
                    showErrorMessage(`在庫情報の取得に失敗しました: ${error?.message}`, setError);
                }
            })();
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    <InventoryCollection/>
                )}
            </>
        );
    };

    return (
        <InventoryProvider>
            <Content />
        </InventoryProvider>
    );
};