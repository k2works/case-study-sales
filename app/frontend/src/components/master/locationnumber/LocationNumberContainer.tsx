import React from "react";
import {SiteLayout} from "../../../views/SiteLayout.tsx";
import {LocationNumberProvider} from "../../../providers/master/LocationNumber.tsx";
import {LocationNumberCollection} from "./LocationNumberCollection.tsx";

export const LocationNumberContainer: React.FC = () => {
    return (
        <SiteLayout>
            <LocationNumberProvider>
                <LocationNumberCollection/>
            </LocationNumberProvider>
        </SiteLayout>
    );
};