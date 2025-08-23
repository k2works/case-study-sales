import React from "react";
import { PurchaseOrderContainer } from "./list/PurchaseOrderContainer.tsx";
import { PurchaseOrderUploadContainer } from "./upload/PurchaseOrderUploadContainer.tsx";
import { PurchaseOrderRuleContainer } from "./rule/PurchaseOrderRuleContainer.tsx";
import {useTab} from "../../application/hooks.ts";
import {SiteLayout} from "../../../views/SiteLayout.tsx";

export const PurchaseOrderTabContainer: React.FC = () => {
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
                    <PurchaseOrderContainer />
                </TabPanel>
                <TabPanel>
                    <PurchaseOrderUploadContainer />
                </TabPanel>
                <TabPanel>
                    <PurchaseOrderRuleContainer />
                </TabPanel>
            </Tabs>
        </SiteLayout>
    );
};