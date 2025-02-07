import React from "react";
import {useProductItemContext} from "../../../../providers/ProductItem.tsx";
import Modal from "react-modal";
import {ProductItemSingle} from "./ProductItemSingle.tsx";

export const ProductItemEditModal: React.FC = () => {
    const {
        setError,
        modalIsOpen,
        setModalIsOpen,
        setEditId,
    } = useProductItemContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    return (
        <Modal
            isOpen={modalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="商品情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <ProductItemSingle/>
        </Modal>
    )
}
