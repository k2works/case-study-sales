import React from "react";
import {useProductItemContext} from "../../../../providers/master/product/ProductItem.tsx";
import {useProductSubstituteContext} from "../../../../providers/master/product/ProductSubstitute.tsx";
import Modal from "react-modal";
import {ProductCollectionSelectView} from "../../../../views/master/product/item/ProductSelect.tsx";
import {ProductType} from "../../../../models/master/product";

export const ProductItemSubstituteModal: React.FC = () => {
    const {
        setError,
        newProduct,
        setNewProduct,
    } = useProductItemContext();

    const {
        substitutePageNation,
        productModalIsOpen,
        setProductModalIsOpen,
        substitutes,
        fetchSubstitutes,
        setProductEditId
    } = useProductSubstituteContext();

    const handleCloseProductModal = () => {
        setError("");
        setProductModalIsOpen(false);
        setProductEditId(null);
    }

    const handleSelectProductModal = (product: ProductType) => {
        const newProducts = newProduct.substituteProduct.filter((e) => e.substituteProductCode.value !== product.productCode.value);
        newProducts.push({
            productCode: newProduct.productCode,
            substituteProductCode: product.productCode,
            priority: 0
        });
        setNewProduct({
            ...newProduct,
            substituteProduct: newProducts
        });
    }

    return (
        <Modal
            isOpen={productModalIsOpen}
            onRequestClose={handleCloseProductModal}
            contentLabel="代替商品情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            {
                <ProductCollectionSelectView
                    products={substitutes}
                    handleSelect={handleSelectProductModal}
                    handleClose={handleCloseProductModal}
                    pageNation={substitutePageNation}
                    fetchProducts={fetchSubstitutes.load}
                />
            }
        </Modal>
    )
}
