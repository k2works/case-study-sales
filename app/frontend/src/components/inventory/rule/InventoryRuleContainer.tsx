import React, { useEffect } from "react";
import { showErrorMessage } from "../../application/utils.ts";
import LoadingIndicator from "../../../views/application/LoadingIndicatior.tsx";
import { InventoryRuleCollection } from "./InventoryRuleCollection.tsx";

export const InventoryRuleContainer: React.FC = () => {
    const Content: React.FC = () => {
        // TODO: InventoryProviderとuseInventoryContextを実装後に置き換える
        const loading = false;
        const setError = (error: string) => console.error(error);

        useEffect(() => {
            (async () => {
                try {
                    // TODO: 在庫ルール関連データの取得処理を実装
                    console.log("在庫ルール画面を初期化中...");
                } catch (error: any) {
                    showErrorMessage(`在庫ルール画面の初期化に失敗しました: ${error?.message}`, setError);
                }
            })();
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    <InventoryRuleCollection/>
                )}
            </>
        );
    };

    return (
        // TODO: InventoryProviderでラップ
        <Content />
    );
};