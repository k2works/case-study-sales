import React, { useState } from "react";
import { useInventoryContext } from "../../../providers/inventory/Inventory.tsx";
import { InventoryUploadSingleView } from "../../../views/inventory/upload/InventoryUploadSingle.tsx";

export const InventoryUploadSingle: React.FC = () => {
    const {
        message,
        setMessage,
        error,
        setError,
        setUploadModalIsOpen,
        uploadInventories,
    } = useInventoryContext();
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
            await uploadInventories(selectedFile);
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
        <InventoryUploadSingleView
            error={error}
            message={message}
            onFileSelect={handleFileSelect}
            onUpload={handleUpload}
            onClose={handleCloseModal}
            isUploadDisabled={!selectedFile}
        />
    );
};