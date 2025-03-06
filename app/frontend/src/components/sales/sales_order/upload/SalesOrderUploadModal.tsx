import React, { useState } from "react";
import Modal from "react-modal";
import { useSalesOrderContext } from "../../../../providers/sales/SalesOrder";

export const SalesOrderUploadModal: React.FC = () => {
    const {
        uploadModalIsOpen,
        setUploadModalIsOpen,
        setError,
        uploadSalesOrders
    } = useSalesOrderContext();

    const [selectedFile, setSelectedFile] = useState<File | null>(null);

    const handleFileSelect = (event: React.ChangeEvent<HTMLInputElement>) => {
        if (event.target.files && event.target.files[0]) {
            setSelectedFile(event.target.files[0]);
        }
    };

    const handleUpload = async () => {
        if (!selectedFile) {
            setError("ファイルを選択してください");
            return;
        }

        try {
            await uploadSalesOrders(selectedFile);
            setUploadModalIsOpen(false);
            setSelectedFile(null);
        } catch (error) {
            const errorMessage = error instanceof Error ? error.message : "アップロード中にエラーが発生しました";
            setError(errorMessage);
            throw error;
        }
    };

    const handleCloseModal = () => {
        setError("");
        setUploadModalIsOpen(false);
        setSelectedFile(null);
    };

    return (
        <Modal
            isOpen={uploadModalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="受注データアップロード"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <div className="single-view-container">
                <div className="single-view-header">
                    <h2>受注データアップロード</h2>
                </div>
                <div className="single-view-content">
                    <div className="single-view-content-item">
                        <div className="single-view-content-item-form">
                            <label>
                                ファイル:
                                <input
                                    type="file"
                                    accept=".csv"
                                    onChange={handleFileSelect}
                                />
                            </label>
                        </div>
                    </div>
                    <div className="button-container">
                        <button
                            className="action-button"
                            onClick={handleUpload}
                            disabled={!selectedFile}
                        >
                            アップロード
                        </button>
                        <button
                            className="action-button"
                            onClick={handleCloseModal}
                        >
                            キャンセル
                        </button>
                    </div>
                </div>
            </div>
        </Modal>
    );
};