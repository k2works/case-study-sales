import React from "react";
import {useProductCategoryContext} from "../../../../providers/master/product/ProductCategory.tsx";
import {useProductItemContext} from "../../../../providers/master/product/ProductItem.tsx";
import Modal from "react-modal";
import {ProductCollectionSelectView} from "../../../../views/master/product/item/ProductSelect.tsx";

export const ProductItemSelectModal: React.FC = () => {
    const {
        setError,
        pageNation,
        newProductCategory,
        setNewProductCategory,
    } = useProductCategoryContext();

    const {
        modalIsOpen: productModalIsOpen,
        setModalIsOpen: setProductModalIsOpen,
        setEditId: setProductEditId,
        fetchProducts,
        products,
    } = useProductItemContext();

    const handleCloseProductModal = () => {
        setError("");
        setProductModalIsOpen(false);
        setProductEditId(null);
    }

    return (
        <Modal
            isOpen={productModalIsOpen}
            onRequestClose={handleCloseProductModal}
            contentLabel="商品情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            {
                <ProductCollectionSelectView
                    products={products}
                    handleSelect={(product) => {
                        const newProducts = newProductCategory.products.filter((e) => e.productCode.value !== product.productCode.value);
                        if (product.productCode.value) {
                            newProducts.push({
                                ...product,
                                addFlag: true,
                                deleteFlag: false
                            });
                        }
                        setNewProductCategory({
                            ...newProductCategory,
                            products: newProducts
                        });
                    }}
                    handleClose={handleCloseProductModal}
                    pageNation={pageNation}
                    fetchProducts={fetchProducts.load}
                />
            }
        </Modal>
    )
}
