import React from "react";
import { useProductItemContext } from "../../../../providers/master/product/ProductItem.tsx";
import Modal from "react-modal";
import {useCustomerContext} from "../../../../providers/master/partner/Customer.tsx";
import {CustomerType} from "../../../../models/master/partner";
import {CustomerCollectionSelectView} from "../../../../views/master/partner/customer/CustomerSelect.tsx";

type SelectModalProps = {
    type: "edit" | "search";
};

// 商品カテゴリ選択モーダル
export const CustomerSelectModal: React.FC<SelectModalProps> = ({ type }) => {
    const {
        setError,
        newProduct,
        setNewProduct,
        searchProductCriteria,
        setSearchProductCriteria,
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

    const customerModalView = () => {
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
                        handleSelect={(customer: CustomerType) => {
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
                            setCustomerModalIsOpen(false); // モーダルを閉じる
                        }}
                        handleClose={handleCloseModal}
                        pageNation={customerPageNation}
                        fetchCustomers={fetchCustomers.load}
                    />
                }
            </Modal>
        );
    }

    const customerSearchModalView = () => {
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
                        handleSelect={() => {
                            setSearchProductCriteria({
                                ...searchProductCriteria
                            });
                        }}
                        handleClose={handleCloseModal}
                        pageNation={customerPageNation}
                        fetchCustomers={fetchCustomers.load}
                    />
                }
            </Modal>
        );
    }


    // モーダルのタイプに応じて表示内容を切り替え
    return (
        <>
            {type === "edit" ? customerModalView() : null}
            {type === "search" ? customerSearchModalView() : null}
        </>
    );
};

