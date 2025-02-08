import React from "react";
import {useTab} from "../../application/hooks.ts";
import {SiteLayout} from "../../../views/SiteLayout.tsx";
import {PartnerCategoryContainer} from "./category/PartnerCategoryContainer.tsx";
import {PartnerGroupContainer} from "./group/PartnerGroupContainer.tsx";
import {PartnerListContainer} from "./list/PartnerListContainer.tsx";
import {CustomerContainer} from "./customer/CustomerContainer.tsx";
import {VendorContainer} from "./vendor/VendorContainer.tsx";

export const PartnerContainer: React.FC = () => {
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
                    <Tab>分類</Tab>
                    <Tab>グループ</Tab>
                    <Tab>一覧</Tab>
                    <Tab>顧客</Tab>
                    <Tab>仕入先</Tab>
                </TabList>
                <TabPanel>
                    <PartnerCategoryContainer/>
                </TabPanel>
                <TabPanel>
                    <PartnerGroupContainer/>
                </TabPanel>
                <TabPanel>
                    <PartnerListContainer/>
                </TabPanel>
                <TabPanel>
                    <CustomerContainer/>
                </TabPanel>
                <TabPanel>
                    <VendorContainer/>
                </TabPanel>
            </Tabs>
        </SiteLayout>
    );
}
