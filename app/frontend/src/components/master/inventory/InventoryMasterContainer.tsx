import React from "react";
import {useTab} from "../../application/hooks.ts";
import {SiteLayout} from "../../../views/SiteLayout.tsx";
import {WarehouseContainer} from "./warehouse/WarehouseContainer.tsx";
import {LocationNumberContainer} from "./locationnumber/LocationNumberContainer.tsx";

export const InventoryMasterContainer: React.FC = () => {
    const {
        Tab,
        TabList,
        TabPanel,
        Tabs,
    } = useTab();

    return (
        <SiteLayout>
            <Tabs>
                <TabList>
                    <Tab>倉庫</Tab>
                    <Tab>棚番</Tab>
                </TabList>
                <TabPanel>
                    <WarehouseContainer/>
                </TabPanel>
                <TabPanel>
                    <LocationNumberContainer/>
                </TabPanel>
            </Tabs>
        </SiteLayout>
    );
}