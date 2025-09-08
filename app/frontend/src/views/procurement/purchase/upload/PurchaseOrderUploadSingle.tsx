import React from "react";
import { Message } from "../../../../components/application/Message.tsx";
import { SingleViewHeaderItem } from "../../../Common.tsx";

interface PurchaseOrderUploadSingleViewProps {
    error: string | null;
    message: string | null;
    onFileSelect: (event: React.ChangeEvent<HTMLInputElement>) => void;
    onUpload: () => void;
    onClose: () => void;
    isUploadDisabled: boolean;
}

export const PurchaseOrderUploadSingleView: React.FC<PurchaseOrderUploadSingleViewProps> = ({
                                                                                          error,
                                                                                          message,
                                                                                          onFileSelect,
                                                                                          onUpload,
                                                                                          onClose,
                                                                                          isUploadDisabled,
                                                                                      }) => {
    return (
        <div className="single-view-object-container">
            <Message error={error} message={message} />
            <div className="single-view-header">
                <SingleViewHeaderItem title="発注データアップロード" subtitle="発注データを一括登録します。"/>
            </div>
            <div className="single-view-container">
                <div className="single-view-content">
                    <div className="single-view-content-item">
                        <div className="single-view-content-item-form">
                            <label>
                                ファイル:
                                <input
                                    type="file"
                                    accept=".csv"
                                    onChange={onFileSelect}
                                />
                            </label>
                        </div>
                    </div>
                    <div className="button-container">
                        <button
                            className="action-button"
                            onClick={onUpload}
                            disabled={isUploadDisabled}
                        >
                            アップロード
                        </button>
                        <button className="action-button" onClick={onClose}>
                            キャンセル
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};