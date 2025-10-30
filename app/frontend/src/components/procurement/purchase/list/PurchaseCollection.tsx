import React from "react";
import { usePurchaseReceiptContext } from "../../../../providers/procurement/Purchase.tsx";
import { PurchaseCollectionView } from "../../../../views/procurement/purchase/list/PurchaseCollection.tsx";
import { PurchaseType } from "../../../../models/procurement/purchase.ts";
import { showErrorMessage } from "../../../application/utils.ts";
import { PurchaseSearchModal } from "./PurchaseSearchModal.tsx";
import { PurchaseEditModal } from "./PurchaseEditModal.tsx";

export const PurchaseCollection: React.FC = () => {
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
        initialPurchase,
        purchases,
        setPurchases,
        setNewPurchase,
        searchPurchaseCriteria,
        setSearchPurchaseCriteria,
        fetchPurchases,
        purchaseService,
        setSearchModalIsOpen,
    } = usePurchaseReceiptContext();

    const handleOpenModal = (purchase?: PurchaseType) => {
        setMessage("");
        setError("");
        if (purchase) {
            setNewPurchase(purchase);
            setEditId(purchase.purchaseNumber);
            setIsEditing(true);
        } else {
            setNewPurchase(initialPurchase);
            setIsEditing(false);
        }
        setModalIsOpen(true);
    };

    const handleOpenSearchModal = () => {
        setSearchModalIsOpen(true);
    }

    const handleDeletePurchase = async (purchaseNumber: string) => {
        try {
            if (!window.confirm(`仕入番号:${purchaseNumber} を削除しますか？`)) return;
            await purchaseService.destroy(purchaseNumber);
            await fetchPurchases.load();
            setMessage("仕入を削除しました。");
        } catch (error: any) {
            showErrorMessage(`仕入の削除に失敗しました: ${error?.message}`, setError);
        }
    };

    const handleCheckPurchase = (purchase: PurchaseType) => {
        const newPurchases = purchases.map((p: PurchaseType) => {
            if (p.purchaseNumber === purchase.purchaseNumber) {
                return {
                    ...p,
                    checked: !p.checked
                };
            }
            return p;
        });
        setPurchases(newPurchases);
    }

    const handleCheckToggleCollection = () => {
        const newPurchases = purchases.map((p: PurchaseType) => {
            return {
                ...p,
                checked: !purchases.every((p: PurchaseType) => p.checked)
            };
        });
        setPurchases(newPurchases);
    }

    const handleDeleteCheckedCollection = async () => {
        const checkedPurchases = purchases.filter((p: PurchaseType) => p.checked);
        if (!checkedPurchases.length) {
            setError("削除する仕入を選択してください。");
            return;
        }

        try {
            if (!window.confirm("選択した仕入を削除しますか？")) return;
            await Promise.all(checkedPurchases.map((p: PurchaseType) => purchaseService.destroy(p.purchaseNumber)));
            await fetchPurchases.load();
            setMessage("選択した仕入を削除しました。");
        } catch (error: any) {
            showErrorMessage(`仕入の削除に失敗しました: ${error?.message}`, setError);
        }
    }

    return (
        <>
            <PurchaseSearchModal/>
            <PurchaseEditModal/>
            <PurchaseCollectionView
                error={error}
                message={message}
                searchItems={{
                    searchPurchaseCriteria,
                    setSearchPurchaseCriteria,
                    handleOpenSearchModal
                }}
                headerItems={{handleOpenModal, handleCheckToggleCollection, handleDeleteCheckedCollection}}
                collectionItems={{purchases, handleOpenModal, handleDeletePurchase: handleDeletePurchase, handleCheckPurchase}}
                pageNationItems={{pageNation, fetchPurchases: fetchPurchases.load, criteria}}
            />
        </>
    );
};
