import {useState} from "react";
import {PaymentAggregateResponse} from "../../../../services/sales/payment.ts";
import {usePaymentContext} from "../../../../providers/sales/Payment.tsx";
import {PaymentAggregateCollectionView} from "../../../../views/sales/payment/aggregate/PaymentAggregateCollection.tsx";

export const PaymentAggregateCollection: React.FC = () => {
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
        <PaymentAggregateCollectionView
            aggregateHeaderItems={{handleExecuteAggregate}}
            aggregateResults={aggregateResults}
            handleDeleteAggregateResult={handleDeleteAggregateResult}
        />
    );
};