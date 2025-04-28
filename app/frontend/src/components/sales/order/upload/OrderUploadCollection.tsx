import React from "react";
import { OrderUploadModal } from "./OrderUploadModal.tsx";
import { useSalesOrderContext } from "../../../../providers/sales/Order.tsx";
import {
    SalesOrderUploadCollectionView
} from "../../../../views/sales/order/upload/OrderUploadCollection.tsx";

export const OrderUploadCollection: React.FC = () => {
    const { uploadResults, setUploadResults, setUploadModalIsOpen } = useSalesOrderContext();

    const handleOpenUploadModal = () => {
        setUploadModalIsOpen(true);
    };

    const handleDeleteUploadResult = (index: number) => {
        setUploadResults(prev => prev.filter((_, i) => i !== index));
    };

    return (
        <>
            <SalesOrderUploadCollectionView
                uploadHeaderItems={{ handleOpenUploadModal }}
                uploadResults={uploadResults}
                handleDeleteUploadResult={handleDeleteUploadResult}
            />
            <OrderUploadModal/>
        </>
    );
};
