import React from "react";
import { usePurchaseReceiptContext } from "../../../../providers/procurement/Purchase.tsx";
import { PurchaseRuleCollectionView } from "../../../../views/procurement/purchase/rule/PurchaseRuleCollection.tsx";

export const PurchaseRuleCollection: React.FC = () => {
    const {
        ruleCheckResults,
        setRuleCheckResults,
        purchaseService,
    } = usePurchaseReceiptContext();

    const handleExecuteRuleCheck = async () => {
        try {
            const result = await purchaseService.check();
            setRuleCheckResults(prevResults => [...prevResults, ...result]);
        } catch (error) {
            const errorMessage = error instanceof Error ? error.message : '不明なエラーが発生しました';
            console.error('Rule check failed:', errorMessage);
        }
    };

    const handleDeleteRuleCheckResult = (index: number) => {
        setRuleCheckResults(prevResults => prevResults.filter((_, i) => i !== index));
    };

    return (
        <PurchaseRuleCollectionView
            ruleCheckHeaderItems={{handleExecuteRuleCheck}}
            ruleCheckResults={ruleCheckResults}
            handleDeleteRuleCheckResult={handleDeleteRuleCheckResult}
        />
    );
};
