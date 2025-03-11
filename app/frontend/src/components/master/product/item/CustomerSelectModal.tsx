import React from "react";
import {useProductItemContext} from "../../../../providers/master/product/ProductItem.tsx";
import {useCustomerContext} from "../../../../providers/master/partner/Customer.tsx";
import Modal from "react-modal";
import {CustomerCollectionSelectView} from "../../../../views/master/partner/customer/CustomerSelect.tsx";
import {CustomerType} from "../../../../models/master/partner";

export const CustomerSelectModal: React.FC = () => {
    const {
        setError,
        newProduct,
        setNewProduct,
    } = useProductItemContext();

    const {
        modalIsOpen: customerModalIsOpen,
        setModalIsOpen: setCustomerModalIsOpen,
        setEditId: setCustomerEditId,
        customers,
        fetchCustomers,
        pageNation: customerPageNation,
    } = useCustomerContext();

    const handleCloseModal = () => {
        setError(""); // エラーをリセット
        setCustomerModalIsOpen(false); // モーダルを閉じる
        setCustomerEditId(null); // 編集IDをリセット
    };

    const handleSelectModal = (customer: CustomerType) => {
        const newProducts = newProduct.customerSpecificSellingPrices.filter((e) => e.customerCode !== customer.customerCode);
        newProducts.push({
            productCode: newProduct.productCode,
            customerCode: customer.customerCode,
            sellingPrice: {
                amount: 0,
                currency: "JPY"
            }
        });
        setNewProduct({
            ...newProduct,
            customerSpecificSellingPrices: newProducts
        });
    }

    return (
        <Modal
            isOpen={customerModalIsOpen}
            onRequestClose={handleCloseModal}
            contentLabel="顧客情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            {
                <CustomerCollectionSelectView
                    customers={customers} // 顧客リストを渡す
                    handleSelect={handleSelectModal}
                    handleClose={handleCloseModal}
                    pageNation={customerPageNation}
                    fetchCustomers={fetchCustomers.load}
                />
            }
        </Modal>
    );
}
