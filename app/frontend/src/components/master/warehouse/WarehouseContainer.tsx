import React from "react";
import {SiteLayout} from "../../../views/SiteLayout.tsx";
import LoadingIndicator from "../../../views/application/LoadingIndicatior.tsx";
import {WarehouseProvider, useWarehouseContext} from "../../../providers/master/Warehouse.tsx";
import {WarehouseCollection} from "./WarehouseCollection.tsx";

export const WarehouseContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading
        } = useWarehouseContext();

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
        <SiteLayout>
            <WarehouseProvider>
                <Content/>
            </WarehouseProvider>
        </SiteLayout>
    );
};