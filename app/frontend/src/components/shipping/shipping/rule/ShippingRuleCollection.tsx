import {useState} from "react";
import {ShippingRuleCheckResponse} from "../../../../services/shipping/shipping";
import {useShippingContext} from "../../../../providers/sales/Shipping";
import {ShippingRuleCollectionView} from "../../../../views/shipping/shipping/rule/ShippingRuleCollection";

export const ShippingRuleCollection: React.FC = () => {
    const [ruleResults, setRuleResults] = useState<ShippingRuleCheckResponse[]>([]);
    const {shippingService} = useShippingContext();

    const handleExecuteRuleCheck = async () => {
        try {
            const result = await shippingService.checkRule();
            setRuleResults(prevResults => [...prevResults, result]);
        } catch (error) {
            const errorMessage = error instanceof Error ? error.message : '不明なエラーが発生しました';
            console.error('Rule check failed:', errorMessage);
        }
    };

    const handleDeleteRuleResult = (index: number) => {
        setRuleResults(prevResults => prevResults.filter((_, i) => i !== index));
    };

    return (
        <ShippingRuleCollectionView
            ruleHeaderItems={{handleExecuteRuleCheck}}
            ruleResults={ruleResults}
            handleDeleteRuleResult={handleDeleteRuleResult}
        />
    );
};