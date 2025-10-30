import React from "react";
import { PurchaseOrderUploadModal } from "./PurchaseOrderUploadModal.tsx";
import { usePurchaseOrderContext } from "../../../../providers/procurement/PurchaseOrder.tsx";
import {
    PurchaseOrderUploadCollectionView
} from "../../../../views/procurement/order/upload/PurchaseOrderUploadCollection.tsx";

export const PurchaseOrderUploadCollection: React.FC = () => {
    const { uploadResults, setUploadResults, setUploadModalIsOpen } = usePurchaseOrderContext();

    const handleOpenUploadModal = () => {
        setUploadModalIsOpen(true);
    };

    const handleDeleteUploadResult = (index: number) => {
        setUploadResults((prev: any[]) => prev.filter((_: any, i: number) => i !== index));
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