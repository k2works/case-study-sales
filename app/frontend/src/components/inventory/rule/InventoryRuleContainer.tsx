import React from "react";
import LoadingIndicator from "../../../views/application/LoadingIndicatior.tsx";
import { InventoryProvider, useInventoryContext } from "../../../providers/inventory/Inventory.tsx";
import { InventoryRuleCollection } from "./InventoryRuleCollection.tsx";

export const InventoryRuleContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading
        } = useInventoryContext();

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
        <InventoryProvider>
            <Content />
        </InventoryProvider>
    );
};