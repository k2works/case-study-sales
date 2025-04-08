import React from "react";
import { useShippingContext } from "../../../../providers/sales/Shipping.tsx";
import { ShippingCollectionView } from "../../../../views/shipping/shipping/list/ShippingCollection.tsx";
import { ShippingSearchModal } from "./ShippingSearchModal.tsx";
import { ShippingEditModal } from "./ShippingEditModal.tsx";
import {ShippingType} from "../../../../models/sales/shipping.ts";

export const ShippingCollection: React.FC = () => {
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

    return (
        <>
            <ShippingSearchModal/>
            <ShippingEditModal/>
            <ShippingCollectionView
                error={error}
                message={message}
                searchItems={{searchShippingCriteria, setSearchShippingCriteria, handleOpenSearchModal}}
                headerItems={{
                    handleOpenModal, 
                    handleCheckToggleCollection: handleCheckAllShippings, 
                }}
                collectionItems={{shippings, handleOpenModal, handleCheckShipping}}
                pageNationItems={{pageNation, fetchShippings: fetchShippings.load, criteria}}
            />
        </>
    );
}
