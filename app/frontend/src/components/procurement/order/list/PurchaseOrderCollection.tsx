import React from "react";
import { usePurchaseOrderContext } from "../../../../providers/procurement/PurchaseOrder.tsx";
import { PurchaseOrderCollectionView } from "../../../../views/procurement/order/list/PurchaseOrderCollection.tsx";
import { PurchaseOrderType } from "../../../../models/procurement/purchaseOrder.ts";
import { showErrorMessage } from "../../../application/utils.ts";
import { PurchaseOrderSearchModal } from "./PurchaseOrderSearchModal.tsx";
import { PurchaseOrderEditModal } from "./PurchaseOrderEditModal.tsx";

export const PurchaseOrderCollection: React.FC = () => {
    const {
        message,
        setMessage,
        error,
        setError,
        pageNation,
        criteria,
        setModalIsOpen,
        setIsEditing,
        setEditId,
        initialPurchaseOrder,
        purchaseOrders,
        setPurchaseOrders,
        setNewPurchaseOrder,
        searchPurchaseOrderCriteria,
        setSearchPurchaseOrderCriteria,
        fetchPurchaseOrders,
        purchaseOrderService,
        setSearchModalIsOpen,
    } = usePurchaseOrderContext();

    const handleOpenModal = (purchaseOrder?: PurchaseOrderType) => {
        setMessage("");
        setError("");
        if (purchaseOrder) {
            setNewPurchaseOrder(purchaseOrder);
            setEditId(purchaseOrder.purchaseOrderNumber);
            setIsEditing(true);
        } else {
            setNewPurchaseOrder(initialPurchaseOrder);
            setIsEditing(false);
        }
        setModalIsOpen(true);
    };

    const handleOpenSearchModal = () => {
        setSearchModalIsOpen(true);
    }

    const handleDeletePurchaseOrder = async (purchaseOrderNumber: string) => {
        try {
            if (!window.confirm(`発注番号:${purchaseOrderNumber} を削除しますか？`)) return;
            await purchaseOrderService.destroy(purchaseOrderNumber);
            await fetchPurchaseOrders.load();
            setMessage("発注を削除しました。");
        } catch (error: any) {
            showErrorMessage(`発注の削除に失敗しました: ${error?.message}`, setError);
        }
    };

    const handleCheckPurchaseOrder = (purchaseOrder: PurchaseOrderType) => {
        const newPurchaseOrders = purchaseOrders.map((p: PurchaseOrderType) => {
            if (p.purchaseOrderNumber === purchaseOrder.purchaseOrderNumber) {
                return {
                    ...p,
                    checked: !p.checked
                };
            }
            return p;
        });
        setPurchaseOrders(newPurchaseOrders);
    }

    const handleCheckAllPurchaseOrder = () => {
        const newPurchaseOrders = purchaseOrders.map((p: PurchaseOrderType) => {
            return {
                ...p,
                checked: !purchaseOrders.every((p: PurchaseOrderType) => p.checked)
            };
        });
        setPurchaseOrders(newPurchaseOrders);
    }

    const handleDeleteCheckedCollection = async () => {
        const checkedPurchaseOrders = purchaseOrders.filter((p: PurchaseOrderType) => p.checked);
        if (!checkedPurchaseOrders.length) {
            setError("削除する発注を選択してください。");
            return;
        }

        try {
            if (!window.confirm("選択した発注を削除しますか？")) return;
            await Promise.all(checkedPurchaseOrders.map((p: PurchaseOrderType) => purchaseOrderService.destroy(p.purchaseOrderNumber)));
            await fetchPurchaseOrders.load();
            setMessage("選択した発注を削除しました。");
        } catch (error: any) {
            showErrorMessage(`発注の削除に失敗しました: ${error?.message}`, setError);
        }
    }

    return (
        <>
            <PurchaseOrderSearchModal/>
            <PurchaseOrderEditModal/>
            <PurchaseOrderCollectionView
                error={error}
                message={message}
                searchItems={{
                    searchPurchaseOrderCriteria: {
                        ...searchPurchaseOrderCriteria,
                        completionFlag: searchPurchaseOrderCriteria.completionFlag === "true"
                    }, 
                    setSearchPurchaseOrderCriteria: (criteria) => {
                        const mappedCriteria = {
                            ...criteria,
                            completionFlag: criteria.completionFlag ? "true" : "false"
                        };
                        setSearchPurchaseOrderCriteria(mappedCriteria);
                    }, 
                    handleOpenSearchModal
                }}
                headerItems={{handleOpenModal, handleCheckToggleCollection: handleCheckAllPurchaseOrder, handleDeleteCheckedCollection}}
                collectionItems={{purchaseOrders, handleOpenModal, handleDeletePurchaseOrder, handleCheckPurchaseOrder}}
                pageNationItems={{pageNation, fetchPurchaseOrders: fetchPurchaseOrders.load, criteria}}
            />
        </>
    );
}