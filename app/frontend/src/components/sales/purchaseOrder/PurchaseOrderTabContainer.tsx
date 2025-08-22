import { PurchaseOrderContainer } from "./list/PurchaseOrderContainer.tsx";
import {useTab} from "../../application/hooks.ts";
import {SiteLayout} from "../../../views/SiteLayout.tsx";

export const PurchaseOrderTabContainer: React.FC = () => {
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
                    <PurchaseOrderContainer />;
                </TabPanel>
                <TabPanel>
                    // TODO: 発注アップロードコンテナを実装
                </TabPanel>
                <TabPanel>
                    // TODO: 発注ルールコンテナを実装
                </TabPanel>
            </Tabs>
        </SiteLayout>
    );
};