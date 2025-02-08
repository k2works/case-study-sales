import React from "react";
import {usePartnerCategoryContext} from "../../../../providers/PartnerCategory.tsx";
import Modal from "react-modal";
import {PartnerCategorySearchView} from "../../../../views/master/partner/category/PartnerCategorySearch.tsx";
import {showErrorMessage} from "../../../application/utils.ts";

export const PartnerCategorySearchModal: React.FC = () => {
    const {
        setLoading,
        setMessage,
        setError,
        setPageNation,
        setCriteria,
        searchModalIsOpen,
        setSearchModalIsOpen,
        setPartnerCategories,
        searchPartnerCategoryCriteria,
        setSearchPartnerCategoryCriteria,
        partnerCategoryService,
    } = usePartnerCategoryContext();

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
            <PartnerCategorySearchView
                criteria={searchPartnerCategoryCriteria}
                setCondition={setSearchPartnerCategoryCriteria}
                handleSelect={async () => {
                    if (!searchPartnerCategoryCriteria) {
                        return;
                    }
                    setLoading(true);
                    try {
                        const result = await partnerCategoryService.search(searchPartnerCategoryCriteria);
                        setPartnerCategories(result ? result.list : []);
                        if (result.list.length === 0) {
                            showErrorMessage("検索結果は0件です", setError);
                        } else {
                            setCriteria(searchPartnerCategoryCriteria);
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
        </Modal>
    );
}
