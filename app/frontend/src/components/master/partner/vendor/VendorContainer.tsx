import React, { useEffect } from 'react';
import LoadingIndicator from "../../../../views/application/LoadingIndicatior.tsx";
import {useVendorContext, VendorProvider} from "../../../../providers/Vendor.tsx";
import {VendorCollection} from "./VendorCollection.tsx";

export const VendorContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            fetchVendors,
        } = useVendorContext();

        useEffect(() => {
            fetchVendors.load();
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator />
                ) : (
                    <VendorCollection/>
                )}
            </>
        );
    };
    return (
        <VendorProvider>
            <Content />
        </VendorProvider>
    );
};