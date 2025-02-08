import React from "react";
import {useProductItemContext} from "../../../../providers/master/product/ProductItem.tsx";
import {useVendorContext} from "../../../../providers/master/partner/Vendor.tsx";
import Modal from "react-modal";
import {VendorCollectionSelectView} from "../../../../views/master/partner/vendor/VendorSelect.tsx";
import {VendorType} from "../../../../models/master/partner";

type SelectModalProps = {
    type: "edit" | "search";
};

export const VendorSelectModal: React.FC<SelectModalProps> = ({ type }) => {
    const {
        setError,
        newProduct,
        setNewProduct,
        searchProductCriteria,
        setSearchProductCriteria,
    } = useProductItemContext();

    const {
        modalIsOpen: vendorModalIsOpen,
        setModalIsOpen: setVendorModalIsOpen,
        setEditId: setVendorEditId,
        searchModalIsOpen: vendorSearchModalIsOpen,
        setSearchModalIsOpen: setVendorSearchModalIsOpen,
        vendors,
        fetchVendors,
        pageNation: vendorPageNation,
    } = useVendorContext();

    const handleCloseVendorModal = () => {
        setError(""); // エラーメッセージをリセット
        setVendorModalIsOpen(false); // モーダルを閉じる
        setVendorEditId(null); // 編集中の仕入先IDをリセット
    };

    const handleCloseVendorSearchModal = () => {
        setError(""); // エラーメッセージをリセット
        setVendorSearchModalIsOpen(false); // モーダルを閉じる
    }

    const vendorEditModalView = () => {
        return (
            <Modal
                isOpen={vendorModalIsOpen}
                onRequestClose={handleCloseVendorModal} // モーダルを閉じる処理
                contentLabel="仕入先情報を入力"
                className="modal"
                overlayClassName="modal-overlay"
                bodyOpenClassName="modal-open"
            >
                {
                    <VendorCollectionSelectView
                        vendors={vendors} // 仕入先リストを渡す
                        handleSelect={(vendor: VendorType) => {
                            setNewProduct({
                                ...newProduct,
                                vendorCode: {
                                    code: vendor.vendorCode.code,
                                    branchNumber: vendor.vendorCode.branchNumber
                                }
                            });
                            setVendorModalIsOpen(false); // モーダルを閉じる
                        }}
                        handleClose={handleCloseVendorModal} // モーダルを閉じる処理
                        pageNation={vendorPageNation} // ページネーション情報
                        fetchVendors={fetchVendors.load} // 仕入先リストを取得する関数
                    />
                }
            </Modal>
        );
    };

    const vendorSearchModalView = () => {
        return (
            <Modal
                isOpen={vendorSearchModalIsOpen}
                onRequestClose={handleCloseVendorSearchModal}
                contentLabel="仕入先情報を入力"
                className="modal"
                overlayClassName="modal-overlay"
                bodyOpenClassName="modal-open"
            >
                {
                    <VendorCollectionSelectView
                        vendors={vendors} // 仕入先情報リストを引数として渡す
                        handleSelect={(vendor) => {
                            setSearchProductCriteria({
                                ...searchProductCriteria,
                                vendorCode: vendor.vendorCode.code.value
                            })
                            setVendorSearchModalIsOpen(false); // モーダルを閉じる
                        }}
                        handleClose={() => setVendorSearchModalIsOpen(false)} // モーダルを閉じる処理
                        pageNation={vendorPageNation} // ページネーション情報
                        fetchVendors={fetchVendors.load} // データを取得する関数
                    />
                }
            </Modal>
        );
    };

    return (
        <>
            {type === "edit" ? vendorEditModalView() : null}
            {type === "search" ? vendorSearchModalView() : null}
        </>
    );
}