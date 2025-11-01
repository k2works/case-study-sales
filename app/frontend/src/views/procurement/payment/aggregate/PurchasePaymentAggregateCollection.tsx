import React from "react";
import {Message} from "../../../../components/application/Message.tsx";
import {PaymentAggregateResponse} from "../../../../services/procurement/payment.ts";

interface PurchasePaymentAggregateCollectionViewProps {
    aggregateHeaderItems: {
        handleExecuteAggregate: () => void;
    };
    aggregateResults: PaymentAggregateResponse[];
    handleDeleteAggregateResult: (index: number) => void;
}

export const PurchasePaymentAggregateCollectionView: React.FC<PurchasePaymentAggregateCollectionViewProps> = ({
                                                                                              aggregateHeaderItems: { handleExecuteAggregate },
                                                                                              aggregateResults,
                                                                                              handleDeleteAggregateResult,
                                                                                          }) => {
    return (
        <div className="collection-view-object-container">
            <div className="collection-view-container">
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h1 className="single-view-title">支払集計</h1>
                    </div>
                </div>
                <div className="collection-view-content">
                    <div className="button-container">
                        <button className="action-button" onClick={handleExecuteAggregate} id="execute">
                            実行
                        </button>
                    </div>
                    <div className="collection-object-container">
                        <ul className="collection-object-list">
                            {aggregateResults.map((result, index) => (
                                <div key={index} className="upload-result-item">
                                    <div className="check-result-message">
                                        <Message error={null} message={result.message}/>
                                        {result.details && result.details.length > 0 && (
                                            <div className="upload-result-details">
                                                {result.details.map((detail, detailIndex) => (
                                                    <div key={detailIndex} className="upload-result-detail">
                                                        {Object.entries(detail).map(([key, value]) => (
                                                            <div key={key} className="detail-item">
                                                                <span className="detail-key">{key}:</span>
                                                                <span className="detail-value">
                                                                  {typeof value === 'object' && value !== null
                                                                      ? JSON.stringify(value)
                                                                      : String(value)}
                                                                </span>
                                                            </div>
                                                        ))}
                                                    </div>
                                                ))}
                                            </div>
                                        )}
                                    </div>
                                    <button
                                        className="action-button"
                                        onClick={() => handleDeleteAggregateResult(index)}
                                    >
                                        x
                                    </button>
                                </div>
                            ))}
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    );
};
