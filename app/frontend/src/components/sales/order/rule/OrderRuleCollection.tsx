import {useState} from "react";
import {RuleCheckResultType} from "../../../../services/sales/order.ts";
import {useSalesOrderContext} from "../../../../providers/sales/Order.tsx";
import {SalesOrderRuleCollectionView} from "../../../../views/sales/order/rule/OrderRuleCollection.tsx";

export const OrderRuleCollection: React.FC = () => {
    const [ruleResults, setRuleResults] = useState<RuleCheckResultType[]>([]);
    const {salesOrderService} = useSalesOrderContext();

    const handleExecuteRuleCheck = async () => {
        try {
            const results = await salesOrderService.check();
            setRuleResults(prevResults => [...prevResults, ...results]);
        } catch (error) {
            const errorMessage = error instanceof Error ? error.message : '不明なエラーが発生しました';
            console.error('Rule check failed:', errorMessage);
        }
    };

    const handleDeleteRuleResult = (index: number) => {
        setRuleResults(prevResults => prevResults.filter((_, i) => i !== index));
    };

    return (
        <SalesOrderRuleCollectionView
            ruleHeaderItems={{handleExecuteRuleCheck}}
            ruleResults={ruleResults}
            handleDeleteRuleResult={handleDeleteRuleResult}
        />
    );
};