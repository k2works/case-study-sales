import {useState} from "react";
import {InvoiceAggregateResponse} from "../../../../services/sales/invoice";
import {useInvoiceContext} from "../../../../providers/sales/Invoice";
import {InvoiceAggregateCollectionView} from "../../../../views/sales/invoice/aggregate/InvoiceAggregateCollection";

export const InvoiceAggregateCollection: React.FC = () => {
    const [aggregateResults, setAggregateResults] = useState<InvoiceAggregateResponse[]>([]);
    const {invoiceService} = useInvoiceContext();

    const handleExecuteAggregate = async () => {
        try {
            const result = await invoiceService.aggregate();
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
        <InvoiceAggregateCollectionView
            aggregateHeaderItems={{handleExecuteAggregate}}
            aggregateResults={aggregateResults}
            handleDeleteAggregateResult={handleDeleteAggregateResult}
        />
    );
};