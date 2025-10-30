import React from "react";
import { usePurchaseOrderContext } from "../../../../providers/procurement/PurchaseOrder.tsx";
import { PurchaseOrderSingleView } from "../../../../views/procurement/order/list/PurchaseOrderSingle.tsx";
import { showErrorMessage } from "../../../application/utils.ts";
import {useEmployeeContext} from "../../../../providers/master/Employee.tsx";
import {useVendorContext} from "../../../../providers/master/partner/Vendor.tsx";
import {useProductItemContext} from "../../../../providers/master/product/ProductItem.tsx";

export const PurchaseOrderSingle: React.FC = () => {
    const {
        message,
        setMessage,
        error,
        setError,
        setModalIsOpen,
        isEditing,
        setEditId,
        newPurchaseOrder,
        setNewPurchaseOrder,
        fetchPurchaseOrders,
        purchaseOrderService,
        setSelectedLineIndex,
    } = usePurchaseOrderContext();

    const {
        setModalIsOpen: setEmployeeModalIsOpen,
    } = useEmployeeContext();

    const {
        setModalIsOpen: setVendorModalIsOpen,
    } = useVendorContext();

    const {
        setModalIsOpen: setProductModalIsOpen,
    } = useProductItemContext();

    const handleCloseModal = () => {
        setModalIsOpen(false);
        setEditId(null);
    };

    const handleCreateOrUpdatePurchaseOrder = async () => {
        try {
            if (isEditing) {
                await purchaseOrderService.update(newPurchaseOrder);
                setMessage("発注を更新しました。");
            } else {
                await purchaseOrderService.create(newPurchaseOrder);
                setMessage("発注を作成しました。");
            }
            await fetchPurchaseOrders.load();
            handleCloseModal();
        } catch (error: any) {
            showErrorMessage(`発注の更新に失敗しました: ${error?.message}`, setError);
        }
    };

    return (
        <PurchaseOrderSingleView
            error={error}
            message={message}
            isEditing={isEditing}
            newPurchaseOrder={newPurchaseOrder}
            setNewPurchaseOrder={setNewPurchaseOrder}
            setSelectedLineIndex={setSelectedLineIndex}
            handleCreateOrUpdatePurchaseOrder={handleCreateOrUpdatePurchaseOrder}
            handleCloseModal={handleCloseModal}
            handleEmployeeSelect={() => setEmployeeModalIsOpen(true)}
            handleVendorSelect={() => setVendorModalIsOpen(true)}
            handleProductSelect={() => setProductModalIsOpen(true)}
        />
    );
};