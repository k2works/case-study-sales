import React, {useEffect} from "react";
import {showErrorMessage} from "../../../application/utils.ts";
import LoadingIndicator from "../../../../views/application/LoadingIndicatior.tsx";
import {WarehouseProvider, useWarehouseContext} from "../../../../providers/master/Warehouse.tsx";
import {WarehouseCollection} from "./WarehouseCollection.tsx";

export const WarehouseContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            setError,
            fetchWarehouses,
        } = useWarehouseContext();

        useEffect(() => {
            (async () => {
                try {
                    await fetchWarehouses.load();
                } catch (error: any) {
                    showErrorMessage(`倉庫情報の取得に失敗しました: ${error?.message}`, setError);
                }
            })();
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    <WarehouseCollection/>
                )}
            </>
        );
    };

    return (
        <WarehouseProvider>
            <Content/>
        </WarehouseProvider>
    );
};