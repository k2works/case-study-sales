import React from "react";
import { InventoryUploadModal } from "./InventoryUploadModal.tsx";
import { useInventoryContext } from "../../../providers/inventory/Inventory.tsx";
import {
    InventoryUploadCollectionView
} from "../../../views/inventory/upload/InventoryUploadCollection.tsx";

export const InventoryUploadCollection: React.FC = () => {
    const { uploadResults, setUploadResults, setUploadModalIsOpen } = useInventoryContext();

    const handleOpenUploadModal = () => {
        setUploadModalIsOpen(true);
    };

    const handleDeleteUploadResult = (index: number) => {
        setUploadResults((prev: any[]) => prev.filter((_: any, i: number) => i !== index));
    };

    return (
        <>
            <InventoryUploadCollectionView
                uploadHeaderItems={{ handleOpenUploadModal }}
                uploadResults={uploadResults}
                handleDeleteUploadResult={handleDeleteUploadResult}
            />
            <InventoryUploadModal/>
        </>
    );
};