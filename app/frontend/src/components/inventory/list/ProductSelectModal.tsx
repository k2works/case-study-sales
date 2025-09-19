import React from "react";
import Modal from "react-modal";
import { useProductItemContext } from "../../../providers/master/product/ProductItem.tsx";
import { useInventoryContext } from "../../../providers/inventory/Inventory.tsx";
import { ProductCollectionSelectView } from "../../../views/master/product/item/ProductSelect.tsx";
import { ProductType } from "../../../models/master/product";

type ProductSelectModalProps = {
    type: "search";
};

export const ProductSelectModal: React.FC<ProductSelectModalProps> = ({ type }) => {
    const {
        searchInventoryCriteria,
        setSearchInventoryCriteria,
    } = useInventoryContext();

    const {
        pageNation: productPageNation,
        searchModalIsOpen: productSearchModalIsOpen,
        setSearchModalIsOpen: setProductSearchModalIsOpen,
        products,
        fetchProducts,
    } = useProductItemContext();

    const handleProductSelect = (product: ProductType) => {
        setSearchInventoryCriteria({
            ...searchInventoryCriteria,
            productCode: product.productCode,
            productName: product.productFormalName
        });
        setProductSearchModalIsOpen(false);
    };

    const handleCloseSearchModal = () => {
        setProductSearchModalIsOpen(false);
    };

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
                handleSelect={handleProductSelect}
                handleClose={handleCloseSearchModal}
                pageNation={productPageNation}
                fetchProducts={fetchProducts.load}
            />
        </Modal>
    );

    return (
        <>
            {type === "search" ? productSearchModalView() : null}
        </>
    );
};