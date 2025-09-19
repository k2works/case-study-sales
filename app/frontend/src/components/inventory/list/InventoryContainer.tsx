import React, { useEffect } from "react";
import { showErrorMessage } from "../../application/utils.ts";
import LoadingIndicator from "../../../views/application/LoadingIndicatior.tsx";
import { InventoryCollection } from "./InventoryCollection.tsx";

export const InventoryContainer: React.FC = () => {
    const Content: React.FC = () => {
        // TODO: InventoryProviderとuseInventoryContextを実装後に置き換える
        const loading = false;
        const setError = (error: string) => console.error(error);

        useEffect(() => {
            (async () => {
                try {
                    // TODO: 在庫データの取得処理を実装
                    console.log("在庫データを取得中...");
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
        // TODO: InventoryProviderでラップ
        <Content />
    );
};