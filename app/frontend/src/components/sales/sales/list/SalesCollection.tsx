import React from "react";
import { useSalesContext } from "../../../../providers/sales/Sales.tsx";
import { SalesCollectionView } from "../../../../views/sales/sales/list/SalesCollection.tsx";
import { SalesType } from "../../../../models/sales/sales";
import { showErrorMessage } from "../../../application/utils.ts";
import { SalesSearchModal } from "./SalesSearchModal.tsx";
import { SalesEditModal } from "./SalesEditModal.tsx";

export const SalesCollection: React.FC = () => {
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
        initialSales,
        sales,
        setSales,
        setNewSales,
        searchSalesCriteria,
        setSearchSalesCriteria,
        fetchSales,
        salesService,
        setSearchModalIsOpen,
    } = useSalesContext();

    const handleOpenModal = (salesItem?: SalesType) => {
        setMessage("");
        setError("");
        if (salesItem) {
            setNewSales(salesItem);
            setEditId(salesItem.salesNumber);
            setIsEditing(true);
        } else {
            setNewSales(initialSales);
            setIsEditing(false);
        }
        setModalIsOpen(true);
    };

    const handleOpenSearchModal = () => {
        setSearchModalIsOpen(true);
    }

    const handleDeleteSales = async (salesNumber: string) => {
        try {
            if (!window.confirm(`売上番号:${salesNumber} を削除しますか？`)) return;
            await salesService.destroy(salesNumber);
            await fetchSales.load();
            setMessage("売上を削除しました。");
        } catch (error: unknown) {
            const errorMessage = error instanceof Error ? error.message : '不明なエラーが発生しました';
            showErrorMessage(`売上の削除に失敗しました: ${errorMessage}`, setError);
        }
    };

    const handleCheckSales = (salesItem: SalesType) => {
        const newSales = sales.map((s: SalesType) => {
            if (s.salesNumber === salesItem.salesNumber) {
                return {
                    ...s,
                    checked: !s.checked
                };
            }
            return s;
        });
        setSales(newSales);
    }

    const handleCheckAllSales = () => {
        const newSales = sales.map((s: SalesType) => {
            return {
                ...s,
                checked: !sales.every((s: SalesType) => s.checked)
            };
        });
        setSales(newSales);
    }

    const handleDeleteCheckedCollection = async () => {
        const checkedSales = sales.filter((s: SalesType) => s.checked);
        if (!checkedSales.length) {
            setError("削除する売上を選択してください。");
            return;
        }

        try {
            if (!window.confirm("選択した売上を削除しますか？")) return;
            await Promise.all(checkedSales.map((s: SalesType) => salesService.destroy(s.salesNumber)));
            await fetchSales.load();
            setMessage("選択した売上を削除しました。");
        } catch (error: unknown) {
            const errorMessage = error instanceof Error ? error.message : '不明なエラーが発生しました';
            showErrorMessage(`売上の削除に失敗しました: ${errorMessage}`, setError);
        }
    }

    return (
        <>
            <SalesSearchModal/>
            <SalesEditModal/>
            <SalesCollectionView
                error={error}
                message={message}
                searchItems={{searchSalesCriteria, setSearchSalesCriteria, handleOpenSearchModal}}
                headerItems={{handleOpenModal, handleCheckToggleCollection: handleCheckAllSales, handleDeleteCheckedCollection}}
                collectionItems={{sales, handleOpenModal, handleDeleteSales, handleCheckSales}}
                pageNationItems={{pageNation, fetchSales: fetchSales.load, criteria}}
            />
        </>
    );
}