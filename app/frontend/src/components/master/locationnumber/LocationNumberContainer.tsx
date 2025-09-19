import React, {useEffect} from "react";
import {showErrorMessage} from "../../application/utils.ts";
import {SiteLayout} from "../../../views/SiteLayout.tsx";
import LoadingIndicator from "../../../views/application/LoadingIndicatior.tsx";
import {LocationNumberProvider, useLocationNumberContext} from "../../../providers/master/LocationNumber.tsx";
import {LocationNumberCollection} from "./LocationNumberCollection.tsx";

export const LocationNumberContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            setError,
            fetchLocationNumbers,
        } = useLocationNumberContext();

        useEffect(() => {
            (async () => {
                try {
                    await fetchLocationNumbers.load();
                } catch (error: any) {
                    showErrorMessage(`棚番情報の取得に失敗しました: ${error?.message}`, setError);
                }
            })();
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    <LocationNumberCollection/>
                )}
            </>
        );
    };

    return (
        <SiteLayout>
            <LocationNumberProvider>
                <Content/>
            </LocationNumberProvider>
        </SiteLayout>
    );
};