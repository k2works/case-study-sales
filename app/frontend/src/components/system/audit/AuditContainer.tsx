import React, {useEffect} from "react";
import {SiteLayout} from "../../../views/SiteLayout.tsx";
import LoadingIndicator from "../../../views/application/LoadingIndicatior.tsx";
import {AuditProvider, useAuditContext} from "../../../providers/Audit.tsx";
import {AuditCollection} from "./AuditCollection.tsx";

export const AuditContainer: React.FC = () => {
    const Content: React.FC = () => {
        const {
            loading,
            fetchAudits,
        } = useAuditContext();

        useEffect(() => {
            fetchAudits.load().then(() => {
            });
        }, []);

        return (
            <>
                {loading ? (
                    <LoadingIndicator/>
                ) : (
                    <AuditCollection/>
                )}
            </>
        );
    };

    return (
        <SiteLayout>
            <AuditProvider>
                <Content/>
            </AuditProvider>
        </SiteLayout>
    );
};