import React from "react";
import {Message} from "../../../../components/application/Message.tsx";
import {ShippingRuleCheckResponse} from "../../../../services/sales/shipping.ts";

interface ShippingRuleCollectionViewProps {
    ruleHeaderItems: {
        handleExecuteRuleCheck: () => void;
    };
    ruleResults: ShippingRuleCheckResponse[];
    handleDeleteRuleResult: (index: number) => void;
}

export const ShippingRuleCollectionView: React.FC<ShippingRuleCollectionViewProps> = ({
                                                                                              ruleHeaderItems: { handleExecuteRuleCheck},
                                                                                              ruleResults,
                                                                                              handleDeleteRuleResult,
                                                                                          }) => {
    return (
        <div className="collection-view-object-container">
            <div className="collection-view-container">
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h1 className="single-view-title">出荷ルールチェック</h1>
                    </div>
                </div>
                <div className="collection-view-content">
                    <div className="button-container">
                        <button className="action-button" onClick={handleExecuteRuleCheck} id="execute">
                            実行
                        </button>
                    </div>
                    <div className="collection-object-container">
                        <ul className="collection-object-list">
                            {ruleResults.map((result, index) => (
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
                                        onClick={() => handleDeleteRuleResult(index)}
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