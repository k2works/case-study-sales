import React from "react";
import {usePartnerCategoryContext} from "../../../../providers/master/partner/PartnerCategory.tsx";
import {usePartnerListContext} from "../../../../providers/master/partner/PartnerList.tsx";
import Modal from "react-modal";
import {PartnerCollectionSelectView} from "../../../../views/master/partner/PartnerSelect.tsx";
import {showErrorMessage} from "../../../application/utils.ts";
import {PartnerType} from "../../../../models/master/partner";

export const PartnerSelectModal: React.FC = () => {
    const {
        setError,
        newPartnerCategory,
        setNewPartnerCategory,
        newPartnerCategoryItem,
        setNewPartnerCategoryItem,
    } = usePartnerCategoryContext();

    const {
        pageNation: partnerPageNation,
        modalIsOpen: partnerModalIsOpen,
        setModalIsOpen: setPartnerModalIsOpen,
        setEditId: setPartnerEditId,
        partners,
        fetchPartners,
    } = usePartnerListContext();

    const handleClosePartnerModal = () => {
        setError("");
        setPartnerModalIsOpen(false);
        setPartnerEditId(null);
    };

    const handlePartnerCollectionSelect = (partner: PartnerType) => {
        const newPartnerCategoryAffiliations = newPartnerCategoryItem.partnerCategoryAffiliations;
        if (newPartnerCategoryAffiliations.some((e) => e.partnerCode === partner.partnerCode)) {
            showErrorMessage("既に追加されている取引先です", setError);
            return;
        }
        newPartnerCategoryAffiliations.push({
            partnerCategoryTypeCode: newPartnerCategory.partnerCategoryTypeCode,
            partnerCategoryItemCode: newPartnerCategoryItem.partnerCategoryItemCode,
            partnerCode: partner.partnerCode,
        })
        setNewPartnerCategoryItem({
            ...newPartnerCategoryItem,
            partnerCategoryAffiliations: newPartnerCategoryAffiliations
        });
        setNewPartnerCategoryItem({
            ...newPartnerCategoryItem,
            partnerCategoryAffiliations: newPartnerCategoryAffiliations
        });
        const newPartnerCategoryItems = newPartnerCategory.partnerCategoryItems.filter((e) => e.partnerCategoryItemCode !== newPartnerCategoryItem.partnerCategoryItemCode);
        newPartnerCategoryItems.push({
            ...newPartnerCategoryItem,
            partnerCategoryAffiliations: newPartnerCategoryAffiliations
        });
        setNewPartnerCategory({
            ...newPartnerCategory,
            partnerCategoryItems: newPartnerCategoryItems
        });
        handleClosePartnerModal();
    }


    return (
        <Modal
            isOpen={partnerModalIsOpen}
            onRequestClose={handleClosePartnerModal}
            contentLabel="取引先情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            {
                <PartnerCollectionSelectView
                    partners={partners}
                    handleSelect={handlePartnerCollectionSelect}
                    handleClose={handleClosePartnerModal}
                    pageNation={partnerPageNation}
                    fetchPartners={fetchPartners.load}
                />
            }
        </Modal>
    )
}
