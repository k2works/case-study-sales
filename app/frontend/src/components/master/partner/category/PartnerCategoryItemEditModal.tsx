import React from "react";
import {usePartnerCategoryContext} from "../../../../providers/master/partner/PartnerCategory.tsx";
import Modal from "react-modal";
import {usePartnerListContext} from "../../../../providers/master/partner/PartnerList.tsx";
import {
    PartnerCategoryAffiliationCollectionAddListView
} from "../../../../views/master/partner/category/PartnerCategoryAffiliationCollection.tsx";

export const PartnerCategoryItemEditModal: React.FC = () => {
    const {
        setMessage,
        setError,
        categoryItemModalIsOpen,
        setCategoryItemModalIsOpen,
        setCategoryItemEditId,
        newPartnerCategory,
        setNewPartnerCategory,
        newPartnerCategoryItem,
        setNewPartnerCategoryItem,
    } = usePartnerCategoryContext();

    const {
        setModalIsOpen: setPartnerModalIsOpen,
        setIsEditing: setPartnerIsEditing,
    } = usePartnerListContext();

    const handleClosePartnerCategoryItemModal = () => {
        setError("");
        setCategoryItemModalIsOpen(false);
        setCategoryItemEditId(null);
    }

    const handleOpenPartnerModal = () => {
        setMessage("");
        setError("");
        setPartnerIsEditing(true);
        setPartnerModalIsOpen(true);
    };

    return (
        <Modal
            isOpen={categoryItemModalIsOpen}
            onRequestClose={handleClosePartnerCategoryItemModal}
            contentLabel="取引先分類情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >

            <PartnerCategoryAffiliationCollectionAddListView
                partnerCategoryAffiliations={newPartnerCategoryItem.partnerCategoryAffiliations}
                handleAdd={handleOpenPartnerModal}
                handleDelete={(partnerCategoryAffiliation) => {
                    setNewPartnerCategoryItem({
                        ...newPartnerCategoryItem,
                        partnerCategoryAffiliations: newPartnerCategoryItem.partnerCategoryAffiliations.filter((e) => e.partnerCode.value !== partnerCategoryAffiliation.partnerCode.value)
                    });

                    const newPartnerCategoryItems = newPartnerCategory.partnerCategoryItems.filter((e) => e.partnerCategoryItemCode !== newPartnerCategoryItem.partnerCategoryItemCode);
                    newPartnerCategoryItems.push({
                        ...newPartnerCategoryItem,
                        partnerCategoryAffiliations: newPartnerCategoryItem.partnerCategoryAffiliations.filter((e) => e.partnerCode.value !== partnerCategoryAffiliation.partnerCode.value)
                    });
                    setNewPartnerCategory({
                        ...newPartnerCategory,
                        partnerCategoryItems: newPartnerCategoryItems
                    })
                }}
                handleClose={handleClosePartnerCategoryItemModal}
            />
        </Modal>
    );
}
