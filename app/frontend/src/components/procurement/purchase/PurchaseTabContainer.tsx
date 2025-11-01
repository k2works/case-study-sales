import React from "react";
import { PurchaseContainer } from "./list/PurchaseContainer.tsx";
import { PurchaseRuleContainer } from "./rule/PurchaseRuleContainer.tsx";
import {useTab} from "../../application/hooks.ts";
import {SiteLayout} from "../../../views/SiteLayout.tsx";

export const PurchaseTabContainer: React.FC = () => {
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
                    <Tab>ルール</Tab>
                </TabList>
                <TabPanel>
                    <PurchaseContainer />
                </TabPanel>
                <TabPanel>
                    <PurchaseRuleContainer />
                </TabPanel>
            </Tabs>
        </SiteLayout>
    );
};
