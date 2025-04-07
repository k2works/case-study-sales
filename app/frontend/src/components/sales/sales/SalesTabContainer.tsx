import {useTab} from "../../application/hooks";
import {SiteLayout} from "../../../views/SiteLayout";
import {SalesContainer} from "./list/SalesContainer";
import {SalesSummaryContainer} from "./summary/SalesSummaryContainer";

export const SalesTabContainer: React.FC = () => {
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
                    <Tab>売上集計</Tab>
                </TabList>
                <TabPanel>
                    <SalesContainer/>
                </TabPanel>
                <TabPanel>
                    <SalesSummaryContainer/>
                </TabPanel>
            </Tabs>
        </SiteLayout>
    );
}