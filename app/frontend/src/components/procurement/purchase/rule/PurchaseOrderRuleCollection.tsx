import { useState } from "react";
import { RuleCheckResultType } from "../../../../services/procurement/purchase.ts";
import { usePurchaseContext } from "../../../../providers/procurement/Purchase.tsx";
import { PurchaseOrderRuleCollectionView } from "../../../../views/procurement/purchase/rule/PurchaseOrderRuleCollection.tsx";

export const PurchaseOrderRuleCollection: React.FC = () => {
    const [ruleResults, setRuleResults] = useState<RuleCheckResultType[]>([]);
    const { purchaseOrderService } = usePurchaseContext();

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