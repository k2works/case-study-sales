import React from "react";
import {usePartnerGroupContext} from "../../../../providers/master/partner/PartnerGroup.tsx";
import Modal from "react-modal";
import {PartnerGroupSearchView} from "../../../../views/master/partner/group/PartnerGroupSearch.tsx";
import {showErrorMessage} from "../../../application/utils.ts";

export const PartnerGroupSearchModal: React.FC = () => {
    const {
        setLoading,
        setMessage,
        setError,
        setPageNation,
        setCriteria,
        searchModalIsOpen,
        setSearchModalIsOpen,
        setPartnerGroups,
        searchPartnerGroupCriteria,
        setSearchPartnerGroupCriteria,
        partnerGroupService,
    } = usePartnerGroupContext();

    const handleCloseSearchModal = () => {
        setSearchModalIsOpen(false);
    };

    const handleSelectSearchModal = async () => {
        if (!searchPartnerGroupCriteria) return;

        setLoading(true);
        try {
            const result = await partnerGroupService.search(searchPartnerGroupCriteria);
            setPartnerGroups(result ? result.list : []);
            if (result.list.length === 0) {
                showErrorMessage("検索結果は0件です", setError);
            } else {
                setCriteria(searchPartnerGroupCriteria);
                setPageNation(result);
                setMessage("");
                setError("");
            }
        } catch (error: any) {
            showErrorMessage(`検索に失敗しました: ${error?.message}`, setError);
        } finally {
            setLoading(false);
        }
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
            <PartnerGroupSearchView
                criteria={searchPartnerGroupCriteria}
                setCondition={setSearchPartnerGroupCriteria}
                handleSelect={handleSelectSearchModal}
                handleClose={handleCloseSearchModal}
            />
        </Modal>
    );
}
