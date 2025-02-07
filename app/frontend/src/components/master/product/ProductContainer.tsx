import React from "react";
import {useTab} from "../../application/hooks.ts";
import {SiteLayout} from "../../../views/SiteLayout.tsx";
import {ProductCategoryContainer} from "./category/ProductCategoryContainer.tsx";
import {ProductItemContainer} from "./item/ProductItemContainer.tsx";

export const ProductContainer: React.FC = () => {
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
                    <Tab>アイテム</Tab>
                </TabList>
                <TabPanel>
                    <ProductCategoryContainer/>
                </TabPanel>
                <TabPanel>
                    <ProductItemContainer/>
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
