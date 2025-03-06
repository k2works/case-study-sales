import {useTab} from "../../application/hooks";
import {SiteLayout} from "../../../views/SiteLayout";
import {SalesOrderContainer} from "./list/SalesOrderContainer";
import {SalesOrderUploadContainer} from "./upload/SalesOrderUploadContainer";
import {SalesOrderRuleContainer} from "./rule/SalesOrderRuleContainer";

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
                    <SalesOrderUploadContainer/>
                </TabPanel>
                <TabPanel>
                    <SalesOrderRuleContainer/>
                </TabPanel>
            </Tabs>
        </SiteLayout>
    );
}
