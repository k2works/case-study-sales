import React from "react";
import {useProductItemContext} from "../../../../providers/ProductItem.tsx";
import {useProductBomContext} from "../../../../providers/ProductBom.tsx";
import Modal from "react-modal";
import {ProductCollectionSelectView} from "../../../../views/master/product/item/ProductSelect.tsx";
import {ProductType} from "../../../../models/master/product";

export const ProductItemBomModal: React.FC = () => {
    const {
        setError,
        newProduct,
        setNewProduct,
    } = useProductItemContext();

    const {
        bomPageNation,
        bomModalIsOpen,
        setBomModalIsOpen,
        boms,
        fetchBoms,
        setBomEditId
    } = useProductBomContext();

    const handleCloseBomModal = () => {
        setError("");
        setBomModalIsOpen(false);
        setBomEditId(null);
    }

    const handleSelectBomModal = (bom: ProductType) => {
        const newProducts = newProduct.boms.filter((e) => e.productCode.value !== bom.productCode.value);
        newProducts.push({
            productCode: newProduct.productCode,
            componentCode: bom.productCode,
            componentQuantity: {
                amount: 1,
                unit: "個"
            }
        });
        setNewProduct({
            ...newProduct,
            boms: newProducts
        });
    }

    return (
        <Modal
            isOpen={bomModalIsOpen}
            onRequestClose={handleCloseBomModal}
            contentLabel="部品情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            {
                <ProductCollectionSelectView
                    products={boms}
                    handleSelect={handleSelectBomModal}
                    handleClose={handleCloseBomModal}
                    pageNation={bomPageNation}
                    fetchProducts={fetchBoms.load}
                />
            }
        </Modal>
    )
}
