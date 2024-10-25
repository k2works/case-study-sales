import React from "react";
import ErrorBoundary from "./ErrorBoundary";
import HomeSingleView from "../../ui/application/Home.tsx";
import {SiteLayout} from "../../ui/SiteLayout.tsx";

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
