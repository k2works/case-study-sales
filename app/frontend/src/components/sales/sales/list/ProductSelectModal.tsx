import React from "react";
import Modal from "react-modal";
import { useProductItemContext } from "../../../../providers/master/product/ProductItem.tsx";
import { ProductCollectionSelectView } from "../../../../views/master/product/item/ProductSelect.tsx";
import {ProductType} from "../../../../models/master/product";
import {useSalesContext} from "../../../../providers/sales/Sales.tsx";

export const ProductSelectModal: React.FC = () => {
    const {
        newSales,
        setNewSales,
        selectedLineIndex,
        setSelectedLineIndex,
    } = useSalesContext();

    const {
        pageNation: productPageNation,
        modalIsOpen: productModalIsOpen,
        setModalIsOpen: setProductModalIsOpen,
        products,
        fetchProducts,
    } = useProductItemContext();

    const handleProductSelect = (product: ProductType) => {
        if (selectedLineIndex !== null && selectedLineIndex >= 0) {
            const updatedLines = [...newSales.salesLines];
            updatedLines[selectedLineIndex] = {
                ...updatedLines[selectedLineIndex],
                productCode: product.productCode,
                productName: product.productFormalName,
                salesUnitPrice: product.sellingPrice,
            };

            setNewSales({
                ...newSales,
                salesLines: updatedLines,
            });
        }
        setProductModalIsOpen(false);
        setSelectedLineIndex(null);
    };

    return (
        <Modal
            isOpen={productModalIsOpen}
            onRequestClose={() => setProductModalIsOpen(false)}
            contentLabel="商品情報を選択"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <ProductCollectionSelectView
                products={products}
                handleSelect={handleProductSelect}
                handleClose={() => setProductModalIsOpen(false)}
                pageNation={productPageNation}
                fetchProducts={fetchProducts.load}
            />
        </Modal>
    );
};
