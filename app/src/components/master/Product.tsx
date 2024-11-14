import React from "react";
import {useTab} from "../application/hooks.ts";
import {ProductCategory} from "./ProductCategory.tsx";
import {ProductDetail} from "./ProductDetail.tsx";
import {SiteLayout} from "../../views/SiteLayout.tsx";

export const Product: React.FC = () => {
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
                    <Tab>詳細</Tab>
                </TabList>
                <TabPanel>
                    <ProductCategory/>
                </TabPanel>
                <TabPanel>
                    <ProductDetail/>
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
