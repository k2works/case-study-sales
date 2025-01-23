import React from "react";
import {useTab} from "../application/hooks.ts";
import {SiteLayout} from "../../views/SiteLayout.tsx";
import {PartnerCategory} from "./PartnerCategory.tsx";
import {PartnerGroup} from "./PartnerGroup.tsx";
import {PartnerList} from "./PartnerList.tsx";

export const Partner: React.FC = () => {
    const {
        Tab,
        TabList,
        TabPanel,
        Tabs,
    } = useTab();

    const Content: React.FC = () => {
        return (
            <Tabs>
                <TabList>
                    <Tab>分類</Tab>
                    <Tab>グループ</Tab>
                    <Tab>一覧</Tab>
                    <Tab>顧客</Tab>
                    <Tab>仕入先</Tab>
                </TabList>
                <TabPanel>
                    <PartnerCategory/>
                </TabPanel>
                <TabPanel>
                    <PartnerGroup/>
                </TabPanel>
                <TabPanel>
                    <PartnerList/>
                </TabPanel>
                <TabPanel>
                    <></>
                </TabPanel>
                <TabPanel>
                    <></>
                </TabPanel>
                <TabPanel>
                    <></>
                </TabPanel>
            </Tabs>
        );
    }

    return (
        <SiteLayout>
            <Content/>
        </SiteLayout>
    );
}
