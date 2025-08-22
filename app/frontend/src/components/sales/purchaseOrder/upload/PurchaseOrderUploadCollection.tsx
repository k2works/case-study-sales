import React from "react";
import { PurchaseOrderUploadModal } from "./PurchaseOrderUploadModal.tsx";
import { usePurchaseOrderContext } from "../../../../providers/sales/PurchaseOrder.tsx";
import {
    PurchaseOrderUploadCollectionView
} from "../../../../views/sales/purchaseOrder/upload/PurchaseOrderUploadCollection.tsx";

export const PurchaseOrderUploadCollection: React.FC = () => {
    const { uploadResults, setUploadResults, setUploadModalIsOpen } = usePurchaseOrderContext();

    const handleOpenUploadModal = () => {
        setUploadModalIsOpen(true);
    };

    const handleDeleteUploadResult = (index: number) => {
        setUploadResults(prev => prev.filter((_, i) => i !== index));
    };

    return (
        <>
            <PurchaseOrderUploadCollectionView
                uploadHeaderItems={{ handleOpenUploadModal }}
                uploadResults={uploadResults}
                handleDeleteUploadResult={handleDeleteUploadResult}
            />
            <PurchaseOrderUploadModal/>
        </>
    );
};