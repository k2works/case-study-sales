import React from "react";
import {useProductCategoryContext} from "../../../../providers/master/product/ProductCategory.tsx";
import Modal from "react-modal";
import {ProductCategorySingle} from "./ProductCategorySingle.tsx";

export const ProductCategoryEditModal: React.FC = () => {
    const {
        setError,
        modalIsOpen,
        setModalIsOpen,
        setEditId,
    } = useProductCategoryContext();

    const handleCloseModal = () => {
        setError("");
        setModalIsOpen(false);
        setEditId(null);
    };

    return (
        <Modal
            isOpen={modalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="商品分類情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <ProductCategorySingle/>
        </Modal>
    )
}
