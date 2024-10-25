import React from "react";
import ErrorBoundary from "./ErrorBoundary";
import {SiteLayout} from "../../ui/application/SiteLayout.tsx";
import HomeSingleView from "../../ui/application/Home.tsx";

export const Home: React.FC = () => {
    const Content: React.FC = () => {
        return (
            <HomeSingleView/>
        );
    }

    return (
        <SiteLayout>
            <ErrorBoundary>
                <Content/>
            </ErrorBoundary>
        </SiteLayout>
    )
}
