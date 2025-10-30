import React from "react";
import { usePurchaseReceiptContext } from "../../../../providers/procurement/Purchase.tsx";
import { PurchaseSingleView } from "../../../../views/procurement/purchase/list/PurchaseSingle.tsx";
import { showErrorMessage } from "../../../application/utils.ts";
import {useEmployeeContext} from "../../../../providers/master/Employee.tsx";
import {useVendorContext} from "../../../../providers/master/partner/Vendor.tsx";
import {useProductItemContext} from "../../../../providers/master/product/ProductItem.tsx";
import {useDepartmentContext} from "../../../../providers/master/Department.tsx";
import {useWarehouseContext} from "../../../../providers/master/Warehouse.tsx";

export const PurchaseSingle: React.FC = () => {
    const {
        message,
        setMessage,
        error,
        setError,
        setModalIsOpen,
        isEditing,
        setEditId,
        newPurchase,
        setNewPurchase,
        fetchPurchases,
        purchaseService,
        setSelectedLineIndex,
    } = usePurchaseReceiptContext();

    const {
        setModalIsOpen: setEmployeeModalIsOpen,
    } = useEmployeeContext();

    const {
        setModalIsOpen: setVendorModalIsOpen,
    } = useVendorContext();

    const {
        setModalIsOpen: setProductModalIsOpen,
    } = useProductItemContext();

    const {
        setModalIsOpen: setDepartmentModalIsOpen,
    } = useDepartmentContext();

    const {
        setModalIsOpen: setWarehouseModalIsOpen,
    } = useWarehouseContext();

    const handleCloseModal = () => {
        setModalIsOpen(false);
        setEditId(null);
    };

    const handleCreateOrUpdatePurchase = async () => {
        try {
            if (isEditing) {
                await purchaseService.update(newPurchase);
                setMessage("仕入を更新しました。");
            } else {
                await purchaseService.create(newPurchase);
                setMessage("仕入を作成しました。");
            }
            await fetchPurchases.load();
            handleCloseModal();
        } catch (error: any) {
            showErrorMessage(`仕入の更新に失敗しました: ${error?.message}`, setError);
        }
    };

    return (
        <PurchaseSingleView
            error={error}
            message={message}
            isEditing={isEditing}
            newPurchase={newPurchase}
            setNewPurchase={setNewPurchase}
            setSelectedLineIndex={setSelectedLineIndex}
            handleCreateOrUpdatePurchase={handleCreateOrUpdatePurchase}
            handleCloseModal={handleCloseModal}
            handleEmployeeSelect={() => setEmployeeModalIsOpen(true)}
            handleVendorSelect={() => setVendorModalIsOpen(true)}
            handleProductSelect={() => setProductModalIsOpen(true)}
            handleDepartmentSelect={() => setDepartmentModalIsOpen(true)}
            handleWarehouseSelect={() => setWarehouseModalIsOpen(true)}
        />
    );
};
