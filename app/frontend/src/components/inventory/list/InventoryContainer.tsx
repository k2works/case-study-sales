import React from "react";
import LoadingIndicator from "../../../views/application/LoadingIndicatior.tsx";
import { InventoryProvider, useInventoryContext } from "../../../providers/inventory/Inventory.tsx";
import { InventoryCollection } from "./InventoryCollection.tsx";

export const InventoryContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading
        } = useInventoryContext();

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