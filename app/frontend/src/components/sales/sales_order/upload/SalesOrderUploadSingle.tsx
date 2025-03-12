import React, { useState } from "react";
import { useSalesOrderContext } from "../../../../providers/sales/SalesOrder";
import {SalesOrderUploadSingleView} from "../../../../views/sales/sales_order/upload/SalesOrderUploadSingle.tsx";

export const SalesOrderUploadSingle: React.FC = () => {
    const {
        message,
        setMessage,
        error,
        setError,
        setUploadModalIsOpen,
        uploadSalesOrders,
    } = useSalesOrderContext();
    const [selectedFile, setSelectedFile] = useState<File | null>(null);

    const handleFileSelect = (event: React.ChangeEvent<HTMLInputElement>) => {
        if (event.target.files && event.target.files[0]) {
            setSelectedFile(event.target.files[0]);
        }
    };

    const handleUpload = async () => {
        setError("");
        setMessage("");
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
        setMessage("");
        setUploadModalIsOpen(false);
        setSelectedFile(null);
    };

    return (
        <SalesOrderUploadSingleView
            error={error}
            message={message}
            onFileSelect={handleFileSelect}
            onUpload={handleUpload}
            onClose={handleCloseModal}
            isUploadDisabled={!selectedFile}
        />
    );
};