import React from "react";
import {useProductItemContext} from "../../../../providers/master/product/ProductItem.tsx";
import {useProductSubstituteContext} from "../../../../providers/master/product/ProductSubstitute.tsx";
import {useProductCategoryContext} from "../../../../providers/master/product/ProductCategory.tsx";
import Modal from "react-modal";
import {ProductCategoryCollectionSelectView} from "../../../../views/master/product/ProductCategorySelect.tsx";
import {ProductCategoryType} from "../../../../models/master/product";

type SelectModalProps = {
    type: "edit" | "search";
};

export const ProductCategorySelectModal: React.FC<SelectModalProps> = ({ type }) => {
    const {
        setError,
        newProduct,
        setNewProduct,
        searchProductCriteria,
        setSearchProductCriteria,
    } = useProductItemContext();

    const {
        setProductModalIsOpen,
        setProductEditId
    } = useProductSubstituteContext();

    const {
        modalIsOpen: productCategoryModalIsOpen,
        setModalIsOpen: setProductCategoryModalIsOpen,
        productCategories,
        fetchProductCategories,
        pageNation: productCategoryPageNation,
    } = useProductCategoryContext();

    const handleCloseProductModal = () => {
        setError("");
        setProductModalIsOpen(false);
        setProductEditId(null);
    }

    const productCategoryEditModalView = () => {
        return (
            <Modal
                isOpen={productCategoryModalIsOpen}
                onRequestClose={handleCloseProductModal}
                contentLabel="商品情報を入力"
                className="modal"
                overlayClassName="modal-overlay"
                bodyOpenClassName="modal-open"
            >
                {
                    <ProductCategoryCollectionSelectView
                        productCategories={productCategories}
                        handleSelect={(productCategory: ProductCategoryType) => {
                            setNewProduct({
                                ...newProduct,
                                productCategoryCode:{
                                    value: productCategory.productCategoryCode.value
                                }
                            })
                            setProductCategoryModalIsOpen(false);
                        }}
                        handleClose={() => setProductCategoryModalIsOpen(false)}
                        pageNation={productCategoryPageNation}
                        fetchProductCategories={fetchProductCategories.load}
                    />
                }
            </Modal>
        )
    }

    const productCategorySearchModalView = () => {
        return (
            <Modal
                isOpen={productCategoryModalIsOpen}
                onRequestClose={handleCloseProductModal}
                contentLabel="商品情報を入力"
                className="modal"
                overlayClassName="modal-overlay"
                bodyOpenClassName="modal-open"
            >
                {
                    <ProductCategoryCollectionSelectView
                        productCategories={productCategories}
                        handleSelect={(productCategory) => {
                            setSearchProductCriteria(
                                {
                                    ...searchProductCriteria,
                                    productCategoryCode: productCategory.productCategoryCode.value
                                }
                            )
                            setProductCategoryModalIsOpen(false);
                        }}
                        handleClose={() => setProductCategoryModalIsOpen(false)}
                        pageNation={productCategoryPageNation}
                        fetchProductCategories={fetchProductCategories.load}
                    />
                }
            </Modal>
        )
    }

    return (
        <>
            {type === "edit" ? productCategoryEditModalView() : productCategorySearchModalView()}
        </>
    )
}
