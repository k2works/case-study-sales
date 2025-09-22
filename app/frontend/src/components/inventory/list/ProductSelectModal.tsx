import React from "react";
import Modal from "react-modal";
import { useProductItemContext } from "../../../providers/master/product/ProductItem.tsx";
import { useInventoryContext } from "../../../providers/inventory/Inventory.tsx";
import { ProductCollectionSelectView } from "../../../views/master/product/item/ProductSelect.tsx";
import { ProductType } from "../../../models/master/product";

type ProductSelectModalProps = {
    type: "edit" | "search";
};

export const ProductSelectModal: React.FC<ProductSelectModalProps> = ({ type }) => {
    const {
        newInventory,
        setNewInventory,
        searchInventoryCriteria,
        setSearchInventoryCriteria,
    } = useInventoryContext();

    const {
        pageNation: productPageNation,
        modalIsOpen: productModalIsOpen,
        setModalIsOpen: setProductModalIsOpen,
        searchModalIsOpen: productSearchModalIsOpen,
        setSearchModalIsOpen: setProductSearchModalIsOpen,
        products,
        fetchProducts,
    } = useProductItemContext();

    // 編集モーダルを閉じる
    const handleCloseEditModal = () => {
        setProductModalIsOpen(false);
    };

    // 検索モーダルを閉じる
    const handleCloseSearchModal = () => {
        setProductSearchModalIsOpen(false);
    };

    // 編集モード用モーダルビュー
    const productEditModalView = () => (
        <Modal
            isOpen={productModalIsOpen}
            onRequestClose={handleCloseEditModal}
            contentLabel="商品情報を選択"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <ProductCollectionSelectView
                products={products}
                handleSelect={(product: ProductType) => {
                    setNewInventory({
                        ...newInventory,
                        productCode: product.productCode,
                        productName: product.productFormalName
                    });
                    setProductModalIsOpen(false);
                }}
                handleClose={handleCloseEditModal}
                pageNation={productPageNation}
                fetchProducts={fetchProducts.load}
            />
        </Modal>
    );

    // 検索モード用モーダルビュー
    const productSearchModalView = () => (
        <Modal
            isOpen={productSearchModalIsOpen}
            onRequestClose={handleCloseSearchModal}
            contentLabel="商品情報を検索"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <ProductCollectionSelectView
                products={products}
                handleSelect={(product: ProductType) => {
                    setSearchInventoryCriteria({
                        ...searchInventoryCriteria,
                        productCode: product.productCode,
                        productName: product.productFormalName
                    });
                    setProductSearchModalIsOpen(false);
                }}
                handleClose={handleCloseSearchModal}
                pageNation={productPageNation}
                fetchProducts={fetchProducts.load}
            />
        </Modal>
    );

    return (
        <>
            {type === "edit" ? productEditModalView() : null}
            {type === "search" ? productSearchModalView() : null}
        </>
    );
};