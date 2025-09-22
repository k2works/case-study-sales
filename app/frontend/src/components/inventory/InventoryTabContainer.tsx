import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { InventoryContainer } from "./list/InventoryContainer.tsx";
import { InventoryUploadContainer } from "./upload/InventoryUploadContainer.tsx";
import { InventoryRuleContainer } from "./rule/InventoryRuleContainer.tsx";
import {useTab} from "../application/hooks.ts";
import {SiteLayout} from "../../views/SiteLayout.tsx";

export const InventoryTabContainer: React.FC = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const [tabIndex, setTabIndex] = useState(0);

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
                    <Tab>一覧</Tab>
                    <Tab>一括登録</Tab>
                    <Tab>ルール</Tab>
                </TabList>
                <TabPanel>
                    <InventoryContainer />
                </TabPanel>
                <TabPanel>
                    <InventoryUploadContainer />
                </TabPanel>
                <TabPanel>
                    <InventoryRuleContainer />
                </TabPanel>
            </Tabs>
        </SiteLayout>
    );
};