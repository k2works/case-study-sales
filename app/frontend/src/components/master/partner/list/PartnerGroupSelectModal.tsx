import React from "react";
import {usePartnerListContext} from "../../../../providers/master/partner/PartnerList.tsx";
import {usePartnerGroupContext} from "../../../../providers/master/partner/PartnerGroup.tsx";
import Modal from "react-modal";
import {PartnerGroupCollectionSelectView} from "../../../../views/master/partner/group/PartnerGroupSelect.tsx";

type SelectModalProps = {
    type: "edit" | "search";
};

export const PartnerGroupSelectModal: React.FC<SelectModalProps> = ({ type }) => {
    const {
        newPartner,
        setNewPartner,
        searchPartnerCriteria,
        setSearchPartnerCriteria,
    } = usePartnerListContext();

    const {
        pageNation: partnerGroupPageNation,
        modalIsOpen: partnerGroupModalIsOpen,
        setModalIsOpen: setPartnerGroupModalIsOpen,
        partnerGroups,
        fetchPartnerGroups,
    } = usePartnerGroupContext();

    const partnerGroupModalView = () => {
        return (
            <Modal
                isOpen={partnerGroupModalIsOpen}
                onRequestClose={() => setPartnerGroupModalIsOpen(false)}
                contentLabel="取引先グループ情報を入力"
                className="modal"
                overlayClassName="modal-overlay"
                bodyOpenClassName="modal-open"
            >
                {
                    <PartnerGroupCollectionSelectView
                        partnerGroups={partnerGroups}
                        handleSelect={(partnerGroup) => {
                            setNewPartner({
                                ...newPartner,
                                partnerGroupCode: partnerGroup.partnerGroupCode
                            })
                            setPartnerGroupModalIsOpen(false);
                        }}
                        handleClose={() => setPartnerGroupModalIsOpen(false)}
                        pageNation={partnerGroupPageNation}
                        fetchPartnerGroups={fetchPartnerGroups.load}
                    />
                }
            </Modal>
        );
    };

    const partnerGroupSearchModalView = () => {
        return (
            <Modal
                isOpen={partnerGroupModalIsOpen}
                onRequestClose={() => setPartnerGroupModalIsOpen(false)}
                contentLabel="取引先グループ情報を検索"
                className="modal"
                overlayClassName="modal-overlay"
                bodyOpenClassName="modal-open"
            >
                {
                    <PartnerGroupCollectionSelectView
                        partnerGroups={partnerGroups}
                        handleSelect={(partnerGroup) => {
                            setSearchPartnerCriteria({
                                ...searchPartnerCriteria,
                                partnerGroupCode: partnerGroup.partnerGroupCode
                            });
                            setPartnerGroupModalIsOpen(false);
                        }}
                        handleClose={() => setPartnerGroupModalIsOpen(false)}
                        pageNation={partnerGroupPageNation}
                        fetchPartnerGroups={fetchPartnerGroups.load}
                    />
                }
            </Modal>
        );
    };

    return (
        <>
            {type === "edit" ? partnerGroupModalView() : null}
            {type === "search" ? partnerGroupSearchModalView() : null}
        </>
    );
}
