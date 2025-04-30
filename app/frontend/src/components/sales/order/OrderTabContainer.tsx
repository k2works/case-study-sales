import {useTab} from "../../application/hooks";
import {SiteLayout} from "../../../views/SiteLayout";
import {OrderContainer} from "./list/OrderContainer.tsx";
import {OrderUploadContainer} from "./upload/OrderUploadContainer.tsx";
import {OrderRuleContainer} from "./rule/OrderRuleContainer.tsx";

export const OrderTabContainer: React.FC = () => {
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
                    <OrderContainer/>
                </TabPanel>
                <TabPanel>
                    <OrderUploadContainer/>
                </TabPanel>
                <TabPanel>
                    <OrderRuleContainer/>
                </TabPanel>
            </Tabs>
        </SiteLayout>
    );
}
