import React from "react";
import Modal from "react-modal";
import { VendorCollectionSelectView } from "../../../../views/master/partner/vendor/VendorSelect.tsx";
import { useVendorContext } from "../../../../providers/master/partner/Vendor.tsx";
import { usePurchaseReceiptContext } from "../../../../providers/procurement/Purchase.tsx";
import { VendorType } from "../../../../models/master/partner";

type VendorSelectModalProps = {
    type: "edit" | "search";
};

export const VendorSelectModal: React.FC<VendorSelectModalProps> = ({ type }) => {
    const {
        newPurchase,
        setNewPurchase,
        searchPurchaseCriteria,
        setSearchPurchaseCriteria,
    } = usePurchaseReceiptContext();

    const {
        modalIsOpen: vendorModalIsOpen,
        setModalIsOpen: setVendorModalIsOpen,
        searchModalIsOpen: vendorSearchModalIsOpen,
        setSearchModalIsOpen: setVendorSearchModalIsOpen,
        vendors,
        fetchVendors,
        pageNation: vendorPageNation,
    } = useVendorContext();

    // 編集モーダルを閉じる
    const handleCloseEditModal = () => {
        setVendorModalIsOpen(false);
    };

    // 検索モーダルを閉じる
    const handleCloseSearchModal = () => {
        setVendorSearchModalIsOpen(false);
    };

    // 編集モード用モーダルビュー
    const vendorEditModalView = () => (
        <Modal
            isOpen={vendorModalIsOpen}
            onRequestClose={handleCloseEditModal}
            contentLabel="仕入先情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <VendorCollectionSelectView
                vendors={vendors}
                handleSelect={(vendor: VendorType) => {
                    setNewPurchase({
                        ...newPurchase,
                        supplierCode: vendor.vendorCode,
                        supplierBranchNumber: vendor.vendorBranchNumber
                    });
                    setVendorModalIsOpen(false);
                }}
                handleClose={handleCloseEditModal}
                pageNation={vendorPageNation}
                fetchVendors={fetchVendors.load}
            />
        </Modal>
    );

    // 検索モード用モーダルビュー
    const vendorSearchModalView = () => (
        <Modal
            isOpen={vendorSearchModalIsOpen}
            onRequestClose={handleCloseSearchModal}
            contentLabel="仕入先情報を検索"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <VendorCollectionSelectView
                vendors={vendors}
                handleSelect={(vendor: VendorType) => {
                    setSearchPurchaseCriteria({
                        ...searchPurchaseCriteria,
                        supplierCode: vendor.vendorCode
                    });
                    setVendorSearchModalIsOpen(false);
                }}
                handleClose={handleCloseSearchModal}
                pageNation={vendorPageNation}
                fetchVendors={fetchVendors.load}
            />
        </Modal>
    );

    return (
        <>
            {type === "edit" ? vendorEditModalView() : null}
            {type === "search" ? vendorSearchModalView() : null}
        </>
    );
};
