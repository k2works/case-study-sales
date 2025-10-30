import React from "react";
import Modal from "react-modal";
import { ProductCollectionSelectView } from "../../../../views/master/product/item/ProductSelect.tsx";
import { useProductItemContext } from "../../../../providers/master/product/ProductItem.tsx";
import { usePurchaseReceiptContext } from "../../../../providers/procurement/Purchase.tsx";
import { ProductType } from "../../../../models/master/product";

export const ProductSelectModal: React.FC = () => {
    const {
        newPurchase,
        setNewPurchase,
        selectedLineIndex
    } = usePurchaseReceiptContext();

    const {
        products,
        modalIsOpen: productModalIsOpen,
        setModalIsOpen: setProductModalIsOpen,
        fetchProducts,
        pageNation: productPageNation,
    } = useProductItemContext();

    // モーダルを閉じる
    const handleCloseModal = () => {
        setProductModalIsOpen(false);
    };

    // 商品選択時の処理
    const handleProductSelect = (product: ProductType) => {
        if (selectedLineIndex !== null) {
            const newLines = [...newPurchase.purchaseLines];
            newLines[selectedLineIndex] = {
                ...newLines[selectedLineIndex],
                productCode: product.productCode,
                productName: product.productFormalName,
                purchaseUnitPrice: product.sellingPrice
            };

            const totalAmount = newLines.reduce((sum, line) =>
                sum + (line.purchaseQuantity * line.purchaseUnitPrice), 0);
            const totalTax = Math.floor(totalAmount * 0.1);

            setNewPurchase({
                ...newPurchase,
                purchaseLines: newLines,
                totalPurchaseAmount: totalAmount,
                totalConsumptionTax: totalTax
            });
        }
        setProductModalIsOpen(false);
    };

    return (
        <Modal
            isOpen={productModalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="商品情報を選択"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <ProductCollectionSelectView
                products={products}
                handleSelect={handleProductSelect}
                handleClose={handleCloseModal}
                pageNation={productPageNation}
                fetchProducts={fetchProducts.load}
            />
        </Modal>
    );
};
