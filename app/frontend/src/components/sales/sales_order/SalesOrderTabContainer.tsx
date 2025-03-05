import React from "react";
import {useTab} from "../../application/hooks.ts";
import {SiteLayout} from "../../../views/SiteLayout.tsx";
import {SalesOrderContainer} from "./list/SalesOrderContainer.tsx";

export const SalesOrderTabContainer: React.FC = () => {
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
                    <SalesOrderContainer/>
                </TabPanel>
                <TabPanel>
                </TabPanel>
                <TabPanel>
                </TabPanel>
            </Tabs>
        </SiteLayout>
    );
}
