import React, { useEffect} from "react";
import LoadingIndicator from "../../../../views/application/LoadingIndicatior.tsx";
import {RegionProvider, useRegionContext} from "../../../../providers/Region.tsx";
import {RegionCollection} from "./RegionCollection.tsx";

export const RegionContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            fetchRegions,
        } = useRegionContext();

        useEffect(() => {
            fetchRegions.load().then(() => {});
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator />
                ) : (
                    <RegionCollection/>
                )}
            </>
        );
    };

    return (
        <RegionProvider>
            <Content />
        </RegionProvider>
    );
};