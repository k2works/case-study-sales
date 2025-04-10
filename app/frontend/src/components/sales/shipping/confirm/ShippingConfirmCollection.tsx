import React from "react";
import { useShippingContext } from "../../../../providers/sales/Shipping.tsx";
import { ShippingConfirmSearchModal } from "./ShippingConfirmSearchModal.tsx";
import { ShippingConfirmEditModal } from "./ShippingConfirmEditModal.tsx";
import { ShippingConfirmCollectionView } from "../../../../views/sales/shipping/confirm/ShippingConfirmCollection.tsx";
import { ShippingType } from "../../../../models/sales/shipping.ts";

export const ShippingConfirmCollection: React.FC = () => {
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

    const handleConfirmShipping = async () => {
        if (!searchShippingCriteria) {
            return;
        }
        try {
            await shippingService.confirmShipping(searchShippingCriteria);
            setMessage("出荷を確認しました。");
        } catch (error) {
            const errorMessage = error instanceof Error ? error.message : '不明なエラーが発生しました';
            console.error('Confirmation failed:', errorMessage);
        }
    };

    return (
        <>
            <ShippingConfirmSearchModal/>
            <ShippingConfirmEditModal/>
            <ShippingConfirmCollectionView
                error={error}
                message={message}
                searchItems={{searchShippingCriteria, setSearchShippingCriteria, handleOpenSearchModal}}
                headerItems={{
                    handleOpenModal,
                    handleCheckToggleCollection: handleCheckAllShippings,
                    handleConfirmShipping
                }}
                collectionItems={{shippings, handleOpenModal, handleCheckShipping}}
                pageNationItems={{pageNation, fetchShippings: fetchShippings.load, criteria}}
            />
        </>
    );
}