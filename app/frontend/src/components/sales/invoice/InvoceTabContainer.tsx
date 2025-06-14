import {useTab} from "../../application/hooks";
import {SiteLayout} from "../../../views/SiteLayout";
import {InvoiceContainer} from "./list/InvoiceContainer.tsx";
import {InvoiceAggregateContainer} from "./aggregate/InvoiceAggregateContainer.tsx";

export const InvoiceTabContainer: React.FC = () => {
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
                    <Tab>集計</Tab>
                </TabList>
                <TabPanel>
                    <InvoiceContainer/>
                </TabPanel>
                <TabPanel>
                    <InvoiceAggregateContainer/>
                </TabPanel>
            </Tabs>
        </SiteLayout>
    );
}