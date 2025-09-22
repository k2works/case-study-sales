import React, { useState } from "react";
import { RuleCheckResultType } from "../../../services/inventory/inventory.ts";
import { useInventoryContext } from "../../../providers/inventory/Inventory.tsx";
import { InventoryRuleCollectionView } from "../../../views/inventory/rule/InventoryRuleCollection.tsx";

export const InventoryRuleCollection: React.FC = () => {
    const [ruleResults, setRuleResults] = useState<RuleCheckResultType[]>([]);
    const { inventoryService } = useInventoryContext();

    const handleExecuteRuleCheck = async () => {
        try {
            const results = await inventoryService.check();
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
        <InventoryRuleCollectionView
            ruleHeaderItems={{ handleExecuteRuleCheck }}
            ruleResults={ruleResults}
            handleDeleteRuleResult={handleDeleteRuleResult}
        />
    );
};