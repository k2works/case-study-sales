import {useTab} from "../../application/hooks";
import {SiteLayout} from "../../../views/SiteLayout";
import {InvoiceContainer} from "./list/InvoiceContainer.tsx";

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
                </TabList>
                <TabPanel>
                    <InvoiceContainer/>
                </TabPanel>
            </Tabs>
        </SiteLayout>
    );
}