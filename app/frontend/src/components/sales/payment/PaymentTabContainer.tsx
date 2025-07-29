import {useTab} from "../../application/hooks";
import {SiteLayout} from "../../../views/SiteLayout";
import {PaymentContainer} from "./list/PaymentContainer.tsx";
import {PaymentAggregateContainer} from "./aggregate/PaymentAggregateContainer.tsx";

export const PaymentTabContainer: React.FC = () => {
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
                    <PaymentContainer/>
                </TabPanel>
                <TabPanel>
                    <PaymentAggregateContainer/>
                </TabPanel>
            </Tabs>
        </SiteLayout>
    );
}