import {useTab} from "../../application/hooks";
import {SiteLayout} from "../../../views/SiteLayout";
import {ShippingContainer} from "./list/ShippingContainer";
import {ShippingRuleContainer} from "./rule/ShippingRuleContainer";
import {ShippingOrderContainer} from "./order/ShippingOrderContainer.tsx";

export const ShippingTabContainer: React.FC = () => {
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
                    <Tab>指示</Tab>
                </TabList>
                <TabPanel>
                    <ShippingContainer/>
                </TabPanel>
                <TabPanel>
                    <ShippingRuleContainer/>
                </TabPanel>
                <TabPanel>
                    <ShippingOrderContainer/>
                </TabPanel>
            </Tabs>
        </SiteLayout>
    );
}
