import React from "react";
import Modal from "react-modal";
import {useVendorContext} from "../../../../providers/master/partner/Vendor.tsx";
import {VendorSearchView} from "../../../../views/master/partner/vendor/VendorSearch.tsx";
import {showErrorMessage} from "../../../application/utils.ts";

export const VendorSearchModal: React.FC = () => {
    const {
        setLoading,
        setMessage,
        setError,
        setPageNation,
        setCriteria,
        searchModalIsOpen,
        setSearchModalIsOpen,
        setVendors,
        searchVendorCriteria,
        setSearchVendorCriteria,
        vendorService
    } = useVendorContext();

    const handleCloseSearchModal = () => {
        setSearchModalIsOpen(false);
    };

    return (
        <Modal
            isOpen={searchModalIsOpen}
            onRequestClose={handleCloseSearchModal}
            contentLabel="検索条件を入力"
            className="modal"
            overlayClassName="modal-overlay"
            bodyOpenClassName="modal-open"
        >
            <VendorSearchView
                criteria={searchVendorCriteria}
                setCondition={setSearchVendorCriteria}
                handleSelect={async () => {
                    if (!searchVendorCriteria) return;
                    setLoading(true);
                    try {
                        const result = await vendorService.search(searchVendorCriteria);
                        setVendors(result ? result.list : []);
                        if (result.list.length === 0) {
                            showErrorMessage("検索結果は0件です", setError);
                        } else {
                            setCriteria(searchVendorCriteria);
                            setPageNation(result);
                            setMessage("");
                            setError("");
                        }
                    } catch (error: unknown) {
                        const errorMessage = error instanceof Error ? error.message : String(error);
                        showErrorMessage(`検索に失敗しました: ${errorMessage}`, setError);
                    } finally {
                        setLoading(false);
                    }
                }}
                handleClose={handleCloseSearchModal}
            />
        </Modal>
    );
}
