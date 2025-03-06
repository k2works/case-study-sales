import React from "react";
import "./SalesOrderUpload.css";
import { SalesOrderUploadModal } from "./SalesOrderUploadModal";
import { SalesOrderUploadResult } from "./SalesOrderUploadResult";
import { useSalesOrderContext } from "../../../../providers/sales/SalesOrder";

export const SalesOrderUploadCollection: React.FC = () => {
    const { uploadResults, setUploadResults, setUploadModalIsOpen } = useSalesOrderContext();

    const handleOpenUploadModal = () => {
        setUploadModalIsOpen(true);
    };

    const handleDeleteUploadResult = (index: number) => {
        setUploadResults(prev => prev.filter((_, i) => i !== index));
    };

    return (
        <div className="upload-container">
            <button
                className="action-button"
                onClick={handleOpenUploadModal}
                id="upload"
            >
                アップロード
            </button>
            <SalesOrderUploadResult
                results={uploadResults}
                onDelete={handleDeleteUploadResult}
            />
            <SalesOrderUploadModal />
        </div>
    );
};
