import React from "react";
import {usePartnerListContext} from "../../../../providers/master/partner/PartnerList.tsx";
import {usePartnerGroupContext} from "../../../../providers/master/partner/PartnerGroup.tsx";
import Modal from "react-modal";
import {PartnerSearchView} from "../../../../views/master/partner/PartnerSearch.tsx";
import {showErrorMessage} from "../../../application/utils.ts";
import {PartnerGroupSelectView} from "../../../../views/master/partner/group/PartnerGroupSelect.tsx";
import {PartnerGroupSelectModal} from "./PartnerGroupSelectModal.tsx";

export const PartnerListSearchModal: React.FC = () => {
    const {
        setLoading,
        setMessage,
        setError,
        setPageNation,
        setCriteria,
        searchModalIsOpen,
        setSearchModalIsOpen,
        setPartners,
        searchPartnerCriteria,
        setSearchPartnerCriteria,
        partnerService
    } = usePartnerListContext();

    const {
        setModalIsOpen: setPartnerGroupModalIsOpen,
    } = usePartnerGroupContext();

    const handleCloseSearchModal = () => {
        setSearchModalIsOpen(false);
    };

    return (
        <Modal
            isOpen={searchModalIsOpen}
            onRequestClose={handleCloseSearchModal}
            contentLabel="検索情報を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            {
                <>
                    <PartnerSearchView
                        criteria={searchPartnerCriteria}
                        setCondition={setSearchPartnerCriteria}
                        handleSelect={async () => {
                            if (!searchPartnerCriteria) return;
                            setLoading(true);
                            try {
                                const result = await partnerService.search(searchPartnerCriteria);
                                setPartners(result ? result.list : []);
                                if (result.list.length === 0) {
                                    showErrorMessage("検索結果は0件です", setError);
                                } else {
                                    setCriteria(searchPartnerCriteria);
                                    setPageNation(result);
                                    setMessage("");
                                    setError("");
                                }
                            } catch (error: any) {
                                showErrorMessage(`検索に失敗しました: ${error?.message}`, setError);
                            } finally {
                                setLoading(false);
                            }
                        }}
                        handleClose={handleCloseSearchModal}
                    />

                    <PartnerGroupSelectModal type={"search"}/>
                    <PartnerGroupSelectView
                        handleSelect={() => setPartnerGroupModalIsOpen(true)}
                    />
                </>
            }
        </Modal>
    );
}
