import React, { useEffect } from 'react';
import LoadingIndicator from "../../../../views/application/LoadingIndicatior.tsx";
import {PartnerGroupProvider, usePartnerGroupContext} from "../../../../providers/PartnerGroup.tsx";
import {PartnerGroupCollection} from "./PartnerGroupCollection.tsx";

export const PartnerGroupContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            fetchPartnerGroups,
        } = usePartnerGroupContext();

        useEffect(() => {
            fetchPartnerGroups.load();
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator />
                ) : (
                    <PartnerGroupCollection/>
                )}
            </>
        );
    };

    return (
        <PartnerGroupProvider>
            <Content />
        </PartnerGroupProvider>
    );
};