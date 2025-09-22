import React from "react";
import LoadingIndicator from "../../../views/application/LoadingIndicatior.tsx";
import { InventoryProvider, useInventoryContext } from "../../../providers/inventory/Inventory.tsx";
import { InventoryUploadCollection } from "./InventoryUploadCollection.tsx";

export const InventoryUploadContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading
        } = useInventoryContext();

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
        <InventoryProvider>
            <Content />
        </InventoryProvider>
    );
};