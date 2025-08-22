import React from "react";
import Modal from "react-modal";
import {useProductItemContext} from "../../../../providers/master/product/ProductItem.tsx";
import {usePurchaseOrderContext} from "../../../../providers/sales/PurchaseOrder.tsx";

export const ProductSelectModal: React.FC = () => {
    const {
        products,
        modalIsOpen,
        setModalIsOpen,
        fetchProducts
    } = useProductItemContext();

    const {
        newPurchaseOrder,
        setNewPurchaseOrder,
        selectedLineIndex
    } = usePurchaseOrderContext();

    React.useEffect(() => {
        if (modalIsOpen && products.length === 0) {
            fetchProducts.load();
        }
    }, [modalIsOpen]);

    const handleProductSelect = (productCode: string, productFormalName: string, sellingPrice: number) => {
        if (selectedLineIndex !== null) {
            const newLines = [...newPurchaseOrder.purchaseOrderLines];
            newLines[selectedLineIndex] = {
                ...newLines[selectedLineIndex],
                productCode: productCode,
                productName: productFormalName,
                purchaseUnitPrice: sellingPrice
            };

            const totalAmount = newLines.reduce((sum, line) => 
                sum + (line.purchaseOrderQuantity * line.purchaseUnitPrice), 0);
            const totalTax = Math.floor(totalAmount * 0.1);

            setNewPurchaseOrder({
                ...newPurchaseOrder,
                purchaseOrderLines: newLines,
                totalPurchaseAmount: totalAmount,
                totalConsumptionTax: totalTax
            });
        }
        setModalIsOpen(false);
    };

    const handleCloseModal = () => {
        setModalIsOpen(false);
    };

    return (
        <Modal
            isOpen={modalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="商品を選択"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <div className="single-view-object-container">
                <div className="single-view-header">
                    <div className="single-view-header-title">
                        <h2>商品選択</h2>
                        <button onClick={handleCloseModal} className="close-button">
                            ✕
                        </button>
                    </div>
                </div>
                <div className="single-view-container">
                    <div className="select-modal-content">
                        <div className="select-modal-list">
                            {products.map((product) => (
                                <div
                                    key={product.productCode}
                                    className="select-modal-item"
                                    onClick={() => handleProductSelect(
                                        product.productCode, 
                                        product.productFormalName,
                                        product.sellingPrice
                                    )}
                                >
                                    <span className="select-modal-item-code">
                                        {product.productCode}
                                    </span>
                                    <span className="select-modal-item-name">
                                        {product.productFormalName}
                                    </span>
                                    <span className="select-modal-item-price">
                                        ¥{product.sellingPrice.toLocaleString()}
                                    </span>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
            </div>
        </Modal>
    );
};