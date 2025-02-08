import React, { useEffect } from 'react';
import { showErrorMessage } from "../../../application/utils.ts";
import LoadingIndicator from "../../../../views/application/LoadingIndicatior.tsx";
import {PartnerListProvider, usePartnerListContext} from "../../../../providers/PartnerList.tsx";
import {PartnerGroupProvider, usePartnerGroupContext} from "../../../../providers/PartnerGroup.tsx";
import {PartnerListCollection} from "./PartnerListCollection.tsx";

export const PartnerListContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            setError,
            fetchPartners,
        } = usePartnerListContext();

        const {
            fetchPartnerGroups,
        } = usePartnerGroupContext();

        useEffect(() => {
            (async () => {
                try {
                    await Promise.all([
                        fetchPartners.load(),
                        fetchPartnerGroups.load()
                    ]);
                } catch (error: any) {
                    showErrorMessage(`取引先情報の取得に失敗しました: ${error?.message}`, setError);
                }
            })();
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator />
                ) : (
                    <PartnerListCollection/>
                )}
            </>
        );
    };

    return (
        <PartnerGroupProvider>
            <PartnerListProvider>
                <Content />
            </PartnerListProvider>
        </PartnerGroupProvider>
    );
};