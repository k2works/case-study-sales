import React from "react";
import Modal from "react-modal";
import { useProductItemContext } from "../../../providers/master/product/ProductItem.tsx";
import { useSalesOrderContext } from "../../../providers/sales/SalesOrder.tsx";
import { ProductCollectionSelectView } from "../../../views/master/product/item/ProductSelect.tsx";
import {ProductType} from "../../../models/master/product";

export const ProductSelectModal: React.FC = () => {
    const {
        newSalesOrder,
        setNewSalesOrder,
        selectedLineIndex,
        setSelectedLineIndex,
    } = useSalesOrderContext();

    const {
        pageNation: productPageNation,
        modalIsOpen: productModalIsOpen,
        setModalIsOpen: setProductModalIsOpen,
        products,
        fetchProducts,
    } = useProductItemContext();

    const handleProductSelect = (product: ProductType) => {
        if (selectedLineIndex !== null && selectedLineIndex >= 0) {
            const updatedLines = [...newSalesOrder.salesOrderLines];
            updatedLines[selectedLineIndex] = {
                ...updatedLines[selectedLineIndex],
                productCode: product.productCode.value,
                productName: product.productName.productFormalName,
                salesUnitPrice: product.sellingPrice.amount,
            };

            setNewSalesOrder({
                ...newSalesOrder,
                salesOrderLines: updatedLines
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