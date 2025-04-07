import {useTab} from "../../application/hooks";
import {SiteLayout} from "../../../views/SiteLayout";
import {ShippingContainer} from "./list/ShippingContainer";

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
                    <Tab>出荷指示</Tab>
                </TabList>
                <TabPanel>
                    <ShippingContainer/>
                </TabPanel>
                <TabPanel>
                    {/* ShippingRuleContainer will be implemented later */}
                    <div>出荷ルール画面（実装予定）</div>
                </TabPanel>
                <TabPanel>
                    {/* ShippingOrderContainer will be implemented later */}
                    <div>出荷指示画面（実装予定）</div>
                </TabPanel>
            </Tabs>
        </SiteLayout>
    );
}