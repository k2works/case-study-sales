import React from "react";
import ErrorBoundary from "./ErrorBoundary";
import HomeSingleView from "../../views/application/Home.tsx";
import {SiteLayout} from "../../views/SiteLayout.tsx";

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
