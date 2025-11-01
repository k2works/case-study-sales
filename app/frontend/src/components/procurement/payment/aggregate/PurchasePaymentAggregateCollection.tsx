import {useState} from "react";
import {PaymentAggregateResponse} from "../../../../services/procurement/payment";
import {usePaymentContext} from "../../../../providers/procurement/Payment";
import {PurchasePaymentAggregateCollectionView} from "../../../../views/procurement/payment/aggregate/PurchasePaymentAggregateCollection";

export const PurchasePaymentAggregateCollection: React.FC = () => {
    const [aggregateResults, setAggregateResults] = useState<PaymentAggregateResponse[]>([]);
    const {paymentService} = usePaymentContext();

    const handleExecuteAggregate = async () => {
        try {
            const result = await paymentService.aggregate();
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
        <PurchasePaymentAggregateCollectionView
            aggregateHeaderItems={{handleExecuteAggregate}}
            aggregateResults={aggregateResults}
            handleDeleteAggregateResult={handleDeleteAggregateResult}
        />
    );
};
