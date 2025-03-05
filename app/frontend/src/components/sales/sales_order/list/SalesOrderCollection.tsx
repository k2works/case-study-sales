import React from "react";
import { useSalesOrderContext } from "../../../../providers/sales/SalesOrder.tsx";
import { SalesOrderCollectionView } from "../../../../views/sales/sales_order/SalesOrderCollection.tsx";
import { SalesOrderType } from "../../../../models/sales/sales_order";
import { showErrorMessage } from "../../../application/utils.ts";
import { SalesOrderSearchModal } from "./SalesOrderSearchModal.tsx";
import { SalesOrderEditModal } from "./SalesOrderEditModal.tsx";

export const SalesOrderCollection: React.FC = () => {
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
        initialSalesOrder,
        salesOrders,
        setSalesOrders,
        setNewSalesOrder,
        searchSalesOrderCriteria,
        setSearchSalesOrderCriteria,
        fetchSalesOrders,
        salesOrderService,
        setSearchModalIsOpen,
    } = useSalesOrderContext();

    const handleOpenModal = (salesOrder?: SalesOrderType) => {
        setMessage("");
        setError("");
        if (salesOrder) {
            setNewSalesOrder(salesOrder);
            setEditId(salesOrder.orderNumber);
            setIsEditing(true);
        } else {
            setNewSalesOrder(initialSalesOrder);
            setIsEditing(false);
        }
        setModalIsOpen(true);
    };

    const handleOpenSearchModal = () => {
        setSearchModalIsOpen(true);
    }

    const handleDeleteSalesOrder = async (orderNumber: string) => {
        try {
            if (!window.confirm(`受注番号:${orderNumber} を削除しますか？`)) return;
            await salesOrderService.destroy(orderNumber);
            await fetchSalesOrders.load();
            setMessage("受注を削除しました。");
        } catch (error: any) {
            showErrorMessage(`受注の削除に失敗しました: ${error?.message}`, setError);
        }
    };

    const handleCheckSalesOrder = (salesOrder: SalesOrderType) => {
        const newSalesOrders = salesOrders.map((s: SalesOrderType) => {
            if (s.orderNumber === salesOrder.orderNumber) {
                return {
                    ...s,
                    checked: !s.checked
                };
            }
            return s;
        });
        setSalesOrders(newSalesOrders);
    }

    const handleCheckAllSalesOrder = () => {
        const newSalesOrders = salesOrders.map((s: SalesOrderType) => {
            return {
                ...s,
                checked: !salesOrders.every((s: SalesOrderType) => s.checked)
            };
        });
        setSalesOrders(newSalesOrders);
    }

    const handleDeleteCheckedCollection = async () => {
        const checkedSalesOrders = salesOrders.filter((s: SalesOrderType) => s.checked);
        if (!checkedSalesOrders.length) {
            setError("削除する受注を選択してください。");
            return;
        }

        try {
            if (!window.confirm("選択した受注を削除しますか？")) return;
            await Promise.all(checkedSalesOrders.map((s: SalesOrderType) => salesOrderService.destroy(s.orderNumber)));
            await fetchSalesOrders.load();
            setMessage("選択した受注を削除しました。");
        } catch (error: any) {
            showErrorMessage(`受注の削除に失敗しました: ${error?.message}`, setError);
        }
    }

    return (
        <>
            <SalesOrderSearchModal/>
            <SalesOrderEditModal/>
            <SalesOrderCollectionView
                error={error}
                message={message}
                searchItems={{searchSalesOrderCriteria, setSearchSalesOrderCriteria, handleOpenSearchModal}}
                headerItems={{handleOpenModal, handleCheckToggleCollection: handleCheckAllSalesOrder, handleDeleteCheckedCollection}}
                collectionItems={{salesOrders, handleOpenModal, handleDeleteSalesOrder, handleCheckSalesOrder}}
                pageNationItems={{pageNation, fetchSalesOrders: fetchSalesOrders.load, criteria}}
            />
        </>
    );
}