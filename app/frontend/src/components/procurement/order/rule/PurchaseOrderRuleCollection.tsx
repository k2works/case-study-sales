import { useState } from "react";
import { RuleCheckResultType } from "../../../../services/procurement/purchaseOrder.ts";
import { usePurchaseOrderContext } from "../../../../providers/procurement/PurchaseOrder.tsx";
import { PurchaseOrderRuleCollectionView } from "../../../../views/procurement/order/rule/PurchaseOrderRuleCollection.tsx";

export const PurchaseOrderRuleCollection: React.FC = () => {
    const [ruleResults, setRuleResults] = useState<RuleCheckResultType[]>([]);
    const { purchaseOrderService } = usePurchaseOrderContext();

    const handleExecuteRuleCheck = async () => {
        try {
            const results = await purchaseOrderService.check();
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
        <PurchaseOrderRuleCollectionView
            ruleHeaderItems={{ handleExecuteRuleCheck }}
            ruleResults={ruleResults}
            handleDeleteRuleResult={handleDeleteRuleResult}
        />
    );
};