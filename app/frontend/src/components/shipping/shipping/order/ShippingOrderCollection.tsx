import React from "react";
import { useShippingContext } from "../../../../providers/shipping/Shipping.tsx";
import { ShippingType } from "../../../../models/shipping/shipping";
import {ShippingOrderSearchModal} from "./ShippingOrderSearchModal.tsx";
import {ShippingOrderEditModal} from "./ShippingOrderEditModal.tsx";
import {ShippingOrderCollectionView} from "../../../../views/shipping/shipping/order/ShippingOrderCollection.tsx";

export const ShippingOrderCollection: React.FC = () => {
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
        initialShipping,
        shippings,
        setShippings,
        setNewShipping,
        searchShippingCriteria,
        setSearchShippingCriteria,
        fetchShippings,
        setSearchModalIsOpen,
        shippingService,
    } = useShippingContext();

    const handleOpenModal = (shippingItem?: ShippingType) => {
        setMessage("");
        setError("");
        if (shippingItem) {
            setNewShipping(shippingItem);
            setEditId(shippingItem.orderNumber);
            setIsEditing(true);
        } else {
            setNewShipping(initialShipping);
            setIsEditing(false);
        }
        setModalIsOpen(true);
    };

    const handleOpenSearchModal = () => {
        setSearchModalIsOpen(true);
    }

    const handleCheckShipping = (shippingItem: ShippingType) => {
        const newShippings = shippings.map((s: ShippingType) => {
            if (s.orderNumber === shippingItem.orderNumber) {
                return {
                    ...s,
                    checked: !s.checked
                };
            }
            return s;
        });
        setShippings(newShippings);
    }

    const handleCheckAllShippings = () => {
        const newShippings = shippings.map((s: ShippingType) => {
            return {
                ...s,
                checked: !shippings.every((s: ShippingType) => s.checked)
            };
        });
        setShippings(newShippings);
    }

    const handleExecuteShippingOrder = async () => {
        if (!searchShippingCriteria) {
            return;
        }
        try {
            await shippingService.orderShipping(searchShippingCriteria);
            setMessage("出荷を確定しました。");
        } catch (error) {
            const errorMessage = error instanceof Error ? error.message : '不明なエラーが発生しました';
            console.error('Rule check failed:', errorMessage);
        }
    };

    return (
        <>
            <ShippingOrderSearchModal/>
            <ShippingOrderEditModal/>
            <ShippingOrderCollectionView
                error={error}
                message={message}
                searchItems={{searchShippingCriteria, setSearchShippingCriteria, handleOpenSearchModal}}
                headerItems={{
                    handleOpenModal,
                    handleCheckToggleCollection: handleCheckAllShippings,
                    handleExecuteShippingOrder
                }}
                collectionItems={{shippings, handleOpenModal, handleCheckShipping}}
                pageNationItems={{pageNation, fetchShippings: fetchShippings.load, criteria}}
            />
        </>
    );
}
