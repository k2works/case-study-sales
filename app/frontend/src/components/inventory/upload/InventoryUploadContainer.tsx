import React, { useEffect } from "react";
import { showErrorMessage } from "../../application/utils.ts";
import LoadingIndicator from "../../../views/application/LoadingIndicatior.tsx";
import { InventoryUploadCollection } from "./InventoryUploadCollection.tsx";

export const InventoryUploadContainer: React.FC = () => {
    const Content: React.FC = () => {
        // TODO: InventoryProviderとuseInventoryContextを実装後に置き換える
        const loading = false;
        const setError = (error: string) => console.error(error);

        useEffect(() => {
            (async () => {
                try {
                    // TODO: 在庫アップロード関連データの取得処理を実装
                    console.log("在庫アップロード画面を初期化中...");
                } catch (error: any) {
                    showErrorMessage(`在庫アップロード画面の初期化に失敗しました: ${error?.message}`, setError);
                }
            })();
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    <InventoryUploadCollection/>
                )}
            </>
        );
    };

    return (
        // TODO: InventoryProviderでラップ
        <Content />
    );
};