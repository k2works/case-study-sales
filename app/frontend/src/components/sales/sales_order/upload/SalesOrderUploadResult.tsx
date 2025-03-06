import React from "react";
import { Message } from "../../../application/Message.tsx";

interface UploadResultType {
    message: string;
    details: Array<{ [key: string]: string }>;
}

interface SalesOrderUploadResultProps {
    results: UploadResultType[];
    onDelete: (index: number) => void;
}

export const SalesOrderUploadResult: React.FC<SalesOrderUploadResultProps> = ({
    results,
    onDelete
}) => {
    if (!results.length) {
        return null;
    }

    return (
        <div className="upload-result-container">
            <h3>アップロード結果</h3>
            <div className="upload-result-list">
                {results.map((result, index) => (
                    <div key={index} className="upload-result-item">
                        <div className="upload-result-message">
                            <Message
                                message={result.message}
                                error={null}
                            />
                            {result.details && result.details.length > 0 && (
                                <div className="upload-result-details">
                                    {result.details.map((detail, detailIndex) => (
                                        <div key={detailIndex} className="upload-result-detail">
                                            {Object.entries(detail).map(([key, value]) => (
                                                <div key={key} className="detail-item">
                                                    <span className="detail-key">{key}:</span>
                                                    <span className="detail-value">{value}</span>
                                                </div>
                                            ))}
                                        </div>
                                    ))}
                                </div>
                            )}
                        </div>
                        <button
                            className="action-button"
                            onClick={() => onDelete(index)}
                        >
                            削除
                        </button>
                    </div>
                ))}
            </div>
        </div>
    );
};