import React from "react";
import {useTab} from "../application/hooks.ts";
import {SiteLayout} from "../../views/SiteLayout.tsx";

export const Code: React.FC = () => {
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
                    <Tab>地域</Tab>
                </TabList>
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
