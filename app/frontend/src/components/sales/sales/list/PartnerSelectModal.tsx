import React from "react";
import Modal from "react-modal";
import {useSalesContext} from "../../../../providers/sales/Sales.tsx";
import {PartnerCollectionSelectView} from "../../../../views/master/partner/PartnerSelect.tsx";
import {PartnerType} from "../../../../models/master/partner";
import {usePartnerListContext} from "../../../../providers/master/partner/PartnerList.tsx";

type CustomerSelectModalProps = {
    type: "edit" | "search";
};

export const PartnerSelectModal: React.FC<CustomerSelectModalProps> = ({ type }) => {
    const {
        newSales,
        setNewSales,
        searchSalesCriteria,
        setSearchSalesCriteria,
    } = useSalesContext();

    const {
        pageNation: partnerPageNation,
        modalIsOpen: partnerModalIsOpen,
        setModalIsOpen: setCustomerModalIsOpen,
        searchModalIsOpen: partnerSearchModalIsOpen,
        setSearchModalIsOpen: setCustomerSearchModalIsOpen,
        partners,
        fetchPartners,
    } = usePartnerListContext();

    // 編集モーダルを閉じる
    const handleCloseEditModal = () => {
        setCustomerModalIsOpen(false);
    };

    // 検索モーダルを閉じる
    const handleCloseSearchModal = () => {
        setCustomerSearchModalIsOpen(false);
    };

    // 編集モード用モーダルビュー
    const partnerEditModalView = () => (
        <Modal
            isOpen={partnerModalIsOpen}
            onRequestClose={handleCloseEditModal}
            contentLabel="取引先情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <PartnerCollectionSelectView
                partners={partners}
                handleSelect={(partner: PartnerType) => {
                    setNewSales({
                        ...newSales,
                        customerCode: partner.partnerCode,
                    });
                    setCustomerModalIsOpen(false);
                }}
                handleClose={handleCloseEditModal}
                pageNation={partnerPageNation}
                fetchPartners={fetchPartners.load}
            />
        </Modal>
    );

    // 検索モード用モーダルビュー
    const partnerSearchModalView = () => (
        <Modal
            isOpen={partnerSearchModalIsOpen}
            onRequestClose={handleCloseSearchModal}
            contentLabel="取引先情報を検索"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <PartnerCollectionSelectView
                partners={partners}
                handleSelect={() => {
                    setSearchSalesCriteria({
                        ...searchSalesCriteria,
                    });
                    setCustomerSearchModalIsOpen(false);
                }}
                handleClose={handleCloseSearchModal}
                pageNation={partnerPageNation}
                fetchPartners={fetchPartners.load}
            />
        </Modal>
    );

    return (
        <>
            {type === "edit" ? partnerEditModalView() : null}
            {type === "search" ? partnerSearchModalView() : null}
        </>
    );
};
