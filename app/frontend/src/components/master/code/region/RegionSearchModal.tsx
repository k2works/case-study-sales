import React from "react";
import {useRegionContext} from "../../../../providers/master/code/Region.tsx";
import Modal from "react-modal";
import {RegionSearchSingleView} from "../../../../views/master/code/region/RegionSearch.tsx";
import {showErrorMessage} from "../../../application/utils.ts";

export const RegionSearchModal: React.FC = () => {
    const {
        setLoading,
        setMessage,
        setError,
        setPageNation,
        setCriteria,
        searchModalIsOpen,
        setSearchModalIsOpen,
        setRegions,
        searchRegionCriteria,
        setSearchRegionCriteria,
        regionService,
    } = useRegionContext();

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
            <RegionSearchSingleView
                criteria={searchRegionCriteria}
                setCondition={setSearchRegionCriteria}
                handleSelect={async () => {
                    if (!searchRegionCriteria) {
                        return;
                    }
                    setLoading(true);
                    try {
                        const result = await regionService.search(searchRegionCriteria);
                        setRegions(result ? result.list : []);
                        if (result.list.length === 0) {
                            showErrorMessage("検索結果は0件です", setError);
                        } else {
                            setCriteria(searchRegionCriteria);
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
