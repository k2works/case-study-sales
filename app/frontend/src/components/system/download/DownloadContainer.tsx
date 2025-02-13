import React from "react";
import {SiteLayout} from "../../../views/SiteLayout.tsx";
import LoadingIndicator from "../../../views/application/LoadingIndicatior.tsx";
import {DownloadProvider, useDownloadContext} from "../../../providers/system/Download.tsx";
import {DownloadSingle} from "./DownloadSingle.tsx";

export const DownloadContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
        } = useDownloadContext();

        return (
            <>
                {loading ? (
                    <LoadingIndicator />
                ) : (
                    <DownloadSingle/>
                )}
            </>
        );
    };

    return (
        <SiteLayout>
            <DownloadProvider>
                <Content />
            </DownloadProvider>
        </SiteLayout>
    );
};
