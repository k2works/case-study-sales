import React, {useEffect } from 'react';
import {showErrorMessage} from "../../../application/utils.ts";
import LoadingIndicator from "../../../../views/application/LoadingIndicatior.tsx";
import {CustomerProvider, useCustomerContext} from "../../../../providers/master/partner/Customer.tsx";
import {RegionProvider, useRegionContext} from "../../../../providers/master/code/Region.tsx";
import {CustomerCollection} from "./CustomerCollection.tsx";

export const CustomerContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            setError,
            fetchCustomers,
        } = useCustomerContext();

        const {
            fetchRegions,
        } = useRegionContext();

        useEffect(() => {
            (async () => {
                try {
                    await Promise.all([
                        fetchCustomers.load(),
                        fetchRegions.load()
                    ]);
                } catch (error: any) {
                    showErrorMessage(`顧客情報の取得に失敗しました: ${error?.message}`, setError);
                }
            })();
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator />
                ) : (
                    <CustomerCollection/>
                )}
            </>
        );
    };

    return (
        <CustomerProvider>
            <RegionProvider>
                <Content />
            </RegionProvider>
        </CustomerProvider>
    );
};