import React from "react";
import {useTab} from "../application/hooks.ts";
import {SiteLayout} from "../../views/SiteLayout.tsx";

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
                    <></>
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
