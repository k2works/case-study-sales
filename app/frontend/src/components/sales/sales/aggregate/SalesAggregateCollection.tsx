import {useState} from "react";
import {SalesAggregateResponse} from "../../../../services/sales/sales";
import {useSalesContext} from "../../../../providers/sales/Sales";
import {SalesAggregateCollectionView} from "../../../../views/sales/sales/aggregate/SalesAggregateCollection";

export const SalesAggregateCollection: React.FC = () => {
    const [aggregateResults, setAggregateResults] = useState<SalesAggregateResponse[]>([]);
    const {salesService} = useSalesContext();

    const handleExecuteAggregate = async () => {
        try {
            const result = await salesService.aggregate();
            setAggregateResults(prevResults => [...prevResults, result]);
        } catch (error) {
            const errorMessage = error instanceof Error ? error.message : '不明なエラーが発生しました';
            console.error('Aggregate failed:', errorMessage);
        }
    };

    const handleDeleteAggregateResult = (index: number) => {
        setAggregateResults(prevResults => prevResults.filter((_, i) => i !== index));
    };

    return (
        <SalesAggregateCollectionView
            aggregateHeaderItems={{handleExecuteAggregate}}
            aggregateResults={aggregateResults}
            handleDeleteAggregateResult={handleDeleteAggregateResult}
        />
    );
};