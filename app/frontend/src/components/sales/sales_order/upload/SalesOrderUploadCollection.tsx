import React from "react";
import { SalesOrderUploadModal } from "./SalesOrderUploadModal";
import { useSalesOrderContext } from "../../../../providers/sales/SalesOrder";
import {
    SalesOrderUploadCollectionView
} from "../../../../views/sales/sales_order/upload/SalesOrderUplaodCollection.tsx";

export const SalesOrderUploadCollection: React.FC = () => {
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
            <SalesOrderUploadModal/>
        </>
    );
};
