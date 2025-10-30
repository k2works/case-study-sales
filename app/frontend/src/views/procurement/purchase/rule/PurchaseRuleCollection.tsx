import React from "react";
import { RuleCheckResultType } from "../../../../services/procurement/purchase.ts";
import { Message } from "../../../../components/application/Message.tsx";

interface PurchaseRuleCollectionViewProps {
    ruleCheckHeaderItems: {
        handleExecuteRuleCheck: () => void;
    };
    ruleCheckResults: RuleCheckResultType[];
    handleDeleteRuleCheckResult: (index: number) => void;
}

export const PurchaseRuleCollectionView: React.FC<PurchaseRuleCollectionViewProps> = ({
    ruleCheckHeaderItems: { handleExecuteRuleCheck },
    ruleCheckResults,
    handleDeleteRuleCheckResult,
}) => {
    return (
        <div className="collection-view-object-container">
            <div className="collection-view-container">
                <div className="collection-view-header">
                    <div className="single-view-header-item">
                        <h1 className="single-view-title">仕入ルール確認</h1>
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
                            {ruleCheckResults.map((result, index) => (
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
                                        onClick={() => handleDeleteRuleCheckResult(index)}
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
