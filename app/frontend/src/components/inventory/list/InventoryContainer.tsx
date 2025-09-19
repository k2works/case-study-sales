import React, {useEffect} from "react";
import {showErrorMessage} from "../../application/utils.ts";
import LoadingIndicator from "../../../views/application/LoadingIndicatior.tsx";
import { InventoryProvider, useInventoryContext } from "../../../providers/inventory/Inventory.tsx";
import { WarehouseProvider, useWarehouseContext } from "../../../providers/master/Warehouse.tsx";
import { ProductItemProvider, useProductItemContext } from "../../../providers/master/product/ProductItem.tsx";
import { InventoryCollection } from "./InventoryCollection.tsx";

export const InventoryContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            setError,
            fetchInventories,
        } = useInventoryContext();

        const {
            fetchWarehouses,
        } = useWarehouseContext();

        const {
            fetchProducts,
        } = useProductItemContext();

        useEffect(() => {
            (async () => {
                try {
                    await Promise.all([
                        fetchInventories.load(),
                        fetchWarehouses.load(),
                        fetchProducts.load(),
                    ]);
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
            <WarehouseProvider>
                <ProductItemProvider>
                    <Content />
                </ProductItemProvider>
            </WarehouseProvider>
        </InventoryProvider>
    );
};